package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.BedType;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

// repository class of bed type
@Repository
public interface BedTypeRepository extends JpaRepository<BedType, Integer> {

    // find bed type by name
    @Query("SELECT bt FROM BedType bt WHERE bt.name =?1")
    BedType findBedTypeByName(@NotBlank String name);
}
