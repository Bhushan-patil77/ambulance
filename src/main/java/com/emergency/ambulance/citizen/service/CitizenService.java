package com.emergency.ambulance.citizen.service;

import com.emergency.ambulance.citizen.dto.CitizenDto;
import com.emergency.ambulance.citizen.dto.CitizenLoginDto;
import com.emergency.ambulance.citizen.dto.CitizenLoginResponseDto;
import com.emergency.ambulance.citizen.dto.CitizenMapper;
import com.emergency.ambulance.citizen.dto.CreateCitizenDto;
import com.emergency.ambulance.citizen.entity.Citizen;
import com.emergency.ambulance.citizen.repository.CitizenRepository;
import com.emergency.ambulance.common.enums.Role;
import com.emergency.ambulance.common.exception.BadRequestException;
import com.emergency.ambulance.common.exception.UnauthorizedException;
import com.emergency.ambulance.common.utility.PasswordUtil;
import com.emergency.ambulance.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CitizenService {

    private final CitizenRepository citizenRepository;
    private final CitizenMapper citizenMapper;
    private final PasswordUtil passwordUtil;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public CitizenDto registerCitizen(CreateCitizenDto createCitizenDto) {
        String email = createCitizenDto.getEmail().trim().toLowerCase();

        citizenRepository.findByEmail(email)
                .ifPresent(citizen -> {
                    throw new BadRequestException("Citizen with email '" + email + "' already exists.");
                });

        citizenRepository.findByMobileNumber(createCitizenDto.getMobileNumber())
                .ifPresent(citizen -> {
                    throw new BadRequestException("Citizen with mobile number '" + createCitizenDto.getMobileNumber() + "' already exists.");
                });

        citizenRepository.findByAdharNumber(createCitizenDto.getAdharNumber())
                .ifPresent(citizen -> {
                    throw new BadRequestException("Citizen with adhar number '" + createCitizenDto.getAdharNumber() + "' already exists.");
                });

        citizenRepository.findByPanNumber(createCitizenDto.getPanNumber())
                .ifPresent(citizen -> {
                    throw new BadRequestException("Citizen with PAN number '" + createCitizenDto.getPanNumber() + "' already exists.");
                });

        Citizen citizen = new Citizen();
        citizen.setName(createCitizenDto.getName());
        citizen.setMobileNumber(createCitizenDto.getMobileNumber());
        citizen.setEmail(email);
        citizen.setAdharNumber(createCitizenDto.getAdharNumber());
        citizen.setPanNumber(createCitizenDto.getPanNumber());
        citizen.setBloodGroup(createCitizenDto.getBloodGroup());
        citizen.setAddress(createCitizenDto.getAddress());
        citizen.setPassword(passwordUtil.getEncodedPassword(createCitizenDto.getPassword()));
        citizen.setRole(Role.CITIZEN);

        Citizen savedCitizen = citizenRepository.save(citizen);
        return citizenMapper.toDto(savedCitizen);
    }

    public CitizenLoginResponseDto login(CitizenLoginDto loginDto) {
        String email = loginDto.getEmail().trim().toLowerCase();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email,
                            loginDto.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new UnauthorizedException("Invalid citizen email or password");
        }

        Citizen citizen = citizenRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid citizen email or password"));

        String token = jwtService.generateToken(
                citizen.getEmail(),
                citizen.getId(),
                Role.CITIZEN
        );

        return new CitizenLoginResponseDto(
                token,
                "Bearer",
                jwtService.getExpirationSeconds(),
                Role.CITIZEN.name(),
                citizen.getId()
        );
    }
}
