package com.ruoyi.order.rocketmq;

import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import com.ruoyi.common.constant.RocketMqConstant;
import com.ruoyi.order.service.impl.douyin.DouDianOrderListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 */
@Configuration
public class ConsumerClient {

    @Autowired
    private RocketMqConfig rocketMqConfig;

    @Resource
    private DouDianOrderListener douDianOrderListener;

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public ConsumerBean buildConsumer() {
        ConsumerBean consumerBean = new ConsumerBean();
        //配置文件
        Properties properties = rocketMqConfig.getMqProperties();
        //将消费者线程数固定为20个 20为默认值j
        consumerBean.setProperties(properties);
        //订阅关系
        Map<Subscription, MessageListener> subscriptionTable = new HashMap<>(1);

        //抖音相关消息订阅
        Subscription douDianOrderSubscription = new Subscription();
        douDianOrderSubscription.setTopic(RocketMqConstant.ORDER_TOPIC);
        douDianOrderSubscription.setExpression(RocketMqConstant.DOUYIN_ORDER_TAG);
        subscriptionTable.put(douDianOrderSubscription, douDianOrderListener);

        consumerBean.setSubscriptionTable(subscriptionTable);
        return consumerBean;
    }

}
