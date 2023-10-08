package com.baidu.sz.web.filter;

import com.baidu.sz.rpc.entity.CurrentContext;

import javax.servlet.http.HttpServletRequest;

/**
 * 构建Context
 * @author liaoqinzhou_sz
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年07月12日 11:22:00
 */
public interface InitContext {

    CurrentContext create(HttpServletRequest request );
}
