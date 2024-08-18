package com.example.notion.service;

import com.example.notion.dto.ApiResp;
import com.example.notion.dto.UserReq;
import com.example.notion.entity.UserInfo;
import com.example.notion.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ApiResp<?> register(UserReq userReq) {

        userRepository.findByEmailId(userReq.getEmailId()).ifPresent(user -> {
            throw new RuntimeException("User already exists with the email id: " + userReq.getEmailId());
        });

        String hashPwd = passwordEncoder.encode(userReq.getPassword());
        UserInfo user = UserInfo.builder()
                .password(hashPwd)
                .emailId(userReq.getEmailId())
                .username(userReq.getEmailId())
                .createdAt(LocalDateTime.now())
                .xStatus((byte) 1) // 1 is active
                .build();

        userRepository.save(user);
        return ApiResp.builder()
                .status(1)
                .message("User registered successfully")
                .build();
    }


}
