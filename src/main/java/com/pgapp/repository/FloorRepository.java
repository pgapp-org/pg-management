package com.pgapp.repository;

import com.pgapp.entity.Floor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FloorRepository extends JpaRepository<Floor, Long> {
    List<Floor> findByPgId(Long pgId);
    Optional<Floor> findByPgIdAndFloorNumber(Long pgId, Integer floorNumber);
}

