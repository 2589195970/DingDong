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

    /**
     * 身份证正面照片URL
     */
    @ApiModelProperty("身份证正面照片URL")
    private String idCardFrontUrl;

    /**
     * 身份证反面照片URL
     */
    @ApiModelProperty("身份证反面照片URL")
    private String idCardBackUrl;

    /**
     * 免冠照片URL
     */
    @ApiModelProperty("免冠照片URL")
    private String personPhotoUrl;

    /**
     * 自定义照片URL
     */
    @ApiModelProperty("自定义照片URL")
    private String customPhotoUrl;

    /**
     * 照片审核状态 0 无需审核 1 待上传照片 2 代理商待提交 3 管理员待审核 4 审核通过 5 审核拒绝
     */
    @ApiModelProperty("照片审核状态")
    private Integer photoStatus;

    /**
     * 是否必传照片
     */
    @ApiModelProperty("是否必传照片")
    private Integer photoRequired;

    /**
     * 是否需要审核 0 否 1 是
     */
    @ApiModelProperty("是否需要审核 0 否 1 是")
    private Integer sfxysh;

    /**
     * 是否参与佣金返现 0 否 1 是
     */
    @ApiModelProperty("是否参与佣金返现 0 否 1 是")
    private Integer sfyjfx;

    /**
     * 是否需要付费提卡 0 否 1 是
     */
    @ApiModelProperty("是否需要付费提卡 0 否 1 是")
    private Integer sffftk;

    /**
     * 基础提卡费(元)
     */
    @ApiModelProperty("基础提卡费(元)")
    private Integer baseCardFee;

    /**
     * 初始话费余额(元)
     */
    @ApiModelProperty("初始话费余额(元)")
    private Integer productInitialBalance;

    /**
     * 上级给当前代理的提卡费成本(元)
     */
    @ApiModelProperty("上级给当前代理的提卡费成本(元)")
    private Integer incomingCardFee;

    /**
     * 当前代理留存的提卡费差价(元)
     */
    @ApiModelProperty("当前代理留存的提卡费差价(元)")
    private Integer cardFeeProfit;

    /**
     * 当前代理对下级的提卡费售价(元)
     */
    @ApiModelProperty("当前代理对下级的提卡费售价(元)")
    private Integer downstreamCardFee;


    /**
     * 照片上传时间
     */
    @ApiModelProperty("照片上传时间")
    private Long photoUploadTime;

    /**
     * 照片审核员ID
     */
    @ApiModelProperty("照片审核员ID")
    private Long photoAuditUserId;

    /**
     * 照片审核时间
     */
    @ApiModelProperty("照片审核时间")
    private Long photoAuditTime;

    /**
     * 照片审核备注
     */
    @ApiModelProperty("照片审核备注")
    private String photoAuditRemark;

    /**
     * 照片审核状态名称
     */
    @ApiModelProperty("照片审核状态名称")
    private String photoStatusName;

    /**
     * 产品照片配置 JSON格式
     */
    @ApiModelProperty("产品照片配置 JSON格式")
    private String photoConfig;
}
