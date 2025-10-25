package com.ruoyi.console.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.AgentCommissionSelectBO;
import com.ruoyi.common.order.entity.OrderCommissionDetails;
import com.ruoyi.common.order.vo.AgentCommissionSelectVO;
import com.ruoyi.common.vip.vo.VipConfigVO;

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
     * 查询当前可用的 VIP 佣金卡片配置。
     *
     * @return VIP 配置列表
     * @throws BizException 异常信息
     */
    java.util.List<VipConfigVO> selectVipCommissionCards(LoginUser loginUser) throws BizException;

    /**
     * 代理商订单佣金列表导出
     * @param response HTTP响应
     * @param agentCommissionSelectBO 查询条件
     * @param loginUser 登录用户
     * @throws BizException
     */
    void exportOrderCommissionList(javax.servlet.http.HttpServletResponse response, AgentCommissionSelectBO agentCommissionSelectBO, LoginUser loginUser) throws BizException;
}
