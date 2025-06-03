package com.ravi.waterlilly.event;

import com.ravi.waterlilly.exception.APIException;
import com.ravi.waterlilly.model.Notification;
import com.ravi.waterlilly.model.Task;
import com.ravi.waterlilly.model.User;
import com.ravi.waterlilly.payload.notification.NotificationDTO;
import com.ravi.waterlilly.payload.notification.NotificationResponseDTO;
import com.ravi.waterlilly.repository.UserRepository;
import com.ravi.waterlilly.service.NotificationService;
import com.ravi.waterlilly.webSocket.WebSocketNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// Event listener for task created events.
@Component
@RequiredArgsConstructor
public class TaskEventListener {

    private final NotificationService notificationService;
    private final WebSocketNotificationService webSocketNotificationService;
    private final UserRepository userRepository;

    @EventListener
    @Transactional
    public void handleTaskCreatedEvent(TaskCreatedEvent event) {
        Task task = event.getTask();
        String targetTypeName = event.getTargetType();
        String targetName = event.getTargetName();

        if (task == null || task.getTaskType() == null) {
            return;
        }

        String title = "New Task For " + targetTypeName + " " + targetName + "!";
        String taskDescription = task.getDescription();
        String message = String.format("%s %s needs %s.", targetTypeName, targetName, taskDescription);

        try {
            // Get housekeeping staff IDs
            List<Long> housekeepingUserIds = getHousekeepingUserIds();

            if (housekeepingUserIds.isEmpty()) {
                return;
            }

            // Create notification for database storage
            NotificationDTO dbNotification = new NotificationDTO();
            dbNotification.setTitle(title);
            dbNotification.setMessage(message);
            dbNotification.setNotificationTypeId(4); // Task notification type
            dbNotification.setRecipientUserIds(housekeepingUserIds);

            // Save to database
            Notification savedNotification = notificationService.addNotification(dbNotification);

            // Create notification for WebSocket delivery
            NotificationResponseDTO wsNotification = new NotificationResponseDTO();
            wsNotification.setTitle(savedNotification.getTitle());
            wsNotification.setMessage(savedNotification.getMessage());
            wsNotification.setNotificationType(savedNotification.getNotificationType().getName());
            wsNotification.setCreatedAt(savedNotification.getCreatedAt());
            wsNotification.setIsRead(false);

            // Send WebSocket notifications to each user
            for (Long userId : housekeepingUserIds) {
                User user = fetchUser(userId);
                webSocketNotificationService.notifyUser(user.getUsername(), wsNotification);
            }

            // Also broadcast to a general housekeeping topic
            webSocketNotificationService.broadcastNotification("housekeeping", wsNotification);

        } catch (Exception e) {
            throw new RuntimeException("Error handling task created event", e);
        }
    }

    private List<Long> getHousekeepingUserIds() {
        List<User> houseKeepers = userRepository.findUsersByRole("Housekeeping");

        // Fallback to Admin if no housekeepers are found
        if (houseKeepers.isEmpty()) {
            houseKeepers = userRepository.findUsersByRole("Admin");
        }

        return houseKeepers.stream()
                .map(User::getId)
                .collect(Collectors.toList());
    }

    private User fetchUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new APIException("User not found"));
    }
}