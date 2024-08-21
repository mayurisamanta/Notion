package com.example.notion.service.impl;

import com.example.notion.dto.*;
import com.example.notion.entity.Document;
import com.example.notion.entity.DocumentHistory;
import com.example.notion.entity.UserInfo;
import com.example.notion.exception.DocumentException;
import com.example.notion.master.dto.TemplateResp;
import com.example.notion.master.entity.MasterTemplate;
import com.example.notion.repository.DocumentHistoryRepository;
import com.example.notion.repository.DocumentRepository;
import com.example.notion.service.DocumentService;
import com.example.notion.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of Document related operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    private final UserService userService;

    private final DocumentHistoryRepository documentHistoryRepository;

    /**
     * Create a document
     *
     * @param documentReq
     * @param userSessionBean
     */
    @Transactional
    @Override
    public ApiResp<?> createDocument(CreateDocumentReq documentReq, UserSessionBean userSessionBean) {

        String emailId = userSessionBean.getEmailId();
        UserInfo userInfo = userService.getUserInfoByEmail(emailId);

        try {
            Document parent = null;
            if (documentReq.getParentId() != null) {
                parent = documentRepository.findByDocumentIdAndStatus(documentReq
                                .getParentId(), (byte) 1)
                        .orElseThrow(() -> new DocumentException("Parent document not found"));
            }
            createDocumentRecursively(documentReq, parent, userInfo);

            return ApiResp.builder()
                    .status(200)
                    .message("Document created successfully")
                    .build();
        } catch (DocumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Email: {} -> Error while creating the document", emailId, e);
            throw new DocumentException("Error while creating the document");
        }

    }

    /**
     * Create the document recursively
     *
     * @param documentReq
     * @param parent
     * @param userInfo
     */
    private void createDocumentRecursively(CreateDocumentReq documentReq, Document parent, UserInfo userInfo) {
        Document document = Document.builder()
                .title(documentReq.getTitle())
                .content(documentReq.getContent())
                .status((byte) 1) // Active
                .createdBy(userInfo)
                .createdAt(LocalDateTime.now())
                .parent(parent)
                .masterTemplate(documentReq.getTemplateId() != null ? new MasterTemplate(documentReq.getTemplateId()) : null)
                .build();


        log.info("Email: {} -> Creating the document: {}", userInfo.getEmailId(), document);
        document = documentRepository.save(document);

        DocumentHistory documentHistory = DocumentHistory.builder()
                .title(documentReq.getTitle())
                .content(documentReq.getContent())
                .document(document)
                .createdBy(userInfo)
                .createdAt(LocalDateTime.now())
                .version(documentHistoryRepository.findLastVersionByDocument(document).orElse(0) + 1)
                .build();

        log.info("Email: {} -> Creating the document history: {}", userInfo.getEmailId(), documentHistory);
        documentHistoryRepository.save(documentHistory);

        if (documentReq.getChildDocuments() != null) {
            for (CreateDocumentReq childDocumentReq : documentReq.getChildDocuments()) {
                createDocumentRecursively(childDocumentReq, document, userInfo);
            }
        }

    }

    /**
     * Update the document
     *
     * @param documentReq
     * @param userSessionBean
     */
    @Transactional
    @Override
    public ApiResp<?> updateDocument(List<UpdateDocumentReq> documentReq, UserSessionBean userSessionBean) {

        String emailId = userSessionBean.getEmailId();
        UserInfo userInfo = userService.getUserInfoByEmail(emailId);

        try {
            for (UpdateDocumentReq updateDocumentReq : documentReq) {
                Integer documentId = updateDocumentReq.getDocumentId();
                log.info("Email: {}, DocumentId: {} -> Going to update the document", emailId, documentId);
                Document document = documentRepository.findByDocumentIdAndStatus(updateDocumentReq.getDocumentId(), (byte) 1)
                        .orElseThrow(() -> new DocumentException("Document not found"));

                log.info("Email: {}, DocumentId: {} -> Found the document: {}", emailId, documentId, document);
                Document parent = null;
                if (updateDocumentReq.getParentId() != null) {
                    parent = documentRepository.findByDocumentIdAndStatus(updateDocumentReq
                                    .getParentId(), (byte) 1)
                            .orElseThrow(() -> new DocumentException("Parent document not found"));

                    log.info("Email: {}, DocumentId: {} -> Found the parent document: {}", emailId, documentId, parent);
                }
                document.setTitle(updateDocumentReq.getTitle());
                document.setContent(updateDocumentReq.getContent());
                document.setMasterTemplate(updateDocumentReq.getTemplateId() != null ? new MasterTemplate(updateDocumentReq.getTemplateId()) : null);
                document.setParent(parent);
                document.setUpdatedBy(userInfo);
                document.setUpdatedAt(LocalDateTime.now());
                document.setStatus((byte) 1); // Active

                DocumentHistory documentHistory = DocumentHistory.builder()
                        .title(document.getTitle())
                        .content(document.getContent())
                        .document(document)
                        .createdBy(userInfo)
                        .createdAt(LocalDateTime.now())
                        .version(documentHistoryRepository.findLastVersionByDocument(document).orElse(0) + 1)
                        .build();

                log.info("Email: {}, DocumentId: {} -> Updated the document: {}", emailId, documentId, document);
                documentRepository.save(document);

                log.info("Email: {}, DocumentId: {} -> Created the document history: {}", emailId, documentId, documentHistory);
                documentHistoryRepository.save(documentHistory);
            }

            return ApiResp.builder()
                    .status(200)
                    .message("Document updated successfully")
                    .build();
        } catch (DocumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Email: {} -> Error while updating the document", emailId, e);
            throw new DocumentException("Error while updating the document");
        }
    }

    /**
     * Get the document
     *
     * @param documentId
     * @param userSessionBean
     */
    @Override
    public ApiResp<?> getDocument(Integer documentId, UserSessionBean userSessionBean) {

        String emailId = userSessionBean.getEmailId();

        DocumentResp documentResp = getDocumentRecursively(documentId, emailId);
        log.info("Email: {}, DocumentId: {} -> Retrieved the document: {}", emailId, documentId, documentResp);

        return ApiResp.builder()
                .status(200)
                .message("Document retrieved successfully")
                .data(documentResp)
                .build();

    }

    /**
     * Get the document recursively
     *
     * @param documentId
     * @param emailId
     */
    private DocumentResp getDocumentRecursively(Integer documentId, String emailId) {

        Document document = documentRepository.findByDocumentIdAndStatus(documentId, (byte) 1)
                .orElseThrow(() -> new DocumentException("Document not found"));

        try {
            log.info("Email: {}, DocumentId: {} -> Found the document: {}", emailId, documentId, document);
            DocumentResp documentResp = DocumentResp.builder()
                    .documentId(document.getDocumentId())
                    .title(document.getTitle())
                    .content(document.getContent())
                    .template(document.getMasterTemplate() != null ? TemplateResp.builder()
                            .templateId(document.getMasterTemplate().getTemplateId())
                            .title(document.getMasterTemplate().getTitle())
                            .content(document.getMasterTemplate().getContent())
                            .build() : null)
                    .childDocument(new ArrayList<>())
                    .build();

            List<Document> childDocuments = documentRepository.findByParentAndStatus(document, (byte) 1);
            log.info("Email: {}, DocumentId: {} -> Found the child documents: {}", emailId, documentId, childDocuments);
            if (childDocuments != null && !childDocuments.isEmpty()) {
                for (Document childDocument : childDocuments) {
                    documentResp.getChildDocument().add(getDocumentRecursively(childDocument.getDocumentId(), emailId));
                }
            }

            return documentResp;
        } catch (Exception e) {
            log.error("Email: {} -> Error while getting the document", emailId, e);
            throw new DocumentException("Error while getting the document");
        }

    }

    /**
     * Delete the document
     *
     * @param documentId
     * @param userSessionBean
     */
    @Transactional
    @Override
    public ApiResp<?> deleteDocument(Integer documentId, UserSessionBean userSessionBean) {

        String emailId = userSessionBean.getEmailId();
        UserInfo userInfo = userService.getUserInfoByEmail(emailId);

        deleteDocumentRecursively(documentId, userInfo);

        return ApiResp.builder()
                .status(200)
                .message("Document deleted successfully")
                .build();

    }

    /**
     * Delete the document recursively
     *
     * @param documentId
     * @param userInfo
     */
    private void deleteDocumentRecursively(Integer documentId, UserInfo userInfo) {
        log.info("Email: {}, DocumentId: {} -> Going to delete the document", userInfo.getEmailId(), documentId);
        Document document = documentRepository.findByDocumentIdAndStatus(documentId, (byte) 1)
                .orElseThrow(() -> new DocumentException("Document not found"));

        try {
            log.info("Email: {}, DocumentId: {} -> Found the document: {}", userInfo.getEmailId(), documentId, document);
            document.setStatus((byte) 0); // Inactive
            document.setUpdatedBy(userInfo);
            document.setUpdatedAt(LocalDateTime.now());
            documentRepository.save(document);

            List<Document> childDocuments = documentRepository.findByParentAndStatus(document, (byte) 1);
            log.info("Email: {}, DocumentId: {} -> Found the child documents: {}", userInfo.getEmailId(), documentId, childDocuments);
            if (childDocuments != null && !childDocuments.isEmpty()) {
                for (Document childDocument : childDocuments) {
                    deleteDocumentRecursively(childDocument.getDocumentId(), userInfo);
                }
            }
        } catch (Exception e) {
            log.error("Email: {} -> Error while deleting the document", userInfo.getEmailId(), e);
            throw new DocumentException("Error while deleting the document");
        }

    }

    /**
     * Get the document version
     *
     * @param documentId
     * @param version
     * @param userSessionBean
     */
    @Override
    public ApiResp<?> getDocumentVersion(Integer documentId, Integer version, UserSessionBean userSessionBean) {

        String emailId = userSessionBean.getEmailId();

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentException("Document not found"));

        try {
            log.info("Email: {}, DocumentId: {} -> Found the document: {}", emailId, documentId, document);
            Pageable pageable = PageRequest.of(0, version);
            List<DocumentHistory> documentHistory = documentHistoryRepository.findByDocumentOrderByVersionDesc(document, pageable);

            List<DocumentResp> documentResp = new ArrayList<>();
            for (DocumentHistory history : documentHistory) {
                documentResp.add(DocumentResp.builder()
                        .documentId(history.getDocument().getDocumentId())
                        .title(history.getTitle())
                        .content(history.getContent())
                        .template(history.getDocument().getMasterTemplate() != null ? TemplateResp.builder()
                                .templateId(history.getDocument().getMasterTemplate().getTemplateId())
                                .title(history.getDocument().getMasterTemplate().getTitle())
                                .content(history.getDocument().getMasterTemplate().getContent())
                                .build() : null)
                        .build());
            }

            return ApiResp.builder()
                    .status(200)
                    .message("Document version retrieved successfully")
                    .data(documentResp)
                    .build();
        } catch (DocumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Email: {} -> Error while getting the document version", emailId, e);
            throw new DocumentException("Error while getting the document version");
        }

    }
}
