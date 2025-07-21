package com.login.user.domain.model;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tb_address")
@SQLRestriction("dt_deleted_at IS NULL")
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk_id")
    private UUID id;

    @Column(name = "st_street", nullable = false)
    @NotBlank
    private String street;

    @Column(name = "it_number", nullable = false)
    private int number;

    @Column(name = "st_complement")
    private String complement;

    @Column(name = "st_neighborhood")
    private String neighborhood;

    @Column(name = "st_city")
    private String city;

    @Column(name = "st_state")
    private String state;

    @Column(name = "st_zip_code")
    private String zipCode;

    @Column(name = "st_country")
    private String country = "Brazil";

    @Column(name = "dt_deleted_at")
    private Instant deletedAt;
}
