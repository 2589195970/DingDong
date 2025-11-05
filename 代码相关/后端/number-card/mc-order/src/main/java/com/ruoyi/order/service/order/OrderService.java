package com.ruoyi.order.service.order;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.OrderListBO;
import com.ruoyi.common.order.entity.Order;
import com.ruoyi.common.order.reuqest.OrderSubmitRequest;
import com.ruoyi.common.order.reuqest.OrderSubmitResponse;
import com.ruoyi.common.order.vo.OrderListVO;
import com.ruoyi.order.bo.OrderUpdateBO;

import java.util.List;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/12 17:11
 */
public interface OrderService extends IService<Order> {

     /**
      * H5页面下单接口
      * @param orderSubmitRequest
      * @return
      * @throws BizException
      */
     OrderSubmitResponse submitOrder(OrderSubmitRequest orderSubmitRequest) throws Exception;


     /**
      * 订单列表查询
      * @return
      * @throws BizException
      */
     List<OrderListVO> selectOrderList(OrderListBO orderListBO) throws Exception;

     /**
      * 查询订单信息（用于照片修改）
      * @param orderId 订单ID
      * @return 订单信息
      * @throws BizException 业务异常
      */
     Order selectOrderById(Long orderId) throws BizException;

     /**
      * 更新订单信息（照片修改专用）
      * @param updateBO 订单更新业务对象
      * @throws BizException 业务异常
      */
     void updateOrderInfo(OrderUpdateBO updateBO) throws BizException;

}
