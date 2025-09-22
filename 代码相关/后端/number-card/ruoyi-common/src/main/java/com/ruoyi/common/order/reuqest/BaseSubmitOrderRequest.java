package com.ruoyi.common.order.reuqest;

import lombok.Data;

/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/12 17:11
 */
@Data
public class BaseSubmitOrderRequest {

    /**
     * 下游订单号
     */
    private String orderDownstreamId;

    /**
     * 用户姓名
     */
    private String cardName;

    /**
     * 用户下单手机号
     */
    private String cardPhone;

    /**
     * 用户身份证
     */
    private String cardId;

    /**
     * 用户下单姓名 默认与身份证姓名保持一致
     */
    private String receiverName;

    /**
     * 省编码(国家标准地址编码)
     */
    private String provinceCode;

    /**
     * 省名称
     */
    private String provinceName;

    /**
     * 市编码(国家标准地址编码)
     */
    private String cityCode;

    /**
     * 市名称
     */
    private String cityName;

    /**
     * 县/区编码(国家标准地址编码)
     */
    private String countyCode;

    /**
     * 县/区名称
     */
    private String countyName;

    /**
     * 用户详细地址
     */
    private String cardAddress;

    /**
     * 产品编码
     */
    private String productCode;

    /**
     * 代理商CODE
     */
    private String  agentCode;

    /**
     * 订单临时字段
     */
    private String jsonParam;

    /**
     * 订单来源 0 信息流 1合作方API进单 2导单 3重推 4快手 5抖店 6转单
     */
    private Integer orderSource;

}
