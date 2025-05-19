package com.example.auditoria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.HashMap;

@Service
public class AIRecommendationService {

    @Autowired
    private QnAService qnAService;
    
    // Cache para armazenar recomendações e evitar chamadas repetidas à API
    private final Map<String, String> recommendationsCache = new ConcurrentHashMap<>();
    
    // Hash map para definir notas em 3 fases para cada controlo
    private static final Map<String, String> CONTROLO_DESCRICOES = new HashMap<>();
    
    //Codigo para efeitos de produção para uso do plano free do gemini: Linha 23 a Linha 65 e  Linha 164 a Linha 201
    
    // controlo de rate limiting para plano free do Gemini
    private final int MAX_REQUESTS_PER_MINUTE = 10; // Limite de 15, usando 10 para margem de segurança
    private final int MAX_REQUESTS_PER_DAY = 1300;  // Limite de 1500, usando 1300 para margem de segurança
    private final int[] requestTimestamps = new int[MAX_REQUESTS_PER_MINUTE];
    private int requestIndex = 0;
    private int dailyRequestCount = 0;
    private long dayStartTimestamp = System.currentTimeMillis() / 1000;
    
    
    //Retorna o status atual do uso da API para monitoramento
    public Map<String, Object> getApiUsageStatus() {
        Map<String, Object> status = new HashMap<>();
        
        long currentTime = System.currentTimeMillis() / 1000;
        
        // Verificar se um novo dia começou
        if (currentTime - dayStartTimestamp >= 86400) {
            dayStartTimestamp = currentTime;
            dailyRequestCount = 0;
        }
        
        // Calcular quantas requisições estão disponíveis no minuto atual
        int requestsInLastMinute = 0;
        for (int i = 0; i < Math.min(requestIndex, MAX_REQUESTS_PER_MINUTE); i++) {
            int idx = (requestIndex - i - 1) % MAX_REQUESTS_PER_MINUTE;
            if (currentTime - requestTimestamps[idx] < 60) {
                requestsInLastMinute++;
            }
        }
        
        status.put("minuteLimit", MAX_REQUESTS_PER_MINUTE);
        status.put("minuteUsed", requestsInLastMinute);
        status.put("minuteAvailable", MAX_REQUESTS_PER_MINUTE - requestsInLastMinute);
        
        status.put("dailyLimit", MAX_REQUESTS_PER_DAY);
        status.put("dailyUsed", dailyRequestCount);
        status.put("dailyAvailable", MAX_REQUESTS_PER_DAY - dailyRequestCount);
        
        status.put("cacheSize", recommendationsCache.size());
        
        return status;
    }
    //
    
     //Recomendações  dos  controlos ISO 27001, tarefa e se esta em  conformidade ou não usando exclusivamente a API de IA, com controlo de rate limiting
    public String generateRecommendation(String controlo, String tarefa, Boolean isCompliant) {
        // Criar chave de cache baseada nos parâmetros
        String cacheKey = controlo + "|" + tarefa + "|" + isCompliant;
        
        // Verificar se já temos uma recomendação em cache
        if (recommendationsCache.containsKey(cacheKey)) {
            return recommendationsCache.get(cacheKey);
        }
        
        // Verificar se podemos fazer uma nova requisição (rate limiting)
        if (!canMakeRequest()) {
            return "API Gemini: Limite de chamadas atingido. Por favor, tente novamente num minuto.";
        }
        
        // Criar recomendações utilizando a API Gemini
        String response = null;
        try {
            String prompt = buildAIPrompt(controlo, tarefa, isCompliant);
            response = qnAService.getAnswer(prompt);
            
            if (response == null || response.isEmpty()) {
                return "API Gemini: Sem resposta.";
            }
            
            // Extrair a resposta do formato JSON da API Gemini
            String extractedText = extractResponseFromJSON(response);
            if (extractedText != null) {
                recommendationsCache.put(cacheKey, extractedText);
                return extractedText;
            }
            
            // Se chegou aqui, a resposta não está no formato esperado
            return response;
            
        } catch (Exception e) {
            System.err.println("Erro ao chamar a API Gemini: " + e.getMessage());
            e.printStackTrace();
            return "API Gemini: Erro ao chamar a API. Erro: " + e.getMessage();
        }
    }
    
    
    //Extrai o texto da resposta JSON da API Gemini
    private String extractResponseFromJSON(String jsonResponse) {
        try {
            if (jsonResponse.contains("\"text\":")) {
                int textStart = jsonResponse.indexOf("\"text\":");
                if (textStart > 0) {
                    textStart += 8; // avançar além de "text": "
                    int textEnd = jsonResponse.indexOf("\"", textStart);
                    // Se não encontrar o final, tente outra abordagem
                    if (textEnd <= textStart) {
                        textEnd = jsonResponse.indexOf("\"}", textStart);
                        if (textEnd > textStart) {
                            return jsonResponse.substring(textStart, textEnd)
                                    .replace("\\n", "\n")
                                    .replace("\\\"", "\"")
                                    .trim();
                        }
                    } else {
                        return jsonResponse.substring(textStart, textEnd)
                                .replace("\\n", "\n")
                                .replace("\\\"", "\"")
                                .trim();
                    }
                }
            }
            
            // Se o JSON contém "candidates"
            if (jsonResponse.contains("\"candidates\":")) {
                int candidatesStart = jsonResponse.indexOf("\"text\":", jsonResponse.indexOf("\"candidates\":"));
                if (candidatesStart > 0) {
                    candidatesStart += 8; // avançar além de "text": "
                    int candidatesEnd = jsonResponse.indexOf("\"", candidatesStart);
                    if (candidatesEnd <= candidatesStart) {
                        candidatesEnd = jsonResponse.indexOf("\"}", candidatesStart);
                    }
                    if (candidatesEnd > candidatesStart) {
                        return jsonResponse.substring(candidatesStart, candidatesEnd)
                                .replace("\\n", "\n")
                                .replace("\\\"", "\"")
                                .trim();
                    }
                }
            }
            
            // Se não conseguiu extrair o texto, retorna o JSON limpo
            return jsonResponse;
        } catch (Exception e) {
            System.err.println("Erro ao extrair texto da resposta JSON: " + e.getMessage());
            return jsonResponse;
        }
    }
    
   
    //Verifica se posso fazer uma nova requisição (controlo de rate limiting)
    private synchronized boolean canMakeRequest() {
        long currentTime = System.currentTimeMillis() / 1000;
        
        
        if (currentTime - dayStartTimestamp >= 86400) {
            dayStartTimestamp = currentTime;
            dailyRequestCount = 0;
        }
        
        // Verificar limite diário
        if (dailyRequestCount >= MAX_REQUESTS_PER_DAY) {
            System.out.println("Limite diário de requisições atingido: " + dailyRequestCount);
            return false;
        }
        
        // Verificar limite por minuto
        if (requestIndex >= MAX_REQUESTS_PER_MINUTE) {
            // Encontrar o timestamp mais antigo
            int oldestIndex = requestIndex % MAX_REQUESTS_PER_MINUTE;
            
            // Se passou menos de 60 segundos desde a requisição mais antiga, não pode fazer nova requisição
            if (currentTime - requestTimestamps[oldestIndex] < 60) {
                System.out.println("Limite por minuto atingido. Aguardando...");
                return false;
            }
        }
        
        // Registar timestamp da requisição atual
        requestTimestamps[requestIndex % MAX_REQUESTS_PER_MINUTE] = (int)currentTime;
        requestIndex++;
        dailyRequestCount++;
        
        System.out.println("Fazendo requisição " + dailyRequestCount + " do dia. Requisição " + 
                          (requestIndex % MAX_REQUESTS_PER_MINUTE + 1) + " deste minuto.");
        
        return true;
    }
    
    
    //Constrói o prompt para a API de IA especificamente para gerar recomendações
    private String buildAIPrompt(String controlo, String tarefa, Boolean isCompliant) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Atue como um consultor especialista em ISO 27001. ");
        
        // Construir o prompt dependendo do estado de conformidade
        if (isCompliant) {
            prompt.append("O seguinte controlo da ISO 27001 está em CONFORMIDADE: ");
            prompt.append("Item: ").append(controlo).append(" ");
            prompt.append("Pergunta: ").append(tarefa).append(" ");
            prompt.append("Resposta: SIM (Conforme). ");
            prompt.append("Forneça uma RECOMENDAÇÃO de como manter e melhorar este controlo que já está conforme. ");
            prompt.append("Comece a resposta com 'Nota: Conforme.' e em seguida 'Recomendação: ' seguido da sua recomendação.");
        } else {
            prompt.append("O seguinte controlo da ISO 27001 está em NÃO CONFORMIDADE: ");
            prompt.append("Item: ").append(controlo).append(" ");
            prompt.append("Pergunta: ").append(tarefa).append(" ");
            prompt.append("Resposta: NÃO (Não Conforme). ");
            prompt.append("Forneça uma SUGESTÃO detalhada de como implementar este controlo para alcançar a conformidade. ");
            prompt.append("Comece a resposta com 'Nota: Não Conforme.' e em seguida 'Sugestão: ' seguido da sua recomendação.");
        }
        
     // Adiciona a descrição específica do controlo, se existir
        if (controlo != null && CONTROLO_DESCRICOES.containsKey(controlo)) {
            prompt.append(" ").append(CONTROLO_DESCRICOES.get(controlo));
        }
        
        prompt.append(" Seja específico e relevante para este controlo. Limite sua resposta a no máximo 3 frases.");
        
        return prompt.toString();
    }
}