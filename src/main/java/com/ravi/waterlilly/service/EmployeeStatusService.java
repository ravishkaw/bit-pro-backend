package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.EmployeeStatus;
import com.ravi.waterlilly.payload.ReferenceDataDTO;

import java.util.List;

// Service interface for managing employee status service.
public interface EmployeeStatusService {

    //Fetches a list of Employee status.
    List<ReferenceDataDTO> getAllStatus();

    // get employee status by id
    EmployeeStatus getEmployeeStatusById(Integer id);

    // set active status
    void setEmployeeStatusActive(Long employeeId);

    // set deleted status
    void setEmployeeStatusDeleted(Long employeeId);
}
