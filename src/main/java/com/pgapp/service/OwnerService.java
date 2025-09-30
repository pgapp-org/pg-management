package com.pgapp.service;

import com.pgapp.entity.Owner;
import com.pgapp.entity.PG;
import com.pgapp.exception.AuthenticationFailedException;
import com.pgapp.exception.DuplicateResourceException;
import com.pgapp.exception.ResourceNotFoundException;
import com.pgapp.repository.OwnerRepository;
import com.pgapp.repository.PGRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OwnerService {
    private final OwnerRepository ownerRepo;
    private final PGRepository pgRepo;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public OwnerService(OwnerRepository ownerRepo , PGRepository pgRepo) {
        this.ownerRepo = ownerRepo;
        this.pgRepo = pgRepo;
    }

    // register owner
    public Owner registerOwner(Owner owner) {
        if (ownerRepo.existsByEmail(owner.getEmail())) {
            throw new DuplicateResourceException("Email " + owner.getEmail() + " is already registered");
        }
        owner.setCreatedAt(LocalDateTime.now());
        owner.setPasswordHash(owner.getPasswordHash()); // hash before saving
        return ownerRepo.save(owner);
    }


//    public Owner loginOwner(String email, String rawPassword) {
//        return ownerRepo.findByEmail(email)
//                .filter(owner -> passwordEncoder.matches(rawPassword, owner.getPasswordHash()))
//                .orElseThrow(() -> new AuthenticationFailedException("Invalid email or password"));
//    }
public Owner loginOwner(String email, String rawPassword) {
    System.out.println("Login attempt:");
    System.out.println("Input Email: '" + email + "'");
    System.out.println("Input Password: '" + rawPassword + "'");

    Optional<Owner> ownerOptional = ownerRepo.findByEmail(email);

    if (ownerOptional.isPresent()) {
        Owner owner = ownerOptional.get();
        System.out.println("DB Email: '" + owner.getEmail() + "'");
        System.out.println("DB Password (hashed): '" + owner.getPasswordHash() + "'");

        if (passwordEncoder.matches(rawPassword, owner.getPasswordHash())) {
            System.out.println("Password matched ✅");
            return owner;
        } else {
            System.out.println("Password mismatch ❌");
        }
    } else {
        System.out.println("No owner found with this email ❌");
    }

    throw new AuthenticationFailedException("Invalid email or password");
}




    // ✅ Get Owner by ID
    public Owner getOwnerById(Long id) {
        return ownerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Owner with id " + id + " not found"));
    }




    // ✅ Get All Owners
    public List<Owner> getAllOwners() {
        return ownerRepo.findAll();
    }

    // ✅ Update Owner
    public Owner updateOwner(Long id, Owner updatedOwner) {
        return ownerRepo.findById(id)
                .map(owner -> {
                    owner.setName(updatedOwner.getName());
                    owner.setEmail(updatedOwner.getEmail());
                    owner.setPhone(updatedOwner.getPhone());
                    return ownerRepo.save(owner);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Owner with id " + id + " not found"));
    }


    // ✅ Delete Owner
    public void deleteOwner(Long id) {
        if (!ownerRepo.existsById(id)) {
            throw new ResourceNotFoundException("Owner with id " + id + " not found");
        }
        ownerRepo.deleteById(id);
    }



    // get list of pg by ownerId

    public List<PG> getPgsByOwnerId(Long ownerId) {
        return pgRepo.findByOwnerId(ownerId);
    }
}
