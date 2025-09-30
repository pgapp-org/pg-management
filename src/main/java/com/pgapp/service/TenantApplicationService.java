package com.pgapp.service;

import com.pgapp.converter.tenant.TenantApplicationConverter;
import com.pgapp.entity.*;
import com.pgapp.repository.*;
import com.pgapp.request.tenant.TenantApplicationRequest;
import com.pgapp.response.tenant.TenantApplicationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TenantApplicationService {

    private final TenantApplicationRepository applicationRepository;
    private final TenantRepository tenantRepository;
    private final PGRepository pgRepository;
    private final RoomRepository roomRepository;
    private final FloorRepository floorRepository;
    private final DailyBookingGuestRepository dailyBookingGuestRepository;


    // ✅ Apply for a PG room
    @Transactional
    public TenantApplicationResponse applyForRoom(TenantApplicationRequest request) {
        Tenant tenant = tenantRepository.findById(request.getTenantId())
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        PG pg = pgRepository.findByName(request.getPgName())
                .orElseThrow(() -> new RuntimeException("PG not found"));

        Floor floor = floorRepository.findByPgIdAndFloorNumber(pg.getId(), request.getFloorNumber())
                .orElseThrow(() -> new RuntimeException("Floor not found"));

        Room room = roomRepository.findByFloorIdAndRoomNumber(floor.getId(), request.getRoomNumber())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        TenantApplication application = TenantApplicationConverter.fromRequest(request, tenant, pg, room);
        TenantApplication saved = applicationRepository.save(application);

        return TenantApplicationConverter.toResponse(saved);
    }

    // ✅ Get all applications for a PG
    @Transactional(readOnly = true)
    public List<TenantApplicationResponse> getApplicationsForPG(Long pgId) {
        return applicationRepository.findByPgId(pgId).stream()
                .map(TenantApplicationConverter::toResponse)
                .collect(Collectors.toList());
    }

    // ✅ Get all applications for a Tenant
    @Transactional(readOnly = true)
    public List<TenantApplicationResponse> getApplicationsForTenant(Long tenantId) {
        return applicationRepository.findByTenantId(tenantId).stream()
                .map(TenantApplicationConverter::toResponse)
                .collect(Collectors.toList());
    }

    // ✅ Update application status (APPROVED / REJECTED / PENDING)
    @Transactional
    public TenantApplicationResponse updateApplicationStatus(Long applicationId, ApplicationStatus status) {
        TenantApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        Room room = application.getRoom();
        Tenant tenant = application.getTenant();

        // Assign bed if approved
        if (status == ApplicationStatus.APPROVED) {
            int bedNum = Integer.parseInt(application.getBedNumber());
            Bed bed = room.getBeds().stream()
                    .filter(b -> b.getBedNumber() == bedNum)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Bed not found in this room"));

            // Check for short-term conflict
            // Check for short-term conflicts
            // Check for short-term conflicts
            List<DailyBookingGuest> shortTermGuests = dailyBookingGuestRepository.findByBedId(bed.getId());
            for (DailyBookingGuest guest : shortTermGuests) {
                DailyBookings booking = guest.getBooking();  // now available
                if (booking != null && booking.getCheckOutDate() != null &&
                        !booking.getCheckOutDate().isBefore(LocalDate.now())) {
                    throw new RuntimeException(
                            "Bed is reserved for short-term until " + booking.getCheckOutDate()
                    );
                }
            }




            if (bed.isOccupied()) {
                throw new RuntimeException("This bed is already occupied");
            }

            // Assign tenant
            tenant.setPg(application.getPg());
            tenant.setRoom(room);
            tenant.setBedNumber(application.getBedNumber());
            bed.setOccupied(true);
            bed.setTenant(tenant);

            room.setOccupiedBeds(room.getOccupiedBeds() + 1);
            roomRepository.save(room);
            tenantRepository.save(tenant);
        }

        application.setStatus(status);
        TenantApplication saved = applicationRepository.save(application);
        return TenantApplicationConverter.toResponse(saved);
    }
}
