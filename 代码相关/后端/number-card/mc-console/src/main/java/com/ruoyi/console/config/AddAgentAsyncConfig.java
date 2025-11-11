package com.ruoyi.console.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 统一的代理产品异步线程池配置，确保任何引用 console 模块的应用都能使用
 * addAgentExecutor 执行器。
 */
@Slf4j
@Configuration
@EnableAsync
public class AddAgentAsyncConfig {

    private int corePoolSize = 3;
    private int maxPoolSize = 5;
    private int queueCapacity = 20000;
    private String namePrefix = "add-agent-";

    @Bean(name = "addAgentExecutor")
    public Executor addAgentExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(namePrefix);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        executor.initialize();
        log.info("Initialized addAgentExecutor with corePoolSize={} maxPoolSize={} queueCapacity={}",
                corePoolSize, maxPoolSize, queueCapacity);
        return executor;
    }
}
