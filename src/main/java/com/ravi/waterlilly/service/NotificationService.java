package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.Notification;
import com.ravi.waterlilly.payload.notification.NotificationDTO;
import com.ravi.waterlilly.payload.notification.NotificationResponseDTO;

import java.util.List;

// Service interface for managing notification service.
public interface NotificationService {

    // Add a notification to the database.
    Notification addNotification(NotificationDTO dto);

    // Get unresolved notifications for a user.
    List<NotificationResponseDTO> getUserNotifications(Long userId);

    // Mark a notification as read.
    void markAsRead(Long notificationId, Long userId);

    // Mark all notifications as read.
    void markAllAsRead(Long userId);

    // Get the total number of unread notifications for a user.
    int getUnreadNotificationCount(Long userId);

    // Mark a notification as resolved
    void markAsResolved(Long notificationId);
}