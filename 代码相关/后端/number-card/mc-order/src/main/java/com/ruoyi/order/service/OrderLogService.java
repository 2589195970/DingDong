package com.ruoyi.order.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.order.entity.AgentCommissionJson;
import com.ruoyi.common.order.entity.OrderLog;

import java.util.List;


/**
 * 订单日志表
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/22 17:11
 */
public interface OrderLogService extends IService<OrderLog> {

    /**
     * 创建日志类
     */
     OrderLog getOrderLog(String orderId,String requestUrl,String requestBody,String requestMsg,String productCode);


    /**
     * 创建日志
     * @param orderLog
     */
    void saveOrderLog(OrderLog orderLog);

}
