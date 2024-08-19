package com.example.notion.service;

import com.example.notion.constants.ApplicationConstants;
import com.example.notion.dto.UserReq;
import com.example.notion.dto.UserSessionBean;
import com.example.notion.entity.UserInfo;
import com.example.notion.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class to generate JWT token
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final AuthenticationManager authenticationManager;

    private final Environment env;

    private final UserRepository userRepository;

    /**
     * Method to generate JWT token
     *
     * @param userReq UserReq
     * @return String
     */
    public String generateToken(UserReq userReq) {
        String emailId = userReq.getEmailId();

        try {
            String jwt = "";
            Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(userReq.getEmailId(),
                    userReq.getPassword());
            Authentication authenticationResponse = authenticationManager.authenticate(authentication);
            log.info("Email: {} -> authenticationResponse: {}", emailId, authenticationResponse);

            Optional<UserInfo> userInfoOpt = userRepository.findByEmailIdAndStatus(userReq.getEmailId(), (byte) 1);

            if (userInfoOpt.isPresent() && Objects.nonNull(authenticationResponse) && authenticationResponse.isAuthenticated()) {
                if (Objects.nonNull(env)) {
                    String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY,
                            ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
                    SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                    UserInfo userInfo = userInfoOpt.get();
                    log.info("Email: {} -> User Info: {}", emailId, userInfo);

                    UserSessionBean userSessionBean = UserSessionBean
                            .builder()
                            .userId(userInfo.getUserId())
                            .emailId(userInfo.getEmailId())
                            .build();

                    log.info("Email: {} -> UserSessionBean: {}", emailId, userSessionBean);
                    jwt = Jwts.builder().issuer("Notion").subject("JWT Token")
                            .claim("username", authenticationResponse.getName())
                            .claim("authorities", authenticationResponse.getAuthorities().stream().map(
                                    GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                            .claim("userSessionBean", userSessionBean)
                            .issuedAt(new java.util.Date())
                            .expiration(new java.util.Date((new java.util.Date()).getTime() + 86400000)) // 24 hours
                            .signWith(secretKey).compact();

                    log.info("Email: {} -> JWT Token: {}", emailId, jwt);
                }
            }

            return jwt;
        } catch (Exception e) {
            log.error("Email: {} -> Error while generating the token: {}", emailId, e.getMessage());
            throw new RuntimeException("Email: " + emailId + " -> Error while generating the token: " + e.getMessage());
        }

    }
}
