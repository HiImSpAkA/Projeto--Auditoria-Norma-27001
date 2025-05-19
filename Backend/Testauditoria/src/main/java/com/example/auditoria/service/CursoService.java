package com.example.auditoria.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.auditoria.entity.Curso;
import com.example.auditoria.repository.CursoRepository;


//Serviço para gestão de operações relacionadas com cursos
@Service
public class CursoService {
    
    @Autowired
    private CursoRepository cursoRepository;
    
    // Obtém todos os cursos disponíveis
    public List<Curso> getAllCursos() {
        return cursoRepository.findAll();
    }
    
    //Obtém um curso específico pelo seu ID
    public Optional<Curso> getCursoById(Long id) {
        return cursoRepository.findById(id);
    }
    
    //Guarda um novo curso ou atualiza um existente
    public Curso saveCurso(Curso curso) {
        return cursoRepository.save(curso);
    }
    
    //Guarda um novo curso ou atualiza um existente
    public Curso updateCurso(Long id, Curso cursoDetails) {
        Optional<Curso> cursoOpt = cursoRepository.findById(id);
        
        if (cursoOpt.isPresent()) {
            Curso curso = cursoOpt.get();
            curso.setTitulo(cursoDetails.getTitulo());
            curso.setDescricao(cursoDetails.getDescricao());
            curso.setDuracao(cursoDetails.getDuracao());
            curso.setModalidade(cursoDetails.getModalidade());
            curso.setUrl(cursoDetails.getUrl());
            curso.setIcon(cursoDetails.getIcon());
            
            return cursoRepository.save(curso);
        }
        
        return null;
    }
    
    //Elimina um curso pelo seu ID
    public boolean deleteCurso(Long id) {
        if (cursoRepository.existsById(id)) {
            cursoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}