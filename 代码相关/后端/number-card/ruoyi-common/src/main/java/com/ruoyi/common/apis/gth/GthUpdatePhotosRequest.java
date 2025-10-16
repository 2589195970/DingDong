package com.ruoyi.common.apis.gth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 感叹号更新照片请求
 *
 * @author 陈思伟
 */
@Data
@ApiModel("感叹号更新照片请求")
public class GthUpdatePhotosRequest {

    @ApiModelProperty(value = "秘钥（接口获取）", required = true)
    private String str_rand;

    @ApiModelProperty(value = "身份证人像面url地址", required = false)
    private String face_url;

    @ApiModelProperty(value = "身份证背面url地址", required = false)
    private String back_url;

    @ApiModelProperty(value = "人像面url地址(手持身份证)", required = false)
    private String hand_url;

    @ApiModelProperty(value = "自定义照片URL地址（学生证）", required = false)
    private String custom_photos_url;
}