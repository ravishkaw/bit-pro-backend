package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

// notification class
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "message")
    private String message;

    @ManyToOne
    @JoinColumn(name = "notification_type_id")
    private NotificationType notificationType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_resolved")
    private Boolean isResolved;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<NotificationRecipient> recipients = new HashSet<>();
}