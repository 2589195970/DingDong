-- 代理商排行榜相关数据库索引优化SQL
-- 执行前请先备份数据库

-- 1. 佣金详情表索引优化（用于佣金排行榜）
-- 添加组合索引：代理商编码 + 创建时间，用于快速查询指定时间范围内的佣金数据
ALTER TABLE `t_order_commission_details`
ADD INDEX `idx_agent_code_create_time` (`agent_code`, `create_time`);

-- 添加索引：创建时间，用于时间范围查询
ALTER TABLE `t_order_commission_details`
ADD INDEX `idx_create_time` (`create_time`);

-- 2. 订单表索引优化（用于订单量和激活量排行榜）
-- 添加组合索引：下游代理商编码 + 创建时间
ALTER TABLE `t_order`
ADD INDEX `idx_downstream_code_create_time` (`downstream_code`, `create_time`);

-- 添加组合索引：下游代理商编码 + 激活时间 + 订单状态
ALTER TABLE `t_order`
ADD INDEX `idx_downstream_code_active_time_status` (`downstream_code`, `active_time`, `order_status`);

-- 添加索引：激活时间，用于激活量统计
ALTER TABLE `t_order`
ADD INDEX `idx_active_time` (`active_time`);

-- 3. 代理商账户表索引优化（用于团队发展排行榜）
-- 添加组合索引：父代理商编码 + 创建时间
ALTER TABLE `t_agent_account`
ADD INDEX `idx_parent_agent_code_create_time` (`parent_agent_code`, `create_time`);

-- 添加索引：父代理商列表，用于团队查询（如果parent_agent_list字段存在）
-- ALTER TABLE `t_agent_account`
-- ADD INDEX `idx_parent_agent_list` (`parent_agent_list`(100));

-- 4. 订单佣金表索引优化
-- 添加索引：佣金状态，用于已结算佣金查询
ALTER TABLE `t_order_commission`
ADD INDEX `idx_order_commission_status` (`order_commission_status`);

-- 添加组合索引：佣金状态 + 创建时间
ALTER TABLE `t_order_commission`
ADD INDEX `idx_status_create_time` (`order_commission_status`, `create_time`);

-- 查询索引创建情况
SHOW INDEX FROM `t_order_commission_details`;
SHOW INDEX FROM `t_order`;
SHOW INDEX FROM `t_agent_account`;
SHOW INDEX FROM `t_order_commission`;

-- 性能优化建议：
-- 1. 定期执行 ANALYZE TABLE 命令更新表统计信息
-- 2. 考虑定期清理过期的历史数据
-- 3. 监控慢查询日志，进一步优化查询语句
-- 4. 在高并发场景下，可考虑使用读写分离