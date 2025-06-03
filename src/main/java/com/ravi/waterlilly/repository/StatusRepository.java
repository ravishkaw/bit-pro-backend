package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.Status;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

// Repository layer of civil status entity
@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {

    // find status by name
    @Query("SELECT s FROM Status s WHERE s.name=?1")
    Status findStatusByName(String name);
}
