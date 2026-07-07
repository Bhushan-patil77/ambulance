package com.emergency.ambulance.ambulance.entity;

import com.emergency.ambulance.common.enums.AmbulanceStatus;
import com.emergency.ambulance.common.enums.AmbulanceType;
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

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "ambulances")
public class Ambulance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ambulance_number", nullable = false, unique = true)
    private String ambulanceNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "ambulance_type", nullable = false)
    private AmbulanceType ambulanceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "ambulance_status", nullable = false)
    private AmbulanceStatus ambulanceStatus;

    @Column(name = "password", nullable = true) // Password can be null if not set
    private String password;

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = true) // driverId can be null
    private Driver driver;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
