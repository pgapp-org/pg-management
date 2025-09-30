package com.pgapp.service;

import com.pgapp.entity.Floor;
import com.pgapp.entity.PG;
import com.pgapp.exception.ResourceNotFoundException;
import com.pgapp.repository.FloorRepository;
import com.pgapp.repository.PGRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FloorService {

    private final FloorRepository floorRepo;
    private final PGRepository pgRepo;

    public FloorService(FloorRepository floorRepo, PGRepository pgRepo) {
        this.floorRepo = floorRepo;
        this.pgRepo = pgRepo;
    }

    // Add floor to a PG
    public Floor addFloor(Long pgId, Floor floor) {
        PG pg = pgRepo.findById(pgId)
                .orElseThrow(() -> new ResourceNotFoundException("PG not found with id " + pgId));
        floor.setPg(pg);
        return floorRepo.save(floor);
    }

    // Update floor
    public Optional<Floor> updateFloor(Long floorId, Floor updatedFloor) {
        return floorRepo.findById(floorId).map(floor -> {
            floor.setFloorNumber(updatedFloor.getFloorNumber());
            return floorRepo.save(floor);
        });
    }


    // Delete floor
    public boolean deleteFloor(Long floorId) {
        return floorRepo.findById(floorId).map(f -> {
            floorRepo.delete(f);
            return true;
        }).orElse(false);
    }

    // Get floors by PG
    public List<Floor> getFloorsByPG(Long pgId) {
        return floorRepo.findByPgId(pgId);
    }
}

