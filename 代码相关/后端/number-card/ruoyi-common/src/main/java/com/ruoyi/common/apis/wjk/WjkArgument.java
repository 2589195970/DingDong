package com.ruoyi.common.apis.wjk;

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
@ApiModel("挖金客参数类")
public class WjkArgument {


    @ApiModelProperty(value = "合作方Id", required = true)
    Integer partnerId;

    @ApiModelProperty(value = "产品Id", required = true)
    Integer productId;

    /**
     * 秘钥
     */
    @ApiModelProperty(value = "秘钥", required = true)
    private String appKey;
}
