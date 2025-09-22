package com.ruoyi.order.service.impl.common;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.apis.wjk.*;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.enums.OrderEnum;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.http.HttpClient;
import com.ruoyi.common.order.apiconstant.WjkConstant;
import com.ruoyi.common.order.entity.*;
import com.ruoyi.common.order.reuqest.*;
import com.ruoyi.order.service.common.CommonService;
import com.ruoyi.order.service.impl.BaseServiceImpl;
import com.ruoyi.order.service.wjk.WjkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 挖金客对接类
 */
@Slf4j
@Service
public class CommonServiceImpl extends BaseServiceImpl implements CommonService {

    @Resource
    HttpClient httpClient;


    @Override
    protected String getServiceName() {
        return "通用";
    }

    /**
     * 获取号码
     * @param request
     * @param product
     * @param params
     * @return
     * @throws Exception
     */
    @Override
    protected List<NumberDTO> apiGetNumberList(GetNumberListRequest request, Product product, Map<String, Object> params) throws Exception {
        return null;
    }

    /**
     * 创建订单
     * @param orderId
     * @param request
     * @param product
     * @return
     * @throws Exception
     */
    @Override
    public Order createOrder(Long orderId, BaseSubmitOrderRequest request, Product product, UpstreamApi upstreamApi) throws Exception {
        return Order.buildOrderFromGeneral(orderId,request, product,upstreamApi);
    }

    /**
     * 发送验证码
     * @param request
     * @param params
     * @throws Exception
     */
    @Override
    public void sendVerificationCode(SendSmsCodeRequest request, Map<String, Object> params) throws Exception {
        return;
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
            if (!org.springframework.util.StringUtils.hasLength(message)) {
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




}
