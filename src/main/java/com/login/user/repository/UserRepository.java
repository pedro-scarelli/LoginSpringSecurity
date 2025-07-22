package com.login.user.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.login.user.domain.model.UserEntity;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<@NonNull UserEntity, @NonNull UUID>{

    UserEntity findByEmail(String email);
    
    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

}
