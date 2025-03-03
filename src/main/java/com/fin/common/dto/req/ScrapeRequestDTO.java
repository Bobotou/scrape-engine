package com.fin.common.dto.req;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ScrapeRequestDTO {

    private String url;

    private List<String> formats;

    private Map<String, String> headers;
}