package com.example.notion.master.service;

import com.example.notion.master.dto.TemplateResp;
import com.example.notion.master.repository.MasterTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MasterService {

    private final MasterTemplateRepository masterTemplateRepository;

    public List<TemplateResp> getAllTemplates() {
        return masterTemplateRepository.getAllTemplates();
    }
}
