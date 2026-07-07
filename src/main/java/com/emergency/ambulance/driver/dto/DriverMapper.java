package com.emergency.ambulance.driver.dto;

import com.emergency.ambulance.driver.entity.Driver;
import org.springframework.stereotype.Component;

@Component
public class DriverMapper {

    public DriverDto toDto(Driver driver) {
        if (driver == null) {
            return null;
        }

        DriverDto dto = new DriverDto();
        dto.setId(driver.getId());
        dto.setName(driver.getName());
        dto.setEmail(driver.getEmail());
        dto.setRole(driver.getRole());
        dto.setCreatedAt(driver.getCreatedAt());
        dto.setUpdatedAt(driver.getUpdatedAt());
        return dto;
    }
}
