package com.ruoyi.console.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.*;
import com.ruoyi.common.order.entity.ProductCheckConfig;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/17 17:11
 */
public interface ProductCheckConfigService extends IService<ProductCheckConfig> {

    /**
     * 分页查询产品校验配置列表
     *
     * @return
     * @throws BizException
     */
     PageResult<ProductCheckConfig> selectProductCheckConfigListPage(ProductCheckSelectBO productCheckSelectBO) throws BizException;


    /**
     * 新增产品校验配置
     *
     * @return
     * @throws BizException
     */
    void addProductCheckConfig(ProductCheckConfigAddBO productCheckConfigAddBO) throws BizException;


    /**
     * 新增产品校验配置
     *
     * @return
     * @throws BizException
     */
     void updateProductCheckConfig(ProductCheckConfigUpdateBO productCheckConfigUpdateBO) throws BizException;


    /**
     * 删除产品校验配置
     */
     void deleteProductCheckConfig(Integer productCheckConfigId);

}
