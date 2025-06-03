package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

//Guest entity to represent employee data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "guest")
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

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
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Status status;

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

    @OneToMany(mappedBy = "guest")
    private Set<Child> child;
}
