package com.ruoyi.console.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.AgentAccountAddBO;
import com.ruoyi.common.order.bo.AgentOrderSelectBO;
import com.ruoyi.common.order.bo.AgentProductSelectBO;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.order.vo.AgentOrderSelectVO;
import com.ruoyi.common.order.vo.AgentProductSelectVO;
import com.ruoyi.common.order.vo.AgentProductVO;
import com.ruoyi.common.order.vo.AgentStatisticsVO;

import java.util.List;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/22 17:11
 */
public interface AgentAccountService extends IService<AgentAccount> {


    /**
     * 代理商注册
     *
     * @return
     * @throws BizException
     */
    void addAgentAccount(AgentAccountAddBO agentAccountAddBO) throws BizException;



    /**
     * 查询登录账号子代理列表
     *isEnabled 0 启用 1禁用 null都查询
     * @return
     */
    List<AgentAccount> selectChildAgentList(LoginUser loginUser,Integer isEnabled) throws BizException;


    /**
     * 根据登录账户ID获取代理商信息
     *
     * @param userId boolean
     * @return
     */
    AgentAccount getAgentAccountByUserId(Long userId, boolean b) throws BizException;


    /**
     * 根据代理商code获取代理商信息
     *
     * @return
     */
     AgentAccount getAgentAccountByCode(String agentCode, boolean b) throws BizException;


    /**
     * 更新代理商实名状态
     *
     * @return
     */
    void updateAgentRealNameStatus(String agentCode, Integer isRealName,String cardIdUrlFront,String cardIdUrlBack) throws BizException;


    /**
     * 更新代理商实名手机号
     *
     * @return
     */
    void updateAgentPhone(String agentCode,String phone) throws BizException;


    /**
     * 登录账号子代理统计
     *
     * @return
     */
    AgentStatisticsVO selectChildAgentStatistics(LoginUser loginUser) throws BizException;

}
