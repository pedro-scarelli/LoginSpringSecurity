package com.login.user.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.login.user.domain.models.User;

import java.util.UUID;


@Repository
public interface UsersRepository extends JpaRepository<User, UUID>{
    
    Page<User> findAll(Pageable pageable);
    User findByMail(String mail);
    User findByLogin(String login);
    boolean existsByLoginOrMail(String login, String mail);
}