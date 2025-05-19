package com.example.auditoria.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.auditoria.service.QnAService;



@RestController

@RequestMapping("/api/qna")
@CrossOrigin(origins = "http://localhost:5173")

public class AIchatController {
	
	private QnAService qnAService;

	public AIchatController(QnAService qnAService) {
        this.qnAService = qnAService;
    }
	@PostMapping("/ask")
	public ResponseEntity<String> askQuestion(@RequestBody Map<String, String> payload){
		String question = payload.get("question");
		String answer = qnAService.getAnswer(question);
		return ResponseEntity.ok(answer);
	}
	

}
