package com.ruoyi.common.order.bo;

import lombok.Data;

import java.util.List;

/**
 * 产品初始化请求
 */
@Data
public class ProductInitRequest {

    /**
     * 指定初始化的产品ID列表，默认全量
     */
    private List<Integer> productIdList;

    /**
     * 是否包含被禁用的代理商，默认否
     */
    private Boolean includeDisabledAgent;

    /**
     * 是否包含已下架的产品，默认否
     */
    private Boolean includeOffShelfProduct;

    /**
     * 仅演练，不执行写入
     */
    private Boolean dryRun;
}

