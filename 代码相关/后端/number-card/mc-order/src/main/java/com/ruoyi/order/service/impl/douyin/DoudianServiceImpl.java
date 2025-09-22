package com.ruoyi.order.service.impl.douyin;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.apis.douyin.*;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.constant.DouDianConstant;
import com.ruoyi.common.constant.RocketMqConstant;
import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.enums.ResponseMessageEnum;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.http.HttpClient;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.order.service.douyin.DouDianManagerService;
import com.ruoyi.order.service.douyin.DoudianService;
import com.ruoyi.order.utils.douyin.DouDianAESUtils;
import com.ruoyi.order.utils.douyin.DouDianMapUtil;
import com.ruoyi.order.utils.douyin.DouDianUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;



/**
 * @author 陈思伟
 * @version 1.0
 * @description: 抖店相关接口实现类
 * @date 2025/7/16 14:32
 */

@Service
@Slf4j
public class DoudianServiceImpl implements DoudianService {
    private static final String TAG = "DoudianServiceImpl";

    @Resource
    private HttpClient httpClient;

    @Resource(name = "configStringRedisTemplate")
    private StringRedisTemplate configStringRedisTemplate;



    @Value("${doudian.batchDecryptUrl}")
    private String batchDecryptUrl;

    @Resource
    private DouDianManagerService douDianManagerService;




    /**
     * 抖店订阅消息接收
     *
     * @param eventSign
     * @param appId
     * @param body
     * @return
     * @throws Exception
     */
    @Override
    public String receiveMessage(String eventSign, String appId, String body) throws Exception {
        //存储订阅消息中的订单id
        HashSet<String> pIdSet = new HashSet<>(64);
        //获取所有的订阅订单号
        List<String> list = JSONObject.parseArray(body, String.class);
        if (list == null) {
            log.error(TAG, "解析订阅数据失败");
            throw new BizException("订阅数据异常");
        }
        for (Object o : list) {
            Map<String, Object> msgMap = JSONUtil.toBean(String.valueOf(o), Map.class);
            if (msgMap.isEmpty() || (!msgMap.containsKey("data")) || (!msgMap.containsKey("tag"))) {
                //数据不全，直接返回失败，让抖店平台重新推送
                log.error(TAG, "解析订阅数据失败");
                throw new BizException("订阅数据异常");
            }
            if ("0".equals(DouDianMapUtil.getString(msgMap, "tag"))) {
                return DouDianUtil.marshal(new DouDianSuccessResponse(), false);
            }
            String detailData = DouDianMapUtil.getString(msgMap, "data");
            if (StrUtil.isBlankIfStr(detailData)) {
                log.error(TAG, "解析订阅数据失败");
                throw new BizException("订阅数据异常");
            }
            Map<String, Object> dataDetailMap = JSONUtil.toBean(detailData, Map.class);
            if (dataDetailMap.isEmpty() || (!dataDetailMap.containsKey("p_id"))) {
                //数据不全，直接返回失败，让抖店平台重新推送
                log.error(TAG, "解析订阅数据失败");
                throw new BizException("订阅数据异常");
            }
            //将获取到的pId存入集合
            String pId = DouDianMapUtil.getString(dataDetailMap, "p_id");
            String shopId = DouDianMapUtil.getString(dataDetailMap, "shop_id");
            String orderStatus = DouDianMapUtil.getString(dataDetailMap, "order_status");
            if (!"105".equals(orderStatus)) {
                log.info("订单{}状态不是105,跳过处理:{},{}",pId,shopId,orderStatus);
                //状态不为105的订单跳过，不处理
                continue;
            }
            //做一个拼接，因为shop_id在获取token时是必要条件->拼接格式为p_id:shop_id
            //并且可以避免在多个抖店商品和多个shopId的情况下频繁的去请求数据库
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(pId);
            stringBuilder.append(":");
            stringBuilder.append(shopId);
            pIdSet.add(stringBuilder.toString());
        }
        //异步发送mq
        DouDianOrderMessage orderMessage = new DouDianOrderMessage();
        orderMessage.setOrderIdSet(pIdSet);
        //todo 根据appID 获取应用参数
        orderMessage.setAppKey("");
        orderMessage.setAppSecret("");

        ShopCommonMessage commonMessage = new ShopCommonMessage();
        commonMessage.setShopType(DouDianConstant.DOU_DIAN);
        commonMessage.setReviewStatus(DouDianConstant.ASYNC_ORDER);
        commonMessage.setMessageData(JSONUtil.toJsonStr(orderMessage));
        douDianManagerService.sendAsyncDouDianMessage(commonMessage, RocketMqConstant.DOUYIN_ORDER_TAG, null);
        //返回获取订阅消息成功
        return DouDianUtil.marshal(new DouDianSuccessResponse(), false);
    }




    /**
     * 解密接口 http 请求抖店云服务
     */
    @Override
    public Map<String, String> batchDecryptCloud(DouDianBatchDecryptRequest douDianBatchDecryptRequest, String shopId, String appKey, String appSecret) throws Exception {
        try {
            DouDianCloudDecryptRequest douDianCloudDecryptRequest = new DouDianCloudDecryptRequest();
            douDianCloudDecryptRequest.setDouDianBatchDecryptRequest(douDianBatchDecryptRequest);
            douDianCloudDecryptRequest.setAccessToken(DouDianAESUtils.encryptStr(douDianManagerService.getAccessToken(shopId, appKey, appSecret), DouDianConstant.AES_KEY));
            douDianCloudDecryptRequest.setAppKey(appKey);
            douDianCloudDecryptRequest.setAppSecret(appSecret);
            String msg = httpClient.postJsonForString(batchDecryptUrl, douDianCloudDecryptRequest, null);
            ResponseEntity responseEntity = JSONObject.parseObject(msg, ResponseEntity.class);
            if (ResponseMessageEnum.SUCCESS.getCode() != responseEntity.getCode()) {
                throw new BizException(responseEntity.getMessage());
            }
            //解密返回数据
            Map<String, String> decryptHashMap = JSONObject.parseObject(DouDianAESUtils.decryptStr(String.valueOf(responseEntity.getData()), DouDianConstant.AES_KEY), Map.class);
            log.info("解密返回数据:{}", JSONObject.toJSONString(decryptHashMap));
            return decryptHashMap;
        } catch (Exception e) {
            throw new BizException("抖店云解密服务请求失败:{}", e.getMessage());
        }

    }


    /**
     * 异步处理逻辑
     *
     * @param
     * @throws Exception
     */
    @Override
    public void asyncOrderHandle(Set<String> orderIdSet, String appKey, String appSecret) throws Exception {
        for (String pId : orderIdSet) {
            try {
                //使用redis对消息进行防重处理，重复订单不处理
                log.info("抖店订单:{}处理开始", pId);
                String key = CacheUtils.generalKey(DouDianConstant.DOUDIAN_ORDER_REPEAT, pId);
                /*Boolean aBoolean = configStringRedisTemplate.opsForValue().setIfAbsent(key, "t", 10, TimeUnit.MINUTES);
                if (Boolean.FALSE.equals(aBoolean)) {
                    //存在相同的订单，跳过不处理
                    log.info("重复订单:{}跳过处理", pId);
                    continue;
                }*/
                //获取抖店订单id和shopId
                String[] split = pId.split(":");
                //获取订单详细信息
                DouDianOrderDetailRequest request = DouDianOrderDetailRequest.builder().shopOrderId(split[0]).build();
                //todo 此处可以考虑前置处理 用于记录抖店相关消息

                String orderDetail = douDianManagerService.orderDetail(request, split[1], appKey, appSecret);
                if (StrUtil.isBlankIfStr(orderDetail)) {
                    log.info("获取订单详情的请求失败，订单id为：{}", pId);
                    throw new BizException("网络错误1,订单详情的请求失败");
                }
                Map<String, Object> orderDetailMap = JSONUtil.toBean(orderDetail, Map.class);
                if (!orderDetailMap.containsKey("code") || !orderDetailMap.containsKey("data")) {
                    log.info("网络错误2,订单详情信息异常，订单id为：{}", pId);
                    throw new BizException("网络错误2,订单详情信息异常");
                }
                String code = DouDianMapUtil.getString(orderDetailMap, "code");
                if (StrUtil.isBlankIfStr(code)) {
                    log.info("网络错误3,订单详情CODE信息异常，订单id为：{}", pId);
                    throw new BizException("网络错误3,订单详情CODE信息异常");
                }
                if (!"10000".equals(code)) {
                    log.info("网络错误4,订单详情CODE:{}信息异常，订单id为：{}", code, pId);
                    throw new BizException("网络错误4,订单详情CODE:{}信息异常", code);
                }
                String data = DouDianMapUtil.getString(orderDetailMap, "data");
                if (StrUtil.isBlankIfStr(data)) {
                    log.info("网络错误5,订单详情data格式异常，订单id为：{}", pId);
                    throw new BizException("网络错误5,订单详情data格式异常");
                }
                Map dataMap = JSONUtil.toBean(data, Map.class);
                if (dataMap == null || !dataMap.containsKey("shop_order_detail")) {
                    log.info("网络错误6,缺少商品订单详情信息，订单id为：{}", pId);
                    throw new BizException("网络错误6,缺少商品订单详情信息");
                }
                String shopOrderDetail = DouDianMapUtil.getString(dataMap, "shop_order_detail");
                if (shopOrderDetail == null) {
                    log.info("网络错误7,shopOrderDetail不允许为空，订单id为：{}", pId);
                    throw new BizException("网络错误7,shopOrderDetail不允许为空");
                }
                //处理订单详细内容，转换为抖店详情返回结果公共类
                DouDianOrderResult douDianOrderResult = DouDianUtil.orderDetailToBean(shopOrderDetail);
                douDianOrderResult.setAppKey(appKey);
                douDianOrderResult.setAppSecret(appSecret);
                //解密
                //构建解密参数
                DouDianBatchDecryptRequest douDianBatchDecryptRequest = new DouDianBatchDecryptRequest();
                douDianBatchDecryptRequest.setCipherInfos(new LinkedList<>());
                //姓名
                CipherInfo cipherInfo1 = new CipherInfo();
                cipherInfo1.setAuthId(douDianOrderResult.getOId());
                cipherInfo1.setCipherText(douDianOrderResult.getName());
                douDianBatchDecryptRequest.getCipherInfos().add(cipherInfo1);
                //身份证号码
                CipherInfo cipherInfo2 = new CipherInfo();
                cipherInfo2.setAuthId(douDianOrderResult.getOId());
                cipherInfo2.setCipherText(douDianOrderResult.getIdCard());
                douDianBatchDecryptRequest.getCipherInfos().add(cipherInfo2);
                //电话号码
                CipherInfo cipherInfo3 = new CipherInfo();
                cipherInfo3.setAuthId(douDianOrderResult.getOId());
                cipherInfo3.setCipherText(douDianOrderResult.getTel());
                douDianBatchDecryptRequest.getCipherInfos().add(cipherInfo3);
                //详细地址
                CipherInfo cipherInfo4 = new CipherInfo();
                cipherInfo4.setAuthId(douDianOrderResult.getOId());
                cipherInfo4.setCipherText(douDianOrderResult.getDetailAdd());
                douDianBatchDecryptRequest.getCipherInfos().add(cipherInfo4);
                //解密请求
                Map<String, String> batchDecryptMap = batchDecryptCloud(douDianBatchDecryptRequest, split[1], appKey, appSecret);
                //更新数据
                douDianOrderResult.setName(batchDecryptMap.get(douDianOrderResult.getName()));
                douDianOrderResult.setIdCard(batchDecryptMap.get(douDianOrderResult.getIdCard()));
                douDianOrderResult.setTel(batchDecryptMap.get(douDianOrderResult.getTel()));
                douDianOrderResult.setDetailAdd(batchDecryptMap.get(douDianOrderResult.getDetailAdd()));
                //调用orderSubmit向第三方提交数据
                douDianOrderResult.setShopId(split[1]);
                orderSubmit(douDianOrderResult);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("抖店订单:{}处理失败,错误原因:{}", pId, e.getMessage());
            }
        }
    }

    /**
     * 获取或删除token
     * 用于获取线上抖店token补偿数据 或清除token缓存重新获取
     *
     * @return
     * @throws Exception
     */
    @Override
    public String getOrDeleteToken(String type, String shopId) throws Exception {
        String key = CacheUtils.generalKey(DouDianConstant.DOUDIAN_TOKEN_KEY, shopId);
        if (type != null && type.equals(BaseConstant.ONE_STRING)) {
            return configStringRedisTemplate.opsForValue().get(key);

        }
        if (type != null && type.equals(BaseConstant.TWO_STRING)) {
            configStringRedisTemplate.delete(key);
        }
        return null;
    }


    /**
     * 抖店数据提交系统
     * 按照抖店订单号一一记录
     * @param
     * @return
     */
    public void orderSubmit(DouDianOrderResult douDianOrderResult) throws Exception {
        log.info("抖店订单{}提交系统流程开始", douDianOrderResult.getOId());
        //todo 此处进行提交系统订单
    }














}
