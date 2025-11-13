package com.ruoyi.order.service;

import com.ruoyi.common.order.entity.Order;
import com.ruoyi.common.order.entity.Product;
import com.ruoyi.common.order.entity.UpstreamApi;
import com.ruoyi.common.order.entity.UpstreamInfo;
import com.ruoyi.common.order.response.GetNumberListResponse;
import com.ruoyi.common.order.reuqest.BaseNotifyRequest;
import com.ruoyi.common.order.reuqest.BaseSubmitOrderRequest;
import com.ruoyi.common.order.reuqest.GetNumberListRequest;
import com.ruoyi.common.order.reuqest.SendSmsCodeRequest;

import java.util.Map;

/**
 * 订单处理流程基类
 * 
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/13 17:11
 */
public interface BaseService {

    /**
     * 发送验证码短信
     *
     * @param request
     * @throws Exception
     */
    void sendVerificationCode(SendSmsCodeRequest request, Map<String, Object> params) throws Exception;

    /**
     * 获取号码
     *
     * @param request
     * @param params
     * @return
     * @throws Exception
     */
    GetNumberListResponse listNumber(GetNumberListRequest request, Map<String, Object> params) throws Exception;

    /**
     * 同步提交订单
     *
     * @param request
     * @return
     * @throws Exception
     */
    Order syncSubmitOrder(Long orderId, BaseSubmitOrderRequest request, Product product, UpstreamApi upstreamApi)
            throws Exception;

    /**
     * 审核通过后重新推送待审核订单
     *
     * @param orderId     订单ID
     * @param request     原始下单请求
     * @param product     产品信息
     * @param upstreamApi 上游API信息
     * @return 订单
     * @throws Exception 业务异常
     */
    Order resumePendingOrder(Long orderId, BaseSubmitOrderRequest request, Product product, UpstreamApi upstreamApi)
            throws Exception;

    /**
     * 同步提交订单
     *
     * @param request
     * @return
     * @throws Exception
     */
    Order syncSubmitOrder(BaseSubmitOrderRequest request, Product product, UpstreamApi upstreamApi) throws Exception;

    /**
     * 回调处理
     *
     * @param notifyRequest
     * @throws Exception
     */
    void callback(BaseNotifyRequest notifyRequest) throws Exception;

    /**
     * 更新订单照片信息
     *
     * @param order       订单信息
     * @param product     产品信息
     * @param upstreamApi 上游API信息
     * @throws Exception
     */
    void updateOrderPhotos(Order order, Product product, UpstreamApi upstreamApi) throws Exception;

    /**
     * 商品上下架回调
     *
     * @param request
     * @throws Exception
     */
    void updateProductStatus(BaseNotifyRequest request) throws Exception;

}
