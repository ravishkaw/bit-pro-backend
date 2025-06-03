package com.ravi.waterlilly.payload.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO to use in user module
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUserDTO {
    private Long id;
    private String empNo;
    private String fullName;
    private String email;
}
