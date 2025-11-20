package com.shenzhewei.quiz.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一响应结果类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code; // 响应码：1成功，0失败
    private String message; // 响应消息
    private T data; // 响应数据

    // 成功响应
    public static <T> Result<T> success() {
        return new Result<>(1, "success", null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(1, "success", data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(1, message, data);
    }

    // 失败响应
    public static <T> Result<T> error(String message) {
        return new Result<>(0, message, null);
    }

    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }
}
