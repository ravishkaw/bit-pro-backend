package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.exception.APIException;
import com.ravi.waterlilly.model.*;
import com.ravi.waterlilly.payload.notification.NotificationDTO;
import com.ravi.waterlilly.payload.notification.NotificationResponseDTO;
import com.ravi.waterlilly.repository.NotificationRecipientRepository;
import com.ravi.waterlilly.repository.NotificationRepository;
import com.ravi.waterlilly.repository.NotificationTypeRepository;
import com.ravi.waterlilly.repository.UserRepository;
import com.ravi.waterlilly.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// Implementation of Notification Service.
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationTypeRepository notificationTypeRepository;
    private final NotificationRecipientRepository recipientRepository;

    // Creates a new notification and saves it to the database
    @Override
    @Transactional
    public Notification addNotification(NotificationDTO dto) {
        // Validate input
        if (dto == null || dto.getRecipientUserIds() == null || dto.getRecipientUserIds().isEmpty()) {
            return null;
        }

        try {
            // Create and save the notification
            Notification notification = createNotification(dto);

            // Create recipients for the notification
            createNotificationRecipients(dto.getRecipientUserIds(), notification);
            return notification;

        } catch (IllegalArgumentException e) {
            throw e; // Pass through validation exceptions with their specific messages
        } catch (Exception e) {
            throw new APIException("Failed to create notification");
        }
    }

    // Creates a notification entity from the DTO and saves it to the database
    private Notification createNotification(NotificationDTO dto) {
        NotificationType type = notificationTypeRepository.findById(dto.getNotificationTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Notification type not found: " + dto.getNotificationTypeId()));

        Notification notification = new Notification();
        notification.setTitle(dto.getTitle());
        notification.setMessage(dto.getMessage());
        notification.setNotificationType(type);
        notification.setIsResolved(false);
        notification.setCreatedAt(LocalDateTime.now());

        return notificationRepository.save(notification);
    }

    // Creates notification recipient records for each user
    private void createNotificationRecipients(List<Long> userIds, Notification notification) {
        for (Long userId : userIds) {
            try {
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new APIException("User not found: " + userId));

                NotificationRecipient recipient = new NotificationRecipient();
                NotificationRecipientId id = new NotificationRecipientId();
                id.setNotificationId(notification.getId());
                id.setUserId(userId);

                recipient.setId(id);
                recipient.setNotification(notification);
                recipient.setUser(user);
                recipient.setIsRead(false);
                recipient.setReadAt(null);

                recipientRepository.save(recipient);
            } catch (Exception e) {
                throw new APIException("Failed to create notification recipient for user " + userId + ": " + e.getMessage());
            }
        }
    }

    // Retrieves unresolved notifications for a user
    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponseDTO> getUserNotifications(Long userId) {
        if (userId == null) {
            return List.of();
        }

        try {
            return recipientRepository.findByUserIdOrderByNotificationCreatedAtDesc(userId).stream()
                    .map(this::mapToResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }

    // Mark a notification as read for a user
    @Transactional
    @Override
    public void markAsRead(Long notificationId, Long userId) {
        if (notificationId == null || userId == null) {
            return;
        }

        try {
            NotificationRecipientId id = new NotificationRecipientId();
            id.setNotificationId(notificationId);
            id.setUserId(userId);

            NotificationRecipient recipient = recipientRepository.findById(id)
                    .orElseThrow(() -> new APIException(
                            String.format("Notification recipient not found for notification %d and user %d",
                                    notificationId, userId)
                    ));

            recipient.setIsRead(true);
            recipient.setReadAt(LocalDateTime.now());
            recipientRepository.save(recipient);

        } catch (Exception e) {
            throw new APIException("Failed to mark notification as read" + e);
        }
    }

    // Mark a notification as resolved
    @Override
    @Transactional
    public void markAsResolved(Long notificationId) {
        if (notificationId == null) {
            return;
        }

        try {
            Notification notification = notificationRepository.findById(notificationId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            String.format("Notification not found with id: %d", notificationId)
                    ));

            notification.setIsResolved(true);
            notification.setResolvedAt(LocalDateTime.now());
            notificationRepository.save(notification);

            // Mark as read for all recipients
            List<NotificationRecipient> recipients = recipientRepository.findByNotificationId(notificationId);
            LocalDateTime now = LocalDateTime.now();

            for (NotificationRecipient recipient : recipients) {
                if (!recipient.getIsRead()) {
                    recipient.setIsRead(true);
                    recipient.setReadAt(now);
                    recipientRepository.save(recipient);
                }
            }
        } catch (Exception e) {
            throw new APIException("Failed to mark notification as resolved: " + e.getMessage());
        }
    }

    // Mark all notifications for a user as read
    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        if (userId == null) {
            return;
        }

        try {
            List<NotificationRecipient> unreadNotifications =
                    recipientRepository.findByUserIdAndIsReadFalse(userId);

            LocalDateTime now = LocalDateTime.now();

            for (NotificationRecipient recipient : unreadNotifications) {
                recipient.setIsRead(true);
                recipient.setReadAt(now);
                recipientRepository.save(recipient);
            }
        } catch (Exception e) {
            throw new APIException("Failed to mark all notifications as read" + e);
        }
    }

    // Get the number of unread notifications for a user
    @Override
    @Transactional(readOnly = true)
    public int getUnreadNotificationCount(Long userId) {
        if (userId == null) {
            return 0;
        }
        try {
            return recipientRepository.countByUserIdAndIsReadFalse(userId);
        } catch (Exception e) {
            return 0;
        }
    }

    // Helper method to map a NotificationRecipient to a NotificationResponseDTO
    private NotificationResponseDTO mapToResponseDTO(NotificationRecipient recipient) {
        Notification notification = recipient.getNotification();
        NotificationResponseDTO dto = new NotificationResponseDTO();
        dto.setId(notification.getId());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setNotificationType(notification.getNotificationType().getName());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setIsRead(recipient.getIsRead());
        dto.setReadAt(recipient.getReadAt());
        return dto;
    }

    // Delete old resolved notifications older than 7 days at midnight
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void deleteOldNotifications() {
        try {
            LocalDateTime cutoffDate = LocalDateTime.now().minusDays(7);
            List<Notification> oldNotifications = notificationRepository.findOldNotifications(cutoffDate);

            if (!oldNotifications.isEmpty()) {
                notificationRepository.deleteAll(oldNotifications);
            }
        } catch (Exception e) {
            throw new APIException("Failed to delete old notifications: " + e.getMessage());
        }
    }
}