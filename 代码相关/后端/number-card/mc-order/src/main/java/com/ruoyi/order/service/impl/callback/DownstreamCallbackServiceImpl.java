package com.ruoyi.order.service.impl.callback;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.apis.partner.SignUtils;
import com.ruoyi.common.enums.OrderCallbackEnum;
import com.ruoyi.common.enums.OrderEnum;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.http.HttpClient;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.order.entity.AgentCallbackRequest;
import com.ruoyi.common.order.entity.CallbackLog;
import com.ruoyi.common.order.entity.Order;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.order.mapper.CallbackLogMapper;
import com.ruoyi.order.service.AgentService;
import com.ruoyi.order.service.callback.DownstreamCallbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;


/**
 * 下游回调 处理
 *
 * @Description
 * @Author 陈思伟
 * @Date 2023/10/16 12:10
 */
@Slf4j
@Service
public class DownstreamCallbackServiceImpl implements DownstreamCallbackService {

    private static final String TAG = "CallbackServiceImpl";

    @Resource
    AgentService agentService;

    @Resource
    HttpClient httpClient;

    @Resource
    CallbackLogMapper callbackLogMapper;

    /**
     * 发送回调 给合作方
     *
     * @param order
     */
    @Async("orderDownstream")
    @Override
    public void pushCallbackInfo(Order order, Long delayTime) {
        CallbackLog callbackLog = new CallbackLog();
        callbackLog.setCreateTime(System.currentTimeMillis());
        try {
            AgentAccount agentAccount =  agentService.getAgentAccountByCode(order.getDownstreamCode());
            if(agentAccount==null|| StringUtils.isEmpty(agentAccount.getCallbackUrl())){
                throw new BizException("代理商{}未配置回调地址,订单ID:{}",order.getDownstreamName(),order.getOrderId());
            }
            if (OrderEnum.CREATE.getStatus().equals(order.getOrderStatus())) {
                log.info("订单创建中状态不处理,订单号{},{}", order.getOrderId(), order.getOrderStatus());
                return;
            }
            AgentCallbackRequest agentCallbackRequest = new AgentCallbackRequest();
            agentCallbackRequest.setPartnerCode(agentAccount.getAgentCode());
            agentCallbackRequest.setOrderId(String.valueOf(order.getOrderId()));
            //提供合作方单号
            agentCallbackRequest.setOrderNo(order.getDownstreamCode());
            agentCallbackRequest.setNumber(order.getAccNumber());
            agentCallbackRequest.setTimestamp(System.currentTimeMillis());
            //正常展示订单相关信息
            agentCallbackRequest.setStatus(OrderCallbackEnum.getOrderCallbackStatusByStatus(order.getOrderStatus()));
            agentCallbackRequest.setMessage(order.getOrderMessage());
            agentCallbackRequest.setExpress(order.getExpress());
            agentCallbackRequest.setTrackingNum(order.getTrackingNumber());
            agentCallbackRequest.setIsRecharged(order.getIsRecharged());
            agentCallbackRequest.setRechargeAmount(order.getRechargeAmount());
            agentCallbackRequest.setDeliveryTime(order.getDeliveryTime());
            agentCallbackRequest.setActiveTime(order.getActiveTime());
            agentCallbackRequest.setRechargeTime(order.getRechargeTime());
            //生成签名
            agentCallbackRequest.setSign(SignUtils.getSign(agentAccount.getSecurityKey(), agentCallbackRequest));
            //构造记录回调记录日志
            callbackLog.setOrderId(String.valueOf(order.getOrderId()));
            callbackLog.setRequestUrl(agentAccount.getCallbackUrl());
            callbackLog.setRequestBody(JSONObject.toJSONString(agentCallbackRequest));
            callbackLog.setProductCode(order.getProductCode());
            String id = UUID.randomUUID().toString().replace("-", "");
            log.info("发送回调数据id:{},url:{},数据:{},合作方订单号:{},本地订单号:{}",id, agentAccount.getCallbackUrl(), JSONObject.toJSONString(agentCallbackRequest), agentCallbackRequest.getOrderNo(),agentCallbackRequest.getOrderId());
            String result = httpClient.postJsonForString(agentAccount.getCallbackUrl(), agentCallbackRequest, null);
            log.info("发送回调数据返回值id:{},url:{},数据:{},订单号:{},本地订单号:{}",id, agentAccount.getCallbackUrl(), result,  agentCallbackRequest.getOrderNo(),agentCallbackRequest.getOrderId());
            callbackLog.setRequestMsg(result);
            callbackLogMapper.insert(callbackLog);
        } catch (Exception e) {
            log.info("实时回调消息处理失败:{},{}", e.getMessage(), order.getOrderId());
            callbackLog.setRequestMsg(e.getMessage());
            callbackLogMapper.insert(callbackLog);
        }
    }


}