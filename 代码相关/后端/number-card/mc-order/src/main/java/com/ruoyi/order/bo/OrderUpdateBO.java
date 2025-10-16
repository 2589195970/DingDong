package com.ruoyi.order.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 订单更新业务对象（照片修改专用）
 *
 * @author Claude
 * @date 2025/01/16
 */
@ApiModel("订单更新业务对象")
@Data
public class OrderUpdateBO {

    @ApiModelProperty("订单ID")
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @ApiModelProperty("详细地址")
    private String cardAddress;

    @ApiModelProperty("省编码")
    private String provinceCode;

    @ApiModelProperty("省名称")
    private String provinceName;

    @ApiModelProperty("市编码")
    private String cityCode;

    @ApiModelProperty("市名称")
    private String cityName;

    @ApiModelProperty("区编码")
    private String countyCode;

    @ApiModelProperty("区名称")
    private String countyName;

    @ApiModelProperty("身份证正面照片URL")
    private String idCardFrontUrl;

    @ApiModelProperty("身份证反面照片URL")
    private String idCardBackUrl;

    @ApiModelProperty("免冠照片URL")
    private String personPhotoUrl;

    @ApiModelProperty("自定义照片URL")
    private String customPhotoUrl;
}