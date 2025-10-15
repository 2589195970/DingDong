-- ========================================
-- 产品照片配置功能数据库升级脚本
-- 版本：V2.1.2
-- 创建时间：2024-12-12
-- 升级说明：为产品表添加照片配置功能，支持商品级别的照片上传要求配置
-- 作者：Claude AI
-- ========================================

-- 1. 产品表扩展
-- 为产品表添加照片配置相关字段
ALTER TABLE `t_product`
ADD COLUMN `photo_required` TINYINT(1) DEFAULT 0 COMMENT '是否需要上传照片 0 否 1 是' AFTER `product_placard_map`,
ADD COLUMN `photo_config` JSON COMMENT '照片上传配置 JSON格式' AFTER `photo_required`;

-- 2. 添加索引（可选，如果需要根据照片配置查询产品）
-- ALTER TABLE `t_product`
-- ADD INDEX `idx_photo_required` (`photo_required`);

-- 3. 为现有产品设置默认值（可选）
-- 确保现有产品的照片配置为默认值（不需要上传照片）
-- UPDATE `t_product` SET `photo_required` = 0, `photo_config` = NULL WHERE `photo_required` IS NULL;

-- 升级脚本执行完成标记
-- SELECT 'Product photo config database upgrade completed successfully!' AS message;