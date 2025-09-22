package com.ruoyi.console.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.AgentCommissionSelectBO;
import com.ruoyi.common.order.entity.CommissionConfig;
import com.ruoyi.common.order.entity.OrderCommissionDetails;
import com.ruoyi.common.order.vo.AgentCommissionSelectVO;

/**
 * 代理商佣金列表
 *
 * @Description
 * @Author 陈思伟
 * @Date 2025/03/07 17:11
 */
public interface AgentCommissionService extends IService<OrderCommissionDetails> {

    /**
     * 代理商订单佣金列表查询
     *
     * @return
     * @throws BizException
     */
     PageResult<AgentCommissionSelectVO> selectOrderCommissionListPage(AgentCommissionSelectBO agentCommissionSelectBO, LoginUser loginUser) throws BizException;

    /**
     * 代理商佣金配置查询
     * @param loginUser
     */
     CommissionConfig selectAgentCommissionConfig(LoginUser loginUser) throws BizException;


    /**
     * 代理商佣金配置修改
     * @param loginUser
     */
    void agentUpdateCommissionConfig(CommissionConfig commissionConfig,LoginUser loginUser) throws BizException;
}
