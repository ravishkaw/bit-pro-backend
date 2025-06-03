package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Privilege entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "privilege")
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "select_op")
    private Boolean selectOp;

    @Column(name = "insert_op")
    private Boolean insertOp;

    @Column(name = "delete_op")
    private Boolean deleteOp;

    @Column(name = "update_op")
    private Boolean updateOp;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "module_id", referencedColumnName = "id")
    private Module module;
}
