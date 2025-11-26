package com.switchapp.util;

import com.switchapp.model.Role;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class SignupRequest {
    public String username;
    public String email;
    public String password;
    public String firstname;
    public String lastname;
    public String phonenumber;
    public LocalDateTime lastloginDt;
    public LocalDate dateofbirthDt;
    public String gender;
    public Set<Role> roles;
    public String displayname;
}
