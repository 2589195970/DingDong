package com.ruoyi.console.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.vip.bo.VipConfigQueryBO;
import com.ruoyi.common.vip.bo.VipConfigSaveBO;
import com.ruoyi.common.vip.entity.VipConfig;
import com.ruoyi.common.vip.vo.VipConfigVO;

import java.util.List;

/**
 * VIP等级配置服务接口
 *
 * 负责VIP等级配置的查询与维护
 *
 * @author Codex
 */
public interface VipConfigService extends IService<VipConfig> {

    /**
     * 分页查询VIP配置
     *
     * @param queryBO 查询参数
     * @return 分页结果
     * @throws BizException 业务异常
     */
    PageResult<VipConfigVO> selectVipConfigPage(VipConfigQueryBO queryBO) throws BizException;

    /**
     * 查询所有VIP等级配置，按等级升序排序
     *
     * @return VIP配置列表
     */
    List<VipConfig> selectVipConfigList();

    /**
     * 查询启用中的VIP等级配置
     *
     * @return 启用的VIP配置集合
     */
    List<VipConfig> selectEnabledVipConfigs();

    /**
     * 根据VIP等级查询配置
     *
     * @param vipLevel VIP等级
     * @return VIP配置
     */
    VipConfig selectByVipLevel(Integer vipLevel);

    /**
     * 查询配置详情
     *
     * @param id 配置ID
     * @return 配置详情
     */
    VipConfigVO selectVipConfigDetail(Long id);

    /**
     * 新增VIP等级配置
     *
     * @param saveBO VIP配置
     * @param loginUser 操作用户
     * @throws BizException 业务异常
     */
    void addVipConfig(VipConfigSaveBO saveBO, LoginUser loginUser) throws BizException;

    /**
     * 更新VIP等级配置
     *
     * @param saveBO VIP配置
     * @param loginUser 操作用户
     * @throws BizException 业务异常
     */
    void updateVipConfig(VipConfigSaveBO saveBO, LoginUser loginUser) throws BizException;

    /**
     * 批量删除配置
     *
     * @param ids 配置ID集合
     * @param loginUser 操作用户
     * @throws BizException 业务异常
     */
    void deleteVipConfigByIds(List<Long> ids, LoginUser loginUser) throws BizException;

    /**
     * 更新配置启用状态
     *
     * @param id 配置ID
     * @param isEnabled 是否启用（0/1）
     * @param loginUser 操作用户
     * @throws BizException 业务异常
     */
    void updateVipConfigStatus(Long id, Integer isEnabled, LoginUser loginUser) throws BizException;

    /**
     * 启用或停用配置
     *
     * @param id 配置ID
     * @param enable 是否启用
     * @param loginUser 操作用户
     * @throws BizException 业务异常
     */
    void toggleVipConfig(Long id, boolean enable, LoginUser loginUser) throws BizException;
}
