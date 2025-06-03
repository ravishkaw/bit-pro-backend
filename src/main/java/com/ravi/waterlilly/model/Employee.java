package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;

//Employee entity to represent employee data
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "emp_no")
    @Length(max = 8)
    private String empNo;

    @NotBlank
    @Size(min = 2, message = "Full Name should be at least two characters")
    @Column(name = "full_name")
    private String fullName;

    @NotBlank
    @Column(name = "calling_name")
    private String callingName;

    @NotBlank
    @Column(name = "id_number")
    private String idNumber;

    @Column(name = "date_of_birth")
    private LocalDate dob;

    @Column(name = "note")
    private String note;

    @NotBlank
    @Column(name = "address")
    private String address;

    @NotBlank
    @Column(name = "mobile_no")
    private String mobileNo;

    @NotBlank
    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "emergency_no")
    private String emergencyNo;

    @Column(name = "added_datetime")
    private LocalDateTime addedDateTime;

    @Column(name = "last_modified_datetime")
    private LocalDateTime lastModifiedDateTime;

    @Column(name = "deleted_datetime")
    private LocalDateTime deletedDateTime;

    @ManyToOne
    @JoinColumn(name = "title_id", referencedColumnName = "id")
    private Title title;

    @ManyToOne
    @JoinColumn(name = "id_type_id", referencedColumnName = "id")
    private IDType idType;

    @ManyToOne
    @JoinColumn(name = "gender_id", referencedColumnName = "id")
    private Gender gender;

    @ManyToOne
    @JoinColumn(name = "nationality_id", referencedColumnName = "id")
    private Nationality nationality;

    @ManyToOne
    @JoinColumn(name = "civil_status_id", referencedColumnName = "id")
    private CivilStatus civilStatus;

    @ManyToOne
    @JoinColumn(name = "designation_id", referencedColumnName = "id")
    private Designation designation;

    @ManyToOne
    @JoinColumn(name = "employee_status_id", referencedColumnName = "id")
    private EmployeeStatus employeeStatus;
}
