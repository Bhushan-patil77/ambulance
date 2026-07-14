package com.emergency.ambulance.emergency.service;

import com.emergency.ambulance.ambulance.entity.Ambulance;
import com.emergency.ambulance.ambulance.repository.AmbulanceRepository;
import com.emergency.ambulance.common.enums.AmbulanceStatus;
import com.emergency.ambulance.common.enums.EmergencyStatus;
import com.emergency.ambulance.common.exception.BadRequestException;
import com.emergency.ambulance.common.exception.ResourceNotFoundException;
import com.emergency.ambulance.driver.entity.Driver;
import com.emergency.ambulance.driver.repository.DriverRepository;
import com.emergency.ambulance.emergency.dto.AcceptEmergencyRequestDto;
import com.emergency.ambulance.emergency.dto.AcceptEmergencyResponseDto;
import com.emergency.ambulance.emergency.entity.Emergency;
import com.emergency.ambulance.emergency.entity.EmergencyLifeCycle;
import com.emergency.ambulance.emergency.repository.EmergencyLifeCycleRepository;
import com.emergency.ambulance.emergency.repository.EmergencyRepository;
import com.emergency.ambulance.websocket.service.EmergencyAlertWebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmergencyService {

    private final EmergencyRepository emergencyRepository;
    private final EmergencyLifeCycleRepository emergencyLifeCycleRepository;
    private final DriverRepository driverRepository;
    private final AmbulanceRepository ambulanceRepository;
    private final EmergencyAlertWebSocketService emergencyAlertWebSocketService;

    @Transactional
    public AcceptEmergencyResponseDto acceptEmergency(AcceptEmergencyRequestDto requestDto) {
        Emergency emergency = emergencyRepository.findByIdForUpdate(requestDto.getEmergencyId())
                .orElseThrow(() -> new ResourceNotFoundException("Emergency not found with id: " + requestDto.getEmergencyId()));

        Driver driver = driverRepository.findById(requestDto.getDriverId())
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id: " + requestDto.getDriverId()));

        Ambulance ambulance = ambulanceRepository.findFirstByDriverId(driver.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No ambulance is assigned to driver id: " + driver.getId()));

        if (emergency.getStatus() != EmergencyStatus.CREATED && emergency.getStatus() != EmergencyStatus.ALERT_SENT) {
            throw new BadRequestException("Emergency cannot be accepted from status: " + emergency.getStatus());
        }

        if (ambulance.getAmbulanceStatus() != AmbulanceStatus.AVAILABLE) {
            throw new BadRequestException("Ambulance is not available to accept emergency.");
        }

        emergency.setAmbulance(ambulance);
        emergency.setDriver(driver);
        emergency.setStatus(EmergencyStatus.ACCEPTED);
        Emergency savedEmergency = emergencyRepository.save(emergency);

        ambulance.setAmbulanceStatus(AmbulanceStatus.ON_EMERGENCY);
        ambulanceRepository.save(ambulance);

        EmergencyLifeCycle emergencyLifeCycle = new EmergencyLifeCycle();
        emergencyLifeCycle.setEmergency(savedEmergency);
        emergencyLifeCycle.setLifeCycle(EmergencyStatus.ACCEPTED);
        emergencyLifeCycle.setLocationPoint(savedEmergency.getMishapLocation());
        emergencyLifeCycleRepository.save(emergencyLifeCycle);

        emergencyAlertWebSocketService.notifyCitizenEmergencyAccepted(savedEmergency);

        return new AcceptEmergencyResponseDto(
                savedEmergency.getId(),
                savedEmergency.getStatus(),
                ambulance.getId(),
                driver.getId(),
                savedEmergency.getUpdatedAt()
        );
    }
}
