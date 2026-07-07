package com.emergency.ambulance.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminLoginResponseDto {

    private String accessToken;
    private String tokenType;
    private long expiresIn;
    private String role;
}
