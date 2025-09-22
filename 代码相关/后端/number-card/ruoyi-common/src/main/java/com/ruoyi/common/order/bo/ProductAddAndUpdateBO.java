package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 产品查询
 */
@Data
public class ProductAddAndUpdateBO {

    /**
     * 产品ID
     */
    @ApiModelProperty("产品ID")
    private Integer productId;

    /**
     * 产品名称
     */
    @NotBlank(message = "产品名称不能为空")
    @ApiModelProperty("产品名称")
    private String productName;

    /**
     * 运营商类型
     */
    @NotNull(message = "运营商类型不能为空")
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
    @NotNull(message = "产品类型不能为空")
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
    @ApiModelProperty("产品合约期限")
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
    private Integer isDispatchUpstreamApi = 0;


    /**
     * '上游API id'
     */
    @ApiModelProperty("上游API id")
    private Integer upstreamApiId;

    /**
     * 上游API CODE
     */
    @NotBlank(message = "上游APICODE不能为空")
    @ApiModelProperty("上游APICODE")
    private String upstreamApiCode;

    /**
     * 上游API 名称
     */
    @NotBlank(message = "上游API名称不能为空")
    @ApiModelProperty("上游API 名称")
    private String upstreamApiName;

    /**
     * '上游产品 id'
     */
    @ApiModelProperty("上游产品 id")
    private Integer upstreamProductId;

    /**
     * 上游产品CODE
     */
    @NotBlank(message = "上游产品CODE不能为空")
    @ApiModelProperty("上游产品CODE")
    private String upstreamProductCode;

    /**
     * 上游产品名称
     */
    @NotBlank(message = "上游产品名称不能为空")
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
    @ApiModelProperty("产品模板其他参数json格式")
    private String productTemplateJson;

    /**
     * 产品展示排序
     */
    @ApiModelProperty("产品展示排序")
    private Integer productSort = 0;

    /**
     * 是否开放全部代理商 0 否 1是
     */
    @ApiModelProperty("是否开放全部代理商 0 否 1是")
    private Integer isAllAgent = 0;

    /**
     * 开放代理商列表
     */
    @ApiModelProperty("开放代理商列表")
    private List<String> agentCodeList;

    /**
     * 余额配置
     */
    @ApiModelProperty("余额配置")
    private Integer balanceConfig;

    /**
     * 产品佣金
     */
    @ApiModelProperty("产品佣金")
    private Integer productCommission;

}
