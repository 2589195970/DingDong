package com.ruoyi.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * 启动程序
 * 
 * @author ruoyi
 */
@EnableAsync
@EnableWebMvc
@EnableCaching
@EnableConfigurationProperties
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@MapperScan("com.ruoyi.order.mapper")
public class OrderApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(OrderApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ 订单服务启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
