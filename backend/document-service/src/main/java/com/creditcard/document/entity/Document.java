package com.creditcard.document.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long applicationId;
    private String fileName;
    private String originalFileName;
    private String filePath;
    private String fileType;
    private Long fileSize;
    private String documentType;

    @Enumerated(EnumType.STRING)
    private DocumentStatus status = DocumentStatus.UPLOADED;

    private LocalDateTime uploadedAt = LocalDateTime.now();

    public Document(){}

    public Long getId() {return id;}
    public void setId(Long id) {
        this.id = id;}

    public Long getUserId() {
        return userId;}
    public void setUserId(Long userId) {
        this.userId = userId;}


    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getOriginalFileName() { return originalFileName; }
    public void setOriginalFileName(String originalFileName) { this.originalFileName = originalFileName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }

    public DocumentStatus getStatus() { return status; }
    public void setStatus(DocumentStatus status) { this.status = status; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }

}

enum DocumentStatus {
    UPLOADED, VERIFIED, REJECTED
}

