package com.login.user.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.*;

import com.login.user.domain.model.enums.AppointmentStatus;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tb_appointment")
@SQLRestriction("dt_deleted_at IS NULL")
public class AppointmentEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk_id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_client_id", nullable = false)
    private ClientEntity client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_company_id", nullable = false)
    private CompanyEntity company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_address_id", nullable = false)
    private AddressEntity address;

    @Column(name = "dt_scheduled_date", nullable = false)
    private LocalDateTime scheduledDate;

    @Column(name = "dc_estimated_duration_hours")
    private Double estimatedDurationHours;

    @Column(name = "dc_actual_duration_hours")
    private Double actualDurationHours;

    @Column(name = "st_service_description", nullable = false, columnDefinition = "TEXT")
    private String serviceDescription;

    @Column(name = "dc_estimated_amount")
    private BigDecimal estimatedAmount;

    @Column(name = "dc_final_amount")
    private BigDecimal finalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "st_status", nullable = false)
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;

    @Column(name = "st_notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "st_nfe_number", length = 20)
    private String nfeNumber;

    @Column(name = "st_nfe_key", length = 100)
    private String nfeKey;

    @Column(name = "dt_nfe_issued_at")
    private Instant nfeIssuedAt;

    @Column(name = "st_google_calendar_event_id", length = 255)
    private String googleCalendarEventId;

    @CreationTimestamp
    @Column(name = "dt_created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "dt_updated_at")
    private Instant updatedAt;

    @Column(name = "dt_completed_at")
    private Instant completedAt;

    @Column(name = "dt_deleted_at")
    private Instant deletedAt;

    public void complete(BigDecimal finalAmount, Double actualHours) {
        this.status = AppointmentStatus.COMPLETED;
        this.finalAmount = finalAmount;
        this.actualDurationHours = actualHours;
        this.completedAt = Instant.now();
    }
}
