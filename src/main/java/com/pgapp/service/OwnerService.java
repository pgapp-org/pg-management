package com.pgapp.service;

import com.pgapp.entity.Owner;
import com.pgapp.entity.PG;
import com.pgapp.repository.OwnerRepository;
import com.pgapp.repository.PGRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OwnerService {
    private final OwnerRepository ownerRepo;
    private final PGRepository pgRepo;

    public OwnerService(OwnerRepository ownerRepo , PGRepository pgRepo) {
        this.ownerRepo = ownerRepo;
        this.pgRepo = pgRepo;
    }

    // register owner
    public Owner registerOwner(Owner owner) {
        owner.setCreatedAt(LocalDateTime.now());
        return ownerRepo.save(owner);
    }

    // ✅ Get Owner by ID
    public Optional<Owner> getOwnerById(Long id) {
        return ownerRepo.findById(id);
    }

    // ✅ Get All Owners
    public List<Owner> getAllOwners() {
        return ownerRepo.findAll();
    }

    // ✅ Update Owner
    public Optional<Owner> updateOwner(Long id, Owner updatedOwner) {
        return ownerRepo.findById(id).map(owner -> {
            owner.setName(updatedOwner.getName());
            owner.setEmail(updatedOwner.getEmail());
            owner.setPhone(updatedOwner.getPhone());
            return ownerRepo.save(owner);
        });
    }

    // ✅ Delete Owner
    public boolean deleteOwner(Long id) {
        if (ownerRepo.existsById(id)) {
            ownerRepo.deleteById(id);
            return true;
        }
        return false;
    }


    // get list of pg by ownerId

    public List<PG> getOwner(Long ownerId) {

        return pgRepo.findByOwnerId(ownerId);
    }
}
