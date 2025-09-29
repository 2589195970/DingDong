# 代理商排行榜API文档

## 功能概述

代理商排行榜系统支持多维度统计，包括佣金排行、订单量排行、激活量排行、团队发展排行等，支持日榜、月榜以及自定义时间范围查询。

## API接口

### 1. 获取代理商日榜

**接口地址：** `GET /console/agent/ranking/daily`

**请求参数：**
- `rankingType` (必填)：排行类型
  - 1：佣金排行
  - 2：订单量排行
  - 3：激活量排行
  - 4：团队发展排行
- `topCount` (可选)：查询TOP数量，默认10，最大100
- `parentAgentCode` (可选)：父代理商编码，用于查询团队内部排行

**请求示例：**
```bash
GET /console/agent/ranking/daily?rankingType=1&topCount=10
```

**响应示例：**
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "ranking": 1,
      "agentCode": "agent001",
      "agentName": "张三",
      "level": 2,
      "statValue": 150000,
      "statDesc": "佣金(分)",
      "statTime": 1704067200000
    },
    {
      "ranking": 2,
      "agentCode": "agent002",
      "agentName": "李四",
      "level": 1,
      "statValue": 120000,
      "statDesc": "佣金(分)",
      "statTime": 1704067200000
    }
  ]
}
```

### 2. 获取代理商月榜

**接口地址：** `GET /console/agent/ranking/monthly`

**请求参数：** 同日榜接口

**请求示例：**
```bash
GET /console/agent/ranking/monthly?rankingType=2&topCount=20
```

### 3. 获取自定义时间范围排行榜

**接口地址：** `POST /console/agent/ranking/custom`

**请求参数：**
```json
{
  "rankingType": 1,
  "topCount": 10,
  "startTime": 1704067200000,
  "endTime": 1704153600000,
  "parentAgentCode": "agent001"
}
```

**参数说明：**
- `rankingType`：排行类型（1-4）
- `topCount`：查询TOP数量
- `startTime`：开始时间戳（毫秒）
- `endTime`：结束时间戳（毫秒）
- `parentAgentCode`：父代理商编码（可选）

### 4. 获取排行榜类型说明

**接口地址：** `GET /console/agent/ranking/types`

**响应示例：**
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "1": "佣金排行 - 按代理商获得的佣金收入排序",
    "2": "订单量排行 - 按代理商完成的订单数量排序",
    "3": "激活量排行 - 按代理商激活的订单数量排序",
    "4": "团队发展排行 - 按代理商直接邀请的下级数量排序"
  }
}
```

## 权限配置

需要在系统菜单管理中配置以下权限：

```
console:agent:ranking:daily    # 日榜查询权限
console:agent:ranking:monthly  # 月榜查询权限
console:agent:ranking:custom   # 自定义查询权限
```

## 缓存策略

### 缓存配置
- **日榜缓存**：30分钟过期，每小时刷新
- **月榜缓存**：2小时过期，每天凌晨1点刷新
- **自定义查询缓存**：10分钟过期

### 缓存Key规则
- 日榜：`agentDailyRanking::{rankingType}_{topCount}_{parentAgentCode}`
- 月榜：`agentMonthlyRanking::{rankingType}_{topCount}_{parentAgentCode}`

## 性能优化

### 数据库索引
系统已自动添加以下索引：
- `t_order_commission_details.idx_agent_code_create_time`
- `t_order.idx_downstream_code_create_time`
- `t_agent_account.idx_parent_agent_code_create_time`

### 查询优化建议
1. 使用缓存接口（日榜/月榜）而非自定义查询
2. 合理设置topCount参数，避免查询过多数据
3. 团队内部排行查询时，传入具体的parentAgentCode

## 使用示例

### 前端集成示例

```javascript
// 获取佣金日榜TOP10
async function getDailyCommissionRanking() {
  const response = await axios.get('/console/agent/ranking/daily', {
    params: {
      rankingType: 1,
      topCount: 10
    }
  });
  return response.data;
}

// 获取某个代理商团队的订单量月榜
async function getTeamOrderRanking(parentAgentCode) {
  const response = await axios.get('/console/agent/ranking/monthly', {
    params: {
      rankingType: 2,
      topCount: 20,
      parentAgentCode: parentAgentCode
    }
  });
  return response.data;
}

// 查询自定义时间范围排行
async function getCustomRanking(params) {
  const response = await axios.post('/console/agent/ranking/custom', params);
  return response.data;
}
```

### 数据可视化建议

```javascript
// 排行榜数据格式化
function formatRankingData(rankings) {
  return rankings.map(item => ({
    rank: item.ranking,
    name: item.agentName,
    value: item.statValue,
    level: item.level,
    desc: item.statDesc
  }));
}

// 生成图表配置
function generateChartOption(rankings) {
  const data = formatRankingData(rankings);

  return {
    title: {
      text: '代理商排行榜'
    },
    xAxis: {
      type: 'category',
      data: data.map(item => item.name)
    },
    yAxis: {
      type: 'value'
    },
    series: [{
      type: 'bar',
      data: data.map(item => item.value)
    }]
  };
}
```

## 错误码说明

- `200`：操作成功
- `400`：参数错误
- `401`：未授权
- `403`：权限不足
- `500`：服务器内部错误

## 注意事项

1. 时间戳使用毫秒级别
2. 佣金金额单位为分
3. 排行榜实时性：日榜30分钟更新，月榜2小时更新
4. 查询TOP数量限制最大100条
5. 团队排行查询需要提供正确的parentAgentCode