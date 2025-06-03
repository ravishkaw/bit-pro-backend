package com.ravi.waterlilly.payload.guest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// reference with id and name
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuestReferenceDTO {
    private Long id;
    private String fullName;
    private String email;
    private String mobileNo;
}
