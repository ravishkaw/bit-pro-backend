package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// late checkin policy class
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "late_checkin_policy")
public class LateCheckinPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "late_hours_threshold")
    private Integer lateHoursThreshold;

    @Column(name = "late_checkin_fee")
    private BigDecimal lateCheckinFee;
}