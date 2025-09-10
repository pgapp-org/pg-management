package com.pgapp.repository;

import com.pgapp.entity.Floor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FloorRepository extends JpaRepository<Floor, Long> {
    List<Floor> findByPgId(Long pgId);
}

