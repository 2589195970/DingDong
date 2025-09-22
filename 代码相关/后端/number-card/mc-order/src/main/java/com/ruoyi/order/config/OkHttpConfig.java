package com.ruoyi.order.config;

import com.ruoyi.common.http.HttpClient;
import com.ruoyi.common.http.HttpProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author 陈思伟
 */
@Slf4j
@Configuration
public class OkHttpConfig {
    private static final String TAG = "OkHttpConfig";


    @Bean(name = "httpProperties")
    @ConfigurationProperties(prefix = "okhttp")
    public HttpProperties commonHttp() {
        return new HttpProperties();
    }

    @Bean
    @Primary
    public HttpClient httpClient(@Qualifier("httpProperties") HttpProperties properties) {
        return new HttpClient(properties);
    }
}
