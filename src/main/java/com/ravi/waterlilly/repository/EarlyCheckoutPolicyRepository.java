package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.EarlyCheckoutPolicy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

// repository layer of early checkout policy
@Repository
public interface EarlyCheckoutPolicyRepository extends JpaRepository<EarlyCheckoutPolicy, Integer> {

    // find applicable early checkout policies
    @Query("""
            SELECT p FROM EarlyCheckoutPolicy p
            WHERE p.status.name = 'Active'
            AND (:reservedNights >= p.minReservedNights)
            AND (p.maxReservedNights IS NULL OR :reservedNights <= p.maxReservedNights)
            AND (:actualNights >= p.minActualNights)
            AND (p.maxActualNights IS NULL OR :actualNights <= p.maxActualNights)
            AND :daysEarly >= p.minDaysBeforeCheckout
            ORDER BY p.id ASC
            """)
    List<EarlyCheckoutPolicy> findApplicablePolicy(Integer reservedNights, Integer actualNights, Integer daysEarly, Pageable limitOne);
}
