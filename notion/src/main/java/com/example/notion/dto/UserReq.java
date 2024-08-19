package com.example.notion.dto;

import com.example.notion.constants.ApplicationConstants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = ApplicationConstants.PASSWORD_REGEX, message = "Password should contain atleast 1 uppercase, 1 lowercase, 1 number, 1 special character and should be of minimum 8 characters")
    private String password;

    @NotNull(message = "Please enter email Id")
    @Pattern(regexp = ApplicationConstants.EMAIL_REGEX, message = "Please enter valid email Id")
    private String emailId;
}
