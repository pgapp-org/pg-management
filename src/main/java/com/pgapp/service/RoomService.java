package com.pgapp.service;

import com.pgapp.converter.RoomConverter;
import com.pgapp.entity.Bed;
import com.pgapp.entity.Floor;
import com.pgapp.entity.PG;
import com.pgapp.entity.Room;
import com.pgapp.exception.ResourceNotFoundException;
import com.pgapp.repository.FloorRepository;
import com.pgapp.repository.PGRepository;
import com.pgapp.repository.RoomRepository;
import com.pgapp.response.PGRoomResponse;
import com.pgapp.response.RoomResponse;
//import jakarta.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final FloorRepository floorRepository;
    private final PGRepository pgRepo;

    public RoomService(RoomRepository roomRepository, FloorRepository floorRepository, PGRepository pgRepo) {
        this.roomRepository = roomRepository;
        this.floorRepository = floorRepository;
        this.pgRepo = pgRepo;
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

    room.setFloor(floor);

    // Save first
    Room savedRoom = roomRepository.save(room);

    // Auto-create beds
    List<Bed> beds = new ArrayList<>();
    for (int i = 1; i <= room.getCapacity(); i++) {
        Bed bed = Bed.builder()
                .bedNumber(i)
                .occupied(false)
                .room(savedRoom)
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
            room.setPrice(updatedRoom.getPrice());
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


}
