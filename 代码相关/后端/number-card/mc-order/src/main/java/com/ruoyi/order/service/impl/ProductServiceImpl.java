package com.ruoyi.order.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.enums.ProductEnum;
import com.ruoyi.common.order.bo.ProductH5BO;
import com.ruoyi.common.order.bo.ProductListBO;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.order.vo.ProductH5VO;
import com.ruoyi.common.order.vo.ProductListVO;
import com.ruoyi.order.mapper.AgentProductMapper;
import com.ruoyi.order.mapper.ProductMapper;
import com.ruoyi.order.service.AgentService;
import com.ruoyi.order.service.ProductService;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.constant.CacheKeyConstants;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.AgentProductBO;
import com.ruoyi.common.order.entity.Product;
import com.ruoyi.common.utils.CacheUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/12 17:13
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Resource(name = "configStringRedisTemplate")
    protected StringRedisTemplate configStringRedisTemplate;

    @Resource
    AgentService agentService;

    @Resource
    AgentProductMapper agentProductMapper;

    @Value(value = "${submit.h5}")
    private String h5url;

    /**
     * 根据Code查询产品信息
     *
     * @param
     * @return
     */
    @Override
    public Product getProduct(String productCode) throws BizException {
        if (StrUtil.isBlankIfStr(productCode)) {
            throw new BizException("product code不存在:{}", productCode);
        }
        Product product;
        String cacheKey = CacheUtils.generalKey(CacheKeyConstants.PRODUCT_API, productCode);
        String json = configStringRedisTemplate.opsForValue().get(cacheKey);
        if (StrUtil.isBlankIfStr(json)) {
            product = baseMapper.selectOne(new LambdaQueryWrapper<Product>().eq(Product::getProductCode, productCode));
            if (product == null) {
                throw new BizException("产品不存在:{}", productCode);
            }
            configStringRedisTemplate.opsForValue().set(cacheKey, JSONObject.toJSONString(product), 10, TimeUnit.MINUTES);
        } else {
            product = JSONObject.parseObject(json, Product.class);
        }
        return product;
    }

    /**
     * 获取代理商产品信息
     *
     * @param
     * @return
     */
    public AgentProductBO getAgentProductBO(String productCode,String agentCode) throws BizException {
        if (StrUtil.isBlankIfStr(productCode)||StrUtil.isBlankIfStr(agentCode)) {
            throw new BizException("productCode或agentCode不存在:{}", productCode);
        }
        AgentProductBO agentProductBO;
        String cacheKey = CacheUtils.generalKey(CacheKeyConstants.AGENT_PRODUCT_API, productCode,agentCode);
        String json = configStringRedisTemplate.opsForValue().get(cacheKey);
        if (StrUtil.isBlankIfStr(json)) {
            if(agentService.isAdminAgent(agentCode)){
                agentProductBO = baseMapper.selectAdminAgentProduct(productCode);
                AgentAccount agentAccount =agentService.getAgentAccountByCode(agentCode);
                agentProductBO.setAgentCode(agentAccount.getAgentCode());
                agentProductBO.setAgentName(agentAccount.getAgentName());
                agentProductBO.setParentAgentList(agentAccount.getParentAgentList());
            }else {
                agentProductBO = baseMapper.selectAgentProduct(productCode,agentCode);
            }
            if (agentProductBO == null) {
                throw new BizException("代理商产品不存在:{}", productCode);
            }
            if(agentProductBO.getProductStatus()== BaseConstant.ZERO_INT){
                throw new BizException("产品{}已下架", productCode);
            }
            configStringRedisTemplate.opsForValue().set(cacheKey, JSONObject.toJSONString(agentProductBO), 10, TimeUnit.MINUTES);
        } else {
            agentProductBO = JSONObject.parseObject(json, AgentProductBO.class);
        }
        return agentProductBO;
    }


    /**
     * 根据Code查询产品H5信息
     *
     * @param
     * @return
     */
    @Override
    public ProductH5VO getProductH5(ProductH5BO productH5BO) throws BizException {
        if (productH5BO==null||StrUtil.isBlankIfStr(productH5BO.getProductCode())) {
            throw new BizException("product code不存在:{}", productH5BO);
        }
        ProductH5VO productH5VO;
        String cacheKey = CacheUtils.generalKey(CacheKeyConstants.PRODUCT_H5_API, productH5BO.getProductCode());
        String json = configStringRedisTemplate.opsForValue().get(cacheKey);
        if (StrUtil.isBlankIfStr(json)) {
            Product product = getProduct(productH5BO.getProductCode());
            if (product == null) {
                throw new BizException("产品不存在:{}", productH5BO.getProductCode());
            }
            if (product.getProductStatus() == ProductEnum.ProductStatusEnum.NO.getStatus()) {
                throw new BizException("产品已下架:{}", product.getProductName());
            }
            productH5VO = new ProductH5VO();
            BeanUtil.copyProperties(product,productH5VO);
            configStringRedisTemplate.opsForValue().set(cacheKey, JSONObject.toJSONString(productH5VO), 10, TimeUnit.MINUTES);
        } else {
            productH5VO = JSONObject.parseObject(json, ProductH5VO.class);
        }
        return productH5VO;
    }



    /**
     * 根据代理商code 获取产品列表
     *
     * @param
     * @return
     */
    @Override
    public List<ProductListVO> getAgentProductList(ProductListBO productListBO) throws BizException {
        if (productListBO==null||StrUtil.isBlankIfStr(productListBO.getAgentCode())) {
            throw new BizException("代理商code不存在:{}", productListBO.getAgentCode());
        }
        List<ProductListVO> productListVOList;
        String cacheKey = CacheUtils.generalKey(CacheKeyConstants.AGENT_PRODUCT_lIST_API, productListBO.getAgentCode(),productListBO.getOperatorType());
        String json = configStringRedisTemplate.opsForValue().get(cacheKey);
        if (StrUtil.isBlankIfStr(json)) {
            if(agentService.isAdminAgent(productListBO.getAgentCode())){
                AgentAccount agentAccount = agentService.getAgentAccountByCode(productListBO.getAgentCode());
                productListVOList = agentProductMapper.selectAdminAgentProductList(productListBO.getOperatorType());
                for (ProductListVO productListVO:productListVOList){
                    productListVO.setAgentCode(agentAccount.getAgentCode());
                    agentAccount.setAgentName(agentAccount.getAgentName());
                }
            }else {
                productListVOList = agentProductMapper.selectAgentProductList(productListBO.getAgentCode(),productListBO.getOperatorType());
            }

            if (CollectionUtils.isEmpty(productListVOList)) {
               return new ArrayList<>();
            }
            //拼接h5落地页链接
            for (ProductListVO productListVO:productListVOList){
                StringBuffer stringBuffer = new StringBuffer(h5url);
                stringBuffer.append("?productCode=").append(productListVO.getProductCode()).append("&agentCode=").append(productListVO.getAgentCode());
                productListVO.setH5Url(stringBuffer.toString());
            }
            //configStringRedisTemplate.opsForValue().set(cacheKey, JSONObject.toJSONString(productListVOList), 10, TimeUnit.MINUTES);
        } else {
            productListVOList = JSONObject.parseArray(json, ProductListVO.class);
        }
        return productListVOList;
    }

}
