package com.example.notion.service;

import com.example.notion.dto.ApiResp;
import com.example.notion.dto.UserReq;
import com.example.notion.entity.UserInfo;
import com.example.notion.exception.UserException;
import com.example.notion.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * UserService
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * register the user
     *
     * @param userReq
     */
    public ApiResp<?> register(UserReq userReq) {

        String emailId = userReq.getEmailId();

        userRepository.findByEmailId(userReq.getEmailId()).ifPresent(user -> {
            log.error("Email: {} -> User already exists", emailId);
            throw new UserException("User already exists with the email id: " + emailId);
        });

        try {
            String hashPwd = passwordEncoder.encode(userReq.getPassword());
            log.info("Email: {} -> Hashed password: {}", emailId, hashPwd);
            UserInfo user = UserInfo.builder()
                    .password(hashPwd)
                    .emailId(userReq.getEmailId())
                    .username(userReq.getEmailId())
                    .createdAt(LocalDateTime.now())
                    .xStatus((byte) 1) // 1 is active
                    .build();

            log.info("Email: {} -> Going to save the user: {}", emailId, user);
            userRepository.save(user);
            return ApiResp.builder()
                    .status(HttpStatus.OK.value())
                    .message("User registered successfully")
                    .build();
        } catch (Exception e) {
            log.error("Email: {} -> Error while registering the user: {}", emailId, e.getMessage());
            throw new UserException("Email: " + emailId + " -> Error while registering the user: " + e.getMessage());
        }

    }

}
