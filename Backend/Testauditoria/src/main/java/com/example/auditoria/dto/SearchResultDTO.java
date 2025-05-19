package com.example.auditoria.dto;




public class SearchResultDTO {
    
    
    private String tipoRecurso;
    private Long id;
    private String titulo;
    private String descricao;
    private String nomeFicheiro;
    private String tipoFicheiro;
    private Long tamanho;
    private String url;
    private String urlVisualizacao;
    private String duracao;
    private String thumbnail;
    private String urlVideo;
    private String modalidade;
    private String icon;
    
    public SearchResultDTO() {}

    public SearchResultDTO(String tipoRecurso, Long id, String nomeFicheiro, 
                          String tipoFicheiro, Long tamanho, String url, String urlVisualizacao) {
        this.tipoRecurso = tipoRecurso;
        this.id = id;
        this.nomeFicheiro = nomeFicheiro;
        this.titulo = nomeFicheiro;  
        this.tipoFicheiro = tipoFicheiro;
        this.tamanho = tamanho;
        this.url = url;
        this.urlVisualizacao = urlVisualizacao;
    }
    

    public SearchResultDTO(String tipoRecurso, Long id, String titulo, 
                          String descricao, String duracao, String thumbnail, String urlVideo) {
        this.tipoRecurso = tipoRecurso;
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.duracao = duracao;
        this.thumbnail = thumbnail;
        this.urlVideo = urlVideo;
    }
    
    
    public SearchResultDTO(String tipoRecurso, Long id, String titulo, 
                          String descricao, String duracao, String modalidade, 
                          String url, String icon) {
        this.tipoRecurso = tipoRecurso;
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.duracao = duracao;
        this.modalidade = modalidade;
        this.url = url;
        this.icon = icon;
    }

    
    public String getTipoRecurso() {
        return tipoRecurso;
    }

    public void setTipoRecurso(String tipoRecurso) {
        this.tipoRecurso = tipoRecurso;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNomeFicheiro() {
        return nomeFicheiro;
    }

    public void setNomeFicheiro(String nomeFicheiro) {
        this.nomeFicheiro = nomeFicheiro;
    }

    public String getTipoFicheiro() {
        return tipoFicheiro;
    }

    public void setTipoFicheiro(String tipoFicheiro) {
        this.tipoFicheiro = tipoFicheiro;
    }

    public Long getTamanho() {
        return tamanho;
    }

    public void setTamanho(Long tamanho) {
        this.tamanho = tamanho;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlVisualizacao() {
        return urlVisualizacao;
    }

    public void setUrlVisualizacao(String urlVisualizacao) {
        this.urlVisualizacao = urlVisualizacao;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public String getModalidade() {
        return modalidade;
    }

    public void setModalidade(String modalidade) {
        this.modalidade = modalidade;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}