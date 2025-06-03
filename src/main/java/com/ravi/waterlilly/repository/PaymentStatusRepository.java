package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

// repository layer of payment status
@Repository
public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Integer> {

    // find payment status by name
    @Query("SELECT s FROM PaymentStatus s WHERE s.name=?1")
    PaymentStatus findByStatusName(String statusName);
}
