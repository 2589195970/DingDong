package com.ruoyi.common.order.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单佣金配置表
 *
 * @Description
 * @Author 陈思伟
 * @Date 2025/2/22 16:39
 */
@TableName(value = "t_commission_config", autoResultMap = true)
@Data
public class CommissionConfig {

    /**
     * 订单佣金配置ID
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("订单佣金配置ID")
    private Integer commissionConfigId;

    /**
     * sysUser表ID
     */
    @ApiModelProperty(value = "sysUser表ID")
    private Long sysUserId;

    /**
     * 代理商编码
     */
    @ApiModelProperty(value = "代理商编码")
    private String agentCode;

    /**
     * 配置类型 0 固定佣金 1 百分比佣金
     */
    @ApiModelProperty("配置类型 0 固定佣金 1 百分比佣金")
    private Integer commissionConfigType;

    /**
     * 固定佣金
     */
    @ApiModelProperty("固定佣金")
    private Integer fixedCommission;

    /**
     * 百分比佣金
     */
    @ApiModelProperty("百分比佣金")
    private Integer scaleCommission;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Long createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Long updateTime;



}
