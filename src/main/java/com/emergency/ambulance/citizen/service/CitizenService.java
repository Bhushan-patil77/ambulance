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
import com.emergency.ambulance.common.exception.ResourceNotFoundException;
import com.emergency.ambulance.common.exception.UnauthorizedException;
import com.emergency.ambulance.common.utility.PasswordUtil;
import com.emergency.ambulance.emergency.dto.CreateEmergencyRequestDto;
import com.emergency.ambulance.emergency.dto.EmergencyCreatedResponseDto;
import com.emergency.ambulance.emergency.entity.Emergency;
import com.emergency.ambulance.emergency.entity.EmergencyLifeCycle;
import com.emergency.ambulance.emergency.repository.EmergencyLifeCycleRepository;
import com.emergency.ambulance.emergency.repository.EmergencyRepository;
import com.emergency.ambulance.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CitizenService {

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

    private final CitizenRepository citizenRepository;
    private final CitizenMapper citizenMapper;
    private final PasswordUtil passwordUtil;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmergencyRepository emergencyRepository;
    private final EmergencyLifeCycleRepository emergencyLifeCycleRepository;

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

        @Transactional
        public EmergencyCreatedResponseDto createEmergency(CreateEmergencyRequestDto requestDto) {
        Citizen citizen = citizenRepository.findById(requestDto.getCitizenId()).orElseThrow(() -> new ResourceNotFoundException("Citizen not found with id: " + requestDto.getCitizenId()));

        Point mishapPoint = GEOMETRY_FACTORY.createPoint(
            new Coordinate(
                requestDto.getMishapLongitude(),
                requestDto.getMishapLatitude()
            )
        );
        mishapPoint.setSRID(4326);

        Emergency emergency = new Emergency();
        emergency.setCitizen(citizen);
        emergency.setRequestedAmbulanceType(requestDto.getRequestedAmbulanceType());
        emergency.setMishapLocation(mishapPoint);
        emergency.setEmergencyLevel(requestDto.getEmergencyLevel());

        Emergency savedEmergency = emergencyRepository.save(emergency);

        EmergencyLifeCycle emergencyLifeCycle = new EmergencyLifeCycle();
        emergencyLifeCycle.setEmergency(savedEmergency);
        emergencyLifeCycle.setLocationPoint(mishapPoint);
        emergencyLifeCycleRepository.save(emergencyLifeCycle);

            return new EmergencyCreatedResponseDto(
                savedEmergency.getId(),
                citizen.getId(),
                savedEmergency.getRequestedAmbulanceType(),
                savedEmergency.getEmergencyLevel(),
                    requestDto.getMishapLatitude(),
                    requestDto.getMishapLongitude(),
                savedEmergency.getStatus(),
                savedEmergency.getCreatedAt()
            );
        }
}
