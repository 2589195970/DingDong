package com.ruoyi.order.service;


import com.ruoyi.common.order.entity.Order;
import com.ruoyi.common.order.reuqest.APISubmitInfoRequest;

/**
 * 产品API分组 service
 *
 * @Description
 */
public interface ProductApiService {


    /**
     * 根据 API类型 提交订单
     *
     * @return
     */
     Order submitAPISubmitOrder(APISubmitInfoRequest apiSubmitInfoRequest) throws Exception;


    /**
     * 根据 API类型 提交订单
     *
     * @return
     */
     Order submitAPISubmitOrder(Long orderId, APISubmitInfoRequest apiSubmitInfoRequest) throws Exception;

}
