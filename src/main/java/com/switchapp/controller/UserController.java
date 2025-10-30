package com.switchapp.controller;

import com.switchapp.model.User;
import com.switchapp.service.UserService;
import com.switchapp.util.ResponseJson;
import com.switchapp.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'GUEST', 'MANAGER')")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<ResponseJson> createUser(@RequestBody User user) {
        try {
            return RestUtil.response(HttpStatus.CREATED, "User created successfully", userService.createUser(user));
        } catch (Exception e) {
            return RestUtil.response(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create user", null);
        }
    }

    // Get All Users with optional filter
    @GetMapping
    public ResponseEntity<ResponseJson> getAllUsers(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Boolean enabled) {
        try {
            return RestUtil.response(HttpStatus.OK, "Users fetched successfully", userService.getAllUsers(email, enabled));
        } catch (Exception e) {
            return RestUtil.response(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch users", null);
        }
    }

    // Get User by Username (PathVariable)
    @GetMapping("/{username}")
    public ResponseEntity<ResponseJson> getUser(@PathVariable String username) {
        return userService.getUserByUsername(username)
                .map(user -> RestUtil.response(HttpStatus.OK, "User found", user))
                .orElse(RestUtil.response(HttpStatus.NOT_FOUND, "User not found", null));
    }

    // Update User by Username (PathVariable)
    @PutMapping("/{username}")
    public ResponseEntity<ResponseJson> updateUser(@PathVariable String username, @RequestBody User user) {
        return userService.updateUser(username, user)
                .map(updated -> RestUtil.response(HttpStatus.OK, "User updated", updated))
                .orElse(RestUtil.response(HttpStatus.NOT_FOUND, "User not found", null));
    }

    // Delete User by Username (PathVariable)
    @DeleteMapping("/{username}")
    public ResponseEntity<ResponseJson> deleteUser(@PathVariable String username) {
        if (userService.deleteUser(username)) {
            return RestUtil.response(HttpStatus.NO_CONTENT, "User deleted", null);
        }
        return RestUtil.response(HttpStatus.NOT_FOUND, "User not found", null);
    }
}
