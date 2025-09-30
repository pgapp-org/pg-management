package com.pgapp.service;

import com.pgapp.converter.DailyBookingConverter;
import com.pgapp.entity.*;
import com.pgapp.repository.*;
import com.pgapp.request.tenant.DailyApproveRequestDTO;
import com.pgapp.request.tenant.DailyBookingRequestDTO;
import com.pgapp.response.BedResponse;
import com.pgapp.response.RoomResponse;
import com.pgapp.response.tenant.DailyBookingResponseDTO;
import com.pgapp.response.tenant.DailyGuestAllocationDTO;
import com.pgapp.response.tenant.DailyGuestDTO;
import com.pgapp.response.tenant.TenantResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DailyBookingService {

    private final DailyBookingRepository bookingRepo;
    private final RoomRepository roomRepo;
    private final BedRepository bedRepo;
    private final DailyBookingGuestRepository guestRepo;
    private final TenantRepository tenantRepo;
    private final PGRepository pgRepo;

    public DailyBookingService(
            DailyBookingRepository bookingRepo,
            RoomRepository roomRepo,
            BedRepository bedRepo,
            DailyBookingGuestRepository guestRepo,
            TenantRepository tenantRepo,
            PGRepository pgRepo
    ) {
        this.bookingRepo = bookingRepo;
        this.roomRepo = roomRepo;
        this.bedRepo = bedRepo;
        this.guestRepo = guestRepo;
        this.tenantRepo = tenantRepo;
        this.pgRepo = pgRepo;
    }

    @Transactional
    public DailyBookingResponseDTO createBooking(DailyBookingRequestDTO request) {
        DailyBookings booking = new DailyBookings();

        Tenant tenant = tenantRepo.findById(request.getTenantId())
                .orElseThrow(() -> new RuntimeException("Tenant not found"));
        PG pg = pgRepo.findById(request.getPgId())
                .orElseThrow(() -> new RuntimeException("PG not found"));

        booking.setTenant(tenant);
        booking.setPg(pg);
        booking.setStatus(ApplicationStatus.PENDING);
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());

        if (request.getGuests() != null) {
            List<DailyBookingGuest> guestEntities = new ArrayList<>();
            for (DailyGuestDTO guestDTO : request.getGuests()) {
                DailyBookingGuest guest = new DailyBookingGuest();
                guest.setName(guestDTO.getName());
                guest.setAge(guestDTO.getAge());
                guest.setGender(guestDTO.getGender());
                guest.setBooking(booking);
                guestEntities.add(guest);
            }
            booking.setGuests(guestEntities);
        }

        bookingRepo.save(booking);
        return DailyBookingConverter.toDTO(booking);
    }

    @Transactional
    public DailyBookingResponseDTO approveBooking(Long bookingId, DailyApproveRequestDTO req) {
        DailyBookings booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        LocalDate checkIn = booking.getCheckInDate();
        LocalDate checkOut = booking.getCheckOutDate();

        // --- Validation loop ---
        for (DailyGuestAllocationDTO alloc : req.getAllocations()) {
            DailyBookingGuest guest = guestRepo.findById(alloc.getGuestId())
                    .orElseThrow(() -> new RuntimeException("Guest not found"));

            if (!guest.getBooking().getId().equals(bookingId)) {
                throw new RuntimeException("Guest does not belong to this booking");
            }

            Bed bed = bedRepo.findById(alloc.getBedId())
                    .orElseThrow(() -> new RuntimeException("Bed not found"));

            // Check for conflicting guests
            if (!guestRepo.findConflictingGuestsForBed(bed.getId(), checkIn, checkOut).isEmpty()) {
                throw new RuntimeException("Bed " + bed.getId() + " already booked in requested dates");
            }
        }

        // --- Assignment loop ---
        for (DailyGuestAllocationDTO alloc : req.getAllocations()) {
            DailyBookingGuest guest = guestRepo.findById(alloc.getGuestId())
                    .orElseThrow(() -> new RuntimeException("Guest not found"));

            Room room = roomRepo.findById(alloc.getRoomId())
                    .orElseThrow(() -> new RuntimeException("Room not found"));
            Bed bed = bedRepo.findById(alloc.getBedId())
                    .orElseThrow(() -> new RuntimeException("Bed not found"));

            guest.setRoom(room);
            guest.setBed(bed);
            guestRepo.save(guest);

            bed.setShortTermOccupied(true);
            bedRepo.save(bed);
        }

        booking.setStatus(ApplicationStatus.APPROVED);
        return DailyBookingConverter.toDTO(bookingRepo.save(booking));
    }

    public DailyBookingResponseDTO rejectBooking(Long bookingId) {
        DailyBookings booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(ApplicationStatus.REJECTED);
        return DailyBookingConverter.toDTO(bookingRepo.save(booking));
    }

    public List<DailyBookingResponseDTO> getAllBookings() {
        return bookingRepo.findAll().stream()
                .map(DailyBookingConverter::toDTO)
                .toList();
    }

    public List<DailyBookingResponseDTO> getPendingBookings() {
        return bookingRepo.findByStatus(ApplicationStatus.PENDING).stream()
                .map(DailyBookingConverter::toDTO)
                .toList();
    }

    // DailyBookingService.java

    public RoomResponse getRoomWithBeds(Long roomId) {
        Room room = roomRepo.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        RoomResponse roomResp = new RoomResponse();
        roomResp.setId(room.getId());
        roomResp.setRoomNumber(room.getRoomNumber());
        roomResp.setCapacity(room.getCapacity());
        roomResp.setPrice(room.getPrice());
        roomResp.setDoorPosition(room.getDoorPosition());
        roomResp.setRoom360ViewUrl(room.getRoom360ViewUrl());

        List<Long> bedIds = new ArrayList<>();
        List<BedResponse> bedResponses = new ArrayList<>();

        for (Bed bed : room.getBeds()) {
            bedIds.add(bed.getId());

            BedResponse bedResp = new BedResponse();
            bedResp.setId(bed.getId());
            bedResp.setBedNumber(bed.getBedNumber());
            bedResp.setOccupied(bed.isOccupied());
            bedResp.setShortTermOccupied(bed.isShortTermOccupied());

            // Permanent tenant
            if (bed.getTenant() != null) {
                TenantResponse tenantResp = new TenantResponse();
                tenantResp.setId(bed.getTenant().getId());
                tenantResp.setName(bed.getTenant().getName());
                tenantResp.setEmail(bed.getTenant().getEmail());
                tenantResp.setPhone(bed.getTenant().getPhone());
                bedResp.setTenant(tenantResp);
            }

            // Short-term guest mapping
            List<DailyBookingGuest> shortTermGuests = guestRepo.findShortTermGuestsForBed(
                    bed.getId(), LocalDate.now()
            );

            if (!shortTermGuests.isEmpty()) {
                // pick the first guest for now
                DailyBookingGuest guest = shortTermGuests.get(0);
                TenantResponse shortTermResp = new TenantResponse();
                shortTermResp.setId(guest.getBooking().getTenant().getId());
                shortTermResp.setName(guest.getBooking().getTenant().getName());
                shortTermResp.setEmail(guest.getBooking().getTenant().getEmail());
                shortTermResp.setPhone(guest.getBooking().getTenant().getPhone());

                bedResp.setShortTermGuest(shortTermResp);
                bedResp.setShortTermOccupied(true);
                bedResp.setShortTermUntil(guest.getBooking().getCheckOutDate());
            }

            bedResponses.add(bedResp);
        }

        roomResp.setBedIds(bedIds);
        roomResp.setBeds(bedResponses);

        return roomResp;
    }


}
