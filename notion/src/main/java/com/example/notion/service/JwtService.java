package com.example.notion.service;

import com.example.notion.dto.UserReq;

/**
 * Interface for JwtService
 */
public interface JwtService {
    String generateToken(UserReq userReq);
}
