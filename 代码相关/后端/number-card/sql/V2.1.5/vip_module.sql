-- ========================================
-- VIP 模块数据库升级脚本
-- 版本：V2.1.5
-- 创建时间：2025-10-21
-- 升级说明：新增 VIP 等级配置表、VIP 操作日志表、VIP 升级日志表，并初始化默认等级数据
-- ========================================

-- 1. VIP 等级配置表
CREATE TABLE IF NOT EXISTS `t_vip_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `vip_level` int NOT NULL COMMENT 'VIP等级',
  `level_name` varchar(50) NOT NULL COMMENT '等级名称',
  `required_orders` int DEFAULT '0' COMMENT '升级所需订单数',
  `fixed_commission` int DEFAULT '0' COMMENT 'VIP固定佣金加成（单位：元）',
  `level_icon` varchar(200) DEFAULT NULL COMMENT '等级图标',
  `is_enabled` tinyint DEFAULT '1' COMMENT '是否启用 0否 1是',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_vip_level` (`vip_level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='VIP配置表';

-- 若已存在旧表结构，调整时间字段类型
ALTER TABLE `t_vip_config`
    MODIFY COLUMN `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    MODIFY COLUMN `update_time` datetime DEFAULT NULL COMMENT '更新时间';

-- 1.1 VIP 用户表
CREATE TABLE IF NOT EXISTS `t_vip_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `agent_account_id` int DEFAULT NULL COMMENT '代理商账号ID(t_agent_account表ID)',
  `user_id` bigint NOT NULL COMMENT 'sys_user表ID',
  `agent_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '代理商编码',
  `agent_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '代理商名称',
  `vip_level` int DEFAULT '0' COMMENT '当前VIP等级',
  `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `operator_id` bigint DEFAULT NULL COMMENT '最近操作人ID',
  `operator_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '最近操作人姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_vip_user_user_id` (`user_id`),
  UNIQUE KEY `uk_vip_user_agent_code` (`agent_code`),
  KEY `idx_vip_level` (`vip_level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='VIP用户表';

-- 1.2 将现有代理商补充为VIP用户（可重复执行，自动跳过已存在数据）
INSERT INTO `t_vip_user` (
    `agent_account_id`,
    `user_id`,
    `agent_code`,
    `agent_name`,
    `vip_level`,
    `remark`,
    `operator_id`,
    `operator_name`,
    `create_time`,
    `update_time`
)
SELECT
    aa.agent_account_id,
    aa.sys_user_id,
    aa.agent_code,
    aa.agent_name,
    COALESCE(aa.level, 0) AS vip_level,
    NULL AS remark,
    NULL AS operator_id,
    NULL AS operator_name,
    CASE
        WHEN aa.create_time IS NULL OR aa.create_time = 0 THEN NOW()
        WHEN aa.create_time >= 1000000000000 THEN FROM_UNIXTIME(aa.create_time / 1000)
        ELSE FROM_UNIXTIME(aa.create_time)
    END AS create_time,
    CASE
        WHEN aa.update_time IS NULL OR aa.update_time = 0 THEN NOW()
        WHEN aa.update_time >= 1000000000000 THEN FROM_UNIXTIME(aa.update_time / 1000)
        ELSE FROM_UNIXTIME(aa.update_time)
    END AS update_time
FROM `t_agent_account` aa
JOIN `sys_user` su ON su.user_id = aa.sys_user_id
WHERE NOT EXISTS (
    SELECT 1
    FROM `t_vip_user` vu
    WHERE vu.user_id = aa.sys_user_id
       OR vu.agent_code = aa.agent_code
);

-- 初始化默认 VIP 等级数据（可多次执行）
INSERT INTO `t_vip_config` (`vip_level`, `level_name`, `required_orders`, `fixed_commission`, `level_icon`, `is_enabled`, `remark`, `create_time`, `update_time`)
VALUES
    (0, '普通会员', 0, 0, NULL, 1, '系统初始等级', NOW(), NOW()),
    (1, '铜牌会员', 10, 0, NULL, 1, NULL, NOW(), NOW()),
    (2, '银牌会员', 50, 0, NULL, 1, NULL, NOW(), NOW()),
    (3, '金牌会员', 200, 0, NULL, 1, NULL, NOW(), NOW()),
    (4, '白金会员', 500, 0, NULL, 1, NULL, NOW(), NOW()),
    (5, '钻石会员', 1000, 0, NULL, 1, NULL, NOW(), NOW())
ON DUPLICATE KEY UPDATE
    `level_name` = VALUES(`level_name`),
    `required_orders` = VALUES(`required_orders`),
    `fixed_commission` = VALUES(`fixed_commission`),
    `level_icon` = VALUES(`level_icon`),
    `is_enabled` = VALUES(`is_enabled`),
    `remark` = VALUES(`remark`),
    `update_time` = NOW();

-- 2. VIP 操作日志表
CREATE TABLE IF NOT EXISTS `t_vip_operation_log` (
  `log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` bigint DEFAULT NULL COMMENT '操作用户ID',
  `target_agent_code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '目标代理商编码',
  `operation_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作类型(SET_LEVEL/UPGRADE等)',
  `method_name` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '执行方法名',
  `request_params` text COLLATE utf8mb4_unicode_ci COMMENT '请求参数(JSON格式)',
  `result` text COLLATE utf8mb4_unicode_ci COMMENT '执行结果',
  `ip_address` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'IP地址',
  `user_agent` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户代理',
  `operation_time` bigint DEFAULT NULL COMMENT '操作时间(毫秒时间戳)',
  `error_message` text COLLATE utf8mb4_unicode_ci COMMENT '错误信息',
  `execution_time` int DEFAULT '0' COMMENT '执行时间(毫秒)',
  `status` tinyint DEFAULT '1' COMMENT '操作状态(1成功 0失败)',
  PRIMARY KEY (`log_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_target_agent_code` (`target_agent_code`),
  KEY `idx_operation_type` (`operation_type`),
  KEY `idx_operation_time` (`operation_time`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='VIP操作日志表';

-- 3. VIP 升级日志表
CREATE TABLE IF NOT EXISTS `t_vip_upgrade_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID(sys_user表ID)',
  `agent_code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '代理商编码',
  `agent_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '代理商名称',
  `from_level` int DEFAULT '0' COMMENT '原VIP等级',
  `to_level` int DEFAULT '0' COMMENT '新VIP等级',
  `upgrade_type` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'AUTO' COMMENT '升级类型(AUTO自动升级/MANUAL手动升级)',
  `upgrade_reason` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '升级原因',
  `order_count` int DEFAULT '0' COMMENT '升级时订单数量',
  `operator_id` bigint DEFAULT NULL COMMENT '操作人ID(手动升级时记录)',
  `operator_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '操作人姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_agent_code` (`agent_code`),
  KEY `idx_agent_name` (`agent_name`),
  KEY `idx_from_level` (`from_level`),
  KEY `idx_to_level` (`to_level`),
  KEY `idx_upgrade_type` (`upgrade_type`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='VIP升级日志表';

ALTER TABLE `t_vip_upgrade_log`
    ADD COLUMN IF NOT EXISTS `agent_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '代理商名称' AFTER `agent_code`,
    MODIFY COLUMN `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间';

-- 4. 菜单与权限配置
-- 4.1 VIP 顶级菜单
INSERT INTO `numbercard`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (2066, 'VIP管理', 0, 8, 'vip', NULL, '', '', 1, 0, 'M', '0', '0', '', 'vip', 'admin', '2025-10-19 21:32:17', '', NULL, 'VIP功能主菜单')
ON DUPLICATE KEY UPDATE
    `menu_name` = VALUES(`menu_name`),
    `order_num` = VALUES(`order_num`),
    `path` = VALUES(`path`),
    `icon` = VALUES(`icon`),
    `remark` = VALUES(`remark`),
    `update_time` = NOW();

-- 4.2 VIP 配置管理菜单及按钮权限
INSERT INTO `numbercard`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (2067, 'VIP配置管理', 2066, 1, 'config', 'vip/config/index', '', '', 1, 0, 'C', '0', '0', 'vip:config:list', 'list', 'admin', '2025-10-19 21:32:17', '', NULL, 'VIP配置管理菜单')
ON DUPLICATE KEY UPDATE
    `menu_name` = VALUES(`menu_name`),
    `order_num` = VALUES(`order_num`),
    `path` = VALUES(`path`),
    `component` = VALUES(`component`),
    `perms` = VALUES(`perms`),
    `icon` = VALUES(`icon`),
    `remark` = VALUES(`remark`),
    `update_time` = NOW();

INSERT INTO `numbercard`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES
    (2068, 'VIP配置查询', 2067, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'vip:config:query', '#', 'admin', '2025-10-19 21:32:18', '', NULL, 'VIP配置查询权限'),
    (2069, 'VIP配置新增', 2067, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'vip:config:add', '#', 'admin', '2025-10-19 21:32:18', '', NULL, 'VIP配置新增权限'),
    (2070, 'VIP配置修改', 2067, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'vip:config:edit', '#', 'admin', '2025-10-19 21:32:18', '', NULL, 'VIP配置修改权限'),
    (2071, 'VIP配置删除', 2067, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'vip:config:remove', '#', 'admin', '2025-10-19 21:32:18', '', NULL, 'VIP配置删除权限'),
    (2072, 'VIP配置导出', 2067, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'vip:config:export', '#', 'admin', '2025-10-19 21:32:18', '', NULL, 'VIP配置导出权限')
ON DUPLICATE KEY UPDATE
    `menu_name` = VALUES(`menu_name`),
    `order_num` = VALUES(`order_num`),
    `perms` = VALUES(`perms`),
    `remark` = VALUES(`remark`),
    `update_time` = NOW();

-- 4.3 VIP 用户管理菜单及按钮权限
INSERT INTO `numbercard`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (2073, 'VIP用户管理', 2066, 2, 'user', 'vip/user/index', '', '', 1, 0, 'C', '0', '0', 'vip:user:list', 'user', 'admin', '2025-10-19 21:32:31', '', NULL, 'VIP用户管理菜单')
ON DUPLICATE KEY UPDATE
    `menu_name` = VALUES(`menu_name`),
    `order_num` = VALUES(`order_num`),
    `path` = VALUES(`path`),
    `component` = VALUES(`component`),
    `perms` = VALUES(`perms`),
    `icon` = VALUES(`icon`),
    `remark` = VALUES(`remark`),
    `update_time` = NOW();

INSERT INTO `numbercard`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES
    (2074, 'VIP用户查询', 2073, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'vip:user:query', '#', 'admin', '2025-10-19 21:32:31', '', NULL, 'VIP用户查询权限'),
    (2075, 'VIP用户新增', 2073, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'vip:user:add', '#', 'admin', '2025-10-19 21:32:31', '', NULL, 'VIP用户新增权限'),
    (2076, 'VIP用户修改', 2073, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'vip:user:edit', '#', 'admin', '2025-10-19 21:32:31', '', NULL, 'VIP用户修改权限'),
    (2077, 'VIP用户删除', 2073, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'vip:user:remove', '#', 'admin', '2025-10-19 21:32:31', '', NULL, 'VIP用户删除权限'),
    (2078, 'VIP用户导出', 2073, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'vip:user:export', '#', 'admin', '2025-10-19 21:32:31', '', NULL, 'VIP用户导出权限'),
    (2079, '设置VIP等级', 2073, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'vip:user:setLevel', '#', 'admin', '2025-10-19 21:32:31', '', NULL, '设置VIP等级权限'),
    (2080, '升级日志查询', 2073, 7, '#', '', '', '', 1, 0, 'F', '0', '0', 'vip:user:upgradeLog', '#', 'admin', '2025-10-19 21:32:31', '', NULL, '升级日志查询权限'),
    (2081, 'VIP用户补录', 2073, 8, '#', '', '', '', 1, 0, 'F', '0', '0', 'vip:user:supplement', '#', 'admin', '2025-10-19 21:32:31', '', NULL, 'VIP用户补录权限')
ON DUPLICATE KEY UPDATE
    `menu_name` = VALUES(`menu_name`),
    `order_num` = VALUES(`order_num`),
    `perms` = VALUES(`perms`),
    `remark` = VALUES(`remark`),
    `update_time` = NOW();

-- 授权管理员角色使用补录按钮
INSERT INTO `numbercard`.`sys_role_menu` (`role_id`, `menu_id`)
VALUES (1, 2081)
ON DUPLICATE KEY UPDATE
    `role_id` = VALUES(`role_id`),
    `menu_id` = VALUES(`menu_id`);

-- 4.4 VIP 升级日志菜单及按钮权限
INSERT INTO `numbercard`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (2081, 'VIP升级日志', 2066, 3, 'upgradeLog', 'vip/upgradeLog/index', '', '', 1, 0, 'C', '0', '0', 'vip:upgradeLog:list', 'log', 'admin', '2025-10-19 21:32:50', '', NULL, 'VIP升级日志菜单')
ON DUPLICATE KEY UPDATE
    `menu_name` = VALUES(`menu_name`),
    `order_num` = VALUES(`order_num`),
    `path` = VALUES(`path`),
    `component` = VALUES(`component`),
    `perms` = VALUES(`perms`),
    `icon` = VALUES(`icon`),
    `remark` = VALUES(`remark`),
    `update_time` = NOW();

INSERT INTO `numbercard`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES
    (2082, '升级日志查询', 2081, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'vip:upgradeLog:query', '#', 'admin', '2025-10-19 21:32:50', '', NULL, '升级日志查询权限'),
    (2083, '升级日志导出', 2081, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'vip:upgradeLog:export', '#', 'admin', '2025-10-19 21:32:50', '', NULL, '升级日志导出权限')
ON DUPLICATE KEY UPDATE
    `menu_name` = VALUES(`menu_name`),
    `order_num` = VALUES(`order_num`),
    `perms` = VALUES(`perms`),
    `remark` = VALUES(`remark`),
    `update_time` = NOW();

-- 升级脚本执行完成标记
SELECT 'VIP module database upgrade completed successfully!' AS message;
