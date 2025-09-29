package com.ruoyi.console.service.impl.agent;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.PageResultFactory;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.LiveAuditSelectBO;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.order.entity.LiveAudit;
import com.ruoyi.common.order.entity.LiveConfig;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.console.mapper.LiveAuditMapper;
import com.ruoyi.console.mapper.LiveConfigMapper;
import com.ruoyi.console.service.AgentAccountService;
import com.ruoyi.console.service.AgentLiveAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 代理商直播审核服务实现
 *
 * @Description 代理商直播管理服务实现类
 * @Author Claude Code
 * @Date 2025/01/28
 */
@Service
@Slf4j
public class AgentLiveAuditServiceImpl extends ServiceImpl<LiveAuditMapper, LiveAudit> implements AgentLiveAuditService {

    @Resource
    AgentAccountService agentAccountService;

    @Resource
    LiveConfigMapper liveConfigMapper;

    /**
     * 代理商直播审核列表查询
     *
     * @param liveAuditSelectBO 查询条件
     * @return 分页结果
     * @throws BizException 业务异常
     */
    @Override
    public PageResult<LiveAudit> selectLiveAuditListPage(LiveAuditSelectBO liveAuditSelectBO) throws BizException {
        Page page = new Page(liveAuditSelectBO.getPageNo(), liveAuditSelectBO.getPageSize());
        Page<LiveAudit> liveAuditPage = baseMapper.selectPage(page, new LambdaQueryWrapper<LiveAudit>()
                .like(StringUtils.isNotEmpty(liveAuditSelectBO.getAgentName()), LiveAudit::getAgentName, liveAuditSelectBO.getAgentName())
                .like(StringUtils.isNotEmpty(liveAuditSelectBO.getAgentCode()), LiveAudit::getAgentCode, liveAuditSelectBO.getAgentCode())
                .like(StringUtils.isNotEmpty(liveAuditSelectBO.getDouyinAccount()), LiveAudit::getDouyinAccount, liveAuditSelectBO.getDouyinAccount())
                .eq(liveAuditSelectBO.getStatus() != null, LiveAudit::getStatus, liveAuditSelectBO.getStatus())
                .orderByDesc(LiveAudit::getCreateTime)
        );
        return PageResultFactory.createPageResult(liveAuditPage);
    }

    /**
     * 查询登录用户直播审核记录
     *
     * @param loginUser 登录用户
     * @return 直播审核记录
     * @throws BizException 业务异常
     */
    @Override
    public LiveAudit selectLiveAudit(LoginUser loginUser) throws BizException {
        AgentAccount agentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(), true);
        return baseMapper.selectOne(new LambdaQueryWrapper<LiveAudit>().eq(LiveAudit::getAgentCode, agentAccount.getAgentCode()));
    }

    /**
     * 新增直播审核记录
     *
     * @param liveAudit 直播审核信息
     * @throws BizException 业务异常
     */
    @Override
    public void addLiveAudit(LiveAudit liveAudit) throws BizException {
        AgentAccount agentAccount = agentAccountService.getAgentAccountByCode(liveAudit.getAgentCode(), true);
        List<LiveAudit> liveAuditList = baseMapper.selectList(new LambdaQueryWrapper<LiveAudit>().eq(LiveAudit::getAgentCode, liveAudit.getAgentCode()));
        if (!CollectionUtil.isEmpty(liveAuditList)) {
            throw new BizException("已存在直播审核记录");
        }
        if (StringUtils.isEmpty(liveAudit.getBackgroundImage())) {
            throw new BizException("直播背景图不能为空");
        }
        if (StringUtils.isEmpty(liveAudit.getDouyinUid())) {
            throw new BizException("抖音UID不能为空");
        }
        // 验证抖音UID格式(纯数字)
        if (!liveAudit.getDouyinUid().matches("^\\d+$")) {
            throw new BizException("抖音UID格式错误，应为纯数字");
        }
        if (StringUtils.isEmpty(liveAudit.getDouyinAccount())) {
            throw new BizException("抖音号不能为空");
        }
        // 验证背景图URL格式
        if (!liveAudit.getBackgroundImage().matches("^https?://.*\\.(jpg|jpeg|png|gif)$")) {
            throw new BizException("背景图URL格式错误，应为有效的图片链接");
        }
        liveAudit.setAgentCode(agentAccount.getAgentCode());
        liveAudit.setAgentName(agentAccount.getAgentName());
        liveAudit.setSysUserId(agentAccount.getSysUserId());
        liveAudit.setStatus(BaseConstant.ZERO_INT);
        liveAudit.setCreateTime(System.currentTimeMillis());
        baseMapper.insert(liveAudit);
    }

    /**
     * 更新直播审核记录
     *
     * @param liveAudit 直播审核信息
     * @throws BizException 业务异常
     */
    @Override
    public void updateLiveAudit(LiveAudit liveAudit) throws BizException {
        LiveAudit liveAuditOld = baseMapper.selectById(liveAudit.getLiveAuditId());
        if (liveAuditOld == null) {
            throw new BizException("直播审核记录ID:{}不存在", liveAudit.getLiveAuditId());
        }
        // 数据验证
        if (StringUtils.isNotEmpty(liveAudit.getDouyinUid()) && !liveAudit.getDouyinUid().matches("^\\d+$")) {
            throw new BizException("抖音UID格式错误，应为纯数字");
        }
        if (StringUtils.isNotEmpty(liveAudit.getBackgroundImage()) &&
            !liveAudit.getBackgroundImage().matches("^https?://.*\\.(jpg|jpeg|png|gif)$")) {
            throw new BizException("背景图URL格式错误，应为有效的图片链接");
        }

        liveAuditOld.setBackgroundImage(liveAudit.getBackgroundImage());
        liveAuditOld.setDouyinUid(liveAudit.getDouyinUid());
        liveAuditOld.setDouyinAccount(liveAudit.getDouyinAccount());
        liveAuditOld.setStatus(BaseConstant.ZERO_INT);
        liveAuditOld.setUpdateTime(System.currentTimeMillis());
        baseMapper.updateById(liveAuditOld);
    }

    /**
     * 更新直播审核状态
     *
     * @param liveAudit 直播审核信息
     * @throws BizException 业务异常
     */
    @Override
    public void updateLiveAuditStatus(LiveAudit liveAudit) throws BizException {
        LiveAudit liveAuditOld = baseMapper.selectById(liveAudit.getLiveAuditId());
        if (liveAuditOld == null) {
            throw new BizException("直播审核记录ID:{}不存在", liveAudit.getLiveAuditId());
        }
        liveAuditOld.setRemark(liveAudit.getRemark());
        liveAuditOld.setStatus(liveAudit.getStatus());
        liveAuditOld.setUpdateTime(System.currentTimeMillis());
        baseMapper.updateById(liveAuditOld);
    }

    /**
     * 删除直播审核记录
     *
     * @param liveAuditId 直播审核ID
     * @throws BizException 业务异常
     */
    @Override
    public void deleteLiveAudit(Integer liveAuditId) throws BizException {
        LiveAudit liveAudit = baseMapper.selectById(liveAuditId);
        if (liveAudit == null) {
            throw new BizException("直播审核记录ID:{}不存在", liveAuditId);
        }
        baseMapper.deleteById(liveAuditId);
    }

    /**
     * 获取直播配置说明
     *
     * @param configKey 配置键
     * @return 直播配置
     * @throws BizException 业务异常
     */
    @Override
    public LiveConfig getLiveConfig(String configKey) throws BizException {
        LiveConfig liveConfig = liveConfigMapper.selectOne(new LambdaQueryWrapper<LiveConfig>()
                .eq(LiveConfig::getConfigKey, configKey)
                .eq(LiveConfig::getStatus, BaseConstant.ONE_INT));
        if (liveConfig == null) {
            throw new BizException("直播配置不存在");
        }
        return liveConfig;
    }

    /**
     * 更新直播配置说明
     *
     * @param liveConfig 直播配置
     * @throws BizException 业务异常
     */
    @Override
    public void updateLiveConfig(LiveConfig liveConfig) throws BizException {
        LiveConfig liveConfigOld = liveConfigMapper.selectOne(new LambdaQueryWrapper<LiveConfig>()
                .eq(LiveConfig::getConfigKey, liveConfig.getConfigKey()));
        if (liveConfigOld == null) {
            // 新增配置
            liveConfig.setStatus(BaseConstant.ONE_INT);
            liveConfig.setCreateTime(System.currentTimeMillis());
            liveConfig.setUpdateTime(System.currentTimeMillis());
            liveConfigMapper.insert(liveConfig);
        } else {
            // 更新配置
            liveConfigOld.setConfigValue(liveConfig.getConfigValue());
            liveConfigOld.setConfigType(liveConfig.getConfigType());
            liveConfigOld.setDescription(liveConfig.getDescription());
            liveConfigOld.setUpdateTime(System.currentTimeMillis());
            liveConfigMapper.updateById(liveConfigOld);
        }
    }
}