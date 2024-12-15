package com.ravi.waterlilly.features.guest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class GuestDTO {
    private Long guestId;
    private String firstName;
    private String lastName;
    private String nic;
    private String gender;
    private String nationality;
    private LocalDate dob;
    private String phone;
    private String email;
    private String address;
    private String emergencyContact;
    private boolean isDeleted;

    /*
     * will add reservation history later
     * also other missed things
     * */
}