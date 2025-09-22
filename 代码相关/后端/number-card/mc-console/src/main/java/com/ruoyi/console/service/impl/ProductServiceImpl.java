package com.ruoyi.console.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.CacheKeyConstants;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.order.bo.ProductCopyBO;
import com.ruoyi.common.order.entity.*;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.console.mapper.ProductMapper;
import com.ruoyi.console.service.*;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.PageResultFactory;
import com.ruoyi.common.enums.ProductEnum;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.ProductAddAndUpdateBO;
import com.ruoyi.common.order.bo.ProductSelectBO;
import com.ruoyi.common.order.bo.ProductUpdateStatusBO;
import com.ruoyi.common.order.vo.ProductSelectVO;
import com.ruoyi.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/12 17:13
 */
@Service
@Slf4j
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {


    @Resource(name = "configStringRedisTemplate")
    protected StringRedisTemplate configStringRedisTemplate;

    @Resource
    AgentProductService agentProductService;

    @Resource
    AgentAccountService agentAccountService;

    @Resource
    CommissionConfigService commissionConfigService;

    @Resource
    UpstreamApiService upstreamApiService;

    @Resource
    UpstreamProductService upstreamProductService;

    @Resource
    AgentProductInitService agentProductInitService;

    @Resource
    QrCodeService qrCodeService;

    @Value(value = "${submit.h5}")
    private String h5url;

    /**
     * 产品查询查询
     * @return
     * @throws BizException
     */
    @Override
    public PageResult<ProductSelectVO> selectProductListPage(ProductSelectBO productSelectBO,LoginUser loginUser) throws BizException {
        //读取分页
        Page page = new Page(productSelectBO.getPageNo(),productSelectBO.getPageSize());
        Page<Product> productPage  = baseMapper.selectPage(page,new LambdaQueryWrapper<Product>()
                .eq(StringUtils.isNotEmpty(productSelectBO.getProductCode()),Product::getProductCode,productSelectBO.getProductCode())
                .like(StringUtils.isNotEmpty(productSelectBO.getProductName()),Product::getProductName,productSelectBO.getProductName())
                .eq(productSelectBO.getOperatorType()!=null,Product::getOperatorType,productSelectBO.getOperatorType())
                .eq(productSelectBO.getProductType()!=null,Product::getProductType,productSelectBO.getProductType())
                .eq(StringUtils.isNotEmpty(productSelectBO.getProductGsdq()),Product::getProductGsdq,productSelectBO.getProductGsdq())
                .eq(productSelectBO.getProductStatus()!=null,Product::getProductStatus,productSelectBO.getProductStatus())
                .orderByDesc(Product::getProductSort).orderByDesc(Product::getCreateTime)
        );
        Page<ProductSelectVO> productSelectVOPage  = new Page<>();
        BeanUtil.copyProperties(productPage,productSelectVOPage);
        AgentAccount agentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(),true);
        if(!CollectionUtils.isEmpty(productPage.getRecords())){
            List<ProductSelectVO> ProductSelectVOList = new ArrayList<>();
            for (Product product:productPage.getRecords()){
                ProductSelectVO productSelectVO = new ProductSelectVO();
                BeanUtil.copyProperties(product,productSelectVO);
                StringBuffer stringBuffer = new StringBuffer(h5url);
                stringBuffer.append("?productCode=").append(product.getProductCode()).append("&agentCode=").append(agentAccount.getAgentCode());
                productSelectVO.setH5Url(stringBuffer.toString());
                List<AgentProduct> agentProductList = agentProductService.list(new LambdaQueryWrapper<AgentProduct>().eq(AgentProduct::getParentProductCode,product.getProductCode()).eq(AgentProduct::getParentAgentCode,agentAccount.getAgentCode()));
                if(!CollectionUtils.isEmpty(agentProductList)){
                    List<String> agentCodeList = agentProductList.stream().map(t -> t.getAgentCode()).collect(Collectors.toList());
                    productSelectVO.setAgentCodeList(agentCodeList);
                }else {
                    productSelectVO.setAgentCodeList(new ArrayList<>());
                }
                ProductSelectVOList.add(productSelectVO);
            }
            productSelectVOPage.setRecords(ProductSelectVOList);
        }
        return PageResultFactory.createPageResult(productSelectVOPage);
    }



    /**
     * 新增产品
     * @return
     * @throws BizException
     */
    @Override
    public void addProduct(ProductAddAndUpdateBO productAddAndUpdateBO,LoginUser loginUser) throws BizException {
        Product product = new Product();
        BeanUtil.copyProperties(productAddAndUpdateBO,product);
        //生成产品编码 8位数
        product.setProductCode(RandomUtil.randomString(BaseConstant.EIGHT_INT));
        product.setProductStatus(ProductEnum.ProductStatusEnum.NO.getStatus());
        //排序默认0
        product.setProductSort(BaseConstant.ZERO_INT);
        product.setProductCommission(BaseConstant.ZERO_INT);
        product.setIsDispatchUpstreamApi(BaseConstant.ZERO_INT);
        product.setIsCopy(BaseConstant.ZERO_INT);
        product.setCreateTime(System.currentTimeMillis());
        if(product.getProductTemplateType()==null){
            //如果不传默认0
            product.setProductTemplateType(BaseConstant.ZERO_INT);
        }
        if(product.getBalanceConfig()==null){
            //如果不传默认0
            product.setBalanceConfig(BaseConstant.ZERO_INT);
        }
        List<AgentAccount> agentAccountList = new ArrayList<>();
        //查询代理商 如果选择全部代理 列表进行查询
        if(productAddAndUpdateBO.getIsAllAgent()!=null&&productAddAndUpdateBO.getIsAllAgent()==BaseConstant.ONE_INT){
            //查询所有有效子代理
            agentAccountList = agentAccountService.selectChildAgentList(loginUser,BaseConstant.ZERO_INT);
        }else {
            if(!CollectionUtils.isEmpty(productAddAndUpdateBO.getAgentCodeList())){
                //查询需要添加代理产品的代理信息
                agentAccountList = agentAccountService.list(new LambdaQueryWrapper<AgentAccount>().in(AgentAccount::getAgentCode,productAddAndUpdateBO.getAgentCodeList()));
            }
        }
        //添加产品
        baseMapper.insert(product);
        //生成所有子代理产品记录
        AgentAccount parentAgentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(),true);
        agentProductInitService.addProductPoster(parentAgentAccount,product);
        //创建所有子代理的代理商产品
        for (AgentAccount agentAccount:agentAccountList){
            agentProductService.addSubAgentProduct(agentAccount,parentAgentAccount,product,product.getProductCommission());
        }
    }


    /**
     * 复制产品
     * @return
     * @throws BizException
     */
    @Override
    public void copyProduct(ProductCopyBO productCopyBO,LoginUser loginUser) throws BizException {
        if(productCopyBO.getProductId()==null){
            throw new BizException("复制产品ID不能为空");
        }
        Product product = baseMapper.selectById(productCopyBO.getProductId());
        if(product==null){
            throw new BizException("产品ID:{}不存在",productCopyBO.getProductId());
        }
        Product productCopy = new Product();
        BeanUtil.copyProperties(product,productCopy);
        productCopy.setProductId(null);
        //生成产品编码 8位数
        productCopy.setProductCode(RandomUtil.randomString(BaseConstant.EIGHT_INT));
        productCopy.setProductStatus(ProductEnum.ProductStatusEnum.NO.getStatus());
        productCopy.setProductCommission(BaseConstant.ZERO_INT);
        //佣金归零
        productCopy.setIsDispatchUpstreamApi(BaseConstant.ZERO_INT);
        productCopy.setIsCopy(BaseConstant.ONE_INT);
        productCopy.setCreateTime(System.currentTimeMillis());
        //查询代理商 如果选择全部代理 列表进行查询
        //查询原产品代理商
        AgentAccount parentAgentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(),true);
        List<AgentAccount> agentAccountList = new ArrayList<>();
        if(productCopy.getIsAllAgent()!=null&&productCopy.getIsAllAgent()==BaseConstant.ONE_INT){
            //查询所有有效子代理
            agentAccountList = agentAccountService.selectChildAgentList(loginUser,BaseConstant.ZERO_INT);
        }else {
            List<AgentProduct> agentProductList = agentProductService.list(new LambdaQueryWrapper<AgentProduct>().eq(AgentProduct::getParentAgentCode,parentAgentAccount.getAgentCode()).eq(AgentProduct::getParentProductCode,product.getProductCode()));
            if(!CollectionUtils.isEmpty(agentProductList)){
                //查询需要添加代理产品的代理信息
                agentAccountList = agentAccountService.list(new LambdaQueryWrapper<AgentAccount>().in(AgentAccount::getAgentCode,agentProductList.stream().map(t -> t.getAgentCode()).collect(Collectors.toList())));
            }
        }
        //添加产品
        baseMapper.insert(productCopy);
        //创建所有子代理的代理商产品
        for (AgentAccount agentAccount:agentAccountList){
            agentProductService.addSubAgentProduct(agentAccount,parentAgentAccount,productCopy,productCopy.getProductCommission());
            agentProductInitService.addSubAgentProductPoster(agentAccount,product);
        }
    }


    /**
     * 更新产品
     * @return
     * @throws BizException
     */
    @Override
    public void updateProduct(ProductAddAndUpdateBO productAddAndUpdateBO,LoginUser loginUser) throws BizException {
        if(productAddAndUpdateBO==null||productAddAndUpdateBO.getProductId()==null){
            throw new BizException("产品ID不能为空");
        }
        Product product = baseMapper.selectById(productAddAndUpdateBO.getProductId());
        if(product==null){
            throw new BizException("产品{}不存在",productAddAndUpdateBO.getProductId());
        }
        Product updateProduct = new Product();
        BeanUtil.copyProperties(productAddAndUpdateBO,updateProduct);
        updateProduct.setProductCode(product.getProductCode());
        updateProduct.setUpdateTime(System.currentTimeMillis());
        UpstreamApi upstreamApi = upstreamApiService.getById(productAddAndUpdateBO.getUpstreamApiId());
        if(upstreamApi == null){
            throw new BizException("上游API不能为空");
        }
        UpstreamProduct upstreamProduct = upstreamProductService.getById(productAddAndUpdateBO.getUpstreamProductId());
        updateProduct.setUpstreamApiId(upstreamApi.getUpstreamApiId());
        updateProduct.setUpstreamApiName(upstreamApi.getUpstreamApiName());
        updateProduct.setUpstreamApiCode(upstreamApi.getUpstreamApiCode());
        //下游产品
        updateProduct.setUpstreamProductId(upstreamProduct.getUpstreamProductId());
        updateProduct.setUpstreamProductCode(upstreamProduct.getUpstreamProductCode());
        updateProduct.setUpstreamProductName(upstreamProduct.getUpstreamProductName());
        baseMapper.updateById(updateProduct);
        //判断是否新增或删除产品
        List<AgentAccount> agentAccountList = new ArrayList<>();
        //判断主图是否更新 如果更新 需要重新生成海报
        AgentAccount parentAgentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(),true);
        //传过来是空值 只需要考虑删除即可
        if(CollectionUtils.isEmpty(productAddAndUpdateBO.getAgentCodeList())){
            //查询所有有效子代理
            agentAccountList = agentAccountService.selectChildAgentList(loginUser,BaseConstant.ZERO_INT);
            for(AgentAccount agentAccount:agentAccountList){
                //删除代理商产品
                agentProductInitService.deleteSubAgentProduct(agentAccount,updateProduct);
            }
        }
        //不为空 计算差值 新增或删除产品
        if(!CollectionUtils.isEmpty(productAddAndUpdateBO.getAgentCodeList())){
            //转为map
            List<AgentProduct> agentProductList = agentProductService.list(new LambdaQueryWrapper<AgentProduct>().eq(AgentProduct::getParentProductCode,product.getProductCode()).eq(AgentProduct::getParentAgentCode,parentAgentAccount.getAgentCode()));
            //当前存在的代理商产品
            Map<String,AgentProduct> agentProductListMap = agentProductList.stream().collect(Collectors.toMap(AgentProduct::getAgentCode, Function.identity()));
            //最新的代理商产品
            Map<String,String> agentProductStrMap =productAddAndUpdateBO.getAgentCodeList().stream().collect(Collectors.toMap(Function.identity(), Function.identity()));
            //需新增代理商产品
            for (String agentCode:productAddAndUpdateBO.getAgentCodeList()){
                //创建所有子代理的代理商产品
                if(agentProductListMap.get(agentCode)==null){
                    AgentAccount agentAccount = agentAccountService.getAgentAccountByCode(agentCode,true);
                    agentProductInitService.addSubAgentProduct(agentAccount,parentAgentAccount,updateProduct,updateProduct.getProductCommission());
                    agentProductInitService.addSubAgentProductPoster(agentAccount,updateProduct);
                }
            }
            //查询一遍最新新增的 代理商产品 再循环匹配需删除代理产品
            agentProductList = agentProductService.list(new LambdaQueryWrapper<AgentProduct>().eq(AgentProduct::getParentProductCode,product.getProductCode()).eq(AgentProduct::getParentAgentCode,parentAgentAccount.getAgentCode()));
            for(AgentProduct agentProduct:agentProductList){
                if(agentProductStrMap.get(agentProduct.getAgentCode())==null){
                    agentProductInitService.deleteSubAgentProduct(agentAccountService.getAgentAccountByCode(agentProduct.getAgentCode(),true),updateProduct);
                }
            }
        }
        //主图更换需要更换下游所有海报
        if(StringUtils.isNotEmpty(updateProduct.getProductMasterMap())&&!updateProduct.getProductMasterMap().equals(product.getProductMasterMap())){
            //生成海报图
            agentProductInitService.addProductPoster(parentAgentAccount,updateProduct);
            //更新下游所有海报图
            agentAccountList = agentAccountService.selectChildAgentList(loginUser,BaseConstant.ZERO_INT);
            if(!CollectionUtils.isEmpty(agentAccountList)){
                for (AgentAccount agentAccount:agentAccountList){
                    agentProductInitService.addSubAgentProductPoster(agentAccount,updateProduct);
                }
            }
        }
    }


    /**
     * 删除产品
     * @return
     * @throws BizException
     */
    @Override
    public void deleteProduct(Integer productId) throws BizException {
        if(productId==null){
            throw new BizException("产品ID不能为空");
        }
        Product product = baseMapper.selectById(productId);
        if(product==null){
            throw new BizException("产品{}不存在",productId);
        }
        baseMapper.deleteById(productId);
        //同时删除所有代理产品
        agentProductService.remove(new LambdaQueryWrapper<AgentProduct>().eq(AgentProduct::getParentProductCode,product.getProductCode()));
    }


    /**
     * 更新产品排序
     * @return
     * @throws BizException
     */
    @Override
    public void updateProductSort(ProductUpdateStatusBO productUpdateStatusBO) throws BizException {
        if(productUpdateStatusBO==null||productUpdateStatusBO.getProductId()==null){
            throw new BizException("产品ID不能为空");
        }
        Product updateProduct = new Product();
        updateProduct.setProductId(productUpdateStatusBO.getProductId());
        if(productUpdateStatusBO.getProductSort()!=null){
            updateProduct.setProductSort(productUpdateStatusBO.getProductSort());
        }
        updateProduct.setUpdateTime(System.currentTimeMillis());
        baseMapper.updateById(updateProduct);
    }



    /**
     * 产品上下架
     * @return
     * @throws BizException
     */
    @Override
    public void updateProductStatus(ProductUpdateStatusBO productUpdateStatusBO,LoginUser loginUser) throws BizException {
        if(productUpdateStatusBO==null||productUpdateStatusBO.getProductId()==null||productUpdateStatusBO.getProductStatus()==null){
            throw new BizException("参数不能为空");
        }
        Product product = baseMapper.selectById(productUpdateStatusBO.getProductId());
        if(product==null){
            throw new BizException("产品不存在:{}",productUpdateStatusBO.getProductId());
        }
        //更新产品状态
        product.setProductStatus(productUpdateStatusBO.getProductStatus());
        product.setUpdateTime(System.currentTimeMillis());
        baseMapper.updateById(product);
        //更新所有代理产品状态
        AgentProduct agentProduct = new AgentProduct();
        agentProduct.setProductStatus(productUpdateStatusBO.getProductStatus());
        agentProduct.setUpdateTime(System.currentTimeMillis());
        agentProductService.update(agentProduct,new LambdaQueryWrapper<AgentProduct>().eq(AgentProduct::getParentProductCode,product.getProductCode()));
    }


    /**
     * 修改佣金
     * @return
     * @throws BizException
     */
    @Override
    public void updateProductCommission(ProductUpdateStatusBO productUpdateStatusBO,LoginUser loginUser) throws BizException {
        if(productUpdateStatusBO==null||productUpdateStatusBO.getProductId()==null||productUpdateStatusBO.getProductCommission()==null){
            throw new BizException("参数不能为空");
        }
        Product product = baseMapper.selectById(productUpdateStatusBO.getProductId());
        if(product==null){
            throw new BizException("产品不存在:{}",productUpdateStatusBO.getProductId());
        }
        //更新产品状态
        product.setProductCommission(productUpdateStatusBO.getProductCommission());
        product.setUpdateTime(System.currentTimeMillis());
        baseMapper.updateById(product);
        //计算代理佣金代理产品状态
        AgentAccount parentAgentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(),true);
        List<AgentProduct> agentProductList = agentProductService.list(new LambdaQueryWrapper<AgentProduct>().eq(AgentProduct::getParentProductCode,product.getProductCode()).eq(AgentProduct::getParentAgentCode,parentAgentAccount.getAgentCode()));
        for (AgentProduct agentProduct:agentProductList){
            //重新计算代理商产品佣金
            agentProductInitService.updateAgentProductCommission(product.getProductCode(),agentProduct.getAgentCode(),productUpdateStatusBO.getProductCommission());
        }
    }




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
            if (product.getProductStatus() == BaseConstant.ZERO_INT) {
                throw new BizException("产品已下架:{}", productCode);
            }
            configStringRedisTemplate.opsForValue().set(cacheKey, JSONObject.toJSONString(product), 10, TimeUnit.MINUTES);
        } else {
            product = JSONObject.parseObject(json, Product.class);
        }
        return product;
    }

    /**
     * 根据Code查询产品信息 包括已下架产品
     *
     * @param
     * @return
     */
    @Override
    public Product getProductNotStatus(String productCode) throws BizException {
        if (StrUtil.isBlankIfStr(productCode)) {
            throw new BizException("product code不存在:{}", productCode);
        }
        Product product = null;
        String cacheKey = CacheUtils.generalKey(CacheKeyConstants.PRODUCT_API_NOT_STATUS, productCode);
        String json = configStringRedisTemplate.opsForValue().get(cacheKey);
        if (StrUtil.isBlankIfStr(json)) {
            product = baseMapper.selectOne(new LambdaQueryWrapper<Product>().eq(Product::getProductCode, productCode));
            if(product != null){
                configStringRedisTemplate.opsForValue().set(cacheKey, JSONObject.toJSONString(product), 10, TimeUnit.MINUTES);
            }
        } else {
            product = JSONObject.parseObject(json, Product.class);
        }
        return product;
    }

    /**
     * 重新刷新海报图
     */
    public void refreshProductPoster(String productCode,LoginUser loginUser){
        try {
            Product product = getProduct(productCode);
            //重新生成主图
            AgentAccount parentAgentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(),true);
            //主图更换需要更换下游所有海报
            if(StringUtils.isNotEmpty(product.getProductMasterMap())){
                //生成海报图
                agentProductInitService.addProductPoster(parentAgentAccount,product);
                //更新下游所有海报图
                List<AgentAccount> agentAccountList = agentAccountService.selectChildAgentList(loginUser,BaseConstant.ZERO_INT);
                if(!CollectionUtils.isEmpty(agentAccountList)){
                    for (AgentAccount agentAccount:agentAccountList){
                        agentProductInitService.addSubAgentProductPoster(agentAccount,product);
                    }
                }
            }
        }catch (Exception e){

        }

    }

    /**
     * 刷新产品佣金
     */
    public void refreshProductCommission(LoginUser loginUser) throws InterruptedException {

            List<Product> productList = this.list();
            if(CollectionUtils.isEmpty(productList)){
                return;
            }
            for (Product product:productList){
                try {
                    ProductUpdateStatusBO productUpdateStatusBO = new ProductUpdateStatusBO();
                    productUpdateStatusBO.setProductId(product.getProductId());
                    productUpdateStatusBO.setProductCommission(product.getProductCommission());
                    updateProductCommission(productUpdateStatusBO,loginUser);
                    log.info("{}已执行:{}",product.getProductId());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
    }


}
