package com.ravi.waterlilly.service;

import com.ravi.waterlilly.model.User;
import com.ravi.waterlilly.payload.users.UserPageResponse;
import com.ravi.waterlilly.payload.users.UserPayloadDTO;
import jakarta.validation.Valid;

//  Service interface for managing users.
public interface UserService {

    // Fetches a list of users.
    UserPageResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String searchQuery);

    // Fetch a single user
    UserPayloadDTO getAnUser(Long userId);

    //Add User
    void addUser(UserPayloadDTO userDTO);

    //Update user
    void updateUser(@Valid UserPayloadDTO userDTO, Long userId);

    //Delete user
    void deleteUser(Long userId);

    // restore a user
    void restoreUser(Long userId);

    // Validate admin deletion by user ID
    void validateAdminDeletion(Long userId);

    // Validate employee status change - handles both status updates and deletions
    void validateEmployeeStatusChange(Long employeeId, Integer newStatusId);

    // Synchronize user status with employee status
    void syncUserStatusWithEmployee(Long employeeId, String employeeStatusName);

    // method to get user by id
    User getUserById(Long userId);
}
