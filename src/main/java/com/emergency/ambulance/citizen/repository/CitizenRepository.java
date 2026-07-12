package com.emergency.ambulance.citizen.repository;

import com.emergency.ambulance.citizen.entity.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CitizenRepository extends JpaRepository<Citizen, Long> {
    Optional<Citizen> findByEmail(String email);
    Optional<Citizen> findByMobileNumber(String mobileNumber);
    Optional<Citizen> findByAdharNumber(String adharNumber);
    Optional<Citizen> findByPanNumber(String panNumber);
}
