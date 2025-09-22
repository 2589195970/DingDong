package com.ruoyi.common.apis.gth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 感叹号客参数类
 */
@Data
@ApiModel("感叹号客参数类")
public class GthArgument {


    @ApiModelProperty(value = "发展人ID", required = true)
    String shareId;

    @ApiModelProperty(value = "签名参数", required = true)
    String apiToken;

    /**
     * Aes 加密密钥
     */
    @ApiModelProperty(value = "Aes 加密密钥", required = true)
    private String aesKey;


    @ApiModelProperty(value = "商品名称", required = true)
    String sku;

    /**
     *
     */
    @ApiModelProperty(value = "商品编码", required = true)
    String sourceSku;

}
