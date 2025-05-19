package com.example.auditoria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.auditoria.entity.User;
import com.example.auditoria.service.UserService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173") 
public class AuthController {

    @Autowired
    private UserService userService;
    
    // Endpoint para registar um novo utilizador
    // Verifica se os campos obrigatórios estão preenchidos e se o email já existe
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        // Verificar se os campos obrigatórios estão presentes
        if (user.getNome() == null || user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body("Campos obrigatórios não preenchidos");
        }
        
        // Verificar se o utilizador já existe
        if (userService.getUserByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email já usado");
        }
        
        // Registar o utilizador
        User newUser = userService.registerUser(user);
        
        
        newUser.setPassword(null);
        
        return ResponseEntity.ok(newUser);
    }
    // Endpoint para autenticar um utilizador
    // Recebe o email e a password e verifica se são válidos
    // Retorna uma mensagem de sucesso ou erro
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        boolean authenticated = userService.authenticateUser(email, password);
        if (authenticated) {
            return ResponseEntity.ok("Login bem-sucedido");
        } else {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
    }
}