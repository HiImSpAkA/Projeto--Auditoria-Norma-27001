package com.example.auditoria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.auditoria.entity.SaveChecklist;

@Repository
public interface SaveChecklistRepository extends JpaRepository <SaveChecklist, Long> {
	
	List <SaveChecklist> findByUserIdOrderByCreatedAtDesc (Long userId);

}
