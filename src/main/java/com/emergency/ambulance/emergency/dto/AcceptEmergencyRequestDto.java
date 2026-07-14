package com.emergency.ambulance.emergency.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcceptEmergencyRequestDto {

    @NotNull(message = "Emergency id is required")
    @JsonProperty("emergency_id")
    private Long emergencyId;

    @NotNull(message = "Driver id is required")
    @JsonProperty("driver_id")
    private Long driverId;
}
