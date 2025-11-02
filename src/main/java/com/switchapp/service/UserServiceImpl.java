package com.switchapp.service;

import com.switchapp.model.Role;
import com.switchapp.model.User;
import com.switchapp.repository.RoleRepository;
import com.switchapp.repository.UserRepository;
import com.switchapp.util.ConvertUtils;
import com.switchapp.util.SignupRequest;
import com.switchapp.util.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            throw new RuntimeException("Invalid credentials");
//        }

        Map<String, Object> jwtClaims = new HashMap<>();
        jwtClaims.put("usernme", user.getUsername());
        jwtClaims.put("usrpk", user.getId());
        List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
        jwtClaims.put("roles", roles);
        logger.info("JWT Info : {}", ConvertUtils.convertObjectToJson(jwtClaims));
        response.put("token", jwtUtil.generateToken(user.getUsername(), jwtClaims));
        response.put("user", ConvertUtils.userDtoFromUser(user));

        if (user != null) {
            user.setLastloginDt(LocalDateTime.now());
            userRepository.save(user);
        }

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
                Role r = roleRepository.findByName(role.getName())
                        .orElseThrow(() -> new RuntimeException("Role not found: " + role));
                roles.add(r);
            });
        }
        user.setRoles(roles);

        return userRepository.save(user);
    }

    @Override
    public User createUser(User user) {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.email = user.getEmail();
        signupRequest.username = user.getUsername();
        signupRequest.password = passwordEncoder.encode(user.getPassword());
        signupRequest.roles = user.getRoles() != null ? user.getRoles() : new HashSet<>();
        signupRequest.firstname = user.getFirstname();
        signupRequest.lastname = user.getLastname();
        signupRequest.phonenumber = user.getPhonenumber();
        signupRequest.gender = user.getGender();
        signupRequest.lastloginDt = user.getLastloginDt();
        signupRequest.dateofbirthDt = user.getDateofbirthDt();

        return registerUser(signupRequest);
    }

    @Override
    public List<UserDto> getAllUsers() {

        return userRepository.findAll().stream()
                .map(ConvertUtils::userDtoFromUser)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> getUserByUsername(String username) {
        return Optional.of(ConvertUtils.userDtoFromUser(userRepository.findByUsername(username).get()));
    }

    @Override
    public Optional<UserDto> updateUser(String username, UserDto user) {
        return userRepository.findByUsername(username).map(existing -> {
            if (!existing.getEmail().equals(user.getEmail())) {
                if (userRepository.existsByEmail(user.getEmail())) {
                    throw new RuntimeException("Email already in use");
                }
                existing.setEmail(user.getEmail());
            }
            // Update username
            if (!existing.getUsername().equals(user.getUsername())) {
                if (userRepository.existsByUsername(user.getUsername())) {
                    throw new RuntimeException("Username already taken");
                }
                existing.setUsername(user.getUsername());
            }
            existing.setEnabled(user.isEnabled());

            logger.info("Incoming roles: {}", user.getRoles());
            Set<Role> updatedRoles = Optional.ofNullable(user.getRoles())
                    .orElse(Collections.emptySet())
                    .stream()
                    .map(role -> roleRepository.findByName(role.getName())
                            .orElseThrow(() -> new RuntimeException("Role not found: " + role.getName())))
                    .collect(Collectors.toSet());
            logger.info("Updated roles: {}", updatedRoles);

            existing.setRoles(updatedRoles);
            // Set other fields as needed
            return ConvertUtils.userDtoFromUser(userRepository.save(existing));
        });
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

    @Override
    public boolean resetPassword(String username, String newPassword) {
        logger.info("Password reset for user: {}", username);
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String encodedPassword = passwordEncoder.encode(newPassword);
            logger.info("Encoded password: {}", encodedPassword);
            user.setPassword(encodedPassword);
            User userOut = userRepository.save(user);
            logger.info("User reset successfully: {}", ConvertUtils.convertObjectToJson(userOut));
            boolean isValid = passwordEncoder.matches(newPassword, encodedPassword);
            logger.info("Password updated for user: {}, isValid : {}", username, isValid);
            return true;
        }
        logger.warn("User not found: {}", username);
        return false;
    }

}
