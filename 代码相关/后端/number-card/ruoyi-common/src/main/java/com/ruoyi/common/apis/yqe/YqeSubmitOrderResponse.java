package com.ruoyi.common.apis.yqe;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 *
 * @author 陈思伟
 */
@Data
@ApiModel("172提交订单返回值")
public class YqeSubmitOrderResponse {


    String code;

    /**
     * code 状态为0 是订单号 非0 错误信息
     */
    String message;

}
