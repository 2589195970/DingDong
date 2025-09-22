package com.ruoyi.console.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.WithdrawalConfigUpdateBO;
import com.ruoyi.common.order.entity.WithdrawalConfig;
import com.ruoyi.common.order.entity.WithdrawalRecord;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2025/1/22 17:11
 */
public interface WithdrawalConfigService extends IService<WithdrawalConfig> {

    /**
     * 查询提现配置
     * 默认查询一条即可
     * @return
     */
     WithdrawalConfig selectWithdrawalConfig();


    /**
     * 更新提现配置
     * 仅admin有权限更改
     * @return
     */
     void updateWithdrawalConfig(WithdrawalConfigUpdateBO withdrawalConfigUpdateBO, Long userId) throws BizException;

}
