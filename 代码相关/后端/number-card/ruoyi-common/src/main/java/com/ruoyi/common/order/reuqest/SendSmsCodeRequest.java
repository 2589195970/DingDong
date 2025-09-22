package com.ruoyi.common.order.reuqest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;


/**
 * 发送短信验证码
 */
@Data
@ApiModel("发送短信验证码")
public class SendSmsCodeRequest {

    /**
     * 接收号码
     */
    @ApiModelProperty(value = "手机号", required = true)
    @NotNull(message = "手机号不能为空")
    @Length(min = 11, max = 11, message = "手机号长度错误")
    private String phoneNumber;

}
