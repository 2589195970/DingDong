package com.ruoyi.console.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.AgentOrderSelectBO;
import com.ruoyi.common.order.bo.WithdrawalApplicationAddBO;
import com.ruoyi.common.order.bo.WithdrawalApplicationSelectBO;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.order.entity.WithdrawalApplication;
import com.ruoyi.common.order.vo.AgentOrderSelectVO;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/22 17:11
 */
public interface AgentWithdrawalApplicationService extends IService<WithdrawalApplication> {


    /**
     * 申请提现记录查询
     *
     * @return
     * @throws BizException
     */
    PageResult<WithdrawalApplication> selectAgentWithdrawalApplicationListPage(WithdrawalApplicationSelectBO withdrawalApplicationSelectBO, LoginUser loginUser) throws BizException;


    /**
     * 新增申请提现记录
     */
    void addAgentWithdrawalApplication(WithdrawalApplicationAddBO withdrawalApplicationAddBO, LoginUser loginUser) throws BizException;

}
