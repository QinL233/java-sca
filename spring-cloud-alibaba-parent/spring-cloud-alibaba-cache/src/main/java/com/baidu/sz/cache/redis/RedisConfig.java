package com.baidu.sz.cache.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

/**
 * 初始化redis各个数据类型
 */
@Configuration
public class RedisConfig {

    @Autowired
    RedisTemplate redisTemplate;

    @Bean(name = "valueOperations")
    public ValueOperations getValueOperations() {
        return redisTemplate.opsForValue();
    }

    @Bean("setOperations")
    public SetOperations getSetOperations() {
        return redisTemplate.opsForSet();
    }

    @Bean("zSetOperations")
    public ZSetOperations getZSetOperations() {
        return redisTemplate.opsForZSet();
    }


}
