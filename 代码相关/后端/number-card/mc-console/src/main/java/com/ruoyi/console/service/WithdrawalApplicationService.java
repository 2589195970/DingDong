package com.ruoyi.console.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.WithdrawalApplicationAddBO;
import com.ruoyi.common.order.bo.WithdrawalApplicationSelectBO;
import com.ruoyi.common.order.bo.WithdrawalApplicationUpdateBO;
import com.ruoyi.common.order.entity.OrderCommissionDetails;
import com.ruoyi.common.order.entity.WithdrawalApplication;
import com.ruoyi.common.order.entity.WithdrawalRecord;
import com.ruoyi.common.order.vo.ComputeRateVO;
import org.springframework.transaction.annotation.Transactional;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2025/02/04 17:11
 */
public interface WithdrawalApplicationService extends IService<WithdrawalApplication> {

    /**
     * 申请提现记录查询
     *
     * @return
     * @throws BizException
     */
     PageResult<WithdrawalApplication> selectWithdrawalApplicationListPage(WithdrawalApplicationSelectBO withdrawalApplicationSelectBO) throws BizException;



    /**
     * 新增申请提现记录
     */
     void addWithdrawalApplication(WithdrawalApplicationAddBO withdrawalApplicationAddBO) throws BizException;


    /**
     * 更新申请提现记录
     */
     void updateWithdrawalApplication(WithdrawalApplicationUpdateBO withdrawalApplicationUpdateBO) throws BizException;


    /**
     * 计算提现费率
     *
     * @param withdrawalAmount 提现金额 单位元
     */
     ComputeRateVO computeRate(Integer withdrawalAmount) throws BizException;


    /**
     * 删除提现申请
     */
     void deleteWithdrawalApplication(Integer withdrawalApplicationId);


}
