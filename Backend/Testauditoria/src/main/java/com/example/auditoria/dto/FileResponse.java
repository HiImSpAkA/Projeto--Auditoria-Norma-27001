package com.example.auditoria.dto;

public class FileResponse {
    private String fileName;
    private String url;
    private String viewUrl;
    private String fileType;
    private long size;
    private String descricao;

    public FileResponse(String fileName, String url, String fileType, long size) {
        this.fileName = fileName;
        this.url = url;
        this.fileType = fileType;
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public String getUrl() {
        return url;
    }

    public String getFileType() {
        return fileType;
    }

    public long getSize() {
        return size;
    }

    public String getViewUrl() {
        return viewUrl;
    }

    public void setViewUrl(String viewUrl) {
        this.viewUrl = viewUrl;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}