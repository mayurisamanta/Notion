package com.example.notion.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDocumentReq {

    @NotNull(message = "Please enter title")
    @Size(min = 1, max = 255, message = "Title should be between 1 to 255 characters")
    private String title;

    @NotNull(message = "Please enter content")
    private String content;

    private Integer templateId;

    private Integer parentId;

    private List<CreateDocumentReq> childDocuments;
}
