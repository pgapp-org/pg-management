package com.pgapp.controller;

import com.pgapp.entity.PG;
import com.pgapp.service.PGService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pgs")
public class PGController {

    private final PGService pgService;

    public PGController(PGService pgService) {
        this.pgService = pgService;
    }

    // ✅ Create PG with Floors + Rooms
    @PostMapping("/{ownerId}/create")
    public ResponseEntity<?> createPG(@PathVariable Long ownerId, @RequestBody PG pg) {
        try {
            PG saved = pgService.createPG(ownerId, pg);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
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
}
