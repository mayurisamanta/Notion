package com.example.notion.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserReq for handling user request
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReq {

    @NotNull(message = "Please enter password")
    private String password;

    @NotNull(message = "Please enter email Id")
    private String emailId;
}
