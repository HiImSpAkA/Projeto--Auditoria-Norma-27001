package com.example.auditoria.entity;



import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "save_checklist")
public class SaveChecklist {


	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	    @Column(nullable = false)
	    private Long userId;
	    
	    public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Long getUserId() {
			return userId;
		}

		public void setUserId(Long userId) {
			this.userId = userId;
		}

		public String getEmpresa() {
			return empresa;
		}

		public void setEmpresa(String empresa) {
			this.empresa = empresa;
		}

		public Date getDataAvaliacao() {
			return dataAvaliacao;
		}

		public void setDataAvaliacao(Date dataAvaliacao) {
			this.dataAvaliacao = dataAvaliacao;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

		public byte[] getData() {
			return data;
		}

		public void setData(byte[] data) {
			this.data = data;
		}

		public Date getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(Date createdAt) {
			this.createdAt = createdAt;
		}

		@Column(nullable = false)
	    private String empresa;
	    
	    @Column(name = "data_avaliacao")
	    private Date dataAvaliacao;
	    
	    @Column(nullable = false)
	    private String fileName;
	    
	    @Column(nullable = false)
	    private String contentType;
	    
	    @Lob
	    @Column(nullable = false, columnDefinition = "LONGBLOB")
	    private byte[] data;
	    
	    @Column(name = "created_at")
	    private Date createdAt;

	    public SaveChecklist() {
	        this.createdAt = new Date();
	    }

	    public SaveChecklist(Long userId, String empresa, Date dataAvaliacao, String fileName, String contentType, byte[] data) {
	        this.userId = userId;
	        this.empresa = empresa;
	        this.dataAvaliacao = dataAvaliacao;
	        this.fileName = fileName;
	        this.contentType = contentType;
	        this.data = data;
	        this.createdAt = new Date();
}
}