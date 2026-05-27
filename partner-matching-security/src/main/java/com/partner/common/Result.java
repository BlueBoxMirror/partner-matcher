package com.partner.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private int code;
    private String message;
    private T data;

    // 成功响应 code: 0
    public static <T> Result<T> success(T data) {
        return new Result<>(0, "success", data);
    }

    // 成功响应 code: 200（如果需要兼容）
    public static <T> Result<T> ok(T data) {
        return new Result<>(200, "success", data);
    }

    // 错误响应
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }
}