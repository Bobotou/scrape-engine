package com.fin.service;

import com.fin.common.ScrapeReq;
import com.fin.common.dto.req.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MarkdownService {

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // 连接超时设为 30 秒
            .readTimeout(60, TimeUnit.SECONDS)   // 读取超时设为 60 秒
            .writeTimeout(60, TimeUnit.SECONDS)  // 写入超时设为 60 秒
            .build();
    private static final String SCRAPE_URL = "http://127.0.0.1:3002/v1/scrape";
    private static final String OUTPUT_PATH = "/Users/fin/Documents/markdown-output/markdown_output.md";

    private static final String OUTPUT_DIR = "/Users/fin/Documents/markdown-output/"; // 输出目录


    public ResponseDTO<String> fetchAndSaveMarkdown(ScrapeReq scrapeReq) {
        try {
            // 构造请求体
            JSONObject requestBody = new JSONObject();
            requestBody.put("url", scrapeReq.getUrl());
            JSONArray formats = new JSONArray();
            formats.put("markdown");
            requestBody.put("formats", formats);
            JSONObject headers = new JSONObject();
            headers.put("Cookie", scrapeReq.getToken());
            requestBody.put("headers", headers);

            String requestBodyString = requestBody.toString();

            // 创建请求
            RequestBody body = RequestBody.create(
                    requestBodyString,
                    MediaType.get("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(SCRAPE_URL)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .header("Content-Length", String.valueOf(requestBodyString.getBytes().length))
                    .post(body)
                    .build();

            // 打印请求详情
            log.info("Sending request to: {}", SCRAPE_URL);
            log.info("Request headers: {}", request.headers());
            log.info("Request body: {}", requestBody.toString(2));

            // 发送请求
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseBody);
                    String markdownContent = jsonObject.getJSONObject("data").getString("markdown");
                    String fileName = jsonObject.getJSONObject("data").getJSONObject("metadata").getString("ajs-latest-published-page-title") + ".md";
                    String outputPath = OUTPUT_DIR + fileName;
                    saveToFile(markdownContent, outputPath);
                    return ResponseDTO.success("Markdown successfully saved", outputPath);
                } else {
                    String responseBody = response.body() != null ? response.body().string() : "No response body";
                    log.error("Request failed with status: {}, response body: {}", response.code(), responseBody);
                    return ResponseDTO.error("Failed to fetch markdown: " + responseBody, response.code());
                }
            }

        } catch (IOException e) {
            log.error("Error occurred while fetching markdown: {}", e.getMessage());
            return ResponseDTO.error("IO error: " + e.getMessage());
        } catch (Exception e) {
            log.error("JSON parsing error: {}", e.getMessage());
            return ResponseDTO.error("JSON parsing error: " + e.getMessage());
        }
    }

    private void saveToFile(String content, String outputPath) throws IOException {
        try (FileWriter writer = new FileWriter(outputPath)) {
            writer.write(content);
            log.info("Markdown content saved to: {}", outputPath);
        }
    }

    private String generateFileNameFromUrl(String url) {
        // 从 URL 中提取文件名，假设 URL 以 pageId=xxx 结尾
        String fileName = url.substring(url.lastIndexOf("pageId=") + 7); // 提取 pageId 后的部分
        if (fileName.isEmpty()) {
            fileName = "default"; // 防止空文件名
        }
        // 替换非法字符，确保文件名合法
        fileName = fileName.replaceAll("[^a-zA-Z0-9-]", "_");
        return fileName + ".md"; // 添加 .md 后缀
    }
}