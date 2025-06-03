package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.Employee;
import com.ravi.waterlilly.payload.employee.EmployeeUserDTO;
import com.ravi.waterlilly.payload.employee.EmployeePayloadDTO;
import com.ravi.waterlilly.payload.employee.EmployeePageResponse;

import java.util.List;

// Service interface for managing employees.
public interface EmployeeService {

    //Retrieves a paginated list of employees.
    EmployeePageResponse getAllEmployees(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String searchQuery);

    //Gets employee list that without user accounts.
    List<EmployeeUserDTO> getEmployeesWithoutUserAccount();

    // Get all employees without pagination
    List<EmployeeUserDTO> getAllEmployeesWithoutPagination();

    //Gets details of a single employee.
    EmployeePayloadDTO getAnEmployee(Long employeeId);

    // Adds a new employee.
    void addEmployee(EmployeePayloadDTO employeeDTO);

    //Updates an existing employee.
    void updateEmployee(EmployeePayloadDTO employeeDTO, Long employeeId);

    //Deletes an employee.
    void deleteEmployee(Long employeeId);

    //Restores a deleted employee.
    void restoreEmployee(Long employeeId);

    // get employee by id
    Employee getEmployeeById(Long employeeId);
}
