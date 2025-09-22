package com.ruoyi.console.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.PageResultFactory;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.*;
import com.ruoyi.common.order.entity.Order;
import com.ruoyi.common.order.entity.Product;
import com.ruoyi.common.order.entity.ProductCheckConfig;
import com.ruoyi.console.mapper.ProductCheckConfigMapper;
import com.ruoyi.console.service.ProductCheckConfigService;
import com.ruoyi.console.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;


/**
 * 产品校验配置相关接口
 *
 * @Description
 * @Author 陈思伟
 * @Date 2025/01/07 17:13
 */
@Service
public class ProductCheckConfigServiceImpl extends ServiceImpl<ProductCheckConfigMapper, ProductCheckConfig> implements ProductCheckConfigService {


    @Resource
    ProductService productService;

    /**
     * 分页查询产品校验配置列表
     *
     * @return
     * @throws BizException
     */
    @Override
    public PageResult<ProductCheckConfig> selectProductCheckConfigListPage(ProductCheckSelectBO productCheckSelectBO) throws BizException {
        //读取分页
        Page page = new Page(productCheckSelectBO.getPageNo(), productCheckSelectBO.getPageSize());
        Page<ProductCheckConfig> productCheckConfigPage = baseMapper.selectPage(page, new LambdaQueryWrapper<ProductCheckConfig>()
                .eq(ProductCheckConfig::getProductCode, productCheckSelectBO.getProductCode())
        );
        return PageResultFactory.createPageResult(productCheckConfigPage);
    }


    /**
     * 新增产品校验配置
     *
     * @return
     * @throws BizException
     */
    @Override
    public void addProductCheckConfig(ProductCheckConfigAddBO productCheckConfigAddBO) throws BizException {
        Product product = productService.getProduct(productCheckConfigAddBO.getProductCode());
        ProductCheckConfig productCheckConfig = new ProductCheckConfig();
        BeanUtil.copyProperties(productCheckConfigAddBO, productCheckConfig);
        //传入产品相关信息
        productCheckConfig.setProductCode(product.getProductCode());
        productCheckConfig.setProductName(product.getProductName());
        productCheckConfig.setProductId(product.getProductId());
        //列表转json
        productCheckConfig.setTerritoryProvinceJson(!CollectionUtils.isEmpty(productCheckConfigAddBO.getTerritoryProvinceList()) ?
                JSONObject.toJSONString(productCheckConfigAddBO.getTerritoryProvinceList()) : null);
        productCheckConfig.setTerritoryCityJson(!CollectionUtils.isEmpty(productCheckConfigAddBO.getTerritoryCityList()) ?
                JSONObject.toJSONString(productCheckConfigAddBO.getTerritoryCityList()) : null);

        productCheckConfig.setCreateTime(System.currentTimeMillis());
        baseMapper.insert(productCheckConfig);
    }

    /**
     * 修改产品校验配置
     *
     * @return
     * @throws BizException
     */
    @Override
    public void updateProductCheckConfig(ProductCheckConfigUpdateBO productCheckConfigUpdateBO) throws BizException {
        ProductCheckConfig productCheckConfig = baseMapper.selectById(productCheckConfigUpdateBO.getProductCheckConfigId());
        if(productCheckConfig == null){
            throw new BizException("产品校验配置ID不存在:{}",productCheckConfigUpdateBO.getProductCheckConfigId());
        }
        productCheckConfig.setTerritoryProvinceJson(!CollectionUtils.isEmpty(productCheckConfigUpdateBO.getTerritoryProvinceList()) ?
                JSONObject.toJSONString(productCheckConfigUpdateBO.getTerritoryProvinceList()) : null);
        productCheckConfig.setTerritoryCityJson(!CollectionUtils.isEmpty(productCheckConfigUpdateBO.getTerritoryCityList()) ?
                JSONObject.toJSONString(productCheckConfigUpdateBO.getTerritoryCityList()) : null);
        productCheckConfig.setAgeMax(productCheckConfigUpdateBO.getAgeMax()!=null?productCheckConfigUpdateBO.getAgeMax():productCheckConfig.getAgeMax());
        productCheckConfig.setAgeMin(productCheckConfigUpdateBO.getAgeMin()!=null?productCheckConfigUpdateBO.getAgeMin():productCheckConfig.getAgeMin());
        productCheckConfig.setUpdateTime(System.currentTimeMillis());
        baseMapper.updateById(productCheckConfig);
    }


    /**
     * 删除产品校验配置记录
     */
    public void deleteProductCheckConfig(Integer productCheckConfigId){
        baseMapper.deleteById(productCheckConfigId);
    }
}
