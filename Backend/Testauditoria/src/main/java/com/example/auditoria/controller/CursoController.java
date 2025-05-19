package com.example.auditoria.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.auditoria.entity.Curso;
import com.example.auditoria.service.CursoService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class CursoController {

    @Autowired
    private CursoService cursoService;
    
    // Obter todos os cursos
    @GetMapping("/cursos")
    public List<Curso> getAllCursos() {
        return cursoService.getAllCursos();
    }
    
    // Criar Cursos
    @PostMapping("/cursos")
    public Curso createCurso(@RequestBody Curso curso) {
        return cursoService.saveCurso(curso);
    }
    
    // Obter curso por ID
    @GetMapping("/cursos/{id}")
    public ResponseEntity<Curso> getCursoById(@PathVariable Long id) {
        Optional<Curso> curso = cursoService.getCursoById(id);
        return curso.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    // Atualizar um curso
    @PutMapping("/cursos/{id}")
    public ResponseEntity<Curso> updateCurso(@PathVariable Long id, @RequestBody Curso cursoDetails) {
        Curso updatedCurso = cursoService.updateCurso(id, cursoDetails);
        
        if (updatedCurso != null) {
            return ResponseEntity.ok(updatedCurso);
        }
        return ResponseEntity.notFound().build();
    }
    
    //Apagar um curso
    @DeleteMapping("/cursos/{id}")
    public ResponseEntity<?> deleteCurso(@PathVariable Long id) {
        if (cursoService.deleteCurso(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}