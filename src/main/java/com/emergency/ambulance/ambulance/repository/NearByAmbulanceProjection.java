package com.emergency.ambulance.ambulance.repository;

import com.emergency.ambulance.common.enums.AmbulanceStatus;
import com.emergency.ambulance.common.enums.AmbulanceType;
import com.emergency.ambulance.common.enums.Role;
import java.time.LocalDateTime;

public interface NearByAmbulanceProjection {

    Long getAmbulanceId();

    String getAmbulanceNumber();

    AmbulanceType getAmbulanceType();

    AmbulanceStatus getAmbulanceStatus();

    LocalDateTime getAmbulanceUpdatedAt();

    Long getLocationId();

    Double getLatitude();

    Double getLongitude();

    Double getAccuracyMeters();

    Double getSpeedKmph();

    Double getHeadingDegrees();

    LocalDateTime getLocationUpdatedAt();

    Long getDriverId();

    String getDriverName();

    String getDriverEmail();

    Role getDriverRole();

    LocalDateTime getDriverUpdatedAt();
}