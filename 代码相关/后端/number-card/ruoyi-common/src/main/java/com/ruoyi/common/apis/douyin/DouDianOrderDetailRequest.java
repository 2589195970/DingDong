package com.ruoyi.common.apis.douyin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

/**
 * @author 陈思伟
 * @version 1.0
 * @description: 抖店订单详情请求类
 * @date 2025/7/20 14:47
 */
@Data
@SuperBuilder
@NoArgsConstructor
public class DouDianOrderDetailRequest extends DouDianBaseRequest {

    /**
     * 店铺订单号
     * 必传
     */
    @NotNull(message ="订单号不能为空" )
    private String shopOrderId;
}
