package com.example.auditoria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.auditoria.entity.Curso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
}