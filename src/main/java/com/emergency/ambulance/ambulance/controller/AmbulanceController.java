package com.emergency.ambulance.ambulance.controller;

import com.emergency.ambulance.ambulance.dto.AmbulanceDto;
import com.emergency.ambulance.ambulance.dto.AmbulanceLoginDto;
import com.emergency.ambulance.ambulance.dto.AmbulanceLoginResponseDto;
import com.emergency.ambulance.ambulance.dto.CreateAmbulanceDto;
import com.emergency.ambulance.ambulance.dto.NearByAmbulancesDto;
import com.emergency.ambulance.ambulance.service.AmbulanceService;
import com.emergency.ambulance.common.response.ApiResponse;
import com.emergency.ambulance.common.response.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ambulances")
@RequiredArgsConstructor
public class AmbulanceController {

    private final AmbulanceService ambulanceService;

    @PostMapping("/createAmbulance")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<AmbulanceDto>> createAmbulance(@Valid @RequestBody CreateAmbulanceDto createAmbulanceDto) {
        AmbulanceDto createdAmbulance = ambulanceService.createAmbulance(createAmbulanceDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseUtil.success("Ambulance created successfully", createdAmbulance));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AmbulanceLoginResponseDto>> login(@Valid @RequestBody AmbulanceLoginDto loginDto) {
        AmbulanceLoginResponseDto responseDto = ambulanceService.login(loginDto);
        return ResponseEntity
                .ok(ResponseUtil.success("Ambulance login successful", responseDto));
    }

    @GetMapping("/getAmbulances")
    public ResponseEntity<ApiResponse<List<NearByAmbulancesDto>>> getAmbulances(@RequestParam double latitude,
                                                                                 @RequestParam double longitude,
                                                                                 @RequestParam double radiusKm) {
        List<NearByAmbulancesDto> ambulanceDtos = ambulanceService.getAmbulances(latitude, longitude, radiusKm);
        return ResponseEntity
                .ok(ResponseUtil.success("Nearby ambulances fetched successfully", ambulanceDtos));
    }
}
