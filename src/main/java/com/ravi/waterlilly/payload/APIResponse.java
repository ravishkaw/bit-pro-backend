package com.ravi.waterlilly.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Response structure for API responses.
@AllArgsConstructor
@NoArgsConstructor
@Data
public class APIResponse {
    private String message;
    private boolean status;
}
