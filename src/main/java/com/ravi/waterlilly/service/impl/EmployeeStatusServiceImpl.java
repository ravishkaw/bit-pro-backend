package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.Employee;
import com.ravi.waterlilly.model.EmployeeStatus;
import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.repository.EmployeeRepository;
import com.ravi.waterlilly.repository.EmployeeStatusRepository;
import com.ravi.waterlilly.service.EmployeeStatusService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// Implementation of Employee Status Service.
@Service
@RequiredArgsConstructor
public class EmployeeStatusServiceImpl implements EmployeeStatusService {
    public final EmployeeStatusRepository statusRepository;
    private final ModelMapper modelMapper;
    private final EmployeeRepository employeeRepository;

    //Fetch all status
    @Override
    public List<ReferenceDataDTO> getAllStatus() {
        return statusRepository.findAll().stream()
                .map((status -> modelMapper.map(status, ReferenceDataDTO.class)))
                .toList();
    }

    // get employee status by id
    @Override
    public EmployeeStatus getEmployeeStatusById(Integer id) {
        return statusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee Status", "id", id.toString()));
    }

    // set active status
    @Override
    public void setEmployeeStatusActive(Long employeeId) {
        Employee employee = getEmployeeById(employeeId);
        setEmployeeStatus(employee, "Active");
        employeeRepository.save(employee);
    }

    // set deleted status
    @Override
    public void setEmployeeStatusDeleted(Long employeeId) {
        Employee employee = getEmployeeById(employeeId);
        setEmployeeStatus(employee, "Deleted");
        employeeRepository.save(employee);
    }

    // helper method to save status
    private void setEmployeeStatus(Employee employee, String statusName) {
        EmployeeStatus status = statusRepository.findByName(statusName);
        if (status == null) {
            throw new ResourceNotFoundException("Employee Status", "name", statusName);
        }
        employee.setEmployeeStatus(status);
        employee.setLastModifiedDateTime(LocalDateTime.now());
    }

    // get employee by id
    private Employee getEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeId", employeeId));
    }
}
