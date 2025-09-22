package com.ruoyi.order.service.impl.partner;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.apis.partner.PartnerSubmitOrderRequest;
import com.ruoyi.common.apis.partner.PartnerSubmitOrderResponse;
import com.ruoyi.common.apis.partner.SignUtils;
import com.ruoyi.common.apis.partner.base.PartnerBaseRequest;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.constant.CacheKeyConstants;
import com.ruoyi.common.enums.OrderEnum;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.order.entity.Order;
import com.ruoyi.common.order.reuqest.APISubmitInfoRequest;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.order.service.AgentService;
import com.ruoyi.order.service.ProductApiService;
import com.ruoyi.order.service.partner.PartnerService;
import com.ruoyi.order.utils.SnowflakeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description 合作方API进单
 * @Author 陈思伟
 * @Date 2020/5/27 12:10
 */
@Slf4j
@Service
public class PartnerServiceImpl implements PartnerService {

    private static final String TAG = "PartnerServiceImpl";

    @Resource
    public SnowflakeUtil snowflakeUtil;

    @Resource(name = "temporarilyStringRedisTemplate")
    StringRedisTemplate temporarilyStringRedisTemplate;

    @Resource
    AgentService agentService;

    @Resource
    ProductApiService productApiService;

    /**
     * 合作方提交订单
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public PartnerSubmitOrderResponse submitOrder(PartnerSubmitOrderRequest request) throws Exception {
        return submitOrder(snowflakeUtil.getNextId(), request);
    }

    @Override
    public PartnerSubmitOrderResponse submitOrder(Long orderId, PartnerSubmitOrderRequest request) throws Exception {
        log.info("收到三方订单:{}", JSONObject.toJSONString(request));
        checkSerial(request.getPartnerCode(), request.getSerial());
        AgentAccount agentAccount = agentService.getAgentAccountByCode(request.getPartnerCode());
        checkSign(agentAccount.getSecurityKey(), request);
        try {
            APISubmitInfoRequest apiSubmitInfoRequest = getAPISubmitInfoRequest(request,agentAccount);
            Order order = productApiService.submitAPISubmitOrder(orderId, apiSubmitInfoRequest);
            PartnerSubmitOrderResponse partnerSubmitOrderResponse = new PartnerSubmitOrderResponse();
            partnerSubmitOrderResponse.setOrderId(order.getOrderId());
            return partnerSubmitOrderResponse;
        } catch (Exception e) {
            log.info("合作方订单请求失败:{},{},{}", request.getOrderNo(), orderId, e.getMessage());
            throw e;
        }
    }

    public APISubmitInfoRequest getAPISubmitInfoRequest(PartnerSubmitOrderRequest request, AgentAccount agentAccount) {
        APISubmitInfoRequest apiSubmitInfoRequest = new APISubmitInfoRequest();
        BeanUtil.copyProperties(request,apiSubmitInfoRequest);
        apiSubmitInfoRequest.setAgentCode(agentAccount.getAgentCode());
        apiSubmitInfoRequest.setReceiverName(request.getCardName());
        apiSubmitInfoRequest.setOrderDownstreamId(request.getOrderNo());
        apiSubmitInfoRequest.setOrderSource(OrderEnum.OrderSourceEnum.API.getSourceType());
        return apiSubmitInfoRequest;
    }


    /**
     * 校验签名
     * @param key
     * @param request
     * @throws Exception
     */
    void checkSign(String key, PartnerBaseRequest request) throws Exception {
        String sign = SignUtils.getSign(key, request).toUpperCase(Locale.ROOT);
        if (StrUtil.isBlankIfStr(sign) || StrUtil.isBlankIfStr(request.getSign()) || !sign.equals(request.getSign().toUpperCase(Locale.ROOT))) {
            log.info("签名校验失败,sign={},signStr={},代理商:{}", sign, SignUtils.getSignStr(key, request),request.getPartnerCode());
            throw new BizException("签名校验失败");
        }
    }

    /**
     * 校验序列号是否重复
     * @param partnerCode
     * @param serial
     * @throws Exception
     */
    void checkSerial(String partnerCode, String serial) throws Exception {
        String key = CacheUtils.generalKey(CacheKeyConstants.PARTNER_REQUEST_SERIAL, partnerCode, serial);
        if (Boolean.TRUE.equals(temporarilyStringRedisTemplate.hasKey(key))) {
            throw new BizException("请勿重复发起请求");
        }
        temporarilyStringRedisTemplate.opsForValue().set(key, "1", BaseConstant.TEN_INT, TimeUnit.MINUTES);
    }



}
