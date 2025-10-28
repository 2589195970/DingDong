package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 提卡费特例列表查询
 */
@Data
@ApiModel("提卡费特例查询参数")
public class AgentProductCardFeeOverrideQueryBO {

    /**
     * 商品编码
     */
    @ApiModelProperty(value = "商品编码", required = true)
    private String productCode;

    /**
     * 上级代理编码
     */
    @ApiModelProperty(value = "上级代理编码", required = true)
    private String parentAgentCode;
}
