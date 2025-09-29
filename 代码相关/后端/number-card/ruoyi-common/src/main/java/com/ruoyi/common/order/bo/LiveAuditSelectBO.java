package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 直播审核查询条件
 *
 * @Description 代理商直播审核查询BO
 * @Author Claude Code
 * @Date 2025/01/28
 */
@Data
public class LiveAuditSelectBO extends BaseBO {

    /**
     * 代理商名称
     */
    @ApiModelProperty("代理商名称")
    private String agentName;

    /**
     * 代理商编码
     */
    @ApiModelProperty("代理商编码")
    private String agentCode;

    /**
     * 抖音号
     */
    @ApiModelProperty("抖音号")
    private String douyinAccount;

    /**
     * 状态 0 待认证 1 审核失败 2 审核成功
     */
    @ApiModelProperty("状态 0 待认证 1 审核失败 2 审核成功")
    private Integer status;
}