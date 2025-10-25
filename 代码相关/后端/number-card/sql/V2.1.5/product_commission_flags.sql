-- 产品返现/提卡字段升级脚本
-- 新增字段默认值为 0，确保历史数据兼容
ALTER TABLE `t_product`
  ADD COLUMN `sfyjfx` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否参与佣金返现：0-否 1-是' AFTER `product_commission`,
  ADD COLUMN `sffftk` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否需要付费提卡：0-否 1-是' AFTER `sfyjfx`;

-- 历史数据兜底
UPDATE `t_product`
SET sfyjfx = 0,
    sffftk = 0
WHERE sfyjfx IS NULL OR sffftk IS NULL;
