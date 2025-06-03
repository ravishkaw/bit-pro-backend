package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository layer of gender entity
@Repository
public interface GenderRepository extends JpaRepository<Gender, Integer> {
}
