package com.ruoyi.common.apis.yqe;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 172参数类
 */
@Data
@ApiModel("172参数类")
public class YqeArgument {


    @ApiModelProperty(value = "secret秘钥", required = true)
    String secret;

    @ApiModelProperty(value = "代理在172号卡登录账号", required = true)
    String userId;

    /**
     * 产品ID
     */
    @ApiModelProperty(value = "产品ID，在172号卡后台商品列表第一列获取", required = true)
    private String ProductID;


}
