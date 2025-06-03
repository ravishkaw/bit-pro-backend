package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.Set;

//Users entity to represent user data
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Column(name = "username")
    @Size(min = 2, message = "Username should be at least two characters")
    private String username;

    @NotBlank
    @Column(name = "password")
    @Length(min = 8, message = "Password should be at least 8 characters")
    private String password;

    @NotBlank
    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "photo")
    private String photo;

    @Column(name = "added_datetime")
    private LocalDateTime addedDatetime;

    @Column(name = "note")
    private String note;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Status status;

    @ManyToMany
    @JoinTable(name = "user_has_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> role;
}
