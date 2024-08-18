package com.example.notion.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResp<T> {

    private Integer status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
}
