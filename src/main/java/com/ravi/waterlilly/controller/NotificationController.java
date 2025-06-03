package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.APIResponse;
import com.ravi.waterlilly.payload.notification.NotificationResponseDTO;
import com.ravi.waterlilly.payload.notification.MarkNotificationReadRequest;
import com.ravi.waterlilly.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // Get unresolved notifications for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponseDTO>> getUserNotifications(@PathVariable Long userId) {
        List<NotificationResponseDTO> notifications = notificationService.getUserNotifications(userId);
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    // Mark a notification as read for a user
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<APIResponse> markNotificationAsRead(
            @PathVariable Long notificationId,
            @RequestBody @Valid MarkNotificationReadRequest request) {
        notificationService.markAsRead(notificationId, request.getUserId());
        return new ResponseEntity<>(
                new APIResponse("Notification marked as read", true),
                HttpStatus.OK
        );
    }

    // Mark all notifications as read for a user
    @PutMapping("/mark-all-read")
    public ResponseEntity<APIResponse> markAllNotificationsAsRead(
            @RequestBody @Valid MarkNotificationReadRequest request) {
        notificationService.markAllAsRead(request.getUserId());
        return new ResponseEntity<>(
                new APIResponse("All notifications marked as read", true),
                HttpStatus.OK
        );
    }

    // Get unread notification count for a user
    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<Integer> getUnreadNotificationCount(@PathVariable Long userId) {
        int count = notificationService.getUnreadNotificationCount(userId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    // Mark a notification as resolved
    @PutMapping("/{notificationId}/resolve")
    public ResponseEntity<APIResponse> markNotificationAsResolved(@PathVariable Long notificationId) {
        notificationService.markAsResolved(notificationId);
        return new ResponseEntity<>(
                new APIResponse("Notification marked as resolved", true),
                HttpStatus.OK
        );
    }
}