package com.emergency.ambulance.location.service;

import com.emergency.ambulance.ambulance.entity.Ambulance;
import com.emergency.ambulance.ambulance.repository.AmbulanceRepository;
import com.emergency.ambulance.common.exception.ResourceNotFoundException;
import com.emergency.ambulance.location.dto.LocationDto;
import com.emergency.ambulance.location.dto.LocationMapper;
import com.emergency.ambulance.location.dto.LocationUpdateRequestDto;
import com.emergency.ambulance.location.entity.Location;
import com.emergency.ambulance.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocationService {

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final LocationRepository locationRepository;
    private final AmbulanceRepository ambulanceRepository;
    private final LocationMapper locationMapper;

    @Transactional
    public LocationDto updateAmbulanceLocation(LocationUpdateRequestDto requestDto) {
        Ambulance ambulance = ambulanceRepository.findById(requestDto.getAmbulanceId())
                .orElseThrow(() -> new ResourceNotFoundException("Ambulance not found with id: " + requestDto.getAmbulanceId()));

        Location location = locationRepository.findByAmbulanceId(ambulance.getId())
                .orElseGet(() -> {
                    Location newLocation = new Location();
                    newLocation.setAmbulance(ambulance);
                    return newLocation;
                });
        boolean isNewLocation = location.getId() == null;

        Point point = GEOMETRY_FACTORY.createPoint(new Coordinate(requestDto.getLongitude(), requestDto.getLatitude()));
        point.setSRID(4326);

        location.setLocation(point);
        location.setAccuracyMeters(requestDto.getAccuracyMeters());
        location.setSpeedKmph(requestDto.getSpeedKmph());
        location.setHeadingDegrees(requestDto.getHeadingDegrees());
        location.setUpdatedAt(LocalDateTime.now());

        Location savedLocation = locationRepository.save(location);
        String action = isNewLocation ? "created" : "updated";
        log.info("Ambulance location is {} with latitude : {} , longitude : {} speed : {} at {}",
                action,
                requestDto.getLatitude(),
                requestDto.getLongitude(),
                requestDto.getSpeedKmph(),
                LocalTime.now().format(TIME_FORMATTER));
        return locationMapper.toDto(savedLocation);
    }
}
