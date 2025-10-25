
-- 为订单佣金表新增 VIP 汇总字段
ALTER TABLE t_order_commission
    ADD COLUMN vip_adjust_amount INT DEFAULT 0 COMMENT 'VIP加成汇总金额';

-- 为订单佣金明细表新增 VIP 相关字段
ALTER TABLE t_order_commission_details
    ADD COLUMN vip_level INT DEFAULT 0 COMMENT 'VIP等级',
    ADD COLUMN vip_bonus_commission INT DEFAULT 0 COMMENT 'VIP加成金额';

DROP TABLE IF EXISTS t_commission_config;
