package com.emergency.ambulance.driver.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignDriverDto {

    @NotNull(message = "Ambulance id is required")
    private Long ambulanceId;

    @NotNull(message = "Driver id is required")
    private Long driverId;
}
