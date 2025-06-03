package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// repository layer of payment method
@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {
}
