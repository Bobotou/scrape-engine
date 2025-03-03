package com.fin.common.dto.req;

import lombok.Data;

@Data
public class ResponseDTO<T> {
    private boolean success;     // 是否成功
    private String message;      // 消息描述
    private T data;              // 数据内容（泛型，支持任意类型）
    private int code;            // 状态码（可选，用于更细粒度的错误区分）

    // 私有构造方法，强制使用静态方法构建
    private ResponseDTO(boolean success, String message, T data, int code) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.code = code;
    }

    // 成功的静态方法
    public static <T> ResponseDTO<T> success(String message, T data) {
        return new ResponseDTO<>(true, message, data, 200);
    }

    public static <T> ResponseDTO<T> success(T data) {
        return new ResponseDTO<>(true, "success!", data, 200);
    }

    // 错误的静态方法
    public static <T> ResponseDTO<T> error(String message, int code) {
        return new ResponseDTO<>(false, message, null, code);
    }

    public static <T> ResponseDTO<T> error(String message) {
        return new ResponseDTO<>(false, message, null, 500); // 默认错误码 500
    }
}