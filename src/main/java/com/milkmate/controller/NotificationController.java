package com.milkmate.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.milkmate.entity.Notification;
import com.milkmate.entity.NotificationType;
import com.milkmate.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
@PreAuthorize("hasAnyRole('ADMIN','STAFF','FARMER')")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService) {

        this.notificationService =
                notificationService;
    }

    // ==========================================
    // GET ALL FARMER NOTIFICATIONS
    // ==========================================

    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<Notification>>
    getFarmerNotifications(
            @PathVariable Long farmerId) {

        return ResponseEntity.ok(
                notificationService
                        .getFarmerNotifications(
                                farmerId
                        )
        );
    }

    // ==========================================
    // GET UNREAD NOTIFICATIONS
    // ==========================================

    @GetMapping("/farmer/{farmerId}/unread")
    public ResponseEntity<List<Notification>>
    getUnreadNotifications(
            @PathVariable Long farmerId) {

        return ResponseEntity.ok(
                notificationService
                        .getUnreadNotifications(
                                farmerId
                        )
        );
    }

    // ==========================================
    // GET UNREAD COUNT
    // ==========================================

    @GetMapping("/farmer/{farmerId}/unread-count")
    public ResponseEntity<Long>
    getUnreadCount(
            @PathVariable Long farmerId) {

        return ResponseEntity.ok(
                notificationService
                        .getUnreadCount(
                                farmerId
                        )
        );
    }

    // ==========================================
    // CREATE NOTIFICATION
    // ==========================================

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<Notification>
    createNotification(

            @RequestParam String title,

            @RequestParam String message,

            @RequestParam NotificationType type,

            @RequestParam Long farmerId) {

        Notification notification =
                notificationService.createNotification(
                        title,
                        message,
                        type,
                        farmerId
                );

        return ResponseEntity.ok(notification);
    }

    // ==========================================
    // MARK ONE AS READ
    // ==========================================

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Notification>
    markAsRead(
            @PathVariable Long notificationId) {

        return ResponseEntity.ok(
                notificationService
                        .markAsRead(
                                notificationId
                        )
        );
    }

    // ==========================================
    // MARK ALL AS READ
    // ==========================================

    @PutMapping("/farmer/{farmerId}/read-all")
    public ResponseEntity<String>
    markAllAsRead(
            @PathVariable Long farmerId) {

        notificationService.markAllAsRead(
                farmerId
        );

        return ResponseEntity.ok(
                "All notifications marked as read"
        );
    }
}