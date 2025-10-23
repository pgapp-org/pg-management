package com.pgapp.repository;

import com.pgapp.entity.PgNotification;
import com.pgapp.entity.PG;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PgNotificationRepository extends JpaRepository<PgNotification, Long> {

    // Get all notifications for a PG, order by newest first
    List<PgNotification> findByPgOrderByCreatedAtDesc(PG pg);
}
