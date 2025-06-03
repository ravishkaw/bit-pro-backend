package com.ravi.waterlilly.payload.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// Notification DTO
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    private String title;
    private String message;
    private Integer notificationTypeId;
    private List<Long> recipientUserIds;
}
