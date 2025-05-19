package com.example.auditoria.controller;

import com.example.auditoria.entity.SaveChecklist;
import com.example.auditoria.service.SaveChecklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/checklists")
@CrossOrigin(origins = "http://localhost:5173")
public class SaveChecklistController {

    @Autowired
    private SaveChecklistService saveChecklistService;
    
    // Obtém todas as checklists de um utilizador específico
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SaveChecklist>> getUserChecklists(@PathVariable Long userId) {
        List<SaveChecklist> checklists = saveChecklistService.getUserChecklists(userId);
        return ResponseEntity.ok(checklists);
    }
    
    // Obtém uma checklist específica pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<SaveChecklist> getChecklist(@PathVariable Long id) {
        Optional<SaveChecklist> checklist = saveChecklistService.getChecklistById(id);
        return checklist.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    // Transfere o ficheiro PDF de uma checklist
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadChecklist(@PathVariable Long id) {
        Optional<SaveChecklist> checklistOpt = saveChecklistService.getChecklistById(id);

        if (checklistOpt.isPresent()) {
            SaveChecklist checklist = checklistOpt.get();
            // Configura cabeçalhos para download de ficheiro
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(checklist.getContentType()));
            headers.setContentDispositionFormData("attachment", checklist.getFileName());
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(checklist.getData(), headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    // Visualiza o PDF diretamente no browser
    @GetMapping("/{id}/view")
    public ResponseEntity<byte[]> viewChecklist(@PathVariable Long id) {
        Optional<SaveChecklist> checklistOpt = saveChecklistService.getChecklistById(id);

        if (checklistOpt.isPresent()) {
            SaveChecklist checklist = checklistOpt.get();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(checklist.getContentType()));
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + checklist.getFileName());

            return new ResponseEntity<>(checklist.getData(), headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Elimina uma checklist guardada
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChecklist(@PathVariable Long id) {
        Optional<SaveChecklist> checklistOpt = saveChecklistService.getChecklistById(id);

        if (checklistOpt.isPresent()) {
            saveChecklistService.deleteChecklist(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
