package com.baidu.sz.cache.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import java.time.Duration;

/**
 * @author liaoqinzhou_sz
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年07月25日 16:33:00
 */
@Configuration
@EnableCaching
public class RedisManageConfig {

    /**
     * 设置 redisTemplate 序列化方式
     *
     * @param factory
     * @param objectMapper jackson配置
     * @return
     */
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory factory, @Qualifier("redisObjectMapper") ObjectMapper objectMapper) {
        RedisTemplate redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        //使用jackson对value进行序列化
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);

        // 设置键（key）的序列化采用StringRedisSerializer。
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        //最终序列化接口
        redisTemplate.setDefaultSerializer(serializer);

        //事务支持
        //redisTemplate.setEnableTransactionSupport(true);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


    /**
     * 自定义RedisCacheManager，用于在使用@Cacheable时设置ttl
     */
    @Bean
    public RedisCacheManager cacheManager(RedisTemplate<String, Object> redisTemplate) {
//        return RedisCacheManager
//                .builder(Objects.requireNonNull(redisTemplate.getConnectionFactory()))
//                .cacheDefaults(
//                        RedisCacheConfiguration
//                                .defaultCacheConfig()
//                                .entryTtl(Duration.ofSeconds(3600))
//                                .computePrefixWith(cacheName -> cacheName)
//                                .disableCachingNullValues()
//                                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer((StringRedisSerializer) redisTemplate.getKeySerializer()))
//                                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer()))
//                )
//                .transactionAware()//事务
//                .build();
        return new TTLRedisCacheManager(RedisCacheWriter.nonLockingRedisCacheWriter(redisTemplate.getConnectionFactory()), RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(3600))
                .computePrefixWith(cacheName -> cacheName)
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer((StringRedisSerializer) redisTemplate.getKeySerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer())));
    }


    /**
     * 实现在key上配置ttl
     */
    public class TTLRedisCacheManager extends RedisCacheManager {
        public TTLRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
            super(cacheWriter, defaultCacheConfiguration);
        }

        @Override
        protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
            String[] cells = StringUtils.delimitedListToStringArray(name, "=");
            name = cells[0];
            if (cells.length > 1) {
                long ttl = Long.parseLong(cells[1]);
                // 根据传参设置缓存失效时间，默认单位是秒
                cacheConfig = cacheConfig.entryTtl(Duration.ofSeconds(ttl));
            }
            return super.createRedisCache(name, cacheConfig);
        }
    }
}
