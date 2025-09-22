package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 代理商产品更新
 */
@Data
public class AgentProductUpdateBO {

    /**
     * 产品ID
     */
    @ApiModelProperty("产品ID")
    private Integer productId;

    /**
     * 开放代理商列表
     */
    @ApiModelProperty("开放代理商列表")
    private List<String> agentCodeList;

}
