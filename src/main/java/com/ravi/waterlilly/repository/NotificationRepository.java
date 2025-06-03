package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

// repository layer of notification
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Delete all resolved notifications older than 7 days
    @Query("SELECT n FROM Notification n WHERE n.isResolved = true AND n.resolvedAt < ?1")
    List<Notification> findOldNotifications(LocalDateTime cutoffDate);
}
