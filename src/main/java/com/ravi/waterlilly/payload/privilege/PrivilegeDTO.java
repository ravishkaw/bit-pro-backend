package com.ravi.waterlilly.payload.privilege;

import com.ravi.waterlilly.model.Module;
import com.ravi.waterlilly.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO to get privileges
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivilegeDTO {

    private Long id;
    private Boolean selectOp;
    private Boolean insertOp;
    private Boolean deleteOp;
    private Boolean updateOp;
    private Role role;
    private Module module;
}

