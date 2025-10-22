package com.ruoyi.common.vip.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * VIP用户保存参数
 *
 * 用于新增或修改VIP用户基础信息
 *
 * @author Codex
 */
@Data
public class VipUserSaveBO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "代理商账号ID")
    private Integer agentAccountId;

    @ApiModelProperty(value = "sys_user表ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @ApiModelProperty(value = "代理商编码", required = true)
    @NotBlank(message = "代理商编码不能为空")
    private String agentCode;

    @ApiModelProperty(value = "代理商名称", required = true)
    @NotBlank(message = "代理商名称不能为空")
    private String agentName;

    @ApiModelProperty(value = "VIP等级", required = true)
    @NotNull(message = "VIP等级不能为空")
    @Min(value = 0, message = "VIP等级不能小于0")
    private Integer vipLevel;

    @ApiModelProperty(value = "备注")
    private String remark;
}

