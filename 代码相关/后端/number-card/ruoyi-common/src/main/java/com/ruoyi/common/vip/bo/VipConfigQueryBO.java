package com.ruoyi.common.vip.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * VIP配置查询参数
 *
 * @author Codex
 */
@Data
public class VipConfigQueryBO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 页码
     */
    @ApiModelProperty(value = "页码", example = "1")
    private Integer pageNo = 1;

    /**
     * 每页大小
     */
    @ApiModelProperty(value = "每页大小", example = "10")
    private Integer pageSize = 10;

    /**
     * 等级名称
     */
    @ApiModelProperty(value = "等级名称")
    private String levelName;

    /**
     * 启用状态
     */
    @ApiModelProperty(value = "启用状态（0-停用 1-启用）")
    private Integer isEnabled;
}

