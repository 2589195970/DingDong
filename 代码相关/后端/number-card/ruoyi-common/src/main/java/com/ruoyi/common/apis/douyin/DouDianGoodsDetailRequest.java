package com.ruoyi.common.apis.douyin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

/**
 * @author 陈思伟
 * @version 1.0
 * @description: 抖店商品详情请求类
 * @date 2027/7/18 14:47
 */
@Data
@SuperBuilder
@NoArgsConstructor
public class DouDianGoodsDetailRequest extends DouDianBaseRequest {

    /**
     * 店铺 商品ID
     * 必传
     */
    @NotNull(message ="店铺商品ID" )
    private String productId;
}
