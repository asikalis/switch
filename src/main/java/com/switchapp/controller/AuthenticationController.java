package com.switchapp.controller;

import com.switchapp.model.User;
import com.switchapp.service.UserService;
import com.switchapp.util.LoginRequest;
import com.switchapp.util.ResponseJson;
import com.switchapp.util.RestUtil;
import com.switchapp.util.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    // SignUp endpoint
    @PostMapping("/signup")
    public ResponseEntity<ResponseJson> registerUser(@RequestBody SignupRequest signupRequest) {
        try {
            User user = userService.registerUser(signupRequest);
            return RestUtil.response(HttpStatus.CREATED, "User registered successfully: " + user.getUsername(), user);
        } catch (RuntimeException e) {
            return RestUtil.response(HttpStatus.BAD_REQUEST, "Registration failed. Please contact the administrator.", null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseJson> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            return RestUtil.response(HttpStatus.OK, "Login successful", userService.authenticateUser(loginRequest.email, loginRequest.password));
        } catch (RuntimeException e) {
            return RestUtil.response(HttpStatus.UNAUTHORIZED, "Login failed: " + e.getMessage(), null);
        }
    }
}
