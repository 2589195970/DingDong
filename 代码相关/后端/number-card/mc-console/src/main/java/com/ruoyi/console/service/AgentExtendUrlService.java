package com.ruoyi.console.service;


import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.vo.AgentApiVO;
import com.ruoyi.common.order.vo.AgentExtendUrlVO;
import com.ruoyi.common.order.vo.AgentInfoVO;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/22 17:11
 */
public interface AgentExtendUrlService  {


    /**
     *
     * 获取推广链接
     * @return
     * @throws BizException
     */
     AgentExtendUrlVO getAgentExtendUrlVO(LoginUser loginUser) throws BizException;


    /**
     *
     * 获取API 信息
     * @return
     * @throws BizException
     */
     AgentApiVO getAgentApiVO(LoginUser loginUser) throws BizException;

    /**
     *
     * 更新url信息
     * @return
     * @throws BizException
     */
     void updateCallbackUrl(String agentCode,String callbackUrl) throws BizException;


    /**
     *
     * 获取账号信息
     * @return
     * @throws BizException
     */
    AgentInfoVO getAgentInfoVO(LoginUser loginUser) throws BizException;


    /**
     *
     * 修改账号手机号
     * @return
     * @throws BizException
     */
    void updateAgentPhone(LoginUser loginUser,String phone,String smsCode) throws BizException;
}
