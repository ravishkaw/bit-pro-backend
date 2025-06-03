package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

// Room pricing rule model class
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pricing_rule")
public class RoomPricingRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "pricing_multiplier")
    private BigDecimal pricingMultiplier;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Status status;

    @ManyToMany(mappedBy = "pricingRules")
    private Set<RoomType> roomTypes;
}
