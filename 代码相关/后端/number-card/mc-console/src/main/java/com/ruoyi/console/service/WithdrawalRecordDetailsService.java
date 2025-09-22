package com.ruoyi.console.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.WithdrawalRecordDetailsSelectBO;
import com.ruoyi.common.order.entity.WithdrawalRecordDetails;
import com.ruoyi.common.order.vo.RevenueVO;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/12 17:11
 */
public interface WithdrawalRecordDetailsService extends IService<WithdrawalRecordDetails> {


    /**
     * 提现记录详情列表分页查询
     * @return
     * @throws BizException
     */
     PageResult<WithdrawalRecordDetails> selectWithdrawalRecordDetailsListPage(WithdrawalRecordDetailsSelectBO withdrawalRecordDetailsSelectBO) throws BizException;


    /**
     * 计算提现记录总余额
     * @param withdrawalRecordId
     * @return
     */
     Integer computeWithdrawalRecordBalance(Integer withdrawalRecordId);


    /**
     * 删除提现记录详情
     */
     void  deleteWithdrawalRecordDetails(Integer withdrawalRecordDetailsId);

    /**
     * 查询登陆用户收益记录
     * @param loginUser
     * @return
     */
    RevenueVO selectRevenue(LoginUser loginUser);

}
