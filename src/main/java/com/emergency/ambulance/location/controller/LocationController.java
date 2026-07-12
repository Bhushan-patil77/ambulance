package com.emergency.ambulance.location.controller;

import com.emergency.ambulance.common.response.ApiResponse;
import com.emergency.ambulance.common.response.ResponseUtil;
import com.emergency.ambulance.location.dto.LocationDto;
import com.emergency.ambulance.location.dto.LocationUpdateRequestDto;
import com.emergency.ambulance.location.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping("/updateAmbulanceLocation")
    @PreAuthorize("hasAnyRole('AMBULANCE','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<LocationDto>> updateAmbulanceLocation(@Valid @RequestBody LocationUpdateRequestDto requestDto) {
        LocationDto responseDto = locationService.updateAmbulanceLocation(requestDto);
        return ResponseEntity
                .ok(ResponseUtil.success("Location updated successfully", responseDto));
    }
}
