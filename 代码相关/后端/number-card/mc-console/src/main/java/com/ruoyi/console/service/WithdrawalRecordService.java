package com.ruoyi.console.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.order.entity.OrderCommissionDetails;
import com.ruoyi.common.order.entity.WithdrawalApplication;
import com.ruoyi.common.order.entity.WithdrawalRecord;
import org.springframework.transaction.annotation.Transactional;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/12 17:11
 */
public interface WithdrawalRecordService extends IService<WithdrawalRecord> {

    /**
     * 查询提现记录
     * 不传参数默认返回 当前登录账号提现记录
     * @param agentCode
     * @param loginUser
     * @return
     */
     WithdrawalRecord selectWithdrawalRecord(String agentCode, LoginUser loginUser);

    /**
     * 新增佣金提现记录
     * 用户注册账号默认生成一条提现记录
     */
     void addWithdrawalRecord(Long sysUserId, String agentCode) throws BizException;


    /**
     * 新增佣金提现详情记录
     * 订单佣金佣金结算使用
     * @param orderCommissionDetails
     */
     void insertOrderCommissionDeposit(OrderCommissionDetails orderCommissionDetails) throws BizException;


    /**
     * 新增佣金余额 提现记录
     *
     */
     void insertBalanceCommissionWithdrawal(WithdrawalApplication withdrawalApplication) throws BizException;


    /**
     * 管理员 代理商余额调整
     */
    void insertAdminBalanceWithdrawal(String agentCode, Integer balanceYun, Integer amountType, AgentAccount agentAccount) throws BizException;


}
