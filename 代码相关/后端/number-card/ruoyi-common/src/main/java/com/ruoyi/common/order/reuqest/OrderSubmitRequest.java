package com.ruoyi.common.order.reuqest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/12 17:11
 */
@Data
public class OrderSubmitRequest {

    /**
     * 用户姓名
     */
    @ApiModelProperty(value = "用户姓名", required = true)
    private String cardName;

    /**
     * 用户下单手机号
     */
    @ApiModelProperty(value = "用户下单手机号", required = true)
    private String cardPhone;

    /**
     * 用户身份证
     */
    @ApiModelProperty(value = "用户身份证", required = true)
    private String cardId;

    /**
     * 省编码(国家标准地址编码)
     */
    @ApiModelProperty(value = "省编码", required = true)
    private String provinceCode;

    /**
     * 省名称
     */
    @ApiModelProperty(value = "省名称", required = true)
    private String provinceName;

    /**
     * 市编码(国家标准地址编码)
     */
    @ApiModelProperty(value = "市编码", required = true)
    private String cityCode;

    /**
     * 市名称
     */
    @ApiModelProperty(value = "市名称", required = true)
    private String cityName;

    /**
     * 县/区编码(国家标准地址编码)
     */
    @ApiModelProperty(value = "县/区编码", required = true)
    private String countyCode;

    /**
     * 县/区名称
     */
    @ApiModelProperty(value = "县/区名称", required = true)
    private String countyName;

    /**
     * 用户详细地址
     */
    @ApiModelProperty(value = "用户详细地址", required = true)
    private String cardAddress;

    /**
     * 产品编码
     */
    @ApiModelProperty(value = "产品编码", required = true)
    private String productCode;

    /**
     * 代理商编码
     */
    @ApiModelProperty(value = "代理商编码", required = true)
    private String agentCode;

    /**
     * 订单临时字段
     */
    @ApiModelProperty(value = "json", required = true)
    private String jsonParam;

    /**
     * 订单来源 0 信息流 1合作方API进单 2导单 3重推 4快手 5抖店 6转单
     */
    @ApiModelProperty(value = "订单来源 0 信息流 1合作方API进单 2导单 3重推 4快手 5抖店 6转单", required = true)
    private Integer orderSource;

    /**
     * 下游订单号
     */
    @ApiModelProperty(value = "下游订单号", required = true)
    private String orderDownstreamId;
}
