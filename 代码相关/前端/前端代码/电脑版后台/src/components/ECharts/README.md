# ECharts 通用组件

一个基于 ECharts 5.x 的通用图表组件，支持所有 ECharts 图表类型和配置。

## 特性

- 🎨 支持所有 ECharts 图表类型
- 📱 响应式设计，自动调整大小
- 🎭 支持主题切换
- ⚡ 支持加载状态
- 🎯 完整的事件支持
- 🔧 灵活的配置选项

## 基本用法

```vue
<template>
  <div>
    <ECharts 
      :options="chartOptions" 
      :height="'400px'"
      @click="handleChartClick"
    />
  </div>
</template>

<script>
import ECharts from '@/components/ECharts'

export default {
  components: {
    ECharts
  },
  data() {
    return {
      chartOptions: {
        title: {
          text: '示例图表'
        },
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: [120, 200, 150, 80, 70, 110, 130],
          type: 'bar'
        }]
      }
    }
  },
  methods: {
    handleChartClick(params) {
      console.log('图表点击事件:', params)
    }
  }
}
</script>
```

## Props

| 参数 | 说明 | 类型 | 可选值 | 默认值 |
|------|------|------|--------|--------|
| options | ECharts 配置选项 | Object | — | {} |
| theme | 图表主题 | String | — | 'macarons' |
| className | 容器类名 | String | — | 'chart' |
| width | 容器宽度 | String | — | '100%' |
| height | 容器高度 | String | — | '300px' |
| autoResize | 是否启用响应式调整 | Boolean | — | true |
| loading | 是否显示加载动画 | Boolean | — | false |
| loadingText | 加载动画文本 | String | — | '加载中...' |

## Events

| 事件名 | 说明 | 回调参数 |
|--------|------|----------|
| click | 点击图表时触发 | (params) |
| dblclick | 双击图表时触发 | (params) |
| mouseover | 鼠标悬停时触发 | (params) |
| mouseout | 鼠标移出时触发 | (params) |
| legendselectchanged | 图例切换时触发 | (params) |
| datazoom | 数据区域缩放时触发 | (params) |
| geoselectchanged | 地图区域选择时触发 | (params) |
| timelinechanged | 时间轴播放时触发 | (params) |

## Methods

| 方法名 | 说明 | 参数 |
|--------|------|------|
| resize | 手动调整图表大小 | — |
| showLoading | 显示加载动画 | (options) |
| hideLoading | 隐藏加载动画 | — |
| getChart | 获取图表实例 | — |
| getOption | 获取图表配置 | — |
| setOption | 设置图表配置 | (option, notMerge, lazyUpdate) |
| clear | 清空图表 | — |

## 使用示例

### 柱状图

```vue
<ECharts 
  :options="{
    title: { text: '柱状图' },
    xAxis: { data: ['A', 'B', 'C', 'D'] },
    yAxis: {},
    series: [{
      type: 'bar',
      data: [10, 20, 30, 40]
    }]
  }"
/>
```

### 折线图

```vue
<ECharts 
  :options="{
    title: { text: '折线图' },
    xAxis: { data: ['1月', '2月', '3月', '4月'] },
    yAxis: {},
    series: [{
      type: 'line',
      data: [100, 200, 150, 300]
    }]
  }"
/>
```

### 饼图

```vue
<ECharts 
  :options="{
    title: { text: '饼图' },
    series: [{
      type: 'pie',
      data: [
        { value: 335, name: '直接访问' },
        { value: 310, name: '邮件营销' },
        { value: 234, name: '联盟广告' }
      ]
    }]
  }"
/>
```

### 带加载状态

```vue
<ECharts 
  :options="chartOptions"
  :loading="isLoading"
  loading-text="数据加载中..."
/>
```

### 自定义主题

```vue
<ECharts 
  :options="chartOptions"
  theme="dark"
/>
```

## 注意事项

1. 确保传入的 `options` 是完整的 ECharts 配置对象
2. 组件会自动处理响应式调整，无需手动调用 resize
3. 在组件销毁时会自动清理图表实例
4. 支持所有 ECharts 5.x 的配置选项和事件
