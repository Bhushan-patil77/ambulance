package com.emergency.ambulance.ambulance.service;

import com.emergency.ambulance.ambulance.dto.AmbulanceDto;
import com.emergency.ambulance.ambulance.dto.AmbulanceMapper;
import com.emergency.ambulance.ambulance.dto.CreateAmbulanceDto;
import com.emergency.ambulance.ambulance.entity.Ambulance;
import com.emergency.ambulance.ambulance.repository.AmbulanceRepository;
import com.emergency.ambulance.common.enums.AmbulanceStatus;
import com.emergency.ambulance.common.exception.BadRequestException;
import com.emergency.ambulance.common.utility.PasswordUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AmbulanceService {

    @Value("${ambulance.default.password:defaultPassword123}") // Default value if not found in properties
    private String defaultAmbulancePassword;

    private final AmbulanceRepository ambulanceRepository;
    private final AmbulanceMapper ambulanceMapper;
    private final PasswordUtil passwordUtil;

    @Transactional
    public AmbulanceDto createAmbulance(CreateAmbulanceDto createAmbulanceDto) {
        ambulanceRepository.findByAmbulanceNumber(createAmbulanceDto.getAmbulanceNumber())
                .ifPresent(ambulance -> {
                    throw new BadRequestException("Ambulance with number '" + createAmbulanceDto.getAmbulanceNumber() + "' already exists.");
                });

        Ambulance ambulance = new Ambulance();
        ambulance.setAmbulanceNumber(createAmbulanceDto.getAmbulanceNumber());
        ambulance.setAmbulanceType(createAmbulanceDto.getAmbulanceType()); // Default status
        ambulance.setPassword(createAmbulanceDto.getPassword() == null || createAmbulanceDto.getPassword().trim().isEmpty() ? passwordUtil.getEncodedPassword(defaultAmbulancePassword) : passwordUtil.getEncodedPassword(createAmbulanceDto.getPassword()));
        ambulance.setAmbulanceStatus(AmbulanceStatus.AVAILABLE); // Default status
        ambulance.setDriver(null); // Initially no driver

        Ambulance savedAmbulance = ambulanceRepository.save(ambulance);

        return ambulanceMapper.toDto(savedAmbulance);
    }
}
