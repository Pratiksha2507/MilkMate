package com.milkmate.service;

import java.util.List;

import com.milkmate.entity.Notification;

public interface NotificationService {

    Notification createNotification(
            String title,
            String message,
            com.milkmate.entity.NotificationType type,
            Long farmerId
    );

    List<Notification> getFarmerNotifications(
            Long farmerId
    );

    List<Notification> getUnreadNotifications(
            Long farmerId
    );

    long getUnreadCount(
            Long farmerId
    );

    Notification markAsRead(
            Long notificationId
    );

    void markAllAsRead(
            Long farmerId
    );
}