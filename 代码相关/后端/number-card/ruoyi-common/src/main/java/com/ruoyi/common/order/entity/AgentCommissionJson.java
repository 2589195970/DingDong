package com.ruoyi.common.order.entity;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 代理商佣金计算
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/22 16:39
 */
@Data
public class AgentCommissionJson {

    /**
     * sysUser表ID
     */
    @ApiModelProperty(value = "sysUser表ID")
    private Long sysUserId;

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

    /**
     * 等级
     */
    @ApiModelProperty(value = "等级0-5级")
    private Integer level;

    /**
     * 产品佣金(代理商能够拿到的佣金)
     */
    @ApiModelProperty(value = "产品佣金")
    private Integer productCommission;

    /**
     * 收入佣金(产品佣金-分销佣金)
     */
    @ApiModelProperty(value = "收入佣金")
    private Integer revenueProductCommission;

    /**
     * 产品分销佣金(代理商给下游分销的佣金)
     */
    @ApiModelProperty(value = "产品分销佣金")
    private Integer distributionProductCommission;

}
