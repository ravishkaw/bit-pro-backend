package com.ravi.waterlilly.payload.guest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// DTO for Guest form data
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuestPayloadDTO {
    private Long id;
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
}
