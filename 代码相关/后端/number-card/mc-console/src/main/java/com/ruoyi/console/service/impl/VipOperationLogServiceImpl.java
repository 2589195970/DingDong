package com.ruoyi.console.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.vip.entity.VipOperationLog;
import com.ruoyi.console.mapper.VipOperationLogMapper;
import com.ruoyi.console.service.VipOperationLogService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * VIP操作日志服务实现
 *
 * @author Codex
 */
@Service
public class VipOperationLogServiceImpl extends ServiceImpl<VipOperationLogMapper, VipOperationLog> implements VipOperationLogService {

    @Override
    public void recordOperation(VipOperationLog operationLog) {
        if (operationLog == null) {
            return;
        }
        long now = System.currentTimeMillis();
        if (operationLog.getOperationTime() == null) {
            operationLog.setOperationTime(now);
        }
        if (operationLog.getStatus() == null) {
            operationLog.setStatus(BaseConstant.ONE_INT);
        }
        if (operationLog.getExecutionTime() == null) {
            operationLog.setExecutionTime(BaseConstant.ZERO_INT);
        }
        save(operationLog);
    }

    @Override
    public List<VipOperationLog> selectRecentLogs(String targetAgentCode, int limit) {
        LambdaQueryWrapper<VipOperationLog> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(targetAgentCode)) {
            queryWrapper.eq(VipOperationLog::getTargetAgentCode, targetAgentCode);
        }
        queryWrapper.orderByDesc(VipOperationLog::getOperationTime);
        if (limit > 0) {
            queryWrapper.last("limit " + Math.min(limit, 200));
        }
        return list(queryWrapper);
    }
}
