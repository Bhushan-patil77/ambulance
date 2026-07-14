package com.emergency.ambulance.emergency.repository;

import com.emergency.ambulance.emergency.entity.EmergencyLifeCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmergencyLifeCycleRepository extends JpaRepository<EmergencyLifeCycle, Long> {
}
