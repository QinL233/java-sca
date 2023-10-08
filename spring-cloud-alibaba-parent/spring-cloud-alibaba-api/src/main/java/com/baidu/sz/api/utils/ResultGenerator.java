package com.baidu.sz.api.utils;


import com.baidu.sz.api.entity.ResultInfo;

/**
 * 响应结果生成工具
 */
public class ResultGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "success";
    private static final String DEFAULT_FAIL_MESSAGE = "failure";

    private ResultGenerator() {
        throw new IllegalStateException("Utility class");
    }

    public static ResultInfo success() {
        return new ResultInfo(200, DEFAULT_SUCCESS_MESSAGE, null);
    }

    public static ResultInfo success(Object data) {
        return new ResultInfo(200, DEFAULT_SUCCESS_MESSAGE, data);
    }

    public static ResultInfo fail() {
        return new ResultInfo(500, DEFAULT_FAIL_MESSAGE, null);
    }

    public static ResultInfo fail(String message) {
        return new ResultInfo(500, message, null);
    }

    public static ResultInfo fail(Integer code, String message) {
        return new ResultInfo(code, message, null);
    }

    public static ResultInfo fail(Integer code, String message, Object data) {
        return new ResultInfo(code, message, data);
    }

}
