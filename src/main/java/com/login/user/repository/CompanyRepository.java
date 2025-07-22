package com.login.user.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.login.user.domain.model.CompanyEntity;

import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<@NonNull CompanyEntity, @NonNull UUID> { }
