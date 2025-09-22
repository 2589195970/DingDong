package com.ruoyi.console.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.PageResultFactory;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.OrderSelectBO;
import com.ruoyi.common.order.bo.WithdrawalRecordDetailsSelectBO;
import com.ruoyi.common.order.entity.Order;
import com.ruoyi.common.order.entity.OrderCommissionDetails;
import com.ruoyi.common.order.entity.WithdrawalRecord;
import com.ruoyi.common.order.entity.WithdrawalRecordDetails;
import com.ruoyi.common.order.vo.OrderSelectVO;
import com.ruoyi.common.order.vo.RevenueVO;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.console.mapper.WithdrawalRecordDetailsMapper;
import com.ruoyi.console.mapper.WithdrawalRecordMapper;
import com.ruoyi.console.service.WithdrawalRecordDetailsService;
import com.ruoyi.console.service.WithdrawalRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * 提现记录详情相关API
 *
 * @Description
 * @Author 陈思伟
 * @Date 2025/1/26 17:13
 */
@Service
@Slf4j
public class WithdrawalRecordDetailsServiceImpl extends ServiceImpl<WithdrawalRecordDetailsMapper, WithdrawalRecordDetails> implements WithdrawalRecordDetailsService {


    /**
     * 提现记录详情列表分页查询
     * @return
     * @throws BizException
     */
    public PageResult<WithdrawalRecordDetails> selectWithdrawalRecordDetailsListPage(WithdrawalRecordDetailsSelectBO withdrawalRecordDetailsSelectBO) throws BizException {
        //读取分页
        Page page = new Page(withdrawalRecordDetailsSelectBO.getPageNo(),withdrawalRecordDetailsSelectBO.getPageSize());
        Page<WithdrawalRecordDetails> withdrawalRecordDetailsPage  = baseMapper.selectPage(page,new LambdaQueryWrapper<WithdrawalRecordDetails>()
                .eq(withdrawalRecordDetailsSelectBO.getWithdrawalRecordId()!=null,WithdrawalRecordDetails::getWithdrawalRecordId,withdrawalRecordDetailsSelectBO.getWithdrawalRecordId())
                .eq(withdrawalRecordDetailsSelectBO.getSysUserId()!=null,WithdrawalRecordDetails::getSysUserId,withdrawalRecordDetailsSelectBO.getSysUserId())
                .eq(StringUtils.isNotEmpty(withdrawalRecordDetailsSelectBO.getAgentCode()),WithdrawalRecordDetails::getAgentCode,withdrawalRecordDetailsSelectBO.getAgentCode())
                .eq(withdrawalRecordDetailsSelectBO.getAmountType()!=null,WithdrawalRecordDetails::getAmountType,withdrawalRecordDetailsSelectBO.getAmountType())
                .eq(withdrawalRecordDetailsSelectBO.getWithdrawalType()!=null,WithdrawalRecordDetails::getWithdrawalType,withdrawalRecordDetailsSelectBO.getWithdrawalType())
                .like(StringUtils.isNotEmpty(withdrawalRecordDetailsSelectBO.getSourceNumber()),WithdrawalRecordDetails::getSourceNumber,withdrawalRecordDetailsSelectBO.getSourceNumber())
        );
        return PageResultFactory.createPageResult(withdrawalRecordDetailsPage);
    }


    /**
     * 计算提现记录总余额
     * @param withdrawalRecordId
     * @return
     */
    public Integer computeWithdrawalRecordBalance(Integer withdrawalRecordId){
        return baseMapper.selectWithdrawalAmountCount(withdrawalRecordId);
    }

    /**
     * 删除提现记录
     */
    public void deleteWithdrawalRecordDetails(Integer withdrawalRecordDetailsId){
        baseMapper.deleteById(withdrawalRecordDetailsId);
    }


    /**
     * 查询登陆用户收益记录
     * @param loginUser
     * @return
     */
    public RevenueVO selectRevenue(LoginUser loginUser){
        return baseMapper.selectRevenue(loginUser.getUserId());
    }

}
