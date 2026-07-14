package com.emergency.ambulance.emergency.dto;

import com.emergency.ambulance.common.enums.AmbulanceType;
import com.emergency.ambulance.common.enums.EmergencyLevel;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmergencyRequestDto {

    @NotNull(message = "Citizen id is required")
    @JsonProperty("citizen_id")
    private Long citizenId;

    @NotNull(message = "Requested ambulance type is required")
    @JsonProperty("requested_ambulance_type")
    private AmbulanceType requestedAmbulanceType;

    @NotNull(message = "Latitude is required")
    @DecimalMin(value = "-90.0", message = "Latitude must be >= -90")
    @DecimalMax(value = "90.0", message = "Latitude must be <= 90")
    @JsonProperty("mishap_latitude")
    private Double mishapLatitude;

    @NotNull(message = "Longitude is required")
    @DecimalMin(value = "-180.0", message = "Longitude must be >= -180")
    @DecimalMax(value = "180.0", message = "Longitude must be <= 180")
    @JsonProperty("mishap_longitude")
    private Double mishapLongitude;

    @NotNull(message = "Emergency level is required")
    @JsonProperty("emergency_level")
    private EmergencyLevel emergencyLevel;
}
