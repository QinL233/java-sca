package com.baidu.sz.api.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 封装结果返回实体类
 *
 * @author hwx
 * @date 2018/3/12
 */
@Data
public class ResultInfo<T> implements Serializable {

    /**
     * 返回码 默认为200(成功) -1：失败
     */
    private int code = 200;

    /**
     * 字符串消息返回
     */
    private String msg;

    /**
     * 返回封装数据
     */
    private T data;


    private ResultInfo() {
    }

    public ResultInfo(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
