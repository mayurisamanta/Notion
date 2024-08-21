package com.example.notion.controller;

import com.example.notion.dto.ApiResp;
import com.example.notion.dto.CreateDocumentReq;
import com.example.notion.dto.UpdateDocumentReq;
import com.example.notion.dto.UserSessionBean;
import com.example.notion.service.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<ApiResp<?>> createDocument(@Valid @RequestBody CreateDocumentReq documentReq, Authentication authentication) {
        UserSessionBean userSessionBean = (UserSessionBean) authentication.getDetails();
        log.info("Email: {} -> Creating the document: {}", userSessionBean.getEmailId(), documentReq);
        return ResponseEntity.ok(documentService.createDocument(documentReq, userSessionBean));
    }

    @PatchMapping("/document")
    public ResponseEntity<ApiResp<?>> updateDocument(@Valid @RequestBody List<UpdateDocumentReq> documentReq,
                                                     Authentication authentication) {
        UserSessionBean userSessionBean = (UserSessionBean) authentication.getDetails();
        log.info("Email: {} -> Updating the document: {}", userSessionBean.getEmailId(), documentReq);
        return ResponseEntity.ok(documentService.updateDocument(documentReq, userSessionBean));
    }

    @GetMapping("/document/{documentId}")
    public ResponseEntity<ApiResp<?>> getDocument(@PathVariable Integer documentId, Authentication authentication) {
        UserSessionBean userSessionBean = (UserSessionBean) authentication.getDetails();
        log.info("Email: {}, DocumentId: {} -> Fetching the document", userSessionBean.getEmailId(), documentId);
        return ResponseEntity.ok(documentService.getDocument(documentId, userSessionBean));
    }

    @DeleteMapping("/document/{documentId}")
    public ResponseEntity<ApiResp<?>> deleteDocument(@PathVariable Integer documentId, Authentication authentication) {
        UserSessionBean userSessionBean = (UserSessionBean) authentication.getDetails();
        log.info("Email: {}, DocumentId: {} -> Deleting the document", userSessionBean.getEmailId(), documentId);
        return ResponseEntity.ok(documentService.deleteDocument(documentId, userSessionBean));
    }


}
