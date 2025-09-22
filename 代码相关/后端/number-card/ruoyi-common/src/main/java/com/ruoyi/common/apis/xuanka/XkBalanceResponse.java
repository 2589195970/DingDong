package com.ruoyi.common.apis.xuanka;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 炫咖余额查询返回值
 * @author 陈思伟
 */
@Data
@ApiModel("炫咖余额查询返回值")
public class XkBalanceResponse {


    /**
     * 号码
     */
    private String mobile;

    /**
     * 查询的余额
     */
    private String curFee;

    /**
     * 账户剩余余额
     */
    private String mobile_fee;

}
