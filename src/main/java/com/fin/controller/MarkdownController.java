package com.fin.controller;

import com.fin.common.ScrapeReq;
import com.fin.common.dto.req.ResponseDTO;
import com.fin.service.MarkdownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MarkdownController {

    @Autowired
    private MarkdownService markdownService;

    @PostMapping("/fetch-markdown")
    public ResponseDTO<String> fetchMarkdown(@RequestBody ScrapeReq scrapeReq) {
        return markdownService.fetchAndSaveMarkdown(scrapeReq);
    }


}