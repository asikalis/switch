package com.switchapp.util;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDto {
    private String username;
    private String email;
    private boolean enabled;
    private LocalDateTime createdDt;
    private List<String> roles;
}
