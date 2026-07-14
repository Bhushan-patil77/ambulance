package com.emergency.ambulance.emergency.controller;

import com.emergency.ambulance.common.response.ApiResponse;
import com.emergency.ambulance.common.response.ResponseUtil;
import com.emergency.ambulance.emergency.dto.AcceptEmergencyRequestDto;
import com.emergency.ambulance.emergency.dto.AcceptEmergencyResponseDto;
import com.emergency.ambulance.emergency.service.EmergencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/emergencies")
@RequiredArgsConstructor
public class EmergencyController {

    private final EmergencyService emergencyService;

    @PostMapping("/acceptEmergency")
    public ResponseEntity<ApiResponse<AcceptEmergencyResponseDto>> acceptEmergency( @Valid @RequestBody AcceptEmergencyRequestDto requestDto) {
        AcceptEmergencyResponseDto responseDto = emergencyService.acceptEmergency(requestDto);
        return ResponseEntity.ok(ResponseUtil.success("Emergency accepted successfully", responseDto));
    }
}
