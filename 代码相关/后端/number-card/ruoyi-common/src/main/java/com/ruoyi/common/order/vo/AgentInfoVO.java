package com.ruoyi.common.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 代理商API信息
 */
@Data
@ApiModel(value = "代理商信息")
public class AgentInfoVO {

    /**
     * 代理商编码
     */
    @ApiModelProperty(value = "代理商编码")
    private String agentCode;


    /**
     * 是否实名 0否 1是
     */
    @ApiModelProperty(value = "是否实名 0否 1是")
    private Integer isRealName;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;
}
