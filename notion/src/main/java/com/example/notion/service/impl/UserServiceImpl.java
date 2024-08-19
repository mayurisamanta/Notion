package com.example.notion.service.impl;

import com.example.notion.dto.ApiResp;
import com.example.notion.dto.UserReq;
import com.example.notion.entity.UserInfo;
import com.example.notion.exception.UserException;
import com.example.notion.repository.UserRepository;
import com.example.notion.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * UserService
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * register the user
     *
     * @param userReq
     */
    @Transactional
    public ApiResp<?> register(UserReq userReq) {

        String emailId = userReq.getEmailId();

        userRepository.findByEmailIdAndStatus(userReq.getEmailId(), (byte) 1).ifPresent(user -> {
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
                    .status((byte) 1) // 1 is active
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

    /**
     * login the user
     *
     * @param userReq
     */
    @Override
    @Transactional
    public ApiResp<?> login(UserReq userReq) {

        try {
            userRepository.updateLastLoginAt(userReq.getEmailId());
            log.info("Email: {} -> User logged in successfully", userReq.getEmailId());
            return ApiResp
                    .builder()
                    .status(HttpStatus.CREATED.value())
                    .message("Login Successful")
                    .build();
        } catch (Exception e) {
            log.error("Email: {} -> Error while logging in the user: {}", userReq.getEmailId(), e.getMessage());
            throw new UserException("Email: " + userReq.getEmailId() + " -> Error while logging in the user: " + e.getMessage());
        }

    }

    /**
     * Get user info by email id
     *
     * @param emailId email id
     * @return user info
     */
    @Override
    public UserInfo getUserInfoByEmail(String emailId) {
        return userRepository.findByEmailIdAndStatus(emailId, (byte) 1)
                .orElseThrow(() -> new UserException("User not found with the email id: " + emailId));
    }

    /**
     * Get user info by user id
     *
     * @param userId user id
     * @return user info
     */
    @Override
    public ApiResp<?> getUserInfo(Integer userId, String emailId) {

        try {
            Map<String, Object> userInfo = userRepository.getUserInfoById(userId);
            log.info("Email: {} -> User Info: {}", emailId, userInfo);

            if (userInfo.isEmpty()) {
                return ApiResp.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message("User not found")
                        .build();
            }
            return ApiResp.builder()
                    .status(HttpStatus.OK.value())
                    .message("User Info fetched successfully")
                    .data(userInfo)
                    .build();
        } catch (Exception e) {
            log.error("Email: {} -> Error while fetching the user info: {}", emailId, e.getMessage());
            throw new UserException("Email: " + emailId + " -> Error while fetching the user info: " + e.getMessage());
        }

    }

    /**
     * Get all user info
     *
     * @return user info
     */
    @Override
    public ApiResp<?> getAllUserInfo() {
        try {
            List<Map<String, Object>> users = userRepository.getAllUsers();
            log.info("All User Info: {}", users);

            if (users.isEmpty()) {
                return ApiResp.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message("No User found")
                        .build();
            }
            return ApiResp.builder()
                    .status(HttpStatus.OK.value())
                    .message("All User Info fetched successfully")
                    .data(users)
                    .build();
        } catch (Exception e) {
            log.error("Error while fetching all user info: {}", e.getMessage());
            throw new UserException("Error while fetching all user info: " + e.getMessage());
        }
    }

    /**
     * Delete user by user id
     *
     * @param userId user id
     * @return ApiResp
     */
    @Override
    public ApiResp<?> deleteUser(Integer userId, String emailId) {
        try {
            userRepository.deleteByUserId(userId);
            log.info("Email: {} -> User deleted successfully", emailId);
            return ApiResp.builder()
                    .status(HttpStatus.NO_CONTENT.value())
                    .message("User deleted successfully")
                    .build();
        } catch (Exception e) {
            log.error("Email: {} -> Error while deleting the user: {}", emailId, e.getMessage());
            throw new UserException("Email: " + emailId + " -> Error while deleting the user: " + e.getMessage());
        }
    }

}
