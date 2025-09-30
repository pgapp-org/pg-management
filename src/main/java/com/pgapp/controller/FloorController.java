package com.pgapp.controller;


import com.pgapp.converter.FloorConverter;
import com.pgapp.entity.Floor;
import com.pgapp.exception.ResourceNotFoundException;
import com.pgapp.request.owner.FloorRequest;
import com.pgapp.response.FloorResponse;
import com.pgapp.service.FloorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/floors")
@CrossOrigin(origins = "http://localhost:4200")
public class FloorController {

    private final FloorService floorService;

    public FloorController(FloorService floorService) {
        this.floorService = floorService;
    }

    // Add Floor to PG
    @PostMapping("/pg/{pgId}")
    public ResponseEntity<FloorResponse> addFloor(@PathVariable Long pgId, @RequestBody FloorRequest floorRequest) {
        Floor floor = floorService.addFloor(pgId, FloorConverter.toEntity(floorRequest));
        return ResponseEntity.ok(FloorConverter.toResponse(floor));
    }

    // Update Floor
    @PutMapping("/{floorId}")
    public ResponseEntity<FloorResponse> updateFloor(@PathVariable Long floorId, @RequestBody FloorRequest request) {
        return floorService.updateFloor(floorId, FloorConverter.toEntity(request))
                .map(f -> ResponseEntity.ok(FloorConverter.toResponse(f)))
                .orElseThrow(() -> new ResourceNotFoundException("Floor not found with id " + floorId));
    }
    // Delete Floor
    @DeleteMapping("/{floorId}")
    public ResponseEntity<Void> deleteFloor(@PathVariable Long floorId) {
        return floorService.deleteFloor(floorId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }


    // Get Floors of a PG
    @GetMapping("/pg/{pgId}")
    public ResponseEntity<List<FloorResponse>> getFloorsByPG(@PathVariable Long pgId) {
        List<FloorResponse> response = floorService.getFloorsByPG(pgId).stream()
                .map(FloorConverter::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
