package com.ruoyi.common.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 直播配置
 *
 * @Description 直播管理配置表
 * @Author Claude Code
 * @Date 2025/01/28
 */
@TableName(value = "t_live_config", autoResultMap = true)
@Data
public class LiveConfig {

    /**
     * 配置ID
     */
    @TableId(type = IdType.AUTO)
    private Integer configId;

    /**
     * 配置键
     */
    @ApiModelProperty("配置键")
    private String configKey;

    /**
     * 配置值内容
     */
    @ApiModelProperty("配置值内容")
    private String configValue;

    /**
     * 配置类型
     */
    @ApiModelProperty("配置类型(text/html)")
    private String configType;

    /**
     * 配置说明
     */
    @ApiModelProperty("配置说明")
    private String description;

    /**
     * 状态 0:禁用 1:启用
     */
    @ApiModelProperty("状态 0:禁用 1:启用")
    private Integer status;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Long updateTime;
}