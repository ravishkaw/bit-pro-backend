package com.ravi.waterlilly.payload.users;

import com.ravi.waterlilly.model.Role;
import com.ravi.waterlilly.model.Status;
import com.ravi.waterlilly.payload.employee.EmployeeUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

// DTO for users table.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTableDataDTO {

    private Long id;
    private String username;
    private String email;
    private String note;
    private String employeeFullName;
    private String statusName;
    private Set<Role> role;
}
