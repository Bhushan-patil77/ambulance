package com.emergency.ambulance.admin.controller;

import com.emergency.ambulance.admin.dto.AdminDto;
import com.emergency.ambulance.admin.dto.AdminLoginDto;
import com.emergency.ambulance.admin.dto.AdminLoginResponseDto;
import com.emergency.ambulance.admin.dto.CreateAdminDto;
import com.emergency.ambulance.admin.service.AdminService;
import com.emergency.ambulance.common.response.ApiResponse;
import com.emergency.ambulance.common.response.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/createAdmin")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<AdminDto>> createAdmin(@Valid @RequestBody CreateAdminDto createAdminDto) {
        AdminDto createdAdmin = adminService.createAdmin(createAdminDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseUtil.success("Admin created successfully", createdAdmin));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AdminLoginResponseDto>> login(@Valid @RequestBody AdminLoginDto loginDto) {
        AdminLoginResponseDto responseDto = adminService.login(loginDto);
        return ResponseEntity
                .ok(ResponseUtil.success("Admin login successful", responseDto));
    }
}
