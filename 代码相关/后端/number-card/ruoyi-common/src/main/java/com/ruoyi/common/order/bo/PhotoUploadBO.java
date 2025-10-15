package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 照片上传业务对象
 *
 * @author 陈思伟
 * @date 2024/12/12
 */
@ApiModel("照片上传业务对象")
@Data
public class PhotoUploadBO {

    @ApiModelProperty("订单ID")
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @ApiModelProperty("身份证正面照片URL")
    private String idCardFrontUrl;

    @ApiModelProperty("身份证反面照片URL")
    private String idCardBackUrl;

    @ApiModelProperty("免冠照片URL")
    private String personPhotoUrl;

    @ApiModelProperty("自定义照片URL")
    private String customPhotoUrl;

    @ApiModelProperty("上传备注")
    private String remark;
}