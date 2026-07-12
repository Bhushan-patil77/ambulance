package com.emergency.ambulance.location.dto;

import com.emergency.ambulance.location.entity.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {

    public LocationDto toDto(Location location) {
        if (location == null) {
            return null;
        }

        LocationDto dto = new LocationDto();
        dto.setId(location.getId());
        dto.setAmbulanceId(location.getAmbulance().getId());
        if (location.getLocation() != null) {
            dto.setLatitude(location.getLocation().getY());
            dto.setLongitude(location.getLocation().getX());
        }
        dto.setAccuracyMeters(location.getAccuracyMeters());
        dto.setSpeedKmph(location.getSpeedKmph());
        dto.setHeadingDegrees(location.getHeadingDegrees());
        dto.setCreatedAt(location.getCreatedAt());
        dto.setUpdatedAt(location.getUpdatedAt());
        return dto;
    }
}
