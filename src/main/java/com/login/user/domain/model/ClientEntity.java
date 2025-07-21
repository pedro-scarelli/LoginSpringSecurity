package com.login.user.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tb_person")
public class ClientEntity extends PersonEntity {
    
    @Id
    @Column(name = "pk_id")
    private UUID id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "pk_id")
    private PersonEntity person;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_company_id", nullable = false)
    private CompanyEntity company;

    @Column(name = "dt_created_at", updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "dt_updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    @Column(name = "dt_deleted_at")
    private Instant deletedAt;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AppointmentEntity> appointments;
}
