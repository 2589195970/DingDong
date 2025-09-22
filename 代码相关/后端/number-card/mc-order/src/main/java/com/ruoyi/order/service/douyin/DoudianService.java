package com.ruoyi.order.service.douyin;

import com.ruoyi.common.apis.douyin.DouDianBatchDecryptRequest;
import com.ruoyi.common.apis.douyin.DouDianOrderDetailRequest;
import com.ruoyi.common.apis.douyin.DouDianOrderResult;

import java.util.Map;
import java.util.Set;

/**
 * 抖店相关方法
 *
 * @author 陈思伟
 */
public interface DoudianService {


    /**
     * 抖店订阅消息接口
     *
     * @param eventSign
     * @param appId
     * @param body
     * @return
     * @throws Exception
     */
    String receiveMessage(String eventSign, String appId, String body) throws Exception;



    /**
     * 批量解密接口
     * 此接口通过 http 抖店云服务实现
     *
     * @param douDianBatchDecryptRequest
     * @param shopId
     * @return
     */
    Map<String, String> batchDecryptCloud(DouDianBatchDecryptRequest douDianBatchDecryptRequest, String shopId, String appKey, String appSecret) throws Exception;

    /**
     * 获取订单详情之后的异步处理逻辑
     *
     * @param orderIdSet
     * @throws Exception
     */
    void asyncOrderHandle(Set<String> orderIdSet, String appKey, String appSecret) throws Exception;


    /**
     * 获取或删除token
     * 用于获取线上抖店token补偿数据
     * 或清除token缓存重新获取
     *
     * @return
     * @throws Exception
     */
    String getOrDeleteToken(String type, String shopId) throws Exception;


    /**
     * 订单提交到运营商系统
     *
     * @param douDianOrderResult
     */
    void orderSubmit(DouDianOrderResult douDianOrderResult) throws Exception;


}
