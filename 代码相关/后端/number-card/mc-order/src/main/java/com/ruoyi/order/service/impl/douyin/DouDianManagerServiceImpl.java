package com.ruoyi.order.service.impl.douyin;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.*;

import com.ruoyi.common.apis.douyin.DouDianCreateTokenRequest;
import com.ruoyi.common.apis.douyin.DouDianLogisticsAddRequest;
import com.ruoyi.common.apis.douyin.DouDianOrderDetailRequest;
import com.ruoyi.common.apis.douyin.DouDianOrderReviewRequest;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.constant.DouDianConstant;
import com.ruoyi.common.constant.RocketMqConstant;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.http.HttpClient;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.common.utils.DBUtils;
import com.ruoyi.common.utils.RocketMqUtils;
import com.ruoyi.order.service.douyin.DouDianManagerService;
import com.ruoyi.order.utils.douyin.DouDianMapUtil;
import com.ruoyi.order.utils.douyin.DouDianUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.ruoyi.order.utils.douyin.DouDianUtil.marshal;

/**
 * @author 陈思伟
 * @version 1.0
 * @description: 抖店结果和发货回传通用逻辑层实现
 * @date 2025/7/16 14:34
 */
@Service
@Slf4j
public class DouDianManagerServiceImpl implements DouDianManagerService {

    @Resource
    private HttpClient httpClient;

    @Resource(name = "configStringRedisTemplate")
    private StringRedisTemplate configStringRedisTemplate;


    @Resource
    private Producer producer;



    /**
     * 查询订单详情
     *
     * @param request
     * @throws Exception
     */
    @Override
    public String orderDetail(DouDianOrderDetailRequest request, String shopId, String appKey, String appSecret) throws Exception {
        String date = DBUtils.getTableNameByDate(new DateTime(), "yyyy-MM-dd HH:mm:ss");
        //获取token
        String accessToken = getAccessToken(shopId, appKey, appSecret);
        if (accessToken == null) {
            throw new BizException("无效token");
        }
        //参数序列化
        String paramJson = DouDianUtil.marshal(request, true);
        log.info("订单详情参数{}", paramJson);
        //构建签名
        String sign = DouDianUtil.sign(appKey, appSecret, DouDianConstant.DOUDIAN_ORDER_DETAIL,date, paramJson);
        //构建url
        String url = DouDianConstant.DOUDIAN_URL + DouDianConstant.DOUDIAN_ORDER_DETAIL_ADD +
                "?method=" + DouDianConstant.DOUDIAN_ORDER_DETAIL + "&app_key=" + appKey +
                "&timestamp=" + date +
                "&v=" + DouDianConstant.APP_V + "&sign=" + sign + "&sign_method=" + DouDianConstant.SIGN_METHOD +
                "&access_token=" + accessToken;
        //请求接口
        Map<String, Object> header = new HashMap<>(1);
        header.put("Content-Type", "application/json");
        log.info("订单详情查询已提交：{}", request);
        String response = httpClient.postJsonStringForString(url, paramJson, header);
        log.info("订单详情查询返回{}", response);
        //返回解析类
        return response;
    }


    /**
     * 获取抖店accessToken的参数的方法，每次请求都需要调用
     */
    @Override
    public String getAccessToken(String shopId, String appKey, String appSecret) throws Exception {
        //获取当前时间
        String date = DBUtils.getTableNameByDate(new DateTime(), "yyyy-MM-dd HH:mm:ss");
        //首先从Redis中获取token,找到就返回
        String accessToken = "";
        String key = CacheUtils.generalKey(DouDianConstant.DOUDIAN_TOKEN_KEY, shopId);
        accessToken = configStringRedisTemplate.opsForValue().get(key);
        if (accessToken != null) {
            return accessToken;
        }
        //构造请求token参数
        DouDianCreateTokenRequest createTokenRequest = DouDianCreateTokenRequest.builder() .code("").grantType("authorization_self").shopId(shopId).build();
        //对参数进行序列化
        String paramJson = DouDianUtil.marshal(createTokenRequest, true);
        //构建签名(加密已经在此完成)，判断是否成功
        String sign = DouDianUtil.sign(appKey, appSecret, DouDianConstant.DOUDIAN_TOKEN_CREATE, date, paramJson);
        if (!StringUtils.hasLength(sign)) {
            throw new BizException("网络错误，请重试");
        }
        //构建URL
        String url = DouDianUtil.urlBuild(DouDianConstant.DOUDIAN_URL, DouDianConstant.DOUDIAN_TOKEN_CREATE_ADD, DouDianConstant.DOUDIAN_TOKEN_CREATE,
                appKey, date, DouDianConstant.APP_V, sign, DouDianConstant.SIGN_METHOD, null);
        //发起请求
        log.info("抖店请求token参数{}", JSONObject.toJSONString(paramJson));
        Map<String, Object> tokenResultMap = httpClient.postJsonStringForMap(url, paramJson, null);
        log.info("抖店请求token响应结果{}", tokenResultMap.toString());
        //处理响应数据，只有错误码为0的数据才是有效的
        if (tokenResultMap.isEmpty() || !tokenResultMap.containsKey("code") || !tokenResultMap.containsKey("data")) {
            log.info("{}", "抖店获取token请求失败");
            throw new BizException("抖店获取token请求失败,请重试");
        }
        if ("10000".equals(DouDianMapUtil.getString(tokenResultMap, "code"))) {
            //获取拿到的token
            Map<String, Object> data = JSONUtil.toBean(DouDianMapUtil.getString(tokenResultMap, "data"), Map.class);
            if (data.containsKey("access_token") && data.get("access_token") != null) {
                accessToken = DouDianMapUtil.getString(data, "access_token");
                if (StringUtils.hasLength(accessToken)) {
                    //获取过期时间
                    Integer expiresIn = data.get("expires_in") != null ? Integer.valueOf(String.valueOf(data.get("expires_in"))) : null;
                    if (expiresIn != null) {
                        //获取返回的token，并且存入缓存
                        configStringRedisTemplate.opsForValue().set(key, accessToken, expiresIn, TimeUnit.SECONDS);
                    }
                    return accessToken;
                }
            }
        }
        log.info("{}", "token返回数据异常,无效数据");
        throw new BizException("网络错误，请重试");
    }

    /**
     * 商家回传订单审核结果
     *
     * @param request
     * @throws Exception
     */
    @Override
    public void orderReview(DouDianOrderReviewRequest request, String shopId, String appKey, String appSecret) throws Exception {
        String date = DBUtils.getTableNameByDate(new DateTime(), "yyyy-MM-dd HH:mm:ss");
        //获取token
        String accessToken = getAccessToken(shopId, appKey, appSecret);
        if (accessToken == null) {
            throw new BizException("无效token");
        }
        //参数序列化
        String paramJson = marshal(request, true);
        log.info("订单审核参数{}", paramJson);
        //构建签名
        String sign = DouDianUtil.sign(appKey, appSecret, DouDianConstant.DOUDIAN_ORDER_REVIEW,
                date, paramJson);
        //构建url
        String url = DouDianUtil.urlBuild(DouDianConstant.DOUDIAN_URL, DouDianConstant.DOUDIAN_ORDER_REVIEW_ADD, DouDianConstant.DOUDIAN_ORDER_REVIEW, appKey,
                date, DouDianConstant.APP_V, sign, DouDianConstant.SIGN_METHOD, accessToken);
        //请求接口
        log.info("商家回传订单审核结果已提交：{}", request);
        String response = httpClient.postJsonStringForString(url, paramJson, null);
        log.info("商家回传订单审核结果返回{}", response);
    }

    /**
     * 抖店订单发货接口
     *
     * @param request
     * @throws Exception
     */
    @Override
    public void logisticsAdd(DouDianLogisticsAddRequest request, String shopId, String appKey, String appSecret) throws Exception {
        String date = DBUtils.getTableNameByDate(new DateTime(), "yyyy-MM-dd HH:mm:ss");
        //获取token
        String accessToken = getAccessToken(shopId, appKey, appSecret);
        if (accessToken == null) {
            throw new BizException("无效token");
        }
        //参数序列化
        String paramJson = marshal(request, true);
        log.info("发货回传参数{}", paramJson);
        //构建签名
        String sign = DouDianUtil.sign(appKey, appSecret, DouDianConstant.DOUDIAN_ORDER_LOGISTICSADD,date, paramJson);
        //构建url
        String url = DouDianUtil.urlBuild(DouDianConstant.DOUDIAN_URL, DouDianConstant.DOUDIAN_ORDER_LOGISTICSADD_ADD, DouDianConstant.DOUDIAN_ORDER_LOGISTICSADD, appKey,
                date, DouDianConstant.APP_V, sign, DouDianConstant.SIGN_METHOD, accessToken);
        //请求接口
        String id = UUID.fastUUID().toString();
        log.info("商家回传发货已提交：{},{}",id, request);
        String response = httpClient.postJsonStringForString(url, paramJson, null);
        log.info("商家回传发货返回:{},{}", id,response);
        if (StringUtils.hasLength(response)) {
            //部分订单发货会产生创建履约单失败错误 重新提交后可成功 尝试重复提交一次 再次失败手工发货处理
            Map<String, Object> map = JSONObject.parseObject(response, Map.class);
            if (map != null && map.get("code") != null && !String.valueOf(map.get("code")).equals("10000")) {
                httpClient.postJsonStringForString(url, paramJson, null);
            }
        }
    }



    /**
     * 抖店订单异步 MQ发送
     *
     * @param orderMessage
     * @throws Exception
     */
    @Override
    public void sendAsyncDouDianMessage(Object orderMessage, String tag, Long delayTime) {
        Message message = RocketMqUtils.createMessage(RocketMqConstant.ORDER_TOPIC, tag, orderMessage);
        if (delayTime != null && delayTime > BaseConstant.ZERO_INT) {
            message.setStartDeliverTime(System.currentTimeMillis() + delayTime);
        }
        producer.sendAsync(message, new SendCallback() {
            @Override
            public void onSuccess(final SendResult sendResult) {
                // 消息发送成功
            }

            @Override
            public void onException(OnExceptionContext context) {
                // 消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理
                log.error("发送消息失败：topic = {} ,msgId = {}", context.getTopic(), context.getMessageId());
            }
        });
        // 在 callback 返回之前即可取得 msgId
        log.info("发送消息,类型：topic = {} ,msgId = {} ,obj = {}", message.getTopic(), message.getMsgID(), orderMessage);
    }

}
