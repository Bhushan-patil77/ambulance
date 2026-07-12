package com.emergency.ambulance.location.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class LocationDto {

    private Long id;
    private Long ambulanceId;
    private Double latitude;
    private Double longitude;
    private Double accuracyMeters;
    private Double speedKmph;
    private Double headingDegrees;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
