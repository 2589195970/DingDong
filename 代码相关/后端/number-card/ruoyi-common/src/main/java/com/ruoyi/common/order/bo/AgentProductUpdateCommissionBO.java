package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 修改代理商佣金
 */
@Data
public class AgentProductUpdateCommissionBO {

    /**
     * 产品CODE
     */
    @ApiModelProperty("产品CODE")
    private String  productCode;

    /**
     * 产品佣金
     */
    @ApiModelProperty("产品佣金")
    private Integer productCommission;

}
