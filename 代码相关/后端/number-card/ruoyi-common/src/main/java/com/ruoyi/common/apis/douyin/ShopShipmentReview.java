package com.ruoyi.common.apis.douyin;

import lombok.Data;

/**
 * 店铺存待发货回传的redis内容的统一格式
 * 快手、拼多多、抖音
 */
@Data
public class ShopShipmentReview {

    /**
     * 店铺类型：
     * 1、快手
     * 2、拼多多
     * 3、抖音
     */
    private Integer shopType;

    /**
     * 待回传的店铺订单号
     */
    private String shopOrderId;

    /**
     * 待回传的应用平台ID
     */
    private String platformId;

    /**
     * app KEY
     */
    private String appKey;

    /**
     * app 秘钥
     */
    private String appSecret;

}
