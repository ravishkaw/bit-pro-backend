package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// handles all system role related API requests.
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/roles")
public class RoleController {
    public final RoleService roleService;

    //Get all roles of user
    @GetMapping
    public ResponseEntity<List<ReferenceDataDTO>> getAll() {
        List<ReferenceDataDTO> roleDTOS = roleService.getAllRoles();
        return new ResponseEntity<>(roleDTOS, HttpStatus.OK);
    }
}
