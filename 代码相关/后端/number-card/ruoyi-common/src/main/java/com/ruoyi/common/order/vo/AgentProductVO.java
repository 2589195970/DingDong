package com.ruoyi.common.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 代理商产品查询返回值
 */
@Data
public class AgentProductVO {

    /**
     * 产品ID
     */
    @ApiModelProperty("产品ID")
    private Integer productId;

    /**
     * 代理商产品ID
     */
    @ApiModelProperty("代理商产品ID")
    private Integer agentProductId;

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
     * 产品类型
     */
    @ApiModelProperty("产品类型")
    private Integer productType;

    /**
     * 产品推广要求
     */
    @ApiModelProperty("产品推广要求")
    private String productDemand;

    /**
     * 产品主图URL
     */
    @ApiModelProperty("产品主图URL")
    private String productMasterMap;

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
    @ApiModelProperty("产品佣金(上游给定佣金)")
    private Integer productCommission;

    /**
     * 产品佣金
     */
    @ApiModelProperty("收入佣金(产品佣金-分销佣金)")
    private Integer revenueProductCommission;

    /**
     * 产品佣金
     */
    @ApiModelProperty("产品分销佣金(代理商给下游分销的佣金)")
    private Integer distributionProductCommission;


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
    @ApiModelProperty("产品合约期限")
    private String productHyqx;

    /**
     * 产品套餐介绍
     */
    @ApiModelProperty("产品套餐介绍")
    private String productTcjs;

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
     * 产品详情图url
     */
    @ApiModelProperty("产品详情图url")
    private String productDetailMap;

    /**
     * 产品详情图url
     */
    @ApiModelProperty("产品海报图url")
    private String productQrcodeMap;


    /**
     * 产品H5链接
     */
    @ApiModelProperty("产品H5链接")
    private String h5Url;

    /**
     * 开放代理商列表
     */
    @ApiModelProperty("开放代理商列表")
    private List<String> agentCodeList;
}
