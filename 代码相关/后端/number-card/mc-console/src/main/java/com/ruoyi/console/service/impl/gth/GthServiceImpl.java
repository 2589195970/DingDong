package com.ruoyi.console.service.impl.gth;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.apis.gth.*;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.http.HttpClient;
import com.ruoyi.common.order.apiconstant.GthConstant;
import com.ruoyi.common.order.entity.*;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.console.service.UpstreamApiService;
import com.ruoyi.console.service.gth.GthService;
import com.ruoyi.console.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 感叹号对接类 - 照片上传功能
 */
@Slf4j
@Service
public class GthServiceImpl extends BaseServiceImpl implements GthService {

    @Resource
    private HttpClient httpClient;

    @Resource
    private UpstreamApiService upstreamApiService;

    @Override
    protected String getServiceName() {
        return "感叹号";
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
            log.info("订单{} 对应产品不需要照片审核，跳过上游更新", order.getOrderId());
            return;
        }

        // 检查是否有照片信息需要更新
        if (StringUtils.isEmpty(order.getIdCardFrontUrl()) &&
            StringUtils.isEmpty(order.getIdCardBackUrl()) &&
            StringUtils.isEmpty(order.getPersonPhotoUrl()) &&
            StringUtils.isEmpty(order.getCustomPhotoUrl())) {
            log.info("订单{} 没有照片信息需要更新", order.getOrderId());
            return;
        }

        try {
            // 获取上游信息
            UpstreamInfo upstreamInfo = upstreamApiService.getUpstreamInfo(upstreamApi.getUpstreamApiCode(), product.getUpstreamProductCode());
            GthArgument gthArgument = getGthArgument(upstreamInfo);

            // 获取照片上传加密秘钥
            String encryptionSecret = getEncryptionSecret(order, gthArgument);

            // 构造更新照片请求
            GthUpdatePhotosRequest updateRequest = new GthUpdatePhotosRequest();

            // 设置从接口获取的加密秘钥
            updateRequest.setStr_rand(encryptionSecret);

            // 设置照片URL（不需要加密，直接传递URL）
            if (StringUtils.isNotEmpty(order.getIdCardFrontUrl())) {
                updateRequest.setFace_url(order.getIdCardFrontUrl());
            }
            if (StringUtils.isNotEmpty(order.getIdCardBackUrl())) {
                updateRequest.setBack_url(order.getIdCardBackUrl());
            }
            if (StringUtils.isNotEmpty(order.getPersonPhotoUrl())) {
                updateRequest.setHand_url(order.getPersonPhotoUrl());
            }
            if (StringUtils.isNotEmpty(order.getCustomPhotoUrl())) {
                updateRequest.setCustom_photos_url(order.getCustomPhotoUrl());
            }

            log.info("感叹号更新照片请求: {}", JSONObject.toJSONString(updateRequest));

            // 发送请求
            String response = gthUpdatePhotoRequest(GthConstant.UPDATE_PHOTO_URL, updateRequest, order.getOrderId(), product.getProductCode());

            // 解析响应
            GthBaseResponse baseResponse = JSONUtil.toBean(response, GthBaseResponse.class);
            if (baseResponse == null || baseResponse.getMsg() == null || !GthConstant.RESPONSE_SUCCESS.equals(baseResponse.getMsg().getCode())) {
                String error = baseResponse != null && baseResponse.getMsg() != null ? baseResponse.getMsg().getInfo() : "接口返回异常";
                log.error("感叹号更新照片失败，订单{}: {}", order.getOrderId(), error);
                throw new BizException("感叹号更新照片失败: " + error);
            }

            log.info("感叹号更新照片成功，订单{}", order.getOrderId());

        } catch (Exception e) {
            log.error("感叹号更新照片异常，订单{}: {}", order.getOrderId(), e.getMessage(), e);
        }
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

    /**
     * 获取照片上传秘钥
     *
     * @param order 订单信息
     * @param gthArgument 感叹号参数
     * @return 加密秘钥
     * @throws Exception
     */
    private String getEncryptionSecret(Order order, GthArgument gthArgument) throws Exception {
        try {
            // 构造请求参数
            Map<String, Object> params = new HashMap<>();
            params.put("share_id", gthArgument.getShareId());
            params.put("order_id", order.getOrderId());

            // 生成签名：order_id=xxx&share_id=xxx + apiToken
            String signString = "order_id=" + order.getOrderId() + "&share_id=" + gthArgument.getShareId() + gthArgument.getApiToken();
            String sign = SecureUtil.md5(signString).toLowerCase();
            params.put("sign", sign);

            log.info("感叹号获取照片上传秘钥请求参数: {}", JSONObject.toJSONString(params));

            // 发送GET请求
            String response = httpClient.getForString(GthConstant.GET_PHOTO_SECRET_URL, params, null);

            log.info("感叹号获取照片上传秘钥响应，订单{}: {}", order.getOrderId(), response);

            // 解析响应
            GthBaseResponse baseResponse = JSONUtil.toBean(response, GthBaseResponse.class);
            if (baseResponse == null || baseResponse.getMsg() == null || !GthConstant.RESPONSE_SUCCESS.equals(baseResponse.getMsg().getCode())) {
                String error = baseResponse != null && baseResponse.getMsg() != null ? baseResponse.getMsg().getInfo() : "接口返回异常";
                log.error("感叹号获取照片上传秘钥失败，订单{}: {}", order.getOrderId(), error);
                throw new BizException("感叹号获取照片上传秘钥失败: " + error);
            }

            // 解析秘钥
            GthPhotoSecretResponse secretResponse = JSONObject.parseObject(JSONObject.toJSONString(baseResponse.getData()), GthPhotoSecretResponse.class);
            if (secretResponse == null || StringUtils.isEmpty(secretResponse.getStr_rand())) {
                throw new BizException("感叹号获取照片上传秘钥为空");
            }

            log.info("感叹号获取照片上传秘钥成功，订单{}: {}", order.getOrderId(), secretResponse.getStr_rand());
            return secretResponse.getStr_rand();

        } catch (Exception e) {
            log.error("感叹号获取照片上传秘钥异常，订单{}: {}", order.getOrderId(), e.getMessage(), e);
            throw new BizException("感叹号获取照片上传秘钥异常: " + e.getMessage());
        }
    }

    /**
     * 发送更新照片请求
     *
     * @param url 请求URL
     * @param request 请求数据
     * @param orderId 订单ID
     * @param productCode 产品编码
     * @return 响应结果
     * @throws Exception
     */
    private String gthUpdatePhotoRequest(String url, GthUpdatePhotosRequest request, Long orderId, String productCode) throws Exception {
        // 记录订单日志
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(orderId.toString());
        orderLog.setRequestUrl(url);
        orderLog.setRequestBody(JSONObject.toJSONString(request));
        orderLog.setProductCode(productCode);
        orderLog.setCreateTime(System.currentTimeMillis());

        try {
            String response = httpClient.postJsonForString(url, request, null);
            orderLog.setRequestMsg(response);
            log.info("感叹号更新照片响应，订单{}: {}", orderId, response);
            return response;
        } catch (Exception e) {
            orderLog.setRequestMsg("更新照片异常: " + e.getMessage());
            log.error("感叹号更新照片请求异常，订单{}: {}", orderId, e.getMessage(), e);
            throw new BizException("感叹号更新照片请求异常: " + e.getMessage());
        } finally {
            // 这里应该保存订单日志，但mc-console中可能没有OrderLogService
            // orderLogService.saveOrderLog(orderLog);
            log.info("订单日志记录完成: 订单ID: {}, 请求URL: {}", orderId, url);
        }
    }

    /**
     * 感叹号参数类
     */
    private static class GthArgument {
        private String shareId;
        private String apiToken;
        private String aesKey;
        private String sku;
        private String sourceSku;

        // getter和setter方法
        public String getShareId() { return shareId; }
        public void setShareId(String shareId) { this.shareId = shareId; }
        public String getApiToken() { return apiToken; }
        public void setApiToken(String apiToken) { this.apiToken = apiToken; }
        public String getAesKey() { return aesKey; }
        public void setAesKey(String aesKey) { this.aesKey = aesKey; }
        public String getSku() { return sku; }
        public void setSku(String sku) { this.sku = sku; }
        public String getSourceSku() { return sourceSku; }
        public void setSourceSku(String sourceSku) { this.sourceSku = sourceSku; }
    }
}