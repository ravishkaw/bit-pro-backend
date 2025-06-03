package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.Role;
import com.ravi.waterlilly.payload.ReferenceDataDTO;

import java.util.List;

// Service interface for managing role service.
public interface RoleService {
    // get all roles
    List<ReferenceDataDTO> getAllRoles();

    // get role by id
    Role getRoleById(Integer id);
}
