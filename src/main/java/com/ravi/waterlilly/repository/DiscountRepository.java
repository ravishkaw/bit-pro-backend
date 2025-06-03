package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

// repository layer of Discount
@Repository
public interface DiscountRepository extends JpaRepository<Discount, Integer> {

    // find applicable discounts on the day
    @Query("SELECT d FROM Discount d WHERE ?1 BETWEEN d.startDate AND d.endDate AND d.status.name='Active' " +
            "ORDER BY d.percentage DESC")
    List<Discount> findApplicableDiscount(LocalDate checkInDate);

    // find discount by code
    @Query("SELECT d FROM Discount d WHERE d.code=?1")
    Discount findByCode(String code);

    // check if there is any discount existing in between days
    @Query("SELECT COUNT(d) > 0 FROM Discount d WHERE " +
            "(?2 >= d.startDate AND ?1 <= d.endDate)")
    boolean existsByDateRangeOverlap(LocalDate startDate, LocalDate endDate);

    // check if there is any discount existing in between days for update excluding id
    @Query("SELECT COUNT(d) > 0 FROM Discount d WHERE " +
            "(?2 >= d.startDate AND ?1 <= d.endDate) AND d.id != ?3")
    boolean existsByDateRangeOverlapExcludingId(LocalDate startDate, LocalDate endDate, Integer id);
}
