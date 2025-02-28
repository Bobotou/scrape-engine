package com.fin.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Fin
 * @version 1.0
 * @date 2025/2/27 17:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScrapeReq {

    String url;

    String token;

}
