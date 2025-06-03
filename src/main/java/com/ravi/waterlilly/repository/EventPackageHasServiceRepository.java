package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.EventPackageHasService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Event package service repository
@Repository
public interface EventPackageHasServiceRepository extends JpaRepository<EventPackageHasService, Integer> {
}
