package com.ruoyi.order.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.*;
import com.ruoyi.common.order.entity.Product;
import com.ruoyi.common.order.vo.ProductH5VO;
import com.ruoyi.common.order.vo.ProductListVO;
import com.ruoyi.common.order.vo.ProductSelectVO;

import java.util.List;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/17 17:11
 */
public interface ProductService extends IService<Product> {


     /**
      * 根据Code查询产品信息
      *
      * @param
      * @return
      */
     Product getProduct(String productCode) throws BizException;


     /**
      * 获取代理商产品信息
      *
      * @param
      * @return
      */
     AgentProductBO getAgentProductBO(String productCode, String agentCode) throws BizException;


     /**
      * 根据Code查询产品H5信息
      *
      * @param
      * @return
      */
     ProductH5VO getProductH5(ProductH5BO productH5BO) throws BizException;



     /**
      * 根据代理商code 获取产品列表
      *
      * @param
      * @return
      */
     List<ProductListVO> getAgentProductList(ProductListBO productListBO) throws BizException;


}
