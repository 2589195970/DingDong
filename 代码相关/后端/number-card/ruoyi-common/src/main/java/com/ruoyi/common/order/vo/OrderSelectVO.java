package com.ruoyi.common.order.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单查询返回值
 */
@Data
@ApiModel(value = "订单查询返回值")
public class OrderSelectVO {

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
     * 上游API
     */
    @ApiModelProperty("上游API")
    private String upstreamApi;

    /**
     * 上游API名称
     */
    @ApiModelProperty("上游API名称")
    private String upstreamApiName;

    /**
     * 上游产品CODE
     */
    @ApiModelProperty("上游产品CODE")
    private String upstreamProductCode;

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
     * 充值时间
     */
    @ApiModelProperty("充值时间")
    private Long rechargeTime;


    /**
     * 快递公司
     */
    @ApiModelProperty("快递公司")
    private String express;


    /**
     * 下快递单号
     */
    @ApiModelProperty("下快递单号")
    private String trackingNumber;

    /**
     * 发货时间
     */
    @ApiModelProperty("发货时间")
    private Long deliveryTime;

    /**
     * 激活时间
     */
    @ApiModelProperty("激活时间")
    private Long activeTime;

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
     * 生产号码
     */
    @ApiModelProperty("生产号码")
    private String accNumber;

    /**
     * 订单来源 具体来源值看枚举
     */
    @ApiModelProperty("订单来源")
    private Integer orderSource;

    /**
     * 订单临时参数
     */
    @ApiModelProperty("订单临时参数")
    private String jsonParam;

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
     * 运营商类型
     */
    @ApiModelProperty("运营商类型")
    private Integer operatorType;

    /**
     * 产品主图URL
     */
    @ApiModelProperty("产品主图URL")
    private String productMasterMap;


    /**
     * 产品推广要求
     */
    @ApiModelProperty("产品推广要求")
    private String productDemand;


    /**
     * 产品佣金
     */
    @ApiModelProperty("产品佣金")
    private Integer productCommission;
}
