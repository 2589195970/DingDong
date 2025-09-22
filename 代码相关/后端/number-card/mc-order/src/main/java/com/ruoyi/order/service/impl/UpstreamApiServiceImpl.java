package com.ruoyi.order.service.impl;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.order.mapper.UpstreamApiMapper;
import com.ruoyi.order.mapper.UpstreamProductMapper;
import com.ruoyi.order.service.UpstreamApiService;
import com.ruoyi.common.constant.CacheKeyConstants;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.entity.UpstreamApi;
import com.ruoyi.common.order.entity.UpstreamInfo;
import com.ruoyi.common.order.entity.UpstreamProduct;
import com.ruoyi.common.utils.CacheUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/19 17:13
 */
@Service
public class UpstreamApiServiceImpl extends ServiceImpl<UpstreamApiMapper, UpstreamApi> implements UpstreamApiService {

    @Resource(name = "configStringRedisTemplate")
    protected StringRedisTemplate configStringRedisTemplate;

    @Resource
    UpstreamProductMapper upstreamProductMapper;

    /**
     * 根据上游API Code查询
     *
     * @param
     * @return
     */
    @Override
    public UpstreamApi getUpstreamApi(String upstreamApiCode) throws BizException {
        if (StrUtil.isBlankIfStr(upstreamApiCode)) {
            throw new BizException("上游API CODE不存在:{}", upstreamApiCode);
        }
        UpstreamApi upstreamApi;
        String cacheKey = CacheUtils.generalKey(CacheKeyConstants.UPSTREAM_PRODUCT_API, upstreamApiCode);
        String json = configStringRedisTemplate.opsForValue().get(cacheKey);
        if (StrUtil.isBlankIfStr(json)) {
            upstreamApi = baseMapper.selectOne(new LambdaQueryWrapper<UpstreamApi>().eq(UpstreamApi::getUpstreamApiCode, upstreamApiCode));
            if (upstreamApi == null) {
                throw new BizException("上游API不存在:{}", upstreamApiCode);
            }
            configStringRedisTemplate.opsForValue().set(cacheKey, JSONObject.toJSONString(upstreamApi), 10, TimeUnit.MINUTES);
        } else {
            upstreamApi = JSONObject.parseObject(json, UpstreamApi.class);
        }
        return upstreamApi;
    }

    /**
     * 根据上游API和条件获取上游产品信息
     *
     * @param
     * @return
     */
    @Override
    public UpstreamInfo getUpstreamInfo(String upstreamApiCode,String upstreamProductCode) throws BizException {
        if (StrUtil.isBlankIfStr(upstreamApiCode)||StrUtil.isBlankIfStr(upstreamProductCode)) {
            throw new BizException("上游APICODE或上游产品CODE不存在:{}", upstreamApiCode,upstreamProductCode);
        }
        UpstreamInfo upstreamInfo = null;
        String cacheKey = CacheUtils.generalKey(CacheKeyConstants.UPSTREAM_PRODUCT_API_PRODUCT, upstreamApiCode,upstreamProductCode);
        String json = configStringRedisTemplate.opsForValue().get(cacheKey);
        if (StrUtil.isBlankIfStr(json)) {
            UpstreamApi upstreamApi = getUpstreamApi(upstreamApiCode);
            UpstreamProduct upstreamProduct = upstreamProductMapper.selectOne(new LambdaQueryWrapper<UpstreamProduct>().eq(UpstreamProduct::getUpstreamApiCode, upstreamApiCode)
                    .eq(UpstreamProduct::getUpstreamProductCode, upstreamProductCode));
            if (upstreamProduct == null) {
                throw new BizException("上游产品CODE:{}不存在", upstreamProductCode);
            }
            upstreamInfo = new UpstreamInfo();
            upstreamInfo.setUpstreamApi(upstreamApi);
            upstreamInfo.setUpstreamProduct(upstreamProduct);
            configStringRedisTemplate.opsForValue().set(cacheKey, JSONObject.toJSONString(upstreamInfo), 10, TimeUnit.MINUTES);
        } else {
            upstreamInfo = JSONObject.parseObject(json, UpstreamInfo.class);
        }
        return upstreamInfo;
    }


}
