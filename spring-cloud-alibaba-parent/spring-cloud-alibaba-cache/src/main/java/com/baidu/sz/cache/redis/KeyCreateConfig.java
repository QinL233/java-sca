package com.baidu.sz.cache.redis;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author liaoqinzhou_sz
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年07月25日 15:41:00
 */
@Configuration
public class KeyCreateConfig {

    @Bean("baseKeyGenerator")
    public KeyGenerator keyGenerator() {
        return (o, method, params) -> {
            StringBuilder r = new StringBuilder(o.getClass().getName());
            r.append(":").append(method.getName());
            int i = 256;
            for (Object param : params) {
                i += param.hashCode();
            }
            r.append(":").append(i);
            return r;
        };
    }
}
