package com.pgapp.service;

import com.pgapp.converter.RoomConverter;
import com.pgapp.entity.*;
import com.pgapp.enums.PaymentStatus;
import com.pgapp.exception.ResourceNotFoundException;
import com.pgapp.repository.FloorRepository;
import com.pgapp.repository.PGRepository;
import com.pgapp.repository.RoomRepository;
import com.pgapp.repository.TenantApplicationRepository;
import com.pgapp.response.PGRoomResponse;
import com.pgapp.response.RoomDetailsResponse;
import com.pgapp.response.RoomResponse;
//import jakarta.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

        private final RoomRepository roomRepository;
        private final FloorRepository floorRepository;
        private final PGRepository pgRepo;
        private final TenantApplicationRepository tenantApplicationRepository;
        private final PaymentService paymentService;

        public RoomService(RoomRepository roomRepository, FloorRepository floorRepository,
                           PGRepository pgRepo, TenantApplicationRepository tenantApplicationRepository,
                           PaymentService paymentService) {
            this.roomRepository = roomRepository;
            this.floorRepository = floorRepository;
            this.pgRepo = pgRepo;
            this.tenantApplicationRepository = tenantApplicationRepository;
            this.paymentService = paymentService;
        }


        //    // ➕ Add Room under a Floor
//    public Room addRoom(Long floorId, Room room) {
//        Floor floor = floorRepository.findById(floorId)
//                .orElseThrow(() -> new RuntimeException("Floor not found with id " + floorId));
//
//        room.setFloor(floor);
//        return roomRepository.save(room);
//    }
public Room addRoom(Long floorId, Room room) {
    Floor floor = floorRepository.findById(floorId)
            .orElseThrow(() -> new ResourceNotFoundException("Floor not found with id " + floorId));

    room.setPg(floor.getPg());
    room.setFloor(floor);
    room.setFoodPolicy(floor.getPg().getFoodPolicy());
    System.out.println(floor.getPg().getFoodPolicy());
    System.out.println(room.getFoodPolicy());
    // Set advanceAmount from PG if variableAdvance == false
    if (!floor.getPg().isVariableAdvance()) {
        room.setAdvanceAmount(floor.getPg().getAdvanceAmount());
    }
    // Save first
    Room savedRoom = roomRepository.save(room);

    // Auto-create beds
    List<Bed> beds = new ArrayList<>();
    for (int i = 1; i <= room.getCapacity(); i++) {
        Bed bed = Bed.builder()
                .bedNumber(i)
                .occupied(false)
                .room(savedRoom)
                .tenant(null)
                .build();
        beds.add(bed);
    }
    savedRoom.getBeds().addAll(beds);

    return roomRepository.save(savedRoom);
}

    // ✏️ Update Room
//    public Room updateRoom(Long roomId, Room updatedRoom) {
//        Room existing = roomRepository.findById(roomId)
//                .orElseThrow(() -> new RuntimeException("Room not found with id " + roomId));
//
//        existing.setRoomNumber(updatedRoom.getRoomNumber());
//        existing.setCapacity(updatedRoom.getCapacity());
//        existing.setPrice(updatedRoom.getPrice());
//        existing.setRoom360ViewUrl(updatedRoom.getRoom360ViewUrl());
//
//        return roomRepository.save(existing);
//    }

//    public Optional<Room> updateRoom(Long roomId, Room updatedRoom) {
//        return roomRepository.findById(roomId).map(room -> {
//        room.setRoomNumber(updatedRoom.getRoomNumber());
//        room.setCapacity(updatedRoom.getCapacity());
//        room.setPrice(updatedRoom.getPrice());
//        room.setRoom360ViewUrl(updatedRoom.getRoom360ViewUrl());
//        room.setDoorPosition(updatedRoom.getDoorPosition()); // ✅ update door
//            return roomRepository.save(room);
//        });
//    }

    public Optional<Room> updateRoom(Long roomId, Room updatedRoom) {
        return roomRepository.findById(roomId).map(room -> {
            room.setRoomNumber(updatedRoom.getRoomNumber());
            room.setCapacity(updatedRoom.getCapacity());
            room.setBaseRent(updatedRoom.getBaseRent());
            room.setRoom360ViewUrl(updatedRoom.getRoom360ViewUrl());
            room.setDoorPosition(updatedRoom.getDoorPosition());

            List<Bed> beds = room.getBeds();
            int currentBedCount = beds.size();
            int newCapacity = updatedRoom.getCapacity();

            if (newCapacity > currentBedCount) {
                // Add new beds
                for (int i = currentBedCount + 1; i <= newCapacity; i++) {
                    Bed bed = Bed.builder()
                            .bedNumber(i)
                            .occupied(false)
                            .room(room)
                            .build();
                    beds.add(bed);
                }
            } else if (newCapacity < currentBedCount) {
                // Remove extra beds (only if not occupied)
                beds.removeIf(b -> b.getBedNumber() > newCapacity && !b.isOccupied());
            }

            return roomRepository.save(room);
        });
    }


    // ❌ Delete Room
        public boolean deleteRoom(Long roomId) {
            return  roomRepository.findById(roomId).map(r -> {
                roomRepository.delete(r);
                return true;
            }).orElse(false);
        }

    // 📥 Get Rooms for a Floor
    public List<Room> getRoomsByFloor(Long floorId) {
        Floor floor = floorRepository.findById(floorId)
                .orElseThrow(() -> new RuntimeException("Floor not found with id " + floorId));
        return floor.getRooms();
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

//    public Optional<Room> getRoomById(Long roomId) {
//        return roomRepository.findById(roomId);
//    }
     @Transactional(readOnly = true)
     public Optional<RoomResponse> getRoomResponseById(Long roomId) {
             return roomRepository.findById(roomId).map(RoomConverter::toResponse);
     }

    // in PGService or RoomService
    public List<PGRoomResponse> getRoomsByPG(Long pgId) {
        PG pg = pgRepo.findById(pgId)
                .orElseThrow(() -> new ResourceNotFoundException("PG not found with id " + pgId));

        return pg.getFloors().stream()
                .flatMap(floor -> floor.getRooms().stream()
                        .map(room -> new PGRoomResponse(
                                room.getId(),
                                room.getRoomNumber(),
                                floor.getId(),
                                room.getCapacity(),
                                (int) room.getBeds().stream().filter(Bed::isOccupied).count()
                        )))
                .toList();
    }


    @Transactional(readOnly = true)
    public RoomDetailsResponse getRoomDetails(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with id " + roomId));

        RoomDetailsResponse response = new RoomDetailsResponse();
        response.setRoomId(room.getId());
        response.setRoomNumber(room.getRoomNumber());
        response.setCapacity(room.getCapacity());

        LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);

        List<RoomDetailsResponse.BedDetails> bedDetailsList = room.getBeds().stream().map(bed -> {
            RoomDetailsResponse.BedDetails bedDetails = new RoomDetailsResponse.BedDetails();
            bedDetails.setBedId(bed.getId());
            bedDetails.setBedNumber(bed.getBedNumber());
            bedDetails.setOccupied(bed.isOccupied());

            if (bed.isOccupied() && bed.getTenant() != null) {
                Tenant tenant = bed.getTenant();
                bedDetails.setTenantId(tenant.getId());
                bedDetails.setTenantName(tenant.getName());
                bedDetails.setPhoneNumber(tenant.getPhone());

                // 🔍 Fetch tenant application manually (since Tenant doesn’t have direct link)
                TenantApplication tenantApp = tenantApplicationRepository
                        .findByTenantIdAndCheckoutConfirmedFalse(tenant.getId())
                        .orElse(null);

                if (tenantApp != null) {
                    bedDetails.setTenantApplicationId(tenantApp.getId());
                    bedDetails.setCheckInDate(tenantApp.getCheckInDate());
                    bedDetails.setAppliedForCheckOut(tenantApp.isCheckoutRequested());

                    System.out.println("executed .......................");
                    // 💰 Fetch current month rent payment
                    var paymentOpt = paymentService.getMonthlyRentPayment(tenant.getId(), currentMonth);
                    if (paymentOpt.isPresent()) {
                        var payment = paymentOpt.get();
                        bedDetails.setRentStatus(payment.getStatus());
                        bedDetails.setRentAmount(payment.getAmount());
                        System.out.println(bedDetails.getRentAmount()+"======="+payment.getAmount());
                        System.out.println(bedDetails.getRentStatus()+" === "+payment.getStatus());
                    } else {
                        bedDetails.setRentStatus(PaymentStatus.PENDING);
                        System.out.println("executed .......................Pending");
                    }

                    System.out.println("executed .......................");

                }
            }
            return bedDetails;
        }).toList();

        response.setBeds(bedDetailsList);
        return response;
    }

}
