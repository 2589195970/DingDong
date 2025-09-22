package com.ruoyi.common.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单佣金记录查询返回值
 */
@Data
@ApiModel(value = "订单佣金记录查询返回值")
public class OrderCommissionSelectVO {

    /**
     * 佣金ID
     */
    @ApiModelProperty("佣金ID")
    private Long orderCommissionId;

    /**
     * 订单ID
     */
    @ApiModelProperty("订单ID")
    private String orderId;


    /**
     * 下游订单号
     */
    @ApiModelProperty("下游订单号")
    private String orderDownstreamId;

    /**
     * 用户姓名
     */
    @ApiModelProperty("用户姓名")
    private String cardName;

    /**
     * 用户下单手机号
     */
    @ApiModelProperty("用户下单手机号")
    private String cardPhone;

    /**
     * 用户身份证
     */
    @ApiModelProperty("用户身份证")
    private String cardId;

    /**
     * 省编码(国家标准地址编码)
     */
    @ApiModelProperty("省编码")
    private String provinceCode;

    /**
     * 省名称
     */
    @ApiModelProperty("省名称")
    private String provinceName;

    /**
     * 市编码(国家标准地址编码)
     */
    @ApiModelProperty("市编码")
    private String cityCode;

    /**
     * 市名称
     */
    @ApiModelProperty("市名称")
    private String cityName;

    /**
     * 县/区编码(国家标准地址编码)
     */
    @ApiModelProperty("县/区编码")
    private String countyCode;

    /**
     * 县/区名称
     */
    @ApiModelProperty("县/区名称")
    private String countyName;


    /**
     * 用户详细地址
     */
    @ApiModelProperty("用户详细地址")
    private String cardAddress;

    /**
     * 订单状态
     */
    @ApiModelProperty("订单状态")
    private Integer orderStatus;

    /**
     * 订单说明信息
     */
    @ApiModelProperty("订单说明信息")
    private String orderMessage;

    /**
     * 是否充值 0 未充值 1已充值是否充值 0 未充值 1已充值
     */
    @ApiModelProperty("是否充值")
    private Integer isRecharged;

    /**
     * 充值信息
     */
    @ApiModelProperty("充值信息")
    private String rechargeAmount;

    /**
     * 订单创建时间
     */
    @ApiModelProperty("订单创建时间")
    private Long orderCreateTime;


    /**
     * 激活时间
     */
    @ApiModelProperty("激活时间")
    private Long activeTime;

    /**
     * 订单佣金状态 0 未到结算状态 1 待结算 22 部分结算 3已结算 4无法结算
     */
    @ApiModelProperty("订单佣金状态")
    private Integer orderCommissionStatus;

    /**
      * 订单佣金说明
     */
    @ApiModelProperty("订单佣金说明")
    private String orderCommissionMessage;


    /**
     * 结算时间
     */
    @ApiModelProperty("结算时间")
    private Long commissionTime;

    /**
     * 产品编码
     */
    @ApiModelProperty("产品编码")
    private String productCode;

    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    private String productName;

    /**
     * 产品类型
     */
    @ApiModelProperty("产品类型")
    private String productType;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 上游产品名称
     */
    @ApiModelProperty("上游产品名称")
    private String upstreamProductName;

    /**
     * 上游订单状态
     */
    @ApiModelProperty("上游订单状态")
    private String upstreamOrderStatus;

    /**
     * 上游订单状态说明
     */
    @ApiModelProperty("上游订单状态说明")
    private String upstreamOrderStatusMessage;

    /**
     * 订单向上游推送时间
     */
    @ApiModelProperty("订单向上游推送时间")
    private Long upstreamPushTime;

    /**
     * 下游代理商code
     */
    @ApiModelProperty("下游代理商code")
    private String downstreamCode;

    /**
     * 下游代理商名称
     */
    @ApiModelProperty("下游代理商名称")
    private String downstreamName;

    /**
     * 归属代理商 code
     */
    @ApiModelProperty("下游代理商code")
    private String showDownstreamCode;

    /**
     * 归属代理商名称
     */
    @ApiModelProperty("下游代理商名称")
    private String showDownstreamName;


    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Long createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Long updateTime;

    /**
     * 产品佣金
     */
    @ApiModelProperty("产品佣金")
    private Integer productCommission;
}
