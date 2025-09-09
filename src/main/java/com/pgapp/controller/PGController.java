package com.pgapp.controller;

import com.pgapp.entity.PG;
import com.pgapp.entity.Owner;
import com.pgapp.repository.PGRepository;
import com.pgapp.repository.OwnerRepository;
import com.pgapp.service.PGService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/pgs")
public class PGController {
    private final PGService pgService;

    public PGController(PGService pgService) {
        this.pgService = pgService;
    }

    @PostMapping("/{ownerId}/create")
    public ResponseEntity<?> createPG(@PathVariable Long ownerId, @RequestBody PG pg) {
        try {
            PG saved = pgService.createPG(ownerId, pg);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
