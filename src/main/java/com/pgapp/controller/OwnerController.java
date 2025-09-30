package com.pgapp.controller;

import com.pgapp.converter.owner.OwnerConverter;
import com.pgapp.entity.Owner;
import com.pgapp.entity.PG;
import com.pgapp.request.owner.OwnerLoginRequest;
import com.pgapp.request.owner.OwnerRegisterRequest;
import com.pgapp.response.owner.OwnerLoginResponse;
import com.pgapp.response.owner.OwnerRegisterResponse;
import com.pgapp.service.OwnerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/owners")
@CrossOrigin(origins = "http://localhost:4200")
public class OwnerController {


    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @PostMapping("/register")
    public ResponseEntity<OwnerRegisterResponse> register(@Valid @RequestBody OwnerRegisterRequest request) {
        Owner owner = OwnerConverter.toEntity(request);
        Owner savedOwner = ownerService.registerOwner(owner);
        return ResponseEntity.ok(OwnerConverter.toRegisterResponse(savedOwner));
    }


    @PostMapping("/login")
    public ResponseEntity<OwnerLoginResponse> login(@Valid @RequestBody OwnerLoginRequest request) {
        Owner owner = ownerService.loginOwner(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new OwnerLoginResponse(true, "Login successful", owner));
    }






    @GetMapping("/{id}")
    public ResponseEntity<Owner> getOwnerById(@PathVariable Long id) {
        Owner owner = ownerService.getOwnerById(id); // throws if not found
        return ResponseEntity.ok(owner);
    }

    @GetMapping
    public ResponseEntity<List<Owner>> getAllOwners() {
        return ResponseEntity.ok(ownerService.getAllOwners());
    }


    @PutMapping("/{id}")
    public ResponseEntity<Owner> updateOwner(@PathVariable Long id, @RequestBody Owner updatedOwner) {
        Owner owner = ownerService.updateOwner(id, updatedOwner); // throws if not found
        return ResponseEntity.ok(owner);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOwner(@PathVariable Long id) {
        ownerService.deleteOwner(id); // throws if not found
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{ownerId}/pgs")
    public ResponseEntity<?> getPgs(@PathVariable Long ownerId) {
        List<PG> pgs = ownerService.getPgsByOwnerId(ownerId); // ✅ use existing method
        return ResponseEntity.ok(pgs);
    }
}
