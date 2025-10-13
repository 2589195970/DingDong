-- ========================================
-- 照片审核默认配置管理功能数据库升级脚本
-- 版本：V2.1.1
-- 创建时间：2024-12-12
-- 升级说明：增加照片审核默认配置管理功能
-- ========================================

-- 1. 创建照片默认配置表
CREATE TABLE IF NOT EXISTS `t_photo_default_config` (
  `config_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_name` VARCHAR(100) NOT NULL COMMENT '配置名称',
  `config_type` TINYINT(1) DEFAULT 1 COMMENT '配置类型 1-默认模板 2-自定义模板',
  `photo_config` TEXT NOT NULL COMMENT '照片配置JSON',
  `description` VARCHAR(500) DEFAULT NULL COMMENT '配置描述',
  `is_active` TINYINT(1) DEFAULT 1 COMMENT '是否启用 0-禁用 1-启用',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`),
  UNIQUE KEY `uk_config_name` (`config_name`),
  KEY `idx_config_type` (`config_type`),
  KEY `idx_is_active` (`is_active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='照片默认配置表';

-- 2. 插入默认照片配置模板
INSERT INTO `t_photo_default_config` (`config_name`, `config_type`, `photo_config`, `description`, `create_by`, `remark`)
VALUES ('标准身份验证模板', 1, '[
  {
    "photoType": 1,
    "photoTypeName": "身份证正面",
    "required": 1,
    "title": "身份证正面照片",
    "description": "请上传清晰的身份证正面照片，确保证件完整、无遮挡、无反光",
    "exampleUrl": "/static/examples/id-card-front-example.jpg",
    "maxSize": 5242880,
    "supportedFormats": "jpg,jpeg,png",
    "minWidth": 800,
    "minHeight": 600
  },
  {
    "photoType": 2,
    "photoTypeName": "身份证反面",
    "required": 1,
    "title": "身份证反面照片",
    "description": "请上传清晰的身份证反面照片，确保证件完整、国徽清晰可见",
    "exampleUrl": "/static/examples/id-card-back-example.jpg",
    "maxSize": 5242880,
    "supportedFormats": "jpg,jpeg,png",
    "minWidth": 800,
    "minHeight": 600
  },
  {
    "photoType": 3,
    "photoTypeName": "免冠照片",
    "required": 1,
    "title": "免冠照片",
    "description": "请上传近期免冠正面照片，要求正面免冠、背景简洁、表情自然",
    "exampleUrl": "/static/examples/person-photo-example.jpg",
    "maxSize": 5242880,
    "supportedFormats": "jpg,jpeg,png",
    "minWidth": 300,
    "minHeight": 400
  }
]', '标准身份验证照片配置模板，包含身份证正面、反面和免冠照片要求', 'admin', '系统默认的照片审核配置模板，用于新商品自动配置')
ON DUPLICATE KEY UPDATE
`photo_config` = VALUES(`photo_config`),
`update_time` = CURRENT_TIMESTAMP,
`update_by` = 'admin';

-- 3. 添加系统管理菜单 - 照片配置管理
-- 注意：照片配置管理属于系统配置，应该放在系统管理目录下（parent_id = 1）
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES ('照片配置管理', 1, 8, 'photoConfig', 'console/photoConfig/index', '', 1, 0, 'C', '0', '0', 'console:photoConfig:list', 'picture', 'admin', NOW(), 'admin', NOW(), '照片配置管理菜单');

-- 获取刚插入的菜单ID，假设为最新的菜单ID
SET @photo_config_menu_id = LAST_INSERT_ID();

-- 4. 添加照片配置管理子菜单权限
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES
('照片配置查询', @photo_config_menu_id, 1, '', '', '', 1, 0, 'F', '0', '0', 'console:photoConfig:query', '#', 'admin', NOW(), 'admin', NOW(), ''),
('照片配置新增', @photo_config_menu_id, 2, '', '', '', 1, 0, 'F', '0', '0', 'console:photoConfig:add', '#', 'admin', NOW(), 'admin', NOW(), ''),
('照片配置修改', @photo_config_menu_id, 3, '', '', '', 1, 0, 'F', '0', '0', 'console:photoConfig:edit', '#', 'admin', NOW(), 'admin', NOW(), ''),
('照片配置删除', @photo_config_menu_id, 4, '', '', '', 1, 0, 'F', '0', '0', 'console:photoConfig:remove', '#', 'admin', NOW(), 'admin', NOW(), ''),
('照片配置导出', @photo_config_menu_id, 5, '', '', '', 1, 0, 'F', '0', '0', 'console:photoConfig:export', '#', 'admin', NOW(), 'admin', NOW(), ''),
('默认配置查询', @photo_config_menu_id, 6, '', '', '', 1, 0, 'F', '0', '0', 'console:photoConfig:default', '#', 'admin', NOW(), 'admin', NOW(), ''),
('默认配置更新', @photo_config_menu_id, 7, '', '', '', 1, 0, 'F', '0', '0', 'console:photoConfig:defaultUpdate', '#', 'admin', NOW(), 'admin', NOW(), '');

-- 5. 为管理员角色添加菜单权限
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 1, menu_id FROM `sys_menu`
WHERE `menu_name` IN ('照片配置管理', '照片配置查询', '照片配置新增', '照片配置修改', '照片配置删除', '照片配置导出', '默认配置查询', '默认配置更新')
AND `menu_id` NOT IN (SELECT `menu_id` FROM `sys_role_menu` WHERE `role_id` = 1);
