package com.example.notion.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity(name = "user")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    private Integer userId;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "emailid")
    private String emailId;

    @Column(name = "xstatus")
    private Byte xStatus;

    @Column(name = "lastloginat")
    private LocalDateTime lastLoginAt;

    @Column(name = "createdat")
    private LocalDateTime createdAt;

    @Column(name = "updatedat")
    private LocalDateTime updatedAt;
}
