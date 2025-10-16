package com.ruoyi.common.apis.wjk;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 挖金客更新照片请求
 *
 * @author 陈思伟
 */
@Data
@ApiModel("挖金客更新照片请求")
public class WjkUpdatePhotosRequest extends WjkBaseRequest {

    @ApiModelProperty(value = "订单号", required = true)
    private String orderNo;

    @ApiModelProperty(value = "身份证正面url地址", required = false)
    private String idCardFrontUrl;

    @ApiModelProperty(value = "身份证背面url地址", required = false)
    private String idCardBackUrl;

    @ApiModelProperty(value = "手持身份证照片url地址", required = false)
    private String personPhotoUrl;

    @ApiModelProperty(value = "自定义照片URL地址", required = false)
    private String customPhotoUrl;
}