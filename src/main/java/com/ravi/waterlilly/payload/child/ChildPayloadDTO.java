package com.ravi.waterlilly.payload.child;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// child payload class
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChildPayloadDTO {
    private Long id;
    private String fullName;
    private LocalDate dob;
    private Integer genderId;
    private Integer nationalityId;
    private Long guestId;
}