package com.creditcard.document.service;

import com.creditcard.document.entity.Document;
import com.creditcard.document.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public Document uploadDocument(MultipartFile file, Long userId, Long applicationId, String documentType){
        validateFile(file);

        try{
            Path uploadPath = Paths.get(uploadDir);
            Files.createDirectories(uploadPath);

            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            Document document = new Document();
            document.setUserId(userId);
            document.setApplicationId(applicationId);
            document.setFileName(fileName);
            document.setOriginalFileName(file.getOriginalFilename());
            document.setFilePath(filePath.toString());
            document.setFileType(file.getContentType());
            document.setFileSize(file.getSize());
            document.setDocumentType(documentType);

            return documentRepository.save(document);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }
    public List<Document> getUserDocuments(Long userId) {
        return documentRepository.findByUserId(userId);
    }

    public List<Document> getApplicationDocuments(Long applicationId) {
        return documentRepository.findByApplicationId(applicationId);
    }

    public Document getDocumentById(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));
    }

    public void deleteDocument(Long documentId) {
        Document document = getDocumentById(documentId);

        try {
            Path filePath = Paths.get(document.getFilePath());
            Files.deleteIfExists(filePath);

            documentRepository.delete(document);

        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + e.getMessage());
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Cannot upload empty file");
        }

        if (file.getSize() > 10 * 1024 * 1024) {
            throw new RuntimeException("File size cannot exceed 10MB");
        }

        String contentType = file.getContentType();
        if (contentType == null ||
                (!contentType.startsWith("image/") &&
                        !contentType.equals("application/pdf") &&
                        !contentType.startsWith("application/vnd.openxmlformats-officedocument"))) {
            throw new RuntimeException("Only images, PDF, and Office documents are allowed");
        }
    }

}
