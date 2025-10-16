package com.ruoyi.order.service;


import com.ruoyi.common.order.entity.Order;
import com.ruoyi.common.order.entity.Product;
import com.ruoyi.common.order.entity.UpstreamApi;
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

    /**
     * 获取产品信息
     *
     * @param productCode 产品编码
     * @return 产品信息
     * @throws Exception
     */
    Product getProduct(String productCode) throws Exception;

    /**
     * 获取上游API信息
     *
     * @param upstreamApiCode 上游API编码
     * @return 上游API信息
     * @throws Exception
     */
    UpstreamApi getUpstreamApi(String upstreamApiCode) throws Exception;

}
