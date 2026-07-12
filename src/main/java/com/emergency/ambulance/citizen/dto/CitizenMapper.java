package com.emergency.ambulance.citizen.dto;

import com.emergency.ambulance.citizen.entity.Citizen;
import org.springframework.stereotype.Component;

@Component
public class CitizenMapper {

    public CitizenDto toDto(Citizen citizen) {
        if (citizen == null) {
            return null;
        }

        CitizenDto dto = new CitizenDto();
        dto.setId(citizen.getId());
        dto.setName(citizen.getName());
        dto.setMobileNumber(citizen.getMobileNumber());
        dto.setEmail(citizen.getEmail());
        dto.setAdharNumber(citizen.getAdharNumber());
        dto.setPanNumber(citizen.getPanNumber());
        dto.setBloodGroup(citizen.getBloodGroup());
        dto.setAddress(citizen.getAddress());
        dto.setRole(citizen.getRole());
        dto.setCreatedAt(citizen.getCreatedAt());
        dto.setUpdatedAt(citizen.getUpdatedAt());
        return dto;
    }
}
