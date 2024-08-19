package com.example.notion.controller;

import com.example.notion.dto.ApiResp;
import com.example.notion.dto.DocumentReq;
import com.example.notion.dto.UserSessionBean;
import com.example.notion.service.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Apis for Document related operations
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class DocumentController {

    private final DocumentService documentService;

    /**
     * createDocument
     * @param documentReq
     * @param authentication
     */
    @PostMapping("/document")
    public ResponseEntity<ApiResp<?>> createDocument(@Valid @RequestBody DocumentReq documentReq, Authentication authentication) {
        UserSessionBean userSessionBean = (UserSessionBean) authentication.getDetails();
        log.info("Email: {} -> Creating the document: {}", userSessionBean.getEmailId(), documentReq);
        return ResponseEntity.ok(documentService.createDocument(documentReq, userSessionBean));
    }
}
