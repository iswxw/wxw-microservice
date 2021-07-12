package com.wxw.manage.config;

import io.netty.util.concurrent.RejectedExecutionHandlers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author weixiaowei
 * @desc: 自定义线程池
 * @date: 2021/7/12
 */
@Configuration
public class ThreadPoolTaskConfig {

    private static int corePoolSize = Runtime.getRuntime().availableProcessors();

    @Bean
    public ThreadPoolTaskExecutor  threadPoolExecutor(){
        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        poolTaskExecutor.setThreadNamePrefix("sec-kill-");
        poolTaskExecutor.setCorePoolSize(corePoolSize);
        poolTaskExecutor.setMaxPoolSize(corePoolSize +1);
        poolTaskExecutor.setKeepAliveSeconds(10);
        poolTaskExecutor.setQueueCapacity(1000);
        poolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return poolTaskExecutor;
    }
}
