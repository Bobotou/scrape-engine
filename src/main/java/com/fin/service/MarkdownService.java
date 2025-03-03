package com.fin.service;

import com.fin.common.ScrapeReq;
import com.fin.common.dto.req.ResponseDTO;

import java.io.IOException;

/**
 * @author Fin
 * @version 1.0
 * @date 2025/2/28 14:24
 */
public interface MarkdownService {

    public ResponseDTO<String> fetchAndSaveMarkdown(ScrapeReq scrapeReq) ;


}
