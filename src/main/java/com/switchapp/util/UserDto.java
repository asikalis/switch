package com.switchapp.util;

import com.switchapp.model.Role;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UserDto {
    private String username;
    private String email;
    private boolean enabled;
    private LocalDateTime createdDt;
    private Set<Role> roles;
    private String firstname;
    private String lastname;
    private String phonenumber;
    private LocalDateTime lastloginDt;
    private LocalDate dateofbirthDt;
    private String gender;
}
