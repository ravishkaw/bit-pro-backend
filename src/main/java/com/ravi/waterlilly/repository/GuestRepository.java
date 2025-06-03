package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.Guest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// Guest repository
@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {

    // Finds an guest by nationality, idType and idNumber
    @Query("SELECT e FROM Guest e WHERE e.nationality.id=?1 AND e.idType.id=?2 AND e.idNumber=?3")
    Guest findGuestByNationalityIdTypeIdNumber(int nationality, int idType, String idNumber);

    // Find guest by email
    @Query("SELECT e FROM Guest e WHERE e.email=?1")
    Guest findGuestByEmail(String email);

    // Find guest by Mobile No
    @Query("SELECT e FROM Guest e WHERE e.mobileNo=?1")
    Guest findGuestByMobileNo(String mobileNo);

    // Search query
    @Query("SELECT e FROM Guest e WHERE " +
            "LOWER(e.fullName) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(e.callingName) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(e.idNumber) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(e.mobileNo) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(e.email) LIKE LOWER(CONCAT('%', :searchQuery, '%'))")
    Page<Guest> searchGuests(@Param("searchQuery") String searchQuery, Pageable pageable);

}