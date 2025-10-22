package com.ruoyi.common.vip.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * VIP用户等级调整参数
 *
 * @author Codex
 */
@Data
public class VipUserSetLevelBO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "VIP用户记录ID", required = true)
    @NotNull(message = "VIP用户记录ID不能为空")
    private Long id;

    @ApiModelProperty(value = "目标VIP等级", required = true)
    @NotNull(message = "VIP等级不能为空")
    @Min(value = 0, message = "VIP等级不能小于0")
    private Integer vipLevel;

    @ApiModelProperty(value = "备注")
    private String remark;
}

