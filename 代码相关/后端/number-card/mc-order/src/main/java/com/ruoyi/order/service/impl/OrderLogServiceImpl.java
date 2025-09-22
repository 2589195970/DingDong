package com.ruoyi.order.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.order.entity.OrderLog;
import com.ruoyi.order.mapper.OrderLogMapper;
import com.ruoyi.order.service.OrderLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;



/**
 * 订单日志表
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/19 17:13
 */
@Service
@Slf4j
public class OrderLogServiceImpl extends ServiceImpl<OrderLogMapper, OrderLog> implements OrderLogService {


    /**
     * 创建日志
     * @param orderLog
     */
    public void saveOrderLog(OrderLog orderLog){
        try {
            orderLog.setCreateTime(System.currentTimeMillis());
            baseMapper.insert(orderLog);
        }catch (Exception e){
            log.info("提单日志保存异常:{}",e.getMessage());
        }

    }


    /**
     * 创建日志类
     */
    public OrderLog getOrderLog(String orderId,String requestUrl,String requestBody,String requestMsg,String productCode){
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(orderId);
        orderLog.setRequestUrl(requestUrl);
        orderLog.setRequestBody(requestBody);
        orderLog.setRequestMsg(requestMsg);
        orderLog.setProductCode(productCode);
        return orderLog;
    }

}
