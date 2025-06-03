package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.payload.module.ModuleWithPrivilegeDTO;
import com.ravi.waterlilly.repository.ModuleRepository;
import com.ravi.waterlilly.service.ModuleService;
import com.ravi.waterlilly.model.Module;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

// Implementation of Module service
@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {
    private final ModuleRepository moduleRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<ReferenceDataDTO> getAllModules() {

        // find map and return module list
        return moduleRepository.findAll().stream()
                .map(module -> modelMapper.map(module, ReferenceDataDTO.class))
                .toList();
    }

    // Get modules specific to role without existing privileges
    @Override
    public List<ReferenceDataDTO> getModulesWithoutPrivileges(Integer roleId) {

        // find map and return module list
        return moduleRepository.findModulesWithoutPrivilegesForRole(roleId).stream()
                .map(module -> modelMapper.map(module, ReferenceDataDTO.class))
                .toList();
    }

    // Get privileged modules to a user account
    @Override
    public List<String> getPrivilegedModulesForUsername(String username) {

        // find map and return module list
        return moduleRepository.findAccessibleModulesForUser(username);
    }

    // Get privileged modules to a user account with the privileges
    @Override
    public List<ModuleWithPrivilegeDTO> getPrivilegedModulesWithPrivilegesForUsername(String username) {

        // find and return module with privileges
        return moduleRepository.findModulePrivilegesForUser(username);
    }

    // get module by id
    @Override
    public Module getModuleById(Integer id) {
        return moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module", "id", id.toString()));
    }
}