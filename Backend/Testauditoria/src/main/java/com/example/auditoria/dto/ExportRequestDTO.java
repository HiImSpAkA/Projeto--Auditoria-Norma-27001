package com.example.auditoria.dto;

import java.util.List;

public class ExportRequestDTO {
    private List<ChecklistItem> checklistItems;
    private String reportTitle;
    private String companyName;
    private String assessmentDate;
    private Long userId;

    public ExportRequestDTO() {
    }

    public ExportRequestDTO(List<ChecklistItem> checklistItems, String reportTitle, String companyName, String assessmentDate, Long userId) {
        this.checklistItems = checklistItems;
        this.reportTitle = reportTitle;
        this.companyName = companyName;
        this.assessmentDate = assessmentDate;
        this.userId = userId;
    }

    public List<ChecklistItem> getChecklistItems() {
        return checklistItems;
    }

    public void setChecklistItems(List<ChecklistItem> checklistItems) {
        this.checklistItems = checklistItems;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(String assessmentDate) {
        this.assessmentDate = assessmentDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}