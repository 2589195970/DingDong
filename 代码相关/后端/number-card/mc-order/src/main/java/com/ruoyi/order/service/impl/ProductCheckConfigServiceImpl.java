package com.ruoyi.order.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.CacheKeyConstants;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.PageResultFactory;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.ProductCheckConfigAddBO;
import com.ruoyi.common.order.bo.ProductCheckConfigUpdateBO;
import com.ruoyi.common.order.bo.ProductCheckSelectBO;
import com.ruoyi.common.order.entity.Product;
import com.ruoyi.common.order.entity.ProductCheckConfig;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.order.mapper.ProductCheckConfigMapper;
import com.ruoyi.order.service.AgentService;
import com.ruoyi.order.service.ProductCheckConfigService;
import com.ruoyi.order.service.ProductService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * 产品校验配置相关接口
 *
 * @Description
 * @Author 陈思伟
 * @Date 2025/01/07 17:13
 */
@Service
public class ProductCheckConfigServiceImpl extends ServiceImpl<ProductCheckConfigMapper, ProductCheckConfig> implements ProductCheckConfigService {

    @Resource(name = "configStringRedisTemplate")
    protected StringRedisTemplate configStringRedisTemplate;


    /**
     * 根据产品Code 获取产品校验信息
     *
     * @param
     * @return
     */
    @Override
    public List<ProductCheckConfig> getProductCheckConfigList(String productCode,Integer checkConfigType) throws BizException {
        if (StrUtil.isBlankIfStr(productCode)) {
            throw new BizException("product code不存在:{}", productCode);
        }
        List<ProductCheckConfig> productCheckConfigList;
        String cacheKey = CacheUtils.generalKey(CacheKeyConstants.PRODUCT_CHECK_CONFIG, productCode,checkConfigType);
        String json = configStringRedisTemplate.opsForValue().get(cacheKey);
        if (StrUtil.isBlankIfStr(json)) {
            productCheckConfigList = baseMapper.selectList(new LambdaQueryWrapper<ProductCheckConfig>().eq(ProductCheckConfig::getProductCode, productCode)
                    .eq(ProductCheckConfig::getCheckConfigType, checkConfigType));
            if (CollectionUtils.isEmpty(productCheckConfigList)) {
                return new ArrayList<>();
            }
            configStringRedisTemplate.opsForValue().set(cacheKey, JSONObject.toJSONString(productCheckConfigList), 10, TimeUnit.MINUTES);
        } else {
            productCheckConfigList = JSONObject.parseArray(json, ProductCheckConfig.class);
        }
        return productCheckConfigList;
    }


}
