package com.emergency.ambulance.ambulance.dto;

import com.emergency.ambulance.common.enums.AmbulanceStatus;
import com.emergency.ambulance.common.enums.AmbulanceType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AmbulanceDto {

    private Long id;
    private String ambulanceNumber;
    private AmbulanceType ambulanceType;
    private AmbulanceStatus ambulanceStatus;
    private Long driverId; // Can be null
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
