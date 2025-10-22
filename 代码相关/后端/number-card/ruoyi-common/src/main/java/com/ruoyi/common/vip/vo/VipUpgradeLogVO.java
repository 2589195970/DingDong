package com.ruoyi.common.vip.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * VIP升级日志返回对象
 *
 * @author Codex
 */
@Data
public class VipUpgradeLogVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "代理商编码")
    private String agentCode;

    @ApiModelProperty(value = "代理商名称")
    private String agentName;

    @ApiModelProperty(value = "原VIP等级")
    private Integer fromLevel;

    @ApiModelProperty(value = "新VIP等级")
    private Integer toLevel;

    @ApiModelProperty(value = "升级类型")
    private String upgradeType;

    @ApiModelProperty(value = "升级原因")
    private String upgradeReason;

    @ApiModelProperty(value = "升级时订单数量")
    private Integer orderCount;

    @ApiModelProperty(value = "操作人ID")
    private Long operatorId;

    @ApiModelProperty(value = "操作人姓名")
    private String operatorName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
