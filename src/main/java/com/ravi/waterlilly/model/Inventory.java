package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

//Inventory class
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Column(name = "item_name")
    private String itemName;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "last_restocked_date")
    private LocalDate lastRestockedDate;

    @Column(name = "added_datetime")
    private LocalDateTime addedDatetime;

    @Column(name = "last_modified_datetime")
    private LocalDateTime lastModifiedDatetime;

    @ManyToOne
    @JoinColumn(name = "item_type_id", referencedColumnName = "id")
    private InventoryItemType itemType;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private InventoryStatus status;
}
