package com.emergency.ambulance.ambulance.dto;

import com.emergency.ambulance.common.enums.AmbulanceStatus;
import com.emergency.ambulance.common.enums.AmbulanceType;
import com.emergency.ambulance.common.enums.Role;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class NearByAmbulancesDto {

    private AmbulanceDetails ambulance;
    private LocationDetails location;
    private DriverDetails driver;

    @Data
    public static class AmbulanceDetails {
        private Long id;
        private String ambulanceNumber;
        private AmbulanceType ambulanceType;
        private AmbulanceStatus ambulanceStatus;
        private LocalDateTime updatedAt;
    }

    @Data
    public static class LocationDetails {
        private Long id;
        private Long ambulanceId;
        private Double latitude;
        private Double longitude;
        private Double accuracyMeters;
        private Double speedKmph;
        private Double headingDegrees;
        private LocalDateTime updatedAt;
    }

    @Data
    public static class DriverDetails {
        private Long id;
        private String name;
        private String email;
        private Role role;
        private LocalDateTime updatedAt;
    }
}