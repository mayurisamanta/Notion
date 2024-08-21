package com.example.notion.service;

import com.example.notion.dto.ApiResp;
import com.example.notion.dto.UserSessionBean;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentUploadService {

    ApiResp<?> uploadDocument(Integer documentId, UserSessionBean userSessionBean, List<MultipartFile> files);

    void downloadDocument(Integer documentUploadId, UserSessionBean userSessionBean, HttpServletResponse response);

    ApiResp<?> deleteDocument(Integer documentUploadId, UserSessionBean userSessionBean);
}
