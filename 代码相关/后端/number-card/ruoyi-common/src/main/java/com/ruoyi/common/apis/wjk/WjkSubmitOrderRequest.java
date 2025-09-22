package com.ruoyi.common.apis.wjk;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author 陈思伟
 */
@Data
@ApiModel("第三方提交订单")
public class WjkSubmitOrderRequest extends WjkBaseRequest {

    /**
     * 第三方订单号
     */
    @ApiModelProperty(value = "合作方订单号", required = true)
    private String orderNo;

    /**
     * 客户姓名
     */
    @ApiModelProperty(value = "客户姓名", required = true)
    private String cusName;

    /**
     * 客户身份证号
     */
    @ApiModelProperty(value = "身份证号", required = true)
    private String cusCertNo;

    /**
     * 客户联系电话
     */
    @NotBlank(message = "客户联系电话不能为空")
    @ApiModelProperty(value = "客户联系电话", required = true)
    private String cusContactPhone;
    /**
     * 收货地址-省
     */
    @ApiModelProperty(value = "收货地址-省", required = true)
    private String province;

    /**
     * 收货地址-市
     */
    @ApiModelProperty(value = "收货地址-市", required = true)
    private String city;
    /**
     * 收货地址-区县
     */
    @ApiModelProperty(value = "收货地址-区县", required = true)
    private String district;

    /**
     * 收货地址-具体地址
     */
    @ApiModelProperty(value = "收货地址-具体地址", required = true)
    private String address;

    /**
     * 短信验证码
     */
    @ApiModelProperty(value = "短信验证码")
    private String smsCode;

    /**
     * 客户预占号码
     */
    @ApiModelProperty(value = "客户预占号码")
    private String cusAccPhone;

    /**
     * 预占号码ID
     */
    @ApiModelProperty(value = "预占号码ID")
    private String numberId;

    /**
     * 预占号码池ID
     */
    @ApiModelProperty(value = "预占号码池ID")
    private String poolId;

    /**
     * 附加字段1
     */
    @ApiModelProperty(value = "附加字段1")
    private String extend1;

    /**
     * 附加字段2
     */
    @ApiModelProperty(value = "附加字段2")
    private String extend2;
}
