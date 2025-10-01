-- ================================
-- 聚合页访问量统计功能
-- 数据库表创建脚本
-- 创建时间: 2025-01-26
-- ================================

-- ================================
-- 1. 页面访问统计汇总表
-- ================================
DROP TABLE IF EXISTS `t_page_view_statistics`;
CREATE TABLE `t_page_view_statistics` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `agent_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '代理商编码',
  `view_date` date NOT NULL COMMENT '访问日期',
  `view_count` int DEFAULT 0 COMMENT '独立访客浏览量（按visitor_id去重，同一用户一天只计算一次）',
  `visitor_count` int DEFAULT 0 COMMENT '独立访客数（与view_count相同，按visitor_id去重）',
  `ip_count` int DEFAULT 0 COMMENT '独立IP数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_agent_date` (`agent_code`,`view_date`) USING BTREE COMMENT '代理商+日期唯一索引',
  KEY `idx_agent_code` (`agent_code`) USING BTREE COMMENT '代理商编码索引',
  KEY `idx_view_date` (`view_date`) USING BTREE COMMENT '访问日期索引'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='页面访问统计汇总表';

-- ================================
-- 2. 页面访问明细记录表
-- ================================
DROP TABLE IF EXISTS `t_page_view_log`;
CREATE TABLE `t_page_view_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `agent_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '代理商编码',
  `visitor_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '访客标识(cookie/UUID)',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'IP地址',
  `user_agent` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '浏览器信息',
  `referer` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '来源页面',
  `device_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '设备类型(PC/Mobile/Tablet)',
  `visit_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_agent_code` (`agent_code`) USING BTREE COMMENT '代理商编码索引',
  KEY `idx_visitor_id` (`visitor_id`) USING BTREE COMMENT '访客ID索引',
  KEY `idx_ip_address` (`ip_address`) USING BTREE COMMENT 'IP地址索引',
  KEY `idx_visit_time` (`visit_time`) USING BTREE COMMENT '访问时间索引',
  KEY `idx_device_type` (`device_type`) USING BTREE COMMENT '设备类型索引',
  KEY `idx_agent_visit_time` (`agent_code`,`visit_time`) USING BTREE COMMENT '代理商+访问时间复合索引'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='页面访问明细记录表';

-- ================================
-- 3. 插入初始化数据示例（可选）
-- ================================
-- INSERT INTO `t_page_view_statistics` (`agent_code`, `view_date`, `view_count`, `visitor_count`, `ip_count`)
-- VALUES ('test_agent', CURDATE(), 0, 0, 0);

-- ================================
-- 4. 统计去重逻辑说明
-- ================================
-- 重要业务规则：同一用户（visitor_id）一天内只计算一次浏览量
--
-- 统计字段说明：
-- 1. view_count（独立访客浏览量）
--    - 按visitor_id去重统计
--    - 同一用户一天内多次访问只计算1次
--    - SQL计算方式：COUNT(DISTINCT visitor_id)
--
-- 2. visitor_count（独立访客数）
--    - 与view_count计算方式和结果完全相同
--    - SQL计算方式：COUNT(DISTINCT visitor_id)
--    - 保留此字段是为了语义清晰和兼容性
--
-- 3. ip_count（独立IP数）
--    - 按ip_address去重统计
--    - SQL计算方式：COUNT(DISTINCT ip_address)
--    - 注意：同一IP可能有多个用户（如公司网络、家庭网络）
--
-- 实现位置：
-- - PageViewMapper.xml 中的 insertOrUpdateStatistics
-- - PageViewMapper.xml 中的 selectTodayRealTimeStats
-- - PageViewMapper.xml 中的 repairStatisticsData

-- ================================
-- 5. 索引优化说明
-- ================================
-- uk_agent_date: 确保同一代理商同一天只有一条统计记录
-- idx_agent_code: 支持按代理商快速查询
-- idx_view_date: 支持按日期范围查询
-- idx_visitor_id: 支持去重统计独立访客
-- idx_ip_address: 支持去重统计独立IP
-- idx_visit_time: 支持按时间排序和分页
-- idx_agent_visit_time: 支持代理商的时间范围查询

-- ================================
-- 6. 数据保留策略建议
-- ================================
-- 建议保留策略：
-- 1. t_page_view_log 明细表保留90天数据
-- 2. t_page_view_statistics 汇总表保留1年数据
-- 3. 定期执行清理任务，删除过期数据

-- 清理过期数据SQL示例（90天前的明细数据）：
-- DELETE FROM t_page_view_log WHERE visit_time < DATE_SUB(NOW(), INTERVAL 90 DAY);

-- 清理过期数据SQL示例（1年前的统计数据）：
-- DELETE FROM t_page_view_statistics WHERE view_date < DATE_SUB(CURDATE(), INTERVAL 365 DAY);

-- ================================
-- 7. 常用查询SQL示例
-- ================================

-- 查询代理商今日访问统计
-- SELECT * FROM t_page_view_statistics
-- WHERE agent_code = 'your_agent_code' AND view_date = CURDATE();

-- 查询代理商最近7天访问趋势
-- SELECT * FROM t_page_view_statistics
-- WHERE agent_code = 'your_agent_code'
-- AND view_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)
-- ORDER BY view_date DESC;

-- 查询代理商今日访问明细
-- SELECT * FROM t_page_view_log
-- WHERE agent_code = 'your_agent_code'
-- AND DATE(visit_time) = CURDATE()
-- ORDER BY visit_time DESC;

-- 查询最活跃的访问者（今日）
-- SELECT ip_address, COUNT(*) as visit_count, MAX(visit_time) as last_visit
-- FROM t_page_view_log
-- WHERE agent_code = 'your_agent_code' AND DATE(visit_time) = CURDATE()
-- GROUP BY ip_address
-- ORDER BY visit_count DESC
-- LIMIT 10;

-- 查询访问来源分析（今日）
-- SELECT referer, COUNT(*) as count
-- FROM t_page_view_log
-- WHERE agent_code = 'your_agent_code'
-- AND DATE(visit_time) = CURDATE()
-- AND referer IS NOT NULL AND referer != ''
-- GROUP BY referer
-- ORDER BY count DESC;

-- 查询设备类型分布（今日）
-- SELECT device_type, COUNT(*) as count
-- FROM t_page_view_log
-- WHERE agent_code = 'your_agent_code' AND DATE(visit_time) = CURDATE()
-- GROUP BY device_type
-- ORDER BY count DESC;

-- ================================
-- 脚本执行完毕
-- ================================