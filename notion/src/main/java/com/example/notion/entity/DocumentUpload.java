package com.example.notion.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "DocumentUpload")
@Table(name = "document_upload")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "documentuploadid")
    private Integer documentUploadId;

    @Column(name = "documentname")
    private String documentName;

    @Column(name = "documenttype")
    private String documentType;

    @Column(name = "documentpath")
    private String documentPath;

    @Column(name = "documentsize")
    private BigDecimal documentSize;

    @Column(name = "xstatus")
    private Byte status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documentid")
    private Document document;

    @Column(name = "uploadedat")
    private LocalDateTime uploadedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploadedby")
    private UserInfo uploadedBy;
}
