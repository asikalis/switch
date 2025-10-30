package com.switchapp.service;

import com.switchapp.model.User;
import com.switchapp.util.SignupRequest;
import com.switchapp.util.UserDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {

    Map<String, Object> authenticateUser(String email, String password);

    User registerUser(SignupRequest signupRequest);

    User createUser(User user);

    List<UserDto> getAllUsers(String email, Boolean enabled);

    Optional<UserDto> getUserByUsername(String username);

    Optional<UserDto> updateUser(String username, User user);

    boolean deleteUser(String username);
}
