package com.ruoyi.web.controller.console;

import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.order.entity.PageViewLog;
import com.ruoyi.common.order.entity.PageViewStatistics;
import com.ruoyi.console.service.PageViewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 页面访问统计控制器
 *
 * @Description 提供后台管理系统的页面访问统计功能
 * @Author Claude
 * @Date 2025-01-26
 */
@RestController
@RequestMapping("/console/pageViewStatistics")
@Slf4j
@Api(tags = "页面访问统计管理")
public class PageViewStatisticsController {

    public static final String TAG = "PageViewStatisticsController";

    @Resource
    private PageViewService pageViewService;

    // ==================== 统计概览 ====================

    /**
     * 获取页面访问统计概览
     * @param agentCode 代理商编码
     * @param dateRange 日期范围
     * @return 统计概览数据
     */
    @GetMapping("/overview")
    @ApiOperation("获取页面访问统计概览")
    public ResponseEntity<Map<String, Object>> getOverview(
            @RequestParam(required = false) String agentCode,
            @RequestParam(defaultValue = "today") String dateRange) {
        try {
            Map<String, Object> stats = pageViewService.getPageViewStats(agentCode, dateRange);
            return ResponseEntity.success(stats);
        } catch (Exception e) {
            log.error("{} getOverview方法异常:{}", TAG, e.getMessage(), e);
            return ResponseEntity.error("获取统计概览失败，请稍候重试", null);
        }
    }

    /**
     * 获取访问趋势数据
     * @param agentCode 代理商编码
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 趋势数据
     */
    @GetMapping("/trend")
    @ApiOperation("获取页面访问趋势")
    public ResponseEntity<List<PageViewStatistics>> getTrend(
            @RequestParam(required = false) String agentCode,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            List<PageViewStatistics> trend = pageViewService.getPageViewTrend(agentCode, startDate, endDate);
            return ResponseEntity.success(trend);
        } catch (Exception e) {
            log.error("{} getTrend方法异常:{}", TAG, e.getMessage(), e);
            return ResponseEntity.error("获取访问趋势失败，请稍候重试", null);
        }
    }

    /**
     * 获取实时统计数据
     * @param agentCode 代理商编码
     * @return 实时统计数据
     */
    @GetMapping("/realtime")
    @ApiOperation("获取实时访问统计")
    public ResponseEntity<Map<String, Object>> getRealTimeStats(
            @RequestParam(required = false) String agentCode) {
        try {
            Map<String, Object> stats = pageViewService.getRealTimeStats(agentCode);
            return ResponseEntity.success(stats);
        } catch (Exception e) {
            log.error("{} getRealTimeStats方法异常:{}", TAG, e.getMessage(), e);
            return ResponseEntity.error("获取实时统计失败，请稍候重试", null);
        }
    }

    // ==================== 详细分析 ====================

    /**
     * 获取设备类型统计
     * @param agentCode 代理商编码
     * @param viewDate 查询日期
     * @return 设备类型统计
     */
    @GetMapping("/deviceStats")
    @ApiOperation("获取设备类型分布统计")
    public ResponseEntity<List<Map<String, Object>>> getDeviceStats(
            @RequestParam(required = false) String agentCode,
            @RequestParam String viewDate) {
        try {
            List<Map<String, Object>> stats = pageViewService.getDeviceTypeStats(agentCode, viewDate);
            return ResponseEntity.success(stats);
        } catch (Exception e) {
            log.error("{} getDeviceStats方法异常:{}", TAG, e.getMessage(), e);
            return ResponseEntity.error("获取设备统计失败，请稍候重试", null);
        }
    }

    /**
     * 获取访问来源统计
     * @param agentCode 代理商编码
     * @param viewDate 查询日期
     * @return 来源统计
     */
    @GetMapping("/refererStats")
    @ApiOperation("获取访问来源统计")
    public ResponseEntity<List<Map<String, Object>>> getRefererStats(
            @RequestParam(required = false) String agentCode,
            @RequestParam String viewDate) {
        try {
            List<Map<String, Object>> stats = pageViewService.getRefererStats(agentCode, viewDate);
            return ResponseEntity.success(stats);
        } catch (Exception e) {
            log.error("{} getRefererStats方法异常:{}", TAG, e.getMessage(), e);
            return ResponseEntity.error("获取来源统计失败，请稍候重试", null);
        }
    }

    /**
     * 获取访问明细记录
     * @param agentCode 代理商编码
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 访问明细列表
     */
    @GetMapping("/logs")
    @ApiOperation("获取访问明细记录")
    public ResponseEntity<List<PageViewLog>> getViewLogs(
            @RequestParam(required = false) String agentCode,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {
        try {
            Date startTime = null, endTime = null;
            if (startDate != null && !startDate.isEmpty()) {
                startTime = java.sql.Date.valueOf(startDate);
            }
            if (endDate != null && !endDate.isEmpty()) {
                endTime = java.sql.Date.valueOf(endDate);
            }

            List<PageViewLog> logs = pageViewService.getViewLogs(agentCode, startTime, endTime, pageNum, pageSize);
            return ResponseEntity.success(logs);
        } catch (Exception e) {
            log.error("{} getViewLogs方法异常:{}", TAG, e.getMessage(), e);
            return ResponseEntity.error("获取访问明细失败，请稍候重试", null);
        }
    }

    /**
     * 获取代理商访问排行
     * @param viewDate 查询日期
     * @param topN 返回前N名
     * @return 代理商访问排行
     */
    @GetMapping("/ranking")
    @ApiOperation("获取代理商访问排行")
    public ResponseEntity<List<Map<String, Object>>> getAgentRanking(
            @RequestParam String viewDate,
            @RequestParam(defaultValue = "20") int topN) {
        try {
            List<Map<String, Object>> ranking = pageViewService.getTopAgentsByViews(viewDate, topN);
            return ResponseEntity.success(ranking);
        } catch (Exception e) {
            log.error("{} getAgentRanking方法异常:{}", TAG, e.getMessage(), e);
            return ResponseEntity.error("获取代理商排行失败，请稍候重试", null);
        }
    }
}