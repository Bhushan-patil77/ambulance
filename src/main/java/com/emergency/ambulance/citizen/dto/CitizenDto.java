package com.emergency.ambulance.citizen.dto;

import com.emergency.ambulance.common.enums.Role;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CitizenDto {

    private Long id;
    private String name;
    private String mobileNumber;
    private String email;
    private String adharNumber;
    private String panNumber;
    private String bloodGroup;
    private String address;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
