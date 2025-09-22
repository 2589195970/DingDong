package com.ruoyi.order.service.impl.gth;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.apis.gth.*;
import com.ruoyi.common.enums.OrderEnum;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.http.HttpClient;
import com.ruoyi.common.order.apiconstant.GthConstant;
import com.ruoyi.common.order.entity.*;
import com.ruoyi.common.order.reuqest.BaseNotifyRequest;
import com.ruoyi.common.order.reuqest.BaseSubmitOrderRequest;
import com.ruoyi.common.order.reuqest.GetNumberListRequest;
import com.ruoyi.common.order.reuqest.SendSmsCodeRequest;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.order.service.gth.GthService;
import com.ruoyi.order.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 感叹号对接类
 */
@Slf4j
@Service
public class GthServiceImpl extends BaseServiceImpl implements GthService {

    @Resource
    HttpClient httpClient;


    @Override
    protected String getServiceName() {
        return "感叹号";
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
     * 感叹号 回调处理流程
     * 0	订单终止
     * 100	已支付
     * 120	初步审核
     * 122	订单欠费
     * 125	敏感地区待审核
     * 140	等待填写身份信息
     * 150	等待上传照片
     * 200	待审核
     * 450	待开卡
     * 460	已领取
     * 470	开卡中
     * 500	待发货
     * 550	已发货
     * -100	待支付
     * -120	信息审核失败
     * -200	证件不合格待重传
     * -460	开卡失败
     * @param notifyRequest
     * @throws Exception
     *
     * 激活状态
     * 0	未激活
     * 100	已激活
     * 200	激活且充值
     *
     * 待发货 已发货 开卡失败 身份证审核失败 订单终止 其余状态不会回调
     */
    @Override
    public void callback(BaseNotifyRequest notifyRequest) throws Exception {
        GthCallbackRequest request = (GthCallbackRequest) notifyRequest;
        log.info("感叹号回调信息处理:{}", JSONObject.toJSONString(request));
        Order order = orderService.getById(request.getOuter_id());
        if (order == null) {
            throw new BizException("回调订单号不存在，orderId：" + request.getOuter_id());
        }
        order.setUpstreamOrderStatus(request.getStatus());
        order.setUpstreamOrderStatusMessage(request.getReason());
        //无效或作废订单
        if ("-460".equals(request.getStatus())){
            String message = request.getReason();
            orderStatusService.saveOrderFail(order, message);
        }
        //保存物流单号和快递
        if (!StrUtil.isBlankIfStr(request.getTracking_company()) && !StrUtil.isBlankIfStr(request.getTracking_number())) {
            orderStatusService.saveDelivery(order, request.getTracking_company(), request.getTracking_number());
        }
        //保存预占号码
        if (!StrUtil.isBlankIfStr(request.getPlan_mobile_produced())) {
            orderStatusService.saveAccNumber(order, request.getPlan_mobile_produced());
        }
        //保存激活订单
        if ("1".equals(request.getIs_activated())){
            Long activeTime = StringUtils.isNotEmpty(request.getActivated_at())? DateUtil.parse(request.getActivated_at(), DatePattern.NORM_DATETIME_PATTERN).getTime():System.currentTimeMillis();
            orderStatusService.saveActive(order,activeTime);
        }
        //保存充值信息
        if ("1".equals(request.getIs_recharged())){
            Long rechargeTime = StringUtils.isNotEmpty(request.getRecharged_at())? DateUtil.parse(request.getRecharged_at(), DatePattern.NORM_DATETIME_PATTERN).getTime():System.currentTimeMillis();
            orderStatusService.saveRecharge(order,request.getRecharge_amount(),rechargeTime);
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
            GthSubmitOrderResponse apiSubmitOrder = apiSubmitOrder(request, order, product, upstreamInfo);
            order.setOrderStatus(OrderEnum.APPLY.getStatus());
            order.setOrderMessage(OrderEnum.APPLY.getMessage());
            order.setOrderUpstreamId(apiSubmitOrder.getId());
            //记录一些感叹号特有的参数
            JSONObject jsonObject = org.springframework.util.StringUtils.hasLength(order.getJsonParam()) ? JSONObject.parseObject(order.getJsonParam()) : new JSONObject();
            jsonObject.put("GthSubmitOrderResponse", JSONObject.toJSONString(apiSubmitOrder));
            order.setJsonParam(jsonObject.toJSONString());
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
    public GthSubmitOrderResponse apiSubmitOrder(BaseSubmitOrderRequest request, Order order, Product product, UpstreamInfo upstreamInfo) throws Exception {
        GthArgument gthArgument = getGthArgument(upstreamInfo);
        //构造请求类
        GthSubmitOrderRequest gthSubmitOrderRequest = new GthSubmitOrderRequest();
        gthSubmitOrderRequest.setSku(gthArgument.getSku());
        gthSubmitOrderRequest.setSource_sku(gthArgument.getSourceSku());
        gthSubmitOrderRequest.setSource_id(order.getOrderId().toString());
        //发展人ID
        gthSubmitOrderRequest.setShare_id(gthArgument.getShareId());
        //用户信息
        gthSubmitOrderRequest.setId_name(request.getCardName());
        gthSubmitOrderRequest.setId_num(request.getCardId());
        gthSubmitOrderRequest.setMobile(request.getCardPhone());
        gthSubmitOrderRequest.setName(request.getCardName());
        //收货信息
        gthSubmitOrderRequest.setProvince_code(request.getProvinceCode());
        gthSubmitOrderRequest.setProvince(request.getProvinceName());
        gthSubmitOrderRequest.setCity_code(request.getCityCode());
        gthSubmitOrderRequest.setCity(request.getCityName());
        gthSubmitOrderRequest.setDistrict_code(request.getCountyCode());
        gthSubmitOrderRequest.setDistrict(request.getCountyName());
        gthSubmitOrderRequest.setAddress(request.getCardAddress());
        //生成签名
        gthSubmitOrderRequest.setSign(getSign(gthArgument.getShareId(),gthArgument.getSku(),order.getOrderId().toString(),gthArgument.getApiToken()));
        log.info("感叹号订单提交报文:{}",JSONObject.toJSONString(gthSubmitOrderRequest));
        //AES 加密关键参数
        gthSubmitOrderRequest.setId_name(getAesParameters(gthArgument.getAesKey(),gthSubmitOrderRequest.getId_name()));
        gthSubmitOrderRequest.setId_num(getAesParameters(gthArgument.getAesKey(),gthSubmitOrderRequest.getId_num()));
        gthSubmitOrderRequest.setMobile(getAesParameters(gthArgument.getAesKey(),gthSubmitOrderRequest.getMobile()));
        gthSubmitOrderRequest.setName(getAesParameters(gthArgument.getAesKey(),gthSubmitOrderRequest.getName()));
        gthSubmitOrderRequest.setAddress(getAesParameters(gthArgument.getAesKey(),gthSubmitOrderRequest.getAddress()));
        String msg;
        try {
            msg = gthRequest(GthConstant.BASE_URL,gthSubmitOrderRequest,order.getOrderId(),product.getProductCode());
        } catch (Exception e) {
            order.setUpstreamOrderStatus("-1");
            order.setUpstreamOrderStatusMessage("提单异常:"+e.getMessage());
            throw new BizException("提单异常:{}", e.getMessage());
        }
        GthSubmitOrderResponse gthSubmitOrderResponse = buildResponseStr(msg, GthSubmitOrderResponse.class,order);
        order.setUpstreamOrderStatus("120");
        order.setUpstreamOrderStatusMessage("成功接收订单");
        return gthSubmitOrderResponse;
    }

    /**
     * 签名生成示例
     * share_id=123&sku=321&source_id=123321jW5DoNjzP7NXb1nvf4ZKGb4bH0fWbbiwokzoyo8QX7cmSuy4l6
     * @param shareId
     * @param sku
     * @param sourceId
     * @param apiToken
     * @return
     */
    public String getSign(String shareId,String sku,String sourceId,String apiToken){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("share_id=").append(shareId).append("&sku=").append(sku).append("&source_id=").append(sourceId).append(apiToken);
        return SecureUtil.md5(stringBuffer.toString()).toLowerCase();
    }

    /**
     *
     * @return
     */
    public String getAesParameters(String aesKey,String content){
        // 创建AES对象，使用ECB模式和PKCS5Padding填充
        AES aes = new AES("ECB", "PKCS5Padding", aesKey.getBytes());
        // 加密操作
        byte[] encryptedBytes = aes.encrypt(content);
        // 将加密后的字节数组转换为Base64字符串
        String encryptedBase64 = Base64.encode(encryptedBytes);
        return encryptedBase64;
        /*// 解密操作
        byte[] decryptedBytes = aes.decrypt(Base64.decode(encryptedBase64));
        String decryptedText = new String(decryptedBytes);
        System.out.println("解密后的明文: " + decryptedText);*/
    }

    /**
     * 请求统一方法
     *
     * @param url
     * @return
     * @throws Exception
     */
    public String gthRequest(String url, GthSubmitOrderRequest gthSubmitOrderRequest,Long orderId,String productCode) throws Exception {
        OrderLog orderLog = orderLogService.getOrderLog(orderId.toString(), url,JSONObject.toJSONString(gthSubmitOrderRequest),null,productCode);
        try {
            String msg = httpClient.postJsonForString(url, gthSubmitOrderRequest, null);
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
    private <T> T buildResponseStr(String response, Class<T> clz,Order order) throws Exception {
        if (StrUtil.isBlankIfStr(response)) {
            throw new BizException("接口返回空");
        }
        GthBaseResponse baseResponse = JSONUtil.toBean(response, GthBaseResponse.class);
        if (baseResponse == null||baseResponse.getMsg()==null) {
            throw new BizException(StrUtil.format("接口返回解码失败"));
        }
        if (!GthConstant.RESPONSE_SUCCESS.equals(baseResponse.getMsg().getCode())) {
            //code值非成功
            String error = StringUtils.isNotEmpty(baseResponse.getMsg().getInfo())?baseResponse.getMsg().getInfo():"";
            //如果未返回错误原因尝试匹配编码值
            /*|50405 |提交方式错误 |post方式提交 |
            |50422 |必传参数未传 |检查必传参数 |
            |50401 |签名错误 |签名错误，检查签名 |
            |50304 |该商品不存在或已下架 |找运营确定商品在敢探号一系统上架 |
            |52000 |数据异常 |核对外部订单号 |*/
            if(StringUtils.isEmpty(error)){
                if(baseResponse.getMsg().getCode().equals("50405")){
                    error = "提交方式错误";
                }if(baseResponse.getMsg().getCode().equals("50422")){
                    error = "必传参数未传";
                }if(baseResponse.getMsg().getCode().equals("50401")){
                    error = "签名错误";
                }if(baseResponse.getMsg().getCode().equals("50304")){
                    error = "该商品不存在或已下架";
                }if(baseResponse.getMsg().getCode().equals("52000")){
                    error = "数据异常";
                }
            }
            order.setUpstreamOrderStatus(baseResponse.getMsg().getCode());
            order.setUpstreamOrderStatusMessage(error);
            throw new BizException(StrUtil.format("接口返回错误:{}", error));
        }
        T realResponse = JSONObject.parseObject(JSONObject.toJSONString(baseResponse.getData()), clz);
        if (realResponse == null) {
            throw new BizException(StrUtil.format("接口返回body空"));
        }
        return realResponse;
    }


    /**
     * 解析感叹号参数
     * 此类主要将参数1 2 3与文档中参数对应 避免后续查找问题
     * @param upstreamInfo
     * @return
     */
    public GthArgument getGthArgument(UpstreamInfo upstreamInfo){
        GthArgument gthArgument= new GthArgument();
        //发展人ID
        gthArgument.setShareId(upstreamInfo.getUpstreamApi().getArgument1());
        //
        gthArgument.setApiToken(upstreamInfo.getUpstreamApi().getArgument2());
        //AES加密秘钥
        gthArgument.setAesKey(upstreamInfo.getUpstreamApi().getArgument3());
        //商品名称
        gthArgument.setSku(upstreamInfo.getUpstreamProduct().getArgument1());
        //商品编码
        gthArgument.setSourceSku(upstreamInfo.getUpstreamProduct().getArgument2());
        return gthArgument;
    }

}
