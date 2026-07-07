package com.emergency.ambulance.ambulance.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AmbulanceLoginDto {

    @NotBlank(message = "Ambulance number cannot be empty")
    private String ambulanceNumber;

    @NotBlank(message = "Password cannot be empty")
    private String password;
}
