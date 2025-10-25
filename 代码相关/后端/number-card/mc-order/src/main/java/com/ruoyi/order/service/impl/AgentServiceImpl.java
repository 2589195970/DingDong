package com.ruoyi.order.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.order.entity.*;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.vip.entity.VipConfig;
import com.ruoyi.order.mapper.AgentAccountMapper;
import com.ruoyi.order.mapper.AgentProductMapper;
import com.ruoyi.order.mapper.VipConfigMapper;
import com.ruoyi.order.service.AgentService;
import com.ruoyi.order.service.ProductService;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.constant.CacheKeyConstants;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.AgentProductBO;
import com.ruoyi.common.utils.CacheUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * 代理商注册相关接口
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/19 17:13
 */
@Service
@Slf4j
public class AgentServiceImpl extends ServiceImpl<AgentAccountMapper, AgentAccount> implements AgentService {

    @Resource
    ProductService productService;

    @Resource
    AgentProductMapper agentProductMapper;

    @Resource
    VipConfigMapper vipConfigMapper;


    @Resource(name = "configStringRedisTemplate")
    StringRedisTemplate configStringRedisTemplate;


    /**
     * 代理商佣金计算
     * @return
     */
    public List<AgentCommissionJson> getAgentFatherList(String agentCode,String productCode) throws BizException {
        if (StrUtil.isBlankIfStr(agentCode)||StrUtil.isBlankIfStr(productCode)) {
            throw new BizException("agentCode或productCode不存在:{},{}", agentCode,productCode);
        }
        List<AgentCommissionJson> agentCommissionJsonList;
        // 注释掉缓存查询，直接从数据库查询
        // String cacheKey = CacheUtils.generalKey(CacheKeyConstants.AGENT_PRODUCT_COMMISSION, agentCode,productCode);
        // String json = configStringRedisTemplate.opsForValue().get(cacheKey);
        // if (StrUtil.isBlankIfStr(json)) {
            //获取代理商信息
            AgentProductBO agentProductBO = productService.getAgentProductBO(productCode,agentCode);
            if (agentProductBO == null) {
                throw new BizException("代理商不存在:{}", agentCode);
            }
            List<ParentAgentJson> parentAgentJsons = JSONObject.parseArray(agentProductBO.getParentAgentList(),ParentAgentJson.class);
            agentCommissionJsonList = new ArrayList<>();
            //查询各级佣金
            for (ParentAgentJson parentAgentJson : parentAgentJsons) {
                AgentCommissionJson agentCommissionJson = new AgentCommissionJson();
                BeanUtil.copyProperties(parentAgentJson, agentCommissionJson);

                AgentAccount parentAccount = getAgentAccountByCode(parentAgentJson.getAgentCode());
                VipConfig vipConfig = requireVipConfig(parentAccount.getLevel());
                AgentProductBO agentProduct = productService.getAgentProductBO(productCode, parentAgentJson.getAgentCode());
                if (agentProduct == null) {
                    throw new BizException("代理商产品不存在:{}", productCode);
                }
                if (agentProduct.getProductStatus() == BaseConstant.ZERO_INT) {
                    throw new BizException("产品已下架:{}", productCode);
                }

                int productCommissionAmount = agentProduct.getProductCommission() == null ? BaseConstant.ZERO_INT : agentProduct.getProductCommission();
                int revenueAmount = agentProduct.getRevenueProductCommission() == null ? BaseConstant.ZERO_INT : agentProduct.getRevenueProductCommission();
                int distributionAmount = agentProduct.getDistributionProductCommission() == null ? BaseConstant.ZERO_INT : agentProduct.getDistributionProductCommission();
                distributionAmount = Math.max(distributionAmount, BaseConstant.ZERO_INT);

                agentCommissionJson.setProductCommission(productCommissionAmount);
                agentCommissionJson.setRevenueProductCommission(revenueAmount);
                agentCommissionJson.setDistributionProductCommission(distributionAmount);
                agentCommissionJson.setVipLevel(parentAccount.getLevel());
                int vipFixed = vipConfig.getFixedCommission() == null ? BaseConstant.ZERO_INT : Math.max(vipConfig.getFixedCommission(), BaseConstant.ZERO_INT);
                agentCommissionJson.setVipFixedCommission(vipFixed);
                agentCommissionJson.setVipBonusCommission(BaseConstant.ZERO_INT);
                agentCommissionJsonList.add(agentCommissionJson);
            }
            int downstreamAllocated = BaseConstant.ZERO_INT;
            for (int i = agentCommissionJsonList.size() - 1; i >= 0; i--) {
                AgentCommissionJson agentCommissionJson = agentCommissionJsonList.get(i);
                int parentVipFixed = agentCommissionJson.getVipFixedCommission() == null ? BaseConstant.ZERO_INT : agentCommissionJson.getVipFixedCommission();
                int vipBonus = Math.max(parentVipFixed - downstreamAllocated, BaseConstant.ZERO_INT);
                agentCommissionJson.setVipBonusCommission(vipBonus);
                downstreamAllocated += vipBonus;
            }
            // 注释掉缓存写入
            // configStringRedisTemplate.opsForValue().set(cacheKey, JSONObject.toJSONString(agentCommissionJsonList), 10, TimeUnit.MINUTES);
        // } else {
        //     agentCommissionJsonList = JSONObject.parseArray(json, AgentCommissionJson.class);
        // }
        return agentCommissionJsonList;
    }


    /**
     * 根据代理商code获取代理商信息
     *
     * @return
     */
    @Override
    public AgentAccount getAgentAccountByCode(String agentCode) throws BizException {
        // 注释掉缓存查询，直接从数据库查询
        // String cacheKey = CacheUtils.generalKey(CacheKeyConstants.AGENT_ACCOUNT_CODE, agentCode);
        // String agentAccountJson = configStringRedisTemplate.opsForValue().get(cacheKey);
        // if (StringUtils.isNotEmpty(agentAccountJson)) {
        //     return JSONObject.parseObject(agentAccountJson, AgentAccount.class);
        // }
        AgentAccount agentAccount = baseMapper.selectOne(new LambdaQueryWrapper<AgentAccount>().eq(AgentAccount::getAgentCode, agentCode));
        if (agentAccount == null) {
            throw new BizException("代理商编码{}不存在",agentCode);
        }
        if(agentAccount.getIsEnabled()!=null&&agentAccount.getIsEnabled()==BaseConstant.ONE_INT){
            throw new BizException("代理商账号禁用");
        }
        // 注释掉缓存写入
        // configStringRedisTemplate.opsForValue().set(cacheKey, JSONObject.toJSONString(agentAccount), CacheKeyConstants.CATCH_TIME, TimeUnit.MINUTES);
        return agentAccount;
    }

    /**
     * 是否是admin代理商
     * @param agentCode
     * @return
     * @throws BizException
     */
    @Override
    public Boolean isAdminAgent(String agentCode) throws BizException {
        AgentAccount agentAccount = getAgentAccountByCode(agentCode);
        if(agentAccount!=null&&agentAccount.getSysUserId() == 1L){
            return true;
        }
        return false;
    }

    private VipConfig requireVipConfig(Integer vipLevel) throws BizException {
        if (vipLevel == null) {
            throw new BizException("代理商VIP等级缺失");
        }
        VipConfig vipConfig = vipConfigMapper.selectOne(new LambdaQueryWrapper<VipConfig>().eq(VipConfig::getVipLevel, vipLevel).last("limit 1"));
        if (vipConfig == null) {
            throw new BizException("VIP等级{}未配置固定加成", vipLevel);
        }
        return vipConfig;
    }

}
