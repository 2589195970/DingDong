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
4. **自动升级**: 满足条件自动升级VIP等级
5. **升级功能**: 满足订单量条件自动升级

### 3.2 业务规则
1. **VIP等级设置**: 管理员可以给所有用户设置VIP等级
2. **代理权限**: 代理商可以给自己的子代理设置VIP等级，但不能高于自己的等级
3. **佣金差异化**: 根据VIP等级设置不同的佣金比例和固定佣金
4. **升级机制**: 基于订单数量、佣金金额等指标自动升级
5. **降级保护**: VIP等级原则上只升不降，保护用户体验

## 四、VIP功能模块设计

### 4.1 VIP等级体系设计

#### 4.1.1 VIP等级定义
```
VIP等级    等级名称    升级所需订单数    佣金加成比例
VIP0       普通会员    0                0%
VIP1       铜牌会员    10               1%
VIP2       银牌会员    50               2%
VIP3       金牌会员    200              3%
VIP4       白金会员    500              4%
VIP5       钻石会员    1000             5%
```

**说明**：
- 升级条件仅基于订单数量，不涉及佣金金额
- 管理员可以在后台调整每个VIP等级所需的订单数
- 调整后的升级条件对后续订单生效

#### 4.1.2 VIP功能说明
- **核心功能**: VIP等级影响代理商能拿到的商品佣金比例
- **权限功能**:
  - 管理员可以给所有用户设置VIP等级
  - 代理商可以给子代理设置VIP等级（≤自己的等级）
- **升级机制**: 仅基于订单数量自动升级，管理员可调整升级参数
- **等级保护**: VIP等级原则上只升不降

### 4.2 差异化佣金配置

#### 4.2.1 佣金配置表扩展
```sql
-- 扩展现有佣金配置表
ALTER TABLE t_commission_config ADD COLUMN vip_level INT DEFAULT 0 COMMENT 'VIP等级';
ALTER TABLE t_commission_config ADD COLUMN commission_rate DECIMAL(5,2) DEFAULT 0.00 COMMENT 'VIP佣金比例';
ALTER TABLE t_commission_config ADD COLUMN commission_discount DECIMAL(5,2) DEFAULT 0.00 COMMENT 'VIP佣金折扣';
```

#### 4.2.2 VIP佣金计算逻辑
```java
public Integer computeVipCommission(String agentCode, Integer originalCommission, Integer vipLevel) {
    // 1. 查询基础佣金配置
    CommissionConfig baseConfig = selectByAgentCode(agentCode);

    // 2. 计算基础分销佣金（扣除给上级的部分）
    Integer baseDistributionCommission = computeCommission(agentCode, originalCommission);

    // 3. 查询VIP等级对应的加成比例
    VipConfig vipConfig = selectVipConfig(vipLevel);

    // 4. 计算VIP加成后的佣金
    if (vipConfig != null && vipConfig.getCommissionRate() > 0) {
        // VIP加成：在原有佣金基础上增加比例
        Integer vipBonus = Math.ceil((originalCommission * vipConfig.getCommissionRate()) / 100);
        return baseDistributionCommission + vipBonus;
    }

    return baseDistributionCommission;
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

#### 5.1.1 VIP配置表
```sql
CREATE TABLE t_vip_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    vip_level INT NOT NULL COMMENT 'VIP等级',
    level_name VARCHAR(50) NOT NULL COMMENT '等级名称',
    required_orders INT DEFAULT 0 COMMENT '升级所需订单数',
    commission_rate DECIMAL(5,2) DEFAULT 0.00 COMMENT '佣金加成比例(%)',
    level_icon VARCHAR(200) COMMENT '等级图标',
    is_enabled TINYINT DEFAULT 1 COMMENT '是否启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注',
    UNIQUE KEY uk_vip_level (vip_level)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='VIP配置表';
```

#### 5.1.2 用户VIP记录表
```sql
CREATE TABLE t_user_vip (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    agent_code VARCHAR(50) COMMENT '代理商编码',
    vip_level INT DEFAULT 0 COMMENT '当前VIP等级',
    previous_level INT DEFAULT 0 COMMENT '上一等级',
    upgrade_time DATETIME COMMENT '升级时间',
    total_orders INT DEFAULT 0 COMMENT '总订单数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_user_id (user_id),
    KEY idx_agent_code (agent_code),
    KEY idx_vip_level (vip_level)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户VIP记录表';
```

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

#### 5.1.4 VIP佣金配置表
```sql
CREATE TABLE t_vip_commission_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    vip_level INT NOT NULL COMMENT 'VIP等级',
    commission_type INT DEFAULT 0 COMMENT '佣金类型(0=固定,1=比例)',
    commission_value DECIMAL(10,2) DEFAULT 0.00 COMMENT '佣金值',
    discount_type INT DEFAULT 0 COMMENT '折扣类型(0=固定金额,1=百分比)',
    discount_value DECIMAL(10,2) DEFAULT 0.00 COMMENT '折扣值',
    product_type VARCHAR(50) COMMENT '适用产品类型',
    is_enabled TINYINT DEFAULT 1 COMMENT '是否启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_vip_level (vip_level),
    KEY idx_product_type (product_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='VIP佣金配置表';
```

### 5.2 修改现有表

#### 5.2.1 扩展代理商账号表
```sql
ALTER TABLE t_agent_account
ADD COLUMN vip_level INT DEFAULT 0 COMMENT 'VIP等级',
ADD COLUMN vip_upgrade_time DATETIME COMMENT 'VIP升级时间',
ADD COLUMN vip_expire_time DATETIME COMMENT 'VIP过期时间';
```

#### 5.2.2 扩展佣金配置表
```sql
ALTER TABLE t_commission_config
ADD COLUMN vip_level INT DEFAULT 0 COMMENT '适用VIP等级',
ADD COLUMN vip_commission_rate DECIMAL(5,2) DEFAULT 0.00 COMMENT 'VIP佣金比例',
ADD COLUMN vip_fixed_commission INT DEFAULT 0 COMMENT 'VIP固定佣金';
```

## 六、后端实现方案

### 6.1 核心服务类设计

#### 6.1.1 VIP配置服务
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

#### 6.1.2 用户VIP服务
```java
@Service
public class UserVipService {

    /**
     * 获取用户VIP信息
     */
    public UserVip getUserVipInfo(Long userId) {
        UserVip userVip = userVipMapper.selectOne(new QueryWrapper<UserVip>()
            .eq("user_id", userId));

        if (userVip == null) {
            // 初始化VIP信息
            userVip = initUserVip(userId);
        }

        return userVip;
    }

    /**
     * 设置用户VIP等级
     */
    @Transactional
    public void setUserVipLevel(Long userId, Integer vipLevel, String reason, Long operatorId) {
        UserVip userVip = getUserVipInfo(userId);
        Integer oldLevel = userVip.getVipLevel();

        if (vipLevel.equals(oldLevel)) {
            return;
        }

        // 检查权限
        checkVipSetPermission(operatorId, vipLevel);

        // 更新VIP信息
        userVip.setVipLevel(vipLevel);
        userVip.setPreviousLevel(oldLevel);
        userVip.setUpgradeTime(new Date());
        userVipMapper.updateById(userVip);

        // 记录升级日志
        recordVipUpgradeLog(userId, oldLevel, vipLevel, "MANUAL", reason, operatorId);
    }

    /**
     * 自动检查VIP升级
     */
    @Scheduled(cron = "0 0 2 * * ?") // 每天凌晨2点执行
    public void autoCheckVipUpgrade() {
        // 获取需要检查的用户列表
        List<UserVip> userVipList = userVipMapper.selectList(new QueryWrapper<UserVip>()
            .lt("vip_level", 5)); // 排除已经是最高等级的用户

        for (UserVip userVip : userVipList) {
            checkAndUpgradeVip(userVip);
        }
    }

    /**
     * 检查并升级VIP
     */
    private void checkAndUpgradeVip(UserVip userVip) {
        Integer currentLevel = userVip.getVipLevel();
        VipConfig nextLevelConfig = getVipConfigByLevel(currentLevel + 1);

        if (nextLevelConfig == null) {
            return; // 已经是最高等级
        }

        // 检查升级条件
        boolean canUpgrade = checkUpgradeConditions(userVip, nextLevelConfig);

        if (canUpgrade) {
            // 执行升级
            upgradeVipLevel(userVip, currentLevel + 1, "自动升级");
        }
    }
}
```

#### 6.1.3 VIP佣金计算服务
```java
@Service
public class VipCommissionService {

    /**
     * 计算VIP佣金
     */
    public Integer calculateVipCommission(String agentCode, Integer originalCommission) {
        // 1. 获取用户VIP信息
        UserVip userVip = userVipService.getUserVipByAgentCode(agentCode);
        if (userVip == null || userVip.getVipLevel() <= 0) {
            return originalCommission; // 非VIP用户使用原佣金
        }

        // 2. 获取VIP佣金配置
        VipCommissionConfig vipConfig = getVipCommissionConfig(userVip.getVipLevel());
        if (vipConfig == null) {
            return originalCommission;
        }

        // 3. 计算VIP优惠佣金
        Integer vipCommission = originalCommission;

        // 固定佣金减免
        if (vipConfig.getDiscountType() == 0 && vipConfig.getDiscountValue() > 0) {
            vipCommission = originalCommission - vipConfig.getDiscountValue().intValue();
        }

        // 百分比佣金减免
        if (vipConfig.getDiscountType() == 1 && vipConfig.getDiscountValue() > 0) {
            Integer discount = Math.ceil((originalCommission * vipConfig.getDiscountValue()) / 100);
            vipCommission = originalCommission - discount;
        }

        // VIP佣金加成
        if (vipConfig.getCommissionType() == 1 && vipConfig.getCommissionValue() > 0) {
            Integer bonus = Math.ceil((vipCommission * vipConfig.getCommissionValue()) / 100);
            vipCommission = vipCommission + bonus;
        }

        return Math.max(vipCommission, 0);
    }
}
```

### 6.2 控制器层设计

#### 6.2.1 VIP管理控制器
```java
@RestController
@RequestMapping("/api/vip")
public class VipController {

    @Autowired
    private VipConfigService vipConfigService;

    @Autowired
    private UserVipService userVipService;

    /**
     * 获取VIP配置列表
     */
    @GetMapping("/config/list")
    @PreAuthorize("@ss.hasPermi('vip:config:list')")
    public AjaxResult getVipConfigList() {
        List<VipConfig> list = vipConfigService.getVipConfigList();
        return AjaxResult.success(list);
    }

    /**
     * 设置用户VIP等级
     */
    @PostMapping("/user/setLevel")
    @PreAuthorize("@ss.hasPermi('vip:user:setLevel')")
    public AjaxResult setUserVipLevel(@RequestBody SetVipLevelRequest request) {
        userVipService.setUserVipLevel(
            request.getUserId(),
            request.getVipLevel(),
            request.getReason(),
            SecurityUtils.getUserId()
        );
        return AjaxResult.success();
    }

    /**
     * 获取用户VIP信息
     */
    @GetMapping("/user/info")
    public AjaxResult getUserVipInfo(@RequestParam Long userId) {
        UserVip userVip = userVipService.getUserVipInfo(userId);
        return AjaxResult.success(userVip);
    }
}
```

## 七、前端实现方案

### 7.1 VIP管理页面组件

#### 7.1.1 VIP配置管理组件
```vue
<template>
  <div class="vip-config-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>VIP等级配置</span>
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
        <el-table-column prop="minOrders" label="最小订单数" width="120" />
        <el-table-column prop="minCommission" label="最小佣金金额" width="150" />
        <el-table-column prop="commissionRate" label="佣金比例" width="100">
          <template slot-scope="scope">
            {{ scope.row.commissionRate }}%
          </template>
        </el-table-column>
        <el-table-column prop="commissionDiscount" label="佣金减免" width="120" />
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
              :type="scope.row.isEnabled ? 'warning' : 'success'"
              @click="handleToggleStatus(scope.row)">
              {{ scope.row.isEnabled ? '禁用' : '启用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'VipConfig',
  data() {
    return {
      vipConfigList: []
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
      // 新增VIP等级配置
    },
    handleEdit(row) {
      // 编辑VIP等级配置
    },
    handleToggleStatus(row) {
      // 切换启用状态
    }
  }
}
</script>
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

#### 8.1.1 VIP配置管理接口
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
      "minOrders": 10,
      "commissionRate": 1.00,
      "isEnabled": 1
    }
  ]
}

// 更新VIP配置
PUT /api/vip/config/{id}
Request: {
  "levelName": "铜牌会员",
  "minOrders": 10,
  "minCommission": 1000.00,
  "commissionRate": 1.00,
  "commissionDiscount": 5.00
}

// 新增VIP配置
POST /api/vip/config
Request: {
  "vipLevel": 5,
  "levelName": "钻石会员",
  "minOrders": 500,
  "commissionRate": 5.00
}
```

#### 8.1.2 用户VIP管理接口
```java
// 获取用户VIP信息
GET /api/vip/user/info?userId={userId}
Response: {
  "code": 200,
  "data": {
    "userId": 123,
    "agentCode": "AGENT001",
    "vipLevel": 2,
    "previousLevel": 1,
    "upgradeTime": "2024-01-15 10:30:00",
    "totalOrders": 35,
    "totalCommission": 3200.00
  }
}

// 设置用户VIP等级
POST /api/vip/user/setLevel
Request: {
  "userId": 123,
  "vipLevel": 3,
  "reason": "业绩优秀，手动升级"
}

// 获取用户VIP列表（分页）
GET /api/vip/user/list?page=1&size=10&agentCode=&vipLevel=
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

#### 8.1.3 VIP佣金计算接口
```java
// 计算VIP佣金
POST /api/vip/commission/calculate
Request: {
  "agentCode": "AGENT001",
  "originalCommission": 10000,
  "productType": "NUMBER_CARD"
}

Response: {
  "code": 200,
  "data": {
    "originalCommission": 10000,
    "vipCommission": 10500,
    "commissionBonus": 500,
    "discountAmount": 0,
    "vipLevel": 2
  }
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

// 获取VIP等级规则
GET /api/app/vip/rules
Response: {
  "code": 200,
  "data": [
    {
      "level": 1,
      "name": "铜牌会员",
      "minOrders": 10,
      "commissionRate": 1.00
    }
  ]
}
```

## 九、实施计划

### 9.1 开发阶段安排

#### 第一阶段：数据库设计和基础功能（5天）
- **Day 1-2**: 数据库表结构设计和创建
- **Day 3**: 实体类和Mapper接口开发
- **Day 4**: 基础Service和Controller开发
- **Day 5**: 单元测试和基础功能验证

#### 第二阶段：VIP核心功能开发（7天）
- **Day 1-2**: VIP配置管理功能开发
- **Day 3-4**: 用户VIP等级管理功能开发
- **Day 5**: VIP佣金计算逻辑开发
- **Day 6**: 自动升级机制开发
- **Day 7**: 定时任务和日志记录功能

#### 第三阶段：前端界面开发（6天）
- **Day 1-2**: 电脑版VIP管理界面开发
- **Day 3-4**: 手机端VIP页面开发
- **Day 5**: 前后端接口联调
- **Day 6**: 前端功能和样式优化

#### 第四阶段：测试和上线（4天）
- **Day 1**: 功能测试和性能测试
- **Day 2**: 集成测试和压力测试
- **Day 3**: 数据迁移和生产环境部署
- **Day 4**: 上线监控和问题修复

### 9.2 风险控制措施

#### 9.2.1 数据安全
- **权限验证**: 严格的VIP等级设置权限控制
- **操作日志**: 完整的VIP操作日志记录
- **数据备份**: VIP相关数据的定期备份

#### 9.2.2 性能优化
- **缓存策略**: VIP配置信息缓存
- **批量处理**: 批量VIP升级操作
- **异步处理**: 佣金计算的异步处理

#### 9.2.3 兼容性保证
- **向下兼容**: 现有代理商功能不受影响
- **平滑升级**: VIP等级升级的平滑过渡
- **回滚机制**: 功能异常时的快速回滚

### 9.3 质量保证

#### 9.3.1 测试策略
- **单元测试**: 核心业务逻辑单元测试覆盖率80%+
- **集成测试**: 前后端接口集成测试
- **性能测试**: VIP功能性能压力测试
- **用户验收测试**: 客户参与的功能验收测试

#### 9.3.2 代码质量
- **代码规范**: 遵循现有代码规范和命名规则
- **代码审查**: 关键功能代码审查
- **文档完善**: 接口文档和技术文档完善
- **性能监控**: 线上性能监控和告警

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

本VIP功能模块设计方案基于现有叮咚号卡系统的技术架构和业务模式，通过完整的VIP等级体系、差异化佣金配置和权限管理机制，实现了客户需求的完整功能。

方案的核心优势：
1. **完整性**: 覆盖VIP等级管理、佣金配置、权限控制等全流程
2. **兼容性**: 基于现有系统扩展，保证现有功能不受影响
3. **扩展性**: 模块化设计，便于后续功能扩展和维护
4. **用户友好**: 电脑端和手机端完整的用户界面设计

通过该方案的实施，将有效提升代理商积极性、优化平台收益结构，为叮咚号卡系统的持续发展提供有力支撑。