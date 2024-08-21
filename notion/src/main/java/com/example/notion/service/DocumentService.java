package com.example.notion.service;

import com.example.notion.dto.ApiResp;
import com.example.notion.dto.CreateDocumentReq;
import com.example.notion.dto.UpdateDocumentReq;
import com.example.notion.dto.UserSessionBean;

import java.util.List;

public interface DocumentService {

    ApiResp<?> createDocument(CreateDocumentReq documentReq, UserSessionBean userSessionBean);

    ApiResp<?> updateDocument(List<UpdateDocumentReq> documentReq, UserSessionBean userSessionBean);

    ApiResp<?> getDocument(Integer documentId, UserSessionBean userSessionBean);

    ApiResp<?> deleteDocument(Integer documentId, UserSessionBean userSessionBean);
}
