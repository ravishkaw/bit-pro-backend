package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.payload.module.ModuleWithPrivilegeDTO;
import com.ravi.waterlilly.service.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// handle module related api requests
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/modules")
public class ModuleController {
    private final ModuleService moduleService;

    // Get all modules
    @GetMapping
    public ResponseEntity<List<ReferenceDataDTO>> getAllModules() {
        List<ReferenceDataDTO> moduleDTOS = moduleService.getAllModules();
        return new ResponseEntity<>(moduleDTOS, HttpStatus.OK);
    }

    // Get modules specific to role without privileges
    @GetMapping("/without-privileges")
    public ResponseEntity<List<ReferenceDataDTO>> getModulesWithoutPrivileges(
            @RequestParam(name = "roleId") Integer roleId
    ) {
        List<ReferenceDataDTO> moduleDTOS = moduleService.getModulesWithoutPrivileges(roleId);
        return new ResponseEntity<>(moduleDTOS, HttpStatus.OK);
    }

    // Get privileged modules to a user account
    @GetMapping("/user-privileged-modules")
    public ResponseEntity<List<String>> getPrivilegedModulesForUsername(@RequestParam(name = "username") String username) {
        List<String> moduleDTOS = moduleService.getPrivilegedModulesForUsername(username);
        return new ResponseEntity<>(moduleDTOS, HttpStatus.OK);
    }

    // Get privileged modules to a user account with the privileges
    @GetMapping("/with-privileges")
    public ResponseEntity<List<ModuleWithPrivilegeDTO>> getPrivilegedModulesWithPrivilegesForUsername(@RequestParam(name = "username") String username) {
        List<ModuleWithPrivilegeDTO> modules = moduleService.getPrivilegedModulesWithPrivilegesForUsername(username);
        return new ResponseEntity<>(modules, HttpStatus.OK);
    }
}
