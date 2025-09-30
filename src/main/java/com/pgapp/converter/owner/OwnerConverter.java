package com.pgapp.converter.owner;

import com.pgapp.entity.Owner;
import com.pgapp.request.owner.OwnerRegisterRequest;
import com.pgapp.response.owner.OwnerDetailsResponse;
import com.pgapp.response.owner.OwnerRegisterResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

public class OwnerConverter {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Request → Entity
    public static Owner toEntity(OwnerRegisterRequest request) {
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        Owner owner = new Owner();
        owner.setName(request.getName());
        owner.setEmail(request.getEmail());
        owner.setPhone(request.getPhone());
        owner.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        owner.setCreatedAt(LocalDateTime.now());
        return owner;
    }

    // Entity → RegisterResponse
    public static OwnerRegisterResponse toRegisterResponse(Owner owner) {
        return new OwnerRegisterResponse(true, "Owner registered successfully", owner.getId(),  owner.getName());
    }

    // Entity → OwnerDetailsResponse
    public static OwnerDetailsResponse toDetailsResponse(Owner owner) {
        return new OwnerDetailsResponse(owner.getId(), owner.getName(), owner.getEmail(), owner.getPhone());
    }
}
