package com.ruoyi.console.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.vip.bo.VipUpgradeLogQueryBO;
import com.ruoyi.common.vip.entity.VipUpgradeLog;
import com.ruoyi.common.vip.vo.VipUpgradeLogVO;

import java.util.List;

/**
 * VIP升级日志服务接口
 *
 * 提供VIP等级升级记录的写入与查询能力
 *
 * @author Codex
 */
public interface VipUpgradeLogService extends IService<VipUpgradeLog> {

    /**
     * 记录VIP等级升级日志
     *
     * @param upgradeLog 升级日志
     */
    void recordUpgrade(VipUpgradeLog upgradeLog);

    /**
     * 查询指定代理商最近的升级日志
     *
     * @param agentCode 代理商编码
     * @param limit 返回数量
     * @return 升级日志列表
     */
    List<VipUpgradeLog> selectRecentUpgrades(String agentCode, int limit);

    /**
     * 查询用户最近一次升级记录
     *
     * @param userId 用户ID
     * @return 最近的升级日志
     */
    VipUpgradeLog selectLatestUpgrade(Long userId);

    /**
     * 分页查询升级日志
     *
     * @param queryBO 查询参数
     * @return 分页结果
     */
    PageResult<VipUpgradeLogVO> selectVipUpgradeLogPage(VipUpgradeLogQueryBO queryBO);

    /**
     * 按条件查询升级日志列表（用于导出）
     *
     * @param queryBO 查询参数
     * @return 日志集合
     */
    List<VipUpgradeLogVO> selectVipUpgradeLogList(VipUpgradeLogQueryBO queryBO);

    /**
     * 根据ID集合查询升级日志
     *
     * @param ids 日志ID集合
     * @return 日志集合
     */
    List<VipUpgradeLogVO> selectVipUpgradeLogListByIds(List<Long> ids);
}
