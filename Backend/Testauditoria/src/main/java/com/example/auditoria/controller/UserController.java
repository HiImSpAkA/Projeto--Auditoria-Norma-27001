package com.example.auditoria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.auditoria.entity.User;
import com.example.auditoria.service.UserService;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserService userService;
    
    // Endpoint para obter o perfil de um utilizador através do email
    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(@RequestParam String email) {
        User user = userService.getUserByEmail(email);
        
        if (user != null) {
            user.setPassword(null);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
 // Endpoint para obter as empresas associadas a um utilizador através do ID
    @GetMapping("/{userId}/empresas")
    public ResponseEntity<List<String>> getUserEmpresas(@PathVariable Long userId) {
        List<String> empresas = userService.getUserEmpresas(userId);
        return ResponseEntity.ok(empresas);
    }
 // Endpoint para obter as empresas associadas a um utilizador através do email
    @GetMapping("/empresas")
    public ResponseEntity<List<String>> getEmpresasByEmail(@RequestParam String email) {
        User user = userService.getUserByEmail(email);
        
        if (user != null) {
            List<String> empresas = userService.getUserEmpresas(user.getId());
            return ResponseEntity.ok(empresas);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    // Endpoint para atualizar a password de um utilizador
    // Recebe o email, a password atual e a nova password
    // Verifica se todos os campos estão preenchidos e se a password atual está correta
    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestBody Map<String, String> passwordData) {
        String email = passwordData.get("email");
        String currentPassword = passwordData.get("currentPassword");
        String newPassword = passwordData.get("newPassword");
        
        if (email == null || currentPassword == null || newPassword == null) {
            return ResponseEntity.badRequest().body("Todos os campos são obrigatórios");
        }
        
        boolean updated = userService.updatePassword(email, currentPassword, newPassword);
        
        if (updated) {
            return ResponseEntity.ok("Password atualizada com sucesso");
        } else {
            return ResponseEntity.badRequest().body("Não foi possível atualizar a password");
        }
    }
    // Endpoint para atualizar o email de um utilizador
    // Recebe o ID, o email atual, o novo email e a password para verificação
    // Verifica se todos os campos estão preenchidos e se o email atual está correto
    @PutMapping("/email")
    public ResponseEntity<?> updateEmail(@RequestBody Map<String, String> emailData) {
        Long userId = Long.parseLong(emailData.get("userId"));
        String currentEmail = emailData.get("currentEmail");
        String newEmail = emailData.get("newEmail");
        String password = emailData.get("password");
        
        if (userId == null || currentEmail == null || newEmail == null || password == null) {
            return ResponseEntity.badRequest().body("Todos os campos são obrigatórios");
        }
        
        boolean updated = userService.updateEmail(userId, currentEmail, newEmail, password);
        
        if (updated) {
            return ResponseEntity.ok("Email atualizado com sucesso");
        } else {
            return ResponseEntity.badRequest().body("Não foi possível atualizar o email");
        }
    }
    // Endpoint para atualizar a empresa principal de um utilizador
    @PutMapping("/empresa")
    public ResponseEntity<?> updateEmpresa(@RequestBody Map<String, String> empresaData) {
        Long userId = Long.parseLong(empresaData.get("userId"));
        String empresa = empresaData.get("empresa");
        
        if (userId == null || empresa == null) {
            return ResponseEntity.badRequest().body("Todos os campos são obrigatórios");
        }
        
        boolean updated = userService.updateEmpresa(userId, empresa);
        
        if (updated) {
            return ResponseEntity.ok("Empresa atualizada com sucesso");
        } else {
            return ResponseEntity.badRequest().body("Não foi possível atualizar a empresa");
        }
    }
    // Endpoint para adicionar uma nova empresa a um utilizador
    // As empresas são guardadas como uma string separada por vírgulas
    @PostMapping("/empresas/add")
    public ResponseEntity<?> addEmpresa(@RequestBody Map<String, String> empresaData) {
        Long userId = Long.parseLong(empresaData.get("userId"));
        String empresa = empresaData.get("empresa");
        
        if (userId == null || empresa == null) {
            return ResponseEntity.badRequest().body("Todos os campos são obrigatórios");
        }
        
        boolean added = userService.addEmpresaToUser(userId, empresa);
        
        if (added) {
            return ResponseEntity.ok("Empresa adicionada com sucesso");
        } else {
            return ResponseEntity.badRequest().body("Não foi possível adicionar a empresa");
        }
    }
}