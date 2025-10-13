-- ========================================
-- 商品照片审核功能数据库升级脚本
-- 版本：V2.1.0
-- 创建时间：2024-12-12
-- 升级说明：增加商品照片配置和订单照片审核功能
-- 作者：Claude AI
-- ========================================

-- 1. 商品表扩展
-- 注意：使用 RuoYi 标准的表结构修改方式
ALTER TABLE `t_product`
ADD COLUMN `photo_required` TINYINT(1) DEFAULT 0 COMMENT '是否需要上传照片 0 否 1 是' AFTER `product_placard_map`,
ADD COLUMN `photo_config` JSON COMMENT '照片上传配置 JSON格式' AFTER `photo_required`;

-- 2. 订单表扩展
-- 注意：保持与现有订单表结构的一致性
ALTER TABLE `t_order`
ADD COLUMN `product_id` INT(11) DEFAULT NULL COMMENT '产品ID',
ADD COLUMN `id_card_front_url` VARCHAR(500) DEFAULT NULL COMMENT '身份证正面照片URL',
ADD COLUMN `id_card_back_url` VARCHAR(500) DEFAULT NULL COMMENT '身份证反面照片URL',
ADD COLUMN `person_photo_url` VARCHAR(500) DEFAULT NULL COMMENT '免冠照片URL',
ADD COLUMN `custom_photo_url` VARCHAR(500) DEFAULT NULL COMMENT '自定义照片URL',
ADD COLUMN `photo_status` TINYINT(1) DEFAULT 0 COMMENT '照片审核状态 0 无需审核 1 待上传照片 2 代理商待提交 3 管理员待审核 4 审核通过 5 审核拒绝',
ADD COLUMN `photo_upload_time` BIGINT(20) DEFAULT NULL COMMENT '照片上传时间',
ADD COLUMN `photo_audit_user_id` BIGINT(20) DEFAULT NULL COMMENT '照片审核员ID',
ADD COLUMN `photo_audit_time` BIGINT(20) DEFAULT NULL COMMENT '照片审核时间',
ADD COLUMN `photo_audit_remark` VARCHAR(500) DEFAULT NULL COMMENT '照片审核备注';

-- 3. 添加索引（遵循 RuoYi 索引命名规范）
ALTER TABLE `t_order`
ADD INDEX `idx_photo_status` (`photo_status`),
ADD INDEX `idx_photo_audit_time` (`photo_audit_time`);

-- 4. 插入菜单权限（遵循 RuoYi 菜单规范）
-- 注意：需要根据实际的父菜单ID进行调整
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
('照片审核管理', 2, 6, 'photoAudit', 'order/photoAudit/index', '', 'photoAudit', 1, 0, 'C', '0', '0', 'order:photoAudit:list', 'photo', 'admin', NOW(), '', NULL, '照片审核管理菜单');

-- 插入权限按钮（需要根据实际菜单ID调整parent_id）
SET @photo_audit_menu_id = LAST_INSERT_ID();

INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
('照片审核查询', @photo_audit_menu_id, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'order:photoAudit:query', '#', 'admin', NOW(), '', NULL, '照片审核查询权限'),
('照片审核审核', @photo_audit_menu_id, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'order:photoAudit:audit', '#', 'admin', NOW(), '', NULL, '照片审核权限'),
('照片审核批量审核', @photo_audit_menu_id, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'order:photoAudit:batchAudit', '#', 'admin', NOW(), '', NULL, '照片审核批量审核权限');

-- 5. 创建示例图片存储目录（如果需要）
-- 此部分通常在应用启动时创建，或者由运维配置

-- 6. 更新现有商品配置（可选）
-- 为现有商品设置默认照片配置（如果需要）
-- UPDATE `t_product` SET `photo_required` = 0, `photo_config` = NULL WHERE 1=1;

-- 7. 添加角色权限（可选，根据实际需求）
-- INSERT INTO `sys_role_menu` (role_id, menu_id)
-- SELECT role_id, @photo_audit_menu_id FROM sys_role WHERE role_name IN ('admin', '管理员');

-- 升级脚本执行完成标记
-- SELECT 'Photo audit feature database upgrade completed successfully!' AS message;