package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 代理商产品 返回值
 */
@Data
public class AgentProductBO {

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
     * 产品类型
     */
    @ApiModelProperty("产品类型")
    private Integer productType;


    /**
     * 产品模板其他参数 json格式
     */
    @ApiModelProperty("产品模板其他参数")
    private String productTemplateJson;

    /**
     * 代理商CODE
     */
    @ApiModelProperty("代理商CODE")
    private String agentCode;

    /**
     * 代理商名称
     */
    @ApiModelProperty("代理商名称")
    private String agentName;


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
     * 收入佣金(产品佣金-分销佣金)
     */
    @ApiModelProperty("收入佣金(产品佣金-分销佣金)")
    private Integer revenueProductCommission;

    /**
     * 产品分销佣金
     */
    @ApiModelProperty("产品分销佣金")
    private Integer distributionProductCommission;

    /**
     * 产品展示排序
     */
    @ApiModelProperty("产品展示排序")
    private Integer productSort;

    /**
     * 上游API CODE
     */
    @ApiModelProperty("上游API CODE")
    private String upstreamApiCode;

    /**
     * 上游API 名称
     */
    @ApiModelProperty("上游API 名称")
    private String upstreamApiName;

    /**
     * '上游产品CODE'
     */
    @ApiModelProperty("'上游产品CODE'")
    private String upstreamProductCode;

    /**
     * '上游产品名称'
     */
    @ApiModelProperty("'上游产品名称'")
    private String upstreamProductName;


    /**
     * 代理商父编码列表 json格式
     */
    @ApiModelProperty(value = "代理商父编码列表 json格式")
    private String parentAgentList;


}
