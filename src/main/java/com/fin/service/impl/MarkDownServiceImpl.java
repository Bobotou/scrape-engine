package com.fin.service.impl;

import com.fin.common.ScrapeReq;
import com.fin.common.dto.req.ResponseDTO;
import com.fin.common.dto.req.ScrapeRequestDTO;
import com.fin.service.FileService;
import com.fin.service.HttpClientService;
import com.fin.service.MarkdownService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Fin
 * @version 1.0
 * @date 2025/2/28 13:47
 */

@Service
@Slf4j
public class MarkDownServiceImpl implements MarkdownService {

    @Autowired
    private HttpClientService httpClient;

    @Autowired
    private FileService fileService;

    @Value("${scrape-engine.cookie}")
    private String cookie;

    @Value("${scrape-engine.output.path}")
    private static String OUTPUT_DIR;

    @Value("${fireCrawl.host}")
    private String SCRAPE_URL;

    public ResponseDTO<String> fetchAndSaveMarkdown(ScrapeReq scrapeReq) {
        // 构建请求对象
        ScrapeRequestDTO requestDTO = buildScrapeRequest(scrapeReq);

        // 执行HTTP请求
        ResponseDTO<String> response = httpClient.postJson(
                SCRAPE_URL + "/v1/scrape",
                new JSONObject(requestDTO).toString(),
                buildHeaders()
        );

        if (response.isSuccess()) {
            try {
                String responseBody = response.getData();
                JSONObject jsonObject = new JSONObject(responseBody);
                String markdownContent = jsonObject.getJSONObject("data").getString("markdown");
                String fileName = jsonObject.getJSONObject("data").getJSONObject("metadata").getString("ajs-latest-published-page-title") + ".md";
                fileService.saveMarkdown(markdownContent, fileName.replaceAll("/", ""));
            } catch (IOException e) {
                log.error("Markdown文件保存失败：{}", e.getMessage(), e);
                return ResponseDTO.error("Markdown文件保存失败");
            }
        } else {
            return ResponseDTO.error("Markdown文件保存失败：" + response.getMessage());
        }
        return ResponseDTO.success("Markdown文件保存成功");
    }

    private Headers buildHeaders() {
        return new Headers.Builder()
                .add("Cookie", cookie)
                .add("Content-Type", "application/json")
                .add("Accept", "application/json")
                .add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")
                .build();
    }

    private ScrapeRequestDTO buildScrapeRequest(ScrapeReq scrapeReq) {

        ScrapeRequestDTO scrapeRequestDTO = new ScrapeRequestDTO();
        scrapeRequestDTO.setFormats(List.of("markdown"));
        scrapeRequestDTO.setUrl(scrapeReq.getUrl().get(0));
        scrapeRequestDTO.setHeaders(Map.of("Cookie", cookie));
//        BeanUtils.copyProperties(scrapeReq, scrapeRequestDTO);
        return scrapeRequestDTO;
    }

}
