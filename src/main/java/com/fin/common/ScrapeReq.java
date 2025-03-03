package com.fin.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Fin
 * @version 1.0
 * @date 2025/2/27 17:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScrapeReq {

    List<String> url;

}
