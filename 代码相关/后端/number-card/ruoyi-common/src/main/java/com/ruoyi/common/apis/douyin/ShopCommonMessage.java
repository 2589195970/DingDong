package com.ruoyi.common.apis.douyin;

import lombok.Data;

/**
 * 店铺用于回传的统一消息格式
 */
@Data
public class ShopCommonMessage {

    /**
     * 店铺类型：抖音
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


}
