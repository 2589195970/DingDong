package com.ruoyi.common.vip.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * VIP用户返回对象
 *
 * @author Codex
 */
@Data
public class VipUserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "记录ID")
    private Long id;

    @ApiModelProperty(value = "代理商账号ID")
    private Integer agentAccountId;

    @ApiModelProperty(value = "sys_user表ID")
    private Long userId;

    @ApiModelProperty(value = "代理商编码")
    private String agentCode;

    @ApiModelProperty(value = "代理商名称")
    private String agentName;

    @ApiModelProperty(value = "VIP等级")
    private Integer vipLevel;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "最近操作人ID")
    private Long operatorId;

    @ApiModelProperty(value = "最近操作人姓名")
    private String operatorName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}

