package com.pgapp.controller;

import com.pgapp.entity.PgNotification;
import com.pgapp.service.PgNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class PgNotificationController {

    private final PgNotificationService notificationService;

    // ✅ Owner adds a notification
    @PostMapping("/owner/{ownerId}/pg/{pgId}")
    public PgNotification addNotification(
            @PathVariable Long ownerId,
            @PathVariable Long pgId,
            @RequestParam String title,
            @RequestParam String message) {
        return notificationService.addNotification(ownerId, pgId, title, message);
    }

    // ✅ Owner deletes a notification
    @DeleteMapping("/owner/{ownerId}/{notificationId}")
    public void deleteNotification(
            @PathVariable Long ownerId,
            @PathVariable Long notificationId) {
        notificationService.deleteNotification(ownerId, notificationId);
    }

    // ✅ Tenant fetches all notifications for their PG
    @GetMapping("/tenant/pg/{pgId}")
    public List<PgNotification> getNotificationsForTenant(@PathVariable Long pgId) {
        return notificationService.getNotificationsForTenant(pgId);
    }
}
