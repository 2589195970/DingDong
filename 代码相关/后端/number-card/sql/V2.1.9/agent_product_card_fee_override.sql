SET @current_schema = DATABASE();

-- 1. 新增提卡费特例表
CREATE TABLE IF NOT EXISTS `t_agent_product_card_fee_override` (
    `override_id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `product_code`         VARCHAR(64)     NOT NULL COMMENT '商品编码',
    `parent_agent_code`    VARCHAR(64)     NOT NULL COMMENT '上级代理编码',
    `target_agent_code`    VARCHAR(64)     NOT NULL COMMENT '特例下级代理编码',
    `override_fee`         INT             NOT NULL DEFAULT 0 COMMENT '特例提卡费售价(元)',
    `incoming_card_fee`    INT             NOT NULL DEFAULT 0 COMMENT '创建时参考上级成本(元)',
    `status`               TINYINT         NOT NULL DEFAULT 1 COMMENT '状态 1-生效 0-失效',
    `effective_time`       BIGINT          NOT NULL COMMENT '生效时间(毫秒时间戳)',
    `expire_time`          BIGINT          NULL COMMENT '失效时间(毫秒时间戳, 可为空)',
    `memo`                 VARCHAR(255)    NULL COMMENT '备注',
    `operator`             VARCHAR(64)     NULL COMMENT '最后操作人',
    `create_time`          BIGINT          NOT NULL COMMENT '创建时间(毫秒时间戳)',
    `update_time`          BIGINT          NOT NULL COMMENT '更新时间(毫秒时间戳)',
    PRIMARY KEY (`override_id`),
    UNIQUE KEY `uk_override_active` (`product_code`, `parent_agent_code`, `target_agent_code`, `status`),
    KEY `idx_override_product` (`product_code`),
    KEY `idx_override_parent` (`parent_agent_code`),
    KEY `idx_override_target` (`target_agent_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '代理商品提卡费特例表';

-- 2. 代理商品表增加特例标记
SELECT COUNT(*) INTO @col_exists
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = @current_schema
  AND TABLE_NAME = 't_agent_product'
  AND COLUMN_NAME = 'has_override';

SET @ddl = IF(
        @col_exists = 0,
        'ALTER TABLE t_agent_product ADD COLUMN has_override TINYINT(1) NOT NULL DEFAULT 0 COMMENT ''是否存在提卡费特例 0-否 1-是'' AFTER downstream_card_fee',
        'SELECT ''t_agent_product.has_override already exists'''
    );
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 3. 历史数据默认无特例
UPDATE t_agent_product
SET has_override = 0
WHERE has_override IS NULL;
