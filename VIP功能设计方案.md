# 叮咚号卡系统VIP功能模块设计方案

## 一、项目背景

基于现有叮咚号卡订单管理系统，客户希望增加VIP功能模块，通过VIP等级体系实现差异化佣金配置，提升代理商积极性和平台收益。参照客户提供的功能界面设计，实现完整的VIP等级管理体系。

## 二、系统现状分析

### 2.1 现有系统架构
- **技术栈**: RuoYi v3.8.8 + Spring Boot 2.5.15 + Vue.js 2.7.16
- **数据库**: MySQL + MyBatis Plus 3.5.7 + Redis缓存
- **权限体系**: RBAC权限控制 + JWT认证
- **业务模块**: 代理商管理、订单处理、佣金计算、提现管理

### 2.2 现有佣金系统
- **佣金配置表**: `t_commission_config` 支持固定佣金和百分比佣金两种模式
- **佣金计算**: 基于`CommissionConfigServiceImpl.computeCommission()`实现
- **佣金分配**: 支持多级代理商佣金分配机制
- **佣金状态**: 未结算→待结算→部分结算→已结算→无法结算

### 2.3 代理商体系
- **多级代理**: 支持父子代理商层级关系
- **实名认证**: 未认证→认证中→已认证→认证失败
- **数据权限**: 基于代理层级的数据访问控制
- **状态管理**: 启用/禁用代理商状态控制

## 三、VIP功能需求分析

### 3.1 核心需求
1. **VIP等级体系**: 设置多个VIP等级，等级越高佣金比例越高
2. **差异化佣金**: 不同VIP等级享受不同的佣金配置
3. **权限管理**: 管理员可设置所有用户VIP等级，代理商可设置子代理VIP等级
4. **订单触发升级**: 订单数量达到条件后立即自动升级VIP等级
5. **实时升级**: 订单完成后实时检查并执行升级

### 3.2 业务规则
1. **VIP等级设置**: 管理员可以给所有用户设置VIP等级
2. **代理权限**: 代理商可以给自己的子代理设置VIP等级，但不能高于自己的等级
3. **佣金差异化**: 根据VIP等级设置不同的佣金比例和固定佣金
4. **升级机制**: 订单完成后实时检查，基于订单数量立即自动升级
5. **降级保护**: VIP等级原则上只升不降，保护用户体验

## 四、VIP功能模块设计

### 4.1 VIP等级体系设计

#### 4.1.1 VIP等级定义（固定金额加成模式）
```
VIP等级    等级名称    升级所需订单数    固定佣金加成    累计加成收益
VIP0       普通会员    0                0元            0元
VIP1       铜牌会员    10               5元            5元
VIP2       银牌会员    50               10元           10元
VIP3       金牌会员    200              15元           15元
VIP4       白金会员    500              20元           20元
VIP5       钻石会员    1000             25元           25元
```

**核心机制说明**：
- **固定加成**：VIP等级不再使用百分比，而是固定金额加成
- **差价收益**：上级代理商通过下级VIP等级差获得收益
- **升级条件**：仅基于订单数量，不涉及佣金金额
- **可配置**：管理员可以在后台调整每个VIP等级的固定加成金额和升级订单数

**佣金分配示例**：
```
产品利润100元：
- 三级代理商zhao(VIP3)：获得 100 + 15 = 115元
- 二级代理商li(VIP2)：获得 (100 + 10) - (100 + 15) = -5元 → 0元（上级VIP等级更高时下级无收益）
- 一级代理商qian(VIP1)：获得 (100 + 10) - (100 + 5) = 5元

当li为VIP2，qian为VIP2时：
- 三级代理商zhao(VIP3)：获得 100 + 15 = 115元
- 二级代理商li(VIP2)：获得 (100 + 10) - (100 + 15) = -5元 → 0元
- 一级代理商qian(VIP2)：获得 (100 + 10) - (100 + 10) = 0元
```

#### 4.1.2 VIP功能说明
- **核心功能**: VIP等级影响代理商的固定佣金加成金额
- **权限功能**:
  - 管理员可以给所有用户设置VIP等级
  - 代理商可以给子代理设置VIP等级（≤自己的等级）
- **升级机制**: 订单完成后实时触发升级，仅基于订单数量立即自动升级，管理员可调整升级参数
- **等级保护**: VIP等级原则上只升不降
- **佣金计算**: 基础利润 + VIP固定加成，上级通过VIP等级差获得收益

### 4.2 差异化佣金配置

**重要说明**：移除原有的百分比佣金模式，`t_commission_config`表将不再使用，改为完全基于VIP等级的固定金额加成模式。

#### 4.2.1 新佣金计算模式
- **基础利润**：产品的基础利润金额
- **VIP固定加成**：根据代理商VIP等级获得固定金额加成
- **多级分配**：上级代理商通过下级VIP等级差获得差价收益
- **收益保障**：当上级VIP等级≥下级时，下级无收益，避免负收益

#### 4.2.2 VIP固定加成计算逻辑
```java
public Integer computeVipCommission(String agentCode, Integer baseProfit) {
    // 1. 获取代理商VIP等级
    AgentAccount agent = getAgentByCode(agentCode);
    Integer agentVipLevel = agent.getVipLevel();

    // 2. 获取VIP配置
    VipConfig vipConfig = getVipConfigByLevel(agentVipLevel);
    Integer fixedBonus = (vipConfig != null) ? vipConfig.getFixedCommission() : 0;

    // 3. 计算最终收益：基础佣金 + VIP固定加成
    // 基础佣金可能与基础利润相同，也可能按比例计算
    Integer baseCommission = calculateBaseCommission(baseProfit);
    return baseCommission + fixedBonus;
}

/**
 * 计算基础佣金
 */
private Integer calculateBaseCommission(Integer baseProfit) {
    // 默认情况下，基础佣金等于基础利润
    // 可以根据业务需求添加其他计算逻辑，如按比例计算
    return baseProfit;
}

/**
 * 计算多级代理商佣金分配
 */
public List<AgentCommission> computeMultiLevelCommission(String orderAgentCode, Integer baseProfit) {
    List<AgentCommission> commissions = new ArrayList<>();

    // 1. 获取代理商链路（从当前代理商到顶级代理）
    List<AgentAccount> agentChain = getAgentChain(orderAgentCode);

    // 2. 从下往上计算每级收益
    for (int i = 0; i < agentChain.size(); i++) {
        AgentAccount currentAgent = agentChain.get(i);
        VipConfig currentVip = getVipConfigByLevel(currentAgent.getVipLevel());

        // 计算基础佣金
        Integer baseCommission = calculateBaseCommission(baseProfit);
        Integer currentAgentTotal = baseCommission + (currentVip != null ? currentVip.getFixedCommission() : 0);
        Integer nextAgentTotal = 0;

        // 查找下级代理商的VIP加成（用于计算差价）
        if (i > 0) {
            AgentAccount nextAgent = agentChain.get(i - 1);
            VipConfig nextVip = getVipConfigByLevel(nextAgent.getVipLevel());
            nextAgentTotal = baseCommission + (nextVip != null ? nextVip.getFixedCommission() : 0);
        }

        // 计算当前代理商收益（下级总价 - 当前级总价）
        Integer commission = nextAgentTotal - currentAgentTotal;

        // 收益不能为负数
        commission = Math.max(commission, 0);

        if (commission > 0) {
            AgentCommission agentCommission = new AgentCommission();
            agentCommission.setAgentCode(currentAgent.getAgentCode());
            agentCommission.setVipLevel(currentAgent.getVipLevel());
            agentCommission.setBaseProfit(baseProfit);
            agentCommission.setBaseCommission(calculateBaseCommission(baseProfit));
            agentCommission.setVipBonus(currentVip != null ? currentVip.getFixedCommission() : 0);
            agentCommission.setFinalCommission(currentAgentTotal);
            agentCommission.setActualCommission(commission);
            agentCommission.setAgentLevel(i);

            commissions.add(agentCommission);
        }
    }

    return commissions;
}
```

### 4.3 权限管理体系

#### 4.3.1 权限层级设计
```
超级管理员
├── VIP配置管理权限
├── 所有用户VIP设置权限
├── VIP升级规则配置权限
└── VIP数据统计权限

代理商(VIP3及以上)
├── 子代理VIP设置权限(≤自己等级)
└── 团队VIP数据查看权限

普通代理商
└── 个人VIP信息查看权限
```

#### 4.3.2 权限控制实现
```java
// VIP等级权限检查注解
@PreVipLevel(minLevel = 3, message = "需要VIP3及以上等级")
public void setSubAgentVipLevel(String subAgentCode, Integer vipLevel) {
    // 检查设置权限
    if (vipLevel > currentUser.getVipLevel()) {
        throw new BusinessException("不能设置高于自己等级的VIP");
    }
    // 执行设置操作
}
```

## 五、数据库设计

### 5.1 新增数据表

#### 5.1.1 VIP配置表（固定金额模式）
```sql
CREATE TABLE t_vip_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    vip_level INT NOT NULL COMMENT 'VIP等级',
    level_name VARCHAR(50) NOT NULL COMMENT '等级名称',
    required_orders INT DEFAULT 0 COMMENT '升级所需订单数',
    fixed_commission INT DEFAULT 0 COMMENT 'VIP固定佣金加成（单位：元）',
    level_icon VARCHAR(200) COMMENT '等级图标',
    is_enabled TINYINT DEFAULT 1 COMMENT '是否启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注',
    UNIQUE KEY uk_vip_level (vip_level)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='VIP配置表';

-- 初始化VIP等级数据
INSERT INTO t_vip_config (vip_level, level_name, required_orders, fixed_commission, remark) VALUES
(0, '普通会员', 0, 0, '基础等级，无加成'),
(1, '铜牌会员', 10, 5, '10单升级，固定加成5元'),
(2, '银牌会员', 50, 10, '50单升级，固定加成10元'),
(3, '金牌会员', 200, 15, '200单升级，固定加成15元'),
(4, '白金会员', 500, 20, '500单升级，固定加成20元'),
(5, '钻石会员', 1000, 25, '1000单升级，固定加成25元');
```

#### 5.1.2 说明：使用现有t_agent_account表
不需要新增用户VIP记录表，直接在现有的 `t_agent_account` 表中添加VIP相关字段即可，避免数据冗余。

#### 5.1.3 VIP升级日志表
```sql
CREATE TABLE t_vip_upgrade_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    agent_code VARCHAR(50) COMMENT '代理商编码',
    from_level INT DEFAULT 0 COMMENT '原等级',
    to_level INT DEFAULT 0 COMMENT '新等级',
    upgrade_type VARCHAR(20) DEFAULT 'AUTO' COMMENT '升级类型(AUTO/MANUAL)',
    upgrade_reason VARCHAR(500) COMMENT '升级原因',
    operator_id BIGINT COMMENT '操作人ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY idx_user_id (user_id),
    KEY idx_agent_code (agent_code),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='VIP升级日志表';
```

#### 5.1.4 扩展现有订单佣金表
**说明**：不新增表，直接扩展现有的 `t_order_commission` 和 `t_order_commission_details` 表来支持VIP功能。

```sql
-- 扩展订单佣金主表 t_order_commission
ALTER TABLE t_order_commission
ADD COLUMN agent_vip_level TINYINT DEFAULT 0 COMMENT '代理商VIP等级(0-5)',
ADD COLUMN base_profit INT DEFAULT 0 COMMENT '基础利润（单位：分）',
ADD COLUMN base_commission INT DEFAULT 0 COMMENT '基础佣金（单位：分）',
ADD COLUMN vip_bonus INT DEFAULT 0 COMMENT 'VIP加成金额（单位：分）',
ADD COLUMN total_commission INT DEFAULT 0 COMMENT '总佣金（基础佣金+VIP加成，单位：分）',
ADD COLUMN vip_calculation_time DATETIME DEFAULT NULL COMMENT 'VIP计算时间',
ADD COLUMN commission_calc_type VARCHAR(20) DEFAULT 'VIP' COMMENT '佣金计算类型(BASE/VIP)';

-- 扩展订单佣金详情表 t_order_commission_details
ALTER TABLE t_order_commission_details
ADD COLUMN agent_vip_level TINYINT DEFAULT 0 COMMENT '代理商VIP等级(0-5)',
ADD COLUMN agent_level TINYINT DEFAULT 0 COMMENT '代理商级别（0=直接代理商，1=上级，2=上上级）',
ADD COLUMN base_profit INT DEFAULT 0 COMMENT '基础利润（单位：分）',
ADD COLUMN base_commission INT DEFAULT 0 COMMENT '基础佣金（单位：分）',
ADD COLUMN vip_bonus INT DEFAULT 0 COMMENT 'VIP加成金额（单位：分）',
ADD COLUMN total_commission INT DEFAULT 0 COMMENT '总佣金（基础佣金+VIP加成，单位：分）',
ADD COLUMN actual_commission INT DEFAULT 0 COMMENT '实际获得佣金（差价收益，单位：分）',
ADD COLUMN parent_agent_code VARCHAR(50) DEFAULT NULL COMMENT '上级代理商编码',
ADD COLUMN vip_calculation_result TEXT DEFAULT NULL COMMENT 'VIP计算结果详情(JSON格式)';

-- 添加VIP相关索引，优化查询性能
ALTER TABLE t_order_commission
ADD INDEX idx_agent_vip_level (agent_vip_level),
ADD INDEX idx_vip_calculation_time (vip_calculation_time),
ADD INDEX idx_commission_calc_type (commission_calc_type);

ALTER TABLE t_order_commission_details
ADD INDEX idx_agent_vip_level (agent_vip_level),
ADD INDEX idx_agent_level (agent_level),
ADD INDEX idx_parent_agent_code (parent_agent_code),
ADD INDEX idx_actual_commission (actual_commission);

-- 添加数据完整性约束
ALTER TABLE t_order_commission
ADD CONSTRAINT chk_agent_vip_level CHECK (agent_vip_level >= 0 AND agent_vip_level <= 5),
ADD CONSTRAINT chk_commission_calc_type CHECK (commission_calc_type IN ('BASE', 'VIP'));

ALTER TABLE t_order_commission_details
ADD CONSTRAINT chk_details_agent_vip_level CHECK (agent_vip_level >= 0 AND agent_vip_level <= 5),
ADD CONSTRAINT chk_agent_level CHECK (agent_level >= 0 AND agent_level <= 2);
```

### 5.2 修改现有表

#### 5.2.1 扩展代理商账号表（核心VIP字段）
```sql
-- 扩展代理商账号表，添加VIP相关字段
ALTER TABLE t_agent_account
ADD COLUMN vip_level TINYINT DEFAULT 0 COMMENT 'VIP等级(0-5)',
ADD COLUMN previous_vip_level TINYINT DEFAULT 0 COMMENT '上一VIP等级',
ADD COLUMN vip_upgrade_time DATETIME DEFAULT NULL COMMENT 'VIP升级时间',
ADD COLUMN total_orders INT DEFAULT 0 COMMENT '总订单数（用于VIP升级判断）',
ADD COLUMN total_commission BIGINT DEFAULT 0 COMMENT '总佣金收益（单位：分）',
ADD COLUMN vip_effective_time DATETIME DEFAULT NULL COMMENT 'VIP等级生效时间',
ADD COLUMN vip_expire_time DATETIME DEFAULT NULL COMMENT 'VIP等级过期时间(预留)';

-- 添加VIP相关索引，提升查询性能
ALTER TABLE t_agent_account
ADD INDEX idx_vip_level (vip_level),
ADD INDEX idx_vip_upgrade_time (vip_upgrade_time),
ADD INDEX idx_total_orders (total_orders);

-- 添加VIP等级约束，确保数据完整性
ALTER TABLE t_agent_account
ADD CONSTRAINT chk_vip_level CHECK (vip_level >= 0 AND vip_level <= 5),
ADD CONSTRAINT chk_previous_vip_level CHECK (previous_vip_level >= 0 AND previous_vip_level <= 5);
```

#### 5.2.2 移除原有佣金配置表
```sql
-- 说明：原有t_commission配置表将不再使用，保留数据作为历史记录
-- ALTER TABLE t_commission_config DROP COLUMN commission_config_type;
-- ALTER TABLE t_commission_config DROP COLUMN scale_comment;
-- 实际使用中完全基于VIP等级计算佣金，不再使用此表
```

#### 5.2.3 扩展订单表支持VIP佣金
```sql
-- 在订单表中添加VIP相关字段，便于查询和统计
ALTER TABLE t_order
ADD COLUMN agent_vip_level INT DEFAULT 0 COMMENT '代理商VIP等级',
ADD COLUMN base_profit INT DEFAULT 0 COMMENT '基础利润（单位：分）',
ADD COLUMN vip_bonus INT DEFAULT 0 COMMENT 'VIP加成金额（单位：分）';
```

#### 5.2.3 数据迁移和兼容性策略

```sql
-- 数据迁移脚本：为现有代理商初始化VIP数据
UPDATE t_agent_account
SET
    vip_level = 0,
    previous_vip_level = 0,
    total_orders = 0,
    total_commission = 0,
    vip_effective_time = NOW()
WHERE vip_level IS NULL;

-- 数据迁移脚本：为现有订单佣金表初始化VIP字段
UPDATE t_order_commission
SET
    agent_vip_level = 0,
    base_profit = product_commission,
    vip_bonus = 0,
    total_commission = product_commission,
    vip_calculation_time = create_time,
    commission_calc_type = 'BASE'
WHERE agent_vip_level IS NULL;

UPDATE t_order_commission_details
SET
    agent_vip_level = 0,
    agent_level = 0,
    base_profit = product_commission,
    vip_bonus = 0,
    total_commission = product_commission,
    actual_commission = revenue_product_commission,
    parent_agent_code = agent_source_code
WHERE agent_vip_level IS NULL;

-- 创建数据备份表（可选）
CREATE TABLE t_agent_account_backup_vip AS
SELECT * FROM t_agent_account WHERE 1=0;

CREATE TABLE t_order_commission_backup_vip AS
SELECT * FROM t_order_commission WHERE 1=0;

CREATE TABLE t_order_commission_details_backup_vip AS
SELECT * FROM t_order_commission_details WHERE 1=0;
```

#### 5.2.4 性能优化索引策略

```sql
-- 为高频查询场景创建复合索引
-- 代理商VIP等级和订单时间组合查询
CREATE INDEX idx_agent_vip_order_time ON t_order_commission(agent_vip_level, create_time);

-- 代理商VIP等级和实际佣金额度组合查询
CREATE INDEX idx_vip_actual_commission ON t_order_commission_details(agent_vip_level, actual_commission);

-- 代理商编码和VIP等级组合查询
CREATE INDEX idx_agent_code_vip_level ON t_agent_account(agent_code, vip_level);

-- 为统计分析创建汇总索引
CREATE INDEX idx_vip_statistics ON t_order_commission_details(agent_vip_level, create_time, actual_commission);
```

### 5.3 RuoYi菜单权限配置

#### 5.3.1 VIP功能主菜单配置
```sql
-- 插入VIP功能主菜单
INSERT INTO `sys_menu` VALUES
(NULL, 'VIP管理', '0', '8', 'vip', NULL, '', 1, 0, 'M', '0', '0', '', 'vip', 'admin', NOW(), '', NULL, 'VIP功能主菜单');

-- 获取刚插入的主菜单ID（假设为 2000）
SET @vip_main_menu_id = LAST_INSERT_ID();
```

#### 5.3.2 VIP配置管理菜单
```sql
-- VIP配置管理菜单
INSERT INTO `sys_menu` VALUES
(NULL, 'VIP配置管理', @vip_main_menu_id, '1', 'config', 'vip/config/index', '', 1, 0, 'C', '0', '0', 'vip:config:list', 'list', 'admin', NOW(), '', NULL, 'VIP配置管理菜单');

-- 获取VIP配置管理菜单ID
SET @vip_config_menu_id = LAST_INSERT_ID();

-- VIP配置管理权限按钮
INSERT INTO `sys_menu` VALUES
(NULL, 'VIP配置查询', @vip_config_menu_id, '1', 'query', '#', '', 1, 0, 'F', '0', '0', 'vip:config:query', '#', 'admin', NOW(), '', NULL, 'VIP配置查询权限'),
(NULL, 'VIP配置新增', @vip_config_menu_id, '2', 'add', '#', '', 1, 0, 'F', '0', '0', 'vip:config:add', '#', 'admin', NOW(), '', NULL, 'VIP配置新增权限'),
(NULL, 'VIP配置修改', @vip_config_menu_id, '3', 'edit', '#', '', 1, 0, 'F', '0', '0', 'vip:config:edit', '#', 'admin', NOW(), '', NULL, 'VIP配置修改权限'),
(NULL, 'VIP配置删除', @vip_config_menu_id, '4', 'remove', '#', '', 1, 0, 'F', '0', '0', 'vip:config:remove', '#', 'admin', NOW(), '', NULL, 'VIP配置删除权限'),
(NULL, 'VIP配置导出', @vip_config_menu_id, '5', 'export', '#', '', 1, 0, 'F', '0', '0', 'vip:config:export', '#', 'admin', NOW(), '', NULL, 'VIP配置导出权限');
```

#### 5.3.3 VIP用户管理菜单
```sql
-- VIP用户管理菜单
INSERT INTO `sys_menu` VALUES
(NULL, 'VIP用户管理', @vip_main_menu_id, '2', 'user', 'vip/user/index', '', 1, 0, 'C', '0', '0', 'vip:user:list', 'user', 'admin', NOW(), '', NULL, 'VIP用户管理菜单');

-- 获取VIP用户管理菜单ID
SET @vip_user_menu_id = LAST_INSERT_ID();

-- VIP用户管理权限按钮
INSERT INTO `sys_menu` VALUES
(NULL, 'VIP用户查询', @vip_user_menu_id, '1', 'query', '#', '', 1, 0, 'F', '0', '0', 'vip:user:query', '#', 'admin', NOW(), '', NULL, 'VIP用户查询权限'),
(NULL, 'VIP用户新增', @vip_user_menu_id, '2', 'add', '#', '', 1, 0, 'F', '0', '0', 'vip:user:add', '#', 'admin', NOW(), '', NULL, 'VIP用户新增权限'),
(NULL, 'VIP用户修改', @vip_user_menu_id, '3', 'edit', '#', '', 1, 0, 'F', '0', '0', 'vip:user:edit', '#', 'admin', NOW(), '', NULL, 'VIP用户修改权限'),
(NULL, 'VIP用户删除', @vip_user_menu_id, '4', 'remove', '#', '', 1, 0, 'F', '0', '0', 'vip:user:remove', '#', 'admin', NOW(), '', NULL, 'VIP用户删除权限'),
(NULL, 'VIP用户导出', @vip_user_menu_id, '5', 'export', '#', '', 1, 0, 'F', '0', '0', 'vip:user:export', '#', 'admin', NOW(), '', NULL, 'VIP用户导出权限'),
(NULL, '设置VIP等级', @vip_user_menu_id, '6', 'setLevel', '#', '', 1, 0, 'F', '0', '0', 'vip:user:setLevel', '#', 'admin', NOW(), '', NULL, '设置VIP等级权限'),
(NULL, '升级日志查询', @vip_user_menu_id, '7', 'upgradeLog', '#', '', 1, 0, 'F', '0', '0', 'vip:user:upgradeLog', '#', 'admin', NOW(), '', NULL, '升级日志查询权限');
```

#### 5.3.4 VIP功能增强现有菜单

VIP功能不需要创建独立的佣金管理和数据分析菜单，而是作为现有功能的增强：

**VIP与现有佣金管理的关系**：
- VIP佣金计算集成到现有的订单处理流程中
- VIP相关信息在现有的佣金管理界面中展示
- VIP等级影响体现在佣金明细和统计报表中

**VIP权限分配建议**：
- VIP配置管理权限：`vip:config:*` 系列权限
- VIP等级设置权限：`vip:agent:setLevel` 权限
- 升级日志查看权限：`vip:agent:upgradeLog` 权限

**权限集成到现有菜单**：
```sql
-- 为现有佣金管理菜单添加VIP相关权限（可选）
-- 如果需要在现有佣金管理中查看VIP相关信息，可以添加以下权限
INSERT INTO `sys_role_menu` (role_id, menu_id)
SELECT 1, menu_id FROM `sys_menu`
WHERE menu_name IN ('佣金管理', '订单管理')
AND role_id = 1; -- 管理员角色
```

#### 5.3.5 管理员角色权限分配
```sql
-- 为超级管理员角色分配VIP功能权限（假设管理员角色ID为 1）
INSERT INTO `sys_role_menu` (role_id, menu_id)
SELECT 1, menu_id FROM `sys_menu` WHERE menu_name LIKE '%VIP%' OR menu_name LIKE '%vip%' OR menu_name LIKE '%配置%' OR menu_name LIKE '%用户%';

-- 为VIP管理员角色分配权限（如果存在该角色，假设角色ID为 2）
INSERT INTO `sys_role_menu` (role_id, menu_id)
SELECT 2, menu_id FROM `sys_menu` WHERE menu_name LIKE '%VIP%' OR menu_name LIKE '%vip%' OR menu_name LIKE '%配置%' OR menu_name LIKE '%用户%';
```

#### 5.3.6 字典数据配置
```sql
-- VIP等级字典类型
INSERT INTO `sys_dict_type` VALUES
(NULL, 'VIP等级', 'vip_level', '0', 'admin', NOW(), '', NULL, 'VIP等级字典');

-- 获取字典类型ID
SET @vip_level_dict_type = LAST_INSERT_ID();

-- VIP等级字典数据
INSERT INTO `sys_dict_data` VALUES
(NULL, 1, '普通会员', '0', 'vip_level', '', 'primary', 'N', '0', 'admin', NOW(), '', NULL, 'VIP等级0'),
(NULL, 2, '铜牌会员', '1', 'vip_level', '', 'success', 'N', '0', 'admin', NOW(), '', NULL, 'VIP等级1'),
(NULL, 3, '银牌会员', '2', 'vip_level', '', 'info', 'N', '0', 'admin', NOW(), '', NULL, 'VIP等级2'),
(NULL, 4, '金牌会员', '3', 'vip_level', '', 'warning', 'N', '0', 'admin', NOW(), '', NULL, 'VIP等级3'),
(NULL, 5, '白金会员', '4', 'vip_level', '', 'danger', 'N', '0', 'admin', NOW(), '', NULL, 'VIP等级4'),
(NULL, 6, '钻石会员', '5', 'vip_level', '', 'primary', 'N', '0', 'admin', NOW(), '', NULL, 'VIP等级5');

-- VIP升级类型字典
INSERT INTO `sys_dict_type` VALUES
(NULL, 'VIP升级类型', 'vip_upgrade_type', '0', 'admin', NOW(), '', NULL, 'VIP升级类型字典');

-- 获取字典类型ID
SET @vip_upgrade_dict_type = LAST_INSERT_ID();

-- VIP升级类型字典数据
INSERT INTO `sys_dict_data` VALUES
(NULL, 1, '自动升级', 'AUTO', 'vip_upgrade_type', '', 'primary', 'N', '0', 'admin', NOW(), '', NULL, '自动升级'),
(NULL, 2, '手动升级', 'MANUAL', 'vip_upgrade_type', '', 'success', 'N', '0', 'admin', NOW(), '', NULL, '手动升级');
```

## 六、后端实现方案

### 6.1 实体类设计

#### 6.1.1 AgentAccount实体类VIP字段扩展
```java
/**
 * 代理商账号表 - VIP字段扩展
 */
@TableName(value = "t_agent_account", autoResultMap = true)
@Data
public class AgentAccount {
    // ... 现有字段 ...

    /**
     * VIP等级(0-5)
     */
    @ApiModelProperty(value = "VIP等级(0-5)")
    private Integer vipLevel;

    /**
     * 上一VIP等级
     */
    @ApiModelProperty(value = "上一VIP等级")
    private Integer previousVipLevel;

    /**
     * VIP升级时间
     */
    @ApiModelProperty(value = "VIP升级时间")
    private Date vipUpgradeTime;

    /**
     * VIP等级生效时间
     */
    @ApiModelProperty(value = "VIP等级生效时间")
    private Date vipEffectiveTime;

    /**
     * VIP等级过期时间(预留)
     */
    @ApiModelProperty(value = "VIP等级过期时间(预留)")
    private Date vipExpireTime;

    /**
     * 总订单数（用于VIP升级判断）
     */
    @ApiModelProperty(value = "总订单数")
    private Integer totalOrders;

    /**
     * 总佣金收益（单位：分）
     */
    @ApiModelProperty(value = "总佣金收益")
    private Long totalCommission;

    // VIP相关方法
    public boolean canUpgradeTo(Integer targetLevel) {
        return targetLevel != null && targetLevel > this.vipLevel && targetLevel <= 5;
    }

    public boolean isVipEffective() {
        Date now = new Date();
        return vipEffectiveTime != null && vipEffectiveTime.before(now) &&
               (vipExpireTime == null || vipExpireTime.after(now));
    }

    public String getVipLevelName() {
        String[] levelNames = {"普通会员", "铜牌会员", "银牌会员", "金牌会员", "白金会员", "钻石会员"};
        return levelNames[this.vipLevel != null ? this.vipLevel : 0];
    }
}
```

#### 6.1.2 OrderCommission实体类VIP字段扩展
```java
/**
 * 订单佣金表 - VIP字段扩展
 */
@TableName(value = "t_order_commission", autoResultMap = true)
@Data
public class OrderCommission {
    // ... 现有字段 ...

    /**
     * 代理商VIP等级(0-5)
     */
    @ApiModelProperty(value = "代理商VIP等级")
    private Integer agentVipLevel;

    /**
     * 基础利润（单位：分）
     */
    @ApiModelProperty(value = "基础利润")
    private Integer baseProfit;

    /**
     * 基础佣金（单位：分）
     */
    @ApiModelProperty(value = "基础佣金")
    private Integer baseCommission;

    /**
     * VIP加成金额（单位：分）
     */
    @ApiModelProperty(value = "VIP加成金额")
    private Integer vipBonus;

    /**
     * 总佣金（基础佣金+VIP加成，单位：分）
     */
    @ApiModelProperty(value = "总佣金")
    private Integer totalCommission;

    /**
     * VIP计算时间
     */
    @ApiModelProperty(value = "VIP计算时间")
    private Date vipCalculationTime;

    /**
     * 佣金计算类型(BASE/VIP)
     */
    @ApiModelProperty(value = "佣金计算类型")
    private String commissionCalcType;

    // VIP相关方法
    public boolean hasVipBonus() {
        return vipBonus != null && vipBonus > 0;
    }

    public Integer getActualCommission() {
        return totalCommission != null ? totalCommission : productCommission;
    }
}
```

#### 6.1.3 OrderCommissionDetails实体类VIP字段扩展
```java
/**
 * 订单佣金详情表 - VIP字段扩展
 */
@TableName(value = "t_order_commission_details", autoResultMap = true)
@Data
public class OrderCommissionDetails {
    // ... 现有字段 ...

    /**
     * 代理商VIP等级(0-5)
     */
    @ApiModelProperty(value = "代理商VIP等级")
    private Integer agentVipLevel;

    /**
     * 代理商级别（0=直接代理商，1=上级，2=上上级）
     */
    @ApiModelProperty(value = "代理商级别")
    private Integer agentLevel;

    /**
     * 基础利润（单位：分）
     */
    @ApiModelProperty(value = "基础利润")
    private Integer baseProfit;

    /**
     * 基础佣金（单位：分）
     */
    @ApiModelProperty(value = "基础佣金")
    private Integer baseCommission;

    /**
     * VIP加成金额（单位：分）
     */
    @ApiModelProperty(value = "VIP加成金额")
    private Integer vipBonus;

    /**
     * 总佣金（基础佣金+VIP加成，单位：分）
     */
    @ApiModelProperty(value = "总佣金")
    private Integer totalCommission;

    /**
     * 实际获得佣金（差价收益，单位：分）
     */
    @ApiModelProperty(value = "实际获得佣金")
    private Integer actualCommission;

    /**
     * 上级代理商编码
     */
    @ApiModelProperty(value = "上级代理商编码")
    private String parentAgentCode;

    /**
     * VIP计算结果详情(JSON格式)
     */
    @ApiModelProperty(value = "VIP计算结果详情")
    private String vipCalculationResult;

    // VIP相关方法
    public boolean hasVipBonus() {
        return vipBonus != null && vipBonus > 0;
    }

    public String getAgentLevelName() {
        String[] levelNames = {"直接代理商", "上级代理", "上上级代理"};
        return levelNames[agentLevel != null ? agentLevel : 0];
    }

    public boolean isValidCommission() {
        return actualCommission != null && actualCommission >= 0;
    }
}
```

### 6.2 核心服务类设计

#### 6.2.1 VIP配置服务
```java
@Service
public class VipConfigService {

    /**
     * 获取VIP配置列表
     */
    public List<VipConfig> getVipConfigList() {
        return vipConfigMapper.selectList(new QueryWrapper<VipConfig>()
            .orderByAsc("vip_level"));
    }

    /**
     * 根据等级获取VIP配置
     */
    public VipConfig getVipConfigByLevel(Integer vipLevel) {
        return vipConfigMapper.selectOne(new QueryWrapper<VipConfig>()
            .eq("vip_level", vipLevel)
            .eq("is_enabled", 1));
    }

    /**
     * 更新VIP配置
     */
    @Transactional
    public int updateVipConfig(VipConfig vipConfig) {
        return vipConfigMapper.updateById(vipConfig);
    }
}
```

#### 6.1.2 代理商VIP服务（基于AgentAccount）
```java
@Service
public class AgentVipService {

    @Autowired
    private AgentAccountMapper agentAccountMapper;

    @Autowired
    private VipUpgradeLogMapper vipUpgradeLogMapper;

    /**
     * 获取代理商VIP信息
     */
    public AgentAccount getAgentVipInfo(String agentCode) {
        AgentAccount agent = agentAccountMapper.selectOne(new QueryWrapper<AgentAccount>()
            .eq("agent_code", agentCode));

        if (agent == null) {
            throw new BusinessException("代理商不存在");
        }

        // 如果VIP字段为null，初始化默认值
        if (agent.getVipLevel() == null) {
            agent.setVipLevel(0);
            agent.setPreviousVipLevel(0);
            agent.setTotalOrders(0);
            agent.setTotalCommission(0L);
            agentAccountMapper.updateById(agent);
        }

        return agent;
    }

    /**
     * 设置代理商VIP等级
     */
    @Transactional
    public void setAgentVipLevel(String agentCode, Integer vipLevel, String reason, Long operatorId) {
        AgentAccount agent = getAgentVipInfo(agentCode);
        Integer oldLevel = agent.getVipLevel();

        if (vipLevel.equals(oldLevel)) {
            return;
        }

        // 检查权限
        checkVipSetPermission(operatorId, vipLevel, agent.getVipLevel());

        // 更新VIP信息
        agent.setPreviousVipLevel(oldLevel);
        agent.setVipLevel(vipLevel);
        agent.setVipUpgradeTime(new Date());
        agentAccountMapper.updateById(agent);

        // 记录升级日志
        recordVipUpgradeLog(agent.getSysUserId(), agentCode, oldLevel, vipLevel, "MANUAL", reason, operatorId);
    }

    /**
     * 订单完成后检查VIP升级
     */
    @Transactional
    public void checkVipUpgradeAfterOrder(String agentCode, Integer orderCount) {
        AgentAccount agent = getAgentVipInfo(agentCode);

        // 更新订单数量
        agent.setTotalOrders(agent.getTotalOrders() + orderCount);
        agentAccountMapper.updateById(agent);

        // 检查是否可以升级
        checkAndUpgradeVip(agent);
    }

    /**
     * 检查并升级VIP（增强版，包含边界条件处理）
     */
    private void checkAndUpgradeVip(AgentAccount agent) {
        // 边界条件1：代理商状态检查
        if (agent.getIsEnabled() != null && agent.getIsEnabled() == 1) {
            logger.warn("代理商已被禁用，无法升级VIP：{}", agent.getAgentCode());
            return;
        }

        // 边界条件2：实名认证状态检查
        if (agent.getIsRealName() == null || agent.getIsRealName() != 1) {
            logger.warn("代理商未完成实名认证，VIP升级受限：{}", agent.getAgentCode());
            // 限制最高只能升级到VIP2
            if (agent.getVipLevel() >= 2) {
                return;
            }
        }

        Integer currentLevel = agent.getVipLevel();

        // 边界条件3：最高等级检查
        if (currentLevel >= 5) {
            return; // 已经是最高等级VIP5
        }

        VipConfig nextLevelConfig = getVipConfigByLevel(currentLevel + 1);
        if (nextLevelConfig == null) {
            logger.warn("VIP{}配置不存在，跳过升级检查", currentLevel + 1);
            return;
        }

        // 边界条件4：升级条件检查（仅基于订单数量）
        if (agent.getTotalOrders() >= nextLevelConfig.getRequiredOrders()) {
            // 边界条件5：防重复升级检查
            if (agent.getPreviousVipLevel() != null && agent.getPreviousVipLevel() >= currentLevel + 1) {
                logger.warn("代理商{}曾经达到VIP{}，无需重复升级", agent.getAgentCode(), currentLevel + 1);
                return;
            }

            // 边界条件6：升级频率限制（防止短时间内频繁升级）
            if (isUpgradeTooFrequent(agent)) {
                logger.warn("代理商{}升级过于频繁，暂时限制", agent.getAgentCode());
                return;
            }

            // 执行升级
            upgradeVipLevel(agent, currentLevel + 1, "订单数量达标自动升级");
        }
    }

    /**
     * 检查升级频率限制
     */
    private boolean isUpgradeTooFrequent(AgentAccount agent) {
        // 检查最近24小时内的升级次数
        List<VipUpgradeLog> recentUpgrades = vipUpgradeLogMapper.selectList(
            new QueryWrapper<VipUpgradeLog>()
                .eq("agent_code", agent.getAgentCode())
                .ge("create_time", System.currentTimeMillis() - 24 * 60 * 60 * 1000)
                .orderByDesc("create_time")
                .last("3")
        );

        return recentUpgrades.size() >= 3; // 24小时内最多升级3次
    }

    /**
     * 执行VIP升级
     */
    @Transactional
    private void upgradeVipLevel(AgentAccount agent, Integer newLevel, String reason) {
        Integer oldLevel = agent.getVipLevel();

        agent.setPreviousVipLevel(oldLevel);
        agent.setVipLevel(newLevel);
        agent.setVipUpgradeTime(new Date());
        agentAccountMapper.updateById(agent);

        // 记录升级日志
        recordVipUpgradeLog(agent.getSysUserId(), agent.getAgentCode(), oldLevel, newLevel, "AUTO", reason, null);
    }

    /**
     * 记录VIP升级日志
     */
    private void recordVipUpgradeLog(Long userId, String agentCode, Integer fromLevel, Integer toLevel,
                                    String upgradeType, String reason, Long operatorId) {
        VipUpgradeLog log = VipUpgradeLog.builder()
                .userId(userId)
                .agentCode(agentCode)
                .fromLevel(fromLevel)
                .toLevel(toLevel)
                .upgradeType(upgradeType)
                .upgradeReason(reason)
                .operatorId(operatorId)
                .createTime(new Date())
                .build();

        vipUpgradeLogMapper.insert(log);
    }

    /**
     * 检查VIP设置权限
     */
    private void checkVipSetPermission(Long operatorId, Integer targetVipLevel, Integer currentAgentVipLevel) {
        // 获取操作者信息
        AgentAccount operator = agentAccountMapper.selectOne(new QueryWrapper<AgentAccount>()
            .eq("sys_user_id", operatorId));

        if (operator == null) {
            throw new BusinessException("操作者不存在");
        }

        // 如果操作者不是管理员，检查VIP等级限制
        if (!isAdmin(operator)) {
            if (targetVipLevel > operator.getVipLevel()) {
                throw new BusinessException("不能设置高于自己等级的VIP");
            }
        }
    }

    private boolean isAdmin(AgentAccount agent) {
        // 实现管理员判断逻辑
        // 可以基于特定角色或等级判断
        return agent.getLevel() != null && agent.getLevel() == 0;
    }
}
```

#### 6.1.3 VIP固定加成佣金计算服务
```java
@Service
public class VipCommissionService {

    /**
     * 计算单代理商VIP佣金（基础利润 + 固定加成）
     */
    public VipCommissionResult calculateAgentVipCommission(String agentCode, Integer baseProfit) {
        // 1. 获取代理商VIP信息（直接使用AgentAccount）
        AgentAccount agent = agentVipService.getAgentVipInfo(agentCode);

        // 2. 获取VIP配置
        VipConfig vipConfig = vipConfigService.getByLevel(agent.getVipLevel());
        Integer fixedBonus = (vipConfig != null) ? vipConfig.getFixedCommission() : 0;

        // 3. 计算最终收益：基础利润 + VIP固定加成
        Integer totalCommission = baseProfit + fixedBonus;

        return VipCommissionResult.builder()
                .agentCode(agentCode)
                .vipLevel(agent.getVipLevel())
                .baseProfit(baseProfit)
                .vipBonus(fixedBonus)
                .totalCommission(totalCommission)
                .build();
    }

    /**
     * 计算多级代理商佣金分配（增强版，包含边界条件处理）
     * 实现上级代理商通过下级VIP等级差获得收益的逻辑
     */
    @Transactional
    public List<OrderVipCommission> calculateMultiLevelCommission(String orderAgentCode, Integer baseProfit, String orderNo) {
        List<OrderVipCommission> commissions = new ArrayList<>();

        try {
            // 边界条件1：基础利润检查
            if (baseProfit == null || baseProfit <= 0) {
                logger.warn("基础利润异常，无法计算VIP佣金：orderNo={}, baseProfit={}", orderNo, baseProfit);
                return commissions;
            }

            // 边界条件2：获取代理商链路（从当前代理商到顶级代理）
            List<AgentAccount> agentChain = getAgentChainWithValidation(orderAgentCode);
            if (agentChain.isEmpty()) {
                logger.warn("未找到有效的代理商链路：{}", orderAgentCode);
                return commissions;
            }

            // 边界条件3：限制最多3级代理
            if (agentChain.size() > 3) {
                logger.warn("代理商链路过长，截取前3级：{}", orderAgentCode);
                agentChain = agentChain.subList(0, 3);
            }

            // 2. 从下往上计算每级收益
            for (int i = 0; i < agentChain.size(); i++) {
                AgentAccount currentAgent = agentChain.get(i);

                // 边界条件4：代理商状态检查
                if (currentAgent.getIsEnabled() != null && currentAgent.getIsEnabled() == 1) {
                    logger.debug("代理商已被禁用，跳过佣金计算：{}", currentAgent.getAgentCode());
                    continue;
                }

                VipConfig currentVip = vipConfigService.getByLevel(currentAgent.getVipLevel());
                Integer currentVipBonus = (currentVip != null) ? currentVip.getFixedCommission() : 0;
                Integer currentTotal = baseProfit + currentVipBonus;
                Integer nextTotal = 0;

                // 查找下级代理商的VIP加成（用于计算差价）
                if (i > 0) {
                    AgentAccount nextAgent = agentChain.get(i - 1);
                    VipConfig nextVip = vipConfigService.getByLevel(nextAgent.getVipLevel());
                    Integer nextVipBonus = (nextVip != null) ? nextVip.getFixedCommission() : 0;
                    nextTotal = baseProfit + nextVipBonus;

                    // 边界条件5：VIP等级倒挂检查
                    if (currentAgent.getVipLevel() > nextAgent.getVipLevel()) {
                        logger.debug("上级VIP等级高于下级，下级无收益：{}({}) > {}({})",
                                currentAgent.getAgentCode(), currentAgent.getVipLevel(),
                                nextAgent.getAgentCode(), nextAgent.getVipLevel());
                    }
                }

                // 计算当前代理商收益（下级总价 - 当前级总价）
                Integer actualCommission = nextTotal - currentTotal;

                // 边界条件6：收益不能为负数
                actualCommission = Math.max(actualCommission, 0);

                // 边界条件7：直接代理商必须记录，上级代理商只有正收益才记录
                if (i == 0 || actualCommission > 0) {
                    OrderVipCommission commission = OrderVipCommission.builder()
                            .orderNo(orderNo)
                            .agentCode(currentAgent.getAgentCode())
                            .agentLevel(i)
                            .vipLevel(currentAgent.getVipLevel())
                            .baseProfit(baseProfit)
                            .vipBonus(currentVipBonus)
                            .totalCommission(currentTotal)
                            .actualCommission(actualCommission)
                            .parentAgentCode(i > 0 ? agentChain.get(i - 1).getAgentCode() : null)
                            .createTime(new Date())
                            .build();

                    commissions.add(commission);

                    // 边界条件8：计算结果验证
                    validateCommissionResult(commission);
                }
            }

            // 边界条件9：佣金分配合理性检查
            validateCommissionDistribution(commissions, baseProfit);

        } catch (Exception e) {
            logger.error("计算多级VIP佣金失败：orderNo={}, agentCode={}", orderNo, orderAgentCode, e);
            // 返回空结果，避免影响订单正常流程
            return new ArrayList<>();
        }

        return commissions;
    }

    /**
     * 获取代理商链路（包含状态验证）
     */
    private List<AgentAccount> getAgentChainWithValidation(String agentCode) {
        List<AgentAccount> chain = new ArrayList<>();
        String currentCode = agentCode;
        Set<String> visitedCodes = new HashSet<>(); // 防止循环引用

        // 向上查找最多3级代理商
        while (currentCode != null && chain.size() < 3 && !visitedCodes.contains(currentCode)) {
            AgentAccount agent = agentAccountMapper.selectOne(new QueryWrapper<AgentAccount>()
                    .eq("agent_code", currentCode));

            if (agent == null) {
                logger.warn("代理商不存在：{}", currentCode);
                break;
            }

            // 验证代理商状态
            if (agent.getIsEnabled() == null || agent.getIsEnabled() != 1) {
                chain.add(agent);
                visitedCodes.add(currentCode);
                currentCode = agent.getParentAgentCode();
            } else {
                logger.debug("代理商被禁用，停止向上查找：{}", currentCode);
                break;
            }
        }

        return chain;
    }

    /**
     * 验证佣金计算结果
     */
    private void validateCommissionResult(OrderVipCommission commission) {
        if (commission.getActualCommission() < 0) {
            logger.error("佣金计算结果异常：实际佣金为负数 {}", commission);
            throw new BusinessException("佣金计算结果异常");
        }

        if (commission.getTotalCommission() < commission.getVipBonus()) {
            logger.error("佣金计算结果异常：总佣金小于VIP加成 {}", commission);
            throw new BusinessException("佣金计算结果异常");
        }
    }

    /**
     * 验证佣金分配的合理性
     */
    private void validateCommissionDistribution(List<OrderVipCommission> commissions, Integer baseProfit) {
        if (commissions.isEmpty()) {
            return;
        }

        // 验证总佣金不超过合理范围
        Integer totalActualCommission = commissions.stream()
                .mapToInt(OrderVipCommission::getActualCommission)
                .sum();

        // 总收益不应该超过基础利润的3倍（包含VIP加成）
        if (totalActualCommission > baseProfit * 3) {
            logger.warn("佣金分配可能异常：基础利润={}, 总实际佣金={}", baseProfit, totalActualCommission);
        }

        // 验证代理商级别分布
        Set<Integer> agentLevels = commissions.stream()
                .map(OrderVipCommission::getAgentLevel)
                .collect(Collectors.toSet());

        if (agentLevels.size() != commissions.size()) {
            logger.warn("代理商级别分布异常，可能存在重复级别");
        }
    }

    /**
     * 处理订单完成后的佣金分配
     */
    @Transactional
    public void processOrderCommission(String orderNo, String agentCode, Integer baseProfit) {
        // 1. 检查VIP升级（使用AgentVipService）
        agentVipService.checkVipUpgradeAfterOrder(agentCode, 1);

        // 2. 计算多级佣金分配
        List<OrderVipCommission> commissions = calculateMultiLevelCommission(agentCode, baseProfit, orderNo);

        // 3. 批量保存佣金记录
        if (!commissions.isEmpty()) {
            orderVipCommissionMapper.insertBatch(commissions);
        }

        // 4. 更新订单表的VIP信息
        Order order = orderService.getByOrderNo(orderNo);
        if (order != null) {
            AgentAccount agent = agentVipService.getAgentVipInfo(agentCode);
            VipConfig vipConfig = vipConfigService.getByLevel(agent.getVipLevel());

            order.setAgentVipLevel(agent.getVipLevel());
            order.setBaseProfit(baseProfit);
            order.setVipBonus(vipConfig != null ? vipConfig.getFixedCommission() : 0);
            orderService.updateById(order);
        }
    }

    /**
     * 获取代理商链路（从当前代理商到顶级代理）
     */
    private List<AgentAccount> getAgentChain(String agentCode) {
        List<AgentAccount> chain = new ArrayList<>();
        String currentCode = agentCode;

        // 向上查找最多3级代理商
        while (currentCode != null && chain.size() < 3) {
            AgentAccount agent = agentAccountMapper.selectOne(new QueryWrapper<AgentAccount>()
                .eq("agent_code", currentCode));
            if (agent == null) break;

            chain.add(agent);
            currentCode = agent.getParentAgentCode();
        }

        return chain;
    }

    /**
     * 保存VIP佣金分配记录到现有表结构
     */
    @Transactional
    public void saveVipCommissionToExistingTables(String orderNo, Long orderId, String orderDownstreamId,
                                                   List<OrderVipCommission> commissions) {
        for (OrderVipCommission commission : commissions) {
            // 1. 更新或插入到 t_order_commission 主表
            OrderCommission orderCommission = orderCommissionMapper.selectOne(
                new QueryWrapper<OrderCommission>()
                    .eq("order_downstream_id", orderDownstreamId)
                    .eq("downstream_agent_code", commission.getAgentCode())
            );

            if (orderCommission == null) {
                // 创建新的佣金记录
                orderCommission = new OrderCommission();
                orderCommission.setOrderId(orderId);
                orderCommission.setOrderDownstreamId(orderDownstreamId);
                orderCommission.setDownstreamAgentCode(commission.getAgentCode());
                orderCommission.setProductCommission(commission.getTotalCommission());
                orderCommission.setOrderCommissionStatus(0); // 未到结算状态
                orderCommission.setCreateTime(System.currentTimeMillis());
            } else {
                // 更新现有记录
                orderCommission.setProductCommission(commission.getTotalCommission());
                orderCommission.setUpdateTime(System.currentTimeMillis());
            }

            // 设置VIP相关字段
            orderCommission.setAgentVipLevel(commission.getVipLevel());
            orderCommission.setBaseProfit(commission.getBaseProfit());
            orderCommission.setVipBonus(commission.getVipBonus());
            orderCommission.setTotalCommission(commission.getTotalCommission());

            if (orderCommission.getOrderCommissionId() == null) {
                orderCommissionMapper.insert(orderCommission);
            } else {
                orderCommissionMapper.updateById(orderCommission);
            }

            // 2. 更新或插入到 t_order_commission_details 详情表
            OrderCommissionDetails details = orderCommissionDetailsMapper.selectOne(
                new QueryWrapper<OrderCommissionDetails>()
                    .eq("order_commission_id", orderCommission.getOrderCommissionId())
                    .eq("agent_code", commission.getAgentCode())
            );

            if (details == null) {
                // 创建新的详情记录
                details = new OrderCommissionDetails();
                details.setOrderCommissionId(orderCommission.getOrderCommissionId());
                details.setOrderId(orderId);
                details.setAgentCode(commission.getAgentCode());
                details.setAgentSourceCode(commission.getParentAgentCode());
                details.setCreateTime(System.currentTimeMillis());
            } else {
                details.setUpdateTime(System.currentTimeMillis());
            }

            // 设置VIP相关字段
            details.setAgentVipLevel(commission.getVipLevel());
            details.setAgentLevel(commission.getAgentLevel());
            details.setBaseProfit(commission.getBaseProfit());
            details.setVipBonus(commission.getVipBonus());
            details.setTotalCommission(commission.getTotalCommission());
            details.setActualCommission(commission.getActualCommission());
            details.setParentAgentCode(commission.getParentAgentCode());

            // 计算原有佣金字段
            details.setProductCommission(commission.getTotalCommission());
            details.setRevenueProductCommission(commission.getActualCommission());

            if (details.getOrderCommissionDetailsId() == null) {
                orderCommissionDetailsMapper.insert(details);
            } else {
                orderCommissionDetailsMapper.updateById(details);
            }
        }
    }
}

/**
 * VIP佣金计算结果
 */
@Data
@Builder
public class VipCommissionResult {
    private String agentCode;
    private Integer vipLevel;
    private Integer baseProfit;
    private Integer vipBonus;
    private Integer totalCommission;

    public static VipCommissionResult empty() {
        return VipCommissionResult.builder().build();
    }
}
```

### 6.2 控制器层设计

#### 6.2.1 VIP配置管理控制器
```java
@RestController
@RequestMapping("/api/vip/config")
public class VipConfigController {

    @Autowired
    private VipConfigService vipConfigService;

    /**
     * 获取VIP配置列表
     */
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('vip:config:list')")
    public AjaxResult getVipConfigList() {
        List<VipConfig> list = vipConfigService.getVipConfigList();
        return AjaxResult.success(list);
    }

    /**
     * 新增VIP配置
     */
    @PostMapping("/add")
    @PreAuthorize("@ss.hasPermi('vip:config:add')")
    public AjaxResult addVipConfig(@RequestBody VipConfig vipConfig) {
        vipConfigService.insertVipConfig(vipConfig);
        return AjaxResult.success();
    }

    /**
     * 更新VIP配置
     */
    @PutMapping("/edit")
    @PreAuthorize("@ss.hasPermi('vip:config:edit')")
    public AjaxResult editVipConfig(@RequestBody VipConfig vipConfig) {
        vipConfigService.updateVipConfig(vipConfig);
        return AjaxResult.success();
    }

    /**
     * 删除VIP配置
     */
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPermi('vip:config:remove')")
    public AjaxResult removeVipConfig(@PathVariable Long[] ids) {
        vipConfigService.deleteVipConfigByIds(ids);
        return AjaxResult.success();
    }
}

#### 6.2.2 VIP代理商管理控制器
```java
@RestController
@RequestMapping("/api/vip/agent")
public class VipAgentController {

    @Autowired
    private AgentVipService agentVipService;

    /**
     * 获取代理商VIP信息
     */
    @GetMapping("/info/{agentCode}")
    @PreAuthorize("@ss.hasPermi('vip:agent:query')")
    public AjaxResult getAgentVipInfo(@PathVariable String agentCode) {
        // 添加数据权限控制：只能查询自己或下级的VIP信息
        if (!checkDataPermission(agentCode)) {
            return AjaxResult.error("无权限查询该代理商VIP信息");
        }

        AgentAccount agent = agentVipService.getAgentVipInfo(agentCode);
        return AjaxResult.success(agent);
    }

    /**
     * 设置代理商VIP等级
     */
    @PostMapping("/setLevel")
    @PreAuthorize("@ss.hasPermi('vip:agent:setLevel')")
    public AjaxResult setAgentVipLevel(@RequestBody SetAgentVipLevelRequest request) {
        agentVipService.setAgentVipLevel(
            request.getAgentCode(),
            request.getVipLevel(),
            request.getReason(),
            SecurityUtils.getUserId()
        );
        return AjaxResult.success();
    }

    /**
     * 获取VIP升级日志
     */
    @GetMapping("/upgradeLog/{agentCode}")
    @PreAuthorize("@ss.hasPermi('vip:agent:upgradeLog')")
    public AjaxResult getVipUpgradeLog(@PathVariable String agentCode) {
        if (!checkDataPermission(agentCode)) {
            return AjaxResult.error("无权限查询该代理商升级日志");
        }

        List<VipUpgradeLog> logs = agentVipService.getVipUpgradeLog(agentCode);
        return AjaxResult.success(logs);
    }

    /**
     * 批量设置VIP等级
     */
    @PostMapping("/batchSetLevel")
    @PreAuthorize("@ss.hasPermi('vip:agent:batchSetLevel')")
    public AjaxResult batchSetVipLevel(@RequestBody BatchSetVipLevelRequest request) {
        agentVipService.batchSetVipLevel(
            request.getAgentCodes(),
            request.getVipLevel(),
            request.getReason(),
            SecurityUtils.getUserId()
        );
        return AjaxResult.success();
    }

    private boolean checkDataPermission(String targetAgentCode) {
        // 实现数据权限检查逻辑
        // 1. 管理员可以查询所有
        // 2. 用户可以查询自己
        // 3. 上级可以查询下级
        return dataPermissionService.hasVipDataViewPermission(SecurityUtils.getUserId(), targetAgentCode);
    }
}
```

#### 6.2.3 VIP统计分析控制器（简化版）
```java
@RestController
@RequestMapping("/api/vip/statistics")
public class VipStatisticsController {

    @Autowired
    private VipStatisticsService vipStatisticsService;

    /**
     * 获取VIP等级分布统计
     */
    @GetMapping("/levelDistribution")
    @PreAuthorize("@ss.hasPermi('vip:statistics:query')")
    public AjaxResult getVipLevelDistribution() {
        Map<String, Object> data = vipStatisticsService.getVipLevelDistribution();
        return AjaxResult.success(data);
    }

    /**
     * 获取VIP升级趋势统计
     */
    @GetMapping("/upgradeTrend")
    @PreAuthorize("@ss.hasPermi('vip:statistics:query')")
    public AjaxResult getVipUpgradeTrend(@RequestParam String timeRange) {
        Map<String, Object> data = vipStatisticsService.getVipUpgradeTrend(timeRange);
        return AjaxResult.success(data);
    }

    /**
     * 获取VIP佣金统计
     */
    @GetMapping("/commissionStats")
    @PreAuthorize("@ss.hasPermi('vip:statistics:query')")
    public AjaxResult getVipCommissionStats(@RequestParam String timeRange) {
        Map<String, Object> data = vipStatisticsService.getVipCommissionStats(timeRange);
        return AjaxResult.success(data);
    }
}
```

#### 6.2.4 移除的不合理接口
```java
// ❌ 以下接口不应该存在，因为它们是内部计算逻辑：
// 1. VIP佣金计算预览 - 这是内部业务逻辑
// 2. 多级佣金计算预览 - 这是内部业务逻辑
// 3. 订单完成后VIP升级检查 - 应该集成到订单事件中

// ✅ 正确的处理方式：
// VIP升级应该通过订单完成事件自动触发
// 佣金计算应该增强现有的OrderCommissionService
// 统计数据应该集成到现有的统计模块
```

/**
 * VIP佣金计算请求
 */
@Data
public class VipCommissionCalculateRequest {
    private String agentCode;
    private Integer baseProfit;
}

/**
 * VIP配置更新请求（固定金额模式）
 */
@Data
public class VipConfigUpdateRequest {
    private Long id;
    private Integer vipLevel;
    private String levelName;
    private Integer requiredOrders;
    private Integer fixedCommission; // 固定佣金加成
    private String levelIcon;
    private Integer isEnabled;
    private String remark;
}

/**
 * 多级佣金计算请求
 */
@Data
public class MultiLevelCommissionRequest {
    private String agentCode;
    private Integer baseProfit;
}
```

## 七、前端实现方案

### 7.1 VIP管理页面组件

#### 7.1.1 VIP配置管理组件（固定金额模式）
```vue
<template>
  <div class="vip-config-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>VIP等级配置（固定金额模式）</span>
        <el-button
          style="float: right; padding: 3px 0"
          type="text"
          @click="handleAdd">
          新增等级
        </el-button>
      </div>

      <el-table :data="vipConfigList" style="width: 100%">
        <el-table-column prop="vipLevel" label="VIP等级" width="100" />
        <el-table-column prop="levelName" label="等级名称" width="150" />
        <el-table-column prop="requiredOrders" label="升级所需订单数" width="150" />
        <el-table-column prop="fixedCommission" label="固定佣金加成" width="150">
          <template slot-scope="scope">
            ¥{{ scope.row.fixedCommission }}
          </template>
        </el-table-column>
        <el-table-column prop="totalBenefit" label="累计收益" width="120">
          <template slot-scope="scope">
            ¥{{ scope.row.fixedCommission }}
          </template>
        </el-table-column>
        <el-table-column prop="isEnabled" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isEnabled ? 'success' : 'danger'">
              {{ scope.row.isEnabled ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button
              size="mini"
              type="primary"
              @click="handlePreview(scope.row)">
              预览收益
            </el-button>
            <el-button
              size="mini"
              :type="scope.row.isEnabled ? 'warning' : 'success'"
              @click="handleToggleStatus(scope.row)">
              {{ scope.row.isEnabled ? '禁用' : '启用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 编辑VIP配置对话框 -->
    <el-dialog title="编辑VIP配置" :visible.sync="dialogVisible" width="50%">
      <el-form :model="vipForm" label-width="150px">
        <el-form-item label="VIP等级">
          <el-input-number v-model="vipForm.vipLevel" :min="0" :max="10" disabled></el-input-number>
        </el-form-item>
        <el-form-item label="等级名称">
          <el-input v-model="vipForm.levelName" placeholder="请输入等级名称"></el-input>
        </el-form-item>
        <el-form-item label="升级所需订单数">
          <el-input-number v-model="vipForm.requiredOrders" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item label="固定佣金加成(元)">
          <el-input-number v-model="vipForm.fixedCommission" :min="0"></el-input-number>
          <div class="form-help">代理商获得此固定金额的佣金加成</div>
        </el-form-item>
        <el-form-item label="等级图标URL">
          <el-input v-model="vipForm.levelIcon" placeholder="请输入图标URL"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="vipForm.isEnabled"></el-switch>
        </el-form-item>
        <el-form-item label="备注">
          <el-input type="textarea" v-model="vipForm.remark" placeholder="请输入备注"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </div>
    </el-dialog>

    <!-- 收益预览对话框 -->
    <el-dialog title="VIP收益预览" :visible.sync="previewVisible" width="60%">
      <div class="preview-section">
        <h4>收益分配示例（基础利润100元）</h4>
        <el-table :data="previewData" style="width: 100%">
          <el-table-column prop="agentLevel" label="代理商级别" width="120" />
          <el-table-column prop="vipLevel" label="VIP等级" width="100" />
          <el-table-column prop="baseProfit" label="基础利润" width="100">
            <template slot-scope="scope">¥{{ scope.row.baseProfit }}</template>
          </el-table-column>
          <el-table-column prop="vipBonus" label="VIP加成" width="100">
            <template slot-scope="scope">¥{{ scope.row.vipBonus }}</template>
          </el-table-column>
          <el-table-column prop="totalCommission" label="总收益" width="100">
            <template slot-scope="scope">¥{{ scope.row.totalCommission }}</template>
          </el-table-column>
          <el-table-column prop="actualCommission" label="实际获得" width="100">
            <template slot-scope="scope">¥{{ scope.row.actualCommission }}</template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'VipConfig',
  data() {
    return {
      vipConfigList: [],
      dialogVisible: false,
      previewVisible: false,
      vipForm: {
        id: null,
        vipLevel: 0,
        levelName: '',
        requiredOrders: 0,
        fixedCommission: 0,
        levelIcon: '',
        isEnabled: true,
        remark: ''
      },
      previewData: []
    }
  },
  created() {
    this.getVipConfigList()
  },
  methods: {
    async getVipConfigList() {
      const response = await this.$http.get('/api/vip/config/list')
      this.vipConfigList = response.data
    },
    handleAdd() {
      this.vipForm = {
        id: null,
        vipLevel: this.vipConfigList.length,
        levelName: '',
        requiredOrders: 0,
        fixedCommission: 0,
        levelIcon: '',
        isEnabled: true,
        remark: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.vipForm = { ...row }
      this.dialogVisible = true
    },
    async handleSave() {
      try {
        await this.$http.post('/api/vip/config/update', this.vipForm)
        this.$message.success('保存成功')
        this.dialogVisible = false
        this.getVipConfigList()
      } catch (error) {
        this.$message.error('保存失败')
      }
    },
    async handlePreview(row) {
      try {
        const response = await this.$http.post('/api/vip/commission/multiLevel', {
          agentCode: 'DEMO_AGENT',
          baseProfit: 10000 // 100元，转换为分
        })
        this.previewData = response.data.map(item => ({
          agentLevel: ['直接代理商', '上级代理', '上上级代理'][item.agentLevel],
          vipLevel: `VIP${item.vipLevel}`,
          baseProfit: (item.baseProfit / 100).toFixed(2),
          vipBonus: (item.vipBonus / 100).toFixed(2),
          totalCommission: (item.totalCommission / 100).toFixed(2),
          actualCommission: (item.actualCommission / 100).toFixed(2)
        }))
        this.previewVisible = true
      } catch (error) {
        this.$message.error('预览失败')
      }
    },
    async handleToggleStatus(row) {
      try {
        await this.$http.post('/api/vip/config/update', {
          ...row,
          isEnabled: !row.isEnabled
        })
        this.$message.success('状态更新成功')
        this.getVipConfigList()
      } catch (error) {
        this.$message.error('状态更新失败')
      }
    }
  }
}
</script>

<style scoped>
.form-help {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
}

.preview-section h4 {
  margin-bottom: 15px;
  color: #333;
}
</style>
```

#### 7.1.2 用户VIP设置组件
```vue
<template>
  <div class="user-vip-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>用户VIP设置</span>
      </div>

      <el-form :inline="true" :model="searchForm" class="demo-form-inline">
        <el-form-item label="代理商编码">
          <el-input v-model="searchForm.agentCode" placeholder="请输入代理商编码"></el-input>
        </el-form-item>
        <el-form-item label="当前VIP等级">
          <el-select v-model="searchForm.vipLevel" placeholder="请选择">
            <el-option label="全部" value=""></el-option>
            <el-option label="VIP0" value="0"></el-option>
            <el-option label="VIP1" value="1"></el-option>
            <el-option label="VIP2" value="2"></el-option>
            <el-option label="VIP3" value="3"></el-option>
            <el-option label="VIP4" value="4"></el-option>
            <el-option label="VIP5" value="5"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="userList" style="width: 100%">
        <el-table-column prop="agentCode" label="代理商编码" width="150" />
        <el-table-column prop="agentName" label="代理商名称" width="150" />
        <el-table-column prop="vipLevel" label="VIP等级" width="100">
          <template slot-scope="scope">
            <el-tag :type="getVipLevelType(scope.row.vipLevel)">
              VIP{{ scope.row.vipLevel }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalOrders" label="总订单数" width="120" />
        <el-table-column prop="totalCommission" label="总佣金金额" width="150" />
        <el-table-column prop="upgradeTime" label="升级时间" width="180" />
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              @click="handleSetVipLevel(scope.row)">
              设置VIP
            </el-button>
            <el-button
              size="mini"
              @click="handleViewVipLog(scope.row)">
              升级日志
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.page"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total">
      </el-pagination>
    </el-card>

    <!-- 设置VIP等级对话框 -->
    <el-dialog title="设置VIP等级" :visible.sync="dialogVisible" width="30%">
      <el-form :model="vipForm" label-width="100px">
        <el-form-item label="代理商">
          <el-input v-model="vipForm.agentName" disabled></el-input>
        </el-form-item>
        <el-form-item label="当前等级">
          <el-tag>VIP{{ vipForm.currentLevel }}</el-tag>
        </el-form-item>
        <el-form-item label="设置等级">
          <el-select v-model="vipForm.targetLevel" placeholder="请选择VIP等级">
            <el-option label="VIP0" value="0"></el-option>
            <el-option label="VIP1" value="1"></el-option>
            <el-option label="VIP2" value="2"></el-option>
            <el-option label="VIP3" value="3"></el-option>
            <el-option label="VIP4" value="4"></el-option>
            <el-option label="VIP5" value="5"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="设置原因">
          <el-input type="textarea" v-model="vipForm.reason" placeholder="请输入设置原因"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmSetVip">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'UserVip',
  data() {
    return {
      searchForm: {
        agentCode: '',
        vipLevel: ''
      },
      userList: [],
      pagination: {
        page: 1,
        size: 10,
        total: 0
      },
      dialogVisible: false,
      vipForm: {
        userId: null,
        agentName: '',
        currentLevel: 0,
        targetLevel: 0,
        reason: ''
      }
    }
  },
  created() {
    this.getUserList()
  },
  methods: {
    async getUserList() {
      const params = {
        page: this.pagination.page,
        size: this.pagination.size,
        ...this.searchForm
      }
      const response = await this.$http.get('/api/vip/user/list', { params })
      this.userList = response.data.records
      this.pagination.total = response.data.total
    },
    handleSearch() {
      this.pagination.page = 1
      this.getUserList()
    },
    handleReset() {
      this.searchForm = {
        agentCode: '',
        vipLevel: ''
      }
      this.handleSearch()
    },
    handleSetVipLevel(row) {
      this.vipForm = {
        userId: row.userId,
        agentName: row.agentName,
        currentLevel: row.vipLevel,
        targetLevel: row.vipLevel,
        reason: ''
      }
      this.dialogVisible = true
    },
    async handleConfirmSetVip() {
      try {
        await this.$http.post('/api/vip/user/setLevel', {
          userId: this.vipForm.userId,
          vipLevel: this.vipForm.targetLevel,
          reason: this.vipForm.reason
        })
        this.$message.success('设置成功')
        this.dialogVisible = false
        this.getUserList()
      } catch (error) {
        this.$message.error('设置失败')
      }
    },
    getVipLevelType(level) {
      const types = ['info', 'success', 'warning', 'danger', 'primary', 'exception']
      return types[level] || 'info'
    }
  }
}
</script>
```

### 7.2 手机端VIP页面实现

#### 7.2.1 VIP等级展示页面
```vue
<template>
  <view class="vip-container">
    <!-- VIP等级卡片 -->
    <view class="vip-card">
      <view class="vip-header">
        <image class="vip-icon" :src="getVipIcon(userInfo.vipLevel)" />
        <view class="vip-info">
          <text class="vip-title">VIP{{ userInfo.vipLevel }} {{ getVipLevelName(userInfo.vipLevel) }}</text>
          <text class="vip-desc">{{ getVipLevelDesc(userInfo.vipLevel) }}</text>
        </view>
      </view>

      <view class="vip-stats">
        <view class="stat-item">
          <text class="stat-value">{{ userInfo.totalOrders }}</text>
          <text class="stat-label">总订单数</text>
        </view>
        <view class="stat-item">
          <text class="stat-value">¥{{ userInfo.totalCommission }}</text>
          <text class="stat-label">总佣金</text>
        </view>
      </view>
    </view>

    <!-- VIP等级进度 -->
    <view class="vip-progress">
      <view class="section-title">升级进度</view>
      <view class="progress-item">
        <text class="progress-label">订单数量</text>
        <view class="progress-bar">
          <view
            class="progress-fill"
            :style="{ width: getOrderProgress() + '%' }">
          </view>
        </view>
        <text class="progress-text">{{ userInfo.totalOrders }}/{{ nextLevelConfig.minOrders }}</text>
      </view>
      <view class="progress-item">
        <text class="progress-label">佣金金额</text>
        <view class="progress-bar">
          <view
            class="progress-fill"
            :style="{ width: getCommissionProgress() + '%' }">
          </view>
        </view>
        <text class="progress-text">¥{{ userInfo.totalCommission }}/¥{{ nextLevelConfig.minCommission }}</text>
      </view>
    </view>

    <!-- VIP升级规则 -->
    <view class="vip-rules">
      <view class="section-title">VIP等级规则</view>
      <view class="rule-list">
        <view
          v-for="rule in vipRules"
          :key="rule.level"
          class="rule-item"
          :class="{ 'current': rule.level === userInfo.vipLevel }">
          <view class="rule-level">
            <text class="level-text">VIP{{ rule.level }}</text>
            <text class="level-name">{{ rule.name }}</text>
          </view>
          <view class="rule-conditions">
            <text class="condition-text">{{ rule.minOrders }}单</text>
          </view>
          </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: 'VipPage',
  data() {
    return {
      userInfo: {
        vipLevel: 2,
        totalOrders: 35,
        totalCommission: 3200
      },
        vipRules: [
        {
          level: 0,
          name: '普通会员',
          minOrders: 0,
          commissionRate: 0
        },
        {
          level: 1,
          name: '铜牌会员',
          minOrders: 10,
          commissionRate: 1
        },
        {
          level: 2,
          name: '银牌会员',
          minOrders: 50,
          commissionRate: 2
        },
        {
          level: 3,
          name: '金牌会员',
          minOrders: 200,
          commissionRate: 3
        },
        {
          level: 4,
          name: '钻石会员',
          minOrders: 500,
          commissionRate: 4
        }
      ],
      nextLevelConfig: {
        minOrders: 50
      }
    }
  },
  onLoad() {
    this.loadVipInfo()
  },
  methods: {
    async loadVipInfo() {
      try {
        const response = await this.$http.get('/api/vip/user/info')
        this.userInfo = response.data
        this.updateNextLevelConfig()
        this.updatePrivileges()
      } catch (error) {
        console.error('加载VIP信息失败', error)
      }
    },
    getVipIcon(level) {
      const icons = [
        '/static/images/vip/level0.png',
        '/static/images/vip/level1.png',
        '/static/images/vip/level2.png',
        '/static/images/vip/level3.png',
        '/static/images/vip/level4.png'
      ]
      return icons[level] || icons[0]
    },
    getVipLevelName(level) {
      const names = ['普通会员', '铜牌会员', '银牌会员', '金牌会员', '钻石会员']
      return names[level] || '普通会员'
    },
    getOrderProgress() {
      const progress = (this.userInfo.totalOrders / this.nextLevelConfig.minOrders) * 100
      return Math.min(progress, 100)
    },
    getCommissionProgress() {
      const progress = (this.userInfo.totalCommission / this.nextLevelConfig.minCommission) * 100
      return Math.min(progress, 100)
    },
    updateNextLevelConfig() {
      const currentLevel = this.userInfo.vipLevel
      if (currentLevel < 4) {
        this.nextLevelConfig = this.vipRules[currentLevel + 1]
      } else {
        this.nextLevelConfig = { minOrders: 0 }
      }
    }
}
</script>

<style scoped>
.vip-container {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.vip-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
  color: white;
}

.vip-header {
  display: flex;
  align-items: center;
  margin-bottom: 30rpx;
}

.vip-icon {
  width: 80rpx;
  height: 80rpx;
  margin-right: 20rpx;
}

.vip-title {
  font-size: 36rpx;
  font-weight: bold;
  display: block;
}

.vip-desc {
  font-size: 24rpx;
  opacity: 0.8;
  margin-top: 10rpx;
}

.vip-stats {
  display: flex;
  justify-content: space-between;
}

.stat-item {
  text-align: center;
  flex: 1;
}

.stat-value {
  font-size: 32rpx;
  font-weight: bold;
  display: block;
}

.stat-label {
  font-size: 20rpx;
  opacity: 0.8;
  margin-top: 10rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
}

.vip-progress, .vip-rules {
  background: white;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
}


.progress-item {
  margin-bottom: 30rpx;
}

.progress-item:last-child {
  margin-bottom: 0;
}

.progress-label {
  font-size: 28rpx;
  color: #333;
  display: block;
  margin-bottom: 15rpx;
}

.progress-bar {
  height: 10rpx;
  background-color: #f0f0f0;
  border-radius: 5rpx;
  overflow: hidden;
  margin-bottom: 10rpx;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
  border-radius: 5rpx;
  transition: width 0.3s ease;
}

.progress-text {
  font-size: 24rpx;
  color: #999;
}

.rule-item {
  display: flex;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.rule-item:last-child {
  border-bottom: none;
}

.rule-item.current {
  background-color: #f8f9ff;
  border-radius: 10rpx;
  padding: 20rpx;
  margin: 0 -10rpx;
}

.rule-level {
  width: 150rpx;
}

.level-text {
  font-size: 28rpx;
  font-weight: bold;
  color: #667eea;
  display: block;
}

.level-name {
  font-size: 24rpx;
  color: #666;
  margin-top: 5rpx;
}

.rule-conditions {
  flex: 1;
  margin: 0 20rpx;
}

.condition-text {
  font-size: 26rpx;
  color: #333;
}

.rule-privileges {
  width: 200rpx;
  text-align: right;
}

.privilege-text {
  font-size: 24rpx;
  color: #999;
}
</style>
```

## 八、接口设计

### 8.1 VIP管理接口

#### 8.1.1 VIP配置管理接口（固定金额模式）
```java
// 获取VIP配置列表
GET /api/vip/config/list
Response: {
  "code": 200,
  "data": [
    {
      "id": 1,
      "vipLevel": 1,
      "levelName": "铜牌会员",
      "requiredOrders": 10,
      "fixedCommission": 5,
      "levelIcon": "/static/vip/vip1.png",
      "isEnabled": 1,
      "remark": "铜牌会员固定加成5元"
    }
  ]
}

// 更新VIP配置（固定金额模式）
POST /api/vip/config/update
Request: {
  "id": 1,
  "vipLevel": 1,
  "levelName": "铜牌会员",
  "requiredOrders": 10,
  "fixedCommission": 5,
  "levelIcon": "/static/vip/vip1.png",
  "isEnabled": 1,
  "remark": "铜牌会员固定加成5元"
}

// 新增VIP配置
POST /api/vip/config/add
Request: {
  "vipLevel": 6,
  "levelName": "超级会员",
  "requiredOrders": 2000,
  "fixedCommission": 30,
  "levelIcon": "/static/vip/vip6.png",
  "isEnabled": 1,
  "remark": "超级会员固定加成30元"
}
```

#### 8.1.2 代理商VIP管理接口
```java
// 获取代理商VIP信息
GET /api/vip/agent/info?agentCode={agentCode}
Response: {
  "code": 200,
  "data": {
    "agentAccountId": 123,
    "sysUserId": 456,
    "agentCode": "AGENT001",
    "agentName": "代理商名称",
    "vipLevel": 2,
    "previousVipLevel": 1,
    "vipUpgradeTime": "2024-01-15 10:30:00",
    "totalOrders": 35,
    "totalCommission": 320000,
    "createTime": "2024-01-01 10:00:00"
  }
}

// 设置代理商VIP等级
POST /api/vip/agent/setLevel
Request: {
  "agentCode": "AGENT001",
  "vipLevel": 3,
  "reason": "业绩优秀，手动升级"
}

// 获取代理商VIP列表（分页）
GET /api/vip/agent/list?page=1&size=10&agentCode=&vipLevel=
Response: {
  "code": 200,
  "data": {
    "records": [...],
    "total": 100,
    "size": 10,
    "current": 1
  }
}
```

#### 8.1.3 订单完成后VIP升级检查接口
```java
// 订单完成后检查VIP升级
POST /api/vip/upgrade/checkAfterOrder
Request: {
  "agentCode": "AGENT001",
  "orderCount": 5,
  "orderId": "ORDER20240115001"
}

Response: {
  "code": 200,
  "data": {
    "success": true,
    "message": "VIP升级检查完成"
  }
}
```

#### 8.1.4 VIP佣金计算接口（固定金额模式）
```java
// 计算VIP佣金（基础利润 + 固定加成）
POST /api/vip/commission/calculate
Request: {
  "agentCode": "AGENT001",
  "baseProfit": 10000
}

Response: {
  "code": 200,
  "data": {
    "agentCode": "AGENT001",
    "vipLevel": 3,
    "baseProfit": 10000,
    "vipBonus": 1500,
    "totalCommission": 11500
  }
}

// 计算多级佣金分配（管理功能）
POST /api/vip/commission/multiLevel
Request: {
  "agentCode": "AGENT001",
  "baseProfit": 10000
}

Response: {
  "code": 200,
  "data": [
    {
      "orderNo": "DEMO_123456789",
      "agentCode": "AGENT001",
      "agentLevel": 0,
      "vipLevel": 3,
      "baseProfit": 10000,
      "vipBonus": 1500,
      "totalCommission": 11500,
      "actualCommission": 11500,
      "parentAgentCode": "PARENT001"
    },
    {
      "orderNo": "DEMO_123456789",
      "agentCode": "PARENT001",
      "agentLevel": 1,
      "vipLevel": 2,
      "baseProfit": 10000,
      "vipBonus": 1000,
      "totalCommission": 11000,
      "actualCommission": 0,
      "parentAgentCode": "GRANDPARENT001"
    }
  ]
}
```

### 8.2 手机端接口

#### 8.2.1 VIP信息查询接口
```java
// 获取个人VIP信息
GET /api/app/vip/info
Response: {
  "code": 200,
  "data": {
    "vipLevel": 2,
    "levelName": "银牌会员",
    "totalOrders": 35,
    "totalCommission": 3200.00,
    "nextLevelOrders": 50
  }
}

// 获取VIP等级规则（固定金额模式）
GET /api/app/vip/rules
Response: {
  "code": 200,
  "data": [
    {
      "level": 1,
      "name": "铜牌会员",
      "minOrders": 10,
      "fixedCommission": 5
    },
    {
      "level": 2,
      "name": "银牌会员",
      "minOrders": 50,
      "fixedCommission": 10
    }
  ]
}
```

## 九、VIP功能和性能监控体系

### 9.1 关键性能指标(KPI)设计

#### 9.1.1 VIP功能核心指标
```java
/**
 * VIP功能监控指标定义
 */
public class VipMetrics {

    // VIP等级分布指标
    public static class VipLevelDistribution {
        private Map<Integer, Long> vipLevelCount; // 各VIP等级代理商数量
        private Double vipUpgradeRate;        // VIP升级成功率
        private Long totalVipUsers;           // VIP用户总数
        private Double avgUpgradeTime;       // 平均升级时间
    }

    // 佣金性能指标
    public class CommissionPerformance {
        private Long avgCommissionCalcTime;    // 平均佣金计算时间(ms)
        private Long maxCommissionCalcTime;    // 最大佣金计算时间(ms)
        private Double commissionSuccessRate;   // 佣金计算成功率
        private Long totalCommissionAmount;    // 总佣金金额
        private Long vipBonusAmount;          // VIP加成总金额
    }

    // 业务健康度指标
    public class BusinessHealth {
        private Long activeVipUsersToday;     // 今日活跃VIP用户数
        private Long vipUpgradeEventsToday;    // 今日VIP升级事件数
        private Double commissionAccuracyRate; // 佣金计算准确率
        private Long abnormalCommissionCount;  // 异常佣金数量
        private Double systemAvailability;     // VIP功能可用性
    }
}
```

#### 9.1.2 监控SQL查询
```sql
-- VIP等级分布统计
SELECT
    vip_level,
    COUNT(*) as agent_count,
    AVG(total_orders) as avg_orders,
    AVG(total_commission/100.0) as avg_commission
FROM t_agent_account
WHERE vip_level > 0
GROUP BY vip_level
ORDER BY vip_level;

-- VIP升级趋势分析
SELECT
    DATE(vip_upgrade_time) as upgrade_date,
    COUNT(*) as upgrade_count,
    from_level,
    to_level
FROM t_vip_upgrade_log
WHERE vip_upgrade_time >= DATE_SUB(NOW(), INTERVAL 30 DAY)
GROUP BY DATE(vip_upgrade_time), from_level, to_level
ORDER BY upgrade_date DESC;

-- 佣金计算性能统计
SELECT
    DATE(create_time) as stat_date,
    COUNT(*) as total_orders,
    AVG(TIMESTAMPDIFF(MICROSECOND, create_time, vip_calculation_time))/1000 as avg_calc_time_ms,
    SUM(CASE WHEN actual_commission > 0 THEN 1 ELSE 0 END) as positive_commission_count,
    SUM(vip_bonus) as total_vip_bonus
FROM t_order_commission_details
WHERE create_time >= DATE_SUB(NOW(), INTERVAL 7 DAY)
    AND agent_vip_level > 0
GROUP BY DATE(create_time)
ORDER BY stat_date DESC;

-- 异常佣金检测
SELECT
    agent_code,
    COUNT(*) as abnormal_count,
    SUM(actual_commission) as total_actual_commission
FROM t_order_commission_details
WHERE actual_commission < 0
   OR total_commission < vip_bonus
   OR agent_vip_level < 0 OR agent_vip_level > 5
GROUP BY agent_code
HAVING abnormal_count > 5
ORDER BY abnormal_count DESC;
```

### 9.2 实时监控和告警机制

#### 9.2.1 Spring Boot Actuator配置
```yaml
# application.yml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,vip-metrics
  endpoint:
    health:
      show-details: always
  metrics:
    export:
      prometheus:
        enabled: true

# 自定义VIP监控端点
endpoints:
  vip-metrics:
    path: /actuator/vip-metrics
    sensitive: false
```

#### 9.2.2 VIP监控控制器
```java
@RestController
@RequestMapping("/actuator")
public class VipMetricsController {

    @Autowired
    private VipCommissionService vipCommissionService;

    @Autowired
    private AgentVipService agentVipService;

    /**
     * VIP功能健康检查
     */
    @GetMapping("/vip-health")
    public Map<String, Object> vipHealth() {
        Map<String, Object> health = new HashMap<>();

        try {
            // 检查VIP配置是否正常
            VipConfig config = vipConfigService.getByLevel(1);
            boolean configOk = config != null && config.getFixedCommission() >= 0;

            // 检查VIP计算服务是否正常
            long startTime = System.currentTimeMillis();
            VipCommissionResult result = vipCommissionService.calculateAgentVipCommission("TEST_AGENT", 10000);
            long calcTime = System.currentTimeMillis() - startTime;
            boolean calcOk = result != null && calcTime < 1000; // 计算时间小于1秒

            health.put("status", configOk && calcOk ? "UP" : "DOWN");
            health.put("config_status", configOk ? "OK" : "ERROR");
            health.put("calculation_status", calcOk ? "OK" : "ERROR");
            health.put("calculation_time_ms", calcTime);
            health.put("timestamp", System.currentTimeMillis());

        } catch (Exception e) {
            health.put("status", "DOWN");
            health.put("error", e.getMessage());
            health.put("timestamp", System.currentTimeMillis());
        }

        return health;
    }

    /**
     * VIP功能关键指标
     */
    @GetMapping("/vip-metrics")
    public Map<String, Object> vipMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        try {
            // VIP等级分布
            metrics.put("vip_distribution", getVipLevelDistribution());

            // 今日VIP升级统计
            metrics.put("today_upgrades", getTodayVipUpgrades());

            // 佣金计算性能
            metrics.put("commission_performance", getCommissionPerformance());

            // 异常监控
            metrics.put("abnormal_alerts", getAbnormalAlerts());

        } catch (Exception e) {
            metrics.put("error", e.getMessage());
        }

        return metrics;
    }

    private Map<String, Object> getVipLevelDistribution() {
        // 实现VIP等级分布统计
        return new HashMap<>();
    }

    private Map<String, Object> getTodayVipUpgrades() {
        // 实现今日VIP升级统计
        return new HashMap<>();
    }

    private Map<String, Object> getCommissionPerformance() {
        // 实现佣金计算性能统计
        return new HashMap<>();
    }

    private Map<String, Object> getAbnormalAlerts() {
        // 实现异常告警统计
        return new HashMap<>();
    }
}
```

### 9.3 性能监控和优化建议

#### 9.3.1 Redis缓存配置
```java
/**
 * VIP配置缓存管理
 */
@Service
public class VipConfigCache {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String VIP_CONFIG_KEY_PREFIX = "vip:config:";
    private static final String VIP_AGENT_KEY_PREFIX = "vip:agent:";
    private static final long CACHE_EXPIRE_TIME = 3600; // 1小时

    /**
     * 缓存VIP配置
     */
    public void cacheVipConfig(VipConfig config) {
        String key = VIP_CONFIG_KEY_PREFIX + config.getVipLevel();
        redisTemplate.opsForValue().set(key, config, CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
    }

    /**
     * 获取缓存的VIP配置
     */
    public VipConfig getCachedVipConfig(Integer vipLevel) {
        String key = VIP_CONFIG_KEY_PREFIX + vipLevel;
        return (VipConfig) redisTemplate.opsForValue().get(key);
    }

    /**
     * 清除VIP配置缓存
     */
    public void evictVipConfigCache(Integer vipLevel) {
        String key = VIP_CONFIG_KEY_PREFIX + vipLevel;
        redisTemplate.delete(key);
    }

    /**
     * 预热VIP配置缓存
     */
    @PostConstruct
    public void preloadVipConfigs() {
        List<VipConfig> configs = vipConfigService.getVipConfigList();
        configs.forEach(this::cacheVipConfig);
    }
}
```

#### 9.3.2 异步处理优化
```java
/**
 * VIP计算异步处理
 */
@Service
public class VipCalculationAsyncService {

    @Autowired
    private TaskExecutor vipCalculationExecutor;

    /**
     * 异步计算VIP佣金
     */
    @Async("vipCalculationExecutor")
    public CompletableFuture<VipCommissionResult> calculateVipCommissionAsync(String agentCode, Integer baseProfit) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return vipCommissionService.calculateAgentVipCommission(agentCode, baseProfit);
            } catch (Exception e) {
                logger.error("异步VIP佣金计算失败", e);
                return VipCommissionResult.empty();
            }
        }, vipCalculationExecutor);
    }

    /**
     * 批量异步计算
     */
    public List<CompletableFuture<VipCommissionResult>> batchCalculateVipCommission(
            List<VipCalculationRequest> requests) {
        return requests.stream()
                .map(req -> calculateVipipCommissionAsync(req.getAgentCode(), req.getBaseProfit()))
                .collect(Collectors.toList());
    }
}

/**
 * VIP计算线程池配置
 */
@Configuration
@EnableAsync
public class VipAsyncConfig {

    @Bean("vipCalculationExecutor")
    public TaskExecutor vipCalculationExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("VipCalc-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
```

## 十、VIP功能安全和权限控制

### 10.1 安全架构设计

#### 10.1.1 基于RuoYi权限体系的VIP安全控制
```java
/**
 * VIP功能安全控制注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VipSecurity {

    /**
     * 最小VIP等级要求
     */
    int minVipLevel() default 0;

    /**
     * 最大VIP等级限制
     */
    int maxVipLevel() default 5;

    /**
     * 权限标识
     */
    String permission() default "";

    /**
     * 是否需要管理员权限
     */
    boolean requireAdmin() default false;

    /**
     * 操作类型
     */
    VipOperation operation() default VipOperation.VIEW;
}

public enum VipOperation {
    VIEW,       // 查看权限
    EDIT,       // 编辑权限
    SET_LEVEL,  // 设置VIP等级
    CONFIG,     // 配置管理
    UPGRADE,    // 升级操作
    EXPORT      // 数据导出
}
```

#### 10.1.2 VIP安全切面实现
```java
/**
 * VIP功能安全控制切面
 */
@Aspect
@Component
@Slf4j
public class VipSecurityAspect {

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private AgentAccountMapper agentAccountMapper;

    @Autowired
    private VipOperationLogService vipOperationLogService;

    /**
     * VIP权限检查切入点
     */
    @Pointcut("@annotation(com.ruoyi.common.annotation.VipSecurity)")
    public void vipSecurityPointcut() {}

    /**
     * 前置权限检查
     */
    @Before("vipSecurityPointcut() && @annotation(vipSecurity)")
    public void checkVipPermission(JoinPoint joinPoint, VipSecurity vipSecurity) {
        // 1. 获取当前用户信息
        Long currentUserId = securityUtils.getUserId();
        if (currentUserId == null) {
            throw new ServiceException("用户未登录");
        }

        // 2. 获取当前用户代理信息
        AgentAccount currentUser = agentAccountMapper.selectOne(
            new QueryWrapper<AgentAccount>().eq("sys_user_id", currentUserId));

        if (currentUser == null) {
            throw new ServiceException("用户代理信息不存在");
        }

        // 3. 检查管理员权限
        if (vipSecurity.requireAdmin() && !isAdmin(currentUser)) {
            log.warn("非管理员用户尝试执行管理员操作: userId={}, operation={}",
                    currentUserId, vipSecurity.operation());
            throw new ServiceException("权限不足，需要管理员权限");
        }

        // 4. 检查VIP等级限制
        checkVipLevelRestriction(currentUser, vipSecurity);

        // 5. 检查功能权限
        checkFunctionalPermission(currentUser, vipSecurity);

        // 6. 记录操作审计日志
        recordOperationAudit(currentUserId, joinPoint, vipSecurity);
    }

    /**
     * 检查VIP等级限制
     */
    private void checkVipLevelRestriction(AgentAccount user, VipSecurity vipSecurity) {
        Integer userVipLevel = user.getVipLevel() != null ? user.getVipLevel() : 0;

        if (userVipLevel < vipSecurity.minVipLevel()) {
            throw new ServiceException(String.format(
                "权限不足，当前VIP等级%d，需要VIP等级%d及以上",
                userVipLevel, vipSecurity.minVipLevel()));
        }

        if (userVipLevel > vipSecurity.maxVipLevel()) {
            throw new ServiceException(String.format(
                "权限限制，当前VIP等级%d超过最大允许等级%d",
                userVipLevel, vipSecurity.maxVipLevel()));
        }
    }

    /**
     * 检查功能权限
     */
    private void checkFunctionalPermission(AgentAccount user, VipSecurity vipSecurity) {
        if (StringUtils.isNotEmpty(vipSecurity.permission())) {
            boolean hasPermission = securityUtils.hasPermi(vipSecurity.permission());
            if (!hasPermission) {
                throw new ServiceException("权限不足，缺少功能权限: " + vipSecurity.permission());
            }
        }
    }

    /**
     * 记录操作审计日志
     */
    private void recordOperationAudit(Long userId, JoinPoint joinPoint, VipSecurity vipSecurity) {
        try {
            String methodName = joinPoint.getSignature().getName();
            String className = joinPoint.getTarget().getClass().getSimpleName();
            Object[] args = joinPoint.getArgs();

            VipOperationLog log = VipOperationLog.builder()
                .userId(userId)
                .operationType(vipSecurity.operation().name())
                .methodName(className + "." + methodName)
                .requestParams(JsonUtils.toJsonString(args))
                .ipAddress(IpUtils.getIpAddr(ServletUtils.getRequest()))
                .userAgent(ServletUtils.getRequest().getHeader("User-Agent"))
                .operationTime(new Date())
                .build();

            vipOperationLogService.recordLog(log);
        } catch (Exception e) {
            log.error("记录VIP操作审计日志失败", e);
        }
    }

    private boolean isAdmin(AgentAccount agent) {
        // 基于用户角色判断是否为管理员
        return agent.getLevel() != null && agent.getLevel() == 0;
    }
}
```

### 10.2 VIP等级设置权限控制

#### 10.2.1 层级权限控制逻辑
```java
/**
 * VIP等级设置权限控制服务
 */
@Service
@Transactional
public class VipLevelPermissionService {

    @Autowired
    private AgentAccountMapper agentAccountMapper;

    @Autowired
    private VipOperationLogService vipOperationLogService;

    /**
     * 检查VIP等级设置权限
     */
    public void checkVipLevelSetPermission(Long operatorId, String targetAgentCode, Integer targetVipLevel) {
        // 1. 获取操作者信息
        AgentAccount operator = getAgentByUserId(operatorId);
        if (operator == null) {
            throw new ServiceException("操作者信息不存在");
        }

        // 2. 获取目标用户信息
        AgentAccount target = getAgentByCode(targetAgentCode);
        if (target == null) {
            throw new ServiceException("目标用户不存在");
        }

        // 3. 检查管理员权限（管理员可以设置任何用户的VIP等级）
        if (isAdmin(operator)) {
            log.info("管理员操作: userId={} 设置用户 {} VIP等级为 {}",
                    operatorId, targetAgentCode, targetVipLevel);
            return;
        }

        // 4. 检查代理层级关系
        checkAgentHierarchyPermission(operator, target, targetVipLevel);

        // 5. 检查VIP等级限制
        checkVipLevelHierarchy(operator, targetVipLevel);

        // 6. 记录权限检查日志
        recordPermissionCheckLog(operator, target, targetVipLevel, "PASS");
    }

    /**
     * 检查代理层级权限
     */
    private void checkAgentHierarchyPermission(AgentAccount operator, AgentAccount target, Integer targetVipLevel) {
        // 管理员不受限制
        if (isAdmin(operator)) {
            return;
        }

        // 检查是否为父子代理关系
        boolean isParentAgent = isParentAgent(operator.getAgentCode(), target.getAgentCode());
        boolean isSubAgent = isSubAgent(operator.getAgentCode(), target.getAgentCode());

        if (!isParentAgent && !isSubAgent) {
            throw new ServiceException("权限不足，只能设置直系代理的VIP等级");
        }

        // 上级代理商设置下级VIP等级时的限制
        if (isParentAgent) {
            checkParentSetSubVipPermission(operator, target, targetVipLevel);
        }
        // 下级代理商不能设置上级VIP等级（除管理员外）
        else if (isSubAgent) {
            throw new ServiceException("权限不足，下级不能设置上级VIP等级");
        }
    }

    /**
     * 检查上级设置下级VIP等级权限
     */
    private void checkParentSetSubVipPermission(AgentAccount parent, AgentAccount sub, Integer targetVipLevel) {
        // 1. 上级不能设置高于自己等级的VIP
        if (targetVipLevel > parent.getVipLevel()) {
            throw new ServiceException(String.format(
                "权限不足，不能设置高于自己等级的VIP。当前等级: VIP%d，目标等级: VIP%d",
                parent.getVipLevel(), targetVipLevel));
        }

        // 2. 特殊情况：VIP3及以上代理商可以设置VIP0-2的子代理
        if (parent.getVipLevel() >= 3 && targetVipLevel <= 2) {
            log.info("高级代理商设置低级子代理VIP: parent={} VIP{} -> sub={} VIP{}",
                    parent.getAgentCode(), parent.getVipLevel(),
                    sub.getAgentCode(), targetVipLevel);
            return;
        }

        // 3. VIP2代理商只能设置VIP0-1的子代理
        if (parent.getVipLevel() == 2 && targetVipLevel > 1) {
            throw new ServiceException("VIP2代理商只能设置VIP0-1等级的子代理");
        }

        // 4. VIP1代理商只能设置VIP0的子代理
        if (parent.getVipLevel() == 1 && targetVipLevel > 0) {
            throw new ServiceException("VIP1代理商只能设置VIP0等级的子代理");
        }
    }

    /**
     * 检查VIP等级层级关系
     */
    private void checkVipLevelHierarchy(AgentAccount operator, Integer targetVipLevel) {
        // VIP0不能设置他人VIP等级
        if (operator.getVipLevel() == 0 && !isAdmin(operator)) {
            throw new ServiceException("VIP0用户无权限设置他人VIP等级");
        }

        // 检查目标VIP等级是否在有效范围内
        if (targetVipLevel < 0 || targetVipLevel > 5) {
            throw new ServiceException("VIP等级设置范围错误，有效范围：0-5");
        }
    }

    /**
     * 判断是否为父代理关系
     */
    private boolean isParentAgent(String parentCode, String subCode) {
        AgentAccount subAgent = agentAccountMapper.selectOne(
            new QueryWrapper<AgentAccount>().eq("agent_code", subCode));

        return subAgent != null && parentCode.equals(subAgent.getParentAgentCode());
    }

    /**
     * 判断是否为子代理关系
     */
    private boolean isSubAgent(String subCode, String parentCode) {
        return isParentAgent(parentCode, subCode);
    }

    private AgentAccount getAgentByUserId(Long userId) {
        return agentAccountMapper.selectOne(
            new QueryWrapper<AgentAccount>().eq("sys_user_id", userId));
    }

    private AgentAccount getAgentByCode(String agentCode) {
        return agentAccountMapper.selectOne(
            new QueryWrapper<AgentAccount>().eq("agent_code", agentCode));
    }

    private boolean isAdmin(AgentAccount agent) {
        return agent.getLevel() != null && agent.getLevel() == 0;
    }

    private void recordPermissionCheckLog(AgentAccount operator, AgentAccount target,
                                         Integer targetVipLevel, String result) {
        VipOperationLog log = VipOperationLog.builder()
            .userId(operator.getSysUserId())
            .operationType("VIP_LEVEL_PERMISSION_CHECK")
            .methodName("VipLevelPermissionService.checkVipLevelSetPermission")
            .requestParams(String.format("operator=%s, target=%s, targetVip=%d",
                    operator.getAgentCode(), target.getAgentCode(), targetVipLevel))
            .result(result)
            .ipAddress(IpUtils.getIpAddr(ServletUtils.getRequest()))
            .operationTime(new Date())
            .build();

        vipOperationLogService.recordLog(log);
    }
}
```

### 10.3 VIP操作审计和日志系统

#### 10.3.1 VIP操作日志记录
```java
/**
 * VIP操作日志服务
 */
@Service
public class VipOperationLogService {

    @Autowired
    private VipOperationLogMapper vipOperationLogMapper;

    /**
     * 记录VIP操作日志
     */
    @Async
    public void recordLog(VipOperationLog log) {
        try {
            vipOperationLogMapper.insert(log);
        } catch (Exception e) {
            log.error("记录VIP操作日志失败", e);
        }
    }

    /**
     * 记录VIP等级变更日志
     */
    public void recordVipLevelChange(Long operatorId, String targetAgentCode,
                                   Integer fromLevel, Integer toLevel, String reason) {
        VipOperationLog log = VipOperationLog.builder()
            .userId(operatorId)
            .targetAgentCode(targetAgentCode)
            .operationType("VIP_LEVEL_CHANGE")
            .methodName("AgentVipService.setAgentVipLevel")
            .requestParams(String.format("fromLevel=%d, toLevel=%d, reason=%s",
                    fromLevel, toLevel, reason))
            .ipAddress(IpUtils.getIpAddr(ServletUtils.getRequest()))
            .operationTime(new Date())
            .build();

        recordLog(log);
    }

    /**
     * 记录VIP配置修改日志
     */
    public void recordVipConfigChange(Long operatorId, VipConfig oldConfig, VipConfig newConfig) {
        VipOperationLog log = VipOperationLog.builder()
            .userId(operatorId)
            .operationType("VIP_CONFIG_CHANGE")
            .methodName("VipConfigService.updateVipConfig")
            .requestParams(String.format("oldConfig=%s, newConfig=%s",
                    JsonUtils.toJsonString(oldConfig), JsonUtils.toJsonString(newConfig)))
            .ipAddress(IpUtils.getIpAddr(ServletUtils.getRequest()))
            .operationTime(new Date())
            .build();

        recordLog(log);
    }

    /**
     * 记录VIP佣金计算日志
     */
    public void recordVipCommissionCalculation(String agentCode, Integer baseProfit,
                                             VipCommissionResult result) {
        VipOperationLog log = VipOperationLog.builder()
            .operationType("VIP_COMMISSION_CALCULATION")
            .methodName("VipCommissionService.calculateAgentVipCommission")
            .requestParams(String.format("agentCode=%s, baseProfit=%d", agentCode, baseProfit))
            .result(JsonUtils.toJsonString(result))
            .ipAddress(IpUtils.getIpAddr(ServletUtils.getRequest()))
            .operationTime(new Date())
            .build();

        recordLog(log);
    }
}

/**
 * VIP操作日志实体
 */
@TableName(value = "t_vip_operation_log")
@Data
@Builder
public class VipOperationLog {
    private Long logId;
    private Long userId;
    private String targetAgentCode;
    private String operationType;
    private String methodName;
    private String requestParams;
    private String result;
    private String ipAddress;
    private String userAgent;
    private Date operationTime;
    private String errorMessage;
    private Integer executionTime;
}
```

#### 10.3.2 VIP操作日志表设计
```sql
-- VIP操作日志表
CREATE TABLE t_vip_operation_log (
    log_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    user_id BIGINT NOT NULL COMMENT '操作用户ID',
    target_agent_code VARCHAR(50) COMMENT '目标代理商编码',
    operation_type VARCHAR(50) NOT NULL COMMENT '操作类型',
    method_name VARCHAR(200) COMMENT '执行方法',
    request_params TEXT COMMENT '请求参数',
    result TEXT COMMENT '执行结果',
    ip_address VARCHAR(50) COMMENT 'IP地址',
    user_agent VARCHAR(500) COMMENT '用户代理',
    operation_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    error_message TEXT COMMENT '错误信息',
    execution_time INT COMMENT '执行时间(ms)',
    KEY idx_user_id (user_id),
    KEY idx_operation_type (operation_type),
    KEY idx_operation_time (operation_time),
    KEY idx_target_agent_code (target_agent_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='VIP操作日志表';
```

### 10.4 VIP功能防刷和安全防护

#### 10.4.1 接口调用频率限制
```java
/**
 * VIP接口防刷限制
 */
@Component
public class VipRateLimiter {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String VIP_RATE_LIMIT_PREFIX = "vip:rate_limit:";
    private static final int DEFAULT_WINDOW_SECONDS = 60;
    private static final int DEFAULT_MAX_REQUESTS = 10;

    /**
     * 检查VIP操作频率限制
     */
    public boolean checkVipOperationRateLimit(Long userId, String operation, int maxRequests, int windowSeconds) {
        String key = VIP_RATE_LIMIT_PREFIX + userId + ":" + operation;

        try {
            // 获取当前计数
            String countStr = redisTemplate.opsForValue().get(key);
            int currentCount = countStr != null ? Integer.parseInt(countStr) : 0;

            if (currentCount >= maxRequests) {
                log.warn("VIP操作频率超限: userId={}, operation={}, count={}",
                        userId, operation, currentCount);
                return false;
            }

            // 增加计数
            if (currentCount == 0) {
                // 第一次设置，带过期时间
                redisTemplate.opsForValue().set(key, "1", windowSeconds, TimeUnit.SECONDS);
            } else {
                // 增加计数
                redisTemplate.opsForValue().increment(key, 1);
            }

            return true;
        } catch (Exception e) {
            log.error("检查VIP操作频率限制失败", e);
            // 异常时允许通过，避免影响正常业务
            return true;
        }
    }

    /**
     * 检查VIP等级设置频率限制
     */
    public boolean checkVipLevelSetRateLimit(Long userId) {
        return checkVipOperationRateLimit(userId, "SET_VIP_LEVEL", 5, 300); // 5分钟内最多5次
    }

    /**
     * 检查VIP配置修改频率限制
     */
    public boolean checkVipConfigModifyRateLimit(Long userId) {
        return checkVipOperationRateLimit(userId, "MODIFY_VIP_CONFIG", 3, 600); // 10分钟内最多3次
    }

    /**
     * 检查VIP佣金计算频率限制
     */
    public boolean checkVipCommissionCalcRateLimit(String agentCode) {
        return checkVipOperationRateLimit(null, "CALC_COMMISSION:" + agentCode, 100, 60); // 每分钟最多100次
    }
}
```

#### 10.4.2 VIP操作安全验证
```java
/**
 * VIP操作安全验证服务
 */
@Service
public class VipSecurityValidator {

    @Autowired
    private VipRateLimiter vipRateLimiter;

    /**
     * 验证VIP等级设置操作安全性
     */
    public void validateVipLevelSetOperation(Long operatorId, String targetAgentCode, Integer targetVipLevel) {
        // 1. 频率限制检查
        if (!vipRateLimiter.checkVipLevelSetRateLimit(operatorId)) {
            throw new ServiceException("操作过于频繁，请稍后再试");
        }

        // 2. 参数合法性检查
        if (targetVipLevel < 0 || targetVipLevel > 5) {
            throw new ServiceException("VIP等级设置参数错误");
        }

        // 3. 目标用户状态检查
        validateTargetUserStatus(targetAgentCode);

        // 4. 操作时间限制（非工作时间限制管理员操作）
        validateOperationTimeRestriction(operatorId);
    }

    /**
     * 验证VIP配置修改操作安全性
     */
    public void validateVipConfigModifyOperation(Long operatorId, VipConfig config) {
        // 1. 频率限制检查
        if (!vipRateLimiter.checkVipConfigModifyRateLimit(operatorId)) {
            throw new ServiceException("配置修改过于频繁，请稍后再试");
        }

        // 2. 配置参数合法性检查
        validateVipConfigParameters(config);

        // 3. 配置变更影响检查
        validateConfigChangeImpact(config);
    }

    /**
     * 验证目标用户状态
     */
    private void validateTargetUserStatus(String agentCode) {
        AgentAccount target = agentAccountMapper.selectOne(
            new QueryWrapper<AgentAccount>().eq("agent_code", agentCode));

        if (target == null) {
            throw new ServiceException("目标用户不存在");
        }

        // 检查用户是否被禁用
        if (target.getIsEnabled() != null && target.getIsEnabled() == 1) {
            throw new ServiceException("目标用户已被禁用，无法设置VIP等级");
        }

        // 检查用户实名认证状态（非管理员操作时）
        if (!isAdmin(SecurityUtils.getUserId()) &&
            (target.getIsRealName() == null || target.getIsRealName() != 1)) {
            if (targetVipLevel > 2) {
                throw new ServiceException("目标用户未完成实名认证，VIP等级不能超过VIP2");
            }
        }
    }

    /**
     * 验证操作时间限制
     */
    private void validateOperationTimeRestriction(Long operatorId) {
        AgentAccount operator = getAgentByUserId(operatorId);

        // 管理员不受时间限制
        if (isAdmin(operator)) {
            return;
        }

        // 非工作时间限制VIP等级设置（22:00-08:00）
        int hour = LocalDateTime.now().getHour();
        if (hour >= 22 || hour < 8) {
            throw new ServiceException("非工作时间（22:00-08:00）不能设置VIP等级");
        }
    }

    /**
     * 验证VIP配置参数
     */
    private void validateVipConfigParameters(VipConfig config) {
        if (config.getVipLevel() < 0 || config.getVipLevel() > 5) {
            throw new ServiceException("VIP等级必须在0-5范围内");
        }

        if (config.getFixedCommission() < 0 || config.getFixedCommission() > 1000) {
            throw new ServiceException("固定佣金加成必须在0-1000分范围内");
        }

        if (config.getRequiredOrders() < 0 || config.getRequiredOrders() > 100000) {
            throw new ServiceException("升级所需订单数必须在0-100000范围内");
        }
    }

    /**
     * 验证配置变更影响
     */
    private void validateConfigChangeImpact(VipConfig config) {
        // 获取现有配置
        VipConfig existingConfig = vipConfigService.getByLevel(config.getVipLevel());

        if (existingConfig != null) {
            // 检查佣金加成变更幅度
            int oldCommission = existingConfig.getFixedCommission();
            int newCommission = config.getFixedCommission();

            if (Math.abs(newCommission - oldCommission) > oldCommission * 0.5) {
                log.warn("VIP配置变更幅度较大: level={}, old={}, new={}",
                        config.getVipLevel(), oldCommission, newCommission);

                // 可以添加需要二次确认的逻辑
            }
        }
    }
}
```

### 10.5 VIP数据权限控制

#### 10.5.1 基于代理层级的数据权限
```java
/**
 * VIP数据权限控制服务
 */
@Service
public class VipDataPermissionService {

    /**
     * 检查VIP数据查看权限
     */
    public void checkVipDataViewPermission(Long currentUserId, String targetAgentCode) {
        AgentAccount currentUser = getAgentByUserId(currentUserId);
        AgentAccount target = getAgentByCode(targetAgentCode);

        // 1. 管理员可以查看所有数据
        if (isAdmin(currentUser)) {
            return;
        }

        // 2. 用户可以查看自己的VIP数据
        if (currentUser.getAgentCode().equals(targetAgentCode)) {
            return;
        }

        // 3. 上级可以查看下级的VIP数据
        if (isParentAgent(currentUser.getAgentCode(), targetAgentCode)) {
            return;
        }

        throw new ServiceException("权限不足，无法查看目标用户的VIP数据");
    }

    /**
     * 获取用户可查看的VIP数据范围
     */
    public List<String> getVipDataViewScope(Long userId) {
        AgentAccount user = getAgentByUserId(userId);
        List<String> scope = new ArrayList<>();

        // 管理员可以查看所有数据
        if (isAdmin(user)) {
            return null; // null表示无限制
        }

        // 添加自己的数据权限
        scope.add(user.getAgentCode());

        // 添加下级代理的数据权限
        List<AgentAccount> subAgents = getSubAgents(user.getAgentCode());
        for (AgentAccount sub : subAgents) {
            scope.add(sub.getAgentCode());
            // 递归添加下级的下级（最多3级）
            addSubAgentScope(sub.getAgentCode(), scope, 0);
        }

        return scope;
    }

    /**
     * 构建数据权限SQL条件
     */
    public String buildDataPermissionSql(Long userId, String agentCodeField) {
        List<String> scope = getVipDataViewScope(userId);

        if (scope == null) {
            return ""; // 管理员无限制
        }

        return String.format(" AND %s IN ('%s')",
                agentCodeField, String.join("','", scope));
    }

    private void addSubAgentScope(String parentCode, List<String> scope, int level) {
        if (level >= 3) return; // 最多3级

        List<AgentAccount> subAgents = getSubAgents(parentCode);
        for (AgentAccount sub : subAgents) {
            scope.add(sub.getAgentCode());
            addSubAgentScope(sub.getAgentCode(), scope, level + 1);
        }
    }

    private List<AgentAccount> getSubAgents(String parentCode) {
        return agentAccountMapper.selectList(
            new QueryWrapper<AgentAccount>().eq("parent_agent_code", parentCode));
    }
}
```

### 10.6 安全配置和部署

#### 10.6.1 Spring Security VIP权限配置
```java
/**
 * VIP功能安全配置
 */
@Configuration
@EnableWebSecurity
public class VipSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    private VipSecurityAspect vipSecurityAspect;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            // VIP管理接口权限配置
            .antMatchers("/api/vip/config/**").hasAnyRole("ADMIN", "VIP_MANAGER")
            .antMatchers("/api/vip/agent/setLevel").hasAnyRole("ADMIN", "AGENT")
            .antMatchers("/api/vip/commission/calculate").hasAnyRole("ADMIN", "AGENT", "USER")
            .antMatchers("/api/vip/upgrade/**").hasAnyRole("ADMIN", "AGENT", "USER")

            // VIP数据查询接口
            .antMatchers("/api/vip/agent/list").hasAnyRole("ADMIN", "VIP_MANAGER")
            .antMatchers("/api/vip/analysis/**").hasAnyRole("ADMIN", "VIP_MANAGER")

            // 其他接口配置
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
            .csrf().disable();
    }
}
```

#### 10.6.2 VIP安全监控和告警
```java
/**
 * VIP安全监控服务
 */
@Service
public class VipSecurityMonitorService {

    @Autowired
    private VipOperationLogService vipOperationLogService;

    /**
     * 检查异常VIP操作
     */
    @Scheduled(fixedRate = 300000) // 每5分钟检查一次
    public void checkAbnormalVipOperations() {
        try {
            // 1. 检查频繁VIP等级设置操作
            checkFrequentVipLevelSet();

            // 2. 检查异常VIP佣金计算
            checkAbnormalVipCommission();

            // 3. 检查权限违规操作
            checkPermissionViolations();

        } catch (Exception e) {
            log.error("VIP安全监控检查失败", e);
        }
    }

    /**
     * 检查频繁VIP等级设置操作
     */
    private void checkFrequentVipLevelSet() {
        List<VipOperationLog> recentLogs = vipOperationLogMapper.selectList(
            new QueryWrapper<VipOperationLog>()
                .eq("operation_type", "VIP_LEVEL_CHANGE")
                .ge("operation_time", LocalDateTime.now().minusMinutes(30))
                .orderByDesc("operation_time")
                .last("100")
        );

        // 统计每个用户的操作次数
        Map<Long, Long> userOperationCount = recentLogs.stream()
            .collect(Collectors.groupingBy(
                VipOperationLog::getUserId,
                Collectors.counting()
            ));

        // 检查异常频繁操作
        userOperationCount.entrySet().stream()
            .filter(entry -> entry.getValue() > 10) // 30分钟内超过10次操作
            .forEach(entry -> {
                log.warn("检测到频繁VIP等级设置操作: userId={}, count={}",
                        entry.getKey(), entry.getValue());
                // 发送告警
                sendSecurityAlert("频繁VIP等级设置操作", entry.getKey(), entry.getValue());
            });
    }

    /**
     * 检查异常VIP佣金计算
     */
    private void checkAbnormalVipCommission() {
        // 检查VIP佣金计算失败率过高的情况
        List<VipOperationLog> calcLogs = vipOperationLogMapper.selectList(
            new QueryWrapper<VipOperationLog>()
                .eq("operation_type", "VIP_COMMISSION_CALCULATION")
                .isNotNull("error_message")
                .ge("operation_time", LocalDateTime.now().minusMinutes(60))
        );

        if (calcLogs.size() > 50) {
            log.warn("检测到异常VIP佣金计算: 失败次数={}", calcLogs.size());
            sendSecurityAlert("VIP佣金计算异常", null, calcLogs.size());
        }
    }

    /**
     * 检查权限违规操作
     */
    private void checkPermissionViolations() {
        List<VipOperationLog> violationLogs = vipOperationLogMapper.selectList(
            new QueryWrapper<VipOperationLog>()
                .like("result", "权限不足")
                .ge("operation_time", LocalDateTime.now().minusMinutes(60))
        );

        if (violationLogs.size() > 20) {
            log.warn("检测到权限违规操作: 违规次数={}", violationLogs.size());
            sendSecurityAlert("VIP权限违规操作", null, violationLogs.size());
        }
    }

    /**
     * 发送安全告警
     */
    private void sendSecurityAlert(String alertType, Object target, Object count) {
        // 实现告警发送逻辑（邮件、短信、钉钉等）
        log.error("VIP安全告警: type={}, target={}, count={}", alertType, target, count);

        // 可以集成具体的告警通知系统
        // alertService.sendAlert(alertType, target, count);
    }
}
```

## 十一、实施计划

### 11.1 开发阶段安排

#### 第一阶段：数据库设计和基础功能（5天）
- **Day 1**: 数据库表结构设计和创建
  - 创建VIP配置表、升级日志表、操作审计表
  - 扩展代理商账户表、订单佣金表
  - 创建必要的索引和约束
  - 编写数据迁移脚本
- **Day 2**: 实体类和Mapper接口开发
  - 开发VIP相关实体类（VipConfig、VipUpgradeLog等）
  - 扩展现有实体类（AgentAccount、OrderCommission等）
  - 开发对应的Mapper接口和XML映射文件
- **Day 3**: 基础Service和Controller开发
  - 开发VIP配置管理基础服务
  - 开发代理商VIP管理基础服务
  - 开发基础的Controller接口
- **Day 4**: 权限控制和安全机制开发
  - 实现VIP安全注解和切面
  - 开发权限验证服务
  - 集成Spring Security配置
- **Day 5**: 单元测试和基础功能验证
  - 编写单元测试用例
  - 验证基础CRUD功能
  - 验证权限控制机制

#### 第二阶段：VIP核心功能开发（8天）
- **Day 1-2**: VIP配置管理功能完整开发
  - VIP配置的增删改查功能
  - VIP配置的缓存机制
  - VIP配置变更的影响评估
- **Day 3-4**: 用户VIP等级管理功能开发
  - 代理商VIP等级设置功能
  - VIP等级自动升级机制
  - VIP升级日志记录
- **Day 5**: VIP佣金计算逻辑完整开发
  - 固定金额VIP加成计算
  - 多级代理商佣金分配逻辑
  - 边界条件处理和异常处理
- **Day 6**: 订单触发升级机制开发和集成
  - 订单完成后VIP升级检查
  - 异步处理VIP升级
  - 与现有订单系统集成
- **Day 7**: VIP操作审计和日志系统
  - 完整的操作日志记录
  - 安全监控和告警机制
  - 防刷和频率限制
- **Day 8**: 核心功能集成测试和性能优化
  - 端到端功能测试
  - 性能瓶颈识别和优化
  - 缓存策略调优

#### 第三阶段：前端界面开发（7天）
- **Day 1-2**: 电脑版VIP管理界面完整开发
  - VIP配置管理界面
  - 用户VIP等级管理界面
  - VIP数据分析和报表界面
- **Day 3-4**: 手机端VIP页面开发
  - VIP等级展示页面
  - VIP升级进度页面
  - VIP佣金明细页面
- **Day 5**: 前后端接口联调和数据校验
  - 接口对接和数据格式校验
  - 错误处理和用户提示
  - 权限控制前端实现
- **Day 6**: 前端功能优化和用户体验提升
  - 界面响应性优化
  - 交互体验改进
  - 移动端适配优化
- **Day 7**: 前端安全性和权限控制
  - 前端权限验证
  - 敏感操作二次确认
  - 防止前端篡改机制

#### 第四阶段：测试和质量保证（6天）
- **Day 1**: 功能测试和边界条件测试
  - 完整功能测试用例执行
  - 边界条件和异常场景测试
  - 权限控制功能测试
- **Day 2**: 性能测试和压力测试
  - VIP佣金计算性能测试
  - 高并发场景压力测试
  - 数据库性能优化验证
- **Day 3**: 安全测试和渗透测试
  - 权限绕过测试
  - SQL注入和XSS防护测试
  - 接口安全测试
- **Day 4**: 集成测试和兼容性测试
  - 与现有系统集成测试
  - 数据迁移验证测试
  - 向后兼容性测试
- **Day 5**: 用户验收测试(UAT)
  - 客户参与的验收测试
  - 业务流程完整性验证
  - 用户操作体验测试
- **Day 6**: 测试问题修复和回归测试
  - 测试发现问题的修复
  - 回归测试验证
  - 发布前最终检查

#### 第五阶段：部署上线和监控（4天）
- **Day 1**: 生产环境准备和数据迁移
  - 生产环境部署准备
  - 数据库迁移脚本执行
  - 配置文件和环境变量设置
- **Day 2**: 灰度发布和功能验证
  - 小范围用户灰度测试
  - 功能正确性验证
  - 性能指标监控
- **Day 3**: 全量发布和实时监控
  - 全量用户发布
  - 实时监控和告警
  - 问题快速响应机制
- **Day 4**: 上线后监控和优化
  - 线上问题处理
  - 性能调优
  - 用户反馈收集和处理

### 11.2 风险识别和控制措施

#### 11.2.1 技术风险控制

**数据库性能风险**
- **风险描述**: VIP功能可能增加数据库查询负担，影响现有系统性能
- **控制措施**:
  - 为VIP相关表添加复合索引，优化查询性能
  - 实现VIP配置的Redis缓存机制，减少数据库访问
  - 采用异步处理VIP升级和佣金计算
  - 设置数据库查询超时和熔断机制

**系统集成风险**
- **风险描述**: VIP功能与现有订单、佣金系统集成可能产生冲突
- **控制措施**:
  - 采用扩展表结构方式，不修改原有核心表
  - 实现功能开关，支持VIP功能的快速启用/禁用
  - 充分测试向后兼容性，确保现有功能不受影响
  - 准备快速回滚方案，可在30分钟内回滚到原版本

**并发安全风险**
- **风险描述**: 高并发场景下VIP等级升级和佣金计算可能出现数据不一致
- **控制措施**:
  - 使用分布式锁防止并发修改VIP等级
  - 采用乐观锁机制处理佣金计算冲突
  - 实现幂等性接口，防止重复操作
  - 添加数据一致性校验和修复机制

#### 11.2.2 业务风险控制

**佣金计算错误风险**
- **风险描述**: VIP固定加成计算错误可能导致佣金损失或用户投诉
- **控制措施**:
  - 实现多重校验机制，确保计算结果正确性
  - 添加详细的计算日志，便于问题追踪和审计
  - 设置异常告警，当佣金计算出现异常时及时通知
  - 提供佣金核算工具，支持人工核对和调整

**VIP等级升级异常风险**
- **风险描述**: 自动升级机制可能出现误升级或升级失败
- **控制措施**:
  - 实现升级前置条件检查，确保升级合理性
  - 添加升级频率限制，防止频繁升级
  - 记录详细的升级日志，支持升级历史追溯
  - 提供手动干预机制，支持管理员修正异常升级

**权限控制失效风险**
- **风险描述**: VIP权限控制机制可能被绕过，导致越权操作
- **控制措施**:
  - 实现多层权限验证（前端+后端+数据库）
  - 添加敏感操作的二次确认机制
  - 记录所有权限检查日志，便于审计追踪
  - 定期进行权限安全测试，发现潜在漏洞

#### 11.2.3 运营风险控制

**用户接受度风险**
- **风险描述**: 用户对新的VIP体系可能不理解或不接受
- **控制措施**:
  - 提供详细的使用说明和FAQ文档
  - 实现用户培训和支持机制
  - 设置过渡期，逐步推广VIP功能
  - 收集用户反馈，及时调整优化策略

**数据迁移风险**
- **风险描述**: 现有数据迁移到新的VIP体系可能出现数据丢失或错误
- **控制措施**:
  - 制定详细的数据迁移方案和脚本
  - 在测试环境充分验证迁移脚本正确性
  - 创建数据备份，确保可以快速恢复
  - 实现分批迁移，降低单次迁移风险

#### 11.2.4 安全风险控制

**接口安全风险**
- **风险描述**: VIP相关接口可能被恶意调用或攻击
- **控制措施**:
  - 实现接口调用频率限制
  - 添加参数签名验证机制
  - 使用HTTPS加密传输敏感数据
  - 定期进行安全漏洞扫描和修复

**数据泄露风险**
- **风险描述**: VIP等级和佣金等敏感数据可能泄露
- **控制措施**:
  - 实现数据访问权限严格控制
  - 敏感数据脱敏显示
  - 定期审计数据访问日志
  - 建立数据泄露应急响应机制

### 11.3 质量保证体系

#### 11.3.1 代码质量标准

**代码规范要求**
- 遵循阿里巴巴Java开发规范
- 统一代码格式化和命名规范
- 强制代码审查，至少2人审查通过才能合入
- 单元测试覆盖率不低于80%，核心业务逻辑100%覆盖

**性能基准要求**
- VIP佣金计算响应时间不超过100ms
- VIP等级升级处理时间不超过500ms
- 系统整体性能下降不超过5%
- 支持1000+并发用户同时使用VIP功能

**安全合规要求**
- 通过安全扫描工具检测，无高危漏洞
- 敏感操作100%记录审计日志
- 权限控制测试通过率100%
- 数据加密传输和存储

#### 11.3.2 测试策略和方案

**单元测试**
```java
// VIP佣金计算单元测试示例
@Test
public void testVipCommissionCalculation() {
    // 测试VIP0用户佣金计算
    VipCommissionResult result0 = vipCommissionService.calculateAgentVipCommission("AGENT_VIP0", 10000);
    assertEquals(10000, result0.getTotalCommission().intValue());
    assertEquals(0, result0.getVipBonus().intValue());

    // 测试VIP3用户佣金计算
    VipCommissionResult result3 = vipCommissionService.calculateAgentVipCommission("AGENT_VIP3", 10000);
    assertEquals(11500, result3.getTotalCommission().intValue());
    assertEquals(1500, result3.getVipBonus().intValue());
}

@Test
public void testMultiLevelCommissionDistribution() {
    // 测试三级代理商佣金分配
    List<OrderVipCommission> commissions = vipCommissionService.calculateMultiLevelCommission(
        "AGENT_VIP3", 10000, "TEST_ORDER_001");

    assertEquals(3, commissions.size()); // 三级代理商链路
    assertEquals(11500, commissions.get(0).getTotalCommission().intValue()); // VIP3代理商
    assertEquals(0, commissions.get(1).getActualCommission().intValue()); // VIP2代理商（等级倒挂）
    assertEquals(500, commissions.get(2).getActualCommission().intValue()); // VIP1代理商
}
```

**集成测试**
- 前后端接口集成测试，覆盖所有VIP相关接口
- 数据库集成测试，验证数据一致性和事务完整性
- 缓存集成测试，确保缓存策略正确生效
- 消息队列集成测试，验证异步处理机制

**性能测试**
```yaml
# VIP功能性能测试场景
performance_test_scenarios:
  - name: "VIP佣金计算性能测试"
    concurrent_users: 100
    duration: 300s
    target_tps: 1000
    max_response_time: 100ms

  - name: "VIP等级升级性能测试"
    concurrent_users: 50
    duration: 180s
    target_tps: 200
    max_response_time: 500ms

  - name: "VIP配置查询性能测试"
    concurrent_users: 500
    duration: 120s
    target_tps: 5000
    max_response_time: 50ms
```

**安全测试**
- 权限绕过测试，验证所有权限控制点
- SQL注入测试，确保参数化查询正确实施
- XSS攻击测试，验证前端输入过滤和输出编码
- 接口安全测试，包括重放攻击、参数篡改等

#### 11.3.3 监控和告警体系

**业务监控指标**
```yaml
business_metrics:
  - name: "VIP等级分布"
    type: gauge
    description: "各VIP等级用户数量分布"
    labels: ["vip_level"]

  - name: "VIP升级成功率"
    type: histogram
    description: "VIP升级操作成功率统计"
    buckets: [0.5, 0.9, 0.95, 0.99, 1.0]

  - name: "VIP佣金计算准确率"
    type: gauge
    description: "VIP佣金计算结果准确性"
    target_value: 0.999

  - name: "VIP功能使用率"
    type: counter
    description: "VIP功能使用次数统计"
    labels: ["operation_type"]
```

**技术监控指标**
```yaml
technical_metrics:
  - name: "VIP接口响应时间"
    type: histogram
    target_p95: 100ms
    target_p99: 200ms

  - name: "VIP数据库查询性能"
    type: histogram
    target_p95: 50ms
    target_p99: 100ms

  - name: "VIP缓存命中率"
    type: gauge
    target_value: 0.95

  - name: "VIP功能错误率"
    type: gauge
    target_value: 0.001
```

**告警规则**
```yaml
alert_rules:
  - name: "VIP功能错误率过高"
    condition: "vip_error_rate > 0.01"
    duration: 5m
    severity: critical
    action: "立即通知开发团队"

  - name: "VIP升级失败"
    condition: "vip_upgrade_success_rate < 0.95"
    duration: 10m
    severity: warning
    action: "通知运维和产品团队"

  - name: "VIP佣金计算异常"
    condition: "vip_commission_accuracy < 0.999"
    duration: 2m
    severity: critical
    action: "立即停止相关功能并排查"
```

### 11.4 上线部署策略

#### 11.4.1 发布策略

**灰度发布方案**
```yaml
gray_release_plan:
  phase_1:
    percentage: 1%
    users: ["内部测试用户", "VIP代理商代表"]
    duration: 24小时
    success_criteria:
      - 错误率 < 0.1%
      - 响应时间 < 200ms
      - 无功能异常反馈

  phase_2:
    percentage: 10%
    users: ["部分活跃代理商", "新注册用户"]
    duration: 48小时
    success_criteria:
      - 错误率 < 0.05%
      - 用户满意度 > 95%

  phase_3:
    percentage: 50%
    users: ["大部分代理商"]
    duration: 72小时
    success_criteria:
      - 系统稳定性良好
      - 业务指标正常

  phase_4:
    percentage: 100%
    users: ["全部用户"]
    duration: 持续监控
```

**回滚策略**
```yaml
rollback_strategy:
  trigger_conditions:
    - 错误率 > 1%
    - 响应时间 > 1000ms
    - 关键功能异常
    - 用户投诉率 > 5%

  rollback_steps:
    1. 立即停止VIP功能开关
    2. 执行数据库回滚脚本
    3. 恢复应用版本
    4. 验证系统正常性
    5. 通知相关人员

  rollback_time_target: 30分钟内完成
```

#### 11.4.2 数据迁移方案

**迁移前准备**
```sql
-- 1. 数据备份
CREATE TABLE t_agent_account_backup_vip AS SELECT * FROM t_agent_account;
CREATE TABLE t_order_commission_backup_vip AS SELECT * FROM t_order_commission;
CREATE TABLE t_order_commission_details_backup_vip AS SELECT * FROM t_order_commission_details;

-- 2. 数据一致性检查
SELECT
    COUNT(*) as total_agents,
    COUNT(CASE WHEN vip_level IS NULL THEN 1 END) as null_vip_agents
FROM t_agent_account;
```

**迁移执行**
```sql
-- 3. 初始化VIP字段数据
UPDATE t_agent_account SET
    vip_level = 0,
    previous_vip_level = 0,
    total_orders = 0,
    total_commission = 0,
    vip_effective_time = NOW()
WHERE vip_level IS NULL;

-- 4. 验证迁移结果
SELECT
    vip_level,
    COUNT(*) as count,
    AVG(total_orders) as avg_orders
FROM t_agent_account
GROUP BY vip_level
ORDER BY vip_level;
```

#### 11.4.3 上线后监控计划

**24小时重点监控**
- 系统错误率和响应时间
- VIP功能使用情况统计
- 用户反馈和投诉情况
- 数据库性能指标
- 缓存命中率和性能

**7天持续监控**
- VIP等级升级趋势
- 佣金计算准确性
- 用户活跃度变化
- 业务指标对比分析
- 系统稳定性评估

**30天长期监控**
- VIP功能对业务整体影响
- 用户满意度调研
- 系统性能优化建议
- 功能改进需求收集
- 下版本迭代规划

### 11.5 预期效果和成功指标

#### 11.5.1 业务指标

**用户活跃度提升**
- 代理商月活跃度提升15%+
- VIP等级升级率达到80%+
- 用户平均使用时长增加20%+

**业务收益提升**
- 平台整体佣金收益提升10%+
- 高等级VIP用户留存率提升25%+
- 新用户转化率提升8%+

#### 11.5.2 技术指标

**系统性能**
- VIP功能接口响应时间P95 < 100ms
- 系统整体可用性 > 99.9%
- 数据库查询性能下降 < 5%

**代码质量**
- 单元测试覆盖率 > 80%
- 代码质量评分 > 8.5分
- 安全漏洞数量 = 0

#### 11.5.3 用户体验指标

**用户满意度**
- VIP功能用户满意度 > 90%
- 用户投诉率 < 1%
- 功能使用率 > 70%

**操作便利性**
- VIP等级设置操作完成时间 < 30秒
- 佣金查询响应时间 < 2秒
- 界面加载时间 < 3秒

## 十、预期效果

### 10.1 业务价值
- **提升代理商积极性**: 通过VIP等级体系激励代理商增加订单量
- **优化佣金结构**: 差异化佣金配置提高平台收益和用户满意度
- **增强用户粘性**: VIP等级体系提高用户留存率和活跃度
- **数据驱动运营**: VIP数据为运营决策提供支持

### 10.2 技术收益
- **系统架构完善**: 基于现有系统的扩展架构设计
- **代码复用**: 充分利用现有业务逻辑和组件
- **性能优化**: VIP功能的性能优化和缓存策略
- **维护性提升**: 模块化设计提高系统可维护性

### 10.3 用户体验提升
- **视觉体验**: VIP等级标识展示
- **功能体验**: 更高佣金比例
- **操作体验**: 简洁直观的VIP管理界面

## 十一、总结

本VIP功能模块设计方案基于现有叮咚号卡系统的技术架构和业务模式，完全重构了佣金分配机制，实现了基于VIP等级的固定金额加成模式。

### 核心创新点
1. **模式创新**: 移除复杂的百分比抽佣，改为简单透明的固定金额加成
2. **分配机制**: 上级代理商通过下级VIP等级差获得差价收益，激励团队发展
3. **简化配置**: `t_commission_config`表不再使用，完全基于VIP等级计算，简化维护
4. **风险控制**: 上级VIP等级≥下级时下级无收益，避免负收益情况

### 方案核心优势
1. **透明度**: 固定金额加成模式，代理商收益一目了然
2. **激励性**: VIP等级带来明确收益提升，激励代理商升级和团队扩展
3. **简洁性**: 移除百分比计算，简化系统复杂度
4. **兼容性**: 基于现有系统扩展，保证现有功能不受影响
5. **扩展性**: 模块化设计，便于后续功能扩展和维护

### 佣金分配示例验证
```
产品利润100元，三级代理商链路：
- 三级代理商zhao(VIP3)：获得100+15=115元
- 二级代理商li(VIP2)：获得max(0, (100+10)-(100+15))=0元
- 一级代理商qian(VIP1)：获得max(0, (100+10)-(100+5))=5元

当qian升级为VIP2时：
- 一级代理商qian(VIP2)：获得max(0, (100+10)-(100+10))=0元
```

### 实施价值
通过该方案的实施：
- **提升代理商积极性**: 明确的VIP等级收益，激励代理商增加订单量
- **优化团队结构**: 上级从下级VIP等级差获得收益，促进团队发展
- **简化系统维护**: 移除复杂配置，降低维护成本
- **增强用户体验**: 透明简单的佣金计算，提升用户满意度

该方案为叮咚号卡系统的持续发展和商业模式优化提供了有力支撑。