package com.ruoyi.order.rocketmq;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@ConditionalOnProperty(prefix = "rocket-mq", name = "nameSrvAddr")
public class ProducerClientConfig {

    @Autowired
    private RocketMqConfig rocketMqConfig;

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public ProducerBean buildProducer() {
        ProducerBean producer = new ProducerBean();
        Properties properties = rocketMqConfig.getMqProperties();
        producer.setProperties(properties);
        return producer;
    }

}