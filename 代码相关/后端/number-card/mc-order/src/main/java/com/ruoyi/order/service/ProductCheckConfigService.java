package com.ruoyi.order.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.ProductCheckConfigAddBO;
import com.ruoyi.common.order.bo.ProductCheckConfigUpdateBO;
import com.ruoyi.common.order.bo.ProductCheckSelectBO;
import com.ruoyi.common.order.entity.ProductCheckConfig;

import java.util.List;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/17 17:11
 */
public interface ProductCheckConfigService extends IService<ProductCheckConfig> {


    /**
     * 根据产品Code 获取产品校验信息
     *
     * @param
     * @return
     */
    List<ProductCheckConfig> getProductCheckConfigList(String productCode,Integer checkConfigType) throws BizException;

}
