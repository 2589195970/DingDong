SET @current_schema = DATABASE();

SELECT COUNT(*)
INTO @col_exists
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = @current_schema
  AND TABLE_NAME = 't_product'
  AND COLUMN_NAME = 'sffftk';

-- 1. 将历史数据迁移到新的产品类型
SET @ddl = IF(
    @col_exists = 1,
    'UPDATE t_product SET product_type = 5 WHERE (product_type IS NULL OR product_type <> 5) AND sffftk = 1',
    'SELECT ''t_product.sffftk not found, skip migration'''
);
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 2. 移除 sffftk 字段
SET @ddl = IF(
    @col_exists = 1,
    'ALTER TABLE t_product DROP COLUMN sffftk',
    'SELECT ''t_product.sffftk already removed'''
);
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
