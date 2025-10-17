-- ========================================
-- 产品审核配置功能数据库升级脚本
-- 版本：V2.1.4
-- 创建时间：2025-01-17
-- 升级说明：为产品表添加审核配置功能，支持商品级别的审核要求配置
-- ========================================

-- 1. 产品表扩展
-- 为产品表添加审核配置相关字段
ALTER TABLE `t_product`
ADD COLUMN `sfxysh` TINYINT(1) DEFAULT 0 COMMENT '是否需要审核 0 否 1 是' AFTER `photo_required`;

-- 2. 添加索引（可选，如果需要根据审核配置查询产品）
-- ALTER TABLE `t_product`
-- ADD INDEX `idx_sfxysh` (`sfxysh`);

-- 3. 为现有产品设置默认值（可选）
-- 确保现有产品的审核配置为默认值（不需要审核）
-- UPDATE `t_product` SET `sfxysh` = 0 WHERE `sfxysh` IS NULL;

-- 升级脚本执行完成标记
SELECT 'Product audit config database upgrade completed successfully!' AS message;