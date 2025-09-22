package com.ruoyi.console.service;


import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.AgentAccountSelectBO;
import com.ruoyi.common.order.bo.AgentUpdateBalanceBO;
import com.ruoyi.common.order.bo.AgentUpdateEnabledBO;
import com.ruoyi.common.order.bo.AgentUpdateEncryptBO;
import com.ruoyi.common.order.vo.AgentAccountListVO;

/**
 * @Description
 * @Author 陈思伟
 * @Date 2025/03/07 17:11
 */
public interface AgentManagementService  {

    /**
     * 代理商列表查询
     *
     * @return
     * @throws BizException
     */
     PageResult<AgentAccountListVO> selectAgentAccountListPage(AgentAccountSelectBO agentAccountSelectBO) throws BizException;


    /**
     * 更新代理生效状态
     * @param agentUpdateEnabledBO
     * @throws BizException
     */
     void updateAgentStatus(AgentUpdateEnabledBO agentUpdateEnabledBO) throws BizException;


    /**
     * 更新代理加密状态状态
     * @throws BizException
     */
     void updateAgentEncryptStatus(AgentUpdateEncryptBO agentUpdateEncryptBO) throws BizException;

    /**
     * 调整余额
     * @param agentUpdateEnabledBO
     * @throws BizException
     */
     void updateBalance(AgentUpdateBalanceBO agentUpdateEnabledBO) throws BizException;


    /**
     * 删除代理商
     * @throws BizException
     */
    void deleteAgentAccount(Integer agentAccountId) throws BizException;


}
