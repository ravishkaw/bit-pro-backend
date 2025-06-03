package com.ravi.waterlilly.payload.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// DTO to use in payload
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeePayloadDTO {

    private Long id;
    private String empNo;
    private String fullName;
    private String callingName;
    private String idNumber;
    private LocalDate dob;
    private String note;
    private String address;
    private String mobileNo;
    private String email;
    private String emergencyNo;
    private Integer titleId;
    private Integer idTypeId;
    private Integer genderId;
    private Integer nationalityId;
    private Integer civilStatusId;
    private Integer designationId;
    private Integer employeeStatusId;
}
