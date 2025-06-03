package com.ravi.waterlilly.payload.guest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO for Populate guest table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuestTableDataDTO {
    private Long id;
    private String titleName;
    private String fullName;
    private String callingName;
    private String mobileNo;
    private String email;
    private String nationalityName;
    private String statusName;
}
