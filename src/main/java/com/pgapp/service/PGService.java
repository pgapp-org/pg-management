package com.pgapp.service;

import com.pgapp.entity.Floor;
import com.pgapp.entity.Owner;
import com.pgapp.entity.PG;
import com.pgapp.entity.Room;
import com.pgapp.repository.OwnerRepository;
import com.pgapp.repository.PGRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PGService {

    private final PGRepository pgRepo;
    private final OwnerRepository ownerRepo;

    public PGService(PGRepository pgRepo, OwnerRepository ownerRepo) {
        this.pgRepo = pgRepo;
        this.ownerRepo = ownerRepo;
    }

    // ✅ Create PG with nested Floors and Rooms
    public PG createPG(Long ownerId, PG pg) {
        Owner owner = ownerRepo.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        pg.setOwner(owner);

        // link back PG -> Floors -> Rooms
        if (pg.getFloors() != null) {
            for (Floor floor : pg.getFloors()) {
                floor.setPg(pg);
                if (floor.getRooms() != null) {
                    for (Room room : floor.getRooms()) {
                        room.setFloor(floor);
                        // Initial vacancy = full capacity
                        if (room.getOccupiedBeds() == null) {
                            room.setOccupiedBeds(0);
                        }
                    }
                }
            }
        }

        return pgRepo.save(pg);
    }

    // ✅ Get PG by ID
    public Optional<PG> getPGById(Long id) {
        return pgRepo.findById(id);
    }

    // ✅ Get All PGs
    public List<PG> getAllPGs() {
        return pgRepo.findAll();
    }

    // ✅ Update PG basic info (not floors/rooms here)
    public Optional<PG> updatePG(Long id, PG updatedPG) {
        return pgRepo.findById(id).map(pg -> {
            pg.setName(updatedPG.getName());
            pg.setAddress(updatedPG.getAddress());
            return pgRepo.save(pg);
        });
    }

    // ✅ Delete PG
    public boolean deletePG(Long id) {
        if (pgRepo.existsById(id)) {
            pgRepo.deleteById(id);
            return true;
        }
        return false;
    }
}
