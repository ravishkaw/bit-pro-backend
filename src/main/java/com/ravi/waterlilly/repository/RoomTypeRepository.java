package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.RoomType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// Room Type repository
@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Integer> {

    // get all room types that isn't deleted
    @Query("SELECT rt FROM RoomType rt WHERE rt.status.name = 'Active'")
    List<RoomType> getRoomTypesNotDeleted();

    // find room type by name
    @Query("SELECT rt FROM RoomType rt WHERE rt.name = ?1")
    RoomType findByRoomTypeName(String name);

    // search room types
    @Query("SELECT rt FROM RoomType rt WHERE " +
            "LOWER(rt.name) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(rt.description) LIKE LOWER(CONCAT('%', :searchQuery, '%'))")
    Page<RoomType> searchRoomTypes(@Param("searchQuery") String searchQuery, Pageable pageable);
}
