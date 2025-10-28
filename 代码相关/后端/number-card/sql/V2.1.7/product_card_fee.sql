SET @current_schema = DATABASE();

-- 1. 产品表增加基础提卡费
SELECT COUNT(*) INTO @col_exists
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = @current_schema
  AND TABLE_NAME = 't_product'
  AND COLUMN_NAME = 'base_card_fee';

SET @ddl = IF(
        @col_exists = 0,
        'ALTER TABLE t_product ADD COLUMN base_card_fee INT NOT NULL DEFAULT 0 COMMENT ''基础提卡费(元，admin对一级成本)'' AFTER sffftk',
        'SELECT ''t_product.base_card_fee already exists'''
    );
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- incoming_card_fee
SELECT COUNT(*) INTO @col_exists
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = @current_schema
  AND TABLE_NAME = 't_agent_product'
  AND COLUMN_NAME = 'incoming_card_fee';

SET @ddl = IF(
        @col_exists = 0,
        'ALTER TABLE t_agent_product ADD COLUMN incoming_card_fee INT NOT NULL DEFAULT 0 COMMENT ''上级给当前代理的提卡费成本(元)'' AFTER product_commission',
        'SELECT ''t_agent_product.incoming_card_fee already exists'''
    );
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- card_fee_profit
SELECT COUNT(*) INTO @col_exists
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = @current_schema
  AND TABLE_NAME = 't_agent_product'
  AND COLUMN_NAME = 'card_fee_profit';

SET @ddl = IF(
        @col_exists = 0,
        'ALTER TABLE t_agent_product ADD COLUMN card_fee_profit INT NOT NULL DEFAULT 0 COMMENT ''当前代理留存的提卡费差价(元)'' AFTER incoming_card_fee',
        'SELECT ''t_agent_product.card_fee_profit already exists'''
    );
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- downstream_card_fee
SELECT COUNT(*) INTO @col_exists
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = @current_schema
  AND TABLE_NAME = 't_agent_product'
  AND COLUMN_NAME = 'downstream_card_fee';

SET @ddl = IF(
        @col_exists = 0,
        'ALTER TABLE t_agent_product ADD COLUMN downstream_card_fee INT NOT NULL DEFAULT 0 COMMENT ''当前代理对下级的提卡费售价(元)'' AFTER card_fee_profit',
        'SELECT ''t_agent_product.downstream_card_fee already exists'''
    );
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 3. 初始化历史数据
-- 3.1 基础提卡费：仅对 sffftk = 1 的商品生效，禁止负值
UPDATE t_product
SET base_card_fee = GREATEST(COALESCE(base_card_fee, 0), 0)
WHERE sffftk = 1;

-- 3.2 代理商品卡费字段兜底为 0
UPDATE t_agent_product
SET incoming_card_fee   = COALESCE(incoming_card_fee, 0),
    downstream_card_fee = COALESCE(downstream_card_fee, 0),
    card_fee_profit     = COALESCE(card_fee_profit, 0);

-- 3.3 构建一个一次性存储过程，自顶向下刷新卡费链
DELIMITER $$
DROP PROCEDURE IF EXISTS sp_refresh_agent_card_fee$$
CREATE PROCEDURE sp_refresh_agent_card_fee()
BEGIN
    DECLARE rows_affected INT DEFAULT 1;

    -- 顶级代理（没有父级）继承产品基础提卡费
    UPDATE t_agent_product ap
        JOIN t_product p ON p.product_code = ap.parent_product_code
    SET ap.incoming_card_fee   = p.base_card_fee,
        ap.downstream_card_fee = p.base_card_fee,
        ap.card_fee_profit     = 0
    WHERE (ap.parent_agent_code IS NULL OR ap.parent_agent_code = '')
      AND p.sffftk = 1;

    -- 逐层遍历，将父级的对下售价同步为子级成本，初始差价拉平
    WHILE rows_affected > 0 DO
        UPDATE t_agent_product child
            JOIN t_agent_product parent
                ON parent.agent_code = child.parent_agent_code
               AND parent.parent_product_code = child.parent_product_code
            JOIN t_product p
                ON p.product_code = child.parent_product_code
        SET child.incoming_card_fee   = parent.downstream_card_fee,
            child.downstream_card_fee = parent.downstream_card_fee,
            child.card_fee_profit     = 0
        WHERE p.sffftk = 1
          AND child.parent_agent_code IS NOT NULL
          AND child.parent_agent_code <> ''
          AND (
              child.incoming_card_fee <> parent.downstream_card_fee
              OR child.downstream_card_fee <> parent.downstream_card_fee
              OR child.card_fee_profit <> 0
          );

        SET rows_affected = ROW_COUNT();
    END WHILE;
END$$
DELIMITER ;

CALL sp_refresh_agent_card_fee();
DROP PROCEDURE IF EXISTS sp_refresh_agent_card_fee;
