package com.ruoyi.console.service;

import com.ruoyi.common.order.entity.Order;
import com.ruoyi.common.order.entity.Product;
import com.ruoyi.common.order.entity.UpstreamApi;

/**
 * 订单处理流程基类
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/13 17:11
 */
public interface BaseService {

    /**
     * 更新订单照片信息
     *
     * @param order 订单信息
     * @param product 产品信息
     * @param upstreamApi 上游API信息
     * @throws Exception
     */
    void updateOrderPhotos(Order order, Product product, UpstreamApi upstreamApi) throws Exception;

}