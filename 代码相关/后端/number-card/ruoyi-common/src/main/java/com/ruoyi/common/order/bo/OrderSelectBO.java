package com.ruoyi.common.order.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单查询
 */
@Data
public class OrderSelectBO extends BaseBO{

    /**
     * 订单ID
     */
    @ApiModelProperty("订单ID")
    private String orderId;

    /**
     * 上游订单号
     */
    @ApiModelProperty("上游订单号")
    private String orderUpstreamId;

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
     * 订单状态
     */
    @ApiModelProperty("订单状态")
    private Integer orderStatus;

    /**
     * 上游API
     */
    @ApiModelProperty("上游API")
    private String upstreamApi;

    /**
     * 是否充值 0 未充值 1已充值是否充值 0 未充值 1已充值
     */
    @ApiModelProperty("是否充值 0 未充值 1已充值")
    private Integer isRecharged;

    /**
     * 生产号码
     */
    @ApiModelProperty("生产号码")
    private String accNumber;

    /**
     * 下游代理商code
     */
    @ApiModelProperty("下游代理商code")
    private String downstreamCode;


    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    private String productName;


    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private Long starTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private Long endTime;

    @ApiModelProperty("订单来源")
    private Integer orderSource;

    /**
     * 订单佣金状态 0 未到结算状态 1 待结算 2 部分结算 3已结算 4无法结算
     */
    @ApiModelProperty("订单佣金状态 0 未到结算状态 1 待结算 2 部分结算 3已结算 4无法结算")
    private Integer orderCommissionStatus;

    /**
     * 上游单号是否为空 0 上游单号为空 1 不为空 默认不选择
     */
    @ApiModelProperty("上游单号是否为空 0 上游单号为空 1 不为空 默认不选择")
    private Integer isNotNullOrderUpstreamId;
}
