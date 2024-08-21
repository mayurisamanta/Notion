package com.example.notion.dto;

import com.example.notion.master.dto.TemplateResp;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResp {

    private Integer documentId;

    private String title;

    private String content;

    private TemplateResp template;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<DocumentResp> childDocument;
}
