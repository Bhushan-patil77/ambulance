package com.emergency.ambulance.admin.service;

import com.emergency.ambulance.admin.entity.Admin;
import com.emergency.ambulance.admin.repository.AdminRepository;
import com.emergency.ambulance.common.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AdminUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final Set<String> superAdminEmails;

    public AdminUserDetailsService(AdminRepository adminRepository,
                                   @Value("${super.admin.emails:}") String superAdminEmails) {
        this.adminRepository = adminRepository;
        this.superAdminEmails = Stream.of(superAdminEmails.split(","))
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found: " + email));

        if (admin.getPassword() == null || admin.getPassword().isBlank()) {
            throw new UsernameNotFoundException("Admin credentials not configured: " + email);
        }

        Role role = isSuperAdminEmail(admin.getEmail())
                ? Role.SUPER_ADMIN
                : (admin.getRole() == null ? Role.ADMIN : admin.getRole());

        return new User(
                admin.getEmail(),
                admin.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()))
        );
    }

    private boolean isSuperAdminEmail(String email) {
        return email != null && superAdminEmails.contains(email.toLowerCase());
    }
}
