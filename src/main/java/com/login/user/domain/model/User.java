package com.login.user.domain.model;

import java.io.Serial;
import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.login.user.domain.model.enums.UserRole;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="tb_users")
@SQLRestriction("dt_deleted_at IS NULL")
public class User implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column (name="pk_id")
    private UUID id;

    @Column (name="st_name", nullable = false)
    private String name;
 
    @Column (name="st_email", nullable = false)
    private String email;

    @Column (name="st_hashed_password", nullable = false)
    private String password;

    @Enumerated(EnumType.ORDINAL) 
    @Column (name="it_role", nullable = false)
    private UserRole role;

    @Column(name = "dt_created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "dt_deleted_at")
    private Instant deletedAt;

    @Column (name="bl_is_enabled", nullable = false)
    private boolean isEnabled;

    @Column (name="st_otp_code")
    private String otpCode;

    @Column (name="dt_otp_timestamp")
    private Instant otpTimestamp;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return email;
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

