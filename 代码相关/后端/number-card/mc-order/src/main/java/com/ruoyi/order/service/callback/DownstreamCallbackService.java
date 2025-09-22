package com.ruoyi.order.service.callback;


import com.ruoyi.common.order.entity.Order;

/**
 * 实时回调处理类
 *
 * @Description
 * @Author 陈思伟
 * @Date 2023-01-25
 */
public interface DownstreamCallbackService {


    void pushCallbackInfo(Order order, Long delayTime);


}
