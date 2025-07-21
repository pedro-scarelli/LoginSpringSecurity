package com.login.user.domain.model;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.login.user.domain.model.enums.PersonType;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "tb_person")
public class PersonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk_id")
    private UUID id;

    @Column(name = "st_name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "st_person_type", nullable = false)
    private PersonType personType;

    @Column(name = "st_cpf", nullable = false)
    private String cpf;

    @Column(name = "st_email", nullable = false)
    private String email;

    @Column(name = "st_phone", nullable = false)
    private String phone;

    @Column(name = "dt_created_at", updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "dt_updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    @Column(name = "dt_deleted_at")
    private Instant deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_address_id")
    private AddressEntity address;
}
