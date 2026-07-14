package com.emergency.ambulance.websocket.dto;

import com.emergency.ambulance.common.enums.EmergencyStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CitizenEmergencyAcceptedMessage {

    private Long emergencyId;
    private EmergencyStatus status;
    private Long ambulanceId;
    private Long driverId;
    private LocalDateTime acceptedAt;
}
