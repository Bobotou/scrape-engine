package com.fin.service;

import com.fin.common.dto.req.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class HttpClientService {
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();


    public ResponseDTO<String> postJson(String url, String jsonBody, Headers headers) {
        try {
            RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));
            log.info("发送请求: {}", url);
            log.info("请求体: {}", jsonBody);
            log.info("请求头: {}", body);
            Request request = new Request.Builder()
                    .url(url)
                    .headers(headers)
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    String errorBody = response.body() != null ? response.body().string() : "未知错误！";
                    return ResponseDTO.error(errorBody, response.code());
                }
                return ResponseDTO.success(response.body().string());
            }
        } catch (IOException e) {
            log.error("HTTP请求失败: {}", e.getMessage());
            return ResponseDTO.error("IO异常: " + e.getMessage());
        }
    }
}