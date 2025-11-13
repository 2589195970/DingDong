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
import com.ruoyi.common.order.bo.ProductInitRequest;
import com.ruoyi.common.order.entity.*;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.common.utils.SecurityUtils;
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
import com.ruoyi.common.order.vo.ProductInitResult;
import com.ruoyi.common.order.vo.ProductSelectVO;
import com.ruoyi.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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
    AgentProductInitService agentProductInitService;

    @Resource
    AgentAccountService agentAccountService;

    @Resource
    UpstreamApiService upstreamApiService;

    @Resource
    UpstreamProductService upstreamProductService;

    
    @Resource
    ProductNoticeService productNoticeService;

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
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<Product>()
                .eq(StringUtils.isNotEmpty(productSelectBO.getProductCode()),Product::getProductCode,productSelectBO.getProductCode())
                .like(StringUtils.isNotEmpty(productSelectBO.getProductName()),Product::getProductName,productSelectBO.getProductName())
                .eq(productSelectBO.getOperatorType()!=null,Product::getOperatorType,productSelectBO.getOperatorType())
                .eq(productSelectBO.getProductType()!=null,Product::getProductType,productSelectBO.getProductType())
                .eq(StringUtils.isNotEmpty(productSelectBO.getProductGsdq()),Product::getProductGsdq,productSelectBO.getProductGsdq())
                .eq(productSelectBO.getProductStatus()!=null,Product::getProductStatus,productSelectBO.getProductStatus())
                .orderByDesc(Product::getProductSort).orderByDesc(Product::getCreateTime);
        if(StringUtils.isNotEmpty(productSelectBO.getKeyword())){
            queryWrapper.and(wrapper -> wrapper.like(Product::getProductName,productSelectBO.getKeyword())
                    .or()
                    .like(Product::getProductCode,productSelectBO.getKeyword()));
        }
        Page<Product> productPage  = baseMapper.selectPage(page,queryWrapper);
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
        if(product.getSfyjfx() == null){
            product.setSfyjfx(BaseConstant.ZERO_INT);
        }
        if (product.getBaseCardFee() == null || product.getBaseCardFee() < 0) {
            product.setBaseCardFee(BaseConstant.ZERO_INT);
        }
        if (!Objects.equals(product.getProductType(), ProductEnum.PAID_CARD.getStatus())) {
            product.setBaseCardFee(BaseConstant.ZERO_INT);
        }
        if (product.getProductInitialBalance() == null || product.getProductInitialBalance() < 0) {
            product.setProductInitialBalance(BaseConstant.ZERO_INT);
        }
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
        Integer targetAllAgentFlag = productAddAndUpdateBO.getIsAllAgent();
        List<String> targetAgentCodes = CollectionUtils.isEmpty(productAddAndUpdateBO.getAgentCodeList())
                ? new ArrayList<>()
                : new ArrayList<>(productAddAndUpdateBO.getAgentCodeList());
        //查询代理商佣金校验
        if (Objects.equals(productAddAndUpdateBO.getSfyjfx(), BaseConstant.ZERO_INT)
                && product.getProductCommission() != null
                && product.getProductCommission() > 0) {
            throw new BizException("当前产品未开启佣金返现，请先将基础佣金调整为0");
        }
        //添加产品
        baseMapper.insert(product);
        //生成所有子代理产品记录
        AgentAccount parentAgentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(),true);
        ensureSelfAgentProduct(product, parentAgentAccount);
        if (parentAgentAccount != null) {
            log.info("Async dispatch: queue product poster generation, productCode={}, agentCode={}",
                    product.getProductCode(), parentAgentAccount.getAgentCode());
            agentProductInitService.addProductPoster(parentAgentAccount, product);
        } else {
            log.warn("Async dispatch skipped: parent agent account missing for productCode={}", product.getProductCode());
        }
        boolean needDispatchSubAgent = Objects.equals(targetAllAgentFlag, BaseConstant.ONE_INT)
                || !CollectionUtils.isEmpty(targetAgentCodes);
        if (!needDispatchSubAgent) {
            log.info("Async dispatch: no sub agent product creation required for productCode={}", product.getProductCode());
        } else {
            log.info("Async dispatch: queue sub agent product creation, productCode={}, parentAgentCode={}, dispatchAll={}, specifiedCount={}",
                    product.getProductCode(),
                    parentAgentAccount == null ? "UNKNOWN" : parentAgentAccount.getAgentCode(),
                    Objects.equals(targetAllAgentFlag, BaseConstant.ONE_INT),
                    targetAgentCodes.size());
            agentProductInitService.addSubAgentProducts(targetAllAgentFlag,
                    targetAgentCodes,
                    parentAgentAccount,
                    product,
                    product.getProductCommission(),
                    loginUser);
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
        ensureSelfAgentProduct(productCopy, parentAgentAccount);
        agentProductService.addSubAgentProducts(agentAccountList,parentAgentAccount,productCopy,productCopy.getProductCommission());
        for (AgentAccount agentAccount:agentAccountList){
            agentProductService.addSubAgentProductPoster(agentAccount,product);
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
        if (Objects.equals(productAddAndUpdateBO.getSfyjfx(), BaseConstant.ZERO_INT)
                && product.getProductCommission() != null
                && product.getProductCommission() > 0) {
            throw new BizException("当前产品未开启佣金返现，请先将基础佣金调整为0");
        }
        Product updateProduct = new Product();
        BeanUtil.copyProperties(productAddAndUpdateBO,updateProduct);
        updateProduct.setProductCode(product.getProductCode());
        updateProduct.setUpdateTime(System.currentTimeMillis());
        if(updateProduct.getSfyjfx() == null){
            updateProduct.setSfyjfx(BaseConstant.ZERO_INT);
        }
        if (updateProduct.getBaseCardFee() == null || updateProduct.getBaseCardFee() < 0) {
            updateProduct.setBaseCardFee(BaseConstant.ZERO_INT);
        }
        if (!Objects.equals(updateProduct.getProductType(), ProductEnum.PAID_CARD.getStatus())) {
            updateProduct.setBaseCardFee(BaseConstant.ZERO_INT);
        }
        if (updateProduct.getProductInitialBalance() == null || updateProduct.getProductInitialBalance() < 0) {
            updateProduct.setProductInitialBalance(BaseConstant.ZERO_INT);
        }
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
                agentProductService.deleteSubAgentProduct(agentAccount,updateProduct);
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
            List<AgentAccount> needAddAgents = new ArrayList<>();
            for (String agentCode:productAddAndUpdateBO.getAgentCodeList()){
                //创建所有子代理的代理商产品
                if(agentProductListMap.get(agentCode)==null){
                    AgentAccount agentAccount = agentAccountService.getAgentAccountByCode(agentCode,true);
                    needAddAgents.add(agentAccount);
                }
            }
            if(!CollectionUtils.isEmpty(needAddAgents)){
                agentProductService.addSubAgentProducts(needAddAgents,parentAgentAccount,updateProduct,updateProduct.getProductCommission());
                for (AgentAccount agentAccount:needAddAgents){
                    agentProductService.addSubAgentProductPoster(agentAccount,updateProduct);
                }
            }
            //查询一遍最新新增的 代理商产品 再循环匹配需删除代理产品
            agentProductList = agentProductService.list(new LambdaQueryWrapper<AgentProduct>().eq(AgentProduct::getParentProductCode,product.getProductCode()).eq(AgentProduct::getParentAgentCode,parentAgentAccount.getAgentCode()));
            for(AgentProduct agentProduct:agentProductList){
                if(agentProductStrMap.get(agentProduct.getAgentCode())==null){
                    agentProductService.deleteSubAgentProduct(agentAccountService.getAgentAccountByCode(agentProduct.getAgentCode(),true),updateProduct);
                }
            }
        }
        //主图更换需要更换下游所有海报
        if(StringUtils.isNotEmpty(updateProduct.getProductMasterMap())&&!updateProduct.getProductMasterMap().equals(product.getProductMasterMap())){
            //生成海报图
            agentProductService.addProductPoster(parentAgentAccount,updateProduct);
            //更新下游所有海报图
            agentAccountList = agentAccountService.selectChildAgentList(loginUser,BaseConstant.ZERO_INT);
            if(!CollectionUtils.isEmpty(agentAccountList)){
                for (AgentAccount agentAccount:agentAccountList){
                    agentProductService.addSubAgentProductPoster(agentAccount,updateProduct);
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
    @Transactional(rollbackFor = Exception.class)
    public void updateProductStatus(ProductUpdateStatusBO productUpdateStatusBO,LoginUser loginUser) throws BizException {
        if(productUpdateStatusBO==null||productUpdateStatusBO.getProductId()==null||productUpdateStatusBO.getProductStatus()==null){
            throw new BizException("参数不能为空");
        }
        Product product = baseMapper.selectById(productUpdateStatusBO.getProductId());
        if(product==null){
            throw new BizException("产品不存在:{}",productUpdateStatusBO.getProductId());
        }

        // 获取当前操作用户的代理商信息
        AgentAccount currentAgentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(), true);
        boolean isAdmin = loginUser.getUser().isAdmin();
        Integer targetStatus = productUpdateStatusBO.getProductStatus();

        // 如果是上架操作，检查上级代理商的产品状态
        if(targetStatus == 1) {
            checkParentAgentProductStatus(product, currentAgentAccount, loginUser);
        }

        // 保存原状态，用于后续公告创建
        Integer oldStatus = product.getProductStatus();

        if (isAdmin) {
            // 更新产品状态
            product.setProductStatus(targetStatus);
            product.setUpdateTime(System.currentTimeMillis());

            // 如果是上架操作，设置上架时间
            if(targetStatus == 1) {
                product.setShelfTime(System.currentTimeMillis());
            }

            baseMapper.updateById(product);

            // 【新增】创建产品状态变更公告
            try {
                productNoticeService.createProductStatusNotice(product, oldStatus, targetStatus, loginUser.getUsername());
            } catch (Exception e) {
                log.warn("创建产品状态变更公告失败，产品ID：{}, 错误：{}", product.getProductId(), e.getMessage());
                // 公告创建失败不影响主流程，只记录警告日志
            }
        }

        // 更新当前代理商自己的产品状态
        updateCurrentAgentProduct(product, targetStatus, currentAgentAccount.getAgentCode());
        boolean needCascadeUpdate = isAdmin || targetStatus == 0;
        if (needCascadeUpdate) {
            triggerAsyncAgentProductStatusUpdate(product, currentAgentAccount, targetStatus);
        }
    }

    /**
     * 检查上级代理商产品状态
     * @param product 产品信息
     * @param currentAgentAccount 当前代理商
     * @param loginUser 登录用户信息
     * @throws BizException 如果上级代理商产品未上架，抛出异常
     */
    private void checkParentAgentProductStatus(Product product, AgentAccount currentAgentAccount, LoginUser loginUser) throws BizException {
        // 如果是admin用户，直接允许上架，不受上级控制
        if (loginUser.getUser().isAdmin()) {
            return;
        }

        // 如果当前代理商没有上级代理商（顶级代理商），直接允许上架
        if (StringUtils.isEmpty(currentAgentAccount.getParentAgentCode())) {
            return;
        }

        // 检查主产品表 t_product 中的状态
        Product parentProduct = baseMapper.selectById(product.getProductId());
        if (parentProduct != null && parentProduct.getProductStatus() != 1) {
            throw new BizException("请先联系上级代理商完成商品上架，仅在上级代理商上架后，您方可进行上架操作");
        }

        // 检查上级代理商的 t_agent_product 表中的状态
        AgentProduct parentAgentProduct = agentProductService.getOne(
            new LambdaQueryWrapper<AgentProduct>()
                .eq(AgentProduct::getParentProductCode, product.getProductCode())
                .eq(AgentProduct::getAgentCode, currentAgentAccount.getParentAgentCode())
                .eq(AgentProduct::getParentAgentCode, currentAgentAccount.getParentAgentCode())
        );
        if(parentAgentProduct == null){

        }else if (parentAgentProduct.getProductStatus() != 1) {
            throw new BizException("请先联系上级代理商完成商品上架，仅在上级代理商上架后，您方可进行上架操作");
        }
    }

    /**
     * 更新当前代理商自己的产品状态
     * @param product 产品信息
     * @param newStatus 新状态
     * @param agentCode 代理商编码
     */
    private void updateCurrentAgentProduct(Product product, Integer newStatus, String agentCode) {
        AgentProduct agentProduct = new AgentProduct();
        agentProduct.setProductStatus(newStatus);
        agentProduct.setUpdateTime(System.currentTimeMillis());

        // 如果是上架操作，设置上架时间
        if(newStatus == 1) {
            agentProduct.setCreateTime(System.currentTimeMillis());
        }

        agentProductService.update(agentProduct,
            new LambdaQueryWrapper<AgentProduct>()
                .eq(AgentProduct::getParentProductCode, product.getProductCode())
                .eq(AgentProduct::getAgentCode, agentCode)
        );
    }

    private void triggerAsyncAgentProductStatusUpdate(Product product,
                                                      AgentAccount currentAgentAccount,
                                                      Integer targetStatus) {
        if (product == null || currentAgentAccount == null || targetStatus == null) {
            log.warn("Skip async cascade: invalid arguments product={} agentAccount={} status={}",
                    product == null ? null : product.getProductCode(),
                    currentAgentAccount,
                    targetStatus);
            return;
        }
        Runnable cascadeTask = () -> {
            try {
                agentProductInitService.asyncUpdateAgentProductStatus(product.getProductCode(),
                        currentAgentAccount.getAgentCode(), targetStatus);
            } catch (BizException ex) {
                log.error("Async cascade dispatch failed immediately productCode={} agentCode={} status={} error={}",
                        product.getProductCode(),
                        currentAgentAccount.getAgentCode(),
                        targetStatus,
                        ex.getMessage(),
                        ex);
            }
        };
        runAfterCommit(cascadeTask);
    }

    private void runAfterCommit(Runnable runnable) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            runnable.run();
            return;
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                runnable.run();
            }
        });
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
        validateCommissionSetting(product, productUpdateStatusBO.getProductCommission());
        //更新产品状态
        product.setProductCommission(productUpdateStatusBO.getProductCommission());
        product.setUpdateTime(System.currentTimeMillis());
        baseMapper.updateById(product);
        //计算代理佣金代理产品状态
        AgentAccount parentAgentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(),true);
        List<AgentProduct> agentProductList = agentProductService.list(new LambdaQueryWrapper<AgentProduct>().eq(AgentProduct::getParentProductCode,product.getProductCode()).eq(AgentProduct::getParentAgentCode,parentAgentAccount.getAgentCode()));
        for (AgentProduct agentProduct:agentProductList){
            //重新计算代理商产品佣金
            agentProductService.updateAgentProductCommission(product.getProductCode(),agentProduct.getAgentCode(),productUpdateStatusBO.getProductCommission());
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
        // 注释掉缓存查询，直接从数据库查询
        // String cacheKey = CacheUtils.generalKey(CacheKeyConstants.PRODUCT_API, productCode);
        // String json = configStringRedisTemplate.opsForValue().get(cacheKey);
        // if (StrUtil.isBlankIfStr(json)) {
            product = baseMapper.selectOne(new LambdaQueryWrapper<Product>().eq(Product::getProductCode, productCode));
            if (product == null) {
                throw new BizException("产品不存在:{}", productCode);
            }
            if (product.getProductStatus() == BaseConstant.ZERO_INT) {
                throw new BizException("产品已下架:{}", productCode);
            }
            // 注释掉缓存写入
            // configStringRedisTemplate.opsForValue().set(cacheKey, JSONObject.toJSONString(product), 10, TimeUnit.MINUTES);
        // } else {
        //     product = JSONObject.parseObject(json, Product.class);
        // }
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
        // 注释掉缓存查询，直接从数据库查询
        // String cacheKey = CacheUtils.generalKey(CacheKeyConstants.PRODUCT_API_NOT_STATUS, productCode);
        // String json = configStringRedisTemplate.opsForValue().get(cacheKey);
        // if (StrUtil.isBlankIfStr(json)) {
            product = baseMapper.selectOne(new LambdaQueryWrapper<Product>().eq(Product::getProductCode, productCode));
            // 注释掉缓存写入
            // if(product != null){
            //     configStringRedisTemplate.opsForValue().set(cacheKey, JSONObject.toJSONString(product), 10, TimeUnit.MINUTES);
            // }
        // } else {
        //     product = JSONObject.parseObject(json, Product.class);
        // }
        return product;
    }

    @Override
    public ProductInitResult initAgentProducts(ProductInitRequest request, LoginUser loginUser) throws BizException {
        ProductInitRequest payload = request == null ? new ProductInitRequest() : request;
        ProductInitResult result = new ProductInitResult();
        boolean dryRun = Boolean.TRUE.equals(payload.getDryRun());
        boolean includeDisabledAgent = Boolean.TRUE.equals(payload.getIncludeDisabledAgent());
        boolean includeOffShelfProduct = Boolean.TRUE.equals(payload.getIncludeOffShelfProduct());

        AgentAccount rootAgent = null;
        if (!loginUser.getUser().isAdmin()) {
            rootAgent = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(), true);
            if (StringUtils.isNotEmpty(rootAgent.getParentAgentCode())) {
                throw new BizException("仅允许最高级代理执行初始化");
            }
        } else {
            try {
                rootAgent = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(), false);
            } catch (BizException ignored) {
            }
        }

        LambdaQueryWrapper<Product> productWrapper = new LambdaQueryWrapper<>();
        if (!CollectionUtils.isEmpty(payload.getProductIdList())) {
            productWrapper.in(Product::getProductId, payload.getProductIdList());
        }
        if (!includeOffShelfProduct) {
            productWrapper.eq(Product::getProductStatus, ProductEnum.ProductStatusEnum.YES.getStatus());
        }
        List<Product> productList = baseMapper.selectList(productWrapper);
        if (CollectionUtils.isEmpty(productList)) {
            result.addWarning("未找到需要初始化的商品");
            return result;
        }

        LambdaQueryWrapper<AgentAccount> agentWrapper = new LambdaQueryWrapper<>();
        if (!includeDisabledAgent) {
            agentWrapper.eq(AgentAccount::getIsEnabled, BaseConstant.ZERO_INT);
        }
        List<AgentAccount> allAgents = agentAccountService.list(agentWrapper);
        if (CollectionUtils.isEmpty(allAgents)) {
            result.addWarning("未找到可用的代理商");
            return result;
        }
        Map<String, AgentAccount> agentMap = allAgents.stream()
                .collect(Collectors.toMap(AgentAccount::getAgentCode, Function.identity(), (a, b) -> a));

        Set<String> scopedAgentCodes = new HashSet<>();
        if (loginUser.getUser().isAdmin()) {
            scopedAgentCodes.addAll(agentMap.keySet());
        } else {
            scopedAgentCodes.add(rootAgent.getAgentCode());
            for (AgentAccount agent : allAgents) {
                if (agent == null) {
                    continue;
                }
                if (rootAgent.getAgentCode().equals(agent.getAgentCode())) {
                    continue;
                }
                if (StringUtils.isNotEmpty(agent.getParentAgentList())
                        && agent.getParentAgentList().contains(rootAgent.getAgentCode())) {
                    scopedAgentCodes.add(agent.getAgentCode());
                }
            }
        }
        List<AgentAccount> scopedAgents = scopedAgentCodes.stream()
                .map(agentMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(scopedAgents)) {
            result.addWarning("未匹配到需要初始化的代理商范围");
            return result;
        }

        for (Product product : productList) {
            if (product == null) {
                continue;
            }
            Map<String, AgentProduct> existingMap = agentProductService.list(
                    new LambdaQueryWrapper<AgentProduct>()
                            .eq(AgentProduct::getParentProductCode, product.getProductCode())
            ).stream().collect(Collectors.toMap(ap -> buildAgentProductKey(ap.getAgentCode(), ap.getParentAgentCode()),
                    Function.identity(), (a, b) -> a));

            for (AgentAccount agent : scopedAgents) {
                if (agent == null) {
                    continue;
                }
                String parentCode = agent.getParentAgentCode();
                if (StringUtils.isEmpty(parentCode)) {
                    continue;
                }
                if (!scopedAgentCodes.contains(parentCode)) {
                    continue;
                }
                AgentAccount parentAgent = agentMap.get(parentCode);
                if (parentAgent == null) {
                    continue;
                }
                String key = buildAgentProductKey(agent.getAgentCode(), parentCode);
                AgentProduct existing = existingMap.remove(key);
                if (existing == null) {
                    if (product.getIsAllAgent() != null && product.getIsAllAgent().equals(BaseConstant.ONE_INT)) {
                        if (!dryRun) {
                            agentProductService.addSubAgentProduct(agent, parentAgent, product, product.getProductCommission());
                        }
                        result.setCreated(result.getCreated() + 1);
                    } else {
                        result.addWarning(String.format("产品[%s]未为代理[%s]配置可见范围，未执行新增", product.getProductCode(), agent.getAgentCode()));
                    }
                    continue;
                }

                boolean needUpdate = false;
                AgentProduct updateEntity = new AgentProduct();

                if (StringUtils.isNotEmpty(product.getProductName())
                        && !product.getProductName().equals(existing.getParentProductName())) {
                    updateEntity.setParentProductName(product.getProductName());
                    needUpdate = true;
                }
                if (product.getProductStatus() != null
                        && !product.getProductStatus().equals(existing.getProductStatus())) {
                    updateEntity.setProductStatus(product.getProductStatus());
                    needUpdate = true;
                }
                if (needUpdate) {
                    if (!dryRun) {
                        updateEntity.setUpdateTime(System.currentTimeMillis());
                        agentProductService.update(updateEntity,
                                new LambdaQueryWrapper<AgentProduct>().eq(AgentProduct::getAgentProductId, existing.getAgentProductId()));
                    }
                    result.setUpdated(result.getUpdated() + 1);
                }

                Integer productCommission = product.getProductCommission() == null ? BaseConstant.ZERO_INT : product.getProductCommission();
                if (existing.getProductCommission() == null || !existing.getProductCommission().equals(productCommission)) {
                    if (!dryRun) {
                        agentProductService.updateAgentProductCommission(product.getProductCode(), agent.getAgentCode(), productCommission);
                    }
                    result.setCommissionSynced(result.getCommissionSynced() + 1);
                }

                if (StringUtils.isEmpty(existing.getProductQrcodeMap()) && StringUtils.isNotEmpty(product.getProductMasterMap())) {
                    if (!dryRun) {
                        try {
                            agentProductService.addSubAgentProductPoster(agent, product);
                        } catch (BizException e) {
                            result.addWarning(String.format("生成代理[%s]产品[%s]海报失败:%s", agent.getAgentCode(), product.getProductCode(), e.getMessage()));
                        }
                    }
                    result.setPosterSynced(result.getPosterSynced() + 1);
                }
            }

            if (product.getIsAllAgent() != null && product.getIsAllAgent().equals(BaseConstant.ONE_INT) && !existingMap.isEmpty()) {
                for (AgentProduct orphan : existingMap.values()) {
                    if (orphan == null || !scopedAgentCodes.contains(orphan.getAgentCode())) {
                        continue;
                    }
                    if (!dryRun) {
                        agentProductService.deleteAgentProduct(orphan.getAgentProductId());
                    }
                    result.setDeleted(result.getDeleted() + 1);
                }
            }
        }
        return result;
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
                agentProductService.addProductPoster(parentAgentAccount,product);
                //更新下游所有海报图
                List<AgentAccount> agentAccountList = agentAccountService.selectChildAgentList(loginUser,BaseConstant.ZERO_INT);
                if(!CollectionUtils.isEmpty(agentAccountList)){
                    for (AgentAccount agentAccount:agentAccountList){
                        agentProductService.addSubAgentProductPoster(agentAccount,product);
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

    /**
     * 查询产品分类数统计
     *
     * @return 产品分类统计Map
     */
    @Override
    public Map<String, Object> getProductCategoryCount() throws BizException {
        Map<String, Object> result = new HashMap<>();
        Map<String, Integer> productTypeCount = initProductTypeCount();
        int totalCount = 0;

        Long userId = null;
        boolean isAdmin = true;
        try {
            userId = SecurityUtils.getUserId();
            isAdmin = SecurityUtils.isAdmin(userId);
        } catch (Exception e) {
            isAdmin = true;
        }

        try {
            if (isAdmin) {
                // 全量产品统计
                totalCount = fillAllProductCounts(productTypeCount);
            } else {
                AgentAccount agentAccount = agentAccountService.getAgentAccountByUserId(userId, true);
                if (agentAccount != null) {
                    totalCount = fillAgentProductCounts(productTypeCount, agentAccount.getAgentCode());
                }
            }
        } catch (Exception e) {
            throw new BizException(e.getMessage());
        }

        result.put("productTypeCount", productTypeCount);
        result.put("totalCount", totalCount);
        return result;
    }

    private int fillAllProductCounts(Map<String, Integer> productTypeCount) {
        int totalCount = 0;

        int dailySettlementCount = baseMapper.selectCount(
            new LambdaQueryWrapper<Product>()
                .eq(Product::getProductType, ProductEnum.DAILY_SETTLEMENT.getStatus())
                .eq(Product::getProductStatus, ProductEnum.ProductStatusEnum.YES.getStatus())
        ).intValue();
        productTypeCount.put("dailySettlement", dailySettlementCount);
        totalCount += dailySettlementCount;

        int monthlyStatementCount = baseMapper.selectCount(
            new LambdaQueryWrapper<Product>()
                .eq(Product::getProductType, ProductEnum.MONTHLY_STATEMENT.getStatus())
                .eq(Product::getProductStatus, ProductEnum.ProductStatusEnum.YES.getStatus())
        ).intValue();
        productTypeCount.put("monthlyStatement", monthlyStatementCount);
        totalCount += monthlyStatementCount;

        int longTimeCount = baseMapper.selectCount(
            new LambdaQueryWrapper<Product>()
                .eq(Product::getProductType, ProductEnum.LONG_TIME.getStatus())
                .eq(Product::getProductStatus, ProductEnum.ProductStatusEnum.YES.getStatus())
        ).intValue();
        productTypeCount.put("longTime", longTimeCount);
        totalCount += longTimeCount;

        int otherCount = baseMapper.selectCount(
            new LambdaQueryWrapper<Product>()
                .eq(Product::getProductType, ProductEnum.OTHER.getStatus())
                .eq(Product::getProductStatus, ProductEnum.ProductStatusEnum.YES.getStatus())
        ).intValue();
        productTypeCount.put("other", otherCount);
        totalCount += otherCount;

        int paidCardCount = baseMapper.selectCount(
            new LambdaQueryWrapper<Product>()
                .eq(Product::getProductType, ProductEnum.PAID_CARD.getStatus())
                .eq(Product::getProductStatus, ProductEnum.ProductStatusEnum.YES.getStatus())
        ).intValue();
        productTypeCount.put("paidCard", paidCardCount);
        totalCount += paidCardCount;

        int combinationCount = baseMapper.selectCount(
            new LambdaQueryWrapper<Product>()
                .eq(Product::getProductType, ProductEnum.COMBINATION.getStatus())
                .eq(Product::getProductStatus, ProductEnum.ProductStatusEnum.YES.getStatus())
        ).intValue();
        productTypeCount.put("combination", combinationCount);
        totalCount += combinationCount;

        return totalCount;
    }

    private int fillAgentProductCounts(Map<String, Integer> productTypeCount, String agentCode) {
        if (StringUtils.isEmpty(agentCode)) {
            return 0;
        }
        List<AgentProduct> agentProducts = agentProductService.list(
            new LambdaQueryWrapper<AgentProduct>()
                .eq(AgentProduct::getAgentCode, agentCode)
                .eq(AgentProduct::getProductStatus, ProductEnum.ProductStatusEnum.YES.getStatus())
        );
        if (CollectionUtils.isEmpty(agentProducts)) {
            return 0;
        }

        Set<String> productCodes = agentProducts.stream()
            .map(AgentProduct::getParentProductCode)
            .filter(StringUtils::isNotEmpty)
            .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(productCodes)) {
            return 0;
        }

        List<Product> productList = baseMapper.selectList(
            new LambdaQueryWrapper<Product>()
                .in(Product::getProductCode, productCodes)
                .eq(Product::getProductStatus, ProductEnum.ProductStatusEnum.YES.getStatus())
        );
        if (CollectionUtils.isEmpty(productList)) {
            return 0;
        }

        for (Product product : productList) {
            incrementProductTypeCount(productTypeCount, product.getProductType());
        }
        return productTypeCount.values().stream().mapToInt(Integer::intValue).sum();
    }

    private Map<String, Integer> initProductTypeCount() {
        Map<String, Integer> productTypeCount = new HashMap<>();
        productTypeCount.put("dailySettlement", 0);
        productTypeCount.put("monthlyStatement", 0);
        productTypeCount.put("longTime", 0);
        productTypeCount.put("other", 0);
        productTypeCount.put("combination", 0);
        productTypeCount.put("paidCard", 0);
        return productTypeCount;
    }

    private void incrementProductTypeCount(Map<String, Integer> productTypeCount, Integer productType) {
        if (productType == null) {
            return;
        }
        if (ProductEnum.DAILY_SETTLEMENT.getStatus().equals(productType)) {
            productTypeCount.computeIfPresent("dailySettlement", (k, v) -> v + 1);
        } else if (ProductEnum.MONTHLY_STATEMENT.getStatus().equals(productType)) {
            productTypeCount.computeIfPresent("monthlyStatement", (k, v) -> v + 1);
        } else if (ProductEnum.LONG_TIME.getStatus().equals(productType)) {
            productTypeCount.computeIfPresent("longTime", (k, v) -> v + 1);
        } else if (ProductEnum.OTHER.getStatus().equals(productType)) {
            productTypeCount.computeIfPresent("other", (k, v) -> v + 1);
        } else if (ProductEnum.COMBINATION.getStatus().equals(productType)) {
            productTypeCount.computeIfPresent("combination", (k, v) -> v + 1);
        } else if (ProductEnum.PAID_CARD.getStatus().equals(productType)) {
            productTypeCount.computeIfPresent("paidCard", (k, v) -> v + 1);
        }
    }

    private void ensureSelfAgentProduct(Product product, AgentAccount agentAccount) throws BizException {
        if (product == null || agentAccount == null || StringUtils.isEmpty(product.getProductCode())) {
            return;
        }

        AgentProduct existing = agentProductService.getOne(
                new LambdaQueryWrapper<AgentProduct>()
                        .eq(AgentProduct::getParentProductCode, product.getProductCode())
                        .eq(AgentProduct::getAgentCode, agentAccount.getAgentCode()),
                false);
        if (existing != null) {
            return;
        }

        AgentProduct selfAgentProduct = new AgentProduct();
        selfAgentProduct.setParentProductCode(product.getProductCode());
        selfAgentProduct.setParentProductName(product.getProductName());
        selfAgentProduct.setParentAgentCode(agentAccount.getParentAgentCode());
        selfAgentProduct.setAgentCode(agentAccount.getAgentCode());
        selfAgentProduct.setAgentName(agentAccount.getAgentName());
        selfAgentProduct.setIsAllAgent(product.getIsAllAgent());
        selfAgentProduct.setProductStatus(product.getProductStatus());
        selfAgentProduct.setProductCommission(product.getProductCommission());

        agentProductService.addAgentProduct(selfAgentProduct);
    }

    private String buildAgentProductKey(String agentCode, String parentAgentCode) {
        String agent = agentCode == null ? "" : agentCode;
        String parent = parentAgentCode == null ? "" : parentAgentCode;
        return agent + "#" + parent;
    }

    private void validateCommissionSetting(Product product, Integer targetCommission) throws BizException {
        if (product == null) {
            return;
        }
        if (Objects.equals(product.getSfyjfx(), BaseConstant.ZERO_INT)) {
            int commission = targetCommission == null ? BaseConstant.ZERO_INT : targetCommission;
            if (commission > BaseConstant.ZERO_INT) {
                throw new BizException("当前产品未开启佣金返现，请将基础佣金调整为0");
            }
        }
    }
}
