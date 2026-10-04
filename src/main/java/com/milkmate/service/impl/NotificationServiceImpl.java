package com.milkmate.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.milkmate.entity.Notification;
import com.milkmate.entity.NotificationType;
import com.milkmate.exception.ResourceNotFoundException;
import com.milkmate.repository.NotificationRepository;
import com.milkmate.service.NotificationService;

@Service
public class NotificationServiceImpl
        implements NotificationService {

    private final NotificationRepository
            notificationRepository;


    public NotificationServiceImpl(
            NotificationRepository notificationRepository) {

        this.notificationRepository =
                notificationRepository;
    }


    // ==========================================
    // CREATE
    // ==========================================

    @Override
    public Notification createNotification(
            String title,
            String message,
            NotificationType type,
            Long farmerId) {

        if (
            title == null ||
            title.trim().isEmpty()
        ) {

            throw new IllegalArgumentException(
                    "Notification title is required"
            );
        }


        if (
            message == null ||
            message.trim().isEmpty()
        ) {

            throw new IllegalArgumentException(
                    "Notification message is required"
            );
        }


        if (type == null) {

            throw new IllegalArgumentException(
                    "Notification type is required"
            );
        }


        if (farmerId == null) {

            throw new IllegalArgumentException(
                    "Farmer ID is required"
            );
        }


        Notification notification =
                new Notification();


        notification.setTitle(
                title.trim()
        );

        notification.setMessage(
                message.trim()
        );

        notification.setType(
                type
        );

        notification.setFarmerId(
                farmerId
        );

        notification.setReadStatus(
                false
        );


        return notificationRepository.save(
                notification
        );
    }


    // ==========================================
    // GET ALL FARMER NOTIFICATIONS
    // ==========================================

    @Override
    public List<Notification>
            getFarmerNotifications(
                    Long farmerId) {

        return notificationRepository
                .findByFarmerIdOrderByCreatedAtDesc(
                        farmerId
                );
    }


    // ==========================================
    // GET UNREAD
    // ==========================================

    @Override
    public List<Notification>
            getUnreadNotifications(
                    Long farmerId) {

        return notificationRepository
                .findByFarmerIdAndReadStatusFalseOrderByCreatedAtDesc(
                        farmerId
                );
    }


    // ==========================================
    // GET UNREAD COUNT
    // ==========================================

    @Override
    public long getUnreadCount(
            Long farmerId) {

        return notificationRepository
                .countByFarmerIdAndReadStatusFalse(
                        farmerId
                );
    }


    // ==========================================
    // MARK ONE AS READ
    // ==========================================

    @Override
    public Notification markAsRead(
            Long notificationId) {

        Notification notification =
                notificationRepository
                        .findById(notificationId)
                        .orElseThrow(
                                () ->
                                new ResourceNotFoundException(
                                        "Notification not found with id: "
                                                + notificationId
                                )
                        );


        notification.setReadStatus(
                true
        );


        return notificationRepository.save(
                notification
        );
    }


    // ==========================================
    // MARK ALL AS READ
    // ==========================================

    @Override
    public void markAllAsRead(
            Long farmerId) {

        List<Notification> notifications =
                notificationRepository
                        .findByFarmerIdAndReadStatusFalseOrderByCreatedAtDesc(
                                farmerId
                        );


        for (
            Notification notification :
            notifications
        ) {

            notification.setReadStatus(
                    true
            );
        }


        if (
            !notifications.isEmpty()
        ) {

            notificationRepository.saveAll(
                    notifications
            );
        }
    }
}