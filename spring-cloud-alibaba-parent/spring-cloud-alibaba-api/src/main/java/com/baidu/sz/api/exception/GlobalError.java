package com.baidu.sz.api.exception;

import lombok.Data;

/**
 * @author liaoqinzhou_sz
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年07月12日 17:49:00
 */
public class GlobalError {

    /**
     * 所有自定义异常父类
     */
    @Data
    public static class CustomException extends RuntimeException {

        private Integer code;

        private String msg;

        public CustomException() {
            code = 500;
            msg = "error";
        }

        public CustomException(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }

    public static class NotLogin extends CustomException {
        public NotLogin() {
            super(401, "未登录");
        }
    }

    public static class NotPermission extends CustomException {
        public NotPermission() {
            super(403, "无权限");
        }
    }


}
