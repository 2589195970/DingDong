package com.ruoyi.common.apis.partner.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 陈思伟
 */
@Data
@ApiModel("合作方请求基础数据")
public class PartnerBaseRequest {

    @NotNull(message = "序列号不能为空")
    @Length(max = 20, message = "序列号长度不超过20位")
    @ApiModelProperty(value = "请求序列号", required = true)
    String serial;

    @NotNull(message = "代理商编码不能为空")
    @ApiModelProperty(value = "代理商编码", required = true)
    String partnerCode;

    @NotNull(message = "产品Id不能为空")
    @ApiModelProperty(value = "产品编码", required = true)
    String productCode;

    @NotBlank(message = "签名不能为空")
    @Length(max = 64, message = "签名长度不超过64位")
    @ApiModelProperty(value = "签名", required = true)
    String sign;

    @NotNull(message = "时间戳不能为空")
    @ApiModelProperty(value = "时间戳", required = true)
    Long timestamp;
}
