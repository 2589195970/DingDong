package com.ruoyi.console.service;

import com.ruoyi.common.order.bo.PageViewBO;
import com.ruoyi.common.order.entity.PageViewLog;
import com.ruoyi.common.order.entity.PageViewStatistics;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 页面访问统计服务接口
 *
 * @Description 页面访问统计相关的业务逻辑接口
 * @Author Claude
 * @Date 2025-01-26
 */
public interface PageViewService {

    // ==================== 核心业务方法 ====================

    /**
     * 记录页面访问
     * @param pageViewBO 访问参数对象
     * @param ipAddress 访问者IP地址
     * @return 记录是否成功
     */
    boolean recordPageView(PageViewBO pageViewBO, String ipAddress);

    /**
     * 批量记录页面访问
     * @param pageViewBOList 访问参数列表
     * @param ipAddressList 对应的IP地址列表
     * @return 成功记录的数量
     */
    int recordPageViewBatch(List<PageViewBO> pageViewBOList, List<String> ipAddressList);

    /**
     * 获取页面访问统计数据
     * @param agentCode 代理商编码
     * @param dateRange 日期范围（可选：today, week, month 或具体日期）
     * @return 统计数据Map
     */
    Map<String, Object> getPageViewStats(String agentCode, String dateRange);

    /**
     * 获取页面访问趋势
     * @param agentCode 代理商编码
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 趋势数据列表
     */
    List<PageViewStatistics> getPageViewTrend(String agentCode, String startDate, String endDate);

    /**
     * 获取实时访问统计
     * @param agentCode 代理商编码
     * @return 实时统计数据
     */
    Map<String, Object> getRealTimeStats(String agentCode);

    // ==================== 查询相关方法 ====================

    /**
     * 获取指定日期的访问统计
     * @param agentCode 代理商编码
     * @param viewDate 查询日期（yyyy-MM-dd格式）
     * @return 统计数据，如果没有记录返回null
     */
    PageViewStatistics getStatisticsByDate(String agentCode, String viewDate);

    /**
     * 获取访问明细记录
     * @param agentCode 代理商编码
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 访问明细列表
     */
    List<PageViewLog> getViewLogs(String agentCode, Date startTime, Date endTime, int pageNum, int pageSize);

    /**
     * 统计访问记录总数
     * @param agentCode 代理商编码
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 总记录数
     */
    int countViewLogs(String agentCode, Date startTime, Date endTime);

    /**
     * 获取最近的访问记录
     * @param agentCode 代理商编码
     * @param limit 限制数量
     * @return 最近访问记录列表
     */
    List<PageViewLog> getRecentViewLogs(String agentCode, int limit);

    // ==================== 高级统计方法 ====================

    /**
     * 获取访问统计摘要
     * @param agentCode 代理商编码
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计摘要信息
     */
    Map<String, Object> getStatisticsSummary(String agentCode, String startDate, String endDate);

    /**
     * 获取设备类型分布统计
     * @param agentCode 代理商编码
     * @param viewDate 查询日期（yyyy-MM-dd格式）
     * @return 设备类型统计列表
     */
    List<Map<String, Object>> getDeviceTypeStats(String agentCode, String viewDate);

    /**
     * 获取访问来源统计
     * @param agentCode 代理商编码
     * @param viewDate 查询日期（yyyy-MM-dd格式）
     * @return 来源统计列表
     */
    List<Map<String, Object>> getRefererStats(String agentCode, String viewDate);

    /**
     * 获取小时访问分布
     * @param agentCode 代理商编码
     * @param viewDate 查询日期（yyyy-MM-dd格式）
     * @return 小时分布统计
     */
    List<Map<String, Object>> getHourlyStats(String agentCode, String viewDate);

    /**
     * 获取IP访问排行
     * @param agentCode 代理商编码
     * @param viewDate 查询日期（yyyy-MM-dd格式）
     * @param topN 返回前N名
     * @return IP访问排行列表
     */
    List<Map<String, Object>> getTopIPStats(String agentCode, String viewDate, int topN);

    /**
     * 获取访问最多的代理商排行
     * @param viewDate 查询日期（yyyy-MM-dd格式）
     * @param topN 返回前N名
     * @return 代理商访问排行
     */
    List<Map<String, Object>> getTopAgentsByViews(String viewDate, int topN);

    // ==================== 数据分析方法 ====================

    /**
     * 分析访问模式
     * @param agentCode 代理商编码
     * @param analysisPeriod 分析周期（day, week, month）
     * @return 访问模式分析结果
     */
    Map<String, Object> analyzeAccessPattern(String agentCode, String analysisPeriod);

    /**
     * 生成访问报告
     * @param agentCode 代理商编码
     * @param reportType 报告类型（daily, weekly, monthly）
     * @param reportDate 报告日期
     * @return 访问报告数据
     */
    Map<String, Object> generateAccessReport(String agentCode, String reportType, String reportDate);

    /**
     * 比较两个时间段的访问数据
     * @param agentCode 代理商编码
     * @param period1Start 第一个周期开始日期
     * @param period1End 第一个周期结束日期
     * @param period2Start 第二个周期开始日期
     * @param period2End 第二个周期结束日期
     * @return 对比分析结果
     */
    Map<String, Object> compareAccessData(String agentCode, String period1Start, String period1End,
                                          String period2Start, String period2End);

    // ==================== 数据管理方法 ====================

    /**
     * 检查代理商是否有访问记录
     * @param agentCode 代理商编码
     * @return true 如果有访问记录
     */
    boolean hasAccessRecords(String agentCode);

    /**
     * 获取代理商首次访问时间
     * @param agentCode 代理商编码
     * @return 首次访问时间，如果没有记录返回null
     */
    Date getFirstAccessTime(String agentCode);

    /**
     * 获取代理商最后访问时间
     * @param agentCode 代理商编码
     * @return 最后访问时间，如果没有记录返回null
     */
    Date getLastAccessTime(String agentCode);

    /**
     * 同步统计数据（手动触发统计数据更新）
     * @param agentCode 代理商编码
     * @param viewDate 同步日期（yyyy-MM-dd格式）
     * @return 同步是否成功
     */
    boolean syncStatisticsData(String agentCode, String viewDate);

    /**
     * 批量同步统计数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 同步的记录数
     */
    int batchSyncStatisticsData(String startDate, String endDate);

    // ==================== 数据清理方法 ====================

    /**
     * 清理过期的访问日志
     * @param retentionDays 保留天数
     * @return 清理的记录数
     */
    int cleanExpiredViewLogs(int retentionDays);

    /**
     * 清理过期的统计数据
     * @param retentionDays 保留天数
     * @return 清理的记录数
     */
    int cleanExpiredStatistics(int retentionDays);

    /**
     * 数据完整性检查
     * @param agentCode 代理商编码
     * @param viewDate 检查日期（yyyy-MM-dd格式）
     * @return 检查结果信息
     */
    Map<String, Object> checkDataConsistency(String agentCode, String viewDate);

    /**
     * 修复统计数据
     * @param agentCode 代理商编码
     * @param viewDate 修复日期（yyyy-MM-dd格式）
     * @return 修复是否成功
     */
    boolean repairStatisticsData(String agentCode, String viewDate);

    // ==================== 配置和工具方法 ====================

    /**
     * 获取统计配置信息
     * @return 配置信息Map
     */
    Map<String, Object> getStatisticsConfig();

    /**
     * 更新统计配置
     * @param configMap 配置参数
     * @return 更新是否成功
     */
    boolean updateStatisticsConfig(Map<String, Object> configMap);

    /**
     * 导出访问数据
     * @param agentCode 代理商编码
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param exportType 导出类型（csv, excel, json）
     * @return 导出文件路径或数据
     */
    Object exportAccessData(String agentCode, String startDate, String endDate, String exportType);

    /**
     * 获取系统访问概况
     * @param viewDate 查询日期（yyyy-MM-dd格式）
     * @return 系统访问概况
     */
    Map<String, Object> getSystemOverview(String viewDate);

    /**
     * 获取活跃代理商统计
     * @param viewDate 查询日期（yyyy-MM-dd格式）
     * @return 活跃代理商统计信息
     */
    Map<String, Object> getActiveAgentsStats(String viewDate);

    // ==================== 预警和监控方法 ====================

    /**
     * 检查异常访问模式
     * @param agentCode 代理商编码
     * @param checkType 检查类型（frequency, ip, device）
     * @return 异常检查结果
     */
    Map<String, Object> checkAbnormalAccess(String agentCode, String checkType);

    /**
     * 生成访问预警
     * @param alertType 预警类型（high_traffic, suspicious_ip, unusual_pattern）
     * @return 预警信息列表
     */
    List<Map<String, Object>> generateAccessAlerts(String alertType);

    /**
     * 获取访问健康度评分
     * @param agentCode 代理商编码
     * @param evaluationPeriod 评估周期（day, week, month）
     * @return 健康度评分和详细信息
     */
    Map<String, Object> getAccessHealthScore(String agentCode, String evaluationPeriod);
}