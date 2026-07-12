package com.emergency.ambulance.ambulance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AmbulanceLoginResponseDto {

    private String accessToken;
    private String tokenType;
    private long expiresIn;
    private String role;
    private Long ambulanceId;
}
