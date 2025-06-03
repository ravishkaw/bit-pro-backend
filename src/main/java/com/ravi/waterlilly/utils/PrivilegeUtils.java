package com.ravi.waterlilly.utils;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.exception.APIException;
import com.ravi.waterlilly.model.User;
import com.ravi.waterlilly.payload.privilege.PrivilegeOfModuleDTO;
import com.ravi.waterlilly.repository.PrivilegeRepository;
import com.ravi.waterlilly.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class PrivilegeUtils {
    private final PrivilegeRepository privilegeRepository;
    private final UserRepository userRepository;

    public PrivilegeUtils(PrivilegeRepository privilegeRepository, UserRepository userRepository) {
        this.privilegeRepository = privilegeRepository;
        this.userRepository = userRepository;
    }

    // Privilege check function
    public void privilegeCheck(String moduleName, String privilege) {
        // get the authentication object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // get username from authentication object
        String username = authentication.getName();

        // fetch privileges for the passed module
        PrivilegeOfModuleDTO userPrivilegesForModule = privilegeRepository.findUserPrivilegesForModule(username, moduleName);

        if (userPrivilegesForModule == null) {
            throw new APIException("You don't have any privileges for " + moduleName + "!");
        }

        switch (privilege) {
            // Select privilege
            case AppConstants.SELECT:
                if (!userPrivilegesForModule.isSelectPrivilege()) {
                    throw new APIException("You don't have SELECT privilege for " + moduleName + "!");
                }
                break;

            // Insert privilege
            case AppConstants.INSERT:
                if (!userPrivilegesForModule.isInsertPrivilege()) {
                    throw new APIException("You don't have INSERT privilege for " + moduleName + "!");
                }
                break;

            // Update privilege
            case AppConstants.UPDATE:
                if (!userPrivilegesForModule.isUpdatePrivilege()) {
                    throw new APIException("You don't have UPDATE privilege for " + moduleName + "!");
                }
                break;

            // Delete privilege
            case AppConstants.DELETE:
                if (!userPrivilegesForModule.isDeletePrivilege()) {
                    throw new APIException("You don't have DELETE privilege for " + moduleName + "!");
                }
                break;
            default:
                throw new APIException("Invalid privilege type: " + privilege);
        }
    }

    // Get current logged in user
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            return userRepository.findByUsername(username);
        }
        throw new APIException("No authenticated user found!");
    }
}
