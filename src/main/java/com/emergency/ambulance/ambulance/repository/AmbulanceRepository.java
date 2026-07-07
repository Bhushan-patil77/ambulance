package com.emergency.ambulance.ambulance.repository;

import com.emergency.ambulance.ambulance.entity.Ambulance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AmbulanceRepository extends JpaRepository<Ambulance, Long> {
    Optional<Ambulance> findByAmbulanceNumber(String ambulanceNumber);
}
