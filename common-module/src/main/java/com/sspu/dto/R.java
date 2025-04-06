package com.sspu.dto;

import lombok.Data;

@Data
public class R<T> {
    /**
     * 相应编码 0为正常 -1为错误
     */
    private int code;
    /**
     * 响应提示信息
     */
    private String msg;
    /**
     * 响应内容
     */
    private T result;

    public R(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public R() {
        this(0, "success");
    }

    /**
     * 错误信息的封装
     */
    public static <T> R<T> fail() {
        R<T> response = new R<>();
        response.setCode(-1);
        return response;
    }

    public static <T> R<T> fail(String msg) {
        R<T> response = new R<>();
        response.setCode(-1);
        response.setMsg(msg);
        return response;
    }

    public static <T> R<T> fail(String msg, T result) {
        R<T> response = new R<>();
        response.setCode(-1);
        response.setMsg(msg);
        response.setResult(result);
        return response;
    }

    /**
     * 正常信息的封装
     */
    public static <T> R<T> success() {
        return new R<>();
    }

    public static <T> R<T> success(T result) {
        R<T> response = new R<>();
        response.setResult(result);
        return response;
    }

    public static <T> R<T> success(String msg, T result) {
        R<T> response = new R<>();
        response.setMsg(msg);
        response.setResult(result);
        return response;
    }
}
