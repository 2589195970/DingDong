package com.ruoyi.console.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.entity.*;

import com.ruoyi.common.order.vo.RevenueVO;
import com.ruoyi.console.mapper.WithdrawalRecordMapper;
import com.ruoyi.console.service.WithdrawalRecordDetailsService;
import com.ruoyi.console.service.WithdrawalRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;


/**
 * 提现记录相关API
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/19 17:13
 */
@Service
@Slf4j
public class WithdrawalRecordServiceImpl extends ServiceImpl<WithdrawalRecordMapper, WithdrawalRecord> implements WithdrawalRecordService {

    @Resource
    WithdrawalRecordDetailsService withdrawalRecordDetailsService;


    /**
     * 查询提现记录
     * 不传参数默认返回 当前登录账号提现记录
     * @param agentCode
     * @param loginUser
     * @return
     */
    public WithdrawalRecord selectWithdrawalRecord(String agentCode, LoginUser loginUser){
        if(StringUtils.hasLength(agentCode)){
            return baseMapper.selectOne(new LambdaQueryWrapper<WithdrawalRecord>().eq(WithdrawalRecord::getAgentCode,agentCode));
        }
        return baseMapper.selectOne(new LambdaQueryWrapper<WithdrawalRecord>().eq(WithdrawalRecord::getSysUserId,loginUser.getUserId()));
    }




    /**
     * 新增提现记录
     * 用户注册账号默认生成一条提现记录
     */
    public void addWithdrawalRecord(Long sysUserId, String agentCode) throws BizException {
        if (sysUserId == null || agentCode == null) {
            throw new BizException("参数不能为空");
        }
        List<WithdrawalRecord> withdrawalRecords = baseMapper.selectList(new LambdaQueryWrapper<WithdrawalRecord>().eq(WithdrawalRecord::getSysUserId, sysUserId).eq(WithdrawalRecord::getAgentCode, agentCode));
        if (!CollectionUtils.isEmpty(withdrawalRecords)) {
            return;
        }
        WithdrawalRecord withdrawalRecord = new WithdrawalRecord();
        withdrawalRecord.setBalance(BaseConstant.ZERO_INT);
        withdrawalRecord.setSysUserId(sysUserId);
        withdrawalRecord.setAgentCode(agentCode);
        withdrawalRecord.setCreateTime(System.currentTimeMillis());
        baseMapper.insert(withdrawalRecord);
    }


    /**
     * 新增订单佣金 存入记录
     *
     * @param orderCommissionDetails
     */
    public void insertOrderCommissionDeposit(OrderCommissionDetails orderCommissionDetails) throws BizException {
        WithdrawalRecord withdrawalRecord = baseMapper.selectOne(new LambdaQueryWrapper<WithdrawalRecord>().eq(WithdrawalRecord::getAgentCode,orderCommissionDetails.getAgentCode()));
        if(withdrawalRecord == null){
            throw new BizException("提现记录编码{}不存在",orderCommissionDetails.getAgentCode());
        }
        //记录提现详情
        WithdrawalRecordDetails withdrawalRecordDetails = new WithdrawalRecordDetails();
        withdrawalRecordDetails.setWithdrawalRecordId(withdrawalRecord.getWithdrawalRecordId());
        withdrawalRecordDetails.setSysUserId(withdrawalRecord.getSysUserId());
        withdrawalRecordDetails.setAgentCode(withdrawalRecord.getAgentCode());
        withdrawalRecordDetails.setAgentName(orderCommissionDetails.getAgentName());
        withdrawalRecordDetails.setSourceNumber(orderCommissionDetails.getOrderId()+"");
        //类型存入
        withdrawalRecordDetails.setAmountType(BaseConstant.ZERO_INT);
        //金额
        withdrawalRecordDetails.setWithdrawalAmount(orderCommissionDetails.getRevenueProductCommission()*100);
        withdrawalRecordDetails.setReceivedAmount(orderCommissionDetails.getRevenueProductCommission()*100);
        withdrawalRecordDetails.setWithdrawalRate(BaseConstant.ZERO_INT);
        withdrawalRecordDetails.setWithdrawalRateAmount(BaseConstant.ZERO_INT);
        withdrawalRecordDetails.setWithdrawalType(BaseConstant.ZERO_INT);
        withdrawalRecordDetails.setCreateTime(System.currentTimeMillis());
        withdrawalRecordDetailsService.save(withdrawalRecordDetails);
        //重新计算提现余额
        withdrawalRecord.setBalance(withdrawalRecordDetailsService.computeWithdrawalRecordBalance(withdrawalRecord.getWithdrawalRecordId()));
        withdrawalRecord.setUpdateTime(System.currentTimeMillis());
        baseMapper.updateById(withdrawalRecord);
        log.info("佣金存入记录余额计算:OrderCommissionDetails:{},WithdrawalRecordDetails:{},WithdrawalRecord:{}", JSONObject.toJSONString(orderCommissionDetails),
                JSONObject.toJSONString(withdrawalRecordDetails), JSONObject.toJSONString(withdrawalRecordDetails));
    }



    /**
     * 佣金余额 提现记录
     *
     */
    public void insertBalanceCommissionWithdrawal(WithdrawalApplication withdrawalApplication) throws BizException {
        WithdrawalRecord withdrawalRecord = baseMapper.selectOne(new LambdaQueryWrapper<WithdrawalRecord>().eq(WithdrawalRecord::getAgentCode,withdrawalApplication.getApplyAgentCode()));
        if(withdrawalRecord == null){
            throw new BizException("提现记录编码{}不存在",withdrawalApplication.getApplyAgentCode());
        }
        //记录提现详情
        WithdrawalRecordDetails withdrawalRecordDetails = new WithdrawalRecordDetails();
        withdrawalRecordDetails.setWithdrawalRecordId(withdrawalRecord.getWithdrawalRecordId());
        withdrawalRecordDetails.setSysUserId(withdrawalRecord.getSysUserId());
        withdrawalRecordDetails.setAgentCode(withdrawalRecord.getAgentCode());
        withdrawalRecordDetails.setAgentName(withdrawalApplication.getApplyAgentName());
        withdrawalRecordDetails.setSourceNumber(withdrawalApplication.getApplicationNumber());
        //类型提取
        withdrawalRecordDetails.setAmountType(BaseConstant.ONE_INT);
        //金额
        withdrawalRecordDetails.setWithdrawalAmount(-withdrawalApplication.getWithdrawalAmount());
        withdrawalRecordDetails.setReceivedAmount(-withdrawalApplication.getReceivedAmount());
        withdrawalRecordDetails.setWithdrawalRate(withdrawalApplication.getWithdrawalRate());
        withdrawalRecordDetails.setWithdrawalRateAmount(-withdrawalApplication.getWithdrawalRateAmount());
        withdrawalRecordDetails.setWithdrawalType(BaseConstant.ONE_INT);
        withdrawalRecordDetails.setCreateTime(System.currentTimeMillis());

        withdrawalRecordDetails.setApplyWithdrawalType(withdrawalApplication.getWithdrawalType());
        withdrawalRecordDetails.setWxUrl(withdrawalApplication.getWxUrl());
        withdrawalRecordDetails.setZfbAccount(withdrawalApplication.getZfbAccount());
        withdrawalRecordDetails.setZfbAccountName(withdrawalApplication.getZfbAccountName());
        withdrawalRecordDetails.setBankName(withdrawalApplication.getBankName());
        withdrawalRecordDetails.setBankNumber(withdrawalApplication.getBankNumber());
        withdrawalRecordDetails.setBankNumberName(withdrawalApplication.getBankNumberName());
        withdrawalRecordDetails.setBankNumberPhone(withdrawalApplication.getBankNumberPhone());
        withdrawalRecordDetails.setSerialNumber(withdrawalApplication.getSerialNumber());
        withdrawalRecordDetails.setReceivedAmount(withdrawalApplication.getReceivedAmount());

        withdrawalRecordDetailsService.save(withdrawalRecordDetails);
        //重新计算提现余额
        withdrawalRecord.setBalance(withdrawalRecordDetailsService.computeWithdrawalRecordBalance(withdrawalRecord.getWithdrawalRecordId()));
        withdrawalRecord.setUpdateTime(System.currentTimeMillis());
        baseMapper.updateById(withdrawalRecord);
        log.info("佣金提现余额计算:WithdrawalApplication:{},WithdrawalRecordDetails:{},WithdrawalRecord:{}", JSONObject.toJSONString(withdrawalApplication),
                JSONObject.toJSONString(withdrawalRecordDetails), JSONObject.toJSONString(withdrawalRecordDetails));
    }

    /**
     * 管理员 代理商余额调整
     *
     */
    //@Transactional
    public void insertAdminBalanceWithdrawal(String agentCode, Integer balanceYun, Integer amountType, AgentAccount agentAccount) throws BizException {
        WithdrawalRecord withdrawalRecord = baseMapper.selectOne(new LambdaQueryWrapper<WithdrawalRecord>().eq(WithdrawalRecord::getAgentCode,agentCode));
        if(withdrawalRecord == null){
            throw new BizException("提现记录编码{}不存在",agentCode);
        }
        //记录提现详情
        WithdrawalRecordDetails withdrawalRecordDetails = new WithdrawalRecordDetails();
        withdrawalRecordDetails.setWithdrawalRecordId(withdrawalRecord.getWithdrawalRecordId());
        withdrawalRecordDetails.setSysUserId(withdrawalRecord.getSysUserId());
        withdrawalRecordDetails.setAgentCode(withdrawalRecord.getAgentCode());
        withdrawalRecordDetails.setAgentName(agentAccount.getAgentName());
        withdrawalRecordDetails.setSourceNumber("9999");
        //类型提取
        withdrawalRecordDetails.setAmountType(amountType);
        //金额
        Integer a = amountType == BaseConstant.ONE_INT?-100:100;
        withdrawalRecordDetails.setWithdrawalAmount(a*balanceYun);
        withdrawalRecordDetails.setReceivedAmount(a*balanceYun);
        withdrawalRecordDetails.setWithdrawalRate(BaseConstant.ZERO_INT);
        withdrawalRecordDetails.setWithdrawalRateAmount(BaseConstant.ZERO_INT);
        withdrawalRecordDetails.setWithdrawalType(BaseConstant.TWO_INT);
        withdrawalRecordDetails.setCreateTime(System.currentTimeMillis());
        withdrawalRecordDetailsService.save(withdrawalRecordDetails);
        //重新计算提现余额
        withdrawalRecord.setBalance(withdrawalRecordDetailsService.computeWithdrawalRecordBalance(withdrawalRecord.getWithdrawalRecordId()));
        withdrawalRecord.setUpdateTime(System.currentTimeMillis());
        baseMapper.updateById(withdrawalRecord);
        log.info("佣金提现余额计算:agentCode:{},WithdrawalRecordDetails:{},WithdrawalRecord:{}", agentCode,JSONObject.toJSONString(withdrawalRecordDetails), JSONObject.toJSONString(withdrawalRecordDetails));
    }




}
