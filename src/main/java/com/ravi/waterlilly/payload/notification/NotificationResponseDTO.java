package com.ravi.waterlilly.payload.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Notification Response DTO
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponseDTO {
    private Long id;
    private String title;
    private String message;
    private String notificationType;
    private LocalDateTime createdAt;
    private Boolean isRead;
    private LocalDateTime readAt;
}
