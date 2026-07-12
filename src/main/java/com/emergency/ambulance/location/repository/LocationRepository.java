package com.emergency.ambulance.location.repository;

import com.emergency.ambulance.location.entity.Location;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByAmbulanceId(Long ambulanceId);

    List<Location> findByAmbulanceIdIn(Collection<Long> ambulanceIds);
}
