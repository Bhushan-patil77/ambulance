package com.emergency.ambulance.admin.service;

import com.emergency.ambulance.admin.dto.AdminDto;
import com.emergency.ambulance.admin.dto.AdminMapper;
import com.emergency.ambulance.admin.dto.CreateAdminDto;
import com.emergency.ambulance.admin.entity.Admin;
import com.emergency.ambulance.admin.repository.AdminRepository;
import com.emergency.ambulance.common.exception.BadRequestException;
import com.emergency.ambulance.common.utility.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;
    private final PasswordUtil passwordUtil;

    @Transactional
    public AdminDto createAdmin(CreateAdminDto createAdminDto) {
        adminRepository.findByEmail(createAdminDto.getEmail())
                .ifPresent(admin -> {
                    throw new BadRequestException("Admin with email '" + createAdminDto.getEmail() + "' already exists.");
                });

        adminRepository.findByMobileNumber(createAdminDto.getMobileNumber())
                .ifPresent(admin -> {
                    throw new BadRequestException("Admin with mobile number '" + createAdminDto.getMobileNumber() + "' already exists.");
                });

        Admin admin = new Admin();
        admin.setName(createAdminDto.getName());
        admin.setMobileNumber(createAdminDto.getMobileNumber());
        admin.setEmail(createAdminDto.getEmail());
        admin.setRole(createAdminDto.getRole());
        admin.setPassword(passwordUtil.getEncodedPassword(createAdminDto.getPassword()));

        Admin savedAdmin = adminRepository.save(admin);
        return adminMapper.toDto(savedAdmin);
    }
}
