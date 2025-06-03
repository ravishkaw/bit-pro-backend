package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.exception.APIException;
import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.*;
import com.ravi.waterlilly.payload.employee.*;
import com.ravi.waterlilly.repository.*;
import com.ravi.waterlilly.service.*;
import com.ravi.waterlilly.utils.PrivilegeUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

// Implementation of Employee Service.
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    private final PrivilegeUtils privilegeUtils;
    private final TitleService titleService;
    private final IDTypeService iDTypeService;
    private final CivilStatusService civilStatusService;
    private final GenderService genderService;
    private final NationalityService nationalityService;
    private final DesignationService designationService;
    private final UserService userService;
    private final EmployeeStatusService employeeStatusService;

    // Get all employees with pagination, sorting, and search options
    @Override
    public EmployeePageResponse getAllEmployees(Integer pageNumber, Integer pageSize,
                                                String sortBy, String sortOrder, String searchQuery) {
        // Privilege check
        privilegeUtils.privilegeCheck("Employee", AppConstants.SELECT);

        // Determine the sorting order and sort by
        Sort sortAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        // Create a Pageable object for pagination and sorting with search query
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortAndOrder);
        Page<Employee> employeePage = StringUtils.hasText(searchQuery)
                ? employeeRepository.searchEmployees(searchQuery.trim(), pageable)
                : employeeRepository.findAll(pageable);

        // Extract the list of employees from the page
        List<Employee> employees = employeePage.getContent();

        // Map the list of Employees to a list of EmployeeTableDTO using ModelMapper
        List<EmployeeTableDataDTO> employeeTableDataDTOS = employees.stream()
                .map(employee -> modelMapper.map(employee, EmployeeTableDataDTO.class))
                .toList();

        // Create a response object of employee data
        return new EmployeePageResponse(
                employeeTableDataDTOS,
                employeePage.getNumber(),
                employeePage.getSize(),
                employeePage.getTotalElements(),
                employeePage.getTotalPages(),
                employeePage.isLast()
        );
    }

    //Gets an employee list that without user accounts.
    @Override
    public List<EmployeeUserDTO> getEmployeesWithoutUserAccount() {

        // privilege check
        privilegeUtils.privilegeCheck("Employee", AppConstants.SELECT);

        // Get a list of employees without user accounts
        List<Employee> employees = employeeRepository.findWithoutUserAccounts();

        // Map the list of Employees to a list of EmployeeForUserDTO using ModelMapper and return
        return employees.stream()
                .map(employee -> modelMapper.map(employee, EmployeeUserDTO.class))
                .toList();
    }

    // Get all employees without pagination
    @Override
    public List<EmployeeUserDTO> getAllEmployeesWithoutPagination() {

        // check privileges
        privilegeUtils.privilegeCheck("Employee", AppConstants.SELECT);

        return employeeRepository.findAll().stream()
                .map(employee -> modelMapper.map(employee, EmployeeUserDTO.class))
                .toList();
    }

    //Get one employee data
    @Override
    public EmployeePayloadDTO getAnEmployee(Long employeeId) {
        privilegeUtils.privilegeCheck("Employee", AppConstants.SELECT);

        // Get single employee details from id
        Employee employee = getEmployeeById(employeeId);

        return modelMapper.map(employee, EmployeePayloadDTO.class);
    }

    //Add new employee
    @Override
    public void addEmployee(EmployeePayloadDTO employeeDTO) {
        // Privilege check
        privilegeUtils.privilegeCheck("Employee", AppConstants.INSERT);

        // Check if the new Employee is already in database
        validateEmployeeUniqueness(employeeDTO, null);

        Employee newEmployee = new Employee();
        newEmployee.setEmpNo(employeeRepository.getNextEmpNO());
        updateEmployeeFields(newEmployee, employeeDTO);

        employeeRepository.save(newEmployee);
    }

    //Update employee
    @Transactional
    @Override
    public void updateEmployee(EmployeePayloadDTO employeeDTO, Long employeeId) {
        // Privilege check
        privilegeUtils.privilegeCheck("Employee", AppConstants.UPDATE);

        // Check if employee exists by the id
        Employee existingEmployee = getEmployeeById(employeeId);

        // user-related validation
        userService.validateEmployeeStatusChange(employeeId, employeeDTO.getEmployeeStatusId());

        // Check if the new Employee is already in database
        validateEmployeeUniqueness(employeeDTO, employeeId);

        // Update and save employee
        updateEmployeeFields(existingEmployee, employeeDTO);
        employeeRepository.save(existingEmployee);

        // Delegate user status synchronization to UserService
        userService.syncUserStatusWithEmployee(employeeId, existingEmployee.getEmployeeStatus().getName());
    }

    //Delete employee - soft delete change the status only.
    @Transactional
    @Override
    public void deleteEmployee(Long employeeId) {
        // Privilege check
        privilegeUtils.privilegeCheck("Employee", AppConstants.DELETE);

        // admin validation
        userService.validateEmployeeStatusChange(employeeId, null); // null indicates deletion

        // Get the deleted status and set it to the employee
        employeeStatusService.setEmployeeStatusDeleted(employeeId);

        // Delegate user status synchronization to UserService
        userService.syncUserStatusWithEmployee(employeeId, "Deleted");
    }

    //Restore option
    @Override
    public void restoreEmployee(Long employeeId) {
        // Privilege check
        privilegeUtils.privilegeCheck("Employee", AppConstants.UPDATE);

        employeeStatusService.setEmployeeStatusActive(employeeId);

        // Delegate user status synchronization to UserService
        userService.syncUserStatusWithEmployee(employeeId, "Active");
    }

    // get employee by id
    @Override
    public Employee getEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeId", employeeId));
    }

    // Helper method to update employee fields
    private void updateEmployeeFields(Employee employee, EmployeePayloadDTO employeeDTO) {
        employee.setTitle(titleService.getTitleById(employeeDTO.getTitleId()));
        employee.setFullName(employeeDTO.getFullName());
        employee.setCallingName(employeeDTO.getCallingName());
        employee.setIdNumber(employeeDTO.getIdNumber());
        employee.setDob(employeeDTO.getDob());
        employee.setNote(employeeDTO.getNote());
        employee.setAddress(employeeDTO.getAddress());
        employee.setMobileNo(employeeDTO.getMobileNo());
        employee.setEmail(employeeDTO.getEmail());
        employee.setEmergencyNo(employeeDTO.getEmergencyNo());
        employee.setIdType(iDTypeService.getIdTypeById(employeeDTO.getIdTypeId()));
        employee.setCivilStatus(civilStatusService.getCivilStatusById(employeeDTO.getCivilStatusId()));
        employee.setGender(genderService.getGenderById(employeeDTO.getGenderId()));
        employee.setNationality(nationalityService.getNationalityById(employeeDTO.getNationalityId()));
        employee.setDesignation(designationService.getDesignationById(employeeDTO.getDesignationId()));
        employee.setEmployeeStatus(employeeStatusService.getEmployeeStatusById(employeeDTO.getEmployeeStatusId()));

        LocalDateTime now = LocalDateTime.now();
        if (employee.getId() == null) {
            employee.setAddedDateTime(now);
        }
        employee.setLastModifiedDateTime(LocalDateTime.now());
    }

    // Helper method to check if the employee is in db
    private void validateEmployeeUniqueness(EmployeePayloadDTO employeeDTO, Long employeeId) {
        Nationality nationality = nationalityService.getNationalityById(employeeDTO.getNationalityId());
        IDType idType = iDTypeService.getIdTypeById(employeeDTO.getIdTypeId());

        // Check employee exist with same nationality, id type and id number
        Employee checkDuplicatedEmployee = employeeRepository.findEmployeeByNationalityIdTypeIdNumber(
                nationality.getId(), idType.getId(), employeeDTO.getIdNumber()
        );

        if (checkDuplicatedEmployee != null && !checkDuplicatedEmployee.getId().equals(employeeId)) {
            throw new APIException(
                    String.format("An employee with %s '%s' from %s already exists!",
                            idType.getName(),
                            employeeDTO.getIdNumber(),
                            nationality.getName()
                    )
            );
        }

        // Check existing employee with email. Also exclude same employee id
        Employee existingEmployeeEmail = employeeRepository.findEmployeeByEmail(employeeDTO.getEmail());
        if (existingEmployeeEmail != null && !existingEmployeeEmail.getId().equals(employeeId)) {
            throw new APIException("An employee with email " + employeeDTO.getEmail() + " already exists!");
        }

        // Check existing employee with mobile number. Also exclude same employee id
        Employee existingEmployeeMobileNo = employeeRepository.findEmployeeByMobileNo(employeeDTO.getMobileNo());
        if (existingEmployeeMobileNo != null && !existingEmployeeMobileNo.getId().equals(employeeId)) {
            throw new APIException("An employee with mobile number " + employeeDTO.getMobileNo() + " already exists!");
        }
    }
}