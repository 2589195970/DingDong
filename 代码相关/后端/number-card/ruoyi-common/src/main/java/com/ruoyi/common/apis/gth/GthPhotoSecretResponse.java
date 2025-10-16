package com.ruoyi.common.apis.gth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 感叹号获取照片上传秘钥响应
 *
 * @author 陈思伟
 */
@Data
@ApiModel("感叹号获取照片上传秘钥响应")
public class GthPhotoSecretResponse {

    @ApiModelProperty(value = "秘钥", required = true)
    private String str_rand;
}