package com.pgapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pgapp.entity.PG;
import com.pgapp.repository.PGRepository;
import com.pgapp.service.PGService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pgs")
@CrossOrigin(origins = "http://localhost:4200")
public class PGController {

    @Autowired
    private PGRepository pgRepository;

    private final PGService pgService;

    public PGController(PGService pgService) {
        this.pgService = pgService;
    }

    // ✅ Create PG with Floors + Rooms
//    @PostMapping("/{ownerId}/create")
//    public ResponseEntity<?> createPG(@PathVariable Long ownerId, @RequestBody PG pg) {
//        try {
//            PG saved = pgService.createPG(ownerId, pg);
//            return ResponseEntity.ok(saved);
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

    @PostMapping(
            value = "/{ownerId}/create",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<?> createPG(
            @PathVariable Long ownerId,
            @RequestPart("pg") String pgJson,   // get raw JSON string
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        try {
            // Convert JSON string to PG object
            ObjectMapper objectMapper = new ObjectMapper();
            PG pg = objectMapper.readValue(pgJson, PG.class);

            PG saved = pgService.createPG(ownerId, pg, images);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    // ✅ Get PG by ID
    @GetMapping("/{id}")
    public ResponseEntity<PG> getPGById(@PathVariable Long id) {
        return pgService.getPGById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Get All PGs
    @GetMapping
    public ResponseEntity<List<PG>> getAllPGs() {
        return ResponseEntity.ok(pgService.getAllPGs());
    }

    // ✅ Update PG basic info
    @PutMapping("/{id}")
    public ResponseEntity<PG> updatePG(@PathVariable Long id, @RequestBody PG updatedPG) {
        return pgService.updatePG(id, updatedPG)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Delete PG
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePG(@PathVariable Long id) {
        return pgService.deletePG(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/city/{city}")
    public List<PG> getByCity(@PathVariable String city) {
        return pgRepository.findByCityIgnoreCase(city);
    }

    @GetMapping("/city/{city}/area/{area}")
    public List<PG> getByCityAndArea(@PathVariable String city, @PathVariable String area) {
        return pgRepository.findByCityAndAreaIgnoreCase(city, area);
    }
}
