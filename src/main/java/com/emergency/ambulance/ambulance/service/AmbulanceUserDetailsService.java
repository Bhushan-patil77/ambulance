package com.emergency.ambulance.ambulance.service;

import com.emergency.ambulance.ambulance.entity.Ambulance;
import com.emergency.ambulance.ambulance.repository.AmbulanceRepository;
import com.emergency.ambulance.common.enums.Role;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AmbulanceUserDetailsService implements UserDetailsService {

    private final AmbulanceRepository ambulanceRepository;

    public AmbulanceUserDetailsService(AmbulanceRepository ambulanceRepository) {
        this.ambulanceRepository = ambulanceRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String ambulanceNumber) throws UsernameNotFoundException {
        Ambulance ambulance = ambulanceRepository.findByAmbulanceNumber(ambulanceNumber)
                .orElseThrow(() -> new UsernameNotFoundException("Ambulance not found: " + ambulanceNumber));

        if (ambulance.getPassword() == null || ambulance.getPassword().isBlank()) {
            throw new UsernameNotFoundException("Ambulance credentials not configured: " + ambulanceNumber);
        }

        return new User(
                ambulance.getAmbulanceNumber(),
                ambulance.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + Role.AMBULANCE.name()))
        );
    }
}
