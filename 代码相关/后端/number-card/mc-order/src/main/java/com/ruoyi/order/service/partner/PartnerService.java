package com.ruoyi.order.service.partner;

import com.ruoyi.common.apis.partner.PartnerSubmitOrderRequest;
import com.ruoyi.common.apis.partner.PartnerSubmitOrderResponse;

/**
 * @Description
 * @Author suyang
 * @Date 2021-01-25
 */
public interface PartnerService {

    /**
     * 合作方提交订单
     * @param request
     * @return
     * @throws Exception
     */
     PartnerSubmitOrderResponse submitOrder(PartnerSubmitOrderRequest request) throws Exception;


    /**
     * 合作方提交订单
     * @param request
     * @return
     * @throws Exception
     */
     PartnerSubmitOrderResponse submitOrder(Long orderId, PartnerSubmitOrderRequest request) throws Exception;


}
