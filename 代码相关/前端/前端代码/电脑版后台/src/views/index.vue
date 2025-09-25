<template>
  <div class="dashboard-container">
    <!-- 顶部区域 -->
    <div class="top-section">
      <!-- 左侧：产品数据图表 -->
      <div class="left-panel">
        <div class="card-box">
          <div class="chart-header">
            <h3 class="section-title">产品数据</h3>
            <el-button 
              type="text" 
              icon="el-icon-refresh" 
              @click="getProductData"
              :loading="productLoading"
              class="refresh-btn"
            >
              刷新
            </el-button>
          </div>
          <ECharts 
            :options="productChartOptions" 
            :height="'calc(100% - 60px)'"
            :loading="productLoading"
            loading-text="产品数据加载中..."
            class="product-chart"
          />
        </div>
      </div>
      
      <!-- 中间：数据统计面板 -->
      <div class="middle-panel">
        <div class="card-box">
          <div class="data-header">
            <h3 class="section-title">数据统计</h3>
            <el-button 
              type="text" 
              icon="el-icon-refresh" 
              @click="getOrderData"
              :loading="orderLoading"
              class="refresh-btn"
            >
              刷新
            </el-button>
          </div>
          <div class="data-section" v-loading="orderLoading" element-loading-text="数据加载中...">
            <div class="data-group">
              <h3 class="data-title">今日/昨日数据</h3>
              <div class="data-row">
                <el-tag class="period-tag" type="success" size="small">今日</el-tag>
                <span class="data-item">订单：{{ formatNumber(orderData.today.totalOrders) }}</span>
                <span class="data-item">激活：{{ formatNumber(orderData.today.activatedOrders) }}</span>
                <span class="data-item">佣金：{{ formatNumber(orderData.today.settledOrders) }}</span>
                <span class="data-item">代理：{{ formatNumber(orderData.today.agentCount) }}</span>
              </div>
              <div class="data-row">
                <el-tag class="period-tag" type="primary" size="small">昨日</el-tag>
                <span class="data-item">订单：{{ formatNumber(orderData.yesterday.totalOrders) }}</span>
                <span class="data-item">激活：{{ formatNumber(orderData.yesterday.activatedOrders) }}</span>
                <span class="data-item">佣金：{{ formatNumber(orderData.yesterday.settledOrders) }}</span>
                <span class="data-item">代理：{{ formatNumber(orderData.yesterday.agentCount) }}</span>
              </div>
            </div>
            <div class="data-group">
              <h3 class="data-title">本月/上月数据</h3>
              <div class="data-row">
                <el-tag class="period-tag" type="success" size="small">本月</el-tag>
                <span class="data-item">订单：{{ formatNumber(orderData.thisMonth.totalOrders) }}</span>
                <span class="data-item">激活：{{ formatNumber(orderData.thisMonth.activatedOrders) }}</span>
                <span class="data-item">佣金：{{ formatNumber(orderData.thisMonth.settledOrders) }}</span>
                <span class="data-item">代理：{{ formatNumber(orderData.thisMonth.agentCount) }}</span>
              </div>
              <div class="data-row">
                <el-tag class="period-tag" type="primary" size="small">上月</el-tag>
                <span class="data-item">订单：{{ formatNumber(orderData.lastMonth.totalOrders) }}</span>
                <span class="data-item">激活：{{ formatNumber(orderData.lastMonth.activatedOrders) }}</span>
                <span class="data-item">佣金：{{ formatNumber(orderData.lastMonth.settledOrders) }}</span>
                <span class="data-item">代理：{{ formatNumber(orderData.lastMonth.agentCount) }}</span>
              </div>
            </div>
            <div class="data-group">
              <h3 class="data-title">本年度数据</h3>
              <div class="data-row">
                <el-tag class="period-tag" type="success" size="small">本年</el-tag>
                <span class="data-item">订单：{{ formatNumber(orderData.thisYear.totalOrders) }}</span>
                <span class="data-item">激活：{{ formatNumber(orderData.thisYear.activatedOrders) }}</span>
                <span class="data-item">佣金：{{ formatNumber(orderData.thisYear.settledOrders) }}</span>
                <span class="data-item">代理：{{ formatNumber(orderData.thisYear.agentCount) }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 右侧：系统通知 -->
      <div class="right-panel">
        <div class="card-box">
          <div class="notification-section">
            <h3 class="section-title">系统通知</h3>
            <div class="notification-list">
              <div class="notification-item" v-for="(item, index) in notifications" :key="index">
                <div class="notification-bar"></div>
                <div class="notification-content">
                  <div class="notification-text">{{ item.text }}</div>
                  <div class="notification-date">{{ item.date }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 底部区域 -->
    <div class="bottom-section">
      <!-- 左侧：订单趋势图 -->
      <div class="left-panel">
        <div class="card-box">
          <div class="chart-header">
            <h3 class="section-title">订单趋势图</h3>
            <el-button 
              type="text" 
              icon="el-icon-refresh" 
              @click="getOrderTrendData"
              :loading="orderTrendLoading"
              class="refresh-btn"
            >
              刷新
            </el-button>
          </div>
          <ECharts 
            :options="orderTrendOptions" 
            :height="'calc(100% - 60px)'"
            :loading="orderTrendLoading"
            loading-text="订单趋势数据加载中..."
            class="trend-chart"
          />
        </div>
      </div>
      
      <!-- 右侧：今日代理排名 -->
      <div class="right-panel">
        <div class="card-box">
          <div class="ranking-section">
            <div class="chart-header">
              <h3 class="section-title">今日代理排名</h3>
              <el-button 
                type="text" 
                icon="el-icon-refresh" 
                @click="getAgentRankingData"
                :loading="agentRankingLoading"
                class="refresh-btn"
              >
                刷新
              </el-button>
            </div>
            <ECharts 
              :options="agentRankingOptions" 
              :height="'calc(100% - 60px)'"
              :loading="agentRankingLoading"
              loading-text="代理排名数据加载中..."
              class="ranking-chart"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ECharts from '@/components/ECharts'
import { getProductCategoryCount } from '@/api/product'
import { getOrderStatistics, getTodayAgentOrderRanking, getOrderTrend } from '@/api/order'

export default {
  name: 'Index',
  components: {
    ECharts
  },
  data() {
    return {
      // 产品数据
      productData: {
        productTypeCount: {
          dailySettlement: 0,
          monthlyStatement: 0,
          longTime: 0,
          other: 0,
          combination: 0
        },
        totalCount: 0
      },
      // 加载状态
      productLoading: false,
      orderLoading: false,
      agentRankingLoading: false,
      orderTrendLoading: false,
      // 代理排名数据
      agentRankingData: {
        agentRankingList: [],
        totalAgents: 0
      },
      // 订单趋势数据
      orderTrendData: [],
      // 订单统计数据
      orderData: {
        today: {
          totalOrders: 0,
          activatedOrders: 0,
          settledOrders: 0,
          pendingSettlementOrders: 0,
          agentCount: 0
        },
        yesterday: {
          totalOrders: 0,
          activatedOrders: 0,
          settledOrders: 0,
          pendingSettlementOrders: 0,
          agentCount: 0
        },
        thisMonth: {
          totalOrders: 0,
          activatedOrders: 0,
          settledOrders: 0,
          pendingSettlementOrders: 0,
          agentCount: 0
        },
        lastMonth: {
          totalOrders: 0,
          activatedOrders: 0,
          settledOrders: 0,
          pendingSettlementOrders: 0,
          agentCount: 0
        },
        thisYear: {
          totalOrders: 0,
          activatedOrders: 0,
          settledOrders: 0,
          pendingSettlementOrders: 0,
          agentCount: 0
        }
      },
      // 产品数据图表配置
      productChartOptions: {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          top: '10%',
          left: 'center'
        },
        series: [
          {
            name: '产品数据',
            type: 'pie',
            radius: ['35%', '55%'],
            center: ['50%', '50%'],
            avoidLabelOverlap: true,
            itemStyle: {
              borderRadius: 8,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: true,
              position: 'outside',
              formatter: '{d}%',
              fontSize: 14,
              fontWeight: 'bold',
              color: '#333'
            },
            labelLine: {
              show: true,
              length: 15,
              length2: 10,
              smooth: true
            },
            data: []
          }
        ],
        graphic: [
          {
            type: 'text',
            left: 'center',
            top: '45%',
            style: {
              text: '产品总数',
              fontSize: 12,
              fontWeight: 'bold',
              fill: '#666',
              textAlign: 'center'
            }
          },
          {
            type: 'text',
            left: 'center',
            top: '52%',
            style: {
              text: '0',
              fontSize: 16,
              fontWeight: 'bold',
              fill: '#333',
              textAlign: 'center'
            }
          }
        ]
      },
      // 订单趋势图配置
      orderTrendOptions: {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['总订单', '激活订单', '有效订单'],
          top: '5%'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: []
        },
        yAxis: {
          type: 'value',
          name: '数量'
        },
        series: [
          {
            name: '总订单',
            type: 'line',
            data: [],
            itemStyle: {
              color: '#409EFF'
            },
            lineStyle: {
              width: 3
            },
            symbol: 'circle',
            symbolSize: 6
          },
          {
            name: '激活订单',
            type: 'bar',
            data: [],
            itemStyle: {
              color: '#67C23A'
            },
            barWidth: '20%',
            barGap: '10%'
          },
          {
            name: '有效订单',
            type: 'bar',
            data: [],
            itemStyle: {
              color: '#E6A23C'
            },
            barWidth: '20%',
            barGap: '10%'
          }
        ]
      },
      // 系统通知数据
      notifications: [
        {
          text: '产品下架: 9...',
          date: '2025-07-21'
        },
        {
          text: '产品下架: 测...',
          date: '2025-07-20'
        },
        {
          text: '新品上新: 9...',
          date: '2025-07-20'
        }
      ],
      // 代理排名图表配置
      agentRankingOptions: {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['激活', '订单'],
          top: '5%',
          right: '10%'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          top: '15%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: [],
          axisLabel: {
            fontSize: 12,
            color: '#666'
          }
        },
        yAxis: {
          type: 'value',
          name: '数量',
          axisLabel: {
            fontSize: 12,
            color: '#666'
          }
        },
        series: [
          {
            name: '激活',
            type: 'bar',
            data: [],
            itemStyle: {
              color: '#409EFF'
            },
            barWidth: '30%'
          },
          {
            name: '订单',
            type: 'bar',
            data: [],
            itemStyle: {
              color: '#67C23A'
            },
            barWidth: '30%'
          }
        ]
      }
    }
  },
  mounted() {
    this.getProductData()
    this.getOrderData()
    this.getAgentRankingData()
    this.getOrderTrendData()
  },
  methods: {
    // 获取产品数据
    async getProductData() {
      this.productLoading = true
      try {
        const response = await getProductCategoryCount()
        
        if (response.code === 200) {
          this.productData = response.data
          this.updateProductChart()
        } else {
          this.$message.error(response.message || '获取产品数据失败')
        }
      } catch (error) {
        console.error('获取产品数据失败:', error)
        this.$message.error('获取产品数据失败，请稍后重试')
      } finally {
        this.productLoading = false
      }
    },
    
    // 获取订单数据
    async getOrderData() {
      this.orderLoading = true
      try {
        const response = await getOrderStatistics()
        
        if (response.code === 200) {
          this.orderData = response.data
        } else {
          this.$message.error(response.message || '获取订单数据失败')
        }
      } catch (error) {
        console.error('获取订单数据失败:', error)
        this.$message.error('获取订单数据失败，请稍后重试')
      } finally {
        this.orderLoading = false
      }
    },
    
    // 获取代理排名数据
    async getAgentRankingData() {
      this.agentRankingLoading = true
      try {
        const response = await getTodayAgentOrderRanking()
        
        if (response.code === 200) {
          this.agentRankingData = response.data
          this.updateAgentRankingChart()
        } else {
          this.$message.error(response.msg || '获取代理排名数据失败')
        }
      } catch (error) {
        console.error('获取代理排名数据失败:', error)
        this.$message.error('获取代理排名数据失败，请稍后重试')
      } finally {
        this.agentRankingLoading = false
      }
    },
    
    // 获取订单趋势数据
    async getOrderTrendData() {
      this.orderTrendLoading = true
      try {
        const response = await getOrderTrend()
        
        if (response.code === 200) {
          this.orderTrendData = response.data
          this.updateOrderTrendChart()
        } else {
          this.$message.error(response.msg || '获取订单趋势数据失败')
        }
      } catch (error) {
        console.error('获取订单趋势数据失败:', error)
        this.$message.error('获取订单趋势数据失败，请稍后重试')
      } finally {
        this.orderTrendLoading = false
      }
    },
    
    // 更新代理排名图表
    updateAgentRankingChart() {
      const { agentRankingList } = this.agentRankingData
      
      if (!agentRankingList || agentRankingList.length === 0) {
        return
      }
      
      // 提取代理名称
      const agentNames = agentRankingList.map(item => item.downstreamName)
      
      // 提取激活订单数据
      const activatedOrders = agentRankingList.map(item => item.activatedOrders)
      
      // 提取总订单数据
      const totalOrders = agentRankingList.map(item => item.totalOrders)
      
      // 更新图表配置
      this.agentRankingOptions = {
        ...this.agentRankingOptions,
        xAxis: {
          ...this.agentRankingOptions.xAxis,
          data: agentNames
        },
        series: [
          {
            ...this.agentRankingOptions.series[0],
            data: activatedOrders
          },
          {
            ...this.agentRankingOptions.series[1],
            data: totalOrders
          }
        ]
      }
    },
    
    // 更新订单趋势图表
    updateOrderTrendChart() {
      if (!this.orderTrendData || this.orderTrendData.length === 0) {
        return
      }
      
      // 提取日期数据，格式化为 MM-DD
      const dates = this.orderTrendData.map(item => {
        const date = new Date(item.orderDate)
        const month = (date.getMonth() + 1).toString().padStart(2, '0')
        const day = date.getDate().toString().padStart(2, '0')
        return `${month}-${day}`
      })
      
      // 提取总订单数据
      const totalOrders = this.orderTrendData.map(item => item.totalOrders)
      
      // 提取激活订单数据
      const activatedOrders = this.orderTrendData.map(item => item.activatedOrders)
      
      // 提取有效订单数据
      const validOrders = this.orderTrendData.map(item => item.validOrders)
      
      // 更新图表配置
      this.orderTrendOptions = {
        ...this.orderTrendOptions,
        xAxis: {
          ...this.orderTrendOptions.xAxis,
          data: dates
        },
        series: [
          {
            ...this.orderTrendOptions.series[0],
            data: totalOrders
          },
          {
            ...this.orderTrendOptions.series[1],
            data: activatedOrders
          },
          {
            ...this.orderTrendOptions.series[2],
            data: validOrders
          }
        ]
      }
    },
    
    // 格式化数字
    formatNumber(num) {
      if (num >= 10000) {
        return (num / 10000).toFixed(1) + '万'
      } else if (num >= 1000) {
        return (num / 1000).toFixed(1) + 'k'
      }
      return num.toString()
    },
    
    // 更新产品图表
    updateProductChart() {
      const { productTypeCount, totalCount } = this.productData
      
      // 构建饼图数据
      const chartData = [
        { value: productTypeCount.dailySettlement, name: '日结秒返' },
        { value: productTypeCount.monthlyStatement, name: '月结产品' },
        { value: productTypeCount.longTime, name: '长期产品' },
        { value: productTypeCount.other, name: '其他产品' },
        { value: productTypeCount.combination, name: '组合返佣' }
      ].filter(item => item.value > 0) // 过滤掉数量为0的产品类型
      
      // 更新图表配置
      this.productChartOptions = {
        ...this.productChartOptions,
        series: [{
          ...this.productChartOptions.series[0],
          data: chartData
        }],
        graphic: [
          {
            ...this.productChartOptions.graphic[0]
          },
          {
            ...this.productChartOptions.graphic[1],
            style: {
              ...this.productChartOptions.graphic[1].style,
              text: totalCount.toString()
            }
          }
        ]
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  height: calc(100vh - 84px);
  padding: 20px;
  background-color: #f5f5f5;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

// 顶部区域
.top-section {
  display: flex;
  gap: 20px;
  height: 45%;
  
  .left-panel {
    flex: 1;
  }
  
  .middle-panel {
    flex: 1.5;
  }
  
  .right-panel {
    flex: 1;
  }
}

// 底部区域
.bottom-section {
  display: flex;
  gap: 20px;
  height: 45%;
  
  .left-panel {
    flex: 1.5;
  }
  
  .right-panel {
    flex: 1;
  }
}

// 卡片样式
.card-box {
  background-color: #FFFFFF;
  height: 100%;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
  box-sizing: border-box;
}

// 产品图表区域
.left-panel .card-box {
  display: flex;
  flex-direction: column;
}

.chart-header, .data-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.refresh-btn {
  padding: 0;
  font-size: 14px;
  color: #409EFF;
  
  &:hover {
    color: #66b1ff;
  }
}

.product-chart {
  height: 100%;
  width: 100%;
  flex: 1;
}

// 饼图响应式调整
@media (max-width: 1400px) {
  .product-chart {
    :deep(.echarts) {
      .graphic text:first-child {
        font-size: 10px !important; // 产品总数
      }
      .graphic text:last-child {
        font-size: 14px !important; // 数量
      }
    }
  }
}

@media (max-width: 1200px) {
  .product-chart {
    :deep(.echarts) {
      .graphic text:first-child {
        font-size: 9px !important; // 产品总数
      }
      .graphic text:last-child {
        font-size: 12px !important; // 数量
      }
    }
  }
}

@media (max-width: 1000px) {
  .product-chart {
    :deep(.echarts) {
      .graphic text:first-child {
        font-size: 8px !important; // 产品总数
      }
      .graphic text:last-child {
        font-size: 10px !important; // 数量
      }
    }
  }
}

// 趋势图表
.trend-chart {
  height: 100%;
  width: 100%;
  flex: 1;
}

// 订单趋势图区域
.left-panel .card-box {
  display: flex;
  flex-direction: column;
}

// 数据统计区域
.data-section {
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: 20px;
  overflow: hidden; // 防止内容溢出
}

.data-group {
  flex: 1;
  min-height: 0; // 确保可以收缩
  display: flex;
  flex-direction: column;
}

.data-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
  padding-bottom: 0;
  flex-shrink: 0; // 标题不收缩
}

.data-row {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
  flex-shrink: 0; // 数据行不收缩
  
  .period-tag {
    flex-shrink: 0; // 标签不收缩
    margin-right: 8px;
  }
  
  .data-item {
    flex: 1;
    text-align: center;
    color: #333;
    font-size: 14px;
    padding: 0 8px;
    white-space: nowrap; // 防止文字换行
    overflow: hidden;
    text-overflow: ellipsis; // 文字过长时显示省略号
  }
}

// 响应式布局 - 电脑端不同尺寸
@media (max-width: 1600px) {
  .data-section {
    gap: 15px;
  }
  
  .data-title {
    font-size: 16px;
    margin-bottom: 12px;
  }
  
  .data-row {
    padding: 10px 0;
    
    .period-tag {
      font-size: 12px;
    }
    
    .data-item {
      font-size: 13px;
      padding: 0 6px;
    }
  }
}

@media (max-width: 1400px) {
  .data-section {
    gap: 12px;
  }
  
  .data-title {
    font-size: 15px;
    margin-bottom: 10px;
  }
  
  .data-row {
    padding: 8px 0;
    
    .period-tag {
      font-size: 11px;
    }
    
    .data-item {
      font-size: 12px;
      padding: 0 4px;
    }
  }
}

@media (max-width: 1200px) {
  .data-section {
    gap: 10px;
  }
  
  .data-title {
    font-size: 14px;
    margin-bottom: 8px;
    padding-bottom: 6px;
  }
  
  .data-row {
    padding: 6px 0;
    
    .period-tag {
      font-size: 10px;
    }
    
    .data-item {
      font-size: 11px;
      padding: 0 3px;
    }
  }
}

@media (max-width: 1000px) {
  .data-section {
    gap: 8px;
  }
  
  .data-title {
    font-size: 13px;
    margin-bottom: 6px;
    padding-bottom: 4px;
  }
  
  .data-row {
    padding: 5px 0;
    
    .period-tag {
      font-size: 9px;
    }
    
    .data-item {
      font-size: 10px;
      padding: 0 2px;
    }
  }
}

// 超小屏幕时的特殊处理
@media (max-width: 900px) {
  .data-section {
    gap: 6px;
  }
  
  .data-title {
    font-size: 12px;
    margin-bottom: 5px;
    padding-bottom: 3px;
  }
  
  .data-row {
    padding: 4px 0;
    flex-wrap: wrap; // 允许换行
    
    .period-tag {
      width: 100%;
      margin-bottom: 2px;
      font-size: 9px;
    }
    
    .data-item {
      flex: 0 0 25%; // 每行4个数据项
      font-size: 10px;
      padding: 2px;
      text-align: left;
    }
  }
}

// 系统通知区域
.notification-section {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
  padding-bottom: 0;
}

.notification-list {
  flex: 1;
  overflow-y: auto;
}

.notification-item {
  display: flex;
  align-items: flex-start;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
  
  .notification-bar {
    width: 4px;
    height: 40px;
    background-color: #67C23A;
    margin-right: 12px;
    border-radius: 2px;
    flex-shrink: 0;
  }
  
  .notification-content {
    flex: 1;
    
    .notification-text {
      font-size: 14px;
      color: #333;
      margin-bottom: 4px;
      line-height: 1.4;
    }
    
    .notification-date {
      font-size: 12px;
      color: #999;
    }
  }
}

// 代理排名区域
.ranking-section {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.ranking-chart {
  height: 100%;
  width: 100%;
  flex: 1;
}

// 响应式设计
@media (max-width: 1600px) {
  .dashboard-container {
    padding: 18px;
    gap: 18px;
  }
  
  .top-section, .bottom-section {
    gap: 18px;
  }
  
  .card-box {
    padding: 18px;
  }
}

@media (max-width: 1400px) {
  .dashboard-container {
    padding: 15px;
    gap: 15px;
  }
  
  .top-section, .bottom-section {
    gap: 15px;
  }
  
  .card-box {
    padding: 15px;
  }
}

@media (max-width: 1200px) {
  .dashboard-container {
    padding: 12px;
    gap: 12px;
  }
  
  .top-section, .bottom-section {
    gap: 12px;
  }
  
  .card-box {
    padding: 12px;
  }
}

@media (max-width: 1000px) {
  .dashboard-container {
    padding: 10px;
    gap: 10px;
  }
  
  .top-section, .bottom-section {
    gap: 10px;
  }
  
  .card-box {
    padding: 10px;
  }
}

@media (max-width: 900px) {
  .dashboard-container {
    padding: 8px;
    gap: 8px;
  }
  
  .top-section, .bottom-section {
    gap: 8px;
  }
  
  .card-box {
    padding: 8px;
  }
}

// 超小屏幕时的布局调整
@media (max-width: 800px) {
  .top-section, .bottom-section {
    flex-direction: column;
    height: auto;
  }
  
  .top-section > div,
  .bottom-section > div {
    height: 300px;
  }
}
</style>
