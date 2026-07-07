package com.emergency.ambulance.ambulance.dto;

import com.emergency.ambulance.common.enums.AmbulanceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAmbulanceDto {

    @NotBlank(message = "Ambulance number cannot be empty")
    @Size(max = 50, message = "Ambulance number cannot exceed 50 characters")
    private String ambulanceNumber;

    @NotNull(message = "Ambulance type cannot be null")
    private AmbulanceType ambulanceType;

    // Password is optional for creation; if not provided, a default will be used.
    @Size(max = 255, message = "Password cannot exceed 255 characters")
    private String password;
}