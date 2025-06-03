package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.CancellationPolicy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

// repository layer of cancellation policy
@Repository
public interface CancellationPolicyRepository extends JpaRepository<CancellationPolicy, Integer> {

    // find applicable cancellation policies
    @Query("SELECT c FROM CancellationPolicy c " +
            "WHERE c.minHoursBeforeCheckin <= ?1 " +
            "ORDER BY c.minHoursBeforeCheckin DESC")
    List<CancellationPolicy> findApplicablePolicies(Integer hoursBeforeCheckin, Pageable pageable);
}
