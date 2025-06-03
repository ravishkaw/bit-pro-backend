package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.RoomPricingRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// repository of room pricing rule
@Repository
public interface RoomPricingRuleRepository extends JpaRepository<RoomPricingRule, Integer> {

    // search room pricing rule
    @Query("SELECT pr FROM RoomPricingRule pr WHERE " +
            "LOWER(pr.name) LIKE LOWER(CONCAT('%', :searchQuery, '%'))")
    Page<RoomPricingRule> searchPricingRules(@Param("searchQuery") String searchQuery, Pageable pageable);

}


//    @Query("SELECT r FROM RoomPricingRule r WHERE r.status.name = 'ACTIVE' " +
//            "AND ((r.startDate <= :endDate AND r.endDate >= :startDate))")
//    List<RoomPricingRule> findActiveRulesByDateRange(
//            @Param("startDate") LocalDate startDate,
//            @Param("endDate") LocalDate endDate);

//    // check if there is any pricing rule exists in between days
//    @Query("SELECT COUNT(r) > 0 FROM RoomPricingRule r WHERE " +
//            "(?2 >= r.startDate AND ?1 <= r.endDate) AND r.status.name  = 'Active'")
//    boolean existsByDateRangeOverlap(LocalDate startDate, LocalDate endDate);
//
//    // check if there is any pricing rule exists in between days for update excluding id
//    @Query("SELECT COUNT(r) > 0 FROM RoomPricingRule r WHERE " +
//            "(?2 >= r.startDate AND ?1 <= r.endDate) AND r.id != ?3 AND r.status.name = 'Active'")
//    boolean existsByDateRangeOverlapExcludingId(LocalDate startDate, LocalDate endDate, Integer ruleId);

