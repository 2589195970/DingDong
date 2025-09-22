package com.ruoyi.console.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.ProductAddAndUpdateBO;
import com.ruoyi.common.order.bo.ProductCopyBO;
import com.ruoyi.common.order.bo.ProductSelectBO;
import com.ruoyi.common.order.bo.ProductUpdateStatusBO;
import com.ruoyi.common.order.entity.Product;
import com.ruoyi.common.order.vo.ProductSelectVO;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/17 17:11
 */
public interface ProductService extends IService<Product> {

     /**
      * 分页查询产品列表
      * @return
      * @throws BizException
      */
     PageResult<ProductSelectVO> selectProductListPage(ProductSelectBO productSelectBO,LoginUser loginUser) throws BizException;

     /**
      *
      * @return
      * @throws BizException
      */
     void addProduct(ProductAddAndUpdateBO productAddAndUpdateBO,LoginUser loginUser) throws BizException;

     /**
      * 复制产品产品
      * @return
      * @throws BizException
      */
     void copyProduct(ProductCopyBO productCopyBO,LoginUser loginUser) throws BizException;

     /**
      * 更新产品
      */
     void updateProduct(ProductAddAndUpdateBO productAddAndUpdateBO,LoginUser loginUser) throws BizException;


     /**
      * 删除产品
      * @return
      * @throws BizException
      */
      void deleteProduct(Integer productId) throws BizException;


     /**
      * 修改产品排序
      * @return
      * @throws BizException
      */
     void updateProductSort(ProductUpdateStatusBO productUpdateStatusBO) throws BizException;

     /**
      * 产品上下架
      * @return
      * @throws BizException
      */
     void updateProductStatus(ProductUpdateStatusBO productUpdateStatusBO,LoginUser loginUser) throws BizException;



     /**
      * 修改佣金
      * @return
      * @throws BizException
      */
      void updateProductCommission(ProductUpdateStatusBO productUpdateStatusBO,LoginUser loginUser) throws BizException;

     /**
      * 根据Code查询产品信息
      *
      * @param
      * @return
      */
     Product getProduct(String productCode) throws BizException;

     /**
      * 根据Code查询产品信息 包括已下架产品
      *
      * @param
      * @return
      */
     Product getProductNotStatus(String productCode) throws BizException;


     /**
      * 重新刷新海报图
      */
     void refreshProductPoster(String productCode,LoginUser loginUser);


     void refreshProductCommission(LoginUser loginUser) throws InterruptedException;

}
