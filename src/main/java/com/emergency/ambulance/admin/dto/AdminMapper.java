package com.emergency.ambulance.admin.dto;

import com.emergency.ambulance.admin.entity.Admin;
import org.springframework.stereotype.Component;

@Component
public class AdminMapper {

    public AdminDto toDto(Admin admin) {
        if (admin == null) {
            return null;
        }

        AdminDto dto = new AdminDto();
        dto.setId(admin.getId());
        dto.setName(admin.getName());
        dto.setMobileNumber(admin.getMobileNumber());
        dto.setEmail(admin.getEmail());
        dto.setRole(admin.getRole());
        dto.setCreatedAt(admin.getCreatedAt());
        dto.setUpdatedAt(admin.getUpdatedAt());

        return dto;
    }
}
