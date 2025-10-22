package com.ruoyi.common.vip.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * VIP升级日志
 *
 * 记录代理商VIP等级变更轨迹
 *
 * @author Codex
 */
@TableName(value = "t_vip_upgrade_log", autoResultMap = true)
@Data
public class VipUpgradeLog {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键ID")
    private Long id;

    /**
     * 用户ID(sys_user表ID)
     */
    @ApiModelProperty(value = "用户ID(sys_user表ID)")
    private Long userId;

    /**
     * 代理商编码
     */
    @ApiModelProperty(value = "代理商编码")
    private String agentCode;

    /**
     * 代理商名称
     */
    @ApiModelProperty(value = "代理商名称")
    private String agentName;

    /**
     * 原VIP等级
     */
    @ApiModelProperty(value = "原VIP等级")
    private Integer fromLevel;

    /**
     * 新VIP等级
     */
    @ApiModelProperty(value = "新VIP等级")
    private Integer toLevel;

    /**
     * 升级类型(AUTO/MANUAL)
     */
    @ApiModelProperty(value = "升级类型(AUTO/MANUAL)")
    private String upgradeType;

    /**
     * 升级原因
     */
    @ApiModelProperty(value = "升级原因")
    private String upgradeReason;

    /**
     * 升级时订单数量
     */
    @ApiModelProperty(value = "升级时订单数量")
    private Integer orderCount;

    /**
     * 操作人ID(手动升级时记录)
     */
    @ApiModelProperty(value = "操作人ID(手动升级时记录)")
    private Long operatorId;

    /**
     * 操作人姓名
     */
    @ApiModelProperty(value = "操作人姓名")
    private String operatorName;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
