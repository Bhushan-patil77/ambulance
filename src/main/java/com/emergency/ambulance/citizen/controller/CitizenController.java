package com.emergency.ambulance.citizen.controller;

import com.emergency.ambulance.citizen.dto.CitizenDto;
import com.emergency.ambulance.citizen.dto.CitizenLoginDto;
import com.emergency.ambulance.citizen.dto.CitizenLoginResponseDto;
import com.emergency.ambulance.citizen.dto.CreateCitizenDto;
import com.emergency.ambulance.citizen.service.CitizenService;
import com.emergency.ambulance.common.response.ApiResponse;
import com.emergency.ambulance.common.response.ResponseUtil;
import com.emergency.ambulance.emergency.dto.CreateEmergencyRequestDto;
import com.emergency.ambulance.emergency.dto.EmergencyCreatedResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/citizens")
@RequiredArgsConstructor
public class CitizenController {

    private final CitizenService citizenService;

    @PostMapping("/registerCitizen")
    public ResponseEntity<ApiResponse<CitizenDto>> registerCitizen(@Valid @RequestBody CreateCitizenDto createCitizenDto) {
        CitizenDto citizenDto = citizenService.registerCitizen(createCitizenDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtil.success("Citizen registered successfully", citizenDto));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<CitizenLoginResponseDto>> login(@Valid @RequestBody CitizenLoginDto loginDto) {
        CitizenLoginResponseDto responseDto = citizenService.login(loginDto);
        return ResponseEntity.ok(ResponseUtil.success("Citizen login successful", responseDto));
    }

    @PostMapping("/createEmergency")
    public ResponseEntity<ApiResponse<EmergencyCreatedResponseDto>> createEmergency(@Valid @RequestBody CreateEmergencyRequestDto requestDto) {
        EmergencyCreatedResponseDto responseDto = citizenService.createEmergency(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtil.success("Emergency created successfully", responseDto));
    }
}
