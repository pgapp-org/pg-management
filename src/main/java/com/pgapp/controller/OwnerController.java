package com.pgapp.controller;

import com.pgapp.entity.Owner;
import com.pgapp.entity.PG;
import com.pgapp.repository.OwnerRepository;
import com.pgapp.repository.PGRepository;
import com.pgapp.service.OwnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/owners")
public class OwnerController {


    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @PostMapping("/register")
    public ResponseEntity<Owner> register(@RequestBody Owner owner) {
        Owner savedOwner = ownerService.registerOwner(owner);
        return ResponseEntity.ok(savedOwner);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Owner> getOwnerById(@PathVariable Long id) {
        return ownerService.getOwnerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Owner>> getAllOwners() {
        return ResponseEntity.ok(ownerService.getAllOwners());
    }


    @PutMapping("/{id}")
    public ResponseEntity<Owner> updateOwner(@PathVariable Long id, @RequestBody Owner updatedOwner) {
        return ownerService.updateOwner(id, updatedOwner)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOwner(@PathVariable Long id) {
        return ownerService.deleteOwner(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }


    @GetMapping("/{ownerId}/pgs")
    public ResponseEntity<?> getPgs(@PathVariable Long ownerId) {
        List<PG> pgs = ownerService.getOwner(ownerId);
        return ResponseEntity.ok(pgs);
    }
}
