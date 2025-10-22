
-----

# 叮咚号卡系统VIP功能模块设计方案

## 一、项目背景

基于现有叮咚号卡订单管理系统，客户希望增加VIP功能模块，通过VIP等级体系实现差异化管理，提升代理商积极性。

## 二、VIP功能需求分析

### 2.1 核心需求

1.  **VIP等级体系**: 设置多个VIP等级。
2.  **权限管理**: 管理员可设置所有用户VIP等级，代理商可设置子代理VIP等级。
3.  **订单触发升级**: 订单数量达到条件后立即自动升级VIP等级。
4.  **实时升级**: 订单完成后实时检查并执行升级。

### 2.2 业务规则

1.  **VIP等级设置**: 管理员可以给所有用户设置VIP等级。
2.  **代理权限**: 代理商可以给自己的子代理设置VIP等级，但不能高于自己的等级。
3.  **升级机制**: 订单完成后实时检查，基于订单数量立即自动升级。
4.  **降级保护**: VIP等级原则上只升不降，保护用户体验。

## 三、VIP功能模块设计

### 3.1 VIP等级体系设计

#### 3.1.1 VIP等级定义

```
VIP等级   等级名称     升级所需订单数
VIP0      普通会员     0
VIP1      铜牌会员     10
VIP2      银牌会员     50
VIP3      金牌会员     200
VIP4      白金会员     500
VIP5      钻石会员     1000
```

**核心机制说明**：

- **升级条件**：仅基于订单数量。
- **可配置**：管理员可以在后台调整每个VIP等级的升级订单数。

#### 3.1.2 VIP功能说明

- **核心功能**: VIP等级用于标识代理商级别。
- **权限功能**:
    - 管理员可以给所有用户设置VIP等级。
    - 代理商可以给子代理设置VIP等级（≤自己的等级）。
- **升级机制**: 订单完成后实时触发升级，仅基于订单数量立即自动升级，管理员可调整升级参数。
- **等级保护**: VIP等级原则上只升不降。

### 3.2 权限管理体系

#### 3.2.1 权限层级设计

```
超级管理员
├── VIP配置管理权限
├── 所有用户VIP设置权限
├── VIP升级规则配置权限

代理商
├── 子代理VIP设置权限(≤自己等级)

普通代理商
└── 个人VIP信息查看权限
```

#### 3.2.2 权限控制实现

```java
public void setSubAgentVipLevel(String subAgentCode, Integer vipLevel) {
    // 检查设置权限
    if (vipLevel > currentUser.getVipLevel()) {
        throw new BusinessException("不能设置高于自己等级的VIP");
    }
    // 执行设置操作
}
```

## 四、数据库设计

### 4.1 新增数据表

#### 4.1.1 VIP配置表

```sql
CREATE TABLE `t_vip_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `vip_level` int NOT NULL COMMENT 'VIP等级',
  `level_name` varchar(50) NOT NULL COMMENT '等级名称',
  `required_orders` int DEFAULT '0' COMMENT '升级所需订单数',
  `fixed_commission` int DEFAULT '0' COMMENT 'VIP固定佣金加成（单位：元）',
  `level_icon` varchar(200) DEFAULT NULL COMMENT '等级图标',
  `is_enabled` tinyint DEFAULT '1' COMMENT '是否启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_vip_level` (`vip_level`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='VIP配置表';
```

**说明**：
- `vip_level`: VIP等级（0-5），唯一索引
- `required_orders`: 升级到该等级所需的订单数量
- `fixed_commission`: VIP固定佣金加成（单位：元）
- `is_enabled`: 是否启用该等级配置

#### 4.1.2 VIP操作日志表

```sql
CREATE TABLE `t_vip_operation_log` (
  `log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` bigint NOT NULL COMMENT '操作用户ID',
  `target_agent_code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '目标代理商编码',
  `operation_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作类型(SET_LEVEL设置等级/UPGRADE升级等)',
  `method_name` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '执行方法名',
  `request_params` text COLLATE utf8mb4_unicode_ci COMMENT '请求参数(JSON格式)',
  `result` text COLLATE utf8mb4_unicode_ci COMMENT '执行结果',
  `ip_address` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'IP地址',
  `user_agent` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户代理',
  `operation_time` bigint DEFAULT NULL COMMENT '操作时间(毫秒时间戳)',
  `error_message` text COLLATE utf8mb4_unicode_ci COMMENT '错误信息',
  `execution_time` int DEFAULT '0' COMMENT '执行时间(毫秒)',
  `status` tinyint DEFAULT '1' COMMENT '操作状态(1成功 0失败)',
  PRIMARY KEY (`log_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_target_agent_code` (`target_agent_code`),
  KEY `idx_operation_type` (`operation_type`),
  KEY `idx_operation_time` (`operation_time`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='VIP操作日志表';
```

**说明**：
- `log_id`: 主键，日志ID
- `method_name`: 执行的Java方法名，便于追踪具体操作
- `request_params`: 请求参数的JSON格式记录
- `result`: 执行结果的JSON格式记录
- `operation_time`: 直接使用bigint类型存储毫秒时间戳
- `execution_time`: 方法执行耗时（毫秒）
- `status`: 1表示成功，0表示失败

```sql
CREATE TABLE `t_vip_upgrade_log`
(
    `id`             bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id`        bigint NOT NULL COMMENT '用户ID(sys_user表ID)',
    `agent_code`     varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '代理商编码',
    `from_level`     int                                     DEFAULT '0' COMMENT '原VIP等级',
    `to_level`       int                                     DEFAULT '0' COMMENT '新VIP等级',
    `upgrade_type`   varchar(20) COLLATE utf8mb4_unicode_ci  DEFAULT 'AUTO' COMMENT '升级类型(AUTO自动升级/MANUAL手动升级)',
    `upgrade_reason` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '升级原因',
    `order_count`    int                                     DEFAULT '0' COMMENT '升级时订单数量',
    `operator_id`    bigint                                  DEFAULT NULL COMMENT '操作人ID(手动升级时记录)',
    `operator_name`  varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '操作人姓名',
    `create_time`    bigint                                  DEFAULT NULL COMMENT '创建时间(毫秒时间戳)',
    PRIMARY KEY (`id`),
    KEY              `idx_user_id` (`user_id`),
    KEY              `idx_agent_code` (`agent_code`),
    KEY              `idx_from_level` (`from_level`),
    KEY              `idx_to_level` (`to_level`),
    KEY              `idx_upgrade_type` (`upgrade_type`),
    KEY              `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='VIP升级日志表';
```

