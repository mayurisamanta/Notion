package com.example.notion.master.controller;

import com.example.notion.master.dto.TemplateResp;
import com.example.notion.master.service.MasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/master")
public class MasterController {

    private final MasterService masterService;

    @GetMapping("/templates")
    public List<TemplateResp> getAllTemplates() {
        return masterService.getAllTemplates();
    }
}
