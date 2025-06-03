package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.RoomPackage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// repository of package
@Repository
public interface RoomPackageRepository extends JpaRepository<RoomPackage, Integer> {

    // find package by name
    @Query("SELECT rp FROM RoomPackage rp WHERE rp.name = ?1")
    RoomPackage findByName(String name);

    // search room packages
    @Query("SELECT rp FROM RoomPackage rp WHERE " +
            "LOWER(rp.name) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(rp.description) LIKE LOWER(CONCAT('%', :searchQuery, '%'))")
    Page<RoomPackage> searchRoomPackages(@Param("searchQuery") String searchQuery, Pageable pageable);
}
