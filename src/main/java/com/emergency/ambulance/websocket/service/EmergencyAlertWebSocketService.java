package com.emergency.ambulance.websocket.service;

import com.emergency.ambulance.ambulance.repository.AmbulanceRepository;
import com.emergency.ambulance.ambulance.repository.NearByAmbulanceProjection;
import com.emergency.ambulance.common.enums.AmbulanceStatus;
import com.emergency.ambulance.emergency.entity.Emergency;
import com.emergency.ambulance.websocket.dto.CitizenEmergencyAcceptedMessage;
import com.emergency.ambulance.websocket.dto.EmergencyAlertMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmergencyAlertWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;
    private final AmbulanceRepository ambulanceRepository;

    @Value("${ambulance.alert.radius-km:15}")
    private double alertRadiusKm;

    @Value("${ambulance.location.stale-seconds:600}")
    private long staleSeconds;

    public int broadcastEmergencyAlert(Emergency emergency) {
        Point mishapLocation = emergency.getMishapLocation();
        if (mishapLocation == null) {
            log.warn("Emergency {} has no mishap location. Skipping alert broadcast.", emergency.getId());
            return 0;
        }

        double latitude = mishapLocation.getY();
        double longitude = mishapLocation.getX();

        List<NearByAmbulanceProjection> nearbyAmbulances = ambulanceRepository.findAmbulancesWithinRadius(
                latitude,
                longitude,
                alertRadiusKm,
                staleSeconds
        );

        int publishedCount = 0;
        for (NearByAmbulanceProjection ambulance : nearbyAmbulances) {
            if (ambulance.getAmbulanceStatus() != AmbulanceStatus.AVAILABLE) {continue;}

            EmergencyAlertMessage message = new EmergencyAlertMessage(
                    emergency.getEmergencyLevel(),
                    new EmergencyAlertMessage.EmergencyLocation(latitude, longitude),
                    emergency.getRequestedAmbulanceType()
            );

            messagingTemplate.convertAndSend("/topic/ambulance/" + ambulance.getAmbulanceId() + "/emergency-alerts", message);
            publishedCount++;
        }

        if (publishedCount > 0) {
            log.info("Broadcasted emergency {} alert to {} nearby ambulances.", emergency.getId(), publishedCount);
        } else {
            log.info("No AVAILABLE nearby ambulances found for emergency {}.", emergency.getId());
        }

        return publishedCount;
    }

    public void notifyCitizenEmergencyAccepted(Emergency emergency) {
        if (emergency.getCitizen() == null || emergency.getCitizen().getId() == null) {
            log.warn("Emergency {} has no citizen info. Skipping citizen accepted notification.", emergency.getId());
            return;
        }

        CitizenEmergencyAcceptedMessage message = new CitizenEmergencyAcceptedMessage(
                emergency.getId(),
                emergency.getStatus(),
                emergency.getAmbulance() != null ? emergency.getAmbulance().getId() : null,
                emergency.getDriver() != null ? emergency.getDriver().getId() : null,
                emergency.getUpdatedAt()
        );

        messagingTemplate.convertAndSend( "/topic/citizen/" + emergency.getCitizen().getId() + "/emergency-updates", message );
    }
}

























// Citizen creates emergency through REST.
// Backend alerts nearby ambulances over websocket.
// Ambulance accepts through REST.
// Backend sends websocket confirmation to:
// citizen: “your emergency is accepted”
// ambulance: “you won the assignment”
// Ambulance app shows a “Start Trip” action.
// When driver clicks “Start Trip”, that should be another backend state change.
// Backend marks emergency as ARRIVING_TO_CITIZEN.
// Backend sends websocket event to both sides that trip has started.
// Ambulance app opens map screen with:
// mishap latitude/longitude from backend
// route calculated client-side or via maps provider
// Ambulance app starts publishing live location.
// Backend validates and rebroadcasts that location to the citizen.