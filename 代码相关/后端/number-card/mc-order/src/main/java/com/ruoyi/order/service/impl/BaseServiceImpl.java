package com.ruoyi.order.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.order.reuqest.ApiCommonNotifyRequest;
import com.ruoyi.order.service.*;
import com.ruoyi.order.service.callback.DownstreamCallbackService;
import com.ruoyi.order.service.order.OrderCheckService;
import com.ruoyi.order.service.order.OrderService;
import com.ruoyi.order.service.order.OrderStatusService;
import com.ruoyi.order.utils.SnowflakeUtil;
import com.ruoyi.common.enums.OrderEnum;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.AgentProductBO;
import com.ruoyi.common.order.entity.*;
import com.ruoyi.common.order.response.GetNumberListResponse;
import com.ruoyi.common.order.reuqest.BaseNotifyRequest;
import com.ruoyi.common.order.reuqest.BaseSubmitOrderRequest;
import com.ruoyi.common.order.reuqest.GetNumberListRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/13 17:11
 */
@Slf4j
@Service
public abstract class BaseServiceImpl implements BaseService {
    private static final String TAG = "BaseServiceImpl";

    @Resource
    public SnowflakeUtil snowflakeUtil;

    @Resource
    public OrderService orderService;

    @Resource
    public OrderCheckService orderCheckService;

    @Resource
    public ProductService productService;

    @Resource
    public UpstreamApiService upstreamApiService;

    @Resource
    public AgentService agentService;

    @Resource
    public OrderLogService orderLogService;

    @Resource
    public OrderStatusService orderStatusService;

    @Resource
    DownstreamCallbackService downstreamCallbackService;


    /**
     * 运营商名称
     *
     * @return
     */
    protected abstract String getServiceName();

    /**
     * api取号
     *
     * @param request
     * @param product
     * @return
     */
    protected abstract List<NumberDTO> apiGetNumberList(GetNumberListRequest request, Product product, Map<String, Object> params) throws Exception;



    /**
     * 获取号码信息
     *
     * @param request
     * @param params
     * @return
     * @throws Exception
     */
    @Override
    public GetNumberListResponse listNumber(GetNumberListRequest request, Map<String, Object> params) throws Exception {
        return null;
    }

    /**
     * 创建订单
     *
     * @return
     * @throws Exception
     */
    public abstract Order createOrder(Long orderId, BaseSubmitOrderRequest request,Product product, UpstreamApi upstreamApi) throws Exception;

    /**
     * 同步提交流程
     * 仅同步提交流程需要继承
     *
     * @param request
     * @param order
     * @return
     * @throws Exception
     */
    public Order syncSubmitOrderFlow(BaseSubmitOrderRequest request, Order order, Product product, UpstreamInfo upstreamInfo) throws Exception {
        log.info("订单{}暂不需要处理,暂时入库",order.getOrderId());
        return order;
    }


    /**
     * 同步提交订单 使用随机生成的ID
     *
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public Order syncSubmitOrder(BaseSubmitOrderRequest request, Product product, UpstreamApi upstreamApi) throws Exception {
        return syncSubmitOrder(snowflakeUtil.getNextId(), request,product,upstreamApi);
    }


    /**
     * 订单预处理
     *
     * @param product
     * @return
     * @throws Exception
     */
    protected Order beforeSubmit(Long orderId, BaseSubmitOrderRequest request, Product product, UpstreamApi upstreamApi) throws Exception {
        log.info("订单提交预处理:{},订单号:{},产品CODE:{},上游APICODE:{}", JSONObject.toJSONString(request),orderId,product.getProductCode(),upstreamApi.getUpstreamApiCode());
        Order order = createOrder(orderId, request, product,upstreamApi);
        orderService.save(order);
        return order;
    }

    /**
     * 同步提交订单
     *
     * @param orderId
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public Order syncSubmitOrder(Long orderId, BaseSubmitOrderRequest request, Product product, UpstreamApi upstreamApi) throws Exception {
        //订单前置处理 保存请求相关信息
        Order order = beforeSubmit(orderId,request,product,upstreamApi);
        AgentProductBO agentProductBO;
        UpstreamInfo upstreamInfo;
        try {
            //获取上游产品信息 后续分单可以在此操作
             agentProductBO = productService.getAgentProductBO(product.getProductCode(),request.getAgentCode());
             upstreamInfo =upstreamApiService.getUpstreamInfo(upstreamApi.getUpstreamApiCode(),agentProductBO.getUpstreamProductCode());
            //计算订单佣金
            middleSubmit(order,agentProductBO,upstreamInfo);
            //订单前置拦截校验
            orderCheckService.orderBeforeCheck(request,product,upstreamInfo,order);
            //订单入库
            order = syncSubmitOrderFlow(request, order, product,upstreamInfo);
        }catch (Exception e){ ;
            //更新错误信息 并进行外部提示
            orderErrorSave(order,product,upstreamApi,request,e.getMessage());
            throw new BizException("提单异常:{}",e.getMessage());
        }
        //订单后处理
        afterSubmit(order,agentProductBO,upstreamInfo);
        return order;
    }


    /**
     * 订单中处理
     * 订单提交第三方前 保存相关信息 计算佣金信息等
     *
     * @param order
     * @return
     * @throws Exception
     */
    protected void middleSubmit(Order order,AgentProductBO agentProductBO, UpstreamInfo upstreamInfo) throws Exception {
        log.info("提交中处理,订单号:{},提交上游API:{},上游产品:{}",order.getOrderId(),upstreamInfo.getUpstreamApi().getUpstreamApiName(),upstreamInfo.getUpstreamProduct().getUpstreamProductName());
        //上游API和产品信息
        order.setProductCode(agentProductBO.getProductCode());
        order.setProductName(agentProductBO.getProductName());
        order.setProductType(agentProductBO.getProductType()+"");
        order.setUpstreamApi(upstreamInfo.getUpstreamApi().getUpstreamApiCode());
        order.setUpstreamApiName(upstreamInfo.getUpstreamApi().getUpstreamApiName());
        order.setUpstreamProductCode(upstreamInfo.getUpstreamProduct().getUpstreamProductCode());
        order.setUpstreamProductName(upstreamInfo.getUpstreamProduct().getUpstreamProductName());
        //下游信息
        order.setDownstreamCode(agentProductBO.getAgentCode());
        order.setDownstreamName(agentProductBO.getAgentName());
        order.setDownstreamFatherList(JSONObject.toJSONString(agentService.getAgentFatherList(agentProductBO.getAgentCode(),agentProductBO.getProductCode())));
        //运营商信息
        order.setOperatorType(agentProductBO.getOperatorType());
        orderService.updateById(order);

    }


    /**
     * 订单后处理
     *
     * @param order
     * @return
     * @throws Exception
     */
    protected Order afterSubmit(Order order,AgentProductBO agentProductBO, UpstreamInfo upstreamInfo) throws Exception {
        log.info("提交后处理:{},订单号:{}", JSONObject.toJSONString(order),order.getOrderId());
        //此处留个口子 通过API接入的 触发一次回调后置处理 通知合作方
        afterCallback(order, null);
        return order;
    }


    /**
     * 订单失败处理流程
     * 后续可扩展其他失败订单利用流程
     * @param product
     * @param order
     * @return
     * @throws Exception
     */
    protected void orderErrorSave(Order order, Product product,UpstreamApi upstreamApi,BaseSubmitOrderRequest request, String errorMessage) {
        try {
            order.setOrderStatus(OrderEnum.INVALID.getStatus());
            order.setOrderMessage(errorMessage);
            orderService.updateById(order);
            log.info("{}订单失败处理流程结束", order.getOrderId());
        } catch (Exception e) {
            log.info("订单失败异常处理流程执行失败:{},订单ID:{}", e.getMessage(), order.getOrderId());
        }
    }

    /**
     * 回调处理流程
     * @param notifyRequest
     * @throws Exception
     */
    @Override
    public void callback(BaseNotifyRequest notifyRequest) throws Exception {
        ApiCommonNotifyRequest request = (ApiCommonNotifyRequest) notifyRequest;
        log.info("公共回调信息处理:{}", com.alibaba.fastjson.JSONObject.toJSONString(request));
        Order order = orderService.getById(request.getOrderId());
        if (order == null) {
            throw new BizException("回调订单号不存在，orderId：" + request.getOrderId());
        }
        //无效或作废订单
        if (OrderEnum.INVALID.getStatus().equals(Integer.valueOf(request.getOrderStatus()))){
            String message = request.getMessage();
            if (!StringUtils.hasLength(message)) {
                message = request.getOrderStatus();
            }
            orderStatusService.saveOrderFail(order, message);
        }
        //保存物流单号和快递
        if (!StrUtil.isBlankIfStr(request.getTrackingNumber()) && !StrUtil.isBlankIfStr(request.getLogisticsName())) {
            orderStatusService.saveDelivery(order, request.getLogisticsName(), request.getTrackingNumber());
        }
        //保存预占号码
        if (!StrUtil.isBlankIfStr(request.getAccNumber())) {
            orderStatusService.saveAccNumber(order, request.getAccNumber());
        }
        //保存激活订单
        if (OrderEnum.ACTIVATED.getStatus().equals(Integer.valueOf(request.getOrderStatus()))){
            orderStatusService.saveActive(order,request.getActiveTime());
        }
        //充值信息
        if (StringUtils.hasLength(request.getRechargeAmount())&& BaseConstant.ONE_STRING.equals(request.getIsRecharged())){
            orderStatusService.saveRecharge(order,request.getRechargeAmount(),System.currentTimeMillis());
        }
        //回调后置处理
        afterCallback(order,null);
    }


    /**
     * 回调后处理
     * @param order
     */
    public void afterCallback(Order order, Long delayTime) {
        try {
            //发送下游回调
            downstreamCallbackService.pushCallbackInfo(order,delayTime);
        } catch (Exception e) {
            log.info("回调后处理同步失败:{}", e.getMessage());
        }
    }
}
