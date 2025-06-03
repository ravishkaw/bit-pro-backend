package com.ravi.waterlilly.payload.users;

import com.ravi.waterlilly.payload.employee.EmployeeUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

// DTO to use with payload
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPayloadDTO {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String photoPath;
    private String note;
    private EmployeeUserDTO employee;
    private String statusName;
    private Set<Integer> roleId;
}
