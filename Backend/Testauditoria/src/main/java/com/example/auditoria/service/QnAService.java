package com.example.auditoria.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class QnAService {
    // Acesso API key e URL Gemini
    @Value("${gemini.api.url}")
    private String geminiApiUrl;
    
    @Value("${gemini.api.key}")
    private String geminiApiKey;
    

    // Cliente HTTP reativo para fazer pedidos à API
    private final WebClient webClient;
    
    public QnAService(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }

    // Método para verificar se a pergunta está dentro dos tópicos permitidos
    // Verifica se a pergunta contém pelo menos uma das palavras-chave relacionadas a auditoria
    private boolean isQuestionValid(String question) {
   
        String[] validKeywords = {
            "auditoria", "norma", "conformidade", "ISO", "procedimento", 
            "controlo interno", "auditor", "certificação", "auditoria interna", 
            "auditoria externa", "gestão de riscos", "auditoria de processos"
        };

        // Verifica se a pergunta contém pelo menos uma palavra-chave
        question = question.toLowerCase();
        for (String keyword : validKeywords) {
            if (question.contains(keyword)) {
                return true;
            }
        }

        return false;
    }

    // Função para obter a resposta
    public String getAnswer(String question) {
        // Verifica se a pergunta está dentro dos tópicos permitidos
        if (!isQuestionValid(question)) {
            return "Desculpe, posso responder apenas sobre auditorias e normas.";
        }

        // Payload Request, configurar de forma a que o pedido seja como gemini atua
        Map<String, Object> requestBody = Map.of(  
            "contents", new Object[] {
                Map.of("parts", new Object[] {
                    Map.of("text", question)
                })
            }
        );

        // API call
        String response = webClient.post()
                .uri(geminiApiUrl + geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return response;
    }
}
