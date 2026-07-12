package com.emergency.ambulance.emergency.entity;

import com.emergency.ambulance.ambulance.entity.Ambulance;
import com.emergency.ambulance.citizen.entity.Citizen;
import com.emergency.ambulance.common.enums.EmergencyStatus;
import com.emergency.ambulance.driver.entity.Driver;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.locationtech.jts.geom.Point;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "routePoints")
@Entity
@Table(
        name = "emergencies",
        indexes = {
                @Index(name = "idx_emergency_citizen_id", columnList = "citizen_id"),
                @Index(name = "idx_emergency_ambulance_id", columnList = "ambulance_id"),
                @Index(name = "idx_emergency_driver_id", columnList = "driver_id"),
                @Index(name = "idx_emergency_status", columnList = "status")
        }
)
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
    @Column(name = "status", nullable = false, length = 40)
    private EmergencyStatus status;

    @Column(name = "emergency_location", nullable = false, columnDefinition = "geography(Point,4326)")
    private Point emergencyLocation;

    @Column(name = "emergency_address", length = 300)
    private String emergencyAddress;

    @Column(name = "hospital_location", columnDefinition = "geography(Point,4326)")
    private Point hospitalLocation;

    @Column(name = "hospital_name", length = 150)
    private String hospitalName;

    @Column(name = "hospital_address", length = 300)
    private String hospitalAddress;

    @Column(name = "route_polyline", columnDefinition = "TEXT")
    private String routePolyline;

    @Column(name = "estimated_distance_km", precision = 10, scale = 2)
    private BigDecimal estimatedDistanceKm;

    @Column(name = "estimated_duration_seconds")
    private Long estimatedDurationSeconds;

    @Column(name = "actual_distance_km", precision = 10, scale = 2)
    private BigDecimal actualDistanceKm;

    @Column(name = "actual_duration_seconds")
    private Long actualDurationSeconds;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "emergency_accepted_at")
    private LocalDateTime emergencyAcceptedAt;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "emergency", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("recordedAt ASC")
    private List<EmergencyRoutePoint> routePoints = new ArrayList<>();

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
