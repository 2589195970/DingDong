package com.ruoyi.common.apis.wjk;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author 陈思伟
 */
@Data
@ApiModel("第三方提交订单")
public class WjkSubmitOrderResponse {

    private String orderId;
}
