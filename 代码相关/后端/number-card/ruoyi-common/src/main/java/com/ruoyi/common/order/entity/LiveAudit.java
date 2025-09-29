package com.ruoyi.common.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 直播审核
 *
 * @Description 代理商直播信息审核表
 * @Author Claude Code
 * @Date 2025/01/28
 */
@TableName(value = "t_live_audit", autoResultMap = true)
@Data
public class LiveAudit {

    /**
     * 直播审核记录ID
     */
    @TableId(type = IdType.AUTO)
    private Integer liveAuditId;

    /**
     * 登录账号ID
     */
    @ApiModelProperty("登录账号ID")
    private Long sysUserId;

    /**
     * 代理商编码
     */
    @ApiModelProperty("代理商编码")
    private String agentCode;

    /**
     * 代理商名称
     */
    @ApiModelProperty("代理商名称")
    private String agentName;

    /**
     * 直播背景图URL
     */
    @ApiModelProperty("直播背景图URL")
    private String backgroundImage;

    /**
     * 抖音UID
     */
    @ApiModelProperty("抖音UID")
    private String douyinUid;

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

    /**
     * 审核备注
     */
    @ApiModelProperty("审核备注")
    private String remark;

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