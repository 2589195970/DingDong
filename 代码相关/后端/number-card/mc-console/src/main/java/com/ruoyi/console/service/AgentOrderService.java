package com.ruoyi.console.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.AgentAccountAddBO;
import com.ruoyi.common.order.bo.AgentOrderSelectBO;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.order.vo.AgentOrderSelectVO;

import java.util.List;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/22 17:11
 */
public interface AgentOrderService extends IService<AgentAccount> {


    /**
     * 代理商订单查询
     *
     * @return
     * @throws BizException
     */
    PageResult<AgentOrderSelectVO> agentSelectOrderListPage(AgentOrderSelectBO agentOrderSelectBO, LoginUser loginUser) throws BizException;




}
