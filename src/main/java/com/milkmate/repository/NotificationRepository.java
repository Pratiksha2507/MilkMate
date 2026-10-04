package com.milkmate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.milkmate.entity.Notification;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findByFarmerIdOrderByCreatedAtDesc(
            Long farmerId
    );

    List<Notification> findByFarmerIdAndReadStatusFalseOrderByCreatedAtDesc(
            Long farmerId
    );

    long countByFarmerIdAndReadStatusFalse(
            Long farmerId
    );
}