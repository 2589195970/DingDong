package com.ruoyi.console.service.impl.agent;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.constant.CacheKeyConstants;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.PageResultFactory;
import com.ruoyi.common.enums.ProductEnum;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.AgentProductCardFeeChildrenQueryBO;
import com.ruoyi.common.order.bo.AgentProductCardFeeOverrideCancelBO;
import com.ruoyi.common.order.bo.AgentProductCardFeeOverrideCreateBO;
import com.ruoyi.common.order.bo.AgentProductCardFeeOverrideQueryBO;
import com.ruoyi.common.order.bo.AgentProductSelectBO;
import com.ruoyi.common.order.bo.AgentProductUpdateBO;
import com.ruoyi.common.order.bo.AgentProductUpdateCardFeeBO;
import com.ruoyi.common.order.entity.*;
import com.ruoyi.common.order.vo.AgentProductCardFeeChildVO;
import com.ruoyi.common.order.vo.AgentProductCardFeeOverrideVO;
import com.ruoyi.common.order.vo.AgentProductVO;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.console.mapper.AgentProductCardFeeOverrideMapper;
import com.ruoyi.console.mapper.AgentProductMapper;
import com.ruoyi.console.mapper.ProductMapper;
import com.ruoyi.console.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 代理商产品相关接口
 *
 * @Description
 * @Author 陈思伟
 * @Date 2025/1/24 17:13
 */
@Service
@Slf4j
public class AgentProductServiceImpl extends ServiceImpl<AgentProductMapper, AgentProduct> implements AgentProductService {

    @Resource
    ProductMapper productMapper;

    @Resource
    AgentAccountService agentAccountService;

    @Resource
    QrCodeService qrCodeService;

    @Resource
    PictureService pictureService;

    @Resource
    AgentProductCardFeeOverrideMapper cardFeeOverrideMapper;

    @Value(value = "${submit.h5}")
    private String h5url;

    private static final int BATCH_INSERT_SIZE = 500;
    private static final int OVERRIDE_ACTIVE = 1;
    private static final int OVERRIDE_INACTIVE = 0;
    private static final int AGENT_CHILD_QUERY_BATCH_SIZE = 100;
    private static final int CARD_FEE_DEFAULT_MARGIN = 20;
    
    @Resource
    ToolConfigService toolConfigService;

    @Resource(name = "configStringRedisTemplate")
    private StringRedisTemplate configStringRedisTemplate;

    /**
     * 代理商分页产品查询
     *
     * @param agentProductSelectBO
     * @param loginUser
     * @return
     * @throws BizException
     */
    @Override
    public PageResult<AgentProductVO> agentSelectProductListPage(AgentProductSelectBO agentProductSelectBO, LoginUser loginUser) throws BizException {
        //获取代理账户信息
        AgentAccount agentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(), true);
        //查询总数
        Integer totalRows = productMapper.selectAgentProductListCount(
                agentProductSelectBO.getOperatorType(),
                agentProductSelectBO.getProductStatus(),
                agentProductSelectBO.getProductType(),
                agentProductSelectBO.getProductName(),
                agentAccount.getAgentCode());
        if (totalRows == null || totalRows == BaseConstant.ZERO_INT) {
            return PageResultFactory.createPageResult(new ArrayList<>(), 0L, agentProductSelectBO.getPageSize(), agentProductSelectBO.getPageNo());
        }
        //分页查询代理商产品
        List<AgentProductVO> agentProductSelectVOS = productMapper.selectAgentProductList(
                agentProductSelectBO.getOperatorType(),
                agentProductSelectBO.getProductStatus(),
                agentProductSelectBO.getProductType(),
                agentProductSelectBO.getProductName(),
                agentAccount.getAgentCode(),
                (agentProductSelectBO.getPageNo() - 1) * agentProductSelectBO.getPageSize(),
                agentProductSelectBO.getPageSize());
        for (AgentProductVO agentProductVO : agentProductSelectVOS) {
            agentProductVO.setH5Url(h5url + "?productCode=" + agentProductVO.getProductCode() + "&agentCode=" + agentAccount.getAgentCode());
            List<AgentProduct> agentProductList = baseMapper.selectList(new LambdaQueryWrapper<AgentProduct>()
                    .eq(AgentProduct::getParentProductCode, agentProductVO.getProductCode())
                    .eq(AgentProduct::getParentAgentCode, agentAccount.getAgentCode()));
            if (!CollectionUtils.isEmpty(agentProductList)) {
                List<String> agentCodeList = agentProductList.stream().map(AgentProduct::getAgentCode).collect(Collectors.toList());
                agentProductVO.setAgentCodeList(agentCodeList);
            } else {
                agentProductVO.setAgentCodeList(new ArrayList<>());
            }
            boolean commissionDisabled = Objects.equals(agentProductVO.getSfyjfx(), BaseConstant.ZERO_INT);
            if (commissionDisabled) {
                agentProductVO.setProductCommission(BaseConstant.ZERO_INT);
                agentProductVO.setDistributionProductCommission(BaseConstant.ZERO_INT);
                agentProductVO.setRevenueProductCommission(BaseConstant.ZERO_INT);
                agentProductVO.setVipFixedCommission(BaseConstant.ZERO_INT);
                agentProductVO.setVipBonusCommission(BaseConstant.ZERO_INT);
                agentProductVO.setSelfCommission(BaseConstant.ZERO_INT);
                agentProductVO.setDownstreamCommission(BaseConstant.ZERO_INT);
            }
            int upstreamAmount = agentProductVO.getProductCommission() == null ? BaseConstant.ZERO_INT : Math.max(agentProductVO.getProductCommission(), BaseConstant.ZERO_INT);
            int retainAmount = agentProductVO.getRevenueProductCommission() == null ? BaseConstant.ZERO_INT : Math.max(agentProductVO.getRevenueProductCommission(), BaseConstant.ZERO_INT);
            int downstreamAmount = agentProductVO.getDistributionProductCommission() == null ? BaseConstant.ZERO_INT : Math.max(agentProductVO.getDistributionProductCommission(), BaseConstant.ZERO_INT);

            // VIP佣金计算逻辑
            // 1. 获取VIP固定加成（已从SQL查询获取，若为null则默认0）
            int vipFixed = agentProductVO.getVipFixedCommission() == null ? BaseConstant.ZERO_INT : Math.max(agentProductVO.getVipFixedCommission(), BaseConstant.ZERO_INT);

            // 2. 计算实际可得的VIP加成（不能超过上游可分配余额）
            // 公式：min(VIP固定加成, 上游佣金 - 基础保留佣金)
            int availableForVip = upstreamAmount - retainAmount;
            int vipBonus = Math.min(vipFixed, availableForVip);
            vipBonus = Math.max(vipBonus, BaseConstant.ZERO_INT); // 确保非负

            // 3. 重新计算各项佣金
            int actualSelfCommission = retainAmount + vipBonus;           // 本级收益 = 基础保留 + VIP加成
            int actualDownstream = upstreamAmount - actualSelfCommission; // 下游可分配 = 上游总额 - 本级收益

            // 4. 设置到VO中
            agentProductVO.setUpstreamCommission(upstreamAmount);
            agentProductVO.setVipFixedCommission(vipFixed);                                    // VIP配置的固定加成
            agentProductVO.setVipBonusCommission(vipBonus);                                   // 实际预估加成
            agentProductVO.setSelfCommission(actualSelfCommission);                           // 本级收益（含VIP）
            agentProductVO.setDownstreamCommission(Math.max(actualDownstream, BaseConstant.ZERO_INT)); // 下游可分配
        }
        return PageResultFactory.createPageResult(agentProductSelectVOS, Long.valueOf(totalRows), agentProductSelectBO.getPageSize(), agentProductSelectBO.getPageNo());
    }


    /**
     * 新增代理商产品记录
     *
     * @param agentProduct
     */
    public void addAgentProduct(AgentProduct agentProduct) throws BizException {
        if (agentProduct == null) {
            throw new BizException("参数不能为空");
        }
        if (agentProduct.getProductStatus() == null) {
            agentProduct.setProductStatus(BaseConstant.ONE_INT);
        }
        if (agentProduct.getProductCommission() == null) {
            agentProduct.setProductCommission(BaseConstant.ZERO_INT);
        }
        if (agentProduct.getDistributionProductCommission() == null) {
            agentProduct.setDistributionProductCommission(BaseConstant.ZERO_INT);
        }
        if (agentProduct.getRevenueProductCommission() == null) {
            agentProduct.setRevenueProductCommission(agentProduct.getProductCommission()-agentProduct.getDistributionProductCommission());
        }
        if (agentProduct.getIsAllAgent() == null) {
            agentProduct.setIsAllAgent(BaseConstant.ZERO_INT);
        }
        Product product = productMapper.selectOne(new LambdaQueryWrapper<Product>().eq(Product::getProductCode, agentProduct.getParentProductCode()));
        if (product == null) {
            throw new BizException("产品不存在");
        }
        int incomingCardFee = computeIncomingCardFee(product, agentProduct.getParentAgentCode());
        agentProduct.setIncomingCardFee(incomingCardFee);
        int downstreamCardFee = resolveDefaultDownstream(product, incomingCardFee);
        agentProduct.setDownstreamCardFee(downstreamCardFee);
        agentProduct.setCardFeeProfit(downstreamCardFee - incomingCardFee);
        agentProduct.setProductSort(BaseConstant.ZERO_INT);
        agentProduct.setCreateTime(System.currentTimeMillis());
        baseMapper.insert(agentProduct);
    }

    /**
     * 根据父代理商新增
     * 代理商账号注册默认产品
     * admin 需要判断默认上架开放产品
     */
    public void addRegisterAccountProduct(AgentAccount agentAccount) throws BizException {
        AgentAccount parentAgent = agentAccountService.getAgentAccountByCode(agentAccount.getParentAgentCode(),false);
        if(parentAgent.getSysUserId() == 1L){
            //查询所有上架产品
            List<Product> productList = productMapper.selectList(new LambdaQueryWrapper<Product>().eq(Product::getIsAllAgent,BaseConstant.ONE_INT).eq(Product::getProductStatus,BaseConstant.ONE_INT));
            if(CollectionUtils.isEmpty(productList)){
                return;
            }
            //添加默认产品
            for(Product product:productList){
                AgentProduct addAgentProduct = new AgentProduct();
                addAgentProduct.setParentProductCode(product.getProductCode());
                addAgentProduct.setParentProductName(product.getProductName());
                addAgentProduct.setParentAgentCode(parentAgent.getAgentCode());
                addAgentProduct.setAgentCode(agentAccount.getAgentCode());
                addAgentProduct.setAgentName(agentAccount.getAgentName());
                addAgentProduct.setProductStatus(BaseConstant.ONE_INT);
                addAgentProduct.setProductCommission(product.getProductCommission());
                addAgentProduct.setDistributionProductCommission(BaseConstant.ZERO_INT);
                addAgentProduct.setRevenueProductCommission(addAgentProduct.getProductCommission() - addAgentProduct.getDistributionProductCommission());
                addAgentProduct(addAgentProduct);
                //生成海报图
                addSubAgentProductPoster(agentAccount,product);
            }

            return;
        }
        //查询父代理的产品 只要上架的
        List<AgentProduct> agentProductList = baseMapper.selectList(new LambdaQueryWrapper<AgentProduct>().eq(AgentProduct::getAgentCode,agentAccount.getParentAgentCode()).eq(AgentProduct::getProductStatus,BaseConstant.ONE_INT));
        if(CollectionUtils.isEmpty(agentProductList)){
            return;
        }
        //添加默认产品
        for(AgentProduct agentProduct:agentProductList){
            AgentProduct addAgentProduct = new AgentProduct();
            addAgentProduct.setParentProductCode(agentProduct.getParentProductCode());
            addAgentProduct.setParentProductName(agentProduct.getParentProductName());
            addAgentProduct.setParentAgentCode(agentProduct.getAgentCode());
            addAgentProduct.setAgentCode(agentAccount.getAgentCode());
            addAgentProduct.setAgentName(agentAccount.getAgentName());
            addAgentProduct.setProductStatus(agentProduct.getProductStatus());
            addAgentProduct.setProductCommission(agentProduct.getProductCommission());
            addAgentProduct.setDistributionProductCommission(BaseConstant.ZERO_INT);
            addAgentProduct.setRevenueProductCommission(addAgentProduct.getProductCommission() - addAgentProduct.getDistributionProductCommission());
            addAgentProduct(addAgentProduct);
            //重新计算佣金
            updateAgentProductCommission(agentProduct.getParentProductCode(),agentProduct.getAgentCode(),agentProduct.getProductCommission());
            //生成海报图
            Product product = productMapper.selectOne(new LambdaQueryWrapper<Product>().eq(Product::getProductCode,agentProduct.getParentProductCode()));
            addSubAgentProductPoster(agentAccount,product);
        }

    }


    /**
     * 删除代理商产品
     */
    public void deleteAgentProduct(Integer agentProductId) {
        baseMapper.deleteById(agentProductId);

    }


    /**
     * 修改代理商产品固定佣金，仅更新当前代理记录。
     * 子级需要在各自触发更新时写入新的固定佣金。
     */
    public void updateAgentProductCommission(String productCode, String agentCode, Integer commission) throws BizException {
        AgentAccount agentAccount = agentAccountService.getAgentAccountByCode(agentCode, true);
        Product product = productMapper.selectOne(new LambdaQueryWrapper<Product>()
                .eq(Product::getProductCode, productCode));
        if (product == null) {
            throw new BizException("产品不存在");
        }
        int safeCommission = commission == null ? BaseConstant.ZERO_INT : Math.max(commission, BaseConstant.ZERO_INT);
        if (Objects.equals(product.getSfyjfx(), BaseConstant.ZERO_INT) && safeCommission > BaseConstant.ZERO_INT) {
            throw new BizException("当前产品未开启佣金返现，请将基础佣金调整为0");
        }

        AgentProduct agentProduct = baseMapper.selectOne(new LambdaQueryWrapper<AgentProduct>()
                .eq(AgentProduct::getParentProductCode, productCode)
                .eq(AgentProduct::getAgentCode, agentAccount.getAgentCode()));
        if (agentProduct == null) {
            return;
        }
        int incomingAmount = Objects.equals(product.getSfyjfx(), BaseConstant.ZERO_INT)
                ? BaseConstant.ZERO_INT
                : safeCommission;
        agentProduct.setProductCommission(incomingAmount);
        agentProduct.setRevenueProductCommission(incomingAmount);
        agentProduct.setDistributionProductCommission(BaseConstant.ZERO_INT);
        agentProduct.setUpdateTime(System.currentTimeMillis());
        baseMapper.updateById(agentProduct);
        clearCommissionCache(agentAccount.getAgentCode(), productCode);

        // 固定佣金只作用于当前代理；子级需要在自身触发更新时写入新的固定值。
    }




    /**
     * 代理商 分配产品下游
     * @return
     * @throws BizException
     */
    @Override
    public void updateAgentProduct(AgentProductUpdateBO agentProductUpdateBO, LoginUser loginUser) throws BizException {
        if(agentProductUpdateBO==null||agentProductUpdateBO.getProductId()==null){
            throw new BizException("产品ID不能为空");
        }
        Product product = productMapper.selectById(agentProductUpdateBO.getProductId());
        if(product==null){
            throw new BizException("产品{}不存在",agentProductUpdateBO.getProductId());
        }

        //判断是否新增或删除产品
        AgentAccount parentAgentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(),true);
        //传过来是空值 只需要考虑删除即可
        if(CollectionUtils.isEmpty(agentProductUpdateBO.getAgentCodeList())){
            List<AgentProduct> agentProductList = baseMapper.selectList(new LambdaQueryWrapper<AgentProduct>().eq(AgentProduct::getParentProductCode,product.getProductCode()).eq(AgentProduct::getParentAgentCode,parentAgentAccount.getAgentCode()));
            for(AgentProduct agentProduct:agentProductList){
                deleteAgentProduct(agentProduct.getAgentProductId());
            }
        }
        //不为空 计算差值 新增或删除产品
        if(!CollectionUtils.isEmpty(agentProductUpdateBO.getAgentCodeList())){
            //转为map
            List<AgentProduct> agentProductList = baseMapper.selectList(new LambdaQueryWrapper<AgentProduct>().eq(AgentProduct::getParentProductCode,product.getProductCode()).eq(AgentProduct::getParentAgentCode,parentAgentAccount.getAgentCode()));
            Map<String,AgentProduct> agentProductListMap = agentProductList.stream().collect(Collectors.toMap(AgentProduct::getAgentCode, Function.identity()));
            Map<String,String> agentProductStrMap =agentProductUpdateBO.getAgentCodeList().stream().collect(Collectors.toMap(Function.identity(), Function.identity()));
            //需新增代理商产品
            for (String agentCode:agentProductUpdateBO.getAgentCodeList()){
                if(agentProductListMap.get(agentCode)==null){
                    AgentAccount agentAccount = agentAccountService.getAgentAccountByCode(agentCode,true);
                    AgentProduct agentProduct = new AgentProduct();
                    agentProduct.setParentProductCode(product.getProductCode());
                    agentProduct.setParentProductName(product.getProductName());
                    agentProduct.setParentAgentCode(parentAgentAccount.getAgentCode());
                    agentProduct.setAgentCode(agentAccount.getAgentCode());
                    agentProduct.setAgentName(agentAccount.getAgentName());
                    agentProduct.setProductStatus(BaseConstant.ONE_INT);
                    addAgentProduct(agentProduct);
                }
            }
            //需删除代理产品
            for(AgentProduct agentProduct:agentProductList){
                if(agentProductStrMap.get(agentProduct.getAgentCode())==null){
                    deleteAgentProduct(agentProduct.getAgentProductId());
                }
            }
        }
    }


    @Override
    public AgentProduct updateAgentProductCardFee(AgentProductUpdateCardFeeBO request, LoginUser loginUser) throws BizException {
        if (request == null || request.getAgentProductId() == null) {
            throw new BizException("参数不能为空");
        }
        AgentProduct agentProduct = baseMapper.selectById(request.getAgentProductId());
        if (agentProduct == null) {
            throw new BizException("代理产品不存在");
        }
        Product product = productMapper.selectOne(new LambdaQueryWrapper<Product>().eq(Product::getProductCode, agentProduct.getParentProductCode()));
        if (!isCardFeeEnabled(product)) {
            throw new BizException("当前产品未开启提卡费");
        }


        int targetDownstream = safeCardFee(request.getDownstreamCardFee());
        long now = System.currentTimeMillis();

        LambdaQueryWrapper<AgentProduct> childrenWrapper = new LambdaQueryWrapper<AgentProduct>()
                .eq(AgentProduct::getParentProductCode, agentProduct.getParentProductCode());
        String parentAgentCode = agentProduct.getParentAgentCode();
        if (StringUtils.hasLength(parentAgentCode)) {
            childrenWrapper.eq(AgentProduct::getParentAgentCode, parentAgentCode);
        } else {
            childrenWrapper.nested(wrapper -> wrapper.isNull(AgentProduct::getParentAgentCode)
                    .or().eq(AgentProduct::getParentAgentCode, ""));
        }

        List<AgentProduct> directChildren = baseMapper.selectList(childrenWrapper);
        if (CollectionUtils.isEmpty(directChildren)) {
            directChildren = Collections.singletonList(agentProduct);
        }

        for (AgentProduct child : directChildren) {
            int childIncoming = safeCardFee(child.getIncomingCardFee());
            if (targetDownstream < childIncoming) {
                throw new BizException("对下提卡费不能低于当前成本" + childIncoming + "元");
            }
        }

        for (AgentProduct child : directChildren) {
            int normalizedIncoming = targetDownstream;
            int normalizedDownstream = resolveDefaultDownstream(product, normalizedIncoming);
            int childProfit = normalizedDownstream - normalizedIncoming;
            baseMapper.update(null, new LambdaUpdateWrapper<AgentProduct>()
                    .set(AgentProduct::getIncomingCardFee, normalizedIncoming)
                    .set(AgentProduct::getDownstreamCardFee, normalizedDownstream)
                    .set(AgentProduct::getCardFeeProfit, childProfit)
                    .set(AgentProduct::getUpdateTime, now)
                    .eq(AgentProduct::getAgentProductId, child.getAgentProductId()));

            child.setIncomingCardFee(normalizedIncoming);
            child.setDownstreamCardFee(normalizedDownstream);
            child.setCardFeeProfit(childProfit);
            child.setUpdateTime(now);

            refreshChildCardFee(product, child.getAgentCode(), normalizedDownstream, now);
        }

        agentProduct.setDownstreamCardFee(targetDownstream);
        agentProduct.setCardFeeProfit(targetDownstream - safeCardFee(agentProduct.getIncomingCardFee()));
        agentProduct.setUpdateTime(now);
        return agentProduct;
    }

    @Override
    public List<AgentProductCardFeeChildVO> listAgentProductCardFeeChildren(AgentProductCardFeeChildrenQueryBO request, LoginUser loginUser) throws BizException {
        if (request == null || request.getAgentProductId() == null) {
            throw new BizException("代理产品ID不能为空");
        }
        AgentAccount parentAgentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(), true);
        AgentProduct parentAgentProduct = baseMapper.selectById(request.getAgentProductId());
        if (parentAgentProduct == null) {
            throw new BizException("代理产品不存在");
        }

        String loginAgentCode = normalizeAgentCode(parentAgentAccount.getAgentCode());
        if (!Objects.equals(normalizeAgentCode(parentAgentProduct.getAgentCode()), loginAgentCode)) {
            throw new BizException("仅可查看自身产品的下级数据");
        }

        List<AgentProduct> children = baseMapper.selectList(new LambdaQueryWrapper<AgentProduct>()
                .eq(AgentProduct::getParentProductCode, parentAgentProduct.getParentProductCode())
                .eq(AgentProduct::getParentAgentCode, loginAgentCode)
                .orderByAsc(AgentProduct::getAgentName));
        if (CollectionUtils.isEmpty(children)) {
            return Collections.emptyList();
        }
        return children.stream().map(child -> {
            AgentProductCardFeeChildVO vo = new AgentProductCardFeeChildVO();
            vo.setAgentProductId(child.getAgentProductId());
            vo.setAgentCode(child.getAgentCode());
            vo.setAgentName(child.getAgentName());
            vo.setIncomingCardFee(safeCardFee(child.getIncomingCardFee()));
            vo.setDownstreamCardFee(safeCardFee(child.getDownstreamCardFee()));
            vo.setCardFeeProfit(safeCardFee(child.getCardFeeProfit()));
            vo.setHasOverride((child.getHasOverride() != null && child.getHasOverride() == BaseConstant.ONE_INT)
                    ? BaseConstant.ONE_INT
                    : BaseConstant.ZERO_INT);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<AgentProductCardFeeOverrideVO> listAgentProductCardFeeOverrides(AgentProductCardFeeOverrideQueryBO request, LoginUser loginUser) throws BizException {
        if (request == null || !StringUtils.hasLength(request.getProductCode())) {
            throw new BizException("商品编码不能为空");
        }
        AgentAccount parentAgentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(), true);
        String parentAgentCode = normalizeAgentCode(parentAgentAccount.getAgentCode());
        List<AgentProductCardFeeOverride> overrides = cardFeeOverrideMapper.selectList(new LambdaQueryWrapper<AgentProductCardFeeOverride>()
                .eq(AgentProductCardFeeOverride::getProductCode, request.getProductCode())
                .eq(AgentProductCardFeeOverride::getParentAgentCode, parentAgentCode)
                .orderByDesc(AgentProductCardFeeOverride::getStatus)
                .orderByDesc(AgentProductCardFeeOverride::getUpdateTime));
        if (CollectionUtils.isEmpty(overrides)) {
            return Collections.emptyList();
        }
        Map<String, String> agentNameMap = buildAgentNameMap(overrides.stream()
                .map(AgentProductCardFeeOverride::getTargetAgentCode)
                .collect(Collectors.toSet()));
        return overrides.stream()
                .map(item -> buildOverrideVO(item, agentNameMap))
                .collect(Collectors.toList());
    }

    @Override
    public void upsertAgentProductCardFeeOverride(AgentProductCardFeeOverrideCreateBO request, LoginUser loginUser) throws BizException {
        if (request == null || request.getAgentProductId() == null) {
            throw new BizException("目标代理产品不能为空");
        }
        AgentAccount parentAgentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(), true);
        AgentProduct targetAgentProduct = baseMapper.selectById(request.getAgentProductId());
        if (targetAgentProduct == null) {
            throw new BizException("代理产品不存在");
        }
        String parentAgentCode = normalizeAgentCode(parentAgentAccount.getAgentCode());
        if (!Objects.equals(normalizeAgentCode(targetAgentProduct.getParentAgentCode()), parentAgentCode)) {
            throw new BizException("仅可操作直属下级提卡费");
        }
        Product product = productMapper.selectOne(new LambdaQueryWrapper<Product>()
                .eq(Product::getProductCode, targetAgentProduct.getParentProductCode()));
        if (product == null) {
            throw new BizException("产品不存在");
        }
        if (!isCardFeeEnabled(product)) {
            throw new BizException("当前产品未开启提卡费");
        }
        AgentProduct parentAgentProduct = baseMapper.selectOne(new LambdaQueryWrapper<AgentProduct>()
                .eq(AgentProduct::getAgentCode, parentAgentCode)
                .eq(AgentProduct::getParentProductCode, targetAgentProduct.getParentProductCode())
                .last("LIMIT 1"));
        int currentDownstream = safeCardFee(targetAgentProduct.getDownstreamCardFee());
        int parentIncoming = parentAgentProduct != null
                ? safeCardFee(parentAgentProduct.getIncomingCardFee())
                : resolveBaseCardFee(product);
        int overrideFee = safeCardFee(request.getOverrideFee());
        if (overrideFee < parentIncoming) {
            throw new BizException("特例售价不能低于当前成本" + parentIncoming + "元");
        }
        long now = System.currentTimeMillis();
        String operator = loginUser.getUsername();

        AgentProductCardFeeOverride activeOverride = cardFeeOverrideMapper.selectOne(new LambdaQueryWrapper<AgentProductCardFeeOverride>()
                .eq(AgentProductCardFeeOverride::getProductCode, targetAgentProduct.getParentProductCode())
                .eq(AgentProductCardFeeOverride::getParentAgentCode, parentAgentCode)
                .eq(AgentProductCardFeeOverride::getTargetAgentCode, targetAgentProduct.getAgentCode())
                .eq(AgentProductCardFeeOverride::getStatus, OVERRIDE_ACTIVE));
        if (activeOverride == null) {
            AgentProductCardFeeOverride newOverride = new AgentProductCardFeeOverride();
            newOverride.setProductCode(targetAgentProduct.getParentProductCode());
            newOverride.setParentAgentCode(parentAgentCode);
            newOverride.setTargetAgentCode(targetAgentProduct.getAgentCode());
            newOverride.setOverrideFee(overrideFee);
            newOverride.setIncomingCardFee(parentIncoming);
            newOverride.setStatus(OVERRIDE_ACTIVE);
            newOverride.setEffectiveTime(now);
            newOverride.setExpireTime(request.getExpireTime());
            newOverride.setMemo(request.getMemo());
            newOverride.setOperator(operator);
            newOverride.setCreateTime(now);
            newOverride.setUpdateTime(now);
            cardFeeOverrideMapper.insert(newOverride);
        } else {
            cardFeeOverrideMapper.update(null, new LambdaUpdateWrapper<AgentProductCardFeeOverride>()
                    .set(AgentProductCardFeeOverride::getOverrideFee, overrideFee)
                    .set(AgentProductCardFeeOverride::getIncomingCardFee, parentIncoming)
                    .set(AgentProductCardFeeOverride::getExpireTime, request.getExpireTime())
                    .set(AgentProductCardFeeOverride::getMemo, request.getMemo())
                    .set(AgentProductCardFeeOverride::getOperator, operator)
                    .set(AgentProductCardFeeOverride::getUpdateTime, now)
                    .eq(AgentProductCardFeeOverride::getOverrideId, activeOverride.getOverrideId()));
        }

        cardFeeOverrideMapper.update(null, new LambdaUpdateWrapper<AgentProductCardFeeOverride>()
                .set(AgentProductCardFeeOverride::getStatus, OVERRIDE_ACTIVE)
                .set(AgentProductCardFeeOverride::getEffectiveTime, now)
                .set(AgentProductCardFeeOverride::getOperator, operator)
                .set(AgentProductCardFeeOverride::getUpdateTime, now)
                .eq(AgentProductCardFeeOverride::getProductCode, targetAgentProduct.getParentProductCode())
                .eq(AgentProductCardFeeOverride::getParentAgentCode, parentAgentCode)
                .eq(AgentProductCardFeeOverride::getTargetAgentCode, targetAgentProduct.getAgentCode())
                .eq(AgentProductCardFeeOverride::getStatus, OVERRIDE_ACTIVE));

        int newProfit = currentDownstream - overrideFee;
        baseMapper.update(null, new LambdaUpdateWrapper<AgentProduct>()
                .set(AgentProduct::getIncomingCardFee, overrideFee)
                .set(AgentProduct::getCardFeeProfit, newProfit)
                .set(AgentProduct::getHasOverride, BaseConstant.ONE_INT)
                .set(AgentProduct::getUpdateTime, now)
                .eq(AgentProduct::getAgentProductId, targetAgentProduct.getAgentProductId()));
    }

    @Override
    public void cancelAgentProductCardFeeOverride(AgentProductCardFeeOverrideCancelBO request, LoginUser loginUser) throws BizException {
        if (request == null || request.getOverrideId() == null) {
            throw new BizException("特例ID不能为空");
        }
        AgentAccount parentAgentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(), true);
        AgentProductCardFeeOverride override = cardFeeOverrideMapper.selectById(request.getOverrideId());
        if (override == null || !Objects.equals(normalizeAgentCode(override.getParentAgentCode()), normalizeAgentCode(parentAgentAccount.getAgentCode()))) {
            throw new BizException("提卡费特例不存在或无操作权限");
        }
        if (Objects.equals(override.getStatus(), OVERRIDE_INACTIVE)) {
            return;
        }
        long now = System.currentTimeMillis();
        // 清理历史的失效记录，避免唯一索引冲突
        cardFeeOverrideMapper.delete(new LambdaQueryWrapper<AgentProductCardFeeOverride>()
                .eq(AgentProductCardFeeOverride::getProductCode, override.getProductCode())
                .eq(AgentProductCardFeeOverride::getParentAgentCode, override.getParentAgentCode())
                .eq(AgentProductCardFeeOverride::getTargetAgentCode, override.getTargetAgentCode())
                .eq(AgentProductCardFeeOverride::getStatus, OVERRIDE_INACTIVE)
                .ne(AgentProductCardFeeOverride::getOverrideId, override.getOverrideId()));
        cardFeeOverrideMapper.update(null, new LambdaUpdateWrapper<AgentProductCardFeeOverride>()
                .set(AgentProductCardFeeOverride::getStatus, OVERRIDE_INACTIVE)
                .set(AgentProductCardFeeOverride::getExpireTime, now)
                .set(AgentProductCardFeeOverride::getOperator, loginUser.getUsername())
                .set(AgentProductCardFeeOverride::getUpdateTime, now)
                .eq(AgentProductCardFeeOverride::getOverrideId, override.getOverrideId()));

        AgentProduct targetAgentProduct = baseMapper.selectOne(new LambdaQueryWrapper<AgentProduct>()
                .eq(AgentProduct::getParentProductCode, override.getProductCode())
                .eq(AgentProduct::getAgentCode, override.getTargetAgentCode())
                .last("LIMIT 1"));
        if (targetAgentProduct == null) {
            return;
        }
        Product product = productMapper.selectOne(new LambdaQueryWrapper<Product>()
                .eq(Product::getProductCode, override.getProductCode()));
        if (product == null) {
            return;
        }
        int parentDownstream = resolveParentDownstreamCardFee(parentAgentAccount, product);
        int newIncoming = Math.max(parentDownstream, BaseConstant.ZERO_INT);
        int currentDownstream = safeCardFee(targetAgentProduct.getDownstreamCardFee());
        int newProfit = currentDownstream - newIncoming;
        baseMapper.update(null, new LambdaUpdateWrapper<AgentProduct>()
                .set(AgentProduct::getIncomingCardFee, newIncoming)
                .set(AgentProduct::getCardFeeProfit, newProfit)
                .set(AgentProduct::getHasOverride, BaseConstant.ZERO_INT)
                .set(AgentProduct::getUpdateTime, now)
                .eq(AgentProduct::getAgentProductId, targetAgentProduct.getAgentProductId()));
    }


    /**
     * 批量创建代理商产品（包含所有下级）
     *
     * @param agentAccounts       待分配代理列表
     * @param parentAgentAccount  上级代理
     * @param product             商品
     * @param productCommission   上级佣金
     * @throws BizException 业务异常
     */
    @Override
    public void addSubAgentProducts(List<AgentAccount> agentAccounts, AgentAccount parentAgentAccount, Product product, Integer productCommission) throws BizException {
        if (CollectionUtils.isEmpty(agentAccounts) || product == null || parentAgentAccount == null) {
            return;
        }
        AgentHierarchySnapshot hierarchySnapshot = buildAgentHierarchy(agentAccounts);
        Map<String, AgentProduct> existingProductMap = loadExistingAgentProducts(product.getProductCode(), hierarchySnapshot.getAllAgentCodes());
        List<AgentProduct> pendingInsert = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        int parentDownstreamCardFee = resolveParentDownstreamCardFee(parentAgentAccount, product);
        for (AgentAccount agentAccount : agentAccounts) {
            collectAgentProducts(agentAccount,
                    parentAgentAccount,
                    product,
                    productCommission,
                    parentDownstreamCardFee,
                    pendingInsert,
                    visited,
                    existingProductMap,
                    hierarchySnapshot.getChildMap());
        }
        batchInsertAgentProducts(pendingInsert);

        if (CollectionUtils.isEmpty(pendingInsert)) {
            return;
        }
        for (AgentProduct agentProduct : pendingInsert) {
            StringBuffer stringBuffer = new StringBuffer(h5url);
            stringBuffer.append("?productCode=").append(agentProduct.getParentProductCode())
                    .append("&agentCode=").append(agentProduct.getAgentCode());
            createAgentProductPoster(agentProduct, product.getProductMasterMap(), stringBuffer.toString());
        }
    }

    /**
     * 保留单个新增接口，内部委托批量方法实现，兼容旧调用
     */
    @Override
    public void addSubAgentProduct(AgentAccount agentAccount, AgentAccount parentAgentAccount, Product product, Integer productCommission) throws BizException {
        if (agentAccount == null) {
            return;
        }
        addSubAgentProducts(Collections.singletonList(agentAccount), parentAgentAccount, product, productCommission);
    }

    private void collectAgentProducts(AgentAccount agentAccount,
                                      AgentAccount parentAgentAccount,
                                      Product product,
                                      Integer parentCommission,
                                      Integer parentDownstreamCardFee,
                                      List<AgentProduct> pendingInsert,
                                      Set<String> visited,
                                      Map<String, AgentProduct> existingProductMap,
                                      Map<String, List<AgentAccount>> childAgentMap) throws BizException {
        if (agentAccount == null || parentAgentAccount == null || product == null) {
            return;
        }

        String recordKey = buildAgentProductKey(agentAccount.getAgentCode(), parentAgentAccount.getAgentCode(), product.getProductCode());
        if (!visited.add(recordKey)) {
            return;
        }

        AgentProduct agentProduct = existingProductMap.get(recordKey);

        int currentCommission;
        int downstreamForChildren;
        if (agentProduct == null) {
            agentProduct = buildAgentProduct(agentAccount, parentAgentAccount, product, parentCommission, parentDownstreamCardFee);
            pendingInsert.add(agentProduct);
            existingProductMap.put(recordKey, agentProduct);
            currentCommission = agentProduct.getProductCommission() == null ? resolveCommission(parentCommission) : agentProduct.getProductCommission();
            downstreamForChildren = safeCardFee(agentProduct.getDownstreamCardFee());
        } else {
            currentCommission = agentProduct.getProductCommission() == null ? resolveCommission(parentCommission) : agentProduct.getProductCommission();
            downstreamForChildren = determineDownstream(agentProduct, product, parentDownstreamCardFee);
        }

        List<AgentAccount> childAgentAccountList = childAgentMap.get(agentAccount.getAgentCode());
        if (CollectionUtils.isEmpty(childAgentAccountList)) {
            return;
        }
        for (AgentAccount childAgentAccount : childAgentAccountList) {
            collectAgentProducts(childAgentAccount,
                    agentAccount,
                    product,
                    currentCommission,
                    downstreamForChildren,
                    pendingInsert,
                    visited,
                    existingProductMap,
                    childAgentMap);
        }
    }

    private AgentHierarchySnapshot buildAgentHierarchy(List<AgentAccount> rootAgents) {
        AgentHierarchySnapshot snapshot = new AgentHierarchySnapshot();
        if (CollectionUtils.isEmpty(rootAgents)) {
            return snapshot;
        }
        Queue<String> pending = new LinkedList<>();
        Set<String> processedParents = new HashSet<>();
        for (AgentAccount root : rootAgents) {
            if (root == null || !StringUtils.hasLength(root.getAgentCode())) {
                continue;
            }
            String agentCode = root.getAgentCode();
            if (snapshot.getAllAgentCodes().add(agentCode)) {
                pending.offer(agentCode);
            }
        }
        while (!pending.isEmpty()) {
            List<String> batch = new ArrayList<>();
            while (!pending.isEmpty() && batch.size() < AGENT_CHILD_QUERY_BATCH_SIZE) {
                String parentCode = pending.poll();
                if (processedParents.add(parentCode)) {
                    batch.add(parentCode);
                }
            }
            if (CollectionUtils.isEmpty(batch)) {
                continue;
            }
            List<AgentAccount> children = agentAccountService.list(new LambdaQueryWrapper<AgentAccount>()
                    .in(AgentAccount::getParentAgentCode, batch));
            if (CollectionUtils.isEmpty(children)) {
                continue;
            }
            Map<String, List<AgentAccount>> grouped = children.stream()
                    .filter(child -> StringUtils.hasLength(child.getParentAgentCode()))
                    .collect(Collectors.groupingBy(AgentAccount::getParentAgentCode));
            for (Map.Entry<String, List<AgentAccount>> entry : grouped.entrySet()) {
                snapshot.getChildMap()
                        .computeIfAbsent(entry.getKey(), key -> new ArrayList<>())
                        .addAll(entry.getValue());
            }
            for (AgentAccount child : children) {
                if (child == null || !StringUtils.hasLength(child.getAgentCode())) {
                    continue;
                }
                if (snapshot.getAllAgentCodes().add(child.getAgentCode())) {
                    pending.offer(child.getAgentCode());
                }
            }
        }
        return snapshot;
    }

    private Map<String, AgentProduct> loadExistingAgentProducts(String productCode, Set<String> agentCodes) {
        Map<String, AgentProduct> existing = new HashMap<>();
        if (!StringUtils.hasLength(productCode) || CollectionUtils.isEmpty(agentCodes)) {
            return existing;
        }
        List<AgentProduct> agentProducts = baseMapper.selectList(new LambdaQueryWrapper<AgentProduct>()
                .eq(AgentProduct::getParentProductCode, productCode)
                .in(AgentProduct::getAgentCode, agentCodes));
        if (CollectionUtils.isEmpty(agentProducts)) {
            return existing;
        }
        for (AgentProduct item : agentProducts) {
            String key = buildAgentProductKey(item.getAgentCode(), item.getParentAgentCode(), item.getParentProductCode());
            existing.putIfAbsent(key, item);
        }
        return existing;
    }

    private AgentProduct buildAgentProduct(AgentAccount agentAccount,
                                           AgentAccount parentAgentAccount,
                                           Product product,
                                           Integer parentCommission,
                                           Integer parentDownstreamCardFee) {
        AgentProduct agentProduct = new AgentProduct();
        agentProduct.setParentProductCode(product.getProductCode());
        agentProduct.setParentProductName(product.getProductName());
        agentProduct.setParentAgentCode(parentAgentAccount.getAgentCode());
        agentProduct.setAgentCode(agentAccount.getAgentCode());
        agentProduct.setAgentName(agentAccount.getAgentName());
        agentProduct.setIsAllAgent(BaseConstant.ZERO_INT);
        agentProduct.setProductStatus(product.getProductStatus() == null ? BaseConstant.ONE_INT : product.getProductStatus());

        int commission = resolveCommission(parentCommission);
        agentProduct.setProductCommission(commission);
        agentProduct.setDistributionProductCommission(BaseConstant.ZERO_INT);
        agentProduct.setRevenueProductCommission(commission);
        int incomingCardFee = resolveIncomingForChild(product, parentDownstreamCardFee);
        agentProduct.setIncomingCardFee(incomingCardFee);
        int downstreamCardFee = resolveDefaultDownstream(product, incomingCardFee);
        agentProduct.setDownstreamCardFee(downstreamCardFee);
        agentProduct.setCardFeeProfit(downstreamCardFee - incomingCardFee);
        agentProduct.setProductSort(BaseConstant.ZERO_INT);
        agentProduct.setCreateTime(System.currentTimeMillis());
        agentProduct.setUpdateTime(null);
        agentProduct.setProductQrcodeMap(null);
        return agentProduct;
    }

    private int resolveIncomingForChild(Product product, Integer parentDownstreamCardFee) {
        if (!isCardFeeEnabled(product)) {
            return BaseConstant.ZERO_INT;
        }
        if (parentDownstreamCardFee != null) {
            return Math.max(parentDownstreamCardFee, BaseConstant.ZERO_INT);
        }
        return resolveBaseCardFee(product);
    }

    private int computeIncomingCardFee(Product product, String parentAgentCode) {
        if (!isCardFeeEnabled(product)) {
            return BaseConstant.ZERO_INT;
        }
        if (!StringUtils.hasLength(parentAgentCode)) {
            return resolveBaseCardFee(product);
        }
        AgentProduct parentAgentProduct = baseMapper.selectOne(new LambdaQueryWrapper<AgentProduct>()
                .eq(AgentProduct::getAgentCode, parentAgentCode)
                .eq(AgentProduct::getParentProductCode, product.getProductCode()));
        if (parentAgentProduct != null && parentAgentProduct.getDownstreamCardFee() != null) {
            return Math.max(parentAgentProduct.getDownstreamCardFee(), BaseConstant.ZERO_INT);
        }
        return resolveBaseCardFee(product);
    }

    private int resolveParentDownstreamCardFee(AgentAccount parentAgentAccount, Product product) {
        if (!isCardFeeEnabled(product) || parentAgentAccount == null) {
            return BaseConstant.ZERO_INT;
        }
        AgentProduct parentProduct = baseMapper.selectOne(new LambdaQueryWrapper<AgentProduct>()
                .eq(AgentProduct::getAgentCode, parentAgentAccount.getAgentCode())
                .eq(AgentProduct::getParentProductCode, product.getProductCode()));
        if (parentProduct != null && parentProduct.getDownstreamCardFee() != null) {
            return Math.max(parentProduct.getDownstreamCardFee(), BaseConstant.ZERO_INT);
        }
        return resolveBaseCardFee(product);
    }

    private int determineDownstream(AgentProduct agentProduct, Product product, Integer parentDownstreamCardFee) {
        if (!isCardFeeEnabled(product)) {
            return BaseConstant.ZERO_INT;
        }
        if (agentProduct != null && agentProduct.getDownstreamCardFee() != null) {
            return Math.max(agentProduct.getDownstreamCardFee(), BaseConstant.ZERO_INT);
        }
        if (parentDownstreamCardFee != null) {
            return resolveDefaultDownstream(product, parentDownstreamCardFee);
        }
        return resolveDefaultDownstream(product, resolveBaseCardFee(product));
    }

    private int resolveBaseCardFee(Product product) {
        if (product == null || product.getBaseCardFee() == null) {
            return BaseConstant.ZERO_INT;
        }
        return Math.max(product.getBaseCardFee(), BaseConstant.ZERO_INT);
    }

    private int resolveDefaultDownstream(Product product, int incomingFee) {
        int safeIncoming = Math.max(incomingFee, BaseConstant.ZERO_INT);
        if (!isCardFeeEnabled(product)) {
            return safeIncoming;
        }
        return safeIncoming + CARD_FEE_DEFAULT_MARGIN;
    }

    private boolean isCardFeeEnabled(Product product) {
        return product != null && Objects.equals(product.getProductType(), ProductEnum.PAID_CARD.getStatus());
    }

    private String normalizeAgentCode(String agentCode) {
        return agentCode == null ? "" : agentCode.trim();
    }

    private Map<String, String> buildAgentNameMap(Set<String> agentCodes) {
        if (CollectionUtils.isEmpty(agentCodes)) {
            return Collections.emptyMap();
        }
        List<AgentAccount> accounts = agentAccountService.list(new LambdaQueryWrapper<AgentAccount>()
                .in(AgentAccount::getAgentCode, agentCodes));
        if (CollectionUtils.isEmpty(accounts)) {
            return Collections.emptyMap();
        }
        return accounts.stream()
                .collect(Collectors.toMap(
                        AgentAccount::getAgentCode,
                        account -> {
                            String name = account.getAgentName();
                            return StringUtils.hasLength(name) ? name : account.getAgentCode();
                        },
                        (existing, replacement) -> existing));
    }

    private AgentProductCardFeeOverrideVO buildOverrideVO(AgentProductCardFeeOverride override, Map<String, String> agentNameMap) {
        AgentProductCardFeeOverrideVO vo = new AgentProductCardFeeOverrideVO();
        vo.setOverrideId(override.getOverrideId());
        vo.setProductCode(override.getProductCode());
        vo.setParentAgentCode(override.getParentAgentCode());
        vo.setTargetAgentCode(override.getTargetAgentCode());
        vo.setTargetAgentName(agentNameMap.getOrDefault(override.getTargetAgentCode(), override.getTargetAgentCode()));
        vo.setOverrideFee(safeCardFee(override.getOverrideFee()));
        vo.setIncomingCardFee(safeCardFee(override.getIncomingCardFee()));
        vo.setStatus(override.getStatus());
        vo.setEffectiveTime(override.getEffectiveTime());
        vo.setExpireTime(override.getExpireTime());
        vo.setMemo(override.getMemo());
        vo.setOperator(override.getOperator());
        vo.setCreateTime(override.getCreateTime());
        vo.setUpdateTime(override.getUpdateTime());
        return vo;
    }

    private int safeCardFee(Integer fee) {
        return fee == null ? BaseConstant.ZERO_INT : Math.max(fee, BaseConstant.ZERO_INT);
    }

    private static final class AgentHierarchySnapshot {
        private final Map<String, List<AgentAccount>> childMap = new HashMap<>();
        private final Set<String> allAgentCodes = new HashSet<>();

        Map<String, List<AgentAccount>> getChildMap() {
            return childMap;
        }

        Set<String> getAllAgentCodes() {
            return allAgentCodes;
        }
    }

    private int resolveCommission(Integer commission) {
        if (commission == null) {
            return BaseConstant.ZERO_INT;
        }
        return Math.max(commission, BaseConstant.ZERO_INT);
    }

    private void batchInsertAgentProducts(List<AgentProduct> agentProducts) {
        if (CollectionUtils.isEmpty(agentProducts)) {
            return;
        }
        int fromIndex = 0;
        int size = agentProducts.size();
        while (fromIndex < size) {
            int toIndex = Math.min(fromIndex + BATCH_INSERT_SIZE, size);
            List<AgentProduct> subList = agentProducts.subList(fromIndex, toIndex);
            baseMapper.insertBatch(subList);
            fromIndex = toIndex;
        }
    }

    private String buildAgentProductKey(String agentCode, String parentAgentCode, String productCode) {
        String agent = agentCode == null ? "" : agentCode;
        String parent = parentAgentCode == null ? "" : parentAgentCode;
        String product = productCode == null ? "" : productCode;
        return agent + "#" + parent + "#" + product;
    }

    private void refreshChildCardFee(String productCode, String parentAgentCode, int parentDownstream, long updateTime) {
        if (!StringUtils.hasLength(productCode) || !StringUtils.hasLength(parentAgentCode)) {
            return;
        }
        Product product = productMapper.selectOne(new LambdaQueryWrapper<Product>()
                .eq(Product::getProductCode, productCode));
        if (product == null) {
            return;
        }
        refreshChildCardFee(product, parentAgentCode, parentDownstream, updateTime);
    }

    private void refreshChildCardFee(Product product, String parentAgentCode, int parentDownstream, long updateTime) {
        if (!StringUtils.hasLength(parentAgentCode)) {
            return;
        }
        List<AgentProduct> children = baseMapper.selectList(new LambdaQueryWrapper<AgentProduct>()
                .eq(AgentProduct::getParentProductCode, product.getProductCode())
                .eq(AgentProduct::getParentAgentCode, parentAgentCode));
        if (CollectionUtils.isEmpty(children)) {
            return;
        }
        for (AgentProduct child : children) {
            int normalizedIncoming = parentDownstream;
            int normalizedDownstream = resolveDefaultDownstream(product, normalizedIncoming);
            int profit = normalizedDownstream - normalizedIncoming;
            baseMapper.update(null, new LambdaUpdateWrapper<AgentProduct>()
                    .set(AgentProduct::getIncomingCardFee, normalizedIncoming)
                    .set(AgentProduct::getDownstreamCardFee, normalizedDownstream)
                    .set(AgentProduct::getCardFeeProfit, profit)
                    .set(AgentProduct::getUpdateTime, updateTime)
                    .eq(AgentProduct::getAgentProductId, child.getAgentProductId()));
            refreshChildCardFee(product, child.getAgentCode(), normalizedDownstream, updateTime);
        }
    }

    private void clearCommissionCache(String agentCode, String productCode) {
        // 注释掉缓存删除操作
        // if (configStringRedisTemplate == null || StringUtils.isEmpty(agentCode) || StringUtils.isEmpty(productCode)) {
        //     return;
        // }
        // String cacheKey = CacheUtils.generalKey(CacheKeyConstants.AGENT_PRODUCT_COMMISSION, agentCode, productCode);
        // configStringRedisTemplate.delete(cacheKey);
    }


    /**
     * 此方法会删除代理下所有子代理的产品 包括本身
     * @param product
     */
    @Override
    public void deleteSubAgentProduct(AgentAccount agentAccount, Product product) {
        //创建子代理商产品信息
       List<AgentProduct> agentProductList = baseMapper.selectList(new LambdaQueryWrapper<AgentProduct>().eq(AgentProduct::getAgentCode,agentAccount.getAgentCode()).eq(AgentProduct::getParentProductCode,product.getProductCode()));
       if(!CollectionUtils.isEmpty(agentProductList)){
           for (AgentProduct agentProduct:agentProductList){
               deleteAgentProduct(agentProduct.getAgentProductId());
           }
       }
        // 获取子代理商编码
        String parentAgentCode = agentAccount.getAgentCode();
        if (parentAgentCode != null && !parentAgentCode.isEmpty()) {
            // 根据父代理商编码查询父代理商账号信息
            List<AgentAccount> childAgentAccountList = agentAccountService.list(new LambdaQueryWrapper<AgentAccount>().eq(AgentAccount::getParentAgentCode,parentAgentCode));
            if (!CollectionUtils.isEmpty(childAgentAccountList)) {
                for(AgentAccount childAgentAccount:childAgentAccountList){
                    // 递归调用创建父代理商的产品信息
                    deleteSubAgentProduct(childAgentAccount,product);
                }
            }
        }
    }



    /**
     * 此方法会生成 代理商下所有子代理的产品海报图
     * @param agentAccount
     * @param product
     */
    @Override
    public void addSubAgentProductPoster(AgentAccount agentAccount, Product product) throws BizException {
        if(!StringUtils.hasLength(product.getProductMasterMap())){
            return;
        }
        AgentProduct agentProduct = baseMapper.selectOne(new LambdaQueryWrapper<AgentProduct>().eq(AgentProduct::getParentProductCode,product.getProductCode()).eq(AgentProduct::getAgentCode,agentAccount.getAgentCode()));
        if(agentProduct ==null){
            return;
        }
        //拼接二维码包含的url信息
        StringBuffer stringBuffer = new StringBuffer(h5url);
        stringBuffer.append("?productCode=").append(product.getProductCode()).append("&agentCode=").append(agentAccount.getAgentCode());
        //创建子代理商产品海报图
        createAgentProductPoster(agentProduct,product.getProductMasterMap(),stringBuffer.toString());
        // 获取子代理商编码
        String parentAgentCode = agentAccount.getAgentCode();
        if (parentAgentCode != null && !parentAgentCode.isEmpty()) {
            // 根据父代理商编码查询父代理商账号信息
            List<AgentAccount> childAgentAccountList = agentAccountService.list(new LambdaQueryWrapper<AgentAccount>().eq(AgentAccount::getParentAgentCode,parentAgentCode));
            if (!CollectionUtils.isEmpty(childAgentAccountList)) {
                for(AgentAccount childAgentAccount:childAgentAccountList){
                    // 递归调用创建父代理商的产品信息
                    addSubAgentProductPoster(childAgentAccount,product);
                }
            }
        }
    }

    /**
     * 创建代理商产品海报图并保存
     */
    private void createAgentProductPoster(AgentProduct agentProduct,String productPictureUrl,String content) {
        try {
            String url = qrCodeService.getProductPoster(productPictureUrl,BaseConstant.BASE_PICTURE_URL,content);
            //成功生成了海报图 删除原图并保存新图
            if(StringUtils.hasLength(url)){
                String old = agentProduct.getProductQrcodeMap();
                AgentProduct updateEntity = new AgentProduct();
                updateEntity.setProductQrcodeMap(url);
                if (agentProduct.getAgentProductId() != null) {
                    updateEntity.setAgentProductId(agentProduct.getAgentProductId());
                    baseMapper.updateById(updateEntity);
                } else {
                    baseMapper.update(updateEntity,
                            new LambdaUpdateWrapper<AgentProduct>()
                                    .eq(AgentProduct::getParentProductCode, agentProduct.getParentProductCode())
                                    .eq(AgentProduct::getParentAgentCode, agentProduct.getParentAgentCode())
                                    .eq(AgentProduct::getAgentCode, agentProduct.getAgentCode()));
                }
                agentProduct.setProductQrcodeMap(url);
                pictureService.deletePicture(old);
            }
        }catch (Exception e){
            log.info("生成产品海报异常:{},{}",content,e.getMessage());
        }
    }

    /**
     * 生成产品海报图
     * @return
     */
    public void addProductPoster(AgentAccount agentAccount, Product product){
        if(!StringUtils.hasLength(product.getProductMasterMap())){
            return;
        }
        //拼接二维码包含的url信息
        String url = qrCodeService.getProductPoster(product.getProductMasterMap(),BaseConstant.BASE_PICTURE_URL, h5url + "?productCode=" + product.getProductCode() + "&agentCode=" + agentAccount.getAgentCode());
        product.setProductQrcodeMap(url);
        productMapper.updateById(product);
    }



    /**
     * 生成推广海报图
     * @return
     */
    public void addRegisterQrcodeMap(AgentAccount agentAccount,String url, Integer number) throws BizException {
        if(agentAccount == null){
            return;
        }
        //拼接二维码包含的url信息
        String code = "?agentCode="+agentAccount.getAgentCode();
        ToolConfig toolConfig = toolConfigService.getToolConfig(BaseConstant.THREE_INT);
        String extendUrl = toolConfig.getDomainNameUrl()+code;
        if(number == BaseConstant.ONE_INT){
            String registerQrcodeMapUrl = qrCodeService.getRegisterQrcodeMap1Url(url,extendUrl);
            agentAccount.setRegisterQrcodeMap1(registerQrcodeMapUrl);
        }
        if(number == BaseConstant.TWO_INT){
            String registerQrcodeMapUrl = qrCodeService.getRegisterQrcodeMap1Url(url,extendUrl);
            agentAccount.setRegisterQrcodeMap2(registerQrcodeMapUrl);
        }
        if(number == BaseConstant.THREE_INT){
            String registerQrcodeMapUrl = qrCodeService.getRegisterQrcodeMap3Url(url,extendUrl);
            agentAccount.setRegisterQrcodeMap3(registerQrcodeMapUrl);
        }
        agentAccountService.updateById(agentAccount);
    }

}
