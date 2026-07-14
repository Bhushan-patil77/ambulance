package com.emergency.ambulance.emergency.dto;

import com.emergency.ambulance.common.enums.AmbulanceType;
import com.emergency.ambulance.common.enums.EmergencyLevel;
import com.emergency.ambulance.common.enums.EmergencyStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyCreatedResponseDto {

    private Long emergencyId;
    private Long citizenId;
    private AmbulanceType requestedAmbulanceType;
    private EmergencyLevel emergencyLevel;
    private Double latitude;
    private Double longitude;
    private EmergencyStatus status;
    private LocalDateTime createdAt;
}
