package com.ruoyi.common.vip.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * VIP升级日志查询参数
 *
 * @author Codex
 */
@Data
public class VipUpgradeLogQueryBO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "页码", example = "1")
    private Integer pageNo = 1;

    @ApiModelProperty(value = "每页条数", example = "10")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "代理商编码")
    private String agentCode;

    @ApiModelProperty(value = "代理商名称")
    private String agentName;

    @ApiModelProperty(value = "原等级")
    private Integer fromLevel;

    @ApiModelProperty(value = "目标等级")
    private Integer toLevel;

    @ApiModelProperty(value = "VIP等级（默认匹配升级后等级）")
    private Integer vipLevel;

    @ApiModelProperty(value = "升级类型(AUTO/MANUAL)")
    private String upgradeType;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
}
