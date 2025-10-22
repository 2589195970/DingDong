package com.ruoyi.common.vip.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * VIP等级配置
 *
 * 用于定义各VIP等级的基础信息及升级条件
 *
 * @author Codex
 */
@TableName(value = "t_vip_config", autoResultMap = true)
@Data
public class VipConfig {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键ID")
    private Long id;

    /**
     * VIP等级
     */
    @ApiModelProperty(value = "VIP等级")
    private Integer vipLevel;

    /**
     * 等级名称
     */
    @ApiModelProperty(value = "等级名称")
    private String levelName;

    /**
     * 升级所需订单数
     */
    @ApiModelProperty(value = "升级所需订单数")
    private Integer requiredOrders;

    /**
     * VIP固定佣金加成（单位：元）
     */
    @ApiModelProperty(value = "VIP固定佣金加成（单位：元）")
    private Integer fixedCommission;

    /**
     * 等级图标
     */
    @ApiModelProperty(value = "等级图标")
    private String levelIcon;

    /**
     * 是否启用 0否 1是
     */
    @ApiModelProperty(value = "是否启用 0否 1是")
    private Integer isEnabled;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String remark;
}
