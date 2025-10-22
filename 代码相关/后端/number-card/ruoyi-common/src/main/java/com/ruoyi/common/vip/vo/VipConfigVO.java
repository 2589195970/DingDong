package com.ruoyi.common.vip.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * VIP配置返回对象
 *
 * @author Codex
 */
@Data
public class VipConfigVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "VIP等级")
    private Integer vipLevel;

    @ApiModelProperty(value = "等级名称")
    private String levelName;

    @ApiModelProperty(value = "升级所需订单数")
    private Integer requiredOrders;

    @ApiModelProperty(value = "固定佣金加成（单位：元）")
    private Integer fixedCommission;

    @ApiModelProperty(value = "等级图标")
    private String levelIcon;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否启用（0-停用 1-启用）")
    private Integer isEnabled;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
