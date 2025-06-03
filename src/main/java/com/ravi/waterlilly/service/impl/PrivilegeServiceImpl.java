package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.exception.APIException;
import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.Module;
import com.ravi.waterlilly.model.Privilege;
import com.ravi.waterlilly.model.Role;
import com.ravi.waterlilly.payload.privilege.PrivilegeDTO;
import com.ravi.waterlilly.payload.privilege.PrivilegePageResponse;
import com.ravi.waterlilly.repository.PrivilegeRepository;
import com.ravi.waterlilly.service.ModuleService;
import com.ravi.waterlilly.service.PrivilegeService;
import com.ravi.waterlilly.service.RoleService;
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

import java.util.List;

// Privilege service implementation
@Service
@RequiredArgsConstructor
public class PrivilegeServiceImpl implements PrivilegeService {
    private final PrivilegeRepository privilegeRepository;
    private final ModelMapper modelMapper;
    private final RoleService roleService;
    private final ModuleService moduleService;
    private final PrivilegeUtils privilegeUtils;

    // get all privileges
    @Override
    public PrivilegePageResponse getAllPrivileges(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String searchQuery) {

        //check privileges
        privilegeUtils.privilegeCheck("Privilege", AppConstants.SELECT);

        // Determine the sorting order and sort by
        Sort sortAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        // Create a Pageable object for pagination and sorting
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortAndOrder);
        Page<Privilege> privilegePage = StringUtils.hasText(searchQuery)
                ? privilegeRepository.searchNonAdminPrivileges(searchQuery.trim(), pageable)
                : privilegeRepository.findAllNonAdminPrivileges(pageable);

        // Extract the list of privileges from the page
        List<Privilege> privileges = privilegePage.getContent();

        // Map the list of Privileges to a list of PrivilegeDTO using ModelMapper
        List<PrivilegeDTO> privilegeDTOS = privileges.stream()
                .map(privilege -> modelMapper.map(privilege, PrivilegeDTO.class))
                .toList();

        // Create a response object of privilege data
        return new PrivilegePageResponse(
                privilegeDTOS,
                privilegePage.getNumber(),
                privilegePage.getSize(),
                privilegePage.getTotalElements(),
                privilegePage.getTotalPages(),
                privilegePage.isLast()
        );
    }

    // get single privilege
    @Override
    public PrivilegeDTO getOnePrivilege(Long privilegeId) {
        //check privileges
        privilegeUtils.privilegeCheck("Privilege", AppConstants.SELECT);

        // find the privilege
        Privilege privilege = getPrivilegeById(privilegeId);

        return modelMapper.map(privilege, PrivilegeDTO.class);
    }

    // Add new privilege
    @Override
    public void addPrivilege(PrivilegeDTO privilegeDTO) {

        //check privileges
        privilegeUtils.privilegeCheck("Privilege", AppConstants.INSERT);

        // check privilege already in db
        validatePrivilegeUniqueness(privilegeDTO, null);

        Role role = roleService.getRoleById(privilegeDTO.getRole().getId());
        Module module = moduleService.getModuleById(privilegeDTO.getModule().getId());

        // map and save
        Privilege privilege = modelMapper.map(privilegeDTO, Privilege.class);
        privilege.setRole(role);
        privilege.setModule(module);
        privilegeRepository.save(privilege);
    }

    // Update existing privilege
    @Override
    @Transactional
    public void updatePrivilege(PrivilegeDTO privilegeDTO, Long privilegeId) {

        //check privileges
        privilegeUtils.privilegeCheck("Privilege", AppConstants.UPDATE);

        // search for privilege exist
        Privilege exisitingPrivilege = getPrivilegeById(privilegeId);

        // check privilege already in db
        validatePrivilegeUniqueness(privilegeDTO, privilegeId);

        Role role = roleService.getRoleById(privilegeDTO.getRole().getId());
        Module module = moduleService.getModuleById(privilegeDTO.getModule().getId());

        // map and save
        modelMapper.map(privilegeDTO, exisitingPrivilege);
        exisitingPrivilege.setId(privilegeId);
        exisitingPrivilege.setRole(role);
        exisitingPrivilege.setModule(module);

        privilegeRepository.save(exisitingPrivilege);
    }

    // Delete privilege - soft delete change operations to false
    @Override
    @Transactional
    public void deletePrivilege(Long privilegeId) {

        //check privileges
        privilegeUtils.privilegeCheck("Privilege", AppConstants.DELETE);

        // search for privilege exist
        Privilege exisitingPrivilege = getPrivilegeById(privilegeId);

        // change operations to false / not granted
        exisitingPrivilege.setSelectOp(false);
        exisitingPrivilege.setInsertOp(false);
        exisitingPrivilege.setDeleteOp(false);
        exisitingPrivilege.setUpdateOp(false);

        privilegeRepository.save(exisitingPrivilege);
    }

    // Helper method to check uniqueness of the privilege
    private void validatePrivilegeUniqueness(PrivilegeDTO privilegeDTO, Long privilegeId) {
        // check for duplicate privilege -> role and module
        Privilege duplicatePrivilege = privilegeRepository.findPrivilegeByRoleAndModule(
                privilegeDTO.getRole().getId(), privilegeDTO.getModule().getId()
        );

        if (duplicatePrivilege != null && !duplicatePrivilege.getId().equals(privilegeId)) {
            throw new APIException("The privilege already exists!");
        }
    }

    // method to get privilege by id
    private Privilege getPrivilegeById(Long privilegeId) {
        return privilegeRepository.findById(privilegeId)
                .orElseThrow(() -> new ResourceNotFoundException("Privilege", "privilegeId", privilegeId));
    }
}
