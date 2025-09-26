<template>
  <view class="page-container">
    <app-navbar title="可视化数据"></app-navbar>

    <view class="container">
      <!-- 数据概览卡片 -->
      <view class="info-card">
        <view class="info-item" v-for="(item, index) in overviewData" :key="index"
              :class="{ 'has-border': index < overviewData.length - 1 }">
          <view class="item-left">{{ item.label }}</view>
          <view class="item-right">
            <text class="item-value" :class="item.valueClass">{{ item.value }}</text>
          </view>
        </view>
      </view>

      <!-- 今日数据图表卡片 -->
      <view class="info-card">
        <view class="chart-header">
          <text class="chart-title">今日数据趋势</text>
        </view>
        <view class="chart-container">
          <div ref="todayChart" class="chart-view"></div>
        </view>
      </view>

      <!-- 本月数据图表卡片 -->
      <view class="info-card">
        <view class="chart-header">
          <text class="chart-title">本月数据统计</text>
        </view>
        <view class="chart-container">
          <div ref="monthChart" class="chart-view"></div>
        </view>
      </view>

      <!-- 趋势分析卡片 -->
      <view class="info-card">
        <view class="info-item" v-for="(item, index) in trendData" :key="index"
              :class="{ 'has-border': index < trendData.length - 1 }">
          <view class="item-left">{{ item.label }}</view>
          <view class="item-right">
            <text class="item-value" :class="item.valueClass">{{ item.value }}</text>
            <text class="trend-icon" :class="item.trendClass">{{ item.trend }}</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: 'VisualData',
  data() {
    return {
      todayChart: null,
      monthChart: null,
      // 概览数据
      overviewData: [
        { label: '总邀请数', value: '1,256', valueClass: 'text-primary' },
        { label: '总订单数', value: '3,842', valueClass: 'text-primary' },
        { label: '总激活数', value: '2,567', valueClass: 'text-primary' },
        { label: '总佣金', value: '￥15,680.50', valueClass: 'text-primary' }
      ],
      // 趋势分析数据
      trendData: [
        { label: '较昨日邀请', value: '+12%', valueClass: 'income', trend: '↑', trendClass: 'trend-up' },
        { label: '较昨日订单', value: '+8%', valueClass: 'income', trend: '↑', trendClass: 'trend-up' },
        { label: '较昨日激活', value: '-3%', valueClass: 'expense', trend: '↓', trendClass: 'trend-down' },
        { label: '较昨日佣金', value: '+15%', valueClass: 'income', trend: '↑', trendClass: 'trend-up' }
      ]
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.initTodayChart();
      this.initMonthChart();
    });
  },
  methods: {
    // 初始化今日数据图表
    initTodayChart() {
      const chartElement = this.$refs.todayChart;
      if (!chartElement) return;

      this.todayChart = echarts.init(chartElement);
      const option = {
        backgroundColor: '#fff',
        grid: {
          left: '10%',
          right: '10%',
          top: '15%',
          bottom: '15%'
        },
        xAxis: {
          type: 'category',
          data: ['00:00', '04:00', '08:00', '12:00', '16:00', '20:00', '24:00'],
          axisLine: {
            lineStyle: { color: '#eaeef1' }
          },
          axisLabel: {
            color: '#909399',
            fontSize: 12
          }
        },
        yAxis: {
          type: 'value',
          axisLine: {
            lineStyle: { color: '#eaeef1' }
          },
          axisLabel: {
            color: '#909399',
            fontSize: 12
          },
          splitLine: {
            lineStyle: { color: '#f5f6f7' }
          }
        },
        series: [{
          name: '邀请数',
          type: 'line',
          data: [5, 12, 25, 18, 32, 28, 15],
          smooth: true,
          lineStyle: {
            color: '#f09b7f',
            width: 3
          },
          itemStyle: {
            color: '#f09b7f'
          },
          areaStyle: {
            color: {
              type: 'linear',
              x: 0,
              y: 0,
              x2: 0,
              y2: 1,
              colorStops: [
                { offset: 0, color: 'rgba(240, 155, 127, 0.3)' },
                { offset: 1, color: 'rgba(240, 155, 127, 0.05)' }
              ]
            }
          }
        }]
      };
      this.todayChart.setOption(option);
    },

    // 初始化本月数据图表
    initMonthChart() {
      const chartElement = this.$refs.monthChart;
      if (!chartElement) return;

      this.monthChart = echarts.init(chartElement);
      const option = {
        backgroundColor: '#fff',
        grid: {
          left: '10%',
          right: '10%',
          top: '15%',
          bottom: '15%'
        },
        xAxis: {
          type: 'category',
          data: ['第1周', '第2周', '第3周', '第4周'],
          axisLine: {
            lineStyle: { color: '#eaeef1' }
          },
          axisLabel: {
            color: '#909399',
            fontSize: 12
          }
        },
        yAxis: {
          type: 'value',
          axisLine: {
            lineStyle: { color: '#eaeef1' }
          },
          axisLabel: {
            color: '#909399',
            fontSize: 12
          },
          splitLine: {
            lineStyle: { color: '#f5f6f7' }
          }
        },
        series: [
          {
            name: '邀请数',
            type: 'bar',
            data: [45, 67, 52, 89],
            itemStyle: {
              color: '#f09b7f'
            },
            barWidth: 20
          },
          {
            name: '订单数',
            type: 'bar',
            data: [120, 145, 98, 167],
            itemStyle: {
              color: '#d87d63'
            },
            barWidth: 20
          }
        ]
      };
      this.monthChart.setOption(option);
    }
  },
  beforeDestroy() {
    if (this.todayChart) {
      this.todayChart.dispose();
    }
    if (this.monthChart) {
      this.monthChart.dispose();
    }
  }
}
</script>

<style lang="scss" scoped>
page {
  background-color: #f5f6f7;
}

.container {
  padding: 15px;
}

/* 单卡片多项目模式样式 */
.info-card {
  background-color: #fff;
  border-radius: 8rpx;
  overflow: hidden;
  margin-bottom: 20rpx;
}

.info-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 26rpx 30rpx;
  min-height: 88rpx;

  &.has-border {
    border-bottom: 1rpx solid #eaeef1;
  }

  .item-left {
    font-size: 32rpx;
    color: #333;
    flex-shrink: 0;
    width: 140rpx;
  }

  .item-right {
    display: flex;
    align-items: center;
    flex: 1;
    justify-content: flex-end;

    .item-value {
      font-size: 32rpx;
      color: #333;

      &.text-primary {
        color: #f09b7f;
        font-weight: 500;
      }

      &.income {
        color: #52c41a;
      }

      &.expense {
        color: #f5222d;
      }
    }

    .trend-icon {
      font-size: 28rpx;
      margin-left: 8rpx;
      font-weight: bold;

      &.trend-up {
        color: #52c41a;
      }

      &.trend-down {
        color: #f5222d;
      }
    }
  }
}

/* 图表相关样式 */
.chart-header {
  padding: 30rpx 30rpx 20rpx 30rpx;
  border-bottom: 1rpx solid #eaeef1;

  .chart-title {
    font-size: 32rpx;
    color: #333;
    font-weight: 500;
  }
}

.chart-container {
  padding: 30rpx;

  .chart-view {
    width: 100%;
    height: 400rpx;
  }
}
</style>