package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.EventPackage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// repository of package
@Repository
public interface EventPackageRepository extends JpaRepository<EventPackage, Integer> {

    // find package by name
    @Query("SELECT ep FROM EventPackage ep WHERE ep.name = ?1")
    EventPackage findByName(String name);

    // search event packages
    @Query("SELECT ep FROM EventPackage ep WHERE " +
            "LOWER(ep.name) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(ep.description) LIKE LOWER(CONCAT('%', :searchQuery, '%'))")
    Page<EventPackage> searchEventPackages(@Param("searchQuery") String searchQuery, Pageable pageable);
}
