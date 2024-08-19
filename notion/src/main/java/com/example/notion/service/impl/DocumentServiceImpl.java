package com.example.notion.service.impl;

import com.example.notion.dto.ApiResp;
import com.example.notion.dto.DocumentReq;
import com.example.notion.dto.UserSessionBean;
import com.example.notion.entity.Document;
import com.example.notion.entity.UserInfo;
import com.example.notion.exception.DocumentException;
import com.example.notion.master.entity.MasterTemplate;
import com.example.notion.repository.DocumentRepository;
import com.example.notion.service.DocumentService;
import com.example.notion.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Implementation of Document related operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    private final UserService userService;

    /**
     * Create a document
     *
     * @param documentReq
     * @param userSessionBean
     */
    @Override
    public ApiResp<?> createDocument(DocumentReq documentReq, UserSessionBean userSessionBean) {

        String emailId = userSessionBean.getEmailId();
        UserInfo userInfo = userService.getUserInfoByEmail(emailId);

        try {
            createDocumentRecursively(documentReq, documentReq.getParentId(), userInfo);

            return ApiResp.builder()
                    .status(200)
                    .message("Document created successfully")
                    .build();
        } catch (Exception e) {
            log.error("Email: {} -> Error while creating the document", emailId, e);
            throw new DocumentException("Error while creating the document");
        }

    }

    /**
     * Create the document recursively
     *
     * @param documentReq
     * @param parentId
     * @param userInfo
     */
    private Document createDocumentRecursively(DocumentReq documentReq, Integer parentId, UserInfo userInfo) {
        log.info("Email: {} -> Creating the document recursively: {}", userInfo.getEmailId(), documentReq);
        if (documentReq == null && parentId == null) return null;

        if (documentReq == null) {
            return documentRepository.findById(parentId).orElse(null);
        }

        Document parent = createDocumentRecursively(documentReq.getParentDocument(), documentReq.getParentId(), userInfo);

        log.info("Email: {} -> Parent document: {}", userInfo.getEmailId(), parent);
        Document document = new Document();
        document.setParent(parent);
        document.setTitle(documentReq.getTitle());
        document.setContent(documentReq.getContent());
        document.setCreatedBy(userInfo);
        document.setCreatedAt(LocalDateTime.now());
        document.setMasterTemplate(documentReq.getTemplateId() != null ? new MasterTemplate(documentReq.getTemplateId()) : null);
        document.setStatus((byte) 1); // Active
        return documentRepository.save(document);
    }
}
