package com.example.auditoria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.auditoria.entity.File;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
}
