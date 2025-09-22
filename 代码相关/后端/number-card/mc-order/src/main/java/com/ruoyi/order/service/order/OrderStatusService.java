package com.ruoyi.order.service.order;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.order.entity.Order;


/**
 * 订单状态处理
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/12 17:11
 */
public interface OrderStatusService extends IService<Order> {

    /**
     * 记录订单失败
     *
     * @param order
     * @param desc
     */
     void saveOrderFail(Order order, String desc);


    /**
     * 记录发货
     *
     * @param order
     * @param logistics      快递公司
     * @param trackingNumber 物流单号
     */
    void saveDelivery(Order order, String logistics, String trackingNumber);


    /**
     * 记录预占号码
     *
     * @param order
     * @param number
     */
     void saveAccNumber(Order order, String number);


    /**
     * 记录激活
     *
     * @param order
     */
     void saveActive(Order order,Long activeTime);


    /**
     * 记录充值
     *
     * @param order
     * @param rechargeAmount
     * @param rechargeTime 充值时间
     */
     void saveRecharge(Order order, String rechargeAmount, Long rechargeTime);
}
