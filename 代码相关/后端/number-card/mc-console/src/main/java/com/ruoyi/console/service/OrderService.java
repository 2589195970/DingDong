package com.ruoyi.console.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.AgainOrderBO;
import com.ruoyi.common.order.bo.OrderSelectBO;
import com.ruoyi.common.order.bo.UpdateOrderStatusBO;
import com.ruoyi.common.order.bo.UploadOrderListExcelBO;
import com.ruoyi.common.order.entity.Order;
import com.ruoyi.common.order.entity.OrderLog;
import com.ruoyi.common.order.vo.OrderSelectVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/12 17:11
 */
public interface OrderService extends IService<Order> {

     /**
      * 分页查询订单列表
      * @param orderSelectBO
      * @return
      * @throws BizException
      */
     PageResult<OrderSelectVO> selectOrderListPage(OrderSelectBO orderSelectBO) throws BizException;

     /**
      * 订单导出
      * @param orderSelectBO
      * @return
      * @throws BizException
      */
     void exportOrderList(OrderSelectBO orderSelectBO, HttpServletResponse response) throws Exception;


     /**
      * 重推订单
      * @param againOrderBO
      * @throws BizException
      */
     void againOrderSubmit(AgainOrderBO againOrderBO) throws Exception;


     /**
      * 订单日志信息
      * @return
      * @throws BizException
      */
     List<OrderLog> selectOrderLogList(String orderId) throws BizException;


     /**
      * 订单状态变更
      * @return
      * @throws BizException
      */
     void updateOrderStatus(UpdateOrderStatusBO updateOrderStatusBO) throws Exception;


     /**
      * 导入订单数据
      * @param uploadOrderListExcelBO
      * @throws Exception
      */
     void uploadOrderListExcel(UploadOrderListExcelBO uploadOrderListExcelBO) throws Exception;


     /**
      * 订单余额查询-
      */
     String selectOrderBalance(String orderId) throws Exception;


}
