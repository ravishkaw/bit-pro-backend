package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Billing repository
@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {
}
