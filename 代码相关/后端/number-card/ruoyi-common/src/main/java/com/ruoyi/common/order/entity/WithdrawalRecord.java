package com.ruoyi.common.order.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 提现记录表
 *
 * @Description
 * @Author 陈思伟
 * @Date 2025/2/22 16:39
 */
@TableName(value = "t_withdrawal_record", autoResultMap = true)
@Data
public class WithdrawalRecord {

    /**
     * 提现记录表id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("提现记录表id")
    private Integer withdrawalRecordId;

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
     * 余额 单位分
     */
    @ApiModelProperty(value = "余额 单位分")
    private Integer balance;

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
