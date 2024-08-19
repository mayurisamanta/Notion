package com.example.notion.service;

import com.example.notion.dto.ApiResp;
import com.example.notion.dto.DocumentReq;
import com.example.notion.dto.UserSessionBean;

public interface DocumentService {

    ApiResp<?> createDocument(DocumentReq documentReq, UserSessionBean userSessionBean);
}
