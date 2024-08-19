package com.example.notion.master.entity;

import com.example.notion.entity.UserInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "MasterTemplate")
@Table(name = "master_template")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MasterTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "templateid")
    private Integer templateId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "xstatus")
    private Byte status;

    @Column(name = "createdat")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdby")
    private UserInfo createdBy;

    @Column(name = "updatedat")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updatedby")
    private UserInfo updatedBy;
}
