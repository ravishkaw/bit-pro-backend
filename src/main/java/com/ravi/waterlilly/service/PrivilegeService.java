package com.ravi.waterlilly.service;

import com.ravi.waterlilly.payload.privilege.PrivilegeDTO;
import com.ravi.waterlilly.payload.privilege.PrivilegePageResponse;

// Service interface for managing privilege service.
public interface PrivilegeService {

    // fetch all privileges
    PrivilegePageResponse getAllPrivileges(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String searchQuery);

    // Get single privilege
    PrivilegeDTO getOnePrivilege(Long privilegeId);

    // Add new privilege
    void addPrivilege(PrivilegeDTO privilegeDTO);

    // Update privilege
    void updatePrivilege(PrivilegeDTO privilegeDTO, Long privilegeId);

    // Delete a privilege
    void deletePrivilege(Long privilegeId);
}
