package com.ruoyi.order.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.order.entity.*;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.order.mapper.AgentAccountMapper;
import com.ruoyi.order.mapper.AgentProductMapper;
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
        String cacheKey = CacheUtils.generalKey(CacheKeyConstants.AGENT_PRODUCT_COMMISSION, agentCode,productCode);
        String json = configStringRedisTemplate.opsForValue().get(cacheKey);
        if (StrUtil.isBlankIfStr(json)) {
            //获取代理商信息
            AgentProductBO agentProductBO = productService.getAgentProductBO(productCode,agentCode);
            if (agentProductBO == null) {
                throw new BizException("代理商不存在:{}", agentCode);
            }
            List<ParentAgentJson> parentAgentJsons = JSONObject.parseArray(agentProductBO.getParentAgentList(),ParentAgentJson.class);
            agentCommissionJsonList = new ArrayList<>();
            //查询各级佣金
            for(ParentAgentJson parentAgentJson:parentAgentJsons){
                AgentCommissionJson agentCommissionJson = new AgentCommissionJson();
                BeanUtil.copyProperties(parentAgentJson,agentCommissionJson);
                AgentProductBO agentProduct = productService.getAgentProductBO(productCode,parentAgentJson.getAgentCode());
                if(agentProduct==null){
                    throw new BizException("代理商产品不存在:{}", productCode);
                }
                if(agentProduct.getProductStatus() == BaseConstant.ZERO_INT){
                    throw new BizException("产品已下架:{}", productCode);
                }
                if(parentAgentJson.getAgentCode().equals(agentCode)){
                    //如果是此代理商进单 则佣金为本身 不需要分给下游
                    agentCommissionJson.setProductCommission(agentProduct.getProductCommission());
                    agentCommissionJson.setRevenueProductCommission(agentProduct.getProductCommission());
                    agentCommissionJson.setDistributionProductCommission(agentCommissionJson.getProductCommission()-agentCommissionJson.getRevenueProductCommission());
                }else {
                    agentCommissionJson.setProductCommission(agentProduct.getProductCommission());
                    agentCommissionJson.setRevenueProductCommission(agentProduct.getRevenueProductCommission());
                    agentCommissionJson.setDistributionProductCommission(agentProduct.getDistributionProductCommission());
                }

                agentCommissionJsonList.add(agentCommissionJson);
            }
            configStringRedisTemplate.opsForValue().set(cacheKey, JSONObject.toJSONString(agentCommissionJsonList), 10, TimeUnit.MINUTES);
        } else {
            agentCommissionJsonList = JSONObject.parseArray(json, AgentCommissionJson.class);
        }
        return agentCommissionJsonList;
    }


    /**
     * 根据代理商code获取代理商信息
     *
     * @return
     */
    @Override
    public AgentAccount getAgentAccountByCode(String agentCode) throws BizException {
        String cacheKey = CacheUtils.generalKey(CacheKeyConstants.AGENT_ACCOUNT_CODE, agentCode);
        String agentAccountJson = configStringRedisTemplate.opsForValue().get(cacheKey);
        if (StringUtils.isNotEmpty(agentAccountJson)) {
            return JSONObject.parseObject(agentAccountJson, AgentAccount.class);
        }
        AgentAccount agentAccount = baseMapper.selectOne(new LambdaQueryWrapper<AgentAccount>().eq(AgentAccount::getAgentCode, agentCode));
        if (agentAccount == null) {
            throw new BizException("代理商编码{}不存在",agentCode);
        }
        if(agentAccount.getIsEnabled()!=null&&agentAccount.getIsEnabled()==BaseConstant.ONE_INT){
            throw new BizException("代理商账号禁用");
        }
        configStringRedisTemplate.opsForValue().set(cacheKey, JSONObject.toJSONString(agentAccount), CacheKeyConstants.CATCH_TIME, TimeUnit.MINUTES);
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

}
