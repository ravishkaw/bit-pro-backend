package com.ravi.waterlilly.controller;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.payload.users.UserPageResponse;
import com.ravi.waterlilly.payload.users.UserPayloadDTO;
import com.ravi.waterlilly.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// handle all users API requests.
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    //Fetches a list of users.
    @GetMapping
    public ResponseEntity<UserPageResponse> getAllUsers(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder,
            @RequestParam(name = "searchQuery", required = false) String searchQuery
    ) {
        UserPageResponse userPageResponses = userService.getAllUsers(pageNumber, pageSize, sortBy, sortOrder, searchQuery);
        return new ResponseEntity<>(userPageResponses, HttpStatus.OK);
    }

    // Get details of single user (no password)
    @GetMapping("/{userId}")
    public ResponseEntity<UserPayloadDTO> getAnUser(@PathVariable Long userId) {
        UserPayloadDTO user = userService.getAnUser(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //Add new user
    @PostMapping
    public ResponseEntity<?> addUser(@Valid @RequestBody UserPayloadDTO userDTO) {
        userService.addUser(userDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // Update a user
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserPayloadDTO userPayloadDTO, @PathVariable Long userId) {
        userService.updateUser(userPayloadDTO, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Deletes an user.
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Restores a deleted user.
    @PutMapping("/{userId}/restore")
    public ResponseEntity<?> restoreUser(@PathVariable Long userId) {
        userService.restoreUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
