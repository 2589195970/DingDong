package com.ruoyi.common.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 */
@Data
@ApiModel(value = "代理商列表返回值")
public class AgentAccountListVO {

    /**
     * 代理商账号ID
     */
    @ApiModelProperty("代理商账号ID")
    private Integer agentAccountId;

    /**
     * 账号ID
     */
    @ApiModelProperty("账号ID")
    private String sysUserId;

    /**
     * 账号名称
     */
    @ApiModelProperty("账号名称")
    private String sysUserName;

    /**
     * 父代理商CODE
     */
    @ApiModelProperty("父代理商CODE")
    private String parentAgentCode;

    /**
     * 父代理商姓名
     */
    @ApiModelProperty("父代理商姓名")
    private String parentAgentName;

    /**
     * 代理商CODE
     */
    @ApiModelProperty("代理商CODE")
    private String agentCode;

    /**
     * 代理商名称
     */
    @ApiModelProperty("代理商名称")
    private String agentName;

    /**
     * 等级
     */
    @ApiModelProperty("等级")
    private String level;

    /**
     * 是否实名 0否 1是
     */
    @ApiModelProperty("是否实名 0否 1是")
    private Integer isRealName;

    /**
     * 等级
     */
    @ApiModelProperty("状态 0 启用 1禁用")
    private Integer isEnabled;

    /**
     *  0 订单加密 1 订单解密
     */
    @ApiModelProperty(value = "0 订单加密 1 订单解密")
    private Integer isEncrypt;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;

    /**
     * 注册时间
     */
    @ApiModelProperty("注册时间")
    private Long createTime;

    /**
     * 余额 单位分
     */
    @ApiModelProperty("余额 单位分")
    private Integer balance;
}
