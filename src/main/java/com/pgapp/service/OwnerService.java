package com.pgapp.service;

import com.pgapp.entity.Owner;
import com.pgapp.entity.PG;
import com.pgapp.repository.OwnerRepository;
import com.pgapp.repository.PGRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OwnerService {
    private final OwnerRepository ownerRepo;
    private final PGRepository pgRepo;

    public OwnerService(OwnerRepository ownerRepo , PGRepository pgRepo) {
        this.ownerRepo = ownerRepo;
        this.pgRepo = pgRepo;
    }

    public Owner registerOwner(Owner owner) {
        owner.setCreatedAt(LocalDateTime.now());
        return ownerRepo.save(owner);
    }

    public List<PG> getOwner(Long ownerId) {

        return pgRepo.findByOwnerId(ownerId);
    }
}
