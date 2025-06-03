package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Employee status repository
@Repository
public interface EmployeeStatusRepository extends JpaRepository<EmployeeStatus, Integer> {

    //Finds employee status by name.
    EmployeeStatus findByName(String name);
}
