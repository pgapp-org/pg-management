package com.pgapp.repository;

import com.pgapp.entity.PG;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PGRepository extends JpaRepository<PG, Long> {
    List<PG> findByOwnerId(Long ownerId);
    Optional<PG> findByName(String name);
}
