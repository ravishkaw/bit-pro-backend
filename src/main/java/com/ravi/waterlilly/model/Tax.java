package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// entity class tax
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tax")
public class Tax {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank
    @Column(name = "name")
    private String name;

    @NotBlank
    @Column(name = "percentage")
    private BigDecimal percentage;

    @NotBlank
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Status status;
}
