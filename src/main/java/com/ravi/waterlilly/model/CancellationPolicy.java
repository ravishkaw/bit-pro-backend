package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// cancellation policy class
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cancellation_policy")
public class CancellationPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "min_hours_before_checkin")
    private Integer minHoursBeforeCheckin;

    @Column(name = "refund_percentage")
    private BigDecimal refundPercentage;

    @Column(name = "cancellation_fee")
    private BigDecimal cancellationFee;
}