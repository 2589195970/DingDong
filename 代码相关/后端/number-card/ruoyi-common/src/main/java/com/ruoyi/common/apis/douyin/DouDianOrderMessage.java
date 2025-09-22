package com.ruoyi.common.apis.douyin;

import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * @author zhouxk
 * @version 1.0
 * @description: 抖店订单消息
 * @date 2022/6/15 20:53
 */
@Data
public class DouDianOrderMessage {
    /**
     * 抖店订阅订单号集合
     */
    private Set<String> orderIdSet;

    /**
     * 抖店回传订单号集合
     */
    private Map<String, String> pIdHashMap;

    /**
     * 抖店订单详情
     * 用于状态4 订单延时处理
     */
    private DouDianOrderResult douDianOrderResult;

    /**
     * APP key
     */
    private String appKey = "";

    /**
     * APP 秘钥
     */
    private String appSecret = "";


}
