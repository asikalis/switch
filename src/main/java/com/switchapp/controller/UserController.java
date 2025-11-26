package com.switchapp.controller;

import com.switchapp.model.User;
import com.switchapp.service.UserService;
import com.switchapp.util.ResponseJson;
import com.switchapp.util.RestUtil;
import com.switchapp.dto.UserDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseJson> createUser(@Valid @RequestBody User user) {
        try {
            Object createdUser = userService.createUser(user);
            return RestUtil.response(HttpStatus.CREATED, "User created successfully", createdUser);
        } catch (RuntimeException ex) {
            return RestUtil.response(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        } catch (Exception ex) {
            return RestUtil.response(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create user", null);
        }
    }

    // Get All Users with optional filter
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseJson> getAllUsers() {
        try {
            return RestUtil.response(HttpStatus.OK, "Users fetched successfully", userService.getAllUsers());
        } catch (Exception e) {
            return RestUtil.response(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch users", null);
        }
    }

    // Get User by Username (PathVariable)
    @GetMapping("/{username}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'GUEST', 'MANAGER')")
    public ResponseEntity<ResponseJson> getUser(@PathVariable String username) {
        return userService.getUserByUsername(username)
                .map(user -> RestUtil.response(HttpStatus.OK, "User found", user))
                .orElse(RestUtil.response(HttpStatus.NOT_FOUND, "User not found", null));
    }

    // Update User by Username (PathVariable)
    @PutMapping("/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseJson> updateUser(@PathVariable String username, @RequestBody UserDto user) {
        return userService.updateUser(username, user)
                .map(updated -> RestUtil.response(HttpStatus.OK, "User updated", updated))
                .orElse(RestUtil.response(HttpStatus.NOT_FOUND, "User not found", null));
    }

    // Delete User by Username (PathVariable)
    @DeleteMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseJson> deleteUser(@PathVariable String username) {
        if (userService.deleteUser(username)) {
            return RestUtil.response(HttpStatus.NO_CONTENT, "User deleted", null);
        }
        return RestUtil.response(HttpStatus.NOT_FOUND, "User not found", null);
    }

    @PutMapping("/{username}/reset-password")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER')")
    public ResponseEntity<ResponseJson> resetPassword(
            @PathVariable String username,
            @RequestBody String newPassword) {
        boolean success = userService.resetPassword(username, newPassword);
        if (success) {
            return RestUtil.response(HttpStatus.OK, "Password reset successfully", null);
        }
        return RestUtil.response(HttpStatus.NOT_FOUND, "User not found", null);
    }
}
