package com.example.auditoria.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.auditoria.dto.SearchResultDTO;
import com.example.auditoria.entity.Curso;
import com.example.auditoria.entity.File;
import com.example.auditoria.entity.Video;
import com.example.auditoria.repository.CursoRepository;
import com.example.auditoria.repository.FileRepository;
import com.example.auditoria.repository.VideoRepository;


 //Serviço responsável pela pesquisa em todas as entidades
 //Procura por documentos, vídeos e cursos que contenham o termo da pesquisa
@Service
public class SearchService {

    @Autowired
    private FileRepository fileRepository;
    
    @Autowired
    private VideoRepository videoRepository;
    
    @Autowired
    private CursoRepository cursoRepository;
    
    
    //Realiza uma pesquisa unificada em todos os recursos
    public List<SearchResultDTO> procurarTodos(String termo) {
        List<SearchResultDTO> resultados = new ArrayList<>();
        
        // Converter o termo para minúsculas para uma pesquisa sem distinção entre maiúsculas e minúsculas
        String termoLowerCase = termo.toLowerCase();
        
        // Procurar nos documentos (ficheiros)
        List<File> ficheiros = fileRepository.findAll();
        List<SearchResultDTO> resultadosFicheiros = ficheiros.stream()
            .filter(ficheiro -> ficheiro.getFileName().toLowerCase().contains(termoLowerCase))
            .map(ficheiro -> {
                // URL para download
                String downloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/files/download/")
                        .path(ficheiro.getId().toString())
                        .toUriString();
                        
                // URL para visualização
                String viewUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/files/view/")
                        .path(ficheiro.getId().toString())
                        .toUriString();
                
                return new SearchResultDTO(
                    "documento", 
                    ficheiro.getId(), 
                    ficheiro.getFileName(), 
                    ficheiro.getFileType(), 
                    ficheiro.getSize(), 
                    downloadUri, 
                    viewUri
                );
            })
            .collect(Collectors.toList());
        
        resultados.addAll(resultadosFicheiros);
        
        // Procurar nos vídeos
        List<Video> videos = videoRepository.findAll();
        List<SearchResultDTO> resultadosVideos = videos.stream()
            .filter(video -> 
                video.getTitulo().toLowerCase().contains(termoLowerCase) || 
                (video.getDescricao() != null && video.getDescricao().toLowerCase().contains(termoLowerCase))
            )
            .map(video -> new SearchResultDTO(
                "video", 
                video.getId(), 
                video.getTitulo(), 
                video.getDescricao(), 
                video.getDuracao(), 
                video.getThumbnail(), 
                video.getVideoURL()
            ))
            .collect(Collectors.toList());
        
        resultados.addAll(resultadosVideos);
        
        // Procurar nos cursos
        List<Curso> cursos = cursoRepository.findAll();
        List<SearchResultDTO> resultadosCursos = cursos.stream()
            .filter(curso -> 
                curso.getTitulo().toLowerCase().contains(termoLowerCase) || 
                (curso.getDescricao() != null && curso.getDescricao().toLowerCase().contains(termoLowerCase)) ||
                (curso.getModalidade() != null && curso.getModalidade().toLowerCase().contains(termoLowerCase))
            )
            .map(curso -> new SearchResultDTO(
                "curso", 
                curso.getId(), 
                curso.getTitulo(), 
                curso.getDescricao(), 
                curso.getDuracao(), 
                curso.getModalidade(), 
                curso.getUrl(), 
                curso.getIcon()
            ))
            .collect(Collectors.toList());
        
        resultados.addAll(resultadosCursos);
        
        return resultados;
    }
    
    
     //Pesquisa apenas em documentos
    public List<SearchResultDTO> procurarDocumentos(String termo) {
        String termoLowerCase = termo.toLowerCase();
        
        List<File> ficheiros = fileRepository.findAll();
        return ficheiros.stream()
            .filter(ficheiro -> ficheiro.getFileName().toLowerCase().contains(termoLowerCase))
            .map(ficheiro -> {
                String downloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/files/download/")
                        .path(ficheiro.getId().toString())
                        .toUriString();
                        
                String viewUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/files/view/")
                        .path(ficheiro.getId().toString())
                        .toUriString();
                
                return new SearchResultDTO(
                    "documento", 
                    ficheiro.getId(), 
                    ficheiro.getFileName(), 
                    ficheiro.getFileType(), 
                    ficheiro.getSize(), 
                    downloadUri, 
                    viewUri
                );
            })
            .collect(Collectors.toList());
    }
    

    //Pesquisa apenas em vídeos
    public List<SearchResultDTO> procurarVideos(String termo) {
        String termoLowerCase = termo.toLowerCase();
        
        List<Video> videos = videoRepository.findAll();
        return videos.stream()
            .filter(video -> 
                video.getTitulo().toLowerCase().contains(termoLowerCase) || 
                (video.getDescricao() != null && video.getDescricao().toLowerCase().contains(termoLowerCase))
            )
            .map(video -> new SearchResultDTO(
                "video", 
                video.getId(), 
                video.getTitulo(), 
                video.getDescricao(), 
                video.getDuracao(), 
                video.getThumbnail(), 
                video.getVideoURL()
            ))
            .collect(Collectors.toList());
    }
    
    //Pesquisa apenas em cursos
    public List<SearchResultDTO> procurarCursos(String termo) {
        String termoLowerCase = termo.toLowerCase();
        
        List<Curso> cursos = cursoRepository.findAll();
        return cursos.stream()
            .filter(curso -> 
                curso.getTitulo().toLowerCase().contains(termoLowerCase) || 
                (curso.getDescricao() != null && curso.getDescricao().toLowerCase().contains(termoLowerCase)) ||
                (curso.getModalidade() != null && curso.getModalidade().toLowerCase().contains(termoLowerCase))
            )
            .map(curso -> new SearchResultDTO(
                "curso", 
                curso.getId(), 
                curso.getTitulo(), 
                curso.getDescricao(), 
                curso.getDuracao(), 
                curso.getModalidade(), 
                curso.getUrl(), 
                curso.getIcon()
            ))
            .collect(Collectors.toList());
    }
}