package com.login.user.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tb_company")
@SQLRestriction("dt_deleted_at IS NULL")
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk_id")
    private UUID id;

    @Column(name = "st_legal_name", nullable = false)
    private String legalName;

    @Column(name = "st_trade_name")
    private String tradeName;

    @Column(name = "st_cnpj")
    private String cnpj;

    @Column(name = "st_state_registration")
    private String stateRegistration;

    @Column(name = "st_municipal_registration")
    private String municipalRegistration;

    @Column(name = "dc_default_hourly_rate")
    private Double defaultHourlyRate;

    @Column(name = "st_tax_regime")
    private String taxRegime;

    @Column(name = "st_municipal_service_code")
    private String municipalServiceCode;

    @Column(name = "dt_created_at", updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "dt_updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    @Column(name = "dt_deleted_at")
    private Instant deletedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user_id", nullable = false)
    private UserEntity owner;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ClientEntity> clients;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AppointmentEntity> appointments;
}
