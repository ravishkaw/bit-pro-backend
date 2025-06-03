package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

//Room type class
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "room_type")
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank
    @Column(name = "name")
    private String name;

    @Column(name = "base_price")
    private BigDecimal basePrice;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "bed_type_id", referencedColumnName = "id")
    private BedType bedType;

    @ManyToMany
    @JoinTable(name = "room_type_has_pricing_rule",
            joinColumns = @JoinColumn(name = "room_type_id"),
            inverseJoinColumns = @JoinColumn(name = "pricing_rule_id"))
    private Set<RoomPricingRule> pricingRules;

    @OneToMany(mappedBy = "roomType")
    private Set<Room> rooms;
}