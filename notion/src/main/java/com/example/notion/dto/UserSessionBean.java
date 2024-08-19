package com.example.notion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserSessionBean for handling user session
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSessionBean {

    private Integer userId;

    private String emailId;

}
