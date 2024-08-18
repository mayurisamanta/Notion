package com.example.notion.controller;

import com.example.notion.constants.ApplicationConstants;
import com.example.notion.dto.ApiResp;
import com.example.notion.dto.UserReq;
import com.example.notion.dto.UserSessionBean;
import com.example.notion.service.JwtService;
import com.example.notion.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserReq userReq) {
        return ResponseEntity.status(200).header(ApplicationConstants.JWT_HEADER, jwtService.generateToken(userReq))
                .body(
                        ApiResp
                                .builder()
                                .status(1)
                                .message("Login Successful")
                                .build());
    }


    @PostMapping("/register")
    public ResponseEntity<ApiResp<?>> register(@RequestBody UserReq userReq) {
        return ResponseEntity.ok(userService.register(userReq));
    }

    @PostMapping("/test")
    public ResponseEntity<?> test(Authentication authentication) {
        UserSessionBean userSessionBean = (UserSessionBean) authentication.getDetails();
        return ResponseEntity.ok(userSessionBean);
    }
}
