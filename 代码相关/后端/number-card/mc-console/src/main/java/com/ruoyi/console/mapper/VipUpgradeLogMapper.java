package com.ruoyi.console.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.vip.entity.VipUpgradeLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * VIP升级日志Mapper
 *
 * 负责VIP等级变更记录的CRUD操作
 *
 * @author Codex
 */
@Mapper
public interface VipUpgradeLogMapper extends BaseMapper<VipUpgradeLog> {

}
