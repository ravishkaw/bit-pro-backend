package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.Module;
import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.payload.module.ModuleWithPrivilegeDTO;

import java.util.List;

// Service interface of module service
public interface ModuleService {
    // Get all modules
    List<ReferenceDataDTO> getAllModules();

    // Get modules specific to role without privileges
    List<ReferenceDataDTO> getModulesWithoutPrivileges(Integer roleId);

    // Get privileged modules to a user account
    List<String> getPrivilegedModulesForUsername(String username);

    // Get privileged modules to a user account with the privileges
    List<ModuleWithPrivilegeDTO> getPrivilegedModulesWithPrivilegesForUsername(String username);

    // get module by id
    Module getModuleById(Integer id);
}
