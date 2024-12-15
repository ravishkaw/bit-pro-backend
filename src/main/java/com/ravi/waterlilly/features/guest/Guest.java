package com.ravi.waterlilly.features.guest;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity(name = "guests")
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guest_id")
    private Long guestId;

    @NotBlank
    @Size(min = 2, message = "First Name should be at least two characters")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Size(min = 2, message = "Last Name should be at least two characters")
    @Column(name = "last_name")
    private String lastName;

    @NotBlank
    @Column(name = "nic")
    private String nic;

    @NotBlank
    @Column(name = "gender")
    private String gender;

    @NotBlank
    @Column(name = "nationality")
    private String nationality;

    @Column(name = "dob")
    private LocalDate dob;

    @NotBlank
    @Column(name = "phone")
    private String phone;

    @NotBlank
    @Email
    @Column(name = "email")
    private String email;

    @NotBlank
    @Column(name = "address")
    private String address;

    @Column(name = "emergency_contact")
    private String emergencyContact;

    @Column(name = "is_deleted")
    private boolean isDeleted;
}
