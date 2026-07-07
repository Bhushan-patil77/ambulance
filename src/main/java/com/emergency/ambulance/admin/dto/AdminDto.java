package com.emergency.ambulance.admin.dto;

import com.emergency.ambulance.common.enums.Role;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminDto {

    private Long id;
    private String name;
    private String mobileNumber;
    private String email;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
