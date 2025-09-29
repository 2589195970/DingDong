package com.ruoyi.order.mapper;

import com.ruoyi.common.order.entity.PageViewLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 页面访问数据收集Mapper接口
 *
 * @Description 用于页面访问数据的数据库操作
 * @Author Claude
 * @Date 2025-01-29
 */
@Mapper
public interface PageViewCollectMapper {

    /**
     * 插入访问日志
     * @param pageViewLog 访问日志对象
     * @return 插入结果：成功返回1，失败返回0
     */
    int insertViewLog(PageViewLog pageViewLog);

    /**
     * 获取代理商今日访问基础统计
     * @param agentCode 代理商编码
     * @return 今日基础统计数据
     */
    Map<String, Object> selectTodayBasicStats(@Param("agentCode") String agentCode);

    /**
     * 检查代理商是否有访问记录
     * @param agentCode 代理商编码
     * @return true表示有记录，false表示无记录
     */
    boolean hasViewRecords(@Param("agentCode") String agentCode);
}