package com.pgapp.controller;

import com.pgapp.entity.Owner;
import com.pgapp.repository.OwnerRepository;
import com.pgapp.repository.PGRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/owners")
public class OwnerController {


    private final OwnerRepository ownerRepo;
    private final PGRepository pgRepo;

    public OwnerController(OwnerRepository ownerRepo, PGRepository pgRepo) {
        this.ownerRepo = ownerRepo;
        this.pgRepo = pgRepo;
    }

    @PostMapping("/register")
    public ResponseEntity<Owner> register(@RequestBody Owner owner) {
        owner.setCreatedAt(LocalDateTime.now());
        // NOTE: store hashed password in production
        Owner saved = ownerRepo.save(owner);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{ownerId}/pgs")
    public ResponseEntity<?> getPgs(@PathVariable Long ownerId) {
        return ResponseEntity.ok(pgRepo.findByOwnerId(ownerId));
    }
}
