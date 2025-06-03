package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.model.Employee;
import com.ravi.waterlilly.model.Role;
import com.ravi.waterlilly.model.User;
import com.ravi.waterlilly.payload.module.ModuleWithPrivilegeDTO;
import com.ravi.waterlilly.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Authentication related operation controller
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final DesignationRepository designationRepository;
    private final EmployeeStatusRepository employeeStatusRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModuleRepository moduleRepository;
    private final IDTypeRepository idTypeRepository;
    private final NationalityRepository nationalityRepository;
    private final GenderRepository genderRepository;
    private final CivilStatusRepository civilStatusRepository;
    private final StatusRepository statusRepository;


    @PostMapping("/signin")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response) {
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // Store authentication in session
            SecurityContextHolder.getContext().setAuthentication(authentication);
            HttpSession session = request.getSession();
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            // get the user details
            User user = userRepository.findByUsername(username);

            // get the user specific privileges
            List<String> module = moduleRepository.findAccessibleModulesForUser(username);

            // get the user specific module with privileges
            List<ModuleWithPrivilegeDTO> privileges = moduleRepository.findModulePrivilegesForUser(username);

            return ResponseEntity.ok(Map.of(
                    "userId", user.getId(),
                    "roles", user.getRole(),
                    "sessionId", session.getId(),
                    "privilegedModules", module,
                    "privileges", privileges,
                    "message", "Login successful"));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        }
    }

    @GetMapping("/session")
    public ResponseEntity<?> checkSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Check if session or authentication is invalid
        if (session == null || session.getAttribute("SPRING_SECURITY_CONTEXT") == null || auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("message", "Session expired"));
        }

        // Get username from authentication
        String username = auth.getName();

        // Fetch user data from repository
        User user = userRepository.findByUsername(username);

        // get the user specific privileges
        List<String> module = moduleRepository.findAccessibleModulesForUser(username);

        // get the user specific module with privileges
        List<ModuleWithPrivilegeDTO> privileges = moduleRepository.findModulePrivilegesForUser(username);


        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid user data"));
        }

        return ResponseEntity.ok(Map.of(
                "userId", user.getId(),
                "roles", user.getRole(),
                "privilegedModules", module,
                "privileges", privileges,
                "sessionId", session.getId()
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    @Transactional
    @GetMapping("/create-admin-employee")
    public String createAdminWithEmployee() {

        // Create Employee
        Employee employee = new Employee();
        employee.setEmpNo("EMP00001");
        employee.setFullName("Admin User");
        employee.setCallingName("Admin");
        employee.setIdNumber("123456789V");
        employee.setDob(LocalDate.of(1990, 1, 1));
        employee.setNote("Admin Employee");
        employee.setAddress("Colombo, Sri Lanka");
        employee.setMobileNo("+94711234567");
        employee.setEmail("admin@gmail.com");
        employee.setEmergencyNo("+94710000000");
        employee.setIdType(idTypeRepository.getReferenceById(1));
        employee.setNationality(nationalityRepository.getReferenceById(1));
        employee.setGender(genderRepository.getReferenceById(1));
        employee.setCivilStatus(civilStatusRepository.getReferenceById(1));
        employee.setEmployeeStatus(employeeStatusRepository.getReferenceById(1));
        employee.setDesignation(designationRepository.getReferenceById(1));
        employee.setAddedDateTime(LocalDateTime.now());

        Employee savedEmployee = employeeRepository.save(employee);

        // Create User Account
        User adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setPassword(bCryptPasswordEncoder.encode("Admin123@"));
        adminUser.setEmail("admin@gmail.com");
        adminUser.setEmployee(savedEmployee);
        adminUser.setStatus(statusRepository.getReferenceById(1));
        adminUser.setAddedDatetime(LocalDateTime.now());

        // Assign Admin Role
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.getReferenceById(1));
        adminUser.setRole(roles);

        userRepository.save(adminUser);

        return "Admin employee and user account created successfully!";
    }
}
