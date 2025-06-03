package com.ravi.waterlilly.repository;

import com.ravi.waterlilly.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// Employee repository
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Finds an employee by nationality, idType and idNumber
    @Query("SELECT e FROM Employee e WHERE e.nationality.id=?1 AND e.idType.id=?2 AND e.idNumber=?3")
    Employee findEmployeeByNationalityIdTypeIdNumber(int nationality, int idType, String idNumber);

    // Find employee by email
    @Query("SELECT e FROM Employee e WHERE e.email=?1")
    Employee findEmployeeByEmail(String email);

    // Find employee by Mobile No
    @Query("SELECT e FROM Employee e WHERE e.mobileNo=?1")
    Employee findEmployeeByMobileNo(String mobileNo);

    //Generates the next employee number - format 'EMP00001'.
    @Query(value = """
            SELECT CONCAT('EMP', LPAD(COALESCE(MAX(CAST(SUBSTRING(emp_no, 4) AS UNSIGNED)), 0) + 1, 5, '0')) AS emp_no
            FROM employee e;
            """, nativeQuery = true)
    String getNextEmpNO();

    // Get employees without User account
    @Query(value = "SELECT e FROM Employee e WHERE e.id NOT IN (SELECT u.employee.id FROM User u)")
    List<Employee> findWithoutUserAccounts();

    // Search query
    @Query("SELECT e FROM Employee e WHERE " +
            "LOWER(e.fullName) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(e.callingName) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(e.idNumber) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(e.mobileNo) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(e.email) LIKE LOWER(CONCAT('%', :searchQuery, '%'))")
    Page<Employee> searchEmployees(@Param("searchQuery") String searchQuery, Pageable pageable);

}