package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.payload.privilege.PrivilegeDTO;
import com.ravi.waterlilly.payload.privilege.PrivilegePageResponse;
import com.ravi.waterlilly.service.PrivilegeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// handles all privilege-related API requests.
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/privileges")
public class PrivilegeController {
    private final PrivilegeService privilegeService;

    // fetch all privileges list
    @GetMapping
    public ResponseEntity<PrivilegePageResponse> getAllPrivileges(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder,
            @RequestParam(name = "searchQuery", required = false) String searchQuery) {
        PrivilegePageResponse privileges = privilegeService.getAllPrivileges(pageNumber, pageSize, sortBy, sortOrder, searchQuery);
        return new ResponseEntity<>(privileges, HttpStatus.OK);
    }

    //fetch a single privilege
    @GetMapping("/{privilegeId}")
    public ResponseEntity<PrivilegeDTO> getOnePrivilege(@PathVariable Long privilegeId) {
        PrivilegeDTO privilegeDTO = privilegeService.getOnePrivilege(privilegeId);
        return new ResponseEntity<>(privilegeDTO, HttpStatus.OK);
    }


    // Add a privilege
    @PostMapping
    public ResponseEntity<?> addPrivilege(@Valid @RequestBody PrivilegeDTO privilegeDTO) {
        privilegeService.addPrivilege(privilegeDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // update a privilege
    @PutMapping("/{privilegeId}")
    public ResponseEntity<?> updateUser(@Valid @RequestBody PrivilegeDTO privilegeDTO, @PathVariable Long privilegeId) {
        privilegeService.updatePrivilege(privilegeDTO, privilegeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // delete a privilege
    @DeleteMapping("/{privilegeId}")
    public ResponseEntity<?> deletePrivilege(@PathVariable Long privilegeId) {
        privilegeService.deletePrivilege(privilegeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
