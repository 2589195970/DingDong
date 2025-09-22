package com.ruoyi.common.apis.douyin;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author 陈思伟
 * @version 1.0
 * @description: 抖店详情返回结果公共类
 * @date 2025/7/15 11:54
 */
@Data
@Builder
public class DouDianOrderResult {
    /**
     * 订单id
     */
    private String oId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 身份证
     */
    private String idCard;
    /**
     * 电话
     */
    private String tel;
    /**
     * 详细地址
     */
    private String detailAdd;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 区
     */
    private String town;
    /**
     * 街道
     */
    private String street;
    /**
     * 商品id
     */
    private String productId;
    /**
     * 商品名
     */
    private String productName;
    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 规格信息 map中包含 name value两个参数
     */
    private List<Map> spec;

    /**
     * 店铺ID
     */
    String shopId;

    private String appKey;

    private String appSecret;


    /**
     * 抖店ID
     */
    private String doudianOpenId;

    /**
     * 订单来源详情
     */
    private String typeDesc;
}
