package com.emergency.ambulance.ambulance.repository;

import com.emergency.ambulance.ambulance.entity.Ambulance;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AmbulanceRepository extends JpaRepository<Ambulance, Long> {
    Optional<Ambulance> findByAmbulanceNumber(String ambulanceNumber);
    Optional<Ambulance> findFirstByDriverId(Long driverId);

    @Query(value = """
            SELECT
                a.id AS ambulanceId,
                a.ambulance_number AS ambulanceNumber,
                a.ambulance_type AS ambulanceType,
                a.ambulance_status AS ambulanceStatus,
                a.updated_at AS ambulanceUpdatedAt,
                l.id AS locationId,
                ST_Y(l.location::geometry) AS latitude,
                ST_X(l.location::geometry) AS longitude,
                l.accuracy_meters AS accuracyMeters,
                l.speed_kmph AS speedKmph,
                l.heading_degrees AS headingDegrees,
                l.updated_at AS locationUpdatedAt,
                d.id AS driverId,
                d.name AS driverName,
                d.email AS driverEmail,
                d.role AS driverRole,
                d.updated_at AS driverUpdatedAt
            FROM ambulances a
            JOIN locations l ON l.ambulance_id = a.id
            LEFT JOIN drivers d ON d.id = a.driver_id
            WHERE ST_DWithin(
                l.location,
                ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography,
                :radiusKm * 1000
            )
            AND l.updated_at >= NOW() - (:staleSeconds * INTERVAL '1 second')
            ORDER BY ST_Distance(
                l.location,
                ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography
            ) ASC
            """, nativeQuery = true)
    List<NearByAmbulanceProjection> findAmbulancesWithinRadius(@Param("latitude") double latitude,
                                                               @Param("longitude") double longitude,
                                                               @Param("radiusKm") double radiusKm,
                                                               @Param("staleSeconds") long staleSeconds);
}
