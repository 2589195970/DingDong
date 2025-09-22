package com.ruoyi.common.apis.douyin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;


/**
 * @author 陈思伟
 * @version 1.0
 * @description: 抖店商品编辑
 * @date 2025/7/20 14:47
 */
@Data
@SuperBuilder
@NoArgsConstructor
public class DouDianGoodsEditRequest extends DouDianBaseRequest {

    /**
     * 店铺 商品ID
     * 必传
     */
    private String productId;

    /**
     * false仅保存，true保存+提审
     */
    private Boolean commit = true;

    /**
     * 商品规格ID
     */
    List<Map> spec_prices_v2;
}
