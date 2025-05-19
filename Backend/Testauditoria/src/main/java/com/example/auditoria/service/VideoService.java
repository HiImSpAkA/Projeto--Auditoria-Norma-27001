package com.example.auditoria.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.auditoria.entity.Video;
import com.example.auditoria.repository.VideoRepository;

@Service
public class VideoService {
    
    @Autowired
    private VideoRepository videoRepository;
    
    // Método para obter todos os vídeos da base de dados
    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }
    // Método para obter um vídeo específico pelo seu ID
    // Retorna um Optional que pode conter o vídeo ou estar vazio se não existir
    public Optional<Video> getVideoById(Long id) {
        return videoRepository.findById(id);
    }
    // Método para guardar um vídeo (tanto para criar novo como para atualizar existente)
    public Video saveVideo(Video video) {
        return videoRepository.save(video);
    }
    // Método para eliminar um vídeo pelo seu ID
    public void deleteVideo(Long id) {
        videoRepository.deleteById(id);
    }
}