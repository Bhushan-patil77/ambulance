package com.emergency.ambulance.driver.dto;

import com.emergency.ambulance.common.enums.Role;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DriverDto {

    private Long id;
    private String name;
    private String email;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
