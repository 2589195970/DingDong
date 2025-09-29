-- 直播管理功能SQL脚本
-- 创建日期: 2025-01-28
-- 功能说明: 代理商直播管理，包含配置管理和审核流程

-- 1. 直播配置表
CREATE TABLE `t_live_config` (
  `config_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_key` varchar(100) NOT NULL COMMENT '配置键',
  `config_value` text COMMENT '配置值内容',
  `config_type` varchar(50) DEFAULT 'text' COMMENT '配置类型(text/html)',
  `description` varchar(500) COMMENT '配置说明',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态(0:禁用 1:启用)',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`config_id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='直播配置表';

-- 2. 直播审核表(参考t_name_audit结构)
CREATE TABLE `t_live_audit` (
  `live_audit_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '直播审核记录ID',
  `sys_user_id` bigint(20) NOT NULL COMMENT '登录账号ID',
  `agent_code` varchar(50) NOT NULL COMMENT '代理商编码',
  `agent_name` varchar(100) COMMENT '代理商名称',
  `background_image` varchar(500) COMMENT '直播背景图URL',
  `douyin_uid` varchar(100) COMMENT '抖音UID',
  `douyin_account` varchar(100) COMMENT '抖音号',
  `status` int(11) DEFAULT '0' COMMENT '状态 0 待认证 1 审核失败 2 审核成功',
  `remark` varchar(500) COMMENT '审核备注',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`live_audit_id`),
  KEY `idx_agent_code` (`agent_code`),
  KEY `idx_sys_user_id` (`sys_user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='直播审核表';

-- 3. 插入默认配置数据
INSERT INTO `t_live_config` (`config_key`, `config_value`, `config_type`, `description`, `status`, `create_time`, `update_time`)
VALUES
('live_instruction', '## 直播操作说明\n\n### 1. 直播背景图要求\n- 图片格式：JPG、PNG\n- 图片尺寸：1920x1080\n- 图片大小：不超过2MB\n\n### 2. 抖音信息要求\n- 抖音UID：纯数字格式\n- 抖音号：符合抖音规范的账号名\n\n### 3. 审核流程\n1. 提交审核资料\n2. 等待管理员审核\n3. 审核通过后即可使用\n\n### 4. 注意事项\n- 请确保信息真实有效\n- 审核不通过可重新提交', 'text', '直播操作指导说明', 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000);

-- 4. 添加索引优化查询性能
CREATE INDEX `idx_live_audit_create_time` ON `t_live_audit` (`create_time`);
CREATE INDEX `idx_live_config_status` ON `t_live_config` (`status`);