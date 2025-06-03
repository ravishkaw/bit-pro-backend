package com.ravi.waterlilly.webSocket;

import com.ravi.waterlilly.payload.notification.NotificationDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Date;

// controller for sending test notifications to users via WebSocket.
@Controller
public class WebSocketTestController {
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketTestController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/test")
    public void test(Principal principal) {
        if (principal == null) {
            System.err.println("Error: No authenticated principal");
            return;
        }

        // Send a test notification
        NotificationDTO notification = new NotificationDTO();
        notification.setTitle("Test Notification");
        notification.setMessage("This is a test notification for " + principal.getName() + " at " + new Date());

        try {
            messagingTemplate.convertAndSendToUser(
                    principal.getName(),
                    "/queue/notifications",
                    notification
            );

            messagingTemplate.convertAndSend(
                    "/topic/public-notifications",
                    notification
            );

        } catch (Exception e) {
            System.err.println("Failed to send notification: " + e);
        }
    }
}