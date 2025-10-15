package com.ruoyi.common.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单列表查询返回值
 */
@Data
@ApiModel(value = "订单列表查询返回值")
public class OrderListVO {

    /**
     * 订单ID
     */
    @ApiModelProperty("订单ID")
    private String orderId;

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
     * 生产号码
     */
    @ApiModelProperty("生产号码")
    private String accNumber;

    /**
     * 运营商类型
     */
    @ApiModelProperty("运营商类型")
    private Integer operatorType;

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
     * 照片上传时间
     */
    @ApiModelProperty("照片上传时间")
    private Long photoUploadTime;

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
}
