package com.login.user.domain.model;

import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.login.user.domain.model.enums.UserRole;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tb_user")
public class UserEntity extends PersonEntity implements UserDetails {

    @Id
    @Column(name = "pk_id")
    private UUID id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "pk_id")
    private PersonEntity person;

    @Column(name = "st_password_hash", nullable = false)
    private String password;

    @Enumerated(EnumType.ORDINAL) 
    @Column(name = "it_role", nullable = false)
    private UserRole role;

    @Column(name = "bl_enabled", nullable = false)
    private boolean isEnabled;

    @Column(name = "st_otp_code")
    private String otpCode;

    @Column(name = "dt_otp_timestamp")
    private Instant otpTimestamp;

    @Column(name = "dt_created_at", updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "dt_updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    @Column(name = "dt_deleted_at")
    private Instant deletedAt;

    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CompanyEntity company;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return super.getEmail();
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}

