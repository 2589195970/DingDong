package com.ruoyi.web.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author 陈思伟
 * @version 1.0
 * @date 2023/1/13 10:39
 */
@Slf4j
@Configuration
@EnableAsync
public class OrderSaveConfig {

    private int corePoolSize = 3;
    private int maxPoolSize = 5 ;
    private int queueCapacity = 20;
    private String namePrefix = "order-save-";

    @Bean(name = "orderSaveExecutor")
    public Executor orderSaveExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(namePrefix);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        executor.initialize();
        return executor;
    }
}

