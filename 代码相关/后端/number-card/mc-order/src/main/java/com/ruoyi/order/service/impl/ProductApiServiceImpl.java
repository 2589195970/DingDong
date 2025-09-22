package com.ruoyi.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.order.mapper.UpstreamProductMapper;
import com.ruoyi.order.service.BaseService;
import com.ruoyi.order.service.ProductApiService;
import com.ruoyi.order.service.ProductService;
import com.ruoyi.order.service.UpstreamApiService;
import com.ruoyi.order.utils.SnowflakeUtil;
import com.ruoyi.order.utils.SpringContextUtil;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.order.entity.Order;
import com.ruoyi.common.order.entity.Product;
import com.ruoyi.common.order.entity.UpstreamApi;
import com.ruoyi.common.order.entity.UpstreamProduct;
import com.ruoyi.common.order.reuqest.APISubmitInfoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Type;


/**
 * 产品所属上游API调用相关
 */
@Slf4j
@Service
public class ProductApiServiceImpl extends ServiceImpl<UpstreamProductMapper, UpstreamProduct> implements ProductApiService {

    @Resource(name = "configStringRedisTemplate")
    protected StringRedisTemplate configStringRedisTemplate;

    @Resource
    SnowflakeUtil snowflakeUtil;

    @Resource
    UpstreamApiService upstreamApiService;

    @Resource
    ProductService productService;


    /**
     * 根据 API类型 提交订单
     *
     * @return
     */
    public Order submitAPISubmitOrder(APISubmitInfoRequest apiSubmitInfoRequest) throws Exception {
        return submitAPISubmitOrder(snowflakeUtil.getNextId(), apiSubmitInfoRequest);
    }

    /**
     * 根据 API类型 提交订单
     *
     * @return
     */
    public Order submitAPISubmitOrder(Long orderId, APISubmitInfoRequest apiSubmitInfoRequest) throws Exception {
        //获取产品信息
        Product product = productService.getProduct(apiSubmitInfoRequest.getProductCode());
        //获取上游API信息
        UpstreamApi upstreamApi = upstreamApiService.getUpstreamApi(product.getUpstreamApiCode());
        BaseService baseService = SpringContextUtil.getBean(upstreamApi.getInterfaceClassName());
        Order order = null;
        if(upstreamApi.getIsAsync() == null|| upstreamApi.getIsAsync() == BaseConstant.ZERO_INT){
            order = baseService.syncSubmitOrder(orderId, JSONObject.parseObject(JSONObject.toJSONString(apiSubmitInfoRequest), (Type) Class.forName(upstreamApi.getParameterClassName())),product,upstreamApi);
        }
        return order;
    }

}
