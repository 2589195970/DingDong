package com.ruoyi.order.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.AgentAccountAddBO;
import com.ruoyi.common.order.bo.AgentOrderSelectBO;
import com.ruoyi.common.order.bo.AgentProductSelectBO;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.order.entity.AgentCommissionJson;
import com.ruoyi.common.order.vo.AgentOrderSelectVO;
import com.ruoyi.common.order.vo.AgentProductVO;

import java.util.List;


/**
 * 代理商相关API
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/22 17:11
 */
public interface AgentService extends IService<AgentAccount> {


    /**
     * 代理商佣金计算
     * @return
     */
     List<AgentCommissionJson> getAgentFatherList(String agentCode, String productCode) throws BizException;

    /**
     * 根据代理商code获取代理商信息
     *
     * @return
     */
     AgentAccount getAgentAccountByCode(String agentCode) throws BizException;

    /**
     * 是否admin代理商
     * @param agentCode
     * @return
     */
     Boolean isAdminAgent(String agentCode) throws BizException;
}
