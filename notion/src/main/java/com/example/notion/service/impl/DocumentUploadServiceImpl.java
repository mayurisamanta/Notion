package com.example.notion.service.impl;

import com.example.notion.dto.ApiResp;
import com.example.notion.dto.UserSessionBean;
import com.example.notion.entity.Document;
import com.example.notion.entity.DocumentUpload;
import com.example.notion.entity.UserInfo;
import com.example.notion.exception.DocumentException;
import com.example.notion.repository.DocumentRepository;
import com.example.notion.repository.DocumentUploadRepository;
import com.example.notion.service.DocumentUploadService;
import com.example.notion.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentUploadServiceImpl implements DocumentUploadService {

    private final DocumentRepository documentRepository;

    private final DocumentUploadRepository documentUploadRepository;

    private final UserService userService;

    @Transactional
    @Override
    public ApiResp<?> uploadDocument(Integer documentId, UserSessionBean userSessionBean, List<MultipartFile> files) {

        String emailId = userSessionBean.getEmailId();

        Document document = documentRepository.findByDocumentIdAndStatus(documentId, (byte) 1)
                .orElseThrow(() -> new DocumentException("Document not found"));

        UserInfo userInfo = userService.getUserInfoByEmail(emailId);

        try {
            for (MultipartFile file : files) {
                Map<String, Object> fileDetails = uploadFile(file, documentId, userSessionBean.getEmailId());

                DocumentUpload documentUpload = DocumentUpload.builder()
                        .documentName(fileDetails.get("fileName").toString())
                        .documentType(fileDetails.get("fileExtension").toString())
                        .documentSize((BigDecimal) fileDetails.get("documentSize"))
                        .documentPath(fileDetails.get("documentPath").toString())
                        .document(document)
                        .uploadedAt(LocalDateTime.now())
                        .uploadedBy(userInfo)
                        .status((byte) 1) // Active
                        .build();
                log.info("Email: {}, DocumentId: {} -> File saved successfully: {}", userSessionBean.getEmailId(), documentId, fileDetails);

                documentUploadRepository.save(documentUpload);
            }
        } catch (Exception e) {
            log.error("Email: {}, DocumentId: {} -> Error while uploading the document", emailId, documentId, e);
            throw new DocumentException("Error while uploading the document");
        }


        return ApiResp.builder().status(200).message("Document uploaded successfully").build();
    }

    private Map<String, Object> uploadFile(MultipartFile file, Integer documentId, String emailId) throws IOException {
        Map<String, Object> fileDetails = new HashMap<>();

        log.info("Email: {}, DocumentId: {} -> Uploading the document", emailId, documentId);

        String folderPath = "src/main/resources/documents/" + documentId + "/";
        String fileName = file.getOriginalFilename();
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));

        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File newFile = new File(folderPath.concat(fileName));
        InputStream inputStream = file.getInputStream();
        OutputStream outputStream = new FileOutputStream(newFile);

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        double documentSize = (double) newFile.length() / 1000000; // in MB

        fileDetails.put("fileName", fileName);
        fileDetails.put("fileExtension", fileExtension);
        fileDetails.put("documentSize", BigDecimal.valueOf(documentSize));
        fileDetails.put("documentPath", newFile.getAbsolutePath());

        log.info("Email: {}, DocumentId: {} -> Document uploaded successfully", emailId, documentId);
        outputStream.close();
        inputStream.close();

        return fileDetails;

    }

    @Override
    public void downloadDocument(Integer documentUploadId, UserSessionBean userSessionBean, HttpServletResponse response) {
        DocumentUpload documentUpload = documentUploadRepository.findByDocumentUploadIdAndStatus(documentUploadId, (byte) 1)
                .orElseThrow(() -> new DocumentException("Document Upload not found"));

        String emailId = "test@gmail.com"; //userSessionBean.getEmailId();

        log.info("Email: {}, DocumentUploadId: {} -> Downloading the document", emailId, documentUploadId);
        try {
            String documentPath = documentUpload.getDocumentPath();
            File file = new File(documentPath);
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + documentUpload.getDocumentName());
            response.setHeader("Content-Length", String.valueOf(file.length()));

            OutputStream outputStream = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.flush();
            bufferedInputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            log.error("Email: {}, DocumentUploadId: {} -> Error while downloading the document: {}", emailId, documentUploadId, e.getMessage());
            throw new DocumentException("Error while downloading the document");
        }
    }

    @Transactional
    @Override
    public ApiResp<?> deleteDocument(Integer documentUploadId, UserSessionBean userSessionBean) {
        DocumentUpload documentUpload = documentUploadRepository.findByDocumentUploadIdAndStatus(documentUploadId, (byte) 1)
                .orElseThrow(() -> new DocumentException("Document Upload not found"));

        String emailId = userSessionBean.getEmailId();

        try {
            log.info("Email: {}, DocumentUploadId: {} -> Deleting the document", emailId, documentUploadId);

            documentUpload.setStatus((byte) 0); // Inactive
            documentUploadRepository.save(documentUpload);

            File file = new File(documentUpload.getDocumentPath());
            file.delete();

            log.info("Email: {}, DocumentUploadId: {} -> Document deleted successfully", emailId, documentUploadId);

            return ApiResp.builder().status(200).message("Document deleted successfully").build();
        } catch (Exception e) {
            log.error("Email: {}, DocumentUploadId: {} -> Error while deleting the document", emailId, documentUploadId, e);
            throw new DocumentException("Error while deleting the document");
        }

    }


}
