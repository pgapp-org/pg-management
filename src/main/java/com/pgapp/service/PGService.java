package com.pgapp.service;

import com.pgapp.entity.Owner;
import com.pgapp.entity.PG;
import com.pgapp.repository.OwnerRepository;
import com.pgapp.repository.PGRepository;
import org.springframework.stereotype.Service;

@Service
public class PGService {
    private final PGRepository pgRepo;
    private final OwnerRepository ownerRepo;

    public PGService(PGRepository pgRepo, OwnerRepository ownerRepo) {
        this.pgRepo = pgRepo;
        this.ownerRepo = ownerRepo;
    }

    public PG createPG(Long ownerId, PG pg) {
        Owner owner = ownerRepo.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        pg.setOwner(owner);
        return pgRepo.save(pg);
    }
}
