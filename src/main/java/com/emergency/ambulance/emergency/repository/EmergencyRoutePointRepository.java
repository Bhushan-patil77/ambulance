package com.emergency.ambulance.emergency.repository;

import com.emergency.ambulance.emergency.entity.EmergencyRoutePoint;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmergencyRoutePointRepository extends JpaRepository<EmergencyRoutePoint, Long> {
    List<EmergencyRoutePoint> findByEmergencyIdOrderByRecordedAtAsc(Long emergencyId);
}
