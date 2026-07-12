package com.emergency.ambulance.emergency.repository;

import com.emergency.ambulance.common.enums.EmergencyStatus;
import com.emergency.ambulance.emergency.entity.Emergency;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmergencyRepository extends JpaRepository<Emergency, Long> {
    Optional<Emergency> findByIdAndCitizenId(Long emergencyId, Long citizenId);
    List<Emergency> findByAmbulanceIdOrderByCreatedAtDesc(Long ambulanceId);
    List<Emergency> findByCitizenIdOrderByCreatedAtDesc(Long citizenId);
    List<Emergency> findByStatusIn(List<EmergencyStatus> statuses);
}
