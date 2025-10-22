package com.ruoyi.common.vip.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
/**
 * VIP配置新增/修改参数
 *
 * @author Codex
 */
@Data
public class VipConfigSaveBO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;

    /**
     * VIP等级
     */
    @ApiModelProperty(value = "VIP等级", required = true)
    @NotNull(message = "VIP等级不能为空")
    @Min(value = 0, message = "VIP等级不能小于0")
    private Integer vipLevel;

    /**
     * 等级名称
     */
    @ApiModelProperty(value = "等级名称", required = true)
    @NotBlank(message = "等级名称不能为空")
    private String levelName;

    /**
     * 升级所需订单数
     */
    @ApiModelProperty(value = "升级所需订单数")
    @NotNull(message = "升级所需订单数不能为空")
    @Min(value = 0, message = "升级所需订单数不能小于0")
    private Integer requiredOrders;

    /**
     * 固定佣金加成（单位：元）
     */
    @ApiModelProperty(value = "固定佣金加成（单位：元）")
    private Integer fixedCommission;

    /**
     * 等级图标
     */
    @ApiModelProperty(value = "等级图标")
    private String levelIcon;

    /**
     * 是否启用（0-停用 1-启用）
     */
    @ApiModelProperty(value = "是否启用（0-停用 1-启用）")
    private Integer isEnabled;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
}
