package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 代理商账号注册
 */
@Data
public class AgentAccountAddBO {

    /**
     * 代理商账户名
     */
    @NotNull(message = "账户名称不能为空")
    @ApiModelProperty("代理商账户名称")
    private String userName;

    /**
     * 代理商账户密码
     */
    @NotNull(message = "账户密码不能为空")
    @ApiModelProperty("代理商账户密码")
    private String password;

    /**
     * 验证码
     */
    @NotNull(message = "验证码不能为空")
    @ApiModelProperty("验证码")
    private String smsCode;

    /**
     * 手机号
     */
    @NotNull(message = "手机号不能为空")
    @ApiModelProperty(value = "手机号")
    private String phone;

    /**
     * 推荐人编码
     */
    @NotNull(message = "推荐人编码不能为空")
    @ApiModelProperty(value = "推荐人编码")
    private String parentAgentCode;



}
