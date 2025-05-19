package com.example.auditoria.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.auditoria.entity.File;
import com.example.auditoria.repository.FileRepository;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    public File storeFile(MultipartFile file, String descricao) throws IOException, java.io.IOException {
        File fileEntity = new File();
        // Define os atributos a partir do ficheiro enviado
        fileEntity.setFileName(file.getOriginalFilename());
        fileEntity.setFileType(file.getContentType());
        fileEntity.setData(file.getBytes());
        fileEntity.setSize(file.getSize());
        fileEntity.setDescricao(descricao != null ? descricao : "Fonte: Documento interno");
        return fileRepository.save(fileEntity);
    }
    
    // Versão sobrecarregada para compatibilidade com o código existente
    public File storeFile(MultipartFile file) throws IOException, java.io.IOException {
        return storeFile(file, null);
    }
    
    // Método para atualizar a descrição de um arquivo existente
    public File updateFileDescription(Long id, String descricao) {
        File fileEntity = getFile(id);
        fileEntity.setDescricao(descricao);
        return fileRepository.save(fileEntity);
    }
    
    // Método para obter um ficheiro pelo seu ID
    // Lança exceção se o ficheiro não for encontrado
    public File getFile(Long id) {
        return fileRepository.findById(id).orElseThrow(() -> new RuntimeException("File not found"));
    }
    
    // Método para obter todos os ficheiros armazenados
    public List<File> getAllFiles() {
        return fileRepository.findAll();
    }
}