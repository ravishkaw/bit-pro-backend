package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Role repository
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
