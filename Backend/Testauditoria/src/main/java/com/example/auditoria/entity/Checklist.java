package com.example.auditoria.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "checklist")
public class Checklist {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String controlos;
	private String fase_implementacao;
	private String tarefas;
	private Boolean emConformidade;
	
	@Column(columnDefinition = "TEXT")
	private String notas;
	
	@Column(columnDefinition = "TEXT")
	private String ai_notas;
	
	private Boolean isHeader;
	private Integer displayorder;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getControlos() {
		return controlos;
	}
	public void setControlos(String controlos) {
		this.controlos = controlos;
	}
	public String getFase_implementacao() {
		return fase_implementacao;
	}
	public void setFase_implementacao(String fase_implementacao) {
		this.fase_implementacao = fase_implementacao;
	}
	public String getTarefas() {
		return tarefas;
	}
	public void setTarefas(String tarefas) {
		this.tarefas = tarefas;
	}
	public Boolean getEmConformidade() {
		return emConformidade;
	}
	public void setEmConformidade(Boolean emConformidade) {
		this.emConformidade = emConformidade;
	}
	public String getNotas() {
		return notas;
	}
	public void setNotas(String notas) {
		this.notas = notas;
	}
	public String getAiNotas() {
		return ai_notas;
	}
	public void setAiNotas(String aiNotas) {
		this.ai_notas = aiNotas;
	}
	public Boolean getIsHeader() {
		return isHeader;
	}
	public void setIsHeader(Boolean isHeader) {
		this.isHeader = isHeader;
	}
	public Integer getDisplayorder() {
		return displayorder;
	}
	public void setDisplayorder(Integer displayorder) {
		this.displayorder = displayorder;
	}
}