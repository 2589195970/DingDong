package com.ruoyi.console.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.PageResultFactory;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.common.vip.bo.VipUpgradeLogQueryBO;
import com.ruoyi.common.vip.entity.VipUpgradeLog;
import com.ruoyi.common.vip.vo.VipUpgradeLogVO;
import com.ruoyi.console.mapper.VipUpgradeLogMapper;
import com.ruoyi.console.service.VipUpgradeLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * VIP升级日志服务实现
 *
 * @author Codex
 */
@Service
@Slf4j
public class VipUpgradeLogServiceImpl extends ServiceImpl<VipUpgradeLogMapper, VipUpgradeLog> implements VipUpgradeLogService {

    private static final String TAG = "VipUpgradeLogService";

    @Override
    public void recordUpgrade(VipUpgradeLog upgradeLog) {
        if (upgradeLog == null) {
            return;
        }
        if (upgradeLog.getCreateTime() == null) {
            upgradeLog.setCreateTime(new Date());
        }
        try {
            save(upgradeLog);
        } catch (Exception e) {
            log.error("{}#recordUpgrade persist upgrade log failed: {}", TAG, upgradeLog, e);
        }
    }

    @Override
    public List<VipUpgradeLog> selectRecentUpgrades(String agentCode, int limit) {
        LambdaQueryWrapper<VipUpgradeLog> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(agentCode)) {
            queryWrapper.eq(VipUpgradeLog::getAgentCode, agentCode);
        }
        queryWrapper.orderByDesc(VipUpgradeLog::getCreateTime);
        if (limit > 0) {
            queryWrapper.last("limit " + Math.min(limit, 200));
        }
        return list(queryWrapper);
    }

    @Override
    public VipUpgradeLog selectLatestUpgrade(Long userId) {
        if (userId == null) {
            return null;
        }
        LambdaQueryWrapper<VipUpgradeLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(VipUpgradeLog::getUserId, userId)
                .orderByDesc(VipUpgradeLog::getCreateTime)
                .last("limit 1");
        return getOne(queryWrapper, false);
    }

    @Override
    public PageResult<VipUpgradeLogVO> selectVipUpgradeLogPage(VipUpgradeLogQueryBO queryBO) {
        if (queryBO == null) {
            queryBO = new VipUpgradeLogQueryBO();
        }
        int pageNo = queryBO.getPageNo() == null || queryBO.getPageNo() <= 0 ? 1 : queryBO.getPageNo();
        int pageSize = queryBO.getPageSize() == null || queryBO.getPageSize() <= 0 ? 10 : queryBO.getPageSize();

        LambdaQueryWrapper<VipUpgradeLog> queryWrapper = buildQueryWrapper(queryBO);
        queryWrapper.orderByDesc(VipUpgradeLog::getCreateTime);

        Page<VipUpgradeLog> page = page(new Page<>(pageNo, pageSize), queryWrapper);
        List<VipUpgradeLogVO> voList = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        Page<VipUpgradeLogVO> voPage = new Page<>(pageNo, pageSize);
        voPage.setTotal(page.getTotal());
        voPage.setCurrent(pageNo);
        voPage.setSize(pageSize);
        voPage.setRecords(voList);
        return PageResultFactory.createPageResult(voPage);
    }

    @Override
    public List<VipUpgradeLogVO> selectVipUpgradeLogList(VipUpgradeLogQueryBO queryBO) {
        LambdaQueryWrapper<VipUpgradeLog> queryWrapper = buildQueryWrapper(queryBO);
        queryWrapper.orderByDesc(VipUpgradeLog::getCreateTime);
        return list(queryWrapper).stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VipUpgradeLogVO> selectVipUpgradeLogListByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return listByIds(ids).stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    private LambdaQueryWrapper<VipUpgradeLog> buildQueryWrapper(VipUpgradeLogQueryBO queryBO) {
        LambdaQueryWrapper<VipUpgradeLog> queryWrapper = new LambdaQueryWrapper<>();
        if (queryBO == null) {
            return queryWrapper;
        }
        if (queryBO.getUserId() != null) {
            queryWrapper.eq(VipUpgradeLog::getUserId, queryBO.getUserId());
        }
        if (StringUtils.isNotEmpty(queryBO.getAgentCode())) {
            queryWrapper.eq(VipUpgradeLog::getAgentCode, queryBO.getAgentCode());
        }
        if (StringUtils.isNotEmpty(queryBO.getAgentName())) {
            queryWrapper.like(VipUpgradeLog::getAgentName, queryBO.getAgentName());
        }
        if (queryBO.getFromLevel() != null) {
            queryWrapper.eq(VipUpgradeLog::getFromLevel, queryBO.getFromLevel());
        }
        if (queryBO.getToLevel() != null) {
            queryWrapper.eq(VipUpgradeLog::getToLevel, queryBO.getToLevel());
        }
        if (queryBO.getVipLevel() != null) {
            queryWrapper.eq(VipUpgradeLog::getToLevel, queryBO.getVipLevel());
        }
        if (StringUtils.isNotEmpty(queryBO.getUpgradeType())) {
            queryWrapper.eq(VipUpgradeLog::getUpgradeType, queryBO.getUpgradeType());
        }
        if (queryBO.getBeginTime() != null) {
            queryWrapper.ge(VipUpgradeLog::getCreateTime, queryBO.getBeginTime());
        }
        if (queryBO.getEndTime() != null) {
            queryWrapper.le(VipUpgradeLog::getCreateTime, queryBO.getEndTime());
        }
        return queryWrapper;
    }

    private VipUpgradeLogVO convertToVO(VipUpgradeLog upgradeLog) {
        if (Objects.isNull(upgradeLog)) {
            return null;
        }
        VipUpgradeLogVO vo = new VipUpgradeLogVO();
        BeanUtils.copyProperties(upgradeLog, vo);
        return vo;
    }
}
