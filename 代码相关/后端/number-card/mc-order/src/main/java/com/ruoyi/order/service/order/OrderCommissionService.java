package com.ruoyi.order.service.order;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.order.entity.Order;
import com.ruoyi.common.order.entity.OrderCommission;


/**
 * 订单状态处理
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/12 17:11
 */
public interface OrderCommissionService extends IService<OrderCommission> {

    /**
     * 生成订单佣金结算记录
     */
     void saveOrderCommission(Order order);


    /**
     * 更新订单佣金充值信息
     */
    void updateOrderCommissionRecharged(Order order);
}
