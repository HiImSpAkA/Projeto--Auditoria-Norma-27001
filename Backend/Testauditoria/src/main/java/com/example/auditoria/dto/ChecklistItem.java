package com.example.auditoria.dto;

public class ChecklistItem {
    private String controlos;
    private String fases_implementacao;
    private String tarefas;
    private Boolean emConformidade;
    private String notas;
    private String ai_notas;
    private Boolean isHeader;
    private Integer displayOrder;

    public ChecklistItem() {
    }

    public ChecklistItem(String controlos, String fases_implementacao, String tarefas, Boolean emConformidade,
                         String notas, String ai_notas, Boolean isHeader, Integer displayOrder) {
        this.controlos = controlos;
        this.fases_implementacao = fases_implementacao;
        this.tarefas = tarefas;
        this.emConformidade = emConformidade;
        this.notas = notas;
        this.ai_notas = ai_notas;
        this.isHeader = isHeader;
        this.displayOrder = displayOrder;
    }

    public String getControlos() {
        return controlos;
    }

    public void setControlos(String controlos) {
        this.controlos = controlos;
    }

    public String getFases_implementacao() {
        return fases_implementacao;
    }

    public void setFases_implementacao(String fases_implementacao) {
        this.fases_implementacao = fases_implementacao;
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

    public String getAi_notas() {
        return ai_notas;
    }

    public void setAi_notas(String ai_notas) {
        this.ai_notas = ai_notas;
    }

    public Boolean getIsHeader() {
        return isHeader;
    }

    public void setIsHeader(Boolean isHeader) {
        this.isHeader = isHeader;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
