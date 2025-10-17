package com.ruoyi.console.service.impl;

import com.ruoyi.common.order.entity.Order;
import com.ruoyi.common.order.entity.Product;
import com.ruoyi.common.order.entity.UpstreamApi;
import com.ruoyi.console.service.BaseService;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/13 17:11
 */
@Slf4j
public abstract class BaseServiceImpl implements BaseService {

    /**
     * 运营商名称
     *
     * @return
     */
    protected abstract String getServiceName();

    /**
     * 更新订单照片信息 - 默认实现
     * 子类可以重写此方法实现特定的照片更新逻辑
     *
     * @param order 订单信息
     * @param product 产品信息
     * @param upstreamApi 上游API信息
     * @throws Exception
     */
    @Override
    public void updateOrderPhotos(Order order, Product product, UpstreamApi upstreamApi) throws Exception {
        log.info("{} 订单{} 暂不需要向上游更新照片信息", getServiceName(), order.getOrderId());
        // 默认不做任何操作，由具体子类实现
    }
}