package com.example.notion.service;

import com.example.notion.dto.ApiResp;
import com.example.notion.dto.UserReq;
import com.example.notion.entity.UserInfo;

public interface UserService {
    ApiResp<?> register(UserReq userReq);

    ApiResp<?> login(UserReq userReq);

    UserInfo getUserInfoByEmail(String email);

    ApiResp<?> getUserInfo(Integer userId, String emailId);

    ApiResp<?> getAllUserInfo();

    ApiResp<?> deleteUser(Integer userId, String emailId);
}
