package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.payload.employee.EmployeeUserDTO;
import com.ravi.waterlilly.payload.employee.EmployeePayloadDTO;
import com.ravi.waterlilly.payload.employee.EmployeePageResponse;
import com.ravi.waterlilly.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Handle all employee API requests.
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService service;

    //Fetches a paginated list of employees
    @GetMapping
    public ResponseEntity<EmployeePageResponse> getAllEmployees(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder,
            @RequestParam(name = "searchQuery", required = false) String searchQuery

    ) {
        EmployeePageResponse response = service.getAllEmployees(pageNumber, pageSize, sortBy, sortOrder, searchQuery);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Gets employee list that without user accounts.
    @GetMapping("/without-user-accounts")
    public ResponseEntity<List<EmployeeUserDTO>> getEmployeesWithoutUserAccount() {
        List<EmployeeUserDTO> employeesWithoutUserAccount = service.getEmployeesWithoutUserAccount();
        return new ResponseEntity<>(employeesWithoutUserAccount, HttpStatus.OK);
    }

    // Get all employees without pagination
    @GetMapping("/get-all")
    public ResponseEntity<List<EmployeeUserDTO>> getEmployeesWithoutPagination() {
        List<EmployeeUserDTO> employees = service.getAllEmployeesWithoutPagination();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    //Gets details of a single employee.
    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeePayloadDTO> getAnEmployee(@PathVariable Long employeeId) {
        EmployeePayloadDTO employeeDTO = service.getAnEmployee(employeeId);
        return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
    }

    //Adds a new employee.
    @PostMapping
    public ResponseEntity<?> addEmployee(@Valid @RequestBody EmployeePayloadDTO employeeDTO) {
        service.addEmployee(employeeDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //Updates an existing employee.
    @PutMapping("/{employeeId}")
    public ResponseEntity<?> updateEmployee(@Valid @RequestBody EmployeePayloadDTO employeeDTO, @PathVariable Long employeeId) {
        service.updateEmployee(employeeDTO, employeeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Deletes an employee.
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long employeeId) {
        service.deleteEmployee(employeeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Restores a deleted employee.
    @PutMapping("/{employeeId}/restore")
    public ResponseEntity<?> restoreEmployee(@PathVariable Long employeeId) {
        service.restoreEmployee(employeeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
