package com.ravi.waterlilly.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Notification Recipient Id for many to many relationship
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRecipientId {

    @Column(name = "notification_id")
    private Long notificationId;

    @Column(name = "user_id")
    private Long userId;
}
