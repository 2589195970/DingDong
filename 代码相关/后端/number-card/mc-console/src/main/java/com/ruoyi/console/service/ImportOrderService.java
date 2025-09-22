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
import com.ruoyi.common.order.reuqest.OrderSubmitRequest;
import com.ruoyi.common.order.vo.OrderSelectVO;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/12 17:11
 */
public interface ImportOrderService  {


     /**
      * 导入订单
      * @return
      * @throws BizException
      */
     void importOrderListSubmit(List<OrderSubmitRequest> orderSubmitRequestList);

}
