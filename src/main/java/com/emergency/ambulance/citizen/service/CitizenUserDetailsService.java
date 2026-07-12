package com.emergency.ambulance.citizen.service;

import com.emergency.ambulance.citizen.entity.Citizen;
import com.emergency.ambulance.citizen.repository.CitizenRepository;
import com.emergency.ambulance.common.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CitizenUserDetailsService implements UserDetailsService {

    private final CitizenRepository citizenRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Citizen citizen = citizenRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Citizen not found: " + email));

        if (citizen.getPassword() == null || citizen.getPassword().isBlank()) {
            throw new UsernameNotFoundException("Citizen credentials not configured: " + email);
        }

        return new User(
                citizen.getEmail(),
                citizen.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + Role.CITIZEN.name()))
        );
    }
}
