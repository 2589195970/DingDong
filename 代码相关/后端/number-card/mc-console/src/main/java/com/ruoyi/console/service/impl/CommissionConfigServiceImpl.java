package com.ruoyi.console.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.entity.CommissionConfig;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.console.mapper.CommissionConfigMapper;
import com.ruoyi.console.service.CommissionConfigService;
import com.ruoyi.framework.web.domain.server.Sys;
import org.springframework.stereotype.Service;



/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/12 17:13
 */
@Service
public class CommissionConfigServiceImpl extends ServiceImpl<CommissionConfigMapper, CommissionConfig> implements CommissionConfigService {

    /**
     * 新增佣金配置
     * @param commissionConfig
     */
    public void  addCommissionConfig(CommissionConfig commissionConfig){
        if(commissionConfig.getCommissionConfigType()==null){
            //20250501默认修改为每层保留5%佣金
            commissionConfig.setCommissionConfigType(BaseConstant.ONE_INT);
            commissionConfig.setScaleCommission(BaseConstant.FIVE_INT);
        }
        if(commissionConfig.getFixedCommission()==null){
            commissionConfig.setFixedCommission(BaseConstant.ZERO_INT);
        }
        if(commissionConfig.getScaleCommission()==null){
            commissionConfig.setScaleCommission(BaseConstant.FIVE_INT);
        }
        commissionConfig.setCreateTime(System.currentTimeMillis());
        baseMapper.insert(commissionConfig);

    }

    /**
     * 修改佣金配置
     * @param commissionConfig
     */
    public void updateCommissionConfig(CommissionConfig commissionConfig) throws BizException {
        if(commissionConfig.getScaleCommission()!=null){
            if(commissionConfig.getScaleCommission()>25){
                throw new BizException("佣金比例不能高于25%");
            }
        }
        commissionConfig.setCreateTime(System.currentTimeMillis());
        baseMapper.updateById(commissionConfig);
    }



    /**
     * 根据代理商编码计算分销佣金
     * @return
     */
    public Integer computeCommission(String agentCode,Integer commission) throws BizException {
        if(StringUtils.isEmpty(agentCode)|| commission == null){
            throw new BizException("佣金计算参数不能为空");
        }
        Integer distributionCommission = BaseConstant.ZERO_INT;
        CommissionConfig commissionConfig= baseMapper.selectOne(new LambdaQueryWrapper<CommissionConfig>().eq(CommissionConfig::getAgentCode,agentCode));
        //未查询到代理商配置 默认佣金为0 代理商自己设置
        if(commissionConfig == null){
            return distributionCommission;
        }
        //固定佣金
        if(commissionConfig.getCommissionConfigType() == BaseConstant.ZERO_INT){
            distributionCommission = commission - commissionConfig.getFixedCommission();
            //佣金范围超限 返回0 手动设置
            if(distributionCommission<BaseConstant.ZERO_INT){
                return BaseConstant.ZERO_INT;
            }
            return distributionCommission;
        }
        //百分比佣金
        if(commissionConfig.getCommissionConfigType() == BaseConstant.ONE_INT){
            //计算保留佣金并向上取整
            int scaleCommission = (int) Math.ceil(((double)commission*commissionConfig.getScaleCommission())/100);
            distributionCommission = commission - scaleCommission;
            //佣金范围超限
            if(distributionCommission>commission){
                return commission;
            }
            return distributionCommission;
        }
        return distributionCommission;
    }


}
