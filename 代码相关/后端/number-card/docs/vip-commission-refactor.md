# VIP等级驱动佣金结算改造方案

## 1. 现状分析
- **梯度代理产品表 (`t_agent_product`)** 以每一层代理和产品的组合记录“拿到多少”“再分多少”。后端实体 `ruoyi-common/src/main/java/com/ruoyi/common/order/entity/AgentProduct.java:18` 维护 `productCommission / revenueProductCommission / distributionProductCommission` 三段金额。
- **佣金配置表 (`t_commission_config`)** 由 `CommissionConfigServiceImpl.computeCommission` 根据代理自定义配置决定子级能拿到的金额。参见 `mc-console/src/main/java/com/ruoyi/console/service/impl/CommissionConfigServiceImpl.java:66`，默认模式是按 5% 保底（`scaleCommission = 5`）。
- **订单落单阶段** `BaseServiceImpl.middleSubmit` 会把订单的代理链写入 `Order.downstreamFatherList`；链数据由 `AgentServiceImpl.getAgentFatherList` 生成 (`mc-order/src/main/java/com/ruoyi/order/service/impl/AgentServiceImpl.java:60`)，该方法依赖各级 `AgentProductBO` 的三段金额并缓存 10 分钟。
- **结算入库阶段** `OrderCommissionServiceImpl.saveOrderCommission` (`mc-order/src/main/java/com/ruoyi/order/service/impl/order/OrderCommissionServiceImpl.java:42`) 把 `downstreamFatherList` 拆分成 `t_order_commission` 与 `t_order_commission_details` 明细，完全沿用生成链时的数值。
- **VIP 模块** 已落地 `t_vip_config / t_vip_user`（对应 `VipConfig`、`VipUser`），但目前只存档等级，不参与分佣计算；代理升级时 `VipUserServiceImpl.handleLevelChange` 仅同步 `AgentAccount.level`。
- **问题小结**
  - 佣金保留比例来源于 `t_commission_config`，与 VIP 等级无关，导致“按 VIP 定价”无法落地。
  - 佣金链缓存没有关注 VIP 变更，VIP 手动调级后 10 分钟内链条仍旧使用旧值。
  - 结算明细缺少 VIP 元数据，无法对账说明“谁因为 VIP 多拿/少拿了多少”。
  - 现有前后台配置界面仍允许代理自定义佣金比例，和“统一按 VIP 规则”冲突。

## 2. 目标与业务约束
- **统一口径**：所有产品、所有链路的分佣仅由上下游 VIP 等级决定，取消个人自定义比例。
- **可解释性**：结算明细需展示 VIP 等级、VIP 加成金额，保证对账可追溯。
- **实时性**：VIP 等级调整后，新订单与佣金链应立即生效；必要时提供批量刷新历史链的机制。
- **场景约束**：三级代理 `zhao(vip3)` 下单，原始利润 100；VIP3 额外 +15；上级 `li(vip2)` 只保留 `20 - 15 = 5`；再上级 `qian(vip1)` 拿到 `25 - 20 = 5`；若 `qian` 升为 VIP2，其额外加成压缩 `li`，直至 `li` 利润 0。

## 3. 差距与改造点
- **配置策略收口**
  - 逐步废弃 `t_commission_config` 的自定义比例，改为按 VIP 配置驱动。建议在 `t_vip_config` 中新增字段 `retain_fixed`（单位元），明确每个 VIP 等级在完成订单时的固定加成额度。
  - 保留历史数据以兼容旧订单，但新订单改走 VIP 路径，可在 `CommissionConfigServiceImpl.computeCommission` 内部首先尝试 VIP 固定加成计算，不命中时回退旧逻辑以降低切换风险。
- **链路计算调整**
  - `AgentProductServiceImpl.updateAgentProductCommission` (`mc-console/src/main/java/com/ruoyi/console/service/impl/agent/AgentProductServiceImpl.java:205`) 目前只关心个人配置，需要改为：获取代理与父级的 VIP 信息 → 基于固定加成计算本级保留金额与下发额度 → 递归同步所有子级。建议新增 `VipCommissionCalculator` 工具类封装固定加成公式，避免逻辑散落在多个服务。
  - `AgentServiceImpl.getAgentFatherList` 在构造链时应识别 VIP 信息，补充 `vipLevel`、`vipFixedBonus` 等字段（可直接扩展 `AgentCommissionJson`）。缓存 Key 需拼接链上各级 VIP 版本号，或在 VIP 变更后主动失效缓存。
- **结算明细补充字段**
  - `OrderCommissionDetails` (`ruoyi-common/src/main/java/com/ruoyi/common/order/entity/OrderCommissionDetails.java:20`) 增加 `vipLevel`、`vipBonusCommission` 字段，记录每条明细因 VIP 改动的金额。
  - `OrderCommission` 主表可追加 `vipAdjustAmount` 统计订单总加成，方便运营导出核对。
- **VIP 配置能力扩展**
  - `VipConfig` 目前已有 `fixedCommission` 字段，可直接沿用并命名为“固定加成”，确保各等级的加成金额集中配置。
  - `VipConfigServiceImpl` (`mc-console/src/main/java/com/ruoyi/console/service/impl/VipConfigServiceImpl.java:35`) 校验逻辑需调整，限制固定加成的最大值，提供批量导出供财务核对。
- **前后台联动**
  - 管理端需新增“VIP佣金策略”配置界面，取代原来的代理自定义佣金页面；前端的佣金设置入口需指向 VIP 页面。
  - 代理端佣金明细 API (`ruoyi-admin/src/main/java/com/ruoyi/web/controller/console/agent/AgentCommissionController.java`) 返回结构需要增加 VIP 字段，UI 上展示“基础收益 + VIP加成”。
- **缓存与批量刷新**
  - VIP 等级变更后，应刷新对应代理及其所有下游的 `t_agent_product` 记录，可复用 `AgentProductServiceImpl.updateAgentProductCommission` 的递归能力。
  - 若历史订单需要重算，可提供后台任务，根据订单时间段重建 `downstreamFatherList` 并重写 `t_order_commission_details`，保证账实一致。

## 4. 实施计划（建议分阶段）
1. **梳理配置**
   - 数据库层为 `t_vip_config` 增加用于记录固定加成的字段（如 `retain_fixed`）并编写迁移脚本。
   - 在 `CommissionConfigServiceImpl` 中兼容 VIP 配置读取；新增开关以便灰度切换。
2. **核心链路改造**
   - 编写 `VipCommissionCalculator`（输入：上游可分配金额、当前代理 VIP、父级 VIP；输出：本级收入、向下分配金额、VIP 加成），同时产出单元测试覆盖边界。
   - 改写 `AgentProductServiceImpl.updateAgentProductCommission` 和 `AgentServiceImpl.getAgentFatherList`，去除对 `CommissionConfig` 的直接依赖。
   - 调整缓存策略，VIP 变更后联动失效。
3. **结算明细扩展**
   - 扩展 `OrderCommissionDetails`、`OrderCommission` 字段，修改 `OrderCommissionServiceImpl.saveOrderCommission` 写入逻辑。
   - 更新导出逻辑（如 `AgentCommissionServiceImpl.exportOrderCommissionList`）以展示新增字段。
4. **前台能力与回溯工具**
   - 更新管理端页面，提供 VIP 策略配置、批量刷新工具入口。
   - 补充“历史订单重算”后台任务，必要时仅在运营确认后执行。
5. **灰度与切换**
   - 先在测试环境验证 VIP 级差是否满足财务预期，再删除/禁用旧的 `t_commission_config` 自定义入口。
   - 发布后持续监控佣金报表，与旧系统交叉核对至少一个结算周期。

## 5. 示例演算（结合业务场景）
| 代理 | 等级 | 基础收入 (旧逻辑) | VIP固定加成 | 新收入 | 上级可分配余额 |
| --- | --- | --- | --- | --- | --- |
| zhao | VIP3 | 100 | +15 | 115 | `commission_parent - 115` |
| li   | VIP2 | 20  | -15（被下级占用） | 5 | `commission_grand_parent - 20` |
| qian | VIP1 | 5   | 0 | 5 | - |

若 `qian` 升为 VIP2，需要从 `li` 继续挪出 5 元：
- 重新计算 `qian` 保留 `5 + vipBonus(VIP2)`，假设 VIP2 加成为 5，则 `li` 留存被压到 0。
- 公式示意：
  - `retain(agent) = min(incoming, baseRetain(agent) + vipFixed(agent))`
  - 仅支持固定加成，`vipFixed(agent)` 来源于 `VipConfig`；不足部分向上游继续扣减直至为零，不再使用比例配置。

## 6. 风险与验证
- **暴露风险**
  - VIP 配置缺失时需要兜底策略，否则链路会返回 0 造成全链断崖。
  - 历史订单若不重算，会出现“新逻辑报表与旧账不一致”的窗口期。
  - 多层代理、跨产品场景需测试上限（默认 5 层，见 `BaseConstant.AGENT_LEVEL_INT`）。
- **验证建议**
  1. 编写单测覆盖不同 VIP 等级组合（含保留额不足、父级不够扣的场景）。
  2. 构造 2~5 层代理链，校验 API 输出与预期一致。
  3. 回归订单提交流程，确认 `downstreamFatherList` 数值与 VIP 调整一致。
  4. 导出报表，与预计公式对账。

## 7. 数据迁移与兼容
- 新增字段通过 `sql/Vx.y.z` 升级脚本发版，迁移时将历史 `CommissionConfig` 逐条映射至对应的 VIP 等级策略（可按当前等级聚合求平均，为后续运营确认提供参考）。
- 迁移完成后关闭代理自定义佣金入口，保留表结构用于审计（或后续彻底删除）。
- 若需要重算历史佣金，可按订单时间批次执行“链路重构 + 结算重写”，操作前需备份相关表。

## 8. 待确认事项
- 固定加成额度是否存在全局上限或分运营商、分产品的差异化配置需求？
- VIP 加成是否允许跨层级消耗（下级加成不足由上上级兜底），需业务给出明确边界。
- 旧订单是否需要实时重算？若否，需在报表中明确切换时间点。
- 前台展示是否需要列出“VIP加成”单独字段，便于代理自查。
