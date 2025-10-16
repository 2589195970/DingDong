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
import com.ruoyi.order.service.BaseService;
import com.ruoyi.common.order.entity.Product;
import com.ruoyi.common.order.entity.UpstreamApi;
import com.ruoyi.order.utils.SpringContextUtil;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.entity.Order;
import com.ruoyi.common.order.reuqest.APISubmitInfoRequest;
import com.ruoyi.common.order.reuqest.OrderSubmitRequest;
import com.ruoyi.common.order.reuqest.OrderSubmitResponse;
import com.ruoyi.order.bo.OrderUpdateBO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/12 17:13
 */
@Service
@Slf4j
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

    /**
     * 查询订单信息（用于照片修改）
     *
     * @param orderId 订单ID
     * @return 订单信息
     * @throws BizException 业务异常
     */
    @Override
    public Order selectOrderById(Long orderId) throws BizException {
        if (orderId == null) {
            throw new BizException("订单ID不能为空");
        }

        Order order = baseMapper.selectById(orderId);
        if (order == null) {
            throw new BizException("订单不存在");
        }

        log.info("查询订单信息成功，订单ID: {}", orderId);
        return order;
    }

    /**
     * 更新订单信息（照片修改专用）
     *
     * @param updateBO 订单更新业务对象
     * @throws BizException 业务异常
     */
    @Override
    public void updateOrderInfo(OrderUpdateBO updateBO) throws BizException {
        if (updateBO == null || updateBO.getOrderId() == null) {
            throw new BizException("订单ID不能为空");
        }

        Order order = baseMapper.selectById(updateBO.getOrderId());
        if (order == null) {
            throw new BizException("订单不存在");
        }

        // 更新照片信息
        if (StringUtils.isNotBlank(updateBO.getIdCardFrontUrl())) {
            order.setIdCardFrontUrl(updateBO.getIdCardFrontUrl());
        }
        if (StringUtils.isNotBlank(updateBO.getIdCardBackUrl())) {
            order.setIdCardBackUrl(updateBO.getIdCardBackUrl());
        }
        if (StringUtils.isNotBlank(updateBO.getPersonPhotoUrl())) {
            order.setPersonPhotoUrl(updateBO.getPersonPhotoUrl());
        }
        if (StringUtils.isNotBlank(updateBO.getCustomPhotoUrl())) {
            order.setCustomPhotoUrl(updateBO.getCustomPhotoUrl());
        }
        order.setUpdateTime(System.currentTimeMillis());

        // 更新数据库
        baseMapper.updateById(order);
        log.info("更新订单信息成功，订单ID: {}", updateBO.getOrderId());

        // 调用上游API更新照片信息
        try {
            updateOrderPhotosToUpstream(order);
        } catch (Exception e) {
            log.error("调用上游API更新照片失败，订单ID: {}, 错误: {}", updateBO.getOrderId(), e.getMessage(), e);
            // 不抛出异常，避免影响本地数据库更新
        }
    }

    /**
     * 调用上游API更新照片信息
     *
     * @param order 订单信息
     * @throws Exception
     */
    private void updateOrderPhotosToUpstream(Order order) throws Exception {
        // 获取产品信息
        Product product = productApiService.getProduct(order.getProductCode());
        if (product == null) {
            log.warn("订单{} 对应的产品不存在，跳过上游更新", order.getOrderId());
            return;
        }

        // 获取上游API信息
        UpstreamApi upstreamApi = productApiService.getUpstreamApi(product.getUpstreamApiCode());
        if (upstreamApi == null) {
            log.warn("订单{} 对应的上游API不存在，跳过上游更新", order.getOrderId());
            return;
        }

        // 获取具体的BaseService实现
        BaseService baseService = SpringContextUtil.getBean(upstreamApi.getInterfaceClassName());
        if (baseService != null) {
            baseService.updateOrderPhotos(order, product, upstreamApi);
        } else {
            log.warn("订单{} 对应的上游API实现类不存在，跳过上游更新", order.getOrderId());
        }
    }

}
