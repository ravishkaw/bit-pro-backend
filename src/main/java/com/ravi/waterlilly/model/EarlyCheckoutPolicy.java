package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// early checkout policy class
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "early_checkout_policy")
public class EarlyCheckoutPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "policy_name")
    private String policyName;

    @Column(name = "min_reserved_nights")
    private Integer minReservedNights;

    @Column(name = "max_reserved_nights")
    private Integer maxReservedNights;

    @Column(name = "min_actual_nights")
    private Integer minActualNights;

    @Column(name = "max_actual_nights")
    private Integer maxActualNights;

    @Column(name = "min_days_before_checkout")
    private Integer minDaysBeforeCheckout;

    @Column(name = "early_checkout_fee")
    private BigDecimal earlyCheckoutFee;

    @Column(name = "partial_refund_percentage")
    private BigDecimal partialRefundPercentage;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;
}
