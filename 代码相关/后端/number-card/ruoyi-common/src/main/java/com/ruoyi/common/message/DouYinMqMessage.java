package com.ruoyi.common.message;

import lombok.Data;

/**
 * 抖音统一消息格式
 */
@Data
public class DouYinMqMessage {

    /**
     * 店铺类型：
     * 快手
     * 拼多多
     * 抖音
     * 天猫
     */
    private Integer shopType;
    /**
     * 回传状态：
     * 1、异步下单
     * 2、发货回传
     * 3、失败回传
     */
    private Integer reviewStatus;

    //具体消息内容
    private String messageData;

    /**
     * 发货类型 0 自动发货 1手动发货
     */
    private Integer shipType;
}
