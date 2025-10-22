package com.ruoyi.common.vip.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * VIP用户实体
 *
 * 用于记录系统内手动维护的VIP用户信息
 *
 * @author Codex
 */
@TableName(value = "t_vip_user", autoResultMap = true)
@Data
public class VipUser {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键ID")
    private Long id;

    /**
     * 代理商账号ID(t_agent_account表ID)
     */
    @ApiModelProperty(value = "代理商账号ID")
    private Integer agentAccountId;

    /**
     * sys_user表ID
     */
    @ApiModelProperty(value = "sys_user表ID")
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
     * 当前VIP等级
     */
    @ApiModelProperty(value = "当前VIP等级")
    private Integer vipLevel;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 最近操作人ID
     */
    @ApiModelProperty(value = "最近操作人ID")
    private Long operatorId;

    /**
     * 最近操作人姓名
     */
    @ApiModelProperty(value = "最近操作人姓名")
    private String operatorName;

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
}

