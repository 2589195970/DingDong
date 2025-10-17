package com.ruoyi.web.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 照片上传异步配置
 *
 * @author Claude
 * @date 2025/01/17
 */
@Slf4j
@Configuration
public class PhotoUploadConfig {

    /**
     * 照片上传专用线程池
     * 核心线程数5，最大线程数10，队列容量50
     */
    @Bean(name = "photoUploadExecutor")
    public Executor photoUploadExecutor() {
        log.info("初始化照片上传异步执行器");

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("photo-upload-");
        executor.setKeepAliveSeconds(60);

        // 设置拒绝策略：由调用线程执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 等待任务完成后关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);

        executor.initialize();
        return executor;
    }
}