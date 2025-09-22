package com.ruoyi.console.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.entity.CommissionConfig;



/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/12 17:11
 */
public interface CommissionConfigService extends IService<CommissionConfig> {

    /**
     * 新增佣金配置记录
     * @param commissionConfig
     */
    void  addCommissionConfig(CommissionConfig commissionConfig);


    /**
     * 更新佣金配置
     * @param commissionConfig
     */
     void updateCommissionConfig(CommissionConfig commissionConfig) throws BizException;


    /**
     * 根据代理商编码计算分销佣金
     * @return
     */
    Integer computeCommission(String agentCode,Integer commission) throws BizException;


}
