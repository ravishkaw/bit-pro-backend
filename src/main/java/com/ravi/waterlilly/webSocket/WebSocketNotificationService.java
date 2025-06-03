package com.ravi.waterlilly.webSocket;

import com.ravi.waterlilly.exception.APIException;
import com.ravi.waterlilly.payload.notification.NotificationDTO;
import com.ravi.waterlilly.payload.notification.NotificationResponseDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

// Service class for sending notifications to users via WebSocket.
@Service
public class WebSocketNotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketNotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Send a notification to a user via WebSocket.
    public void notifyUser(String userName, NotificationResponseDTO notification) {
        try {
            messagingTemplate.convertAndSendToUser(
                    userName,
                    "/queue/notifications",
                    notification
            );
        } catch (Exception e) {
            System.err.println("Failed to send notification: " + e);
            throw new APIException("Failed to send notification to user " + userName + ": " + e.getMessage());
        }
    }

    // Send broadcast notification to all users via WebSocket.
    public void broadcastNotification(String topic, NotificationResponseDTO notification) {
        try {
            messagingTemplate.convertAndSend("/topic/" + topic, notification);
        } catch (Exception e) {
            throw new APIException("Failed to send notification to topic " + topic + ": " + e.getMessage());
        }
    }
}