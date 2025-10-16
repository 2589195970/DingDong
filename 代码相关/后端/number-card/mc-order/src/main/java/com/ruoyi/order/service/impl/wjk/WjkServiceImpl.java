package com.ruoyi.order.service.impl.wjk;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.order.reuqest.*;
import com.ruoyi.order.service.impl.BaseServiceImpl;
import com.ruoyi.order.service.wjk.WjkService;
import com.ruoyi.common.apis.wjk.*;
import com.ruoyi.common.enums.OrderEnum;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.http.HttpClient;
import com.ruoyi.common.order.apiconstant.WjkConstant;
import com.ruoyi.common.order.entity.*;
import com.ruoyi.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 挖金客对接类
 */
@Slf4j
@Service
public class WjkServiceImpl extends BaseServiceImpl implements WjkService {

    @Resource
    HttpClient httpClient;


    @Override
    protected String getServiceName() {
        return "挖金客";
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
        WjkCallbackRequest request = (WjkCallbackRequest) notifyRequest;
        log.info("挖金客回调信息处理:{}", JSONObject.toJSONString(request));
        Order order = orderService.getById(request.getOrderNo());
        if (order == null) {
            throw new BizException("回调订单号不存在，orderId：" + request.getOrderId());
        }
        //无效或作废订单
        if ("10".equals(request.getStatus())){
            String message = request.getMessage();
            orderStatusService.saveOrderFail(order, message);
        }
        //保存物流单号和快递
        if (!StrUtil.isBlankIfStr(request.getTrackingNum()) && !StrUtil.isBlankIfStr(request.getExpress())) {
            orderStatusService.saveDelivery(order, request.getExpress(), request.getTrackingNum());
        }
        //保存预占号码
        if (!StrUtil.isBlankIfStr(request.getNumber())) {
            orderStatusService.saveAccNumber(order, request.getNumber());
        }
        //保存激活订单
        if ("2".equals(request.getStatus())){
            orderStatusService.saveActive(order,request.getActiveTime());
        }
        if (StringUtils.isNotEmpty(request.getAmount())&&Integer.valueOf(request.getAmount())>0){
            orderStatusService.saveRecharge(order,request.getAmount(),System.currentTimeMillis());
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
            WjkSubmitOrderResponse apiSubmitOrder = apiSubmitOrder(request, order, product, upstreamInfo);
            order.setOrderStatus(OrderEnum.APPLY.getStatus());
            order.setOrderMessage(OrderEnum.APPLY.getMessage());
            order.setOrderUpstreamId(apiSubmitOrder.getOrderId());
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
    public WjkSubmitOrderResponse apiSubmitOrder(BaseSubmitOrderRequest request, Order order, Product product, UpstreamInfo upstreamInfo) throws Exception {
        WjkArgument wjkArgument = getWjkArgument(upstreamInfo);
        //构造请求类
        WjkSubmitOrderRequest wjkSubmitOrderRequest = new WjkSubmitOrderRequest();
        wjkSubmitOrderRequest.setOrderNo(order.getOrderId().toString());
        wjkSubmitOrderRequest.setCusName(request.getCardName());
        wjkSubmitOrderRequest.setCusCertNo(request.getCardId());
        wjkSubmitOrderRequest.setCusContactPhone(request.getCardPhone());
        wjkSubmitOrderRequest.setProvince(request.getProvinceName());
        wjkSubmitOrderRequest.setCity(request.getCityName());
        wjkSubmitOrderRequest.setDistrict(request.getCountyName());
        wjkSubmitOrderRequest.setAddress(request.getCardAddress());
        String msg;
        try {
            msg = wjkRequest(WjkConstant.BASE_URL,WjkConstant.SUBMIT_URL,wjkSubmitOrderRequest,wjkArgument,order.getOrderId(),product.getProductCode());
        } catch (Exception e) {
            throw new BizException("提单异常:{}", e.getMessage());
        }
        WjkSubmitOrderResponse wjkSubmitOrderResponse = buildResponseStr(msg, WjkSubmitOrderResponse.class);
        order.setUpstreamOrderStatus("200");
        order.setUpstreamOrderStatusMessage("提单成功");
        return wjkSubmitOrderResponse;
    }


    /**
     * 请求统一方法
     *
     * @param base
     * @param url
     * @return
     * @throws Exception
     */
    public String wjkRequest(String base, String url, WjkBaseRequest wjkBaseRequest,WjkArgument wjkArgument,Long orderId,String productCode) throws Exception {
        //生产序列号
        wjkBaseRequest.setSerial(UUID.fastUUID().toString().replaceAll("-", "").substring(0,15));
        wjkBaseRequest.setPartnerId(wjkArgument.getPartnerId());
        wjkBaseRequest.setProductId(wjkArgument.getProductId());
        wjkBaseRequest.setTimestamp(System.currentTimeMillis());
        wjkBaseRequest.setSign(SignUtils.getSign(wjkArgument.getAppKey(),wjkBaseRequest));
        OrderLog orderLog = orderLogService.getOrderLog(orderId.toString(),base + url,JSONObject.toJSONString(wjkBaseRequest),null,productCode);
        try {
            String msg = httpClient.postJsonForString(base + url, wjkBaseRequest, null);
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
     * @param clz
     * @param <T>
     * @return
     * @throws Exception
     */
    private <T> T buildResponseStr(String response, Class<T> clz) throws Exception {
        if (StrUtil.isBlankIfStr(response)) {
            throw new BizException("接口返回空");
        }
        WjkBaseResponse baseResponse = JSONUtil.toBean(response, WjkBaseResponse.class);
        if (baseResponse == null) {
            throw new BizException(StrUtil.format("接口返回解码失败"));
        }
        if (!WjkConstant.RESPONSE_SUCCESS.equals(baseResponse.getCode())) {
            //code值非成功
            String error = StringUtils.isNotEmpty(baseResponse.getDesc())?baseResponse.getDesc():baseResponse.getMessage();
            throw new BizException(StrUtil.format("接口返回错误:{}", error));
        }
        T realResponse = JSONObject.parseObject(JSONObject.toJSONString(baseResponse.getData()), clz);
        if (realResponse == null) {
            throw new BizException(StrUtil.format("接口返回body空"));
        }
        return realResponse;
    }


    /**
     * 解析挖金客参数
     * 此类主要将参数1 2 3与文档中参数对应 避免后续查找问题
     * @param upstreamInfo
     * @return
     */
    public WjkArgument getWjkArgument(UpstreamInfo upstreamInfo){
        WjkArgument wjkArgument= new WjkArgument();
        //合作方ID
        wjkArgument.setPartnerId(Integer.valueOf(upstreamInfo.getUpstreamApi().getArgument1()));
        //秘钥
        wjkArgument.setAppKey(upstreamInfo.getUpstreamApi().getArgument2());
        //产品ID
        wjkArgument.setProductId(Integer.valueOf(upstreamInfo.getUpstreamProduct().getArgument1()));
        return wjkArgument;
    }

    /**
     * 更新订单照片信息
     *
     * @param order 订单信息
     * @param product 产品信息
     * @param upstreamApi 上游API信息
     * @throws Exception
     */
    @Override
    public void updateOrderPhotos(Order order, Product product, UpstreamApi upstreamApi) throws Exception {
        if (product.getPhotoRequired() == null || product.getPhotoRequired() != 1) {
            log.info("挖金客订单{} 对应产品不需要照片审核，跳过上游更新", order.getOrderId());
            return;
        }

        // 检查是否有照片信息需要更新
        if (StringUtils.isEmpty(order.getIdCardFrontUrl()) &&
            StringUtils.isEmpty(order.getIdCardBackUrl()) &&
            StringUtils.isEmpty(order.getPersonPhotoUrl()) &&
            StringUtils.isEmpty(order.getCustomPhotoUrl())) {
            log.info("挖金客订单{} 没有照片信息需要更新", order.getOrderId());
            return;
        }

        try {
            // 获取上游信息
            UpstreamInfo upstreamInfo = upstreamApiService.getUpstreamInfo(upstreamApi.getUpstreamApiCode(), product.getUpstreamProductCode());
            WjkArgument wjkArgument = getWjkArgument(upstreamInfo);

            // 构造更新照片请求
            WjkUpdatePhotosRequest updateRequest = new WjkUpdatePhotosRequest();
            updateRequest.setOrderNo(order.getOrderId().toString());

            // 设置照片URL
            if (StringUtils.isNotEmpty(order.getIdCardFrontUrl())) {
                updateRequest.setIdCardFrontUrl(order.getIdCardFrontUrl());
            }
            if (StringUtils.isNotEmpty(order.getIdCardBackUrl())) {
                updateRequest.setIdCardBackUrl(order.getIdCardBackUrl());
            }
            if (StringUtils.isNotEmpty(order.getPersonPhotoUrl())) {
                updateRequest.setPersonPhotoUrl(order.getPersonPhotoUrl());
            }
            if (StringUtils.isNotEmpty(order.getCustomPhotoUrl())) {
                updateRequest.setCustomPhotoUrl(order.getCustomPhotoUrl());
            }

            log.info("挖金客更新照片请求: {}", JSONObject.toJSONString(updateRequest));

            // 发送请求
            String response = wjkUpdatePhotoRequest(WjkConstant.BASE_URL, WjkConstant.UPDATE_PHOTO_URL, updateRequest, wjkArgument, order.getOrderId(), product.getProductCode());

            // 解析响应
            WjkBaseResponse baseResponse = JSONUtil.toBean(response, WjkBaseResponse.class);
            if (baseResponse == null || !WjkConstant.RESPONSE_SUCCESS.equals(baseResponse.getCode())) {
                String error = baseResponse != null ? baseResponse.getDesc() : "接口返回异常";
                log.error("挖金客更新照片失败，订单{}: {}", order.getOrderId(), error);
                throw new BizException("挖金客更新照片失败: " + error);
            }

            log.info("挖金客更新照片成功，订单{}", order.getOrderId());

        } catch (Exception e) {
            log.error("挖金客更新照片异常，订单{}: {}", order.getOrderId(), e.getMessage(), e);
            // 不抛出异常，避免影响本地数据库更新
            // 可以考虑记录失败状态或加入重试机制
        }
    }

    /**
     * 发送更新照片请求
     *
     * @param base 基础URL
     * @param url 接口路径
     * @param request 请求数据
     * @param wjkArgument 挖金客参数
     * @param orderId 订单ID
     * @param productCode 产品编码
     * @return 响应结果
     * @throws Exception
     */
    private String wjkUpdatePhotoRequest(String base, String url, WjkUpdatePhotosRequest request, WjkArgument wjkArgument, Long orderId, String productCode) throws Exception {
        // 设置挖金客通用参数
        request.setSerial(UUID.fastUUID().toString().replaceAll("-", "").substring(0,15));
        request.setPartnerId(wjkArgument.getPartnerId());
        request.setProductId(wjkArgument.getProductId());
        request.setTimestamp(System.currentTimeMillis());
        request.setSign(SignUtils.getSign(wjkArgument.getAppKey(), request));

        OrderLog orderLog = orderLogService.getOrderLog(orderId.toString(), base + url, JSONObject.toJSONString(request), null, productCode);
        try {
            String response = httpClient.postJsonForString(base + url, request, null);
            orderLog.setRequestMsg(response);
            log.info("挖金客更新照片响应，订单{}: {}", orderId, response);
            return response;
        } catch (Exception e) {
            orderLog.setRequestMsg("更新照片异常: " + e.getMessage());
            log.error("挖金客更新照片请求异常，订单{}: {}", orderId, e.getMessage(), e);
            throw new BizException("挖金客更新照片请求异常: " + e.getMessage());
        } finally {
            orderLogService.saveOrderLog(orderLog);
        }
    }

}
