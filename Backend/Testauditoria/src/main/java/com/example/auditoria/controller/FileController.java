package com.example.auditoria.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.auditoria.dto.FileResponse;
import com.example.auditoria.entity.File;
import com.example.auditoria.service.FileService;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "http://localhost:5173")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<FileResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "descricao", required = false) String descricao) throws java.io.IOException {
        try {
            File savedFile = fileService.storeFile(file, descricao);
            
            // URL para download
            String downloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/files/download/")
                    .path(savedFile.getId().toString())
                    .toUriString();
                    
            // URL para visualização
            String viewUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/files/view/")
                    .path(savedFile.getId().toString())
                    .toUriString();
            
            FileResponse response = new FileResponse(
                savedFile.getFileName(), 
                downloadUri,  
                file.getContentType(), 
                file.getSize()
            );
            
            response.setViewUrl(viewUri);
            response.setDescricao(savedFile.getDescricao());
            
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Endpoint para atualizar apenas a descrição de um arquivo existente
    @PutMapping("/{id}/descricao")
    public ResponseEntity<FileResponse> updateFileDescription(
            @PathVariable Long id,
            @RequestParam("descricao") String descricao) {
        try {
            File updatedFile = fileService.updateFileDescription(id, descricao);
            
            // URL para download
            String downloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/files/download/")
                    .path(updatedFile.getId().toString())
                    .toUriString();
                    
            // URL para visualização
            String viewUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/files/view/")
                    .path(updatedFile.getId().toString())
                    .toUriString();
            
            FileResponse response = new FileResponse(
                updatedFile.getFileName(), 
                downloadUri,  
                updatedFile.getFileType(), 
                updatedFile.getSize()
            );
            
            response.setViewUrl(viewUri);
            response.setDescricao(updatedFile.getDescricao());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //download documento
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        File fileEntity = fileService.getFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileEntity.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(fileEntity.getFileType()))
                .body(fileEntity.getData());
    }
    
    //ver documento
    @GetMapping("/view/{id}")
    public ResponseEntity<byte[]> viewFile(@PathVariable Long id) {
        File fileEntity = fileService.getFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileEntity.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(fileEntity.getFileType()))
                .body(fileEntity.getData());
    }

    @GetMapping
    public ResponseEntity<List<FileResponse>> listFiles() {
        List<FileResponse> files = fileService.getAllFiles().stream()
                .map(file -> {
                    String downloadUrl = ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/api/files/download/")
                        .path(file.getId().toString())
                        .toUriString();
                        
                    String viewUrl = ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/api/files/view/")
                        .path(file.getId().toString())
                        .toUriString();
                        
                    FileResponse response = new FileResponse(
                        file.getFileName(),
                        downloadUrl,
                        file.getFileType(),
                        file.getSize()
                    );
                    
                    response.setViewUrl(viewUrl);
                    response.setDescricao(file.getDescricao());
                    return response;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(files);
    }
}