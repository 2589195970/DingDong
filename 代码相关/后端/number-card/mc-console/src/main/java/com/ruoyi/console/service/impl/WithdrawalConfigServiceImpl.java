package com.ruoyi.console.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.WithdrawalConfigUpdateBO;
import com.ruoyi.common.order.entity.WithdrawalConfig;
import com.ruoyi.console.mapper.WithdrawalConfigMapper;
import com.ruoyi.console.service.WithdrawalConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;



/**
 * 提现配置
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/19 17:13
 */
@Service
@Slf4j
public class WithdrawalConfigServiceImpl extends ServiceImpl<WithdrawalConfigMapper, WithdrawalConfig> implements WithdrawalConfigService {


    /**
     * 查询提现配置
     * 默认查询一条即可
     * @return
     */
    public WithdrawalConfig selectWithdrawalConfig(){
        return baseMapper.selectOne(new LambdaQueryWrapper<WithdrawalConfig>().eq(WithdrawalConfig::getSysUserId,1L));
    }



    /**
     * 更新提现配置
     * 仅admin有权限更改
     * @return
     */
    public void updateWithdrawalConfig(WithdrawalConfigUpdateBO withdrawalConfigUpdateBO, Long userId) throws BizException {
        if(userId!=1L){
            throw new BizException("权限不足");
        }
        WithdrawalConfig withdrawalConfig = selectWithdrawalConfig();
        if(withdrawalConfigUpdateBO.getWithdrawRate()!=null){
            if(withdrawalConfigUpdateBO.getWithdrawRate()>=100){
                throw new BizException("提现费率不能超过100");
            }
            withdrawalConfig.setWithdrawRate(withdrawalConfigUpdateBO.getWithdrawRate());
        }
        if(withdrawalConfigUpdateBO.getWithdrawMinAmount()!=null){
            withdrawalConfig.setWithdrawMinAmount(withdrawalConfigUpdateBO.getWithdrawMinAmount());
        }
        withdrawalConfig.setUpdateTime(System.currentTimeMillis());
        baseMapper.updateById(withdrawalConfig);
    }

}
