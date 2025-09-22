package com.ruoyi.order.service.impl.order;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.enums.OrderEnum;
import com.ruoyi.common.order.bo.OrderListBO;
import com.ruoyi.common.order.vo.OrderListVO;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.order.mapper.OrderMapper;
import com.ruoyi.order.service.ProductApiService;
import com.ruoyi.order.service.order.OrderService;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.entity.Order;
import com.ruoyi.common.order.reuqest.APISubmitInfoRequest;
import com.ruoyi.common.order.reuqest.OrderSubmitRequest;
import com.ruoyi.common.order.reuqest.OrderSubmitResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/12 17:13
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Resource
    ProductApiService productApiService;

    /**
     * H5页面下单接口
     * @param orderSubmitRequest
     * @return
     * @throws BizException
     */
    @Override
    public OrderSubmitResponse submitOrder(OrderSubmitRequest orderSubmitRequest) throws Exception {
        APISubmitInfoRequest apiSubmitInfoRequest = new APISubmitInfoRequest();
        BeanUtil.copyProperties(orderSubmitRequest,apiSubmitInfoRequest);
        apiSubmitInfoRequest.setReceiverName(orderSubmitRequest.getCardName());
        apiSubmitInfoRequest.setOrderSource(orderSubmitRequest.getOrderSource()!=null?orderSubmitRequest.getOrderSource():OrderEnum.OrderSourceEnum.XIN_XI_LIU.getSourceType());
        Order order = productApiService.submitAPISubmitOrder(apiSubmitInfoRequest);
        //隔一层 避免后续有些参数需要返给前端
        OrderSubmitResponse orderSubmitResponse = new OrderSubmitResponse();
        orderSubmitResponse.setOrderId(order.getOrderId());
        return orderSubmitResponse;
    }

    /**
     * 订单列表查询
     * @return
     * @throws BizException
     */
    @Override
    public List<OrderListVO> selectOrderList(OrderListBO orderListBO) throws Exception {
        if(orderListBO==null|| StringUtils.isEmpty(orderListBO.getCardId())|| StringUtils.isEmpty(orderListBO.getCardPhone())){
            throw new BizException("参数不能为空");
        }
        List<Order> orderList = baseMapper.selectList(new LambdaQueryWrapper<Order>().eq(Order::getCardId,orderListBO.getCardId()).eq(Order::getCardPhone,orderListBO.getCardPhone()));
        if(CollectionUtils.isEmpty(orderList)){
            return new ArrayList<>();
        }
        List<OrderListVO> orderListVOS = new ArrayList<>();
        for (Order order:orderList){
            OrderListVO orderListVO = new OrderListVO();
            BeanUtil.copyProperties(order,orderListVO);
            orderListVOS.add(orderListVO);
        }
        return orderListVOS;
    }

}
