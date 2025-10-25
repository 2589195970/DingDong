package com.ruoyi.console.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.PageResultFactory;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.common.vip.bo.VipUserQueryBO;
import com.ruoyi.common.vip.bo.VipUserSaveBO;
import com.ruoyi.common.vip.bo.VipUserSetLevelBO;
import com.ruoyi.common.vip.entity.VipConfig;
import com.ruoyi.common.vip.entity.VipOperationLog;
import com.ruoyi.common.vip.entity.VipUpgradeLog;
import com.ruoyi.common.vip.entity.VipUser;
import com.ruoyi.common.vip.vo.VipUserSummaryVO;
import com.ruoyi.common.vip.vo.VipUserVO;
import com.ruoyi.console.mapper.VipUserMapper;
import com.ruoyi.console.service.AgentAccountService;
import com.ruoyi.console.service.VipConfigService;
import com.ruoyi.console.service.VipOperationLogService;
import com.ruoyi.console.service.VipUpgradeLogService;
import com.ruoyi.console.service.VipUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * VIP用户服务实现
 *
 * @author Codex
 */
@Service
@Slf4j
public class VipUserServiceImpl extends ServiceImpl<VipUserMapper, VipUser> implements VipUserService {

    private static final String TAG = "VipUserService";

    @Resource
    private AgentAccountService agentAccountService;

    @Resource
    private VipConfigService vipConfigService;

    @Resource
    private VipOperationLogService vipOperationLogService;

    @Resource
    private VipUpgradeLogService vipUpgradeLogService;

    @Override
    public PageResult<VipUserVO> selectVipUserPage(VipUserQueryBO queryBO) {
        if (queryBO == null) {
            queryBO = new VipUserQueryBO();
        }
        int pageNo = queryBO.getPageNo() == null || queryBO.getPageNo() <= 0 ? 1 : queryBO.getPageNo();
        int pageSize = queryBO.getPageSize() == null || queryBO.getPageSize() <= 0 ? 10 : queryBO.getPageSize();

        LambdaQueryWrapper<VipUser> queryWrapper = buildQueryWrapper(queryBO);
        queryWrapper.orderByDesc(VipUser::getUpdateTime).orderByDesc(VipUser::getCreateTime);

        Page<VipUser> page = page(new Page<>(pageNo, pageSize), queryWrapper);
        List<VipUserVO> voList = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        Page<VipUserVO> voPage = new Page<>(pageNo, pageSize);
        voPage.setTotal(page.getTotal());
        voPage.setSize(page.getSize());
        voPage.setCurrent(pageNo);
        voPage.setRecords(voList);
        return PageResultFactory.createPageResult(voPage);
    }

    @Override
    public VipUserVO selectVipUserDetail(Long id) {
        if (id == null) {
            return null;
        }
        VipUser vipUser = getById(id);
        return convertToVO(vipUser);
    }

    @Override
    public VipUserSummaryVO selectVipSummaryByUserId(Long userId) {
        VipUserSummaryVO summary = buildDefaultSummary();
        if (userId == null) {
            return summary;
        }

        try {
            LambdaQueryWrapper<VipUser> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(VipUser::getUserId, userId).last("limit 1");
            VipUser vipUser = getOne(queryWrapper, false);
            if (vipUser != null) {
                BeanUtils.copyProperties(vipUser, summary);
                summary.setHasVipRecord(true);
                populateLevelMeta(summary, vipUser.getVipLevel());
                return summary;
            }
        } catch (Exception e) {
            log.error("{}#selectVipSummaryByUserId query vip user error", TAG, e);
        }

        try {
            AgentAccount agentAccount = agentAccountService.getAgentAccountByUserId(userId, false);
            if (agentAccount != null) {
                summary.setAgentAccountId(agentAccount.getAgentAccountId());
                summary.setUserId(agentAccount.getSysUserId());
                summary.setAgentCode(agentAccount.getAgentCode());
                summary.setAgentName(agentAccount.getAgentName());
                summary.setVipLevel(agentAccount.getLevel());
                summary.setUpdateTime(resolveTimestamp(agentAccount.getUpdateTime(), new Date()));
                populateLevelMeta(summary, summary.getVipLevel());
            }
        } catch (BizException e) {
            log.debug("{}#selectVipSummaryByUserId agent account missing: {}", TAG, e.getMessage());
        } catch (Exception e) {
            log.error("{}#selectVipSummaryByUserId fetch agent account error", TAG, e);
        }

        return summary;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addVipUser(VipUserSaveBO saveBO, LoginUser loginUser) throws BizException {
        validateOperator(loginUser);
        validateVipUserSave(saveBO, true);

        AgentAccount agentAccount = agentAccountService.getAgentAccountByCode(saveBO.getAgentCode(), true);
        validateAgentAccount(saveBO, agentAccount);

        if (existsByUserId(saveBO.getUserId())) {
            throw new BizException("该用户已存在VIP记录");
        }
        if (existsByAgentCode(saveBO.getAgentCode())) {
            throw new BizException("该代理商编码已存在VIP记录");
        }
        ensureVipLevelExists(saveBO.getVipLevel());
        ensureNotDowngrade(agentAccount.getLevel(), saveBO.getVipLevel());

        Date now = new Date();
        VipUser vipUser = new VipUser();
        BeanUtils.copyProperties(saveBO, vipUser);
        vipUser.setAgentAccountId(agentAccount.getAgentAccountId());
        vipUser.setAgentName(agentAccount.getAgentName());
        vipUser.setCreateTime(now);
        vipUser.setUpdateTime(now);
        vipUser.setOperatorId(loginUser.getUserId());
        vipUser.setOperatorName(getOperatorName(loginUser));

        save(vipUser);

        handleLevelChange(vipUser, agentAccount, agentAccount.getLevel(), saveBO.getVipLevel(), loginUser, saveBO.getRemark());

        recordOperation(loginUser, saveBO.getAgentCode(), "USER_ADD", "addVipUser", saveBO, "success", BaseConstant.ONE_INT);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateVipUser(Long id, VipUserSaveBO saveBO, LoginUser loginUser) throws BizException {
        validateOperator(loginUser);
        if (id == null) {
            throw new BizException("缺少VIP用户记录ID");
        }
        validateVipUserSave(saveBO, false);

        VipUser exist = getById(id);
        if (exist == null) {
            throw new BizException("待更新的VIP用户不存在");
        }
        if (!Objects.equals(exist.getUserId(), saveBO.getUserId())) {
            throw new BizException("用户ID不允许修改");
        }
        if (!StringUtils.equals(exist.getAgentCode(), saveBO.getAgentCode())) {
            throw new BizException("代理商编码不允许修改");
        }

        AgentAccount agentAccount = agentAccountService.getAgentAccountByCode(saveBO.getAgentCode(), true);
        validateAgentAccount(saveBO, agentAccount);
        ensureVipLevelExists(saveBO.getVipLevel());

        Integer originalLevel = exist.getVipLevel();
        Integer targetLevel = saveBO.getVipLevel();
        ensureNotDowngrade(originalLevel, targetLevel);
        ensureNotDowngrade(agentAccount.getLevel(), targetLevel);

        VipUser updateEntity = new VipUser();
        BeanUtils.copyProperties(exist, updateEntity);
        updateEntity.setId(id);
        updateEntity.setRemark(saveBO.getRemark());
        updateEntity.setVipLevel(targetLevel);
        updateEntity.setAgentName(agentAccount.getAgentName());
        updateEntity.setUpdateTime(new Date());
        updateEntity.setOperatorId(loginUser.getUserId());
        updateEntity.setOperatorName(getOperatorName(loginUser));

        updateById(updateEntity);

        if (!Objects.equals(originalLevel, targetLevel)) {
            handleLevelChange(updateEntity, agentAccount, originalLevel, targetLevel, loginUser, saveBO.getRemark());
        }

        recordOperation(loginUser, saveBO.getAgentCode(), "USER_EDIT", "updateVipUser", saveBO, "success", BaseConstant.ONE_INT);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteVipUserByIds(List<Long> ids, LoginUser loginUser) throws BizException {
        validateOperator(loginUser);
        if (CollectionUtils.isEmpty(ids)) {
            throw new BizException("请选择要删除的VIP用户");
        }
        List<VipUser> records = listByIds(ids);
        if (CollectionUtils.isEmpty(records)) {
            return;
        }
        removeByIds(ids);
        records.forEach(record -> recordOperation(loginUser, record.getAgentCode(), "USER_DELETE", "deleteVipUser", record.getId(), "success", BaseConstant.ONE_INT));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setVipLevel(VipUserSetLevelBO setLevelBO, LoginUser loginUser) throws BizException {
        validateOperator(loginUser);
        if (setLevelBO == null || setLevelBO.getId() == null) {
            throw new BizException("缺少VIP用户记录ID");
        }
        ensureVipLevelExists(setLevelBO.getVipLevel());

        VipUser vipUser = getById(setLevelBO.getId());
        if (vipUser == null) {
            throw new BizException("VIP用户记录不存在");
        }
        Integer originalLevel = vipUser.getVipLevel();
        Integer targetLevel = setLevelBO.getVipLevel();
        ensureNotDowngrade(originalLevel, targetLevel);

        AgentAccount agentAccount = agentAccountService.getAgentAccountByCode(vipUser.getAgentCode(), true);
        ensureNotDowngrade(agentAccount.getLevel(), targetLevel);

        vipUser.setVipLevel(targetLevel);
        vipUser.setRemark(setLevelBO.getRemark());
        vipUser.setUpdateTime(new Date());
        vipUser.setOperatorId(loginUser.getUserId());
        vipUser.setOperatorName(getOperatorName(loginUser));
        updateById(vipUser);

        handleLevelChange(vipUser, agentAccount, originalLevel, targetLevel, loginUser, setLevelBO.getRemark());

        recordOperation(loginUser, vipUser.getAgentCode(), "USER_SET_LEVEL", "setVipLevel", setLevelBO, "success", BaseConstant.ONE_INT);
    }

    @Override
    public List<VipUserVO> exportVipUsers(List<Long> ids) {
        List<VipUser> userList;
        if (CollectionUtils.isEmpty(ids)) {
            LambdaQueryWrapper<VipUser> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.orderByAsc(VipUser::getVipLevel);
            userList = list(queryWrapper);
        } else {
            userList = listByIds(ids);
        }
        return userList.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int supplementVipUsers(LoginUser loginUser) throws BizException {
        validateOperator(loginUser);

        List<VipUser> existingList = list();
        Set<Long> existingUserIds = existingList.stream()
                .map(VipUser::getUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(HashSet::new));
        Set<String> existingAgentCodes = existingList.stream()
                .map(VipUser::getAgentCode)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toCollection(HashSet::new));

        List<AgentAccount> agentAccounts = agentAccountService.list();
        if (CollectionUtils.isEmpty(agentAccounts)) {
            return 0;
        }

        List<VipUser> toInsert = new ArrayList<>();
        Date now = new Date();
        for (AgentAccount agentAccount : agentAccounts) {
            if (agentAccount == null) {
                continue;
            }
            Long sysUserId = agentAccount.getSysUserId();
            String agentCode = agentAccount.getAgentCode();
            if (sysUserId == null || StringUtils.isBlank(agentCode)) {
                continue;
            }
            if (existingUserIds.contains(sysUserId) || existingAgentCodes.contains(agentCode)) {
                continue;
            }

            VipUser vipUser = new VipUser();
            vipUser.setAgentAccountId(agentAccount.getAgentAccountId());
            vipUser.setUserId(sysUserId);
            vipUser.setAgentCode(agentCode);
            vipUser.setAgentName(agentAccount.getAgentName());

            Integer targetLevel = agentAccount.getLevel();
            if (targetLevel == null || targetLevel < BaseConstant.ZERO_INT
                    || vipConfigService.selectByVipLevel(targetLevel) == null) {
                targetLevel = BaseConstant.ZERO_INT;
            }
            vipUser.setVipLevel(targetLevel);
            vipUser.setRemark("系统补录");
            vipUser.setOperatorId(loginUser.getUserId());
            vipUser.setOperatorName(getOperatorName(loginUser));
            vipUser.setCreateTime(resolveTimestamp(agentAccount.getCreateTime(), now));
            vipUser.setUpdateTime(resolveTimestamp(agentAccount.getUpdateTime(), now));

            toInsert.add(vipUser);
            existingUserIds.add(sysUserId);
            existingAgentCodes.add(agentCode);
        }

        if (CollectionUtils.isEmpty(toInsert)) {
            return 0;
        }

        boolean saved = saveBatch(toInsert);
        if (!saved) {
            throw new BizException("补录VIP用户失败");
        }

        recordOperation(loginUser, null, "USER_SUPPLEMENT", "supplementVipUsers",
                toInsert.stream().map(VipUser::getAgentCode).collect(Collectors.toList()),
                "success", BaseConstant.ONE_INT);
        return toInsert.size();
    }

    private LambdaQueryWrapper<VipUser> buildQueryWrapper(VipUserQueryBO queryBO) {
        LambdaQueryWrapper<VipUser> queryWrapper = new LambdaQueryWrapper<>();
        if (queryBO == null) {
            return queryWrapper;
        }
        if (StringUtils.isNotBlank(queryBO.getAgentCode())) {
            queryWrapper.like(VipUser::getAgentCode, queryBO.getAgentCode());
        }
        if (StringUtils.isNotBlank(queryBO.getAgentName())) {
            queryWrapper.like(VipUser::getAgentName, queryBO.getAgentName());
        }
        if (queryBO.getVipLevel() != null) {
            queryWrapper.eq(VipUser::getVipLevel, queryBO.getVipLevel());
        }
        return queryWrapper;
    }

    private VipUserVO convertToVO(VipUser vipUser) {
        if (vipUser == null) {
            return null;
        }
        VipUserVO vo = new VipUserVO();
        BeanUtils.copyProperties(vipUser, vo);
        return vo;
    }

    private VipUserSummaryVO buildDefaultSummary() {
        VipUserSummaryVO summary = new VipUserSummaryVO();
        summary.setVipLevel(BaseConstant.ZERO_INT);
        summary.setHasVipRecord(false);
        populateLevelMeta(summary, summary.getVipLevel());
        return summary;
    }

    private void populateLevelMeta(VipUserSummaryVO summary, Integer vipLevel) {
        if (summary == null) {
            return;
        }
        int level = vipLevel == null ? BaseConstant.ZERO_INT : vipLevel;
        summary.setVipLevel(level);
        VipConfig config = vipConfigService.selectByVipLevel(level);
        if (config != null) {
            String levelName = StringUtils.isNotBlank(config.getLevelName())
                    ? config.getLevelName()
                    : defaultLevelName(level);
            summary.setVipLevelName(levelName);
            summary.setVipLevelIcon(config.getLevelIcon());
        } else {
            summary.setVipLevelName(defaultLevelName(level));
            summary.setVipLevelIcon(null);
        }
    }

    private String defaultLevelName(int level) {
        return "VIP" + level;
    }

    private void handleLevelChange(VipUser vipUser, AgentAccount agentAccount, Integer fromLevel, Integer toLevel,
                                   LoginUser loginUser, String remark) {
        if (vipUser == null || agentAccount == null) {
            return;
        }
        if (!Objects.equals(fromLevel, toLevel)) {
            // 更新代理商等级
            agentAccount.setLevel(toLevel);
            agentAccount.setUpdateTime(System.currentTimeMillis());
            agentAccountService.updateById(agentAccount);

            VipUpgradeLog upgradeLog = new VipUpgradeLog();
            upgradeLog.setUserId(vipUser != null ? vipUser.getUserId() : null);
            upgradeLog.setAgentCode(vipUser != null ? vipUser.getAgentCode() : agentAccount.getAgentCode());
            upgradeLog.setAgentName(agentAccount.getAgentName());
            upgradeLog.setFromLevel(fromLevel);
            upgradeLog.setToLevel(toLevel);
            upgradeLog.setUpgradeType("MANUAL");
            upgradeLog.setUpgradeReason(remark);
            upgradeLog.setOperatorId(loginUser != null ? loginUser.getUserId() : null);
            upgradeLog.setOperatorName(getOperatorName(loginUser));
            upgradeLog.setCreateTime(new Date());
            vipUpgradeLogService.recordUpgrade(upgradeLog);
        }
    }

    private void ensureVipLevelExists(Integer vipLevel) throws BizException {
        if (vipLevel == null) {
            throw new BizException("VIP等级不能为空");
        }
        if (vipConfigService.selectByVipLevel(vipLevel) == null) {
            throw new BizException("未找到对应的VIP等级配置");
        }
    }

    private void ensureNotDowngrade(Integer originalLevel, Integer targetLevel) throws BizException {
        if (originalLevel != null && targetLevel != null && targetLevel < originalLevel) {
            throw new BizException("VIP等级仅支持升级，不支持降低");
        }
    }

    private void validateVipUserSave(VipUserSaveBO saveBO, boolean isCreate) throws BizException {
        if (saveBO == null) {
            throw new BizException("VIP用户参数不能为空");
        }
        if (!isCreate && saveBO.getId() == null) {
            throw new BizException("VIP用户ID不能为空");
        }
        if (saveBO.getUserId() == null) {
            throw new BizException("用户ID不能为空");
        }
        if (StringUtils.isBlank(saveBO.getAgentCode())) {
            throw new BizException("代理商编码不能为空");
        }
        if (StringUtils.isBlank(saveBO.getAgentName())) {
            throw new BizException("代理商名称不能为空");
        }
        if (saveBO.getVipLevel() == null || saveBO.getVipLevel() < BaseConstant.ZERO_INT) {
            throw new BizException("VIP等级必须大于等于0");
        }
    }

    private void validateAgentAccount(VipUserSaveBO saveBO, AgentAccount agentAccount) throws BizException {
        if (agentAccount == null) {
            throw new BizException("代理商信息不存在，请确认代理商编码是否正确");
        }
        if (!Objects.equals(agentAccount.getSysUserId(), saveBO.getUserId())) {
            throw new BizException("用户ID与代理商信息不匹配");
        }
    }

    private boolean existsByUserId(Long userId) {
        if (userId == null) {
            return false;
        }
        LambdaQueryWrapper<VipUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(VipUser::getUserId, userId);
        return count(queryWrapper) > 0;
    }

    private boolean existsByAgentCode(String agentCode) {
        if (StringUtils.isBlank(agentCode)) {
            return false;
        }
        LambdaQueryWrapper<VipUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(VipUser::getAgentCode, agentCode);
        return count(queryWrapper) > 0;
    }

    private void validateOperator(LoginUser loginUser) throws BizException {
        if (loginUser == null || loginUser.getUserId() == null) {
            throw new BizException("未获取到操作人信息");
        }
    }

    private String getOperatorName(LoginUser loginUser) {
        if (loginUser == null) {
            return null;
        }
        if (loginUser.getUser() != null && StringUtils.isNotBlank(loginUser.getUser().getNickName())) {
            return loginUser.getUser().getNickName();
        }
        return loginUser.getUsername();
    }

    private void recordOperation(LoginUser loginUser, String agentCode, String operationType, String methodName,
                                 Object params, String result, int status) {
        try {
            VipOperationLog logEntity = new VipOperationLog();
            logEntity.setUserId(loginUser != null ? loginUser.getUserId() : null);
            logEntity.setTargetAgentCode(agentCode);
            logEntity.setOperationType(operationType);
            logEntity.setMethodName(TAG + "#" + methodName);
            logEntity.setRequestParams(JSONUtil.toJsonStr(params));
            logEntity.setResult(result);
            logEntity.setStatus(status);
            logEntity.setOperationTime(System.currentTimeMillis());
            logEntity.setExecutionTime(BaseConstant.ZERO_INT);
            vipOperationLogService.recordOperation(logEntity);
        } catch (Exception e) {
            log.warn("recordOperation error", e);
        }
    }

    private Date resolveTimestamp(Long timestamp, Date fallback) {
        Date base = fallback != null ? new Date(fallback.getTime()) : new Date();
        if (timestamp == null || timestamp <= 0) {
            return base;
        }
        if (timestamp >= 1000000000000L) {
            return new Date(timestamp);
        }
        return new Date(timestamp * 1000);
    }
}
