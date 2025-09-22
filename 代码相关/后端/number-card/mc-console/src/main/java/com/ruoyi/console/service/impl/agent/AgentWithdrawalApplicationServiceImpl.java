package com.ruoyi.console.service.impl.agent;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.WithdrawalApplicationAddBO;
import com.ruoyi.common.order.bo.WithdrawalApplicationSelectBO;
import com.ruoyi.common.order.entity.WithdrawalApplication;
import com.ruoyi.console.mapper.WithdrawalApplicationMapper;
import com.ruoyi.console.service.AgentWithdrawalApplicationService;
import com.ruoyi.console.service.WithdrawalApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;



/**
 * 代理商提现申请相关接口
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/19 17:13
 */
@Service
@Slf4j
public class AgentWithdrawalApplicationServiceImpl extends ServiceImpl<WithdrawalApplicationMapper, WithdrawalApplication> implements AgentWithdrawalApplicationService {

    @Resource
    WithdrawalApplicationService withdrawalApplicationService;


    /**
     * 申请提现记录查询
     *
     * @return
     * @throws BizException
     */
    public PageResult<WithdrawalApplication> selectAgentWithdrawalApplicationListPage(WithdrawalApplicationSelectBO withdrawalApplicationSelectBO, LoginUser loginUser) throws BizException {
        if(withdrawalApplicationSelectBO.getApplyUserId()==null){
            withdrawalApplicationSelectBO.setApplyUserId(loginUser.getUserId());
        }
        return withdrawalApplicationService.selectWithdrawalApplicationListPage(withdrawalApplicationSelectBO);
    }


    /**
     * 新增申请提现记录
     */
    public void addAgentWithdrawalApplication(WithdrawalApplicationAddBO withdrawalApplicationAddBO, LoginUser loginUser) throws BizException {
        if(withdrawalApplicationAddBO.getApplyUserId()==null){
            withdrawalApplicationAddBO.setApplyUserId(loginUser.getUserId());
        }
        withdrawalApplicationService.addWithdrawalApplication(withdrawalApplicationAddBO);
    }

}
