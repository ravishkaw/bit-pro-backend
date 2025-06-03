package com.ravi.waterlilly.payload.child;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChildTableDataDTO {
    private Long id;
    private String fullName;
    private LocalDate dob;
    private String genderName;
    private String nationalityName;
    private String guestFullName;
}