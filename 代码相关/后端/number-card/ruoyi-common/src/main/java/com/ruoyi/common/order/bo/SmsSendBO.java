package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 短信发送
 */
@Data
public class SmsSendBO extends BaseBO{

    /**
     * 短信发送手机号
     */
    @ApiModelProperty(value = "短信发送手机号")
    private String phoneNumber;

    /**
     * 0 注册 1 改绑手机号
     */
    @ApiModelProperty(value = "0 注册 1 改绑手机号")
    private Integer smsType;

}
