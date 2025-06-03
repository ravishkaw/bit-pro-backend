package com.ravi.waterlilly.payload.module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO to get modules with privilege
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleWithPrivilegeDTO {
    private String module_name;
    private Boolean select_privilege;
    private Boolean insert_privilege;
    private Boolean update_privilege;
    private Boolean delete_privilege;
}
