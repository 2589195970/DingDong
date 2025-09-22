package com.ruoyi.common.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 代理商产品查询返回值
 */
@Data
@ApiModel(value = "代理商产品查询返回值")
public class AgentProductSelectVO {

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
     * 运营商类型
     */
    @ApiModelProperty("运营商类型")
    private Integer operatorType;

    /**
     * 产品特征
     */
    @ApiModelProperty("产品特征")
    private String productCharacteristics;

    /**
     * 产品类型
     */
    @ApiModelProperty("产品类型")
    private Integer productType;

    /**
     * 产品备注
     */
    @ApiModelProperty("产品备注")
    private String productRemark;

    /**
     * 产品通用流量
     */
    @ApiModelProperty("产品通用流量")
    private String productTyll;

    /**
     * 产品定向流量
     */
    @ApiModelProperty("产品定向流量")
    private String productDxll;

    /**
     * 产品通话分钟
     */
    @ApiModelProperty("产品通话分钟")
    private String productThfz;

    /**
     * 产品优惠月租
     */
    @ApiModelProperty("产品优惠月租")
    private String productYhyz;

    /**
     * 产品原始月租
     */
    @ApiModelProperty("产品原始月租")
    private String productYsyz;

    /**
     * 产品首充说明
     */
    @ApiModelProperty("产品首充说明")
    private String productScsm;

    /**
     * 产品归属地区
     */
    @ApiModelProperty("产品归属地区")
    private String productGsdq;

    /**
     * 产品发货方式
     */
    @ApiModelProperty("产品发货方式")
    private String productFafs;

    /**
     * 产品合约期限
     */
    @ApiModelProperty("订单ID")
    private String productHyqx;

    /**
     * 产品套餐介绍
     */
    @ApiModelProperty("产品套餐介绍")
    private String productTcjs;

    /**
     * 产品推广要求
     */
    @ApiModelProperty("产品推广要求")
    private String productDemand;

    /**
     * 开启产品分单 0 关闭 1开启 开启后上游API和产品从配置表中读取
     */
    @ApiModelProperty("开启产品分单 0 关闭 1开启 开启后上游API和产品从配置表中读取")
    private Integer isDispatchUpstreamApi;

    /**
     * '上游API CODE'
     */
    @ApiModelProperty("上游API CODE")
    private String upstreamApiCode;

    /**
     * 上游API 名称
     */
    @ApiModelProperty("上游API 名称")
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
     * 产品最小年龄(周岁)
     */
    @ApiModelProperty("产品最小年龄(周岁)")
    private Integer productAgeMin;

    /**
     * 产品最大年龄(周岁)
     */
    @ApiModelProperty("产品最大年龄(周岁)")
    private Integer productAgeMax;

    /**
     * 产品模板类型
     */
    @ApiModelProperty("产品模板类型")
    private Integer productTemplateType;

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
     * 产品模板其他参数 json格式
     */
    @ApiModelProperty("产品模板其他参数")
    private String productTemplateJson;

    /**
     * 产品状态 0 下架 1上架
     */
    @ApiModelProperty("产品状态 0 下架 1上架")
    private Integer productStatus;

    /**
     * 产品佣金
     */
    @ApiModelProperty("产品佣金")
    private Integer productCommission;


    /**
     * 产品展示排序
     */
    @ApiModelProperty("产品展示排序")
    private Integer productSort;

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
}
