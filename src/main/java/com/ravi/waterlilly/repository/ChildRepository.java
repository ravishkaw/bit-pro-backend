package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.Child;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// repository layer of child
@Repository
public interface ChildRepository extends JpaRepository<Child, Long> {

    // search child
    @Query("SELECT c FROM Child c WHERE " +
            "LOWER(c.fullName) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(c.guest.email) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(c.guest.fullName) LIKE LOWER(CONCAT('%', :searchQuery, '%'))")
    Page<Child> searchChildren(@Param("searchQuery") String searchQuery, Pageable pageable);
}