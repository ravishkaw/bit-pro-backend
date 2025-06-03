package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.LateCheckinPolicy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

// Late checkin policy repository
@Repository
public interface LateCheckinPolicyRepository extends JpaRepository<LateCheckinPolicy, Integer> {

    // find applicable late checkin policies
    @Query("SELECT l FROM LateCheckinPolicy l " +
            "WHERE l.lateHoursThreshold <= ?1 " +
            "ORDER BY l.lateHoursThreshold DESC")
    List<LateCheckinPolicy> findApplicablePolicies(Integer hoursLate, Pageable pageable);
}
