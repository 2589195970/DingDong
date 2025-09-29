package com.ruoyi.order.service.impl;

import com.ruoyi.common.order.bo.PageViewBO;
import com.ruoyi.common.order.entity.PageViewLog;
import com.ruoyi.order.mapper.PageViewCollectMapper;
import com.ruoyi.order.service.PageViewCollectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 页面访问数据收集服务实现类
 *
 * @Description 实现页面访问数据的收集、处理和基础统计功能
 * @Author Claude
 * @Date 2025-01-29
 */
@Service
@Slf4j
public class PageViewCollectServiceImpl implements PageViewCollectService {

    private static final String TAG = "PageViewCollectServiceImpl";

    @Resource
    private PageViewCollectMapper pageViewCollectMapper;

    /**
     * 收集页面访问数据
     * @param pageViewBO 页面访问数据对象
     * @param ipAddress 访问者IP地址
     * @return 收集是否成功
     */
    @Override
    public boolean collectPageView(PageViewBO pageViewBO, String ipAddress) {
        try {
            // 数据验证
            if (pageViewBO == null || !pageViewBO.isValid()) {
                log.warn("{} 收集页面访问数据失败：数据无效", TAG);
                return false;
            }

            // 数据标准化
            pageViewBO.normalize();

            // agentCode为空直接忽略
            if (!pageViewBO.hasAgentCode()) {
                log.debug("{} agentCode为空，忽略页面访问记录", TAG);
                return true;
            }

            // 转换为数据库实体
            PageViewLog pageViewLog = convertToPageViewLog(pageViewBO, ipAddress);

            // 保存到数据库
            int result = pageViewCollectMapper.insertViewLog(pageViewLog);

            if (result > 0) {
                log.info("{} 成功记录页面访问：agentCode={}, visitorId={}, deviceType={}",
                    TAG, pageViewLog.getAgentCode(), pageViewLog.getVisitorId(), pageViewLog.getDeviceType());
                return true;
            } else {
                log.warn("{} 页面访问记录保存失败：数据库插入返回0", TAG);
                return false;
            }

        } catch (Exception e) {
            log.error("{} 收集页面访问数据异常：{}", TAG, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 获取基础访问统计数据
     * @param agentCode 代理商编码
     * @return 基础统计数据
     */
    @Override
    public Map<String, Object> getBasicStats(String agentCode) {
        try {
            // 获取今日基础统计
            Map<String, Object> stats = pageViewCollectMapper.selectTodayBasicStats(agentCode);

            if (stats == null) {
                // 如果没有数据，返回默认统计
                stats = createDefaultStats(agentCode);
            }

            // 检查是否有历史记录
            boolean hasRecords = pageViewCollectMapper.hasViewRecords(agentCode);
            stats.put("hasHistoryRecords", hasRecords);

            log.debug("{} 获取基础统计成功：agentCode={}, stats={}", TAG, agentCode, stats);
            return stats;

        } catch (Exception e) {
            log.error("{} 获取基础统计异常：agentCode={}, error={}", TAG, agentCode, e.getMessage(), e);

            // 异常情况返回默认统计
            return createDefaultStats(agentCode);
        }
    }

    /**
     * 将PageViewBO转换为PageViewLog实体
     * @param pageViewBO 前端传来的访问参数
     * @param ipAddress IP地址
     * @return PageViewLog实体对象
     */
    private PageViewLog convertToPageViewLog(PageViewBO pageViewBO, String ipAddress) {
        PageViewLog pageViewLog = new PageViewLog();

        // 设置基本信息
        pageViewLog.setBasicInfo(
            pageViewBO.getSafeAgentCode(),
            pageViewBO.getSafeVisitorId(),
            ipAddress
        );

        // 设置来源信息和设备类型检测
        pageViewLog.setSourceInfo(
            pageViewBO.getSafeUserAgent(),
            pageViewBO.getSafeReferer()
        );

        // 设置访问时间
        pageViewLog.setVisitTimeNow();

        return pageViewLog;
    }

    /**
     * 创建默认统计数据
     * @param agentCode 代理商编码
     * @return 默认统计数据
     */
    private Map<String, Object> createDefaultStats(String agentCode) {
        Map<String, Object> defaultStats = new HashMap<>();
        defaultStats.put("todayViews", 0);
        defaultStats.put("todayVisitors", 0);
        defaultStats.put("todayIps", 0);
        defaultStats.put("mobileViews", 0);
        defaultStats.put("pcViews", 0);
        defaultStats.put("lastVisitTime", null);
        defaultStats.put("hasHistoryRecords", false);

        log.debug("{} 返回默认统计数据：agentCode={}", TAG, agentCode);
        return defaultStats;
    }
}