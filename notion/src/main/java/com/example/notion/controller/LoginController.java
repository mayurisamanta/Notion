package com.example.notion.controller;

import com.example.notion.constants.ApplicationConstants;
import com.example.notion.dto.ApiResp;
import com.example.notion.dto.UserReq;
import com.example.notion.service.JwtService;
import com.example.notion.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * LoginController
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final JwtService jwtService;

    private final UserService userService;

    /**
     * login
     *
     * @param userReq
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResp<?>> login(@Valid @RequestBody UserReq userReq) {
        log.info("Email: {} -> Got request to login: {}", userReq.getEmailId(), userReq);

        return ResponseEntity.status(HttpStatus.OK).header(ApplicationConstants.JWT_HEADER, jwtService.generateToken(userReq))
                .body(userService.login(userReq));
    }

}
