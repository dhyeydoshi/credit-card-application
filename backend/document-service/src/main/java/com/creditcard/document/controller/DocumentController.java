package com.creditcard.document.controller;

import com.creditcard.document.entity.Document;
import com.creditcard.document.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin(origins = "*")
public class DocumentController {
    @Autowired
    private DocumentService documentService;

    @PostMapping("/api/documents/upload")
    public ResponseEntity<?> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId,
            @RequestParam("applicationId") Long applicationId,
            @RequestParam("documentType") String documentType) {

        System.out.println("===== Document Upload Request =====");
        System.out.println("File: " + (file != null ? file.getOriginalFilename() : "null"));
        System.out.println("User ID: " + userId);
        System.out.println("Application ID: " + applicationId);
        System.out.println("Document Type: " + documentType);


        try {
            Document document = documentService.uploadDocument(file, userId, applicationId, documentType);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "File uploaded successfully");
            response.put("documentId", document.getId());
            response.put("fileName", document.getOriginalFileName());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/api/documents/user/{userId}")
    public ResponseEntity<List<Document>> getUserDocuments(@PathVariable Long userId) {
        List<Document> documents = documentService.getUserDocuments(userId);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/api/documents/application/{applicationId}")
    public ResponseEntity<List<Document>> getApplicationDocuments(@PathVariable Long applicationId) {
        List<Document> documents = documentService.getApplicationDocuments(applicationId);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/api/documents/download/{documentId}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long documentId) {
        try {
            Document document = documentService.getDocumentById(documentId);

            Path filePath = Paths.get(document.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + document.getOriginalFileName() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/api/documents/{documentId}")
    public ResponseEntity<?> deleteDocument(@PathVariable Long documentId) {
        try {
            documentService.deleteDocument(documentId);
            return ResponseEntity.ok(Map.of("message", "Document deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/api/documents/{documentId}")
    public ResponseEntity<Document> getDocument(@PathVariable Long documentId) {
        try {
            Document document = documentService.getDocumentById(documentId);
            return ResponseEntity.ok(document);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
