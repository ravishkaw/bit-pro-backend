package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.Role;
import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.repository.RoleRepository;
import com.ravi.waterlilly.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

// Implementation of RoleService.
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    public final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    // Get all roles
    @Override
    public List<ReferenceDataDTO> getAllRoles() {

        //get roles, map and return
        return roleRepository.findAll().stream()
                .map((role -> modelMapper.map(role, ReferenceDataDTO.class)))
                .toList();
    }

    // get role by id
    @Override
    public Role getRoleById(Integer id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id.toString()));
    }
}
