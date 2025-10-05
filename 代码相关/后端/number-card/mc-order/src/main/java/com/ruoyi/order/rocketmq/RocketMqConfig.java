package com.ruoyi.order.rocketmq;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Data
@Configuration
@ConfigurationProperties(prefix = RocketMqConfig.PREFIX)
public class RocketMqConfig {

    private static final Logger log = LoggerFactory.getLogger(RocketMqConfig.class);

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

        // 添加空值保护，避免设置null值导致NPE
        if (this.accessKey != null) {
            properties.setProperty(PropertyKeyConst.AccessKey, this.accessKey);
        }
        if (this.secretKey != null) {
            properties.setProperty(PropertyKeyConst.SecretKey, this.secretKey);
        }
        if (this.nameSrvAddr != null) {
            properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, this.nameSrvAddr);
        }
        if (this.groupId != null) {
            properties.setProperty(PropertyKeyConst.GROUP_ID, this.groupId);
        }
        if (this.consumeThreadNums != null) {
            properties.setProperty(PropertyKeyConst.ConsumeThreadNums, this.consumeThreadNums);
        }
        if (this.consumeTimeout != null) {
            properties.put(PropertyKeyConst.ConsumeTimeout, this.consumeTimeout);
        }
        if (this.reconsumeTimes != null) {
            properties.put(PropertyKeyConst.MaxReconsumeTimes, this.reconsumeTimes);
        }
        if (this.suspendTimeMillis != null) {
            properties.put(PropertyKeyConst.SuspendTimeMillis, this.suspendTimeMillis);
        }

        return properties;
    }
}