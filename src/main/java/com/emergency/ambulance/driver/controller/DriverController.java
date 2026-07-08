package com.emergency.ambulance.driver.controller;

import com.emergency.ambulance.ambulance.dto.AmbulanceDto;
import com.emergency.ambulance.common.response.ApiResponse;
import com.emergency.ambulance.common.response.ResponseUtil;
import com.emergency.ambulance.driver.dto.AssignDriverDto;
import com.emergency.ambulance.driver.dto.CreateDriverDto;
import com.emergency.ambulance.driver.dto.DriverDto;
import com.emergency.ambulance.driver.dto.DriverLoginDto;
import com.emergency.ambulance.driver.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @PostMapping("/createDriver")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<DriverDto>> createDriver(@Valid @RequestBody CreateDriverDto createDriverDto) {
        DriverDto createdDriver = driverService.createDriver(createDriverDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseUtil.success("Driver created successfully", createdDriver));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<DriverDto>> login(@Valid @RequestBody DriverLoginDto loginDto) {
        DriverDto responseDto = driverService.login(loginDto);
        return ResponseEntity
                .ok(ResponseUtil.success("Driver login successful", responseDto));
    }

    @PostMapping("/assignDriver")
    public ResponseEntity<ApiResponse<AmbulanceDto>> assignDriver(@Valid @RequestBody AssignDriverDto assignDriverDto) {
        AmbulanceDto responseDto = driverService.assignDriver(assignDriverDto);
        return ResponseEntity
                .ok(ResponseUtil.success("Driver assigned successfully", responseDto));
    }
}
