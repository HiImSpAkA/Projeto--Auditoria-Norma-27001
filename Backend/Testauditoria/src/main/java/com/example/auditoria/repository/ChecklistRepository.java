package com.example.auditoria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.auditoria.entity.Checklist;

@Repository
public interface ChecklistRepository extends JpaRepository<Checklist, Long> {
	List<Checklist> findAllByOrderByDisplayorderAsc(); // Change from DisplayOrder to Displayorder
}
