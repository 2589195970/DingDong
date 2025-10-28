package com.ruoyi.common.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 代理商产品列表
 */
@Data
public class ProductListVO {

    /**
     * 产品ID
     */
    @ApiModelProperty("产品ID")
    private Integer productId;


    /**
     * 产品CODE 自动生成的编码
     */
    @ApiModelProperty("产品CODE")
    private String productCode;

    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    private String productName;

    /**
     * 产品主图URL
     */
    @ApiModelProperty("产品主图URL")
    private String productMasterMap;

    /**
     * 产品详情图url
     */
    @ApiModelProperty("产品详情图url")
    private String productDetailMap;

    /**
     * 产品海报图url
     */
    @ApiModelProperty("产品海报图url")
    private String productPlacardMap;

    /**
     * 产品二维码推广图url
     */
    @ApiModelProperty("产品二维码推广图url")
    private String productQrcodeMap;

    /**o
     * 产品模板其他参数 json格式
     */
    @ApiModelProperty("产品模板其他参数json格式")
    private String productTemplateJson;

    /**
     * 产品H5链接
     */
    @ApiModelProperty("产品H5链接")
    private String h5Url;

    /**
     * 代理商编码
     */
    @ApiModelProperty(value = "代理商编码")
    private String agentCode;

    /**
     * 代理商名称
     */
    @ApiModelProperty(value = "代理商名称")
    private String agentName;

    @ApiModelProperty("运营商类型")
    private Integer operatorType;

    /**
     * '产品通用流量'
     */
    @ApiModelProperty(value = "'产品通用流量'")
    private String productTyll;

    /**
     * '产品定向流量'
     */
    @ApiModelProperty(value = "'产品定向流量'")
    private String productDxll;

    /**
     * 产品通话分钟
     */
    @ApiModelProperty(value = "产品通话分钟")
    private String productThfz;

    /**
     * 产品首充说明
     */
    @ApiModelProperty(value = "产品首充说明")
    private String productScsm;

    /**
     * '产品归属地区'
     */
    @ApiModelProperty(value = "'产品归属地区'")
    private String productGsdq;
    /**
     * '产品发货方式'
     */
    @ApiModelProperty(value = "'产品发货方式'")
    private String productFafs;
    /**
     * '产品合约期限'
     */
    @ApiModelProperty(value = "'产品合约期限'")
    private String productHyqx;

    /**
     * 是否需要上传照片 0 否 1 是
     */
    @ApiModelProperty("是否需要上传照片 0 否 1 是")
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
     * 照片上传配置 JSON格式
     */
    @ApiModelProperty("照片上传配置 JSON格式")
    private String photoConfig;

}
