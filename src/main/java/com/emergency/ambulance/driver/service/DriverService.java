package com.emergency.ambulance.driver.service;

import com.emergency.ambulance.common.enums.Role;
import com.emergency.ambulance.common.exception.BadRequestException;
import com.emergency.ambulance.common.exception.UnauthorizedException;
import com.emergency.ambulance.common.utility.PasswordUtil;
import com.emergency.ambulance.driver.dto.CreateDriverDto;
import com.emergency.ambulance.driver.dto.DriverDto;
import com.emergency.ambulance.driver.dto.DriverLoginDto;
import com.emergency.ambulance.driver.dto.DriverMapper;
import com.emergency.ambulance.driver.entity.Driver;
import com.emergency.ambulance.driver.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;
    private final PasswordUtil passwordUtil;

    @Transactional
    public DriverDto createDriver(CreateDriverDto createDriverDto) {
        driverRepository.findByEmail(createDriverDto.getEmail())
                .ifPresent(driver -> {
                    throw new BadRequestException("Driver with email '" + createDriverDto.getEmail() + "' already exists.");
                });

        Driver driver = new Driver();
        driver.setName(createDriverDto.getName());
        driver.setEmail(createDriverDto.getEmail());
        driver.setPassword(passwordUtil.getEncodedPassword(createDriverDto.getPassword()));
        driver.setRole(Role.DRIVER);

        Driver savedDriver = driverRepository.save(driver);
        return driverMapper.toDto(savedDriver);
    }

    public DriverDto login(DriverLoginDto loginDto) {
        String email = loginDto.getEmail().trim().toLowerCase();

        Driver driver = driverRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid driver email or password"));

        if (!passwordUtil.matches(loginDto.getPassword(), driver.getPassword())) {
            throw new UnauthorizedException("Invalid driver email or password");
        }

        return driverMapper.toDto(driver);
    }
}
