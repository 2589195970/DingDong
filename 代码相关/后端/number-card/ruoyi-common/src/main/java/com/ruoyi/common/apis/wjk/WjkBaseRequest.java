package com.ruoyi.common.apis.wjk;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 挖金客请求base类
 * @author 陈思伟
 */
@Data
@ApiModel("合作方请求基础数据")
public class WjkBaseRequest {

    @ApiModelProperty(value = "请求序列号", required = true)
    String serial;

    @ApiModelProperty(value = "合作方Id", required = true)
    Integer partnerId;

    @ApiModelProperty(value = "产品Id", required = true)
    Integer productId;

    @NotBlank(message = "签名不能为空")
    @Length(max = 64, message = "签名长度不超过64位")
    @ApiModelProperty(value = "签名", required = true)
    String sign;

    @NotNull(message = "时间戳不能为空")
    @ApiModelProperty(value = "时间戳", required = true)
    Long timestamp;
}
