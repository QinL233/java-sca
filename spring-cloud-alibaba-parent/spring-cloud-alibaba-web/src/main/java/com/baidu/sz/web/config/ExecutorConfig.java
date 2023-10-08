package com.baidu.sz.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author liaoqinzhou_sz
 * @version 1.0.0
 * @Description 线程池配置
 * @createTime 2021年06月03日 11:57:00
 */
@Configuration
@EnableAsync
public class ExecutorConfig {

    @Value("${thread.core_pool_size}")
    private int corePoolSize;
    @Value("${thread.max_pool_size}")
    private int maxPoolSize;
    @Value("${thread.queue_capacity}")
    private int queueCapacity;
    @Value("${thread.prefix}")
    private String prefix;

    @Bean(name = "asyncServiceExecutor")
    public Executor asyncServiceExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(corePoolSize);
        //配置最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        //配置队列大小
        executor.setQueueCapacity(queueCapacity);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix(prefix);
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }

}