package com.emergency.ambulance.emergency.repository;

import com.emergency.ambulance.emergency.entity.Emergency;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmergencyRepository extends JpaRepository<Emergency, Long> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT e FROM Emergency e WHERE e.id = :id")
	Optional<Emergency> findByIdForUpdate(@Param("id") Long id);
}
