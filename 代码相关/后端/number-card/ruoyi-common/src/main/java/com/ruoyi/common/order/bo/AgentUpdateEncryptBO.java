package com.ruoyi.common.order.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 代理商更新订单加密状态
 */
@Data
public class AgentUpdateEncryptBO extends BaseBO{

    /**
     * 代理商账号ID
     */
    @ApiModelProperty("代理商账号ID")
    private Integer agentAccountId;

    /**
     * 订单加密状态 0 订单加密 1订单解密
     */
    @ApiModelProperty("0 订单加密 1订单解密")
    private Integer isEncrypt;



}
