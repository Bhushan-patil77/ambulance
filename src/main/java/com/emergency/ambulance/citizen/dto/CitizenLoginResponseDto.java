package com.emergency.ambulance.citizen.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CitizenLoginResponseDto {

    private String accessToken;
    private String tokenType;
    private long expiresIn;
    private String role;
    private Long citizenId;
}
