package com.example.auditoria.service;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;
import com.example.auditoria.dto.ChecklistItem;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class PDFService {

    public byte[] generatePDF(List<ChecklistItem> checklistItems, String companyName, String assessmentDate) {
        try {
            // Debug: imprimir o conteúdo dos controlos antes de criar o PDF
            System.out.println("A criar o PDF com " + checklistItems.size() + " itens");
            for (ChecklistItem item : checklistItems) {
                if (!item.getIsHeader()) {
                    System.out.println("Item: " + item.getControlos() + 
                                       ", Fase: " + item.getFases_implementacao() + 
                                       ", AI Notas: " + (item.getAi_notas() != null ? item.getAi_notas().substring(0, Math.min(50, item.getAi_notas().length())) + "..." : "null"));
                }
            }
            // Inicializa o documento PDF
            Document document = new Document(PageSize.A4);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            
            
            writer.setPageEvent(new HeaderFooterPageEvent(companyName));
            
            document.open();
            document.addTitle("ISO 27001 Checklist Anexo A");
            document.addCreator("Site Apoio A Norma 27001");
            
            // Adiciona título ao documento
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
            Paragraph title = new Paragraph("ISO 27001 Checklist Anexo A", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);
            
            // Adiciona informações da empresa e data
            Font infoFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            Paragraph info = new Paragraph();
            info.add(new Chunk("Empresa: " + companyName + "\n", infoFont));
            info.add(new Chunk("Data da Avaliação: " + assessmentDate + "\n", infoFont));
            info.setSpacingAfter(20);
            document.add(info);
            
            // Cria a tabela principal do relatório
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            
            // Define larguras das colunas
            float[] columnWidths = {2f, 2.5f, 2f, 2.5f, 2.5f};
            table.setWidths(columnWidths);
            
            // Adiciona cabeçalho da tabela
            addTableHeader(table);
            
            // Adiciona dados da tabela
            for (ChecklistItem item : checklistItems) {
                if (item.getIsHeader()) {
                    addSectionHeader(table, item.getTarefas());
                } else {
                    addItemRow(table, item);
                }
            }
            
            document.add(table);
            document.close();
            
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate PDF: " + e.getMessage(), e);
        }
    }
    // Adiciona o cabeçalho da tabela
    private void addTableHeader(PdfPTable table) {
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.WHITE);
        
        PdfPCell controlCell = new PdfPCell(new Phrase("CONTROLO", headerFont));
        controlCell.setBackgroundColor(new BaseColor(86, 121, 168));
        controlCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        controlCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        controlCell.setPadding(5);
        
        PdfPCell phaseCell = new PdfPCell(new Phrase("FASES DE IMPLEMENTAÇÃO", headerFont));
        phaseCell.setBackgroundColor(new BaseColor(86, 121, 168));
        phaseCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        phaseCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        phaseCell.setPadding(5);
        
        PdfPCell taskCell = new PdfPCell(new Phrase("TAREFA", headerFont));
        taskCell.setBackgroundColor(new BaseColor(86, 121, 168));
        taskCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        taskCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        taskCell.setPadding(5);
        
        PdfPCell compliantCell = new PdfPCell(new Phrase("CONFORMIDADE", headerFont));
        compliantCell.setBackgroundColor(new BaseColor(86, 121, 168));
        compliantCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        compliantCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        compliantCell.setPadding(5);
        
        PdfPCell notesCell = new PdfPCell(new Phrase("RECOMENDAÇÕES/NOTAS", headerFont));
        notesCell.setBackgroundColor(new BaseColor(86, 121, 168));
        notesCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        notesCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        notesCell.setPadding(5);
        
        table.addCell(controlCell);
        table.addCell(phaseCell);
        table.addCell(taskCell);
        table.addCell(compliantCell);
        table.addCell(notesCell);
    }
    // Adiciona o cabeçalho da tabela
    private void addSectionHeader(PdfPTable table, String headerText) {
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
        
        PdfPCell cell = new PdfPCell(new Phrase(headerText, headerFont));
        cell.setColspan(5);
        cell.setBackgroundColor(new BaseColor(230, 230, 230));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        
        table.addCell(cell);
    }
    // Adiciona uma linha para cada contorlo da checklist à tabela
    private void addItemRow(PdfPTable table, ChecklistItem item) {
        Font cellFont = new Font(Font.FontFamily.HELVETICA, 10);
        Font notaFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
        Font conformeFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.GREEN);
        Font naoConformeFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.RED);
        Font symbolFont = new Font(Font.FontFamily.ZAPFDINGBATS, 12);
        
        // Célula para o código do controle
        PdfPCell controlCell = new PdfPCell(new Phrase(item.getControlos() != null ? item.getControlos() : "-", cellFont));
        controlCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        controlCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        controlCell.setPadding(4);
        
        // Célula para a fase de implementação
        PdfPCell phaseCell = new PdfPCell(new Phrase(item.getFases_implementacao() != null ? item.getFases_implementacao() : "-", cellFont));
        phaseCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        phaseCell.setPadding(4);
        
        // Célula para a tarefa
        PdfPCell taskCell = new PdfPCell(new Phrase(item.getTarefas() != null ? item.getTarefas() : "-", cellFont));
        taskCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        taskCell.setPadding(4);
        
        // Célula para o status de conformidade
        PdfPCell compliantCell = new PdfPCell();
        compliantCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        compliantCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        compliantCell.setPadding(4);
        
        // Adiciona símbolos diferentes para conforme (✓) e não conforme (✗)
        if (item.getEmConformidade() != null) {
            if (item.getEmConformidade()) {
                // Símbolo checkmark para conforme
                Phrase checkmark = new Phrase("✓", symbolFont);
                compliantCell.addElement(checkmark);
                compliantCell.setBackgroundColor(new BaseColor(230, 255, 230)); // Verde claro para conformidade
            } else {
                // Símbolo X para não conforme
                Phrase xmark = new Phrase("✗", symbolFont);
                compliantCell.addElement(xmark);
                compliantCell.setBackgroundColor(new BaseColor(255, 230, 230)); // Vermelho claro para não conformidade
            }
        }
        
        // Célula para recomendações e notas
        PdfPCell notesCell = new PdfPCell();
        notesCell.setPadding(4);
        
        // Processar e formatar as recomendações de IA
        if (item.getAi_notas() != null && !item.getAi_notas().isEmpty()) {
            String aiNotas = item.getAi_notas();
            
            // Formatar "Nota: Conforme" ou "Nota: Não Conforme"
            Paragraph notasParagraph = new Paragraph();
            
            if (aiNotas.contains("Nota: Conforme")) {
                notasParagraph.add(new Chunk("Nota: Conforme", conformeFont));
                aiNotas = aiNotas.replace("Nota: Conforme.", "").trim();
            } else if (aiNotas.contains("Nota: Não Conforme")) {
                notasParagraph.add(new Chunk("Nota: Não Conforme", naoConformeFont));
                aiNotas = aiNotas.replace("Nota: Não Conforme.", "").trim();
            }
            
            notesCell.addElement(notasParagraph);
            
            // Adicionar a recomendação ou sugestão
            Paragraph recParagraph = new Paragraph();
            
            if (aiNotas.contains("Recomendação:")) {
                int recIndex = aiNotas.indexOf("Recomendação:");
                String titulo = "Recomendação:";
                String conteudo = aiNotas.substring(recIndex + titulo.length()).trim();
                
                recParagraph.add(new Chunk(titulo + " ", notaFont));
                recParagraph.add(new Chunk(conteudo, cellFont));
                
            } else if (aiNotas.contains("Sugestão:")) {
                int sugIndex = aiNotas.indexOf("Sugestão:");
                String titulo = "Sugestão:";
                String conteudo = aiNotas.substring(sugIndex + titulo.length()).trim();
                
                recParagraph.add(new Chunk(titulo + " ", notaFont));
                recParagraph.add(new Chunk(conteudo, cellFont));
                
            } else {
                // Se não encontrar os marcadores, adicionar o texto completo
                recParagraph.add(new Chunk(aiNotas, cellFont));
            }
            
            notesCell.addElement(recParagraph);
        }
        
        // Se não tiver nenhuma nota ou recomendação, adiciona texto informativo
        if ((notesCell.getCompositeElements() == null || notesCell.getCompositeElements().isEmpty())) {
            notesCell.addElement(new Phrase("Sem recomendações ou notas", new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY)));
        }
        
        notesCell.setVerticalAlignment(Element.ALIGN_TOP);
        // Adiciona todas as células à tabela
        table.addCell(controlCell);
        table.addCell(phaseCell);
        table.addCell(taskCell);
        table.addCell(compliantCell);
        table.addCell(notesCell);
    }
    
    // Classe interna para gerir cabeçalhos e rodapés de página
    class HeaderFooterPageEvent extends PdfPageEventHelper {
        private final String companyName;
        
        public HeaderFooterPageEvent(String companyName) {
            this.companyName = companyName;
        }
        
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            
            // Adiciona rodapé com número de página e informações da empresa
            Font footerFont = new Font(Font.FontFamily.HELVETICA, 8);
            Phrase footer = new Phrase("ISO 27001 Checklist Anexo A - " + companyName + " - Página " + 
                                       writer.getPageNumber(), footerFont);
            
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, footer,
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.bottom() - 10, 0);
            
            // Adiciona data no canto inferior direito
            Phrase datePhrase = new Phrase("Criado em: " + new Date().toString(), footerFont);
            ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, datePhrase,
                    document.right(), document.bottom() - 10, 0);
        }
    }
}