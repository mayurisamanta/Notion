package com.example.notion.exception;

import com.example.notion.dto.ApiResp;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * CustomBasicAuthenticationEntryPoint for handling authentication failure
 */
public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Commence
     *
     * @param request       HttpServletRequest
     * @param response      HttpServletResponse
     * @param authException AuthenticationException
     * @throws IOException IOException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {

        String message = (authException != null && authException.getMessage() != null) ? authException.getMessage()
                : "Unauthorized";
        response.setHeader("eazybank-error-reason", "Authentication failed");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");

        ApiResp<?> apiResp = ApiResp.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .message(message)
                .build();

        String jsonResponse = new ObjectMapper().writeValueAsString(apiResp);

        response.getWriter().write(jsonResponse);
    }
}
