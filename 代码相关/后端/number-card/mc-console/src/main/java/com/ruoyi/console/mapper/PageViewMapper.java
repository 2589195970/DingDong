package com.ruoyi.console.mapper;

import com.ruoyi.common.order.entity.PageViewLog;
import com.ruoyi.common.order.entity.PageViewStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 页面访问统计Mapper接口
 *
 * @Description 页面访问统计相关的数据库操作接口
 * @Author Claude
 * @Date 2025-01-26
 */
@Mapper
public interface PageViewMapper {

    // ==================== 访问日志相关操作 ====================

    /**
     * 插入访问日志
     * @param log 访问日志对象
     * @return 影响行数
     */
    int insertViewLog(PageViewLog log);

    /**
     * 批量插入访问日志
     * @param logs 访问日志列表
     * @return 影响行数
     */
    int insertViewLogBatch(@Param("logs") List<PageViewLog> logs);

    /**
     * 根据条件查询访问日志
     * @param agentCode 代理商编码
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 访问日志列表
     */
    List<PageViewLog> selectViewLogs(@Param("agentCode") String agentCode,
                                     @Param("startTime") Date startTime,
                                     @Param("endTime") Date endTime,
                                     @Param("offset") Integer offset,
                                     @Param("limit") Integer limit);

    /**
     * 统计访问日志数量
     * @param agentCode 代理商编码
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 总数量
     */
    int countViewLogs(@Param("agentCode") String agentCode,
                      @Param("startTime") Date startTime,
                      @Param("endTime") Date endTime);

    /**
     * 根据代理商和日期查询当日访问明细
     * @param agentCode 代理商编码
     * @param viewDate 访问日期（yyyy-MM-dd格式）
     * @return 访问日志列表
     */
    List<PageViewLog> selectTodayViewLogs(@Param("agentCode") String agentCode,
                                          @Param("viewDate") String viewDate);

    /**
     * 删除过期的访问日志（数据清理）
     * @param beforeDate 指定日期之前的数据将被删除
     * @return 删除的记录数
     */
    int deleteExpiredViewLogs(@Param("beforeDate") Date beforeDate);

    // ==================== 访问统计相关操作 ====================

    /**
     * 插入或更新访问统计（使用ON DUPLICATE KEY UPDATE）
     * @param agentCode 代理商编码
     * @param viewDate 访问日期（yyyy-MM-dd格式）
     * @return 影响行数
     */
    int insertOrUpdateStatistics(@Param("agentCode") String agentCode,
                                 @Param("viewDate") String viewDate);

    /**
     * 直接插入访问统计记录
     * @param statistics 统计对象
     * @return 影响行数
     */
    int insertStatistics(PageViewStatistics statistics);

    /**
     * 更新访问统计记录
     * @param statistics 统计对象
     * @return 影响行数
     */
    int updateStatistics(PageViewStatistics statistics);

    /**
     * 根据代理商和日期查询统计数据
     * @param agentCode 代理商编码
     * @param viewDate 访问日期（yyyy-MM-dd格式）
     * @return 统计数据
     */
    PageViewStatistics selectStatistics(@Param("agentCode") String agentCode,
                                        @Param("viewDate") String viewDate);

    /**
     * 查询代理商的访问趋势（指定日期范围）
     * @param agentCode 代理商编码
     * @param startDate 开始日期（yyyy-MM-dd格式）
     * @param endDate 结束日期（yyyy-MM-dd格式）
     * @return 统计数据列表，按日期倒序
     */
    List<PageViewStatistics> selectViewTrend(@Param("agentCode") String agentCode,
                                             @Param("startDate") String startDate,
                                             @Param("endDate") String endDate);

    /**
     * 查询所有代理商的访问统计（指定日期）
     * @param viewDate 访问日期（yyyy-MM-dd格式）
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 统计数据列表
     */
    List<PageViewStatistics> selectAllAgentStatistics(@Param("viewDate") String viewDate,
                                                       @Param("offset") Integer offset,
                                                       @Param("limit") Integer limit);

    /**
     * 统计指定日期有访问记录的代理商数量
     * @param viewDate 访问日期（yyyy-MM-dd格式）
     * @return 代理商数量
     */
    int countActiveAgents(@Param("viewDate") String viewDate);

    /**
     * 删除过期的统计数据（数据清理）
     * @param beforeDate 指定日期之前的数据将被删除
     * @return 删除的记录数
     */
    int deleteExpiredStatistics(@Param("beforeDate") Date beforeDate);

    // ==================== 高级统计查询 ====================

    /**
     * 获取代理商访问统计摘要
     * @param agentCode 代理商编码
     * @param startDate 开始日期（yyyy-MM-dd格式）
     * @param endDate 结束日期（yyyy-MM-dd格式）
     * @return 统计摘要信息
     */
    Map<String, Object> selectStatisticsSummary(@Param("agentCode") String agentCode,
                                                @Param("startDate") String startDate,
                                                @Param("endDate") String endDate);

    /**
     * 获取访问最多的代理商排行榜
     * @param viewDate 访问日期（yyyy-MM-dd格式）
     * @param limit 限制数量
     * @return 代理商访问排行
     */
    List<Map<String, Object>> selectTopAgentsByViews(@Param("viewDate") String viewDate,
                                                     @Param("limit") Integer limit);

    /**
     * 获取访问来源统计
     * @param agentCode 代理商编码
     * @param viewDate 访问日期（yyyy-MM-dd格式）
     * @return 来源统计列表
     */
    List<Map<String, Object>> selectRefererStatistics(@Param("agentCode") String agentCode,
                                                       @Param("viewDate") String viewDate);

    /**
     * 获取设备类型统计
     * @param agentCode 代理商编码
     * @param viewDate 访问日期（yyyy-MM-dd格式）
     * @return 设备类型统计列表
     */
    List<Map<String, Object>> selectDeviceTypeStatistics(@Param("agentCode") String agentCode,
                                                          @Param("viewDate") String viewDate);

    /**
     * 获取小时访问分布统计
     * @param agentCode 代理商编码
     * @param viewDate 访问日期（yyyy-MM-dd格式）
     * @return 小时访问分布
     */
    List<Map<String, Object>> selectHourlyStatistics(@Param("agentCode") String agentCode,
                                                      @Param("viewDate") String viewDate);

    /**
     * 获取IP访问排行
     * @param agentCode 代理商编码
     * @param viewDate 访问日期（yyyy-MM-dd格式）
     * @param limit 限制数量
     * @return IP访问排行
     */
    List<Map<String, Object>> selectTopIPsByViews(@Param("agentCode") String agentCode,
                                                  @Param("viewDate") String viewDate,
                                                  @Param("limit") Integer limit);

    // ==================== 实时统计查询 ====================

    /**
     * 获取代理商今日实时统计
     * @param agentCode 代理商编码
     * @return 实时统计数据
     */
    Map<String, Object> selectTodayRealTimeStats(@Param("agentCode") String agentCode);

    /**
     * 获取代理商最近访问记录
     * @param agentCode 代理商编码
     * @param limit 限制数量
     * @return 最近访问记录
     */
    List<PageViewLog> selectRecentViewLogs(@Param("agentCode") String agentCode,
                                           @Param("limit") Integer limit);

    /**
     * 检查代理商是否有访问记录
     * @param agentCode 代理商编码
     * @return true 如果有访问记录
     */
    boolean hasViewRecords(@Param("agentCode") String agentCode);

    /**
     * 获取代理商首次访问时间
     * @param agentCode 代理商编码
     * @return 首次访问时间
     */
    Date selectFirstVisitTime(@Param("agentCode") String agentCode);

    /**
     * 获取代理商最后访问时间
     * @param agentCode 代理商编码
     * @return 最后访问时间
     */
    Date selectLastVisitTime(@Param("agentCode") String agentCode);

    // ==================== 数据完整性检查 ====================

    /**
     * 检查统计数据完整性（某日期的日志记录数与统计表的访问次数是否一致）
     * @param agentCode 代理商编码
     * @param viewDate 访问日期（yyyy-MM-dd格式）
     * @return 检查结果Map，包含logCount、statsCount、isConsistent
     */
    Map<String, Object> checkDataConsistency(@Param("agentCode") String agentCode,
                                             @Param("viewDate") String viewDate);

    /**
     * 修复统计数据（重新计算指定日期的统计数据）
     * @param agentCode 代理商编码
     * @param viewDate 访问日期（yyyy-MM-dd格式）
     * @return 影响行数
     */
    int repairStatisticsData(@Param("agentCode") String agentCode,
                             @Param("viewDate") String viewDate);
}