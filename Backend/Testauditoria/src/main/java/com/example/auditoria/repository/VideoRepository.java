package com.example.auditoria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.auditoria.entity.Video;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
}