package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.NotificationRecipient;
import com.ravi.waterlilly.model.NotificationRecipientId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

// repository layer of notification recipient
@Repository
public interface NotificationRecipientRepository extends JpaRepository<NotificationRecipient, NotificationRecipientId> {
    // get all unresolved notifications for a user ordered by creation date descending
    @Query("SELECT nr FROM NotificationRecipient nr " +
            "JOIN FETCH nr.notification n " +
            "WHERE nr.user.id = ?1 AND n.isResolved!=true " +
            "ORDER BY n.createdAt DESC")
    List<NotificationRecipient> findByUserIdOrderByNotificationCreatedAtDesc(Long userId);

    // get all unread notifications for a user
    @Query("SELECT nr FROM NotificationRecipient nr " +
            "JOIN FETCH nr.notification n " +
            "WHERE nr.user.id = ?1 AND nr.isRead = false")
    List<NotificationRecipient> findByUserIdAndIsReadFalse(Long userId);

    // get count of unread notifications for a user
    @Query("SELECT COUNT(nr) FROM NotificationRecipient nr " +
            "WHERE nr.user.id = ?1 AND nr.isRead = false")
    int countByUserIdAndIsReadFalse(Long userId);

    // Find all recipients for a specific notification
    @Query("SELECT nr FROM NotificationRecipient nr WHERE nr.notification.id = ?1")
    List<NotificationRecipient> findByNotificationId(Long notificationId);
}