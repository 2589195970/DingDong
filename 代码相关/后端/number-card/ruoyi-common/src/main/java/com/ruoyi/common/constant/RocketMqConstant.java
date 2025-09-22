package com.ruoyi.common.constant;


import java.util.HashMap;
import java.util.Map;

/**
 * mq 相关常量
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/17 17:36
 */
public class RocketMqConstant {

    private RocketMqConstant() {
    }

    /**
     * 订单异步 topic
     */
    public static final String ORDER_TOPIC = "order";

    /**
     * 抖音订单异步tag
     */
    public static final String DOUYIN_ORDER_TAG = "douyin-order-tag";

}
