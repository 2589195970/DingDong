package com.ruoyi.common.order.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 发送短信
 *
 * @Description
 * @Author 陈思伟
 * @Date 2025-03-09 11:21
 */
@Data
public class SmsDTO {
    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phoneNumber;

    /**
     * 发送短信模板类型
     */
    @ApiModelProperty("发送短信模板类型 0 代理商注册 1 实名手机号 2短信登陆")
    private Integer smsTemplateType;

    /**
     * 验证码
     */
    @ApiModelProperty("验证码")
    private String smsCode;
}
