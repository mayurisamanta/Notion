package com.example.notion.controller;

import com.example.notion.dto.ApiResp;
import com.example.notion.dto.UserReq;
import com.example.notion.dto.UserSessionBean;
import com.example.notion.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * UserController
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * register
     * @param userReq
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResp<?>> register(@Valid @RequestBody UserReq userReq) {
        log.info("Email: {} -> Registering the user", userReq.getEmailId());
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(userReq));
    }

    /**
     * getUserInfo
     * @param authentication
     */
    @GetMapping("/user")
    public ResponseEntity<ApiResp<?>> getUserInfo(Authentication authentication) {
        UserSessionBean userSessionBean = (UserSessionBean) authentication.getDetails();
        log.info("Email: {} -> Getting the user info", userSessionBean.getEmailId());
        return ResponseEntity.ok(userService.getUserInfo(userSessionBean.getUserId(), userSessionBean.getEmailId()));
    }

    /**
     * getAllUserInfo
     */
    @GetMapping("/users")
    public ResponseEntity<ApiResp<?>> getAllUserInfo() {
        return ResponseEntity.ok(userService.getAllUserInfo());
    }

    /**
     * updateUser
     * @param authentication
     */
    @DeleteMapping("/user")
    public ResponseEntity<ApiResp<?>> deleteUser(Authentication authentication) {
        UserSessionBean userSessionBean = (UserSessionBean) authentication.getDetails();
        log.info("Email: {} -> Deleting the user", userSessionBean.getEmailId());
        return ResponseEntity.ok(userService.deleteUser(userSessionBean.getUserId(), userSessionBean.getEmailId()));
    }

}
