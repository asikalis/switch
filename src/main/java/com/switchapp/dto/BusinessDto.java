package com.switchapp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BusinessDto {
    private String name;
    private String address;
    private String email;
    private String type;
    private String phone;
    private String gstNumber;
    private String logourl;
    private LocalDateTime createdDt;
}
