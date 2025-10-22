package com.ruoyi.console.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.vip.entity.VipOperationLog;

import java.util.List;

/**
 * VIP操作日志服务接口
 *
 * 负责记录和查询VIP相关操作日志
 *
 * @author Codex
 */
public interface VipOperationLogService extends IService<VipOperationLog> {

    /**
     * 记录VIP操作日志
     *
     * @param operationLog 操作日志
     */
    void recordOperation(VipOperationLog operationLog);

    /**
     * 查询指定代理商最近的操作日志
     *
     * @param targetAgentCode 代理商编码
     * @param limit 返回数量
     * @return 操作日志列表
     */
    List<VipOperationLog> selectRecentLogs(String targetAgentCode, int limit);
}
