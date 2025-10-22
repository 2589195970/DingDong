package com.ruoyi.console.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.vip.entity.VipOperationLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * VIP操作日志Mapper
 *
 * 负责VIP操作日志的持久化操作
 *
 * @author Codex
 */
@Mapper
public interface VipOperationLogMapper extends BaseMapper<VipOperationLog> {

}
