package com.ruoyi.order.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据库初始化控制器
 * 用于创建页面访问统计相关的数据库表
 */
@Api(tags = "数据库初始化")
@RestController
@RequestMapping("/db-init")
@Slf4j
public class DatabaseInitController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/createPageViewTables")
    @ApiOperation("创建页面访问统计表")
    public String createPageViewTables() {
        try {
            // 创建页面访问明细记录表
            String createPageViewLogTable = "DROP TABLE IF EXISTS `t_page_view_log`;" +
                    "CREATE TABLE `t_page_view_log` (" +
                    "  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID'," +
                    "  `agent_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '代理商编码'," +
                    "  `visitor_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '访客标识(cookie/UUID)'," +
                    "  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'IP地址'," +
                    "  `user_agent` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '浏览器信息'," +
                    "  `referer` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '来源页面'," +
                    "  `device_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '设备类型(PC/Mobile/Tablet)'," +
                    "  `visit_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间'," +
                    "  PRIMARY KEY (`id`) USING BTREE," +
                    "  KEY `idx_agent_code` (`agent_code`) USING BTREE COMMENT '代理商编码索引'," +
                    "  KEY `idx_visitor_id` (`visitor_id`) USING BTREE COMMENT '访客ID索引'," +
                    "  KEY `idx_ip_address` (`ip_address`) USING BTREE COMMENT 'IP地址索引'," +
                    "  KEY `idx_visit_time` (`visit_time`) USING BTREE COMMENT '访问时间索引'," +
                    "  KEY `idx_device_type` (`device_type`) USING BTREE COMMENT '设备类型索引'," +
                    "  KEY `idx_agent_visit_time` (`agent_code`,`visit_time`) USING BTREE COMMENT '代理商+访问时间复合索引'" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='页面访问明细记录表';";

            // 先删除表
            jdbcTemplate.execute("DROP TABLE IF EXISTS `t_page_view_log`");
            log.info("删除旧的t_page_view_log表成功");

            // 创建表
            String createTableSql = "CREATE TABLE `t_page_view_log` (" +
                    "  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID'," +
                    "  `agent_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '代理商编码'," +
                    "  `visitor_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '访客标识(cookie/UUID)'," +
                    "  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'IP地址'," +
                    "  `user_agent` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '浏览器信息'," +
                    "  `referer` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '来源页面'," +
                    "  `device_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '设备类型(PC/Mobile/Tablet)'," +
                    "  `visit_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间'," +
                    "  PRIMARY KEY (`id`) USING BTREE," +
                    "  KEY `idx_agent_code` (`agent_code`) USING BTREE COMMENT '代理商编码索引'," +
                    "  KEY `idx_visitor_id` (`visitor_id`) USING BTREE COMMENT '访客ID索引'," +
                    "  KEY `idx_ip_address` (`ip_address`) USING BTREE COMMENT 'IP地址索引'," +
                    "  KEY `idx_visit_time` (`visit_time`) USING BTREE COMMENT '访问时间索引'," +
                    "  KEY `idx_device_type` (`device_type`) USING BTREE COMMENT '设备类型索引'," +
                    "  KEY `idx_agent_visit_time` (`agent_code`,`visit_time`) USING BTREE COMMENT '代理商+访问时间复合索引'" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='页面访问明细记录表'";

            jdbcTemplate.execute(createTableSql);
            log.info("创建t_page_view_log表成功");

            // 创建页面访问统计汇总表
            String createStatisticsTable = "CREATE TABLE IF NOT EXISTS `t_page_view_statistics` (" +
                    "  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID'," +
                    "  `agent_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '代理商编码'," +
                    "  `view_date` date NOT NULL COMMENT '访问日期'," +
                    "  `view_count` int DEFAULT 0 COMMENT '访问次数'," +
                    "  `visitor_count` int DEFAULT 0 COMMENT '独立访客数'," +
                    "  `ip_count` int DEFAULT 0 COMMENT '独立IP数'," +
                    "  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'," +
                    "  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'," +
                    "  PRIMARY KEY (`id`) USING BTREE," +
                    "  UNIQUE KEY `uk_agent_date` (`agent_code`,`view_date`) USING BTREE COMMENT '代理商+日期唯一索引'," +
                    "  KEY `idx_agent_code` (`agent_code`) USING BTREE COMMENT '代理商编码索引'," +
                    "  KEY `idx_view_date` (`view_date`) USING BTREE COMMENT '访问日期索引'" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='页面访问统计汇总表'";

            jdbcTemplate.execute(createStatisticsTable);
            log.info("创建t_page_view_statistics表成功");

            return "SUCCESS: 页面访问统计表创建成功！包含t_page_view_log和t_page_view_statistics两个表。";

        } catch (Exception e) {
            log.error("创建页面访问统计表失败：", e);
            return "ERROR: 创建表失败 - " + e.getMessage();
        }
    }

    @PostMapping("/checkTables")
    @ApiOperation("检查表是否存在")
    public String checkTables() {
        try {
            String checkSql = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'simple_deployment' AND table_name IN ('t_page_view_log', 't_page_view_statistics')";
            List<String> tables = jdbcTemplate.queryForList(checkSql, String.class);

            StringBuilder result = new StringBuilder("数据库表检查结果:\n");
            result.append("已存在的表: ").append(tables).append("\n");

            if (tables.contains("t_page_view_log")) {
                result.append("✅ t_page_view_log 表存在\n");
            } else {
                result.append("❌ t_page_view_log 表不存在\n");
            }

            if (tables.contains("t_page_view_statistics")) {
                result.append("✅ t_page_view_statistics 表存在\n");
            } else {
                result.append("❌ t_page_view_statistics 表不存在\n");
            }

            return result.toString();
        } catch (Exception e) {
            log.error("检查表失败：", e);
            return "ERROR: 检查表失败 - " + e.getMessage();
        }
    }
}