package com.emergency.ambulance.emergency.entity;

import com.emergency.ambulance.common.enums.EmergencyStatus;
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
@Table(name = "emergency_life_cycles")
public class EmergencyLifeCycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "emergency_id", nullable = false)
    private Emergency emergency;

    @Enumerated(EnumType.STRING)
    @Column(name = "life_cycle", nullable = false, length = 40)
    private EmergencyStatus lifeCycle;

    @Column(name = "life_cycle_created_at", nullable = false, updatable = false)
    private LocalDateTime lifeCycleCreatedAt;

    @Column(name = "location_point", columnDefinition = "geography(Point,4326)")
    private Point locationPoint;

    @PrePersist
    protected void onCreate() {
        if (lifeCycle == null) {
            lifeCycle = EmergencyStatus.CREATED;
        }
        if (lifeCycleCreatedAt == null) {
            lifeCycleCreatedAt = LocalDateTime.now();
        }
    }
}
