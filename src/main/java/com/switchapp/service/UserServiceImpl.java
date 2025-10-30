package com.switchapp.service;

import com.switchapp.model.Role;
import com.switchapp.model.User;
import com.switchapp.repository.RoleRepository;
import com.switchapp.repository.UserRepository;
import com.switchapp.util.ConvertUtils;
import com.switchapp.util.SignupRequest;
import com.switchapp.util.UserDto;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtUtil;

    //login
    @Override
    public Map<String, Object> authenticateUser(String email, String password) {
        Map<String, Object> response = new HashMap<>();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        Map<String, Object> jwtClaims = new HashMap<>();
        jwtClaims.put("user_name", user.getUsername());
        jwtClaims.put("csr_pk", user.getId());
        List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
        jwtClaims.put("roles", roles);
        logger.info("JWT Info : {}", ConvertUtils.convertObjectToJson(jwtClaims));
        response.put("token", jwtUtil.generateToken(user.getUsername(), jwtClaims));
        response.put("user", ConvertUtils.userDtoFromUser(user));

        // Generate JWT token (implement JwtUtil accordingly)
        return response;
    }

    // SignUp
    @Override
    public User registerUser(SignupRequest signupRequest) {

        if (userRepository.existsByUsername(signupRequest.username)) {
            throw new RuntimeException("Username already taken");
        }
        if (userRepository.existsByEmail(signupRequest.email)) {
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setUsername(signupRequest.username);
        user.setEmail(signupRequest.email);
        user.setPassword(passwordEncoder.encode(signupRequest.password));

        Set<Role> roles = new HashSet<>();
        if (signupRequest.roles == null) {
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            roles.add(userRole);
        } else {
            signupRequest.roles.forEach(role -> {
                Role r = roleRepository.findByName(role)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + role));
                roles.add(r);
            });
        }
        user.setRoles(roles);

        return userRepository.save(user);
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<UserDto> getAllUsers(String email, Boolean enabled) {
        List<User> users = new ArrayList<>();
        if (email != null && enabled != null) {
            users = userRepository.findByEmailAndEnabled(email, enabled);
        } else if (email != null) {
            users = List.of(userRepository.findByEmail(email).get());
        } else if (enabled != null) {
            users = userRepository.findByEnabled(enabled);
        } else {
            users = userRepository.findAll();
        }

        return users.stream()
                .map(ConvertUtils::userDtoFromUser)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> getUserByUsername(String username) {
        return Optional.of(ConvertUtils.userDtoFromUser(userRepository.findByUsername(username).get()));
    }

    @Override
    public Optional<UserDto> updateUser(String username, User user) {
        return Optional.of(ConvertUtils.userDtoFromUser(userRepository.findByUsername(username).map(existing -> {
            existing.setEmail(user.getEmail());
            existing.setEnabled(user.isEnabled());
            // Set other fields as needed
            return userRepository.save(existing);
        }).get()));
    }

    @Override
    public boolean deleteUser(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            userRepository.delete(userOpt.get());
            return true;
        }
        return false;
    }

}
