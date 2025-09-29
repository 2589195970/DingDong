package com.ruoyi.console.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.LiveAuditSelectBO;
import com.ruoyi.common.order.entity.LiveAudit;
import com.ruoyi.common.order.entity.LiveConfig;

/**
 * 代理商直播审核Service
 *
 * @Description 代理商直播管理服务接口
 * @Author Claude Code
 * @Date 2025/01/28
 */
public interface AgentLiveAuditService extends IService<LiveAudit> {

    /**
     * 代理商直播审核列表查询
     *
     * @param liveAuditSelectBO 查询条件
     * @return 分页结果
     * @throws BizException 业务异常
     */
    PageResult<LiveAudit> selectLiveAuditListPage(LiveAuditSelectBO liveAuditSelectBO) throws BizException;

    /**
     * 查询登录用户直播审核记录
     *
     * @param loginUser 登录用户
     * @return 直播审核记录
     * @throws BizException 业务异常
     */
    LiveAudit selectLiveAudit(LoginUser loginUser) throws BizException;

    /**
     * 新增直播审核记录
     *
     * @param liveAudit 直播审核信息
     * @throws BizException 业务异常
     */
    void addLiveAudit(LiveAudit liveAudit) throws BizException;

    /**
     * 更新直播审核记录
     *
     * @param liveAudit 直播审核信息
     * @throws BizException 业务异常
     */
    void updateLiveAudit(LiveAudit liveAudit) throws BizException;

    /**
     * 更新直播审核状态
     *
     * @param liveAudit 直播审核信息
     * @throws BizException 业务异常
     */
    void updateLiveAuditStatus(LiveAudit liveAudit) throws BizException;

    /**
     * 删除直播审核记录
     *
     * @param liveAuditId 直播审核ID
     * @throws BizException 业务异常
     */
    void deleteLiveAudit(Integer liveAuditId) throws BizException;

    /**
     * 获取直播配置说明
     *
     * @param configKey 配置键
     * @return 直播配置
     * @throws BizException 业务异常
     */
    LiveConfig getLiveConfig(String configKey) throws BizException;

    /**
     * 更新直播配置说明
     *
     * @param liveConfig 直播配置
     * @throws BizException 业务异常
     */
    void updateLiveConfig(LiveConfig liveConfig) throws BizException;
}