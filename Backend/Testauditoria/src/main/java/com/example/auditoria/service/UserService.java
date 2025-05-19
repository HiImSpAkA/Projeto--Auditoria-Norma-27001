package com.example.auditoria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.auditoria.entity.User;
import com.example.auditoria.repository.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Usa o BCrypt para criptografar e validar passwords

    // Método para registar um novo utilizador
    // Codifica a password antes de guardar na base de dados
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encripta as passwords
        return userRepository.save(user);
    }
    // Método para autenticar um utilizador
    // Verifica se o email existe e se a password corresponde ao hash guardado
    public boolean authenticateUser(String email, String rawPassword) {
        User user = userRepository.findByEmail(email);
        return user != null && passwordEncoder.matches(rawPassword, user.getPassword()); // Verifica a password
    }
    // Método para obter um utilizador pelo email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    // Método para obter um utilizador pelo ID
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    // Método para atualizar a password de um utilizador
    // Verifica se a password atual está correta antes de atualizar
    public boolean updatePassword(String email, String currentPassword, String newPassword) {
        User user = userRepository.findByEmail(email);
        
        if (user != null && passwordEncoder.matches(currentPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        
        return false;
    }
    
    // Método para atualizar o email de um utilizador
    // Verifica se o email atual e a password estão corretos antes de atualizar
    // Verifica também se o novo email já está em uso
    public boolean updateEmail(Long userId, String currentEmail, String newEmail, String password) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            
            // Verificar se o email atual e a password estão corretos
            if (user.getEmail().equals(currentEmail) && passwordEncoder.matches(password, user.getPassword())) {
                // Verificar se o novo email já está em uso
                if (userRepository.findByEmail(newEmail) == null) {
                    user.setEmail(newEmail);
                    userRepository.save(user);
                    return true;
                }
            }
        }
        
        return false;
    }
    // Método para atualizar a empresa de um utilizador
    public boolean updateEmpresa(Long userId, String empresa) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setEmpresa(empresa);
            userRepository.save(user);
            return true;
        }
        
        return false;
    }
    // Método para obter a lista de empresas de um utilizador
    // Divide a string de empresas em uma lista
    public List<String> getUserEmpresas(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String empresa = user.getEmpresa();
            
            if (empresa != null && !empresa.isEmpty()) {
                return Arrays.asList(empresa.split(","));
            }
        }
        
        return new ArrayList<>();
    }
    
    // Método para adicionar uma nova empresa a um utilizador
    // Verifica se a empresa já existe antes de adicionar
    public boolean addEmpresaToUser(Long userId, String novaEmpresa) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String empresaAtual = user.getEmpresa();
            
            // Se já existe uma empresa, adicionar a nova com separador
            if (empresaAtual != null && !empresaAtual.isEmpty()) {
                // Verificar se a empresa já existe
                List<String> empresas = Arrays.asList(empresaAtual.split(","));
                if (!empresas.contains(novaEmpresa)) {
                    user.setEmpresa(empresaAtual + "," + novaEmpresa);
                }
            } else {
                // Se não existe empresa, definir a nova empresa
                user.setEmpresa(novaEmpresa);
            }
            
            userRepository.save(user);
            return true;
        }
        
        return false;
    }
}