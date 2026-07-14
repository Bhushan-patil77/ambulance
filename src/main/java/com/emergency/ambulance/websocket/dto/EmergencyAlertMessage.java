package com.emergency.ambulance.websocket.dto;

import com.emergency.ambulance.common.enums.AmbulanceType;
import com.emergency.ambulance.common.enums.EmergencyLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyAlertMessage {

    private EmergencyLevel emergencyLevel;
    private EmergencyLocation emergencyLocation;
    private AmbulanceType requestedAmbulanceType;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmergencyLocation {
        private Double latitude;
        private Double longitude;
    }
}
