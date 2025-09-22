package com.ruoyi.order.service.douyin;


import com.ruoyi.common.apis.douyin.DouDianLogisticsAddRequest;
import com.ruoyi.common.apis.douyin.DouDianOrderDetailRequest;
import com.ruoyi.common.apis.douyin.DouDianOrderReviewRequest;

/**
 * @author 陈思伟
 * @version 1.0
 * @description: 抖店结果和发货回传通用逻辑层，为避免循环依赖，所以抽取出该层
 * @date 2025/7/16 14:32
 */
public interface DouDianManagerService {


    /**
     * 查询订单详情
     *
     * @param request
     * @throws Exception
     */
    String orderDetail(DouDianOrderDetailRequest request, String shopId, String appKey, String appSecret) throws Exception;

    /**
     * 抖店获取token
     *
     * @return
     * @throws Exception
     */
    String getAccessToken(String shopId, String appKey, String appSecret) throws Exception;

    /**
     * 订单发送第三方之后的结果回传
     *
     * @param request
     * @throws Exception
     */
    void orderReview(DouDianOrderReviewRequest request, String shopId, String appKey, String appSecret) throws Exception;

    /**
     * 发货信息提交接口
     *
     * @param request
     * @throws Exception
     */
    void logisticsAdd(DouDianLogisticsAddRequest request, String shopId, String appKey, String appSecret) throws Exception;

    /**
     * 异步发送MQ
     *
     * @param orderMessage
     * @param tag
     */
    void sendAsyncDouDianMessage(Object orderMessage, String tag, Long delayTime);
}
