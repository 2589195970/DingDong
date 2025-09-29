package com.ruoyi.order.service;

import com.ruoyi.common.order.bo.PageViewBO;

import java.util.Map;

/**
 * 页面访问数据收集服务接口
 *
 * @Description 用于收集和处理前端传来的页面访问数据
 * @Author Claude
 * @Date 2025-01-29
 */
public interface PageViewCollectService {

    /**
     * 收集页面访问数据
     * @param pageViewBO 页面访问数据对象
     * @param ipAddress 访问者IP地址
     * @return 收集是否成功
     */
    boolean collectPageView(PageViewBO pageViewBO, String ipAddress);

    /**
     * 获取基础访问统计数据
     * @param agentCode 代理商编码
     * @return 基础统计数据
     */
    Map<String, Object> getBasicStats(String agentCode);
}