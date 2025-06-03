package com.ravi.waterlilly.payload.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Employee table data
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeTableDataDTO {
    private Long id;
    private String empNo;
    private String fullName;
    private String callingName;
    private String mobileNo;
    private String email;
    private String designationName;
    private String employeeStatusName;
}
