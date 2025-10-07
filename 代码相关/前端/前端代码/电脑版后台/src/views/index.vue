<template>
  <div class="app-container">
    <!-- 数据统计卡片行 -->
    <el-row :gutter="16" class="mb20">
      <el-col :span="6">
        <el-card class="box-card order-card">
          <div class="card-content">
            <div class="card-left">
              <i class="el-icon-shopping-cart-2 card-icon"></i>
            </div>
            <div class="card-right">
              <div class="card-header">
                <span>今日订单</span>
                <div class="trend-indicator" :class="getTrendClass(orderData.today.totalOrders, orderData.yesterday.totalOrders)">
                  <i :class="getTrendIcon(orderData.today.totalOrders, orderData.yesterday.totalOrders)"></i>
                  {{ getTrendPercent(orderData.today.totalOrders, orderData.yesterday.totalOrders) }}
                </div>
              </div>
              <div class="card-number">{{ formatNumber(orderData.today.totalOrders) }}</div>
              <div class="card-desc">较昨日 {{ getChangeText(orderData.today.totalOrders, orderData.yesterday.totalOrders) }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card active-card">
          <div class="card-content">
            <div class="card-left">
              <i class="el-icon-rocket card-icon"></i>
            </div>
            <div class="card-right">
              <div class="card-header">
                <span>今日激活</span>
                <div class="trend-indicator" :class="getTrendClass(orderData.today.activatedOrders, orderData.yesterday.activatedOrders)">
                  <i :class="getTrendIcon(orderData.today.activatedOrders, orderData.yesterday.activatedOrders)"></i>
                  {{ getTrendPercent(orderData.today.activatedOrders, orderData.yesterday.activatedOrders) }}
                </div>
              </div>
              <div class="card-number">{{ formatNumber(orderData.today.activatedOrders) }}</div>
              <div class="card-desc">较昨日 {{ getChangeText(orderData.today.activatedOrders, orderData.yesterday.activatedOrders) }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card commission-card">
          <div class="card-content">
            <div class="card-left">
              <i class="el-icon-wallet card-icon"></i>
            </div>
            <div class="card-right">
              <div class="card-header">
                <span>今日佣金</span>
                <div class="trend-indicator" :class="getTrendClass(orderData.today.settledOrders, orderData.yesterday.settledOrders)">
                  <i :class="getTrendIcon(orderData.today.settledOrders, orderData.yesterday.settledOrders)"></i>
                  {{ getTrendPercent(orderData.today.settledOrders, orderData.yesterday.settledOrders) }}
                </div>
              </div>
              <div class="card-number">{{ formatNumber(orderData.today.settledOrders) }}</div>
              <div class="card-desc">较昨日 {{ getChangeText(orderData.today.settledOrders, orderData.yesterday.settledOrders) }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card agent-card">
          <div class="card-content">
            <div class="card-left">
              <i class="el-icon-user-solid card-icon"></i>
            </div>
            <div class="card-right">
              <div class="card-header">
                <span>活跃代理</span>
                <div class="trend-indicator" :class="getTrendClass(orderData.today.agentCount, orderData.yesterday.agentCount)">
                  <i :class="getTrendIcon(orderData.today.agentCount, orderData.yesterday.agentCount)"></i>
                  {{ getTrendPercent(orderData.today.agentCount, orderData.yesterday.agentCount) }}
                </div>
              </div>
              <div class="card-number">{{ formatNumber(orderData.today.agentCount) }}</div>
              <div class="card-desc">较昨日 {{ getChangeText(orderData.today.agentCount, orderData.yesterday.agentCount) }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表和数据行 -->
    <el-row :gutter="20" class="mb20 equal-height-row">
      <!-- 产品数据图表 -->
      <el-col :span="7">
        <el-card class="equal-height-card">
          <div slot="header">
            <span>产品分布</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              icon="el-icon-refresh"
              @click="getProductData"
              :loading="productLoading">刷新</el-button>
          </div>
          <div class="chart-container">
            <ECharts
              :options="productChartOptions"
              height="240px"
              :loading="productLoading" />
          </div>
        </el-card>
      </el-col>

      <!-- 数据统计表格 -->
      <el-col :span="11">
        <el-card class="equal-height-card">
          <div slot="header">
            <span>数据统计</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              icon="el-icon-refresh"
              @click="getOrderData"
              :loading="orderLoading">刷新</el-button>
          </div>
          <div class="table-container">
            <el-table :data="statisticsData" size="small" stripe class="statistics-table" height="240">
              <el-table-column label="时间" width="80" align="center">
                <template slot-scope="scope">
                  <el-tag
                    :type="scope.row.period === '今日' ? 'primary' : scope.row.period === '昨日' ? 'success' : ''"
                    size="mini">
                    {{ scope.row.period }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="订单" align="right">
                <template slot-scope="scope">
                  <div class="table-cell-content">
                    <span :class="{'today-data': scope.row.period === '今日'}">
                      {{ scope.row.totalOrders }}
                    </span>
                    <div v-if="scope.row.period === '今日'" class="trend-mini">
                      <i :class="getTrendIcon(orderData.today.totalOrders, orderData.yesterday.totalOrders)"
                         :style="{color: getTrendColor(orderData.today.totalOrders, orderData.yesterday.totalOrders)}"></i>
                    </div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="激活" align="right">
                <template slot-scope="scope">
                  <div class="table-cell-content">
                    <span :class="{'today-data': scope.row.period === '今日'}">
                      {{ scope.row.activatedOrders }}
                    </span>
                    <div v-if="scope.row.period === '今日'" class="trend-mini">
                      <i :class="getTrendIcon(orderData.today.activatedOrders, orderData.yesterday.activatedOrders)"
                         :style="{color: getTrendColor(orderData.today.activatedOrders, orderData.yesterday.activatedOrders)}"></i>
                    </div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="佣金" align="right">
                <template slot-scope="scope">
                  <div class="table-cell-content">
                    <span :class="{'today-data': scope.row.period === '今日'}">
                      {{ scope.row.settledOrders }}
                    </span>
                    <div v-if="scope.row.period === '今日'" class="trend-mini">
                      <i :class="getTrendIcon(orderData.today.settledOrders, orderData.yesterday.settledOrders)"
                         :style="{color: getTrendColor(orderData.today.settledOrders, orderData.yesterday.settledOrders)}"></i>
                    </div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="代理" align="right">
                <template slot-scope="scope">
                  <div class="table-cell-content">
                    <span :class="{'today-data': scope.row.period === '今日'}">
                      {{ scope.row.agentCount }}
                    </span>
                    <div v-if="scope.row.period === '今日'" class="trend-mini">
                      <i :class="getTrendIcon(orderData.today.agentCount, orderData.yesterday.agentCount)"
                         :style="{color: getTrendColor(orderData.today.agentCount, orderData.yesterday.agentCount)}"></i>
                    </div>
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-card>
      </el-col>

      <!-- 系统通知 -->
      <el-col :span="6">
        <el-card class="equal-height-card">
          <div slot="header">
            <span>系统通知</span>
            <el-badge :value="notifications.length" :max="99" class="notification-badge" />
          </div>
          <div class="notification-container">
            <div class="notification-list" v-loading="notificationLoading">
              <div class="notification-item" v-for="(item, index) in notifications" :key="item.id || index" @click="handleNotificationClick(item)">
                <div class="notification-icon">
                  <i :class="getNotificationIcon(item.type)" :style="{color: getNotificationColor(item.type)}"></i>
                </div>
                <div class="notification-content">
                  <div class="notification-header">
                    <span class="notification-text">{{ item.text }}</span>
                    <el-tag :type="getNotificationTagType(item.type)" size="mini">{{ getNotificationTypeText(item.type) }}</el-tag>
                  </div>
                  <div class="notification-time">{{ formatRelativeTime(item.date) }}</div>
                </div>
              </div>
              <div v-if="notifications.length === 0 && !notificationLoading" class="no-notifications">
                <i class="el-icon-bell" style="font-size: 32px; color: #C0C4CC; margin-bottom: 8px;"></i>
                <div style="color: #909399; font-size: 12px;">暂无系统通知</div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 趋势图表行 -->
    <el-row :gutter="20">
      <!-- 订单趋势图 -->
      <el-col :span="16">
        <el-card>
          <div slot="header">
            <span>订单趋势</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              icon="el-icon-refresh"
              @click="getOrderTrendData"
              :loading="orderTrendLoading">刷新</el-button>
          </div>
          <ECharts
            :options="orderTrendOptions"
            height="280px"
            :loading="orderTrendLoading" />
        </el-card>
      </el-col>

      <!-- 代理排名 -->
      <el-col :span="8">
        <el-card>
          <div slot="header">
            <span>代理排名</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              icon="el-icon-refresh"
              @click="getAgentRankingData"
              :loading="agentRankingLoading">刷新</el-button>
          </div>
          <ECharts
            :options="agentRankingOptions"
            height="280px"
            :loading="agentRankingLoading" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import ECharts from '@/components/ECharts'
import { getProductCategoryCount } from '@/api/product'
import { getOrderStatistics, getTodayAgentOrderRanking, getOrderTrend } from '@/api/order'
import { listNotice } from '@/api/system/notice'

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
          formatter: '{b}: {c}个 ({d}%)'
        },
        legend: {
          bottom: '2%',
          left: 'center',
          textStyle: {
            fontSize: 11
          },
          itemWidth: 10,
          itemHeight: 10
        },
        series: [
          {
            name: '产品分布',
            type: 'pie',
            radius: ['45%', '75%'],
            center: ['50%', '42%'],
            data: [],
            itemStyle: {
              borderWidth: 2,
              borderColor: '#fff',
              shadowBlur: 10,
              shadowColor: 'rgba(0, 0, 0, 0.1)'
            },
            label: {
              show: true,
              formatter: '{d}%',
              fontSize: 11,
              color: '#606266'
            },
            labelLine: {
              show: true,
              length: 10,
              length2: 5
            },
            emphasis: {
              itemStyle: {
                shadowBlur: 20,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.2)'
              }
            }
          }
        ],
        graphic: [
          {
            type: 'text',
            left: 'center',
            top: '38%',
            style: {
              text: '产品总数',
              fontSize: 12,
              fontWeight: 'normal',
              fill: '#909399',
              textAlign: 'center'
            }
          },
          {
            type: 'text',
            left: 'center',
            top: '45%',
            style: {
              text: '0',
              fontSize: 24,
              fontWeight: 'bold',
              fill: '#303133',
              textAlign: 'center'
            }
          }
        ],
        color: ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399']
      },
      // 订单趋势图配置
      orderTrendOptions: {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            crossStyle: {
              color: '#999'
            }
          },
          backgroundColor: 'rgba(255, 255, 255, 0.95)',
          borderColor: '#E4E7ED',
          borderWidth: 1,
          textStyle: {
            color: '#606266'
          }
        },
        legend: {
          data: ['总订单', '激活订单', '有效订单'],
          top: '5%',
          textStyle: {
            fontSize: 12
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '8%',
          top: '18%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: [],
          axisLabel: {
            fontSize: 11,
            color: '#606266'
          },
          axisLine: {
            lineStyle: {
              color: '#E4E7ED'
            }
          },
          axisTick: {
            show: false
          }
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            fontSize: 11,
            color: '#606266'
          },
          axisLine: {
            show: false
          },
          axisTick: {
            show: false
          },
          splitLine: {
            lineStyle: {
              color: '#F5F7FA',
              type: 'dashed'
            }
          }
        },
        series: [
          {
            name: '总订单',
            type: 'line',
            data: [],
            smooth: true,
            symbol: 'circle',
            symbolSize: 6,
            lineStyle: {
              width: 3,
              color: '#409EFF'
            },
            itemStyle: {
              color: '#409EFF',
              borderWidth: 2,
              borderColor: '#fff'
            },
            areaStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [{
                  offset: 0, color: 'rgba(64, 158, 255, 0.3)'
                }, {
                  offset: 1, color: 'rgba(64, 158, 255, 0.05)'
                }]
              }
            }
          },
          {
            name: '激活订单',
            type: 'line',
            data: [],
            smooth: true,
            symbol: 'circle',
            symbolSize: 5,
            lineStyle: {
              width: 2,
              color: '#67C23A'
            },
            itemStyle: {
              color: '#67C23A',
              borderWidth: 2,
              borderColor: '#fff'
            },
            areaStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [{
                  offset: 0, color: 'rgba(103, 194, 58, 0.25)'
                }, {
                  offset: 1, color: 'rgba(103, 194, 58, 0.03)'
                }]
              }
            }
          },
          {
            name: '有效订单',
            type: 'line',
            data: [],
            smooth: true,
            symbol: 'circle',
            symbolSize: 5,
            lineStyle: {
              width: 2,
              color: '#E6A23C'
            },
            itemStyle: {
              color: '#E6A23C',
              borderWidth: 2,
              borderColor: '#fff'
            },
            areaStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [{
                  offset: 0, color: 'rgba(230, 162, 60, 0.25)'
                }, {
                  offset: 1, color: 'rgba(230, 162, 60, 0.03)'
                }]
              }
            }
          }
        ]
      },
      // 系统通知数据
      notifications: [],
      notificationLoading: false,
      // 代理排名图表配置
      agentRankingOptions: {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          },
          formatter: function(params) {
            let result = params[0].name + '<br/>'
            params.forEach(function(item) {
              result += item.marker + ' ' + item.seriesName + ': ' + item.value + '<br/>'
            })
            return result
          }
        },
        legend: {
          data: ['激活订单', '总订单'],
          top: '5%',
          textStyle: {
            fontSize: 11
          }
        },
        grid: {
          left: '15%',
          right: '8%',
          bottom: '8%',
          top: '20%',
          containLabel: true
        },
        xAxis: {
          type: 'value',
          axisLabel: {
            fontSize: 11,
            color: '#606266'
          },
          axisLine: {
            show: false
          },
          axisTick: {
            show: false
          },
          splitLine: {
            lineStyle: {
              color: '#F5F7FA',
              type: 'dashed'
            }
          }
        },
        yAxis: {
          type: 'category',
          data: [],
          axisLabel: {
            fontSize: 10,
            color: '#606266',
            formatter: function(value) {
              return value.length > 6 ? value.substring(0, 6) + '...' : value
            }
          },
          axisLine: {
            lineStyle: {
              color: '#E4E7ED'
            }
          },
          axisTick: {
            show: false
          }
        },
        series: [
          {
            name: '激活订单',
            type: 'bar',
            data: [],
            barHeight: 16,
            itemStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 1,
                y2: 0,
                colorStops: [{
                  offset: 0, color: '#67C23A'
                }, {
                  offset: 1, color: '#85ce61'
                }]
              },
              borderRadius: [0, 4, 4, 0]
            },
            label: {
              show: true,
              position: 'right',
              fontSize: 10,
              color: '#606266',
              formatter: '{c}'
            }
          },
          {
            name: '总订单',
            type: 'bar',
            data: [],
            barHeight: 16,
            itemStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 1,
                y2: 0,
                colorStops: [{
                  offset: 0, color: '#409EFF'
                }, {
                  offset: 1, color: '#66b1ff'
                }]
              },
              borderRadius: [0, 4, 4, 0]
            },
            label: {
              show: true,
              position: 'right',
              fontSize: 10,
              color: '#606266',
              formatter: '{c}'
            }
          }
        ]
      }
    }
  },
  computed: {
    // 统计数据表格数据
    statisticsData() {
      return [
        {
          period: '今日',
          totalOrders: this.formatNumber(this.orderData.today.totalOrders),
          activatedOrders: this.formatNumber(this.orderData.today.activatedOrders),
          settledOrders: this.formatNumber(this.orderData.today.settledOrders),
          agentCount: this.formatNumber(this.orderData.today.agentCount)
        },
        {
          period: '昨日',
          totalOrders: this.formatNumber(this.orderData.yesterday.totalOrders),
          activatedOrders: this.formatNumber(this.orderData.yesterday.activatedOrders),
          settledOrders: this.formatNumber(this.orderData.yesterday.settledOrders),
          agentCount: this.formatNumber(this.orderData.yesterday.agentCount)
        },
        {
          period: '本月',
          totalOrders: this.formatNumber(this.orderData.thisMonth.totalOrders),
          activatedOrders: this.formatNumber(this.orderData.thisMonth.activatedOrders),
          settledOrders: this.formatNumber(this.orderData.thisMonth.settledOrders),
          agentCount: this.formatNumber(this.orderData.thisMonth.agentCount)
        },
        {
          period: '上月',
          totalOrders: this.formatNumber(this.orderData.lastMonth.totalOrders),
          activatedOrders: this.formatNumber(this.orderData.lastMonth.activatedOrders),
          settledOrders: this.formatNumber(this.orderData.lastMonth.settledOrders),
          agentCount: this.formatNumber(this.orderData.lastMonth.agentCount)
        },
        {
          period: '本年',
          totalOrders: this.formatNumber(this.orderData.thisYear.totalOrders),
          activatedOrders: this.formatNumber(this.orderData.thisYear.activatedOrders),
          settledOrders: this.formatNumber(this.orderData.thisYear.settledOrders),
          agentCount: this.formatNumber(this.orderData.thisYear.agentCount)
        }
      ]
    }
  },
  mounted() {
    this.getProductData()
    this.getOrderData()
    this.getAgentRankingData()
    this.getOrderTrendData()
    this.getNotificationData()
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

    // 获取系统通知数据
    async getNotificationData() {
      this.notificationLoading = true
      try {
        const response = await listNotice({
          pageNum: 1,
          pageSize: 6 // 只获取最新的6条通知
        })

        if (response.code === 200) {
          // 转换数据格式，添加类型判断
          this.notifications = response.rows.map(item => ({
            text: item.noticeTitle,
            date: item.createTime,
            type: this.getNotificationType(item.noticeType),
            id: item.noticeId
          }))
        } else {
          console.warn('获取系统通知失败:', response.msg || '未知错误')
        }
      } catch (error) {
        console.error('获取系统通知数据失败:', error)
        // 不显示错误提示，避免影响页面主要功能
      } finally {
        this.notificationLoading = false
      }
    },

    // 更新代理排名图表
    updateAgentRankingChart() {
      const { agentRankingList } = this.agentRankingData

      if (!agentRankingList || agentRankingList.length === 0) {
        return
      }

      // 取前10名并倒序显示（从下往上排列）
      const topAgents = agentRankingList.slice(0, 10).reverse()

      // 提取代理名称
      const agentNames = topAgents.map(item => item.downstreamName)

      // 提取激活订单数据
      const activatedOrders = topAgents.map(item => item.activatedOrders)

      // 提取总订单数据
      const totalOrders = topAgents.map(item => item.totalOrders)

      // 更新图表配置
      this.agentRankingOptions = {
        ...this.agentRankingOptions,
        yAxis: {
          ...this.agentRankingOptions.yAxis,
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

    // 获取变化文本
    getChangeText(current, previous) {
      if (previous === 0) {
        return current > 0 ? '新增' : '-'
      }
      const change = current - previous
      if (change > 0) {
        return `+${this.formatNumber(change)}`
      } else if (change < 0) {
        return `${this.formatNumber(change)}`
      } else {
        return '持平'
      }
    },

    // 获取趋势样式类
    getTrendClass(current, previous) {
      if (previous === 0) return 'trend-new'
      const change = current - previous
      if (change > 0) return 'trend-up'
      if (change < 0) return 'trend-down'
      return 'trend-equal'
    },

    // 获取趋势图标
    getTrendIcon(current, previous) {
      if (previous === 0) return 'el-icon-plus'
      const change = current - previous
      if (change > 0) return 'el-icon-top'
      if (change < 0) return 'el-icon-bottom'
      return 'el-icon-minus'
    },

    // 获取趋势百分比
    getTrendPercent(current, previous) {
      if (previous === 0) return current > 0 ? '+100%' : '0%'
      const percent = ((current - previous) / previous * 100).toFixed(1)
      return `${percent >= 0 ? '+' : ''}${percent}%`
    },

    // 获取趋势颜色
    getTrendColor(current, previous) {
      if (previous === 0) return '#409EFF'
      const change = current - previous
      if (change > 0) return '#67C23A'
      if (change < 0) return '#F56C6C'
      return '#909399'
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
    },

    // 获取通知图标
    getNotificationIcon(type) {
      const iconMap = {
        success: 'el-icon-check',
        warning: 'el-icon-warning',
        danger: 'el-icon-delete',
        info: 'el-icon-info'
      }
      return iconMap[type] || 'el-icon-bell'
    },

    // 获取通知颜色
    getNotificationColor(type) {
      const colorMap = {
        success: '#67C23A',
        warning: '#E6A23C',
        danger: '#F56C6C',
        info: '#409EFF'
      }
      return colorMap[type] || '#909399'
    },

    // 获取通知标签类型
    getNotificationTagType(type) {
      const tagMap = {
        success: 'success',
        warning: 'warning',
        danger: 'danger',
        info: 'primary'
      }
      return tagMap[type] || ''
    },

    // 获取通知类型文本
    getNotificationTypeText(type) {
      const textMap = {
        success: '成功',
        warning: '警告',
        danger: '错误',
        info: '信息'
      }
      return textMap[type] || '通知'
    },

    // 格式化相对时间
    formatRelativeTime(dateTime) {
      const now = new Date()
      const date = new Date(dateTime)
      const diffInSeconds = Math.floor((now - date) / 1000)

      if (diffInSeconds < 60) {
        return '刚刚'
      } else if (diffInSeconds < 3600) {
        const minutes = Math.floor(diffInSeconds / 60)
        return `${minutes}分钟前`
      } else if (diffInSeconds < 86400) {
        const hours = Math.floor(diffInSeconds / 3600)
        return `${hours}小时前`
      } else if (diffInSeconds < 2592000) { // 30天
        const days = Math.floor(diffInSeconds / 86400)
        return `${days}天前`
      } else {
        // 超过30天显示具体日期
        const month = (date.getMonth() + 1).toString().padStart(2, '0')
        const day = date.getDate().toString().padStart(2, '0')
        return `${month}-${day}`
      }
    },

    // 根据通知类型转换为样式类型
    getNotificationType(noticeType) {
      // 根据若依系统的通知类型转换
      switch(noticeType) {
        case '1': // 通知
          return 'info'
        case '2': // 公告
          return 'success'
        default:
          return 'info'
      }
    },

    // 处理通知点击事件
    handleNotificationClick() {
      // 直接跳转到通知管理页面
      this.$router.push('/system/notice')
    }
  }
}
</script>

<style lang="scss" scoped>
// 统计卡片样式
.box-card {
  border: 1px solid #EBEEF5;
  transition: all 0.3s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.04);
  height: 100px; // 固定卡片高度

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  }

  :deep(.el-card__body) {
    padding: 16px; // 减少内边距
  }

  .card-content {
    display: flex;
    align-items: center;
    height: 68px; // 固定内容高度
  }

  .card-left {
    margin-right: 12px;

    .card-icon {
      font-size: 32px;
      width: 48px;
      height: 48px;
      line-height: 48px;
      text-align: center;
      border-radius: 50%;
      color: #fff;
    }
  }

  .card-right {
    flex: 1;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 6px;

      span {
        font-size: 13px;
        color: #606266;
        font-weight: 500;
      }
    }

    .card-number {
      font-size: 24px;
      font-weight: bold;
      color: #303133;
      margin-bottom: 2px;
      line-height: 1;
    }

    .card-desc {
      font-size: 11px;
      color: #909399;
    }
  }

  // 不同卡片的主题色
  &.order-card .card-icon {
    background: linear-gradient(135deg, #409EFF, #66b1ff);
  }

  &.active-card .card-icon {
    background: linear-gradient(135deg, #67C23A, #85ce61);
  }

  &.commission-card .card-icon {
    background: linear-gradient(135deg, #E6A23C, #ebb563);
  }

  &.agent-card .card-icon {
    background: linear-gradient(135deg, #F56C6C, #f78989);
  }
}

// 趋势指示器样式
.trend-indicator {
  font-size: 10px;
  font-weight: bold;
  padding: 1px 4px;
  border-radius: 8px;

  i {
    margin-right: 1px;
    font-size: 9px;
  }

  &.trend-up {
    color: #67C23A;
    background: rgba(103, 194, 58, 0.1);
  }

  &.trend-down {
    color: #F56C6C;
    background: rgba(245, 108, 108, 0.1);
  }

  &.trend-equal {
    color: #909399;
    background: rgba(144, 147, 153, 0.1);
  }

  &.trend-new {
    color: #409EFF;
    background: rgba(64, 158, 255, 0.1);
  }
}

// 通知徽章样式
.notification-badge {
  margin-left: 8px;

  :deep(.el-badge__content) {
    background-color: #F56C6C;
    font-size: 10px;
    height: 16px;
    line-height: 16px;
    min-width: 16px;
    padding: 0 4px;
  }
}

// 通知列表滚动条样式
.notification-list::-webkit-scrollbar {
  width: 4px;
}

.notification-list::-webkit-scrollbar-track {
  background: #F5F7FA;
  border-radius: 2px;
}

.notification-list::-webkit-scrollbar-thumb {
  background: #C0C4CC;
  border-radius: 2px;

  &:hover {
    background: #A4A9AE;
  }
}

// 等高卡片行样式
.equal-height-row {
  .el-col {
    display: flex;
    flex-direction: column;
  }
}

// 等高卡片样式
.equal-height-card {
  height: 320px; // 减少高度：卡片头部50px + 内容区270px
  display: flex;
  flex-direction: column;

  :deep(.el-card__body) {
    flex: 1;
    padding: 16px;
    display: flex;
    flex-direction: column;
    height: 270px; // 内容区域固定高度
  }

  // 图表容器
  .chart-container {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    height: 240px;
  }

  // 表格容器
  .table-container {
    flex: 1;
    display: flex;
    flex-direction: column;
    height: 240px;
  }

  // 通知容器
  .notification-container {
    flex: 1;
    display: flex;
    flex-direction: column;
    height: 240px;
  }
}

// 通知列表高度调整
.notification-list {
  flex: 1;
  height: 100%;
  max-height: 240px;
  overflow-y: auto;
  padding: 4px;

  .notification-item {
    display: flex;
    align-items: flex-start;
    padding: 12px 8px;
    margin-bottom: 8px;
    border-radius: 6px;
    background: #FAFAFA;
    border: 1px solid transparent;
    transition: all 0.2s ease;
    cursor: pointer;

    &:last-child {
      margin-bottom: 0;
    }

    &:hover {
      background: #F5F7FA;
      border-color: #E4E7ED;
      transform: translateX(2px);
    }

    .notification-icon {
      margin-right: 12px;
      margin-top: 2px;
      min-width: 20px;
      text-align: center;

      i {
        font-size: 16px;
        font-weight: bold;
      }
    }

    .notification-content {
      flex: 1;
      min-width: 0;

      .notification-header {
        display: flex;
        align-items: flex-start;
        justify-content: space-between;
        margin-bottom: 6px;
        gap: 8px;

        .notification-text {
          font-size: 13px;
          color: #303133;
          line-height: 1.4;
          font-weight: 500;
          flex: 1;
          word-break: break-all;
        }

        .el-tag--mini {
          font-size: 10px;
          height: 18px;
          line-height: 16px;
          padding: 0 4px;
          border-radius: 9px;
          flex-shrink: 0;
        }
      }

      .notification-time {
        font-size: 11px;
        color: #909399;
        display: flex;
        align-items: center;

        &::before {
          content: '';
          display: inline-block;
          width: 4px;
          height: 4px;
          background: #C0C4CC;
          border-radius: 50%;
          margin-right: 6px;
        }
      }
    }
  }

  // 空状态样式
  .no-notifications {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100%;
    color: #909399;
    text-align: center;
  }
}

// 统计表格样式
.statistics-table {
  .today-data {
    font-weight: bold;
    color: #303133;
    font-size: 14px;
  }

  .table-cell-content {
    display: flex;
    align-items: center;
    justify-content: flex-end;
    gap: 6px;
  }

  .trend-mini {
    font-size: 10px;
    line-height: 1;

    i {
      font-size: 12px;
    }
  }

  // 表格行高调整
  :deep(.el-table__row) {
    td {
      padding: 12px 0;
    }
  }

  // 表头样式
  :deep(.el-table__header-wrapper) {
    th {
      font-weight: 600;
      color: #606266;
      background: #F8F9FA;
    }
  }
}

// 表格行间距调整
:deep(.el-table td) {
  padding: 8px 0;

  .el-tag--mini {
    font-size: 11px;
    padding: 0 6px;
    height: 20px;
    line-height: 18px;
  }
}

// 卡片头部按钮样式
:deep(.el-card__header) {
  display: flex;
  justify-content: space-between;
  align-items: center;

  .el-button--text {
    color: #409EFF;
    font-size: 12px;
    padding: 0;
  }
}

// 整体容器优化
.app-container {
  padding: 16px;
  min-height: calc(100vh - 84px); // 减去头部导航高度
  overflow: hidden; // 防止页面滚动
}

// 行间距优化
.mb20 {
  margin-bottom: 16px;

  &:last-child {
    margin-bottom: 0;
  }
}

// 卡片内容填充优化
:deep(.el-card) {
  border-radius: 8px;
  border: 1px solid #E4E7ED;

  .el-card__header {
    background: #FAFBFC;
    border-bottom: 1px solid #E4E7ED;
    padding: 16px 20px;
    font-weight: 600;
    color: #303133;
  }

  .el-card__body {
    padding: 20px;
  }
}

// 统一按钮样式
:deep(.el-button--text) {
  color: #409EFF;
  font-size: 12px;
  padding: 0;

  &:hover {
    color: #66b1ff;
  }

  &.is-loading {
    color: #C0C4CC;
  }
}

// 响应式处理
@media (max-width: 1400px) {
  .app-container {
    padding: 16px;
  }

  .box-card {
    .card-number {
      font-size: 24px;
    }
  }
}

@media (max-width: 768px) {
  .app-container {
    padding: 12px;
  }

  .box-card {
    margin-bottom: 15px;

    .card-number {
      font-size: 20px;
    }

    .card-left .card-icon {
      font-size: 32px;
      width: 50px;
      height: 50px;
      line-height: 50px;
    }
  }
}
</style>
