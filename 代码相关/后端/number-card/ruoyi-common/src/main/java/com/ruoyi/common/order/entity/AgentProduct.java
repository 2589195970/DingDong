package com.ruoyi.common.order.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 代理商产品表
 * 记录各个代理 产品可见及佣金
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/15 16:39
 */
@TableName(value = "t_agent_product", autoResultMap = true)
@Data
public class AgentProduct {

    /**
     * 代理产品ID
     */
    @TableId(type = IdType.AUTO)
    private Integer agentProductId;

    /**
     * 父产品CODE
     */
    private String parentProductCode;

    /**
     * 父产品名称
     */
    private String parentProductName;

    /**
     * 父代理商CODE
     */
    private String parentAgentCode;

    /**
     * 代理商CODE
     */
    private String agentCode;

    /**
     * 代理商名称
     */
    private String agentName;

    /**
     * 是否开放全部代理商 0 否 1是
     */
    @ApiModelProperty("是否开放全部代理商 0 否 1是")
    private Integer isAllAgent = 0;

    /**
     * 代理商产品状态 0 下架 1上架
     */
    private Integer productStatus;

    /**
     * 产品佣金(上游给定佣金)
     */
    private Integer productCommission;

    /**
     * 收入佣金(产品佣金-分销佣金)
     */
    private Integer revenueProductCommission;

    /**
     * 分销佣金(下游结算佣金)
     */
    private Integer distributionProductCommission;

    /**
     * 产品展示排序
     */
    private Integer productSort;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 产品二维码推广图url
     */
    @ApiModelProperty("产品二维码推广图url")
    private String productQrcodeMap;




}
