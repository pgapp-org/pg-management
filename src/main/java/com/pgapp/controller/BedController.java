package com.pgapp.controller;


import com.pgapp.converter.BedConverter;
import com.pgapp.entity.Bed;
import com.pgapp.response.BedResponse;
import com.pgapp.service.BedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/beds")
@CrossOrigin(origins = "*")
public class BedController {

    private final BedService bedService;

    public BedController(BedService bedService) {
        this.bedService = bedService;
    }

    // 📥 Get all beds in a room
    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<BedResponse>> getBedsByRoom(@PathVariable Long roomId) {
        List<BedResponse> response = bedService.getBedsByRoom(roomId).stream()
                .map(BedConverter::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // ✏️ Update bed occupancy
    @PutMapping("/{bedId}/status")
    public ResponseEntity<Bed> updateBedStatus(@PathVariable Long bedId,
                                               @RequestParam boolean occupied) {
        return ResponseEntity.ok(bedService.updateBedStatus(bedId, occupied));
    }
}
