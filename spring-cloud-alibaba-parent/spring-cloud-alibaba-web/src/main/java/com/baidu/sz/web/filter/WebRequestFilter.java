package com.baidu.sz.web.filter;

import com.baidu.sz.rpc.utils.RpcContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author liaoqinzhou_sz
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021年07月01日 17:48:00
 */
@Slf4j
@Configuration
public class WebRequestFilter implements Filter {

    @Autowired
    @Lazy
    private InitContext initContext;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //从header中获取token并放到localThread（MDC）中，rpc通信时在filter从（MDC）
        try {
            RpcContextUtil.init(initContext.create(request));
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        filterChain.doFilter(request, response);
        RpcContextUtil.clear();
    }


}