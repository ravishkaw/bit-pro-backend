package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Notification Recipient many to many relationship entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notification_recipient")
public class NotificationRecipient {

    @EmbeddedId
    private NotificationRecipientId id;

    @ManyToOne
    @MapsId("notificationId")
    @JoinColumn(name = "notification_id")
    private Notification notification;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "is_read")
    private Boolean isRead = false;

    @Column(name = "read_at")
    private LocalDateTime readAt;
}
