package com.ruoyi.common.vip.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Lightweight view of the current user's VIP information.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VipUserSummaryVO extends VipUserVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "VIP等级名称")
    private String vipLevelName;

    @ApiModelProperty(value = "VIP等级图标")
    private String vipLevelIcon;

    @ApiModelProperty(value = "是否存在VIP记录")
    private boolean hasVipRecord;
}
