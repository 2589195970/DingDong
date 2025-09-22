package com.ruoyi.order.service.impl.yqe;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.apis.yqe.*;
import com.ruoyi.common.enums.OrderEnum;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.http.HttpClient;
import com.ruoyi.common.order.apiconstant.YqeConstant;
import com.ruoyi.common.order.entity.*;
import com.ruoyi.common.order.reuqest.BaseNotifyRequest;
import com.ruoyi.common.order.reuqest.BaseSubmitOrderRequest;
import com.ruoyi.common.order.reuqest.GetNumberListRequest;
import com.ruoyi.common.order.reuqest.SendSmsCodeRequest;
import com.ruoyi.order.service.impl.BaseServiceImpl;
import com.ruoyi.order.service.yqe.YqeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 172 对接类
 */
@Slf4j
@Service
public class YqeServiceImpl extends BaseServiceImpl implements YqeService {

    @Resource
    HttpClient httpClient;


    @Override
    protected String getServiceName() {
        return "172";
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
     * 172 回调处理流程
     */
    @Override
    public void callback(BaseNotifyRequest notifyRequest) throws Exception {
        YqeCallbackRequest request = (YqeCallbackRequest) notifyRequest;
        log.info("172回调信息处理:{}", JSONObject.toJSONString(request));
        CallbackData callbackData = JSONObject.parseObject(request.getData(),CallbackData.class);
        Order order = orderService.getById(callbackData.getOrderNo());
        if (order == null) {
            throw new BizException("回调订单号不存在,orderId：" + callbackData.getOrderNo());
        }
        order.setUpstreamOrderStatus(callbackData.getOrderStatus());
        order.setUpstreamOrderStatusMessage(callbackData.getRemark());
        //无效或作废订单
        if ("审核不通过".equals(callbackData.getOrderStatus())||"已取消".equals(callbackData.getOrderStatus())
        ||"已撤单".equals(callbackData.getOrderStatus())){
            String message = callbackData.getRemark();
            orderStatusService.saveOrderFail(order, message);
        }
        //保存物流单号和快递
        if (!StrUtil.isBlankIfStr(callbackData.getExpressCode()) && !StrUtil.isBlankIfStr(callbackData.getExpressName())) {
            orderStatusService.saveDelivery(order, callbackData.getExpressName(), callbackData.getExpressCode());
        }
        //保存预占号码
        if (!StrUtil.isBlankIfStr(callbackData.getThirdPhone())) {
            orderStatusService.saveAccNumber(order, callbackData.getThirdPhone());
        }
        //保存激活订单
        if ("已激活".equals(callbackData.getCardStatus())){
            orderStatusService.saveActive(order,System.currentTimeMillis());
        }
        //保存充值信息
        if ("1".equals(callbackData.getIsFirstCharge())){
            orderStatusService.saveRecharge(order,callbackData.getFirstCharge(),System.currentTimeMillis());
        }
        //回调后置处理
        afterCallback(order,null);
    }


    /**
     * 订单处理
     * @param request
     * @param order
     * @param product
     * @param upstreamInfo
     * @return
     * @throws Exception
     */
    @Override
    public Order syncSubmitOrderFlow(BaseSubmitOrderRequest request, Order order, Product product, UpstreamInfo upstreamInfo) throws Exception {
        try {
            order.setUpstreamPushTime(System.currentTimeMillis());
            //提交订单信息
            YqeSubmitOrderResponse apiSubmitOrder = apiSubmitOrder(request, order, product, upstreamInfo);
            order.setOrderStatus(OrderEnum.APPLY.getStatus());
            order.setOrderMessage(OrderEnum.APPLY.getMessage());
            order.setOrderUpstreamId(apiSubmitOrder.getMessage());
            orderService.updateById(order);
            return order;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 提交订单
     * @param request
     * @param order
     * @param product
     * @param upstreamInfo
     * @return
     * @throws Exception
     */
    public YqeSubmitOrderResponse apiSubmitOrder(BaseSubmitOrderRequest request, Order order, Product product, UpstreamInfo upstreamInfo) throws Exception {
        YqeArgument yqeArgument = getYqeArgument(upstreamInfo);
        YqeSubmitOrderRequest yqeSubmitOrderRequest = new YqeSubmitOrderRequest();
        yqeSubmitOrderRequest.setUser_id(yqeArgument.getUserId());
        yqeSubmitOrderRequest.setTimestamp(System.currentTimeMillis()/1000+"");
        yqeSubmitOrderRequest.setName(request.getCardName());
        yqeSubmitOrderRequest.setPhone(request.getCardPhone());
        yqeSubmitOrderRequest.setIDCard(request.getCardId());
        yqeSubmitOrderRequest.setProvince(request.getProvinceName());
        yqeSubmitOrderRequest.setCity(request.getCityName());
        yqeSubmitOrderRequest.setArea(request.getCountyName());
        yqeSubmitOrderRequest.setAddress(request.getCardAddress());
        yqeSubmitOrderRequest.setProductID(Integer.valueOf(yqeArgument.getProductID()));
        yqeSubmitOrderRequest.setDownOrderID(order.getOrderId()+"");
        yqeSubmitOrderRequest.setUser_sign(getSign(yqeSubmitOrderRequest,yqeArgument.getSecret()));
        String msg;
        try {
            msg = yqeRequest(YqeConstant.BASE_URL,yqeSubmitOrderRequest,order.getOrderId(),product.getProductCode());
        } catch (Exception e) {
            order.setUpstreamOrderStatus("-1");
            order.setUpstreamOrderStatusMessage("提单异常:"+e.getMessage());
            throw new BizException("提单异常:{}", e.getMessage());
        }
        YqeSubmitOrderResponse yqeSubmitOrderResponse = buildResponseStr(msg,order);
        order.setUpstreamOrderStatus("0");
        order.setUpstreamOrderStatusMessage("成功接收订单");
        return yqeSubmitOrderResponse;
    }

    public String getSign(YqeSubmitOrderRequest yqeSubmitOrderRequest,String secret){
            StringBuilder paramString = new StringBuilder();
            paramString.append("Address=").append(yqeSubmitOrderRequest.getAddress()).append("&");
            paramString.append("Area=").append(yqeSubmitOrderRequest.getArea()).append("&");
            paramString.append("City=").append(yqeSubmitOrderRequest.getCity()).append("&");
            paramString.append("DownOrderID=").append(yqeSubmitOrderRequest.getDownOrderID()).append("&");
            paramString.append("IDCard=").append(yqeSubmitOrderRequest.getIDCard()).append("&");
            paramString.append("Name=").append(yqeSubmitOrderRequest.getName()).append("&");
            paramString.append("Phone=").append(yqeSubmitOrderRequest.getPhone()).append("&");
            paramString.append("ProductID=").append(yqeSubmitOrderRequest.getProductID()).append("&");
            paramString.append("Province=").append(yqeSubmitOrderRequest.getProvince()).append("&");
            paramString.append("ThirdPhone=").append(yqeSubmitOrderRequest.getThirdPhone()).append("&");
            paramString.append("Timestamp=").append(yqeSubmitOrderRequest.getTimestamp()).append("&");
            paramString.append("user_id=").append(yqeSubmitOrderRequest.getUser_id());
            paramString.append(secret);
            return SecureUtil.md5(paramString.toString());
    }

    /**
     * 请求统一方法
     *
     * @param url
     * @return
     * @throws Exception
     */
    public String yqeRequest(String url, YqeSubmitOrderRequest yqeSubmitOrderRequest,Long orderId,String productCode) throws Exception {
        OrderLog orderLog = orderLogService.getOrderLog(orderId.toString(), url,JSONObject.toJSONString(yqeSubmitOrderRequest),null,productCode);
        try {
            String msg = httpClient.postFormForString(url, BeanUtil.beanToMap(yqeSubmitOrderRequest), null);
            orderLog.setRequestMsg(msg);
            return msg;
        } catch (Exception e) {
            orderLog.setRequestMsg("提单异常:"+e.getMessage());
            throw new BizException(e.getMessage());
        }finally {
            orderLogService.saveOrderLog(orderLog);
        }
    }


    /**
     * 返回值统一解析类
     *
     * @param response
     * @return
     * @throws Exception
     */
    private YqeSubmitOrderResponse buildResponseStr(String response,Order order) throws Exception {
        if (StrUtil.isBlankIfStr(response)) {
            throw new BizException("接口返回空");
        }
        YqeSubmitOrderResponse baseResponse = JSONUtil.toBean(response, YqeSubmitOrderResponse.class);
        if (baseResponse == null||baseResponse.getCode()==null) {
            throw new BizException(StrUtil.format("接口返回解码失败"));
        }
        if (!YqeConstant.RESPONSE_SUCCESS.equals(baseResponse.getCode())) {
            //code值非成功
            String error = baseResponse.getMessage();
            order.setUpstreamOrderStatus(baseResponse.getCode());
            order.setUpstreamOrderStatusMessage(error);
            throw new BizException(StrUtil.format("接口返回错误:{}", error));
        }
        return baseResponse;
    }


    /**
     * 解析172相关参数类
     * 此类主要将参数1 2 3与文档中参数对应 避免后续查找问题
     * @param upstreamInfo
     * @return
     */
    public YqeArgument getYqeArgument(UpstreamInfo upstreamInfo){
        YqeArgument yqeArgument= new YqeArgument();
        //代理在172号卡登录账号
        yqeArgument.setUserId(upstreamInfo.getUpstreamApi().getArgument1());
        //秘钥
        yqeArgument.setSecret(upstreamInfo.getUpstreamApi().getArgument2());
        //产品ID
        yqeArgument.setProductID(upstreamInfo.getUpstreamProduct().getArgument1());
        return yqeArgument;
    }

}
