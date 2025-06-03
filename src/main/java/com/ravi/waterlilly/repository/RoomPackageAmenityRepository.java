package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.RoomPackageAmenity;
import com.ravi.waterlilly.model.RoomPackageAmenityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Room package amenities repository
@Repository
public interface RoomPackageAmenityRepository extends JpaRepository<RoomPackageAmenity, RoomPackageAmenityId> {
}
