package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// repository layer of refund
@Repository
public interface RefundRepository extends JpaRepository<Refund, Integer> {
}
