-- ========================================
-- 商品照片审核功能数据库升级脚本
-- 版本：V2.1.0
-- 创建时间：2024-12-12
-- 升级说明：增加商品照片配置和订单照片审核功能
-- 作者：Claude AI
-- ========================================

--  订单表扩展
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

--  添加索引（遵循 RuoYi 索引命名规范）
ALTER TABLE `t_order`
ADD INDEX `idx_photo_status` (`photo_status`),
ADD INDEX `idx_photo_audit_time` (`photo_audit_time`);
