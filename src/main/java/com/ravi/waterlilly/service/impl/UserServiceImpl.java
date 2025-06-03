package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.exception.APIException;
import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.*;
import com.ravi.waterlilly.payload.users.UserTableDataDTO;
import com.ravi.waterlilly.payload.users.UserPageResponse;
import com.ravi.waterlilly.payload.users.UserPayloadDTO;
import com.ravi.waterlilly.repository.EmployeeRepository;
import com.ravi.waterlilly.repository.UserRepository;
import com.ravi.waterlilly.service.*;
import com.ravi.waterlilly.utils.PrivilegeUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// Implementation of UserService.
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PrivilegeUtils privilegeUtils;
    private final StatusService statusService;
    private final RoleService roleService;
    private final EmployeeStatusService employeeStatusService;
    private final EmployeeRepository employeeRepository;

    //Get all users
    @Override
    public UserPageResponse getAllUsers(Integer pageNumber, Integer pageSize,
                                        String sortBy, String sortOrder, String searchQuery) {

        //check privileges
        privilegeUtils.privilegeCheck("User", AppConstants.SELECT);

        // Determine the sorting order and sort by
        Sort sortAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        // Create a Pageable object for pagination and sorting
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortAndOrder);
        Page<User> usersPage = StringUtils.hasText(searchQuery)
                ? userRepository.searchUsers(searchQuery.trim(), pageable)
                : userRepository.findAll(pageable);

        // Extract the list of users from the page
        List<User> users = usersPage.getContent();

        // Map the list of Users to a list of usersTableDTOS using ModelMapper
        List<UserTableDataDTO> userTableDataDTOS = users.stream()
                .map(user -> modelMapper.map(user, UserTableDataDTO.class))
                .toList();

        // Create a response object of users data
        return new UserPageResponse(
                userTableDataDTOS,
                usersPage.getNumber(),
                usersPage.getSize(),
                usersPage.getTotalElements(),
                usersPage.getTotalPages(),
                usersPage.isLast()
        );
    }

    // Get a single user data
    @Override
    public UserPayloadDTO getAnUser(Long userId) {
        //check privileges
        privilegeUtils.privilegeCheck("User", AppConstants.SELECT);

        //Get single employee details from id
        User existingUser = getUserById(userId);

        UserPayloadDTO user = modelMapper.map(existingUser, UserPayloadDTO.class);

        user.setPassword(null);
        Set<Integer> roles = existingUser.getRole().stream()
                .map(role -> role.getId())
                .collect(Collectors.toSet());
        user.setRoleId(roles);
        return user;
    }

    //Add new user
    @Override
    public void addUser(UserPayloadDTO userPayloadDTO) {
        //check privileges
        privilegeUtils.privilegeCheck("User", AppConstants.INSERT);

        // check if the new user is already in database
        validateUserUniqueness(userPayloadDTO, null);

        //map and save further details then save
        User newUser = new User();
        updateUserFields(newUser, userPayloadDTO);
        encryptPassword(userPayloadDTO, newUser);
        newUser.setAddedDatetime(LocalDateTime.now());

        userRepository.save(newUser);
    }

    // Update user
    @Transactional
    @Override
    public void updateUser(UserPayloadDTO userPayloadDTO, Long userId) {

        //check privileges
        privilegeUtils.privilegeCheck("User", AppConstants.UPDATE);

        //check if user exists
        User existingUser = getUserById(userId);

        if (hasAdminRole(existingUser)) {
            // If status is being changed to inactive/deleted
            if (!userPayloadDTO.getStatusName().equals("Active")) {
                validateAdminDeletion(userId);
            }
        }

        // check if the user is already in database
        validateUserUniqueness(userPayloadDTO, userId);

        // store existing user password
        String existingUserPassword = existingUser.getPassword();
        //set details
        updateUserFields(existingUser, userPayloadDTO);

        if (userPayloadDTO.getPassword() != null && !userPayloadDTO.getPassword().trim().isEmpty()) {
            encryptPassword(userPayloadDTO, existingUser);
        } else {
            existingUser.setPassword(existingUserPassword);
        }
        userRepository.save(existingUser);
    }

    //Delete user - soft delete
    @Transactional
    @Override
    public void deleteUser(Long userId) {

        //check privileges
        privilegeUtils.privilegeCheck("User", AppConstants.DELETE);

        // validate admin deletion
        validateAdminDeletion(userId);

        //check if user exists
        User user = getUserById(userId);

        user.setStatus(statusService.getDeletedStatus());
        userRepository.save(user);
    }

    // restore a user
    @Override
    public void restoreUser(Long userId) {

        //check privileges
        privilegeUtils.privilegeCheck("User", AppConstants.DELETE);

        //check if user exists
        User user = getUserById(userId);

        user.setStatus(statusService.getActiveStatus());
        userRepository.save(user);
    }

    // method to get user by id
    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "user id", userId));
    }

    // Validate admin deletion by user ID
    @Override
    public void validateAdminDeletion(Long userId) {
        User currentUser = privilegeUtils.getCurrentUser();

        if (currentUser.getId().equals(userId)) {
            throw new APIException("You cannot delete or deactivate your own admin account");
        }

        User targetUser = getUserById(userId);

        if (hasAdminRole(targetUser)) {
            Long activeAdminCount = userRepository.countAdmins();
            if (activeAdminCount <= 1) {
                throw new APIException("Cannot delete or deactivate the admin account. Make another admin account active first.");
            }
        }
    }

    // Validate employee status change - handles both status updates and deletions
    @Override
    public void validateEmployeeStatusChange(Long employeeId, Integer newStatusId) {
        User currentUser = privilegeUtils.getCurrentUser();

        // Prevent self-modification
        if (currentUser.getEmployee().getId().equals(employeeId)) {
            throw new APIException("You cannot modify your own account status");
        }

        User targetUser = findUserByEmployeeId(employeeId);
        if (targetUser != null && hasAdminRole(targetUser)) {
            if (newStatusId == null) {
                // Deletion case
                validateAdminDeletion(targetUser.getId());
            } else {
                // Status change case - check if new status would deactivate admin
                EmployeeStatus newStatus = employeeStatusService.getEmployeeStatusById(newStatusId);
                if (!isActiveEmployeeStatus(newStatus.getName())) {
                    validateAdminDeletion(targetUser.getId());
                }
            }
        }
    }

    // Synchronize user status with employee status
    @Override
    public void syncUserStatusWithEmployee(Long employeeId, String employeeStatusName) {
        User user = findUserByEmployeeId(employeeId);
        if (user != null) {
            Status newUserStatus = mapEmployeeStatusToUserStatus(employeeStatusName);
            user.setStatus(newUserStatus);
            userRepository.save(user);
        }
    }

    // Helper method to update user fields
    private void updateUserFields(User user, UserPayloadDTO userPayloadDTO) {
        user.setUsername(userPayloadDTO.getUsername());
        user.setPassword(userPayloadDTO.getPassword());
        user.setEmail(userPayloadDTO.getEmail());
        user.setPhoto(userPayloadDTO.getPhotoPath());
        user.setNote(userPayloadDTO.getNote());
        user.setEmployee(fetchEmployee(userPayloadDTO.getEmployee().getId()));
        user.setStatus(statusService.getStatusByName(userPayloadDTO.getStatusName()));
        user.setRole(fetchRoles(userPayloadDTO.getRoleId()));
    }

    // Helper method to check if the user is in db
    private void validateUserUniqueness(UserPayloadDTO userPayloadDTO, Long userId) {

        //Check for duplicated named user
        User duplicatedUserNamedUser = userRepository.findByUsername(userPayloadDTO.getUsername());
        if (duplicatedUserNamedUser != null && !duplicatedUserNamedUser.getId().equals(userId)) {
            throw new APIException("User with username " + duplicatedUserNamedUser.getUsername() + " already exists!");
        }

        //check if user exists with email
        User existingUserEmail = userRepository.findByEmail(userPayloadDTO.getEmail());
        if (existingUserEmail != null && !existingUserEmail.getId().equals(userId))
            throw new APIException("User with " + existingUserEmail.getEmail() + " already exists!");
    }

    // Helper method to encrypt password
    private void encryptPassword(UserPayloadDTO userPayloadDTO, User user) {
        //Check password provided. if then encrypt
        if (userPayloadDTO.getPassword() != null && !userPayloadDTO.getPassword().trim().isEmpty()) {
            user.setPassword(bCryptPasswordEncoder.encode(userPayloadDTO.getPassword()));
        } else {
            throw new APIException("Password cannot be null or empty");
        }
    }

    // method to get user by employee id
    private User findUserByEmployeeId(Long employeeId) {
        return userRepository.findUserByEmployeeId(employeeId);
    }

    // Helper method to fetch employee - now uses EmployeeService instead of repository
    private Employee fetchEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeId", employeeId));
    }

    // method to check if user has admin role
    private boolean hasAdminRole(User user) {
        return user.getRole().stream()
                .anyMatch(role -> role.getName().equals("Admin"));
    }

    // Helper method to fetch roles
    private Set<Role> fetchRoles(Set<Integer> roles) {
        return roles.stream()
                .map(role -> roleService.getRoleById(role))
                .collect(Collectors.toSet());
    }

    // Helper method to check if employee status is considered active
    private boolean isActiveEmployeeStatus(String statusName) {
        return "Active".equals(statusName) || "On Leave".equals(statusName);
    }

    // Helper method to map employee status to user status
    private Status mapEmployeeStatusToUserStatus(String employeeStatusName) {
        return switch (employeeStatusName) {
            case "Active", "On Leave" -> statusService.getActiveStatus();
            case "Deleted" -> statusService.getDeletedStatus();
            default -> statusService.getInactiveStatus();
        };
    }

//    //Delete user - Hard delete
//    @Override
//    public void deleteUser(Long userId) {
//        Users user = usersRepository.findById(userId).
//                orElseThrow(() -> new ResourceNotFoundException("User", "user id", userId));
//
//        // Remove all role associations
//        user.getRole().clear();
//        usersRepository.save(user);
//
//        usersRepository.delete(user);
//    }
}