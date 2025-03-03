package com.fin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;

@Service
@Slf4j
public class FileService {

    @Value("${scrape-engine.output.path}")
    private String outputDir;

    public void saveMarkdown(String content, String fileName) throws IOException {
        String path = outputDir + fileName ;

        try (FileWriter writer = new FileWriter(path)) {
            writer.write(content);
            log.info("文件保存成功: {}", path);
        }
    }
}