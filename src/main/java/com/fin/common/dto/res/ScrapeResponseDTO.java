package com.fin.common.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ScrapeResponseDTO {

    private DataDTO data;
    
    @Data
    public static class DataDTO {
        private String markdown;
        private MetadataDTO metadata;
    }

    @Data
    public static class MetadataDTO {
        @JsonProperty("ajs-latest-published-page-title")
        private String title;
    }
}