package com.ruoyi.common.order.reuqest;

/**
 * 商品上下架回调请求
 */
public interface ProductStatusNotifyRequest {

    /**
     * 获取商品上下架状态
     *
     * @return 商品状态
     */
    String getCallbackProductStatus();

    /**
     * 获取商品编码
     *
     * @return 商品编码
     */
    String getCallbackProductCode();
}
