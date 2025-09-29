# 代理商排行榜接口文档 (无权限版本)

## 接口概览

| 接口名称 | 请求方式 | 接口地址 | 功能描述 |
|---------|---------|----------|----------|
| 获取日榜 | GET | `/console/agent/ranking/daily` | 获取代理商日排行榜 |
| 获取月榜 | GET | `/console/agent/ranking/monthly` | 获取代理商月排行榜 |
| 自定义查询 | POST | `/console/agent/ranking/custom` | 自定义时间范围排行榜 |
| 排行类型 | GET | `/console/agent/ranking/types` | 获取排行榜类型说明 |

---

## 1. 获取代理商日榜

### 基本信息
- **接口地址**: `GET /console/agent/ranking/daily`
- **权限要求**: 无
- **缓存时间**: 30分钟

### 请求参数

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| rankingType | int | 是 | 排行类型 | 1 |
| topCount | int | 否 | 查询TOP数量，默认10，最大100 | 10 |
| parentAgentCode | string | 否 | 父代理商编码，查询团队内部排行 | "agent001" |

**排行类型说明**:
- `1`: 佣金排行
- `2`: 订单量排行
- `3`: 激活量排行
- `4`: 团队发展排行

### 请求示例

```bash
# 获取佣金日榜TOP10
GET /console/agent/ranking/daily?rankingType=1&topCount=10

# 获取某团队的订单量日榜TOP20
GET /console/agent/ranking/daily?rankingType=2&topCount=20&parentAgentCode=agent001
```

### 响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "ranking": 1,
      "agentCode": "p6dmuwgg",
      "agentName": "石井电信",
      "level": 2,
      "statValue": 150000,
      "statDesc": "佣金(分)",
      "statTime": 1704067200000
    },
    {
      "ranking": 2,
      "agentCode": "knllel7s",
      "agentName": "叮咚通信",
      "level": 1,
      "statValue": 120000,
      "statDesc": "佣金(分)",
      "statTime": 1704067200000
    }
  ]
}
```

---

## 2. 获取代理商月榜

### 基本信息
- **接口地址**: `GET /console/agent/ranking/monthly`
- **权限要求**: 无
- **缓存时间**: 2小时

### 请求参数
参数与日榜接口相同

### 请求示例

```bash
# 获取激活量月榜TOP15
GET /console/agent/ranking/monthly?rankingType=3&topCount=15

# 获取全部代理商的团队发展月榜
GET /console/agent/ranking/monthly?rankingType=4&topCount=50
```

### 响应格式
与日榜接口响应格式相同

---

## 3. 自定义时间范围排行榜

### 基本信息
- **接口地址**: `POST /console/agent/ranking/custom`
- **权限要求**: 无
- **缓存时间**: 10分钟

### 请求参数

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| rankingType | int | 是 | 排行类型(1-4) | 1 |
| topCount | int | 否 | 查询TOP数量，默认10，最大100 | 10 |
| startTime | long | 是 | 开始时间戳(毫秒) | 1704067200000 |
| endTime | long | 是 | 结束时间戳(毫秒) | 1704153600000 |
| parentAgentCode | string | 否 | 父代理商编码 | "agent001" |

### 请求示例

```bash
POST /console/agent/ranking/custom
Content-Type: application/json

{
  "rankingType": 1,
  "topCount": 10,
  "startTime": 1704067200000,
  "endTime": 1704153600000,
  "parentAgentCode": "agent001"
}
```

### 响应格式
与日榜接口响应格式相同

---

## 4. 获取排行榜类型说明

### 基本信息
- **接口地址**: `GET /console/agent/ranking/types`
- **权限要求**: 无
- **缓存时间**: 长期缓存

### 请求示例

```bash
GET /console/agent/ranking/types
```

### 响应示例

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

---

## 响应数据结构说明

### AgentRankingVO 字段说明

| 字段名 | 类型 | 说明 | 示例值 |
|--------|------|------|--------|
| ranking | int | 排名(1开始) | 1 |
| agentCode | string | 代理商编码 | "p6dmuwgg" |
| agentName | string | 代理商名称 | "石井电信" |
| level | int | 代理商等级 | 2 |
| statValue | decimal | 统计数值 | 150000 |
| statDesc | string | 统计描述 | "佣金(分)" |
| statTime | long | 统计时间戳(毫秒) | 1704067200000 |

### 统计数值说明

| 排行类型 | statValue含义 | statDesc | 单位 |
|----------|---------------|----------|------|
| 佣金排行(1) | 代理商获得的佣金总额 | "佣金(分)" | 分 |
| 订单量排行(2) | 代理商完成的订单数量 | "订单数" | 个 |
| 激活量排行(3) | 代理商激活的订单数量 | "激活数" | 个 |
| 团队发展排行(4) | 代理商直接邀请的下级数量 | "邀请数" | 个 |

---

## 前端对接示例

### 1. 封装API请求方法

```javascript
// api/ranking.js
import request from '@/utils/request'

export default {
  // 获取日榜
  getDailyRanking(params) {
    return request({
      url: '/console/agent/ranking/daily',
      method: 'get',
      params
    })
  },

  // 获取月榜
  getMonthlyRanking(params) {
    return request({
      url: '/console/agent/ranking/monthly',
      method: 'get',
      params
    })
  },

  // 自定义时间查询
  getCustomRanking(data) {
    return request({
      url: '/console/agent/ranking/custom',
      method: 'post',
      data
    })
  },

  // 获取排行类型
  getRankingTypes() {
    return request({
      url: '/console/agent/ranking/types',
      method: 'get'
    })
  }
}
```

### 2. 调用示例

```javascript
// 在组件中使用
import rankingApi from '@/api/ranking'

export default {
  data() {
    return {
      rankingData: [],
      loading: false,
      rankingTypes: {
        1: '佣金排行',
        2: '订单量排行',
        3: '激活量排行',
        4: '团队发展排行'
      }
    }
  },

  methods: {
    // 获取佣金日榜TOP10
    async loadDailyCommissionRanking() {
      this.loading = true
      try {
        const response = await rankingApi.getDailyRanking({
          rankingType: 1,
          topCount: 10
        })
        if (response.code === 200) {
          this.rankingData = response.data
        }
      } catch (error) {
        console.error('获取排行榜失败:', error)
      } finally {
        this.loading = false
      }
    },

    // 获取团队月榜
    async loadTeamMonthlyRanking(parentAgentCode) {
      this.loading = true
      try {
        const response = await rankingApi.getMonthlyRanking({
          rankingType: 2,
          topCount: 20,
          parentAgentCode
        })
        if (response.code === 200) {
          this.rankingData = response.data
        }
      } catch (error) {
        console.error('获取团队排行榜失败:', error)
      } finally {
        this.loading = false
      }
    },

    // 自定义时间范围查询
    async loadCustomRanking(params) {
      this.loading = true
      try {
        const response = await rankingApi.getCustomRanking({
          rankingType: params.type,
          topCount: params.count,
          startTime: new Date(params.startDate).getTime(),
          endTime: new Date(params.endDate).getTime(),
          parentAgentCode: params.parentCode
        })
        if (response.code === 200) {
          this.rankingData = response.data
        }
      } catch (error) {
        console.error('获取自定义排行榜失败:', error)
      } finally {
        this.loading = false
      }
    },

    // 格式化统计数值显示
    formatStatValue(item) {
      if (item.statDesc === '佣金(分)') {
        return (item.statValue / 100).toFixed(2) + '元'
      }
      return item.statValue + item.statDesc.replace(/[()]/g, '')
    }
  }
}
```

### 3. 错误处理

```javascript
// 统一错误处理
async function handleRankingRequest(requestFn) {
  try {
    const response = await requestFn()

    if (response.code === 200) {
      return { success: true, data: response.data }
    } else {
      return { success: false, message: response.msg }
    }
  } catch (error) {
    console.error('请求失败:', error)

    if (error.response) {
      switch (error.response.status) {
        case 400:
          return { success: false, message: '参数错误：' + error.response.data.msg }
        case 500:
          return { success: false, message: '服务器错误，请稍后重试' }
        default:
          return { success: false, message: '请求失败，请稍后重试' }
      }
    }

    return { success: false, message: '网络错误，请检查网络连接' }
  }
}
```

---

## 接口测试示例

### 使用curl测试

```bash
# 1. 测试获取佣金日榜
curl -X GET "http://localhost:8080/console/agent/ranking/daily?rankingType=1&topCount=10"

# 2. 测试获取订单量月榜
curl -X GET "http://localhost:8080/console/agent/ranking/monthly?rankingType=2&topCount=20"

# 3. 测试自定义时间查询
curl -X POST "http://localhost:8080/console/agent/ranking/custom" \
  -H "Content-Type: application/json" \
  -d '{
    "rankingType": 1,
    "topCount": 10,
    "startTime": 1704067200000,
    "endTime": 1704153600000
  }'

# 4. 测试获取排行类型
curl -X GET "http://localhost:8080/console/agent/ranking/types"
```

### 使用Postman测试

**1. 日榜接口**
- Method: GET
- URL: `{{baseUrl}}/console/agent/ranking/daily`
- Params:
  - rankingType: 1
  - topCount: 10

**2. 自定义查询接口**
- Method: POST
- URL: `{{baseUrl}}/console/agent/ranking/custom`
- Headers: Content-Type: application/json
- Body (raw JSON):
```json
{
  "rankingType": 1,
  "topCount": 10,
  "startTime": 1704067200000,
  "endTime": 1704153600000
}
```

---

## 重要提醒

### 1. 无权限限制
- 所有接口均无权限限制，可直接访问
- 建议在生产环境根据需要添加适当的访问控制

### 2. 参数校验
- `rankingType` 必须在1-4范围内
- `topCount` 最大不超过100
- 自定义查询时，`startTime` 必须小于 `endTime`
- 时间戳使用毫秒级别

### 3. 性能建议
- 优先使用日榜/月榜接口，避免频繁自定义查询
- 合理设置`topCount`，避免查询过多数据
- 利用接口缓存，相同参数短时间内避免重复请求

### 4. 数据格式
- 佣金金额单位为**分**，显示时需要除以100转换为元
- 时间戳为毫秒级别，需要转换为日期格式显示
- 排名从1开始，连续递增

### 5. 接口地址
- 开发环境: `http://localhost:8080`
- 所有接口路径都以 `/console/agent/ranking` 开头