package com.ruoyi.order.rocketmq;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Data
@Configuration
@ConfigurationProperties(prefix = RocketMqConfig.PREFIX)
@Slf4j
public class RocketMqConfig {

    public static final String PREFIX = "rocket-mq";

    String accessKey;
    String secretKey;
    String nameSrvAddr;
    String groupId;
    String consumeThreadNums;
    String reconsumeTimes;
    String consumeTimeout;
    String suspendTimeMillis;

    public Properties getMqProperties() {
        log.info("{}", this);
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.AccessKey, this.accessKey);
        properties.setProperty(PropertyKeyConst.SecretKey, this.secretKey);
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, this.nameSrvAddr);
        properties.setProperty(PropertyKeyConst.GROUP_ID, this.groupId);
        properties.setProperty(PropertyKeyConst.ConsumeThreadNums, this.consumeThreadNums);
        properties.put(PropertyKeyConst.ConsumeTimeout, this.consumeTimeout);
        properties.put(PropertyKeyConst.MaxReconsumeTimes, this.reconsumeTimes);
        properties.put(PropertyKeyConst.SuspendTimeMillis, this.suspendTimeMillis);
        return properties;
    }
}