package com.pgapp.service;

import com.pgapp.entity.Bed;
import com.pgapp.repository.BedRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BedService {

    private final BedRepository bedRepository;

    public BedService(BedRepository bedRepository) {
        this.bedRepository = bedRepository;
    }

    public List<Bed> getBedsByRoom(Long roomId) {
        return bedRepository.findByRoomId(roomId);
    }

    public Bed updateBedStatus(Long bedId, boolean occupied) {
        Bed bed = bedRepository.findById(bedId)
                .orElseThrow(() -> new RuntimeException("Bed not found with id " + bedId));
        bed.setOccupied(occupied);
        return bedRepository.save(bed);
    }
}
