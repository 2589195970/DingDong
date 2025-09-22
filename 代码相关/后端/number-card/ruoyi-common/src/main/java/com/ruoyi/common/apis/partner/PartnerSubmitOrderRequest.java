package com.ruoyi.common.apis.partner;

import com.ruoyi.common.apis.partner.base.PartnerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author 陈思伟
 */
@Data
@ApiModel("合作方提交订单")
public class PartnerSubmitOrderRequest extends PartnerBaseRequest {

    /**
     * 第三方订单号
     */
    @NotBlank(message = "合作方订单号不能为空")
    @Length(max = 32, message = "合作方订单号长度不超过32位")
    @ApiModelProperty(value = "合作方订单号", required = true)
    private String orderNo;

    /**
     * 客户姓名
     */
    @NotBlank(message = "客户姓名不能为空")
    @ApiModelProperty(value = "客户姓名", required = true)
    private String cardName;

    /**
     * 客户身份证号
     */
    @NotBlank(message = "客户身份证号不能为空")
    @ApiModelProperty(value = "身份证号", required = true)
    private String cardId;

    /**
     * 客户联系电话
     */
    @NotBlank(message = "客户联系电话不能为空")
    @ApiModelProperty(value = "客户联系电话", required = true)
    private String cardPhone;
    /**
     * 收货地址-省
     */
    @NotBlank(message = "收货地址-省不能为空")
    @ApiModelProperty(value = "收货地址-省", required = true)
    private String provinceName;

    /**
     * 收货地址-省编码
     */
    @NotBlank(message = "收货地址-省编码不能为空")
    @ApiModelProperty(value = "收货地址-省编码", required = true)
    private String provinceCode;

    /**
     * 收货地址-市
     */
    @NotBlank(message = "收货地址-市不能为空")
    @ApiModelProperty(value = "收货地址-市", required = true)
    private String cityName;

    /**
     * 收货地址-市编码
     */
    @NotBlank(message = "收货地址-市编码不能为空")
    @ApiModelProperty(value = "收货地址-市编码", required = true)
    private String cityCode;

    /**
     * 收货地址-区县
     */
    @NotBlank(message = "收货地址-区县不能为空")
    @ApiModelProperty(value = "收货地址-区县", required = true)
    private String countyName;
    /**
     * 收货地址-区县编码
     */
    @NotBlank(message = "收货地址-区县编码不能为空")
    @ApiModelProperty(value = "收货地址-区县编码", required = true)
    private String countyCode;

    /**
     * 收货地址-具体地址
     */
    @NotBlank(message = "收货地址-具体地址不能为空")
    @ApiModelProperty(value = "收货地址-具体地址", required = true)
    private String cardAddress;

}
