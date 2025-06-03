package com.ravi.waterlilly.payload.notification;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MarkNotificationReadRequest {
    @NotNull(message = "User ID is required")
    private Long userId;
}