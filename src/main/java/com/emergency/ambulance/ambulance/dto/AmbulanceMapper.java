package com.emergency.ambulance.ambulance.dto;

import com.emergency.ambulance.ambulance.entity.Ambulance;
import org.springframework.stereotype.Component;

@Component
public class AmbulanceMapper {

    public AmbulanceDto toDto(Ambulance ambulance) {
        if (ambulance == null) {
            return null;
        }

        AmbulanceDto dto = new AmbulanceDto();
        dto.setId(ambulance.getId());
        dto.setAmbulanceNumber(ambulance.getAmbulanceNumber());
        dto.setAmbulanceType(ambulance.getAmbulanceType());
        dto.setAmbulanceStatus(ambulance.getAmbulanceStatus());
        dto.setCreatedAt(ambulance.getCreatedAt());
        dto.setUpdatedAt(ambulance.getUpdatedAt());

        if (ambulance.getDriver() != null) {
            dto.setDriverId(ambulance.getDriver().getId());
        } else {
            dto.setDriverId(null);
        }
        dto.setAmbulanceStatus(ambulance.getAmbulanceStatus());

        return dto;
    }
}
