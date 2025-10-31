package com.pgapp.controller;

import com.pgapp.converter.RoomConverter;
import com.pgapp.entity.Room;
import com.pgapp.exception.ResourceNotFoundException;
import com.pgapp.repository.RoomRepository;
import com.pgapp.request.owner.RoomRequest;
import com.pgapp.response.PGRoomResponse;
import com.pgapp.response.RoomDetailsResponse;
import com.pgapp.response.RoomResponse;
import com.pgapp.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*")
public class RoomController {

    private final RoomService roomService;
    private final RoomRepository roomRepository;

    public RoomController(RoomService roomService , RoomRepository roomRepository) {
        this.roomService = roomService;
        this.roomRepository = roomRepository;
    }

    // ➕ Add Room under Floor
    @PostMapping("/floor/{floorId}")
    public ResponseEntity<RoomResponse> addRoom(@PathVariable Long floorId, @RequestBody RoomRequest roomRequest) {
        Room room = roomService.addRoom(floorId, RoomConverter.toEntity(roomRequest));
        return ResponseEntity.ok(RoomConverter.toResponse(room));
    }

    // ✏️ Update Room
    @PutMapping("/{roomId}")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long roomId, @RequestBody RoomRequest request) {
        return roomService.updateRoom(roomId, RoomConverter.toEntity(request))
                .map(r -> ResponseEntity.ok(RoomConverter.toResponse(r)))
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id " + roomId));
    }

    // ❌ Delete Room
    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        return roomService.deleteRoom(roomId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // 📥 Get all Rooms for a Floor
    @GetMapping("/floor/{floorId}")
    public ResponseEntity<List<RoomResponse>> getRoomsByFloor(@PathVariable Long floorId) {
        List<RoomResponse> response = roomService.getRoomsByFloor(floorId).stream()
                .map(RoomConverter::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // 📥 Get all rooms
    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    // 📥 Get Room by Id
//    @GetMapping("/{roomId}")
//    public ResponseEntity<Room> getRoomById(@PathVariable Long roomId) {
//        return roomService.getRoomById(roomId)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
    @GetMapping("/{roomId}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long roomId) {
        return roomService.getRoomResponseById(roomId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // in PGController or RoomController
    @GetMapping("/{pgId}/rooms")
    public ResponseEntity<List<PGRoomResponse>> getRoomsByPG(@PathVariable Long pgId) {
        List<PGRoomResponse> rooms = roomService.getRoomsByPG(pgId);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/tenant/rooms/{roomId}")
    public ResponseEntity<RoomDetailsResponse> getRoomDetails(@PathVariable Long roomId) {
        RoomDetailsResponse rooms = roomService.getRoomDetails(roomId);
        return ResponseEntity.ok(rooms);
    }



}
