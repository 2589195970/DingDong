-- ----------------------------
-- 1.初始化admin账户t_withdrawal_config记录
-- ----------------------------
INSERT INTO `numberCard`.`t_withdrawal_config` (`withdraw_config_id`, `sys_user_id`, `agent_code`, `withdraw_min_amount`, `withdraw_rate`, `create_time`, `update_time`) VALUES (1, 1, 'msyn2kps', 100, 6, 1737611513174, NULL);

-- ----------------------------
-- 2.初始化admin账户t_agent_account记录
-- ----------------------------
INSERT INTO `numberCard`.`t_agent_account` (`agent_account_id`, `sys_user_id`, `parent_agent_code`, `agent_code`, `agent_name`, `parent_agent_list`, `level`, `is_real_name`, `card_id_url_front`, `card_id_url_back`, `phone`, `is_enabled`, `create_time`, `update_time`) VALUES (1, 1, 'msyn2kps1', 'msyn2kps', 'admin', '[{\"agentCode\":\"msyn2kps\",\"level\":0,\"sysUserId\":1}]', 0, 0, NULL, NULL, NULL, 0, 1734678657422, 1734678657422);
