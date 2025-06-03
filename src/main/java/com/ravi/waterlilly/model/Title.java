package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//title class (Mr. Mrs.)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "title")
public class Title {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank
    @Column(name = "name", unique = true)
    private String name;

}
