package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.CivilStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository layer of civil status entity
@Repository
public interface CivilStatusRepository extends JpaRepository<CivilStatus, Integer> {
}
