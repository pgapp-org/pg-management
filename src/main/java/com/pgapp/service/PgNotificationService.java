package com.pgapp.service;

import com.pgapp.entity.Owner;
import com.pgapp.entity.PG;
import com.pgapp.entity.PgNotification;
import com.pgapp.repository.PgNotificationRepository;
import com.pgapp.repository.PGRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PgNotificationService {

    private final PgNotificationRepository notificationRepository;
    private final PGRepository pgRepository;

    // OWNER: add notification
    public PgNotification addNotification(Long ownerId, Long pgId, String title, String message) {
        PG pg = pgRepository.findById(pgId)
                .orElseThrow(() -> new RuntimeException("PG not found"));

        // Optional: check if owner owns this PG
        if (!pg.getOwner().getId().equals(ownerId)) {
            throw new RuntimeException("Owner not authorized for this PG");
        }

        PgNotification notification = PgNotification.builder()
                .pg(pg)
                .title(title)
                .message(message)
                .createdAt(LocalDateTime.now())
                .build();

        return notificationRepository.save(notification);
    }

    // OWNER: delete notification
    public void deleteNotification(Long ownerId, Long notificationId) {
        PgNotification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        PG pg = notification.getPg();

        if (!pg.getOwner().getId().equals(ownerId)) {
            throw new RuntimeException("Owner not authorized for this PG");
        }

        notificationRepository.delete(notification);
    }

    // TENANT: get notifications for tenant's PG
    public List<PgNotification> getNotificationsForTenant(Long pgId) {
        PG pg = pgRepository.findById(pgId)
                .orElseThrow(() -> new RuntimeException("PG not found"));

        return notificationRepository.findByPgOrderByCreatedAtDesc(pg);
    }
}
