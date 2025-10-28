package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 新增或更新提卡费特例请求
 */
@Data
@ApiModel("新增或更新提卡费特例请求")
public class AgentProductCardFeeOverrideCreateBO {

    /**
     * 目标代理产品ID
     */
    @ApiModelProperty(value = "目标代理产品ID", required = true)
    private Integer agentProductId;

    /**
     * 特例提卡费售价(元)
     */
    @ApiModelProperty(value = "特例提卡费售价(元)", required = true)
    private Integer overrideFee;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String memo;

    /**
     * 失效时间(毫秒时间戳，可选)
     */
    @ApiModelProperty("失效时间(毫秒时间戳，可选)")
    private Long expireTime;
}
