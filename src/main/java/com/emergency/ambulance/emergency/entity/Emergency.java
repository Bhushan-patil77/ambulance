package com.emergency.ambulance.emergency.entity;

import com.emergency.ambulance.ambulance.entity.Ambulance;
import com.emergency.ambulance.citizen.entity.Citizen;
import com.emergency.ambulance.common.enums.AmbulanceType;
import com.emergency.ambulance.common.enums.EmergencyLevel;
import com.emergency.ambulance.common.enums.EmergencyStatus;
import com.emergency.ambulance.driver.entity.Driver;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "emergencies")
public class Emergency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "citizen_id", nullable = false)
    private Citizen citizen;

    @ManyToOne
    @JoinColumn(name = "ambulance_id")
    private Ambulance ambulance;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @Enumerated(EnumType.STRING)
    @Column(name = "requested_ambulance_type", nullable = false, length = 30)
    private AmbulanceType requestedAmbulanceType;

    @Column(name = "mishap_location", nullable = false, columnDefinition = "geography(Point,4326)")
    private Point mishapLocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = true, length = 40)
    private EmergencyStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "emergency_level", nullable = false, length = 20)
    private EmergencyLevel emergencyLevel;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = EmergencyStatus.CREATED;
        }
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
