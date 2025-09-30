package com.pgapp.controller;

import com.pgapp.converter.PGConverter;
import com.pgapp.entity.Bed;
import com.pgapp.entity.Floor;
import com.pgapp.entity.PG;
import com.pgapp.entity.Room;
import com.pgapp.exception.ResourceNotFoundException;
import com.pgapp.repository.PGRepository;
import com.pgapp.request.owner.PGRequest;
import com.pgapp.response.PGResponse;
import com.pgapp.service.PGService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pgs")
@CrossOrigin(origins = "http://localhost:4200")
public class PGController {

    private final PGRepository pgRepository;
    private final PGService pgService;

    public PGController(PGService pgService, PGRepository pgRepository) {
        this.pgService = pgService;
        this.pgRepository = pgRepository;
    }

    // ✅ Create PG (basic info only)
    @PostMapping("/{ownerId}/create")
    public ResponseEntity<PGResponse> createPG(@PathVariable Long ownerId, @RequestBody PGRequest pgRequest) {
        PG saved = pgService.createPG(ownerId, PGConverter.toEntity(pgRequest));
        return ResponseEntity.ok(PGConverter.toResponse(saved));
    }

    // ✅ Update PG
    @PutMapping("/{id}")
    public ResponseEntity<PGResponse> updatePG(@PathVariable Long id, @RequestBody PGRequest updatedRequest) {
        return pgService.updatePG(id, PGConverter.toEntity(updatedRequest))
                .map(pg -> ResponseEntity.ok(PGConverter.toResponse(pg)))
                .orElseThrow(() -> new ResourceNotFoundException("PG not found with id " + id));
    }

    // ✅ Delete PG
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePG(@PathVariable Long id) {
        return pgService.deletePG(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // ✅ Get PG by ID
    @GetMapping("/{id}")
    public ResponseEntity<PGResponse> getPGById(@PathVariable Long id) {
        return pgService.getPGById(id)
                .map(pg -> ResponseEntity.ok(PGConverter.toResponse(pg)))
                .orElseThrow(() -> new ResourceNotFoundException("PG not found with id " + id));
    }


    // ✅ Get All PGs
    @GetMapping
    public ResponseEntity<List<PGResponse>> getAllPGs() {
        return ResponseEntity.ok(pgService.getAllPGs().stream()
                .map(PGConverter::toResponse)
                .collect(Collectors.toList()));
    }

    // ------------------- IMAGE ENDPOINTS ------------------- //


    @PostMapping(value = "/{pgId}/images", consumes = "multipart/form-data")
    public ResponseEntity<PGResponse> uploadImages(@PathVariable Long pgId,
                                                   @RequestParam("images") MultipartFile[] images) {
        PG pg = pgService.addImages(pgId, Arrays.asList(images));
        return ResponseEntity.ok(PGConverter.toResponse(pg));
    }


    @DeleteMapping("/{pgId}/images")
    public ResponseEntity<PGResponse> removeImage(@PathVariable Long pgId, @RequestParam String imageUrl) {
        PG pg = pgService.removeImage(pgId, imageUrl);
        return ResponseEntity.ok(PGConverter.toResponse(pg));
    }

    // ------------------- FILTER ENDPOINTS ------------------- //


    @GetMapping("/city/{city}")
    public List<PGResponse> getByCity(@PathVariable String city) {
        return pgRepository.findByCityIgnoreCase(city).stream()
                .map(PGConverter::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/city/{city}/area/{area}")
    public List<PGResponse> getByCityAndArea(@PathVariable String city, @PathVariable String area) {
        return pgRepository.findByCityAndAreaIgnoreCase(city, area).stream()
                .map(PGConverter::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{pgId}/occupancy")
    public Map<String, Integer> getPgOccupancy(@PathVariable Long pgId) {
        PG pg = pgRepository.findById(pgId)
                .orElseThrow(() -> new RuntimeException("PG not found with id " + pgId));

        // Traverse floors → rooms → beds
        int totalBeds = 0;
        int occupiedBeds = 0;

        for (Floor floor : pg.getFloors()) {
            for (Room room : floor.getRooms()) {
                totalBeds += room.getCapacity();
                occupiedBeds += (int) room.getBeds().stream().filter(Bed::isOccupied).count();
            }
        }

        int vacantBeds = totalBeds - occupiedBeds;

        Map<String, Integer> response = new HashMap<>();
        response.put("occupiedBeds", occupiedBeds);
        response.put("vacantBeds", vacantBeds);

        return response;
    }

}

