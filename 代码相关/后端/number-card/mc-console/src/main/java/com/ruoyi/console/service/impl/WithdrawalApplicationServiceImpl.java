package com.ruoyi.console.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.PageResultFactory;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.WithdrawalApplicationAddBO;
import com.ruoyi.common.order.bo.WithdrawalApplicationSelectBO;
import com.ruoyi.common.order.bo.WithdrawalApplicationUpdateBO;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.order.entity.WithdrawalApplication;
import com.ruoyi.common.order.entity.WithdrawalConfig;
import com.ruoyi.common.order.entity.WithdrawalRecord;
import com.ruoyi.common.order.vo.ComputeRateVO;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.console.mapper.WithdrawalApplicationMapper;
import com.ruoyi.console.service.AgentAccountService;
import com.ruoyi.console.service.WithdrawalApplicationService;
import com.ruoyi.console.service.WithdrawalConfigService;
import com.ruoyi.console.service.WithdrawalRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


/**
 * 申请提现相关API
 *
 * @Description
 * @Author 陈思伟
 * @Date 2025/02/04 17:13
 */
@Service
@Slf4j
public class WithdrawalApplicationServiceImpl extends ServiceImpl<WithdrawalApplicationMapper, WithdrawalApplication> implements WithdrawalApplicationService {

    @Resource
    WithdrawalConfigService withdrawalConfigService;

    @Resource
    WithdrawalRecordService withdrawalRecordService;

    @Resource
    AgentAccountService agentAccountService;

    /**
     * 申请提现记录查询
     *
     * @return
     * @throws BizException
     */
    public PageResult<WithdrawalApplication> selectWithdrawalApplicationListPage(WithdrawalApplicationSelectBO withdrawalApplicationSelectBO) throws BizException {
        //读取分页
        Page page = new Page(withdrawalApplicationSelectBO.getPageNo(), withdrawalApplicationSelectBO.getPageSize());
        Page<WithdrawalApplication> withdrawalApplicationPage = baseMapper.selectPage(page, new LambdaQueryWrapper<WithdrawalApplication>()
                .eq(StringUtils.isNotEmpty(withdrawalApplicationSelectBO.getApplicationNumber()), WithdrawalApplication::getApplicationNumber, withdrawalApplicationSelectBO.getApplicationNumber())
                .eq(StringUtils.isNotEmpty(withdrawalApplicationSelectBO.getApplyAgentCode()), WithdrawalApplication::getApplyAgentCode, withdrawalApplicationSelectBO.getApplyAgentCode())
                .eq(withdrawalApplicationSelectBO.getApplyUserId() != null, WithdrawalApplication::getApplyUserId, withdrawalApplicationSelectBO.getApplyUserId())
                .eq(withdrawalApplicationSelectBO.getWithdrawalStatus() != null, WithdrawalApplication::getWithdrawalStatus, withdrawalApplicationSelectBO.getWithdrawalStatus())
                .eq(withdrawalApplicationSelectBO.getWithdrawalType() != null, WithdrawalApplication::getWithdrawalType, withdrawalApplicationSelectBO.getWithdrawalType())
                .between((withdrawalApplicationSelectBO.getStarTime() != null && withdrawalApplicationSelectBO.getEndTime() != null), WithdrawalApplication::getCreateTime, withdrawalApplicationSelectBO.getStarTime(), withdrawalApplicationSelectBO.getEndTime())
        );
        return PageResultFactory.createPageResult(withdrawalApplicationPage);
    }


    /**
     * 新增申请提现记录
     */
    public void addWithdrawalApplication(WithdrawalApplicationAddBO withdrawalApplicationAddBO) throws BizException {
        if (withdrawalApplicationAddBO == null || withdrawalApplicationAddBO.getApplyUserId() == null) {
            throw new BizException("提现申请人参数不能为空");
        }
        WithdrawalApplication withdrawalApplication = new WithdrawalApplication();
        BeanUtil.copyProperties(withdrawalApplicationAddBO, withdrawalApplication);
        //查询申请提现账户信息
        AgentAccount agentAccount = agentAccountService.getAgentAccountByUserId(withdrawalApplicationAddBO.getApplyUserId(), true);
        List<WithdrawalApplication> withdrawalApplicationList = baseMapper.selectList(new LambdaQueryWrapper<WithdrawalApplication>().eq(WithdrawalApplication::getApplyUserId, agentAccount.getSysUserId()).eq(WithdrawalApplication::getWithdrawalStatus, BaseConstant.ZERO_INT));
        if (!CollectionUtils.isEmpty(withdrawalApplicationList)) {
            throw new BizException("您已经有一条提现申请记录,请审批后再次申请");
        }

        //计算费率
        ComputeRateVO computeRateVO = computeRate(withdrawalApplicationAddBO.getWithdrawalAmount());
        //计算余额是否足够
        WithdrawalRecord withdrawalRecord = withdrawalRecordService.selectWithdrawalRecord(agentAccount.getAgentCode(),null);
        if(withdrawalRecord.getBalance()<computeRateVO.getWithdrawalAmount()){
            throw new BizException("余额不足,请减少提现金额");
        }
        withdrawalApplication.setApplicationNumber(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + RandomUtil.randomNumbers(BaseConstant.SEX_INT));
        withdrawalApplication.setApplyUserId(agentAccount.getSysUserId());
        withdrawalApplication.setApplyAgentCode(agentAccount.getAgentCode());
        withdrawalApplication.setApplyAgentName(agentAccount.getAgentName());
        withdrawalApplication.setWithdrawalAmount(computeRateVO.getWithdrawalAmount());
        withdrawalApplication.setReceivedAmount(computeRateVO.getReceivedAmount());
        withdrawalApplication.setWithdrawalRate(computeRateVO.getWithdrawalRate());
        withdrawalApplication.setWithdrawalRateAmount(computeRateVO.getWithdrawalRateAmount());
        withdrawalApplication.setWithdrawalStatus(BaseConstant.ZERO_INT);
        withdrawalApplication.setCreateTime(System.currentTimeMillis());
        baseMapper.insert(withdrawalApplication);
    }


    /**
     * 更新申请提现记录
     */
    public void updateWithdrawalApplication(WithdrawalApplicationUpdateBO withdrawalApplicationUpdateBO) throws BizException {
        if (withdrawalApplicationUpdateBO == null || withdrawalApplicationUpdateBO.getWithdrawalApplicationId() == null) {
            throw new BizException("申请记录ID不能为空");
        }
        WithdrawalApplication withdrawalApplication = baseMapper.selectById(withdrawalApplicationUpdateBO.getWithdrawalApplicationId());
        if(withdrawalApplication==null){
            throw new BizException("{}申请提现记录不存在",withdrawalApplication.getWithdrawalApplicationId());
        }
        //提现成功或提现失败属于最终状态 不允许修改
        if(withdrawalApplication.getWithdrawalStatus()>=BaseConstant.TWO_INT){
            throw new BizException("申请记录处于最终状态,不允许修改");
        }
        withdrawalApplication.setWithdrawalStatus(withdrawalApplicationUpdateBO.getWithdrawalStatus()!=null?withdrawalApplicationUpdateBO.getWithdrawalStatus():withdrawalApplication.getWithdrawalStatus());
        withdrawalApplication.setSerialNumber(StringUtils.isNotEmpty(withdrawalApplicationUpdateBO.getSerialNumber())?withdrawalApplicationUpdateBO.getSerialNumber():withdrawalApplication.getSerialNumber());
        withdrawalApplication.setRemark(StringUtils.isNotEmpty(withdrawalApplicationUpdateBO.getRemark())?withdrawalApplicationUpdateBO.getRemark():withdrawalApplication.getRemark());
        withdrawalApplication.setUpdateTime(System.currentTimeMillis());
        baseMapper.updateById(withdrawalApplication);
        //提现成功后变更余额记录
        if(withdrawalApplicationUpdateBO.getWithdrawalStatus().equals(BaseConstant.TWO_INT)){
            withdrawalRecordService.insertBalanceCommissionWithdrawal(withdrawalApplication);
        }
    }



    /**
     * 计算提现费率
     *
     * @param withdrawalAmount 提现金额 单位元
     */
    public ComputeRateVO computeRate(Integer withdrawalAmount) throws BizException {
        WithdrawalConfig withdrawalConfig = withdrawalConfigService.selectWithdrawalConfig();
        if (withdrawalAmount == null || withdrawalAmount < withdrawalConfig.getWithdrawMinAmount()) {
            throw new BizException("未达到最小提现金额要求");
        }
        ComputeRateVO computeRateVO = new ComputeRateVO();
        computeRateVO.setWithdrawalAmount(withdrawalAmount * 100);
        computeRateVO.setWithdrawalRate(withdrawalConfig.getWithdrawRate());
        //计算提现手续费
        computeRateVO.setWithdrawalRateAmount(computeRateVO.getWithdrawalAmount() * computeRateVO.getWithdrawalRate() / 100);
        computeRateVO.setReceivedAmount(computeRateVO.getWithdrawalAmount() - computeRateVO.getWithdrawalRateAmount());
        return computeRateVO;
    }


    /**
     * 删除提现申请
     */
    public void deleteWithdrawalApplication(Integer withdrawalApplicationId){
        baseMapper.deleteById(withdrawalApplicationId);
    }

}
