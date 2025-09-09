package com.pgapp.controller;

import com.pgapp.entity.PG;
import com.pgapp.entity.Owner;
import com.pgapp.repository.PGRepository;
import com.pgapp.repository.OwnerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/pgs")
public class PGController {
    private final PGRepository pgRepo;
    private final OwnerRepository ownerRepo;

    public PGController(PGRepository pgRepo, OwnerRepository ownerRepo) {
        this.pgRepo = pgRepo;
        this.ownerRepo = ownerRepo;
    }

    @PostMapping("/{ownerId}/create")
    public ResponseEntity<?> createPG(@PathVariable Long ownerId, @RequestBody PG pg) {
        Optional<Owner> ownerOpt = ownerRepo.findById(ownerId);
        if (ownerOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Owner not found");
        }
        pg.setOwner(ownerOpt.get());
        PG saved = pgRepo.save(pg);
        return ResponseEntity.ok(saved);
    }


}
