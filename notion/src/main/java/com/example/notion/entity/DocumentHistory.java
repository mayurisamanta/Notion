package com.example.notion.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "DocumentHistory")
@Table(name = "document_history")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "documenthistoryid")
    private Integer documentHistoryId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documentid")
    private Document document;

    @Column(name = "createdat")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdby")
    private UserInfo createdBy;

    @Column(name = "version")
    private Integer version;
}
