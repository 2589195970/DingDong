package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 实名认证审核
 */
@Data
public class NameAuditSelectBO extends BaseBO{



    /**
     * 代理商名称
     */
    @ApiModelProperty("代理商名称")
    private String agentName;

    /**
     * 状态 0 待认证 1 实名审核失败 2实名认证成功
     */
    @ApiModelProperty("状态 0 待认证 1 实名审核失败 2实名认证成功")
    private Integer status;

}
