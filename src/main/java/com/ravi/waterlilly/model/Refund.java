package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// refund class
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "refund")
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "billing_id", referencedColumnName = "id")
    private Billing billing;

    @NotBlank
    @Column(name = "amount")
    private BigDecimal amount;

    @NotBlank
    @Column(name = "reason")
    private String reason;

    @Column(name = "refund_datetime")
    private LocalDateTime refundDatetime;
}