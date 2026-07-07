package com.emergency.ambulance.admin.service;

import com.emergency.ambulance.admin.dto.AdminDto;
import com.emergency.ambulance.admin.dto.AdminLoginDto;
import com.emergency.ambulance.admin.dto.AdminLoginResponseDto;
import com.emergency.ambulance.admin.dto.AdminMapper;
import com.emergency.ambulance.admin.dto.CreateAdminDto;
import com.emergency.ambulance.admin.entity.Admin;
import com.emergency.ambulance.admin.repository.AdminRepository;
import com.emergency.ambulance.common.enums.Role;
import com.emergency.ambulance.common.exception.BadRequestException;
import com.emergency.ambulance.common.exception.UnauthorizedException;
import com.emergency.ambulance.common.utility.PasswordUtil;
import com.emergency.ambulance.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AdminService {

    private final Set<String> superAdminEmails;
    private final String superAdminPassword;
    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;
    private final PasswordUtil passwordUtil;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public AdminService(@Value("${super.admin.emails:}") String superAdminEmails,
            @Value("${super.admin.password:}") String superAdminPassword,
                        AdminRepository adminRepository,
                        AdminMapper adminMapper,
                        PasswordUtil passwordUtil,
                        AuthenticationManager authenticationManager,
                        JwtService jwtService) {
        this.superAdminEmails = Stream.of(superAdminEmails.split(","))
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    this.superAdminPassword = superAdminPassword;
        this.adminRepository = adminRepository;
        this.adminMapper = adminMapper;
        this.passwordUtil = passwordUtil;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

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

    public AdminLoginResponseDto login(AdminLoginDto loginDto) {
        String email = loginDto.getEmail() == null ? null : loginDto.getEmail().trim().toLowerCase();

        if (isSuperAdminEmail(email)) {
            if (superAdminPassword == null || superAdminPassword.isBlank() || !superAdminPassword.equals(loginDto.getPassword())) {
                throw new UnauthorizedException("Invalid admin email or password");
            }

            String token = jwtService.generateToken(email, null, Role.SUPER_ADMIN);
            return new AdminLoginResponseDto(
                    token,
                    "Bearer",
                    jwtService.getExpirationSeconds(),
                    Role.SUPER_ADMIN.name()
            );
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email,
                            loginDto.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new UnauthorizedException("Invalid admin email or password");
        }

        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid admin email or password"));

        Role role = isSuperAdminEmail(admin.getEmail())
            ? Role.SUPER_ADMIN
            : (admin.getRole() == null ? Role.ADMIN : admin.getRole());
        String token = jwtService.generateToken(admin.getEmail(), admin.getId(), role);

        return new AdminLoginResponseDto(
                token,
                "Bearer",
                jwtService.getExpirationSeconds(),
                role.name()
        );
    }

    private boolean isSuperAdminEmail(String email) {
        return email != null && superAdminEmails.contains(email.toLowerCase());
    }
}
