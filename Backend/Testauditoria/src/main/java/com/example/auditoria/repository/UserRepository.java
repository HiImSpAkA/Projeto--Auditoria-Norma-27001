package com.example.auditoria.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.auditoria.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
