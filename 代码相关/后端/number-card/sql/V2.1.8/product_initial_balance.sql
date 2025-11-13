SET @current_schema = DATABASE();

-- 1. t_product 表新增自带话费余额字段，默认 0
SELECT COUNT(*) INTO @col_exists
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = @current_schema
  AND TABLE_NAME = 't_product'
  AND COLUMN_NAME = 'product_initial_balance';

SET @ddl = IF(
        @col_exists = 0,
        'ALTER TABLE t_product ADD COLUMN product_initial_balance INT NOT NULL DEFAULT 0 COMMENT ''自带话费余额(元)'' AFTER base_card_fee',
        'SELECT ''t_product.product_initial_balance already exists'''
    );
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 2. 历史数据兜底为空值
UPDATE t_product
SET product_initial_balance = COALESCE(product_initial_balance, 0);

