package com.ruoyi.common.vip.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * VIP操作日志实体
 *
 * 记录后台或代理商执行的VIP相关操作
 *
 * @author Codex
 */
@TableName(value = "t_vip_operation_log", autoResultMap = true)
@Data
public class VipOperationLog {

    /**
     * 日志ID
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "日志ID")
    private Long logId;

    /**
     * 操作用户ID
     */
    @ApiModelProperty(value = "操作用户ID")
    private Long userId;

    /**
     * 目标代理商编码
     */
    @ApiModelProperty(value = "目标代理商编码")
    private String targetAgentCode;

    /**
     * 操作类型(SET_LEVEL/UPGRADE等)
     */
    @ApiModelProperty(value = "操作类型(SET_LEVEL/UPGRADE等)")
    private String operationType;

    /**
     * 执行方法名
     */
    @ApiModelProperty(value = "执行方法名")
    private String methodName;

    /**
     * 请求参数(JSON格式)
     */
    @ApiModelProperty(value = "请求参数(JSON格式)")
    private String requestParams;

    /**
     * 执行结果(JSON格式)
     */
    @ApiModelProperty(value = "执行结果(JSON格式)")
    private String result;

    /**
     * IP地址
     */
    @ApiModelProperty(value = "IP地址")
    private String ipAddress;

    /**
     * 用户代理
     */
    @ApiModelProperty(value = "用户代理")
    private String userAgent;

    /**
     * 操作时间（毫秒时间戳）
     */
    @ApiModelProperty(value = "操作时间（毫秒时间戳）")
    private Long operationTime;

    /**
     * 错误信息
     */
    @ApiModelProperty(value = "错误信息")
    private String errorMessage;

    /**
     * 执行时间(毫秒)
     */
    @ApiModelProperty(value = "执行时间(毫秒)")
    private Integer executionTime;

    /**
     * 操作状态(1成功 0失败)
     */
    @ApiModelProperty(value = "操作状态(1成功 0失败)")
    private Integer status;
}
