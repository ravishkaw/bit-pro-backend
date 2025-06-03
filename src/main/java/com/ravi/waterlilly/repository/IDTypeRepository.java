package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.IDType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Id type repository
@Repository
public interface IDTypeRepository extends JpaRepository<IDType, Integer> {
}
