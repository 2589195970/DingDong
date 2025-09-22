package com.ruoyi.order.service.order;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.entity.Order;
import com.ruoyi.common.order.entity.Product;
import com.ruoyi.common.order.entity.UpstreamInfo;
import com.ruoyi.common.order.reuqest.BaseSubmitOrderRequest;


/**
 * 订单校验相关类
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/12 17:11
 */
public interface OrderCheckService  {

    /**
     * 订单前置校验
     *
     */
    void orderBeforeCheck(BaseSubmitOrderRequest request, Product product, UpstreamInfo upstreamInfo,Order order) throws BizException;

}
