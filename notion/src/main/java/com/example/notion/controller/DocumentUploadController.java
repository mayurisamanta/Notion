package com.example.notion.controller;

import com.example.notion.dto.ApiResp;
import com.example.notion.dto.UserSessionBean;
import com.example.notion.service.DocumentUploadService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DocumentUploadController {

    private final DocumentUploadService documentUploadService;

    @PostMapping(value = "/upload-document/{documentId}", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResp<?>> uploadDocument(@PathVariable Integer documentId, Authentication authentication, List<MultipartFile> files) {
        UserSessionBean userSessionBean = (UserSessionBean) authentication.getDetails();
        log.info("Email: {}, DocumentId: {} -> Uploading the document size: {}", userSessionBean.getEmailId(), documentId, files.size());
        return ResponseEntity.ok(documentUploadService.uploadDocument(documentId, userSessionBean, files));
    }

    @GetMapping("/download-document/{documentUploadId}")
    public void downloadDocument(@PathVariable Integer documentUploadId, Authentication authentication,
                                 HttpServletRequest request, HttpServletResponse response) {
        UserSessionBean userSessionBean = (UserSessionBean) authentication.getDetails();
        log.info("Email: {}, DocumentUploadId: {} -> Downloading the document", userSessionBean.getEmailId(), documentUploadId);
        documentUploadService.downloadDocument(documentUploadId, userSessionBean, response);

    }

    @DeleteMapping("/delete-document/{documentUploadId}")
    public ResponseEntity<ApiResp<?>> deleteDocument(@PathVariable Integer documentUploadId, Authentication authentication) {
        UserSessionBean userSessionBean = (UserSessionBean) authentication.getDetails();
        log.info("Email: {}, DocumentUploadId: {} -> Deleting the document", userSessionBean.getEmailId(), documentUploadId);
        return ResponseEntity.ok(documentUploadService.deleteDocument(documentUploadId, userSessionBean));
    }
}
