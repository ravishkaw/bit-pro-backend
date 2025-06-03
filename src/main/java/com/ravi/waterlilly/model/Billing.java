package com.ravi.waterlilly.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Billing entity class
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "billing")
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "base_price")
    private BigDecimal basePrice;

    @ManyToOne
    @JoinColumn(name = "discount_id", referencedColumnName = "id")
    private Discount discount;

    @Column(name = "discount")
    private BigDecimal discountAmount;

    @ManyToOne
    @JoinColumn(name = "tax_id", referencedColumnName = "id")
    private Tax tax;

    @Column(name = "total_tax")
    private BigDecimal totalTax;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "net_amount")
    private BigDecimal netAmount;

    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    @Column(name = "paid_amount")
    private BigDecimal paidAmount;

    @Column(name = "billing_date")
    private LocalDateTime billingDate;

    @ManyToOne
    @JoinColumn(name = "payment_status_id")
    private PaymentStatus paymentStatus;

    @Column(name = "last_modified_datetime")
    private LocalDateTime lastModifiedDatetime;

    @Column(name = "note")
    private String note;
}
