package com.example.auditoria.service;

import com.example.auditoria.entity.SaveChecklist;
import com.example.auditoria.repository.SaveChecklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SaveChecklistService {

    @Autowired
    private SaveChecklistRepository saveChecklistRepository;
    
    // Guarda uma checklist no sistema
    public SaveChecklist saveChecklist(Long userId, String empresa, String dataAvaliacaoStr, byte[] fileData, String fileName, String contentType) {
        try {
            Date dataAvaliacao = null;
            
            // Processa a data de avaliação ou usa a atual
            if (dataAvaliacaoStr != null && !dataAvaliacaoStr.isBlank()) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                dataAvaliacao = formatter.parse(dataAvaliacaoStr);
            } else {
                dataAvaliacao = new Date(); // Data atual
            }
            
            // Cria e guarda o objeto SaveChecklist
            SaveChecklist checklist = new SaveChecklist(
                userId,
                empresa,
                dataAvaliacao,
                fileName,
                contentType,
                fileData
            );

            return saveChecklistRepository.save(checklist);
        } catch (ParseException e) {
            throw new RuntimeException("Erro ao processar a data da avaliação: " + e.getMessage());
        }
    }
    
    // Obtém todas as checklists de um utilizador
    public List<SaveChecklist> getUserChecklists(Long userId) {
        return saveChecklistRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    // Obtém uma checklist específica pelo ID
    public Optional<SaveChecklist> getChecklistById(Long id) {
        return saveChecklistRepository.findById(id);
    }
    
    // Elimina uma checklist
    public void deleteChecklist(Long id) {
        saveChecklistRepository.deleteById(id);
    }
}
