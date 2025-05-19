package com.example.auditoria.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.auditoria.dto.SearchResultDTO;
import com.example.auditoria.service.SearchService;

//Controlador para operações de pesquisa
//Permite procurar recursos (documentos, vídeos e cursos) por termo
@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "http://localhost:5173")
public class SearchController {

    @Autowired
    private SearchService searchService;
    
    
    // Endpoint principal para pesquisa em todos os recursos
    @GetMapping
    public ResponseEntity<List<SearchResultDTO>> procurarTodos(
            @RequestParam(name = "termo", required = true) String termo) {
        
        List<SearchResultDTO> resultados = searchService.procurarTodos(termo);
        return ResponseEntity.ok(resultados);
    }
    
    
    //Endpoint para procurar apenas documentos
    @GetMapping("/documentos")
    public ResponseEntity<List<SearchResultDTO>> procurarDocumentos(
            @RequestParam(name = "termo", required = true) String termo) {
        
        List<SearchResultDTO> resultados = searchService.procurarDocumentos(termo);
        return ResponseEntity.ok(resultados);
    }
    
    //Endpoint para procurar apenas videos
    @GetMapping("/videos")
    public ResponseEntity<List<SearchResultDTO>> procurarVideos(
            @RequestParam(name = "termo", required = true) String termo) {
        
        List<SearchResultDTO> resultados = searchService.procurarVideos(termo);
        return ResponseEntity.ok(resultados);
    }
    
    //Endpoint para procurar apenas cursos
    @GetMapping("/cursos")
    public ResponseEntity<List<SearchResultDTO>> procurarCursos(
            @RequestParam(name = "termo", required = true) String termo) {
        
        List<SearchResultDTO> resultados = searchService.procurarCursos(termo);
        return ResponseEntity.ok(resultados);
    }
}