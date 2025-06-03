package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Designation Repository
@Repository
public interface DesignationRepository extends JpaRepository<Designation, Integer> {
}
