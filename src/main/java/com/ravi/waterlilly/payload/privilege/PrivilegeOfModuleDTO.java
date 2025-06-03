package com.ravi.waterlilly.payload.privilege;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// dto to send privileges of a module
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrivilegeOfModuleDTO {
    private boolean selectPrivilege;
    private boolean insertPrivilege;
    private boolean updatePrivilege;
    private boolean deletePrivilege;
}
