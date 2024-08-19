package com.example.notion.master.repository;

import com.example.notion.master.dto.TemplateResp;
import com.example.notion.master.entity.MasterTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MasterTemplateRepository extends JpaRepository<MasterTemplate, Integer> {

    @Query("SELECT new com.example.notion.master.dto.TemplateResp(mt.templateId, mt.title, mt.content) FROM MasterTemplate mt " +
            "WHERE mt.status = 1")
    List<TemplateResp> getAllTemplates();
}
