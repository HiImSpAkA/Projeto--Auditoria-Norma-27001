package com.example.auditoria.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.auditoria.dto.ChecklistItem;
import com.example.auditoria.dto.ExportRequestDTO;
import com.example.auditoria.entity.Checklist;
import com.example.auditoria.entity.SaveChecklist;
import com.example.auditoria.service.AIRecommendationService;
import com.example.auditoria.service.ChecklistService;
import com.example.auditoria.service.PDFService;
import com.example.auditoria.service.SaveChecklistService;

@RestController
@RequestMapping("/api/checklist")
@CrossOrigin(origins = "http://localhost:5173")
public class ChecklistController {

    @Autowired
    private ChecklistService checklistService; // Serviço que gere operações de checklist

    @Autowired
    private PDFService pdfService; // Serviço para criar PDFs

    @Autowired
    private SaveChecklistService saveChecklistService; // Serviço para guardar checklists
    
    @Autowired
    private AIRecommendationService aiRecommendationService; // Serviço de recomendações IA

    // Retorna todos os controlos da checklist
    @GetMapping
    public ResponseEntity<List<Checklist>> getAllItems() {
        return ResponseEntity.ok(checklistService.getAllItems());
    }
    // Retorna um controlo específico por ID
    @GetMapping("/{id}")
    public ResponseEntity<Checklist> getItem(@PathVariable Long id) {
        return ResponseEntity.ok(checklistService.getItem(id));
    }
    // Atualiza um controlo específico da checklist
    @PutMapping("/{id}")
    public ResponseEntity<Checklist> updateItem(@PathVariable Long id, @RequestBody ChecklistItem itemDTO) {
        return ResponseEntity.ok(checklistService.updateItem(id, itemDTO));
    }
    // Atualiza vários controlos da checklist ao mesmo tempo
    @PutMapping("/bulk-update")
    public ResponseEntity<List<Checklist>> bulkUpdate(@RequestBody Map<Long, Boolean> complianceUpdates) {
        return ResponseEntity.ok(checklistService.updateBulkItems(complianceUpdates));
    }
    
    
    //Endpoint para criar recomendações AI para um controlo específico da checklist
    @PostMapping("/generate-recommendation/{id}")
    public ResponseEntity<Checklist> generateSingleRecommendation(@PathVariable Long id) {
        try {
            Checklist item = checklistService.getItem(id);
            
            if (item.getIsHeader()) {
                return ResponseEntity.badRequest().build();
            }
            
            // Cria recomendação utilizando e a API daa Gemini
            String recommendation = aiRecommendationService.generateRecommendation(
                item.getControlos(),
                item.getTarefas(),
                item.getEmConformidade()
            );
            
            // Extrair apenas a nota e sugestão do texto completo
            String cleanRecommendation = extractCleanRecommendation(recommendation);
            
            // Atualizar o controlo se a recomendação foi criada com sucesso
            if (cleanRecommendation != null && !cleanRecommendation.isEmpty() && 
                !cleanRecommendation.contains("em processamento")) {
                item.setAiNotas(cleanRecommendation);
                checklistService.getChecklistRepository().save(item);
            }
            
            return ResponseEntity.ok(item);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    

     //Extrai apenas a nota e a sugestão do texto completo da API
    private String extractCleanRecommendation(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        try {
            // Verificar se o texto contém JSON
            if (text.contains("\"candidates\":")) {
                // Tentar extrair o texto da estrutura JSON
                int textStart = text.indexOf("\"text\":");
                if (textStart > 0) {
                    textStart += 8; // avançar além de "text": "
                    int textEnd = text.indexOf("\"", textStart);
                    // Se não encontrar o final, tente outra abordagem
                    if (textEnd <= textStart) {
                        textEnd = text.indexOf("\"}", textStart);
                        if (textEnd > textStart) {
                            return text.substring(textStart, textEnd)
                                    .replace("\\n", "\n")
                                    .replace("\\\"", "\"")
                                    .trim();
                        }
                    } else {
                        return text.substring(textStart, textEnd)
                                .replace("\\n", "\n")
                                .replace("\\\"", "\"")
                                .trim();
                    }
                }
                
                // Outra abordagem: procurar por objetos JSON completos
                if (text.trim().startsWith("{") && text.trim().endsWith("}")) {
                    try {
                        // Encontrar a parte do texto que contém "Nota:" 
                        int notaIndex = text.indexOf("Nota:");
                        if (notaIndex >= 0) {
                            return text.substring(notaIndex);
                        }
                    } catch (Exception e) {
                        // Falhou no parse, continuar com o texto original
                    }
                }
            }
            
            // Se o texto já começa com "Nota:", retornar como está
            if (text.trim().startsWith("Nota:")) {
                return text;
            }
            
            // Se não conseguiu extrair, retornar o texto original
            return text;
        } catch (Exception e) {
            return text;
        }
    }
    
    // Endpoint para verificar os recursos utilizados da API (Usado para ambiente de produção)
    @GetMapping("/api-status")
    public ResponseEntity<Map<String, Object>> getApiStatus() {
        try {
            Map<String, Object> status = aiRecommendationService.getApiUsageStatus();
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    //Converter a checklist para PDF
    @PostMapping("/export-pdf")
    public ResponseEntity<byte[]> exportPDF(@RequestBody ExportRequestDTO exportRequest) {
  
        List<ChecklistItem> checklistItems;
        // Obter os dados dos controlos da checklist 
        if (exportRequest.getChecklistItems() != null && !exportRequest.getChecklistItems().isEmpty()) {
            checklistItems = exportRequest.getChecklistItems();
        } else {
        	// Converte os controlos da base de dados para DTOs 
            List<Checklist> items = checklistService.getAllItems();
            
            checklistItems = items.stream()
                .map(item -> {
                    ChecklistItem dto = new ChecklistItem();
                    dto.setControlos(item.getControlos());
                    dto.setFases_implementacao(item.getFase_implementacao());
                    dto.setTarefas(item.getTarefas());
                    dto.setEmConformidade(item.getEmConformidade());
                    dto.setNotas(item.getNotas());
                    
                    
                    String aiNotas = item.getAiNotas();
                    if (aiNotas != null && !aiNotas.isEmpty()) {
                        dto.setAi_notas(extractCleanRecommendation(aiNotas));
                    } else {
                        dto.setAi_notas(aiNotas);
                    }
                    
                    dto.setIsHeader(item.getIsHeader());
                    dto.setDisplayOrder(item.getDisplayorder());
                    return dto;
                })
                .collect(Collectors.toList());
        }

        String companyName = exportRequest.getCompanyName() != null ? exportRequest.getCompanyName() : "Empresa";
        String assessmentDate = exportRequest.getAssessmentDate() != null ? exportRequest.getAssessmentDate() : "N/A";

        byte[] pdfBytes = pdfService.generatePDF(
            checklistItems,
            companyName,
            assessmentDate
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        if (exportRequest.getUserId() != null) {
            try {
                String fileName = "iso27001-checklist-" + companyName.replaceAll("\\s+", "_") + ".pdf";

                SaveChecklist savedChecklist = saveChecklistService.saveChecklist(
                    exportRequest.getUserId(),
                    companyName,
                    assessmentDate,
                    pdfBytes,
                    fileName,
                    "application/pdf"
                );

                headers.setContentDispositionFormData("filename", fileName);
                headers.add("Report-Id", savedChecklist.getId().toString());

                return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            } catch (Exception e) {
                System.err.println("Erro ao guardar a checklist: " + e.getMessage());
            }
        }

        headers.setContentDispositionFormData("filename", "iso27001-checklist.pdf");
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}