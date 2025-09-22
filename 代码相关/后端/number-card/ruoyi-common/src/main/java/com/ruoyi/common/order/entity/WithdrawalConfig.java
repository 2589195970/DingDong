package com.ruoyi.common.order.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 提现配置表
 *
 * @Description
 * @Author 陈思伟
 * @Date 2025/2/22 16:39
 */
@TableName(value = "t_withdrawal_config", autoResultMap = true)
@Data
public class WithdrawalConfig {

    /**
     * 提现配置ID
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("提现配置ID")
    private Integer withdrawConfigId;

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
     * 最小提现金额 单位元
     */
    @ApiModelProperty(value = "最小提现金额 单位元")
    private Integer withdrawMinAmount;

    /**
     * 提现费率
     */
    @ApiModelProperty(value = "提现费率")
    private Integer withdrawRate;

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
