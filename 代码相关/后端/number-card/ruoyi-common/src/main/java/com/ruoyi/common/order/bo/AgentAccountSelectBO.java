package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 代理商列表查询
 */
@Data
public class AgentAccountSelectBO extends BaseBO{

    /**
     * 上级名称
     */
    @ApiModelProperty("上级名称")
    private String parentAgentName;

    /**
     * 代理商名称
     */
    @ApiModelProperty("代理商名称")
    private String agentName;

    /**
     * 是否实名 0否 1是
     */
    @ApiModelProperty("是否实名 0否 1是")
    private Integer isRealName;

    /**
     *是否启用  0 启用 1禁用
     */
    @ApiModelProperty("0 启用 1禁用")
    private Integer isEnabled;

    /**
     * 代理商等级(1-5)
     */
    @ApiModelProperty("代理商等级(1-5)")
    private Integer level;


}
