<template>
  <div 
    ref="chartContainer" 
    :class="className" 
    :style="{ height: height, width: width }" 
  />
</template>

<script>
import * as echarts from 'echarts'
import resizeMixin from './mixins/resize'

export default {
  name: 'ECharts',
  mixins: [resizeMixin],
  props: {
    // 图表配置选项
    options: {
      type: Object,
      required: true,
      default: () => ({})
    },
    // 图表主题
    theme: {
      type: String,
      default: 'macarons'
    },
    // 容器类名
    className: {
      type: String,
      default: 'chart'
    },
    // 容器宽度
    width: {
      type: String,
      default: '100%'
    },
    // 容器高度
    height: {
      type: String,
      default: '300px'
    },
    // 是否启用响应式调整
    autoResize: {
      type: Boolean,
      default: true
    },
    // 是否显示加载动画
    loading: {
      type: Boolean,
      default: false
    },
    // 加载动画文本
    loadingText: {
      type: String,
      default: '加载中...'
    }
  },
  data() {
    return {
      chart: null
    }
  },
  watch: {
    options: {
      handler(newOptions) {
        if (this.chart) {
          this.chart.setOption(newOptions, true)
        }
      },
      deep: true
    },
    loading(newVal) {
      if (this.chart) {
        if (newVal) {
          this.chart.showLoading({
            text: this.loadingText,
            color: '#409EFF',
            textColor: '#000',
            maskColor: 'rgba(255, 255, 255, 0.8)',
            zlevel: 0
          })
        } else {
          this.chart.hideLoading()
        }
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy() {
    this.destroyChart()
  },
  methods: {
    // 初始化图表
    initChart() {
      if (!this.$refs.chartContainer) return
      
      this.chart = echarts.init(this.$refs.chartContainer, this.theme)
      
      // 设置配置
      this.chart.setOption(this.options, true)
      
      // 绑定事件
      this.bindEvents()
      
      // 显示加载状态
      if (this.loading) {
        this.chart.showLoading({
          text: this.loadingText,
          color: '#409EFF',
          textColor: '#000',
          maskColor: 'rgba(255, 255, 255, 0.8)',
          zlevel: 0
        })
      }
    },
    
    // 销毁图表
    destroyChart() {
      if (this.chart) {
        this.chart.dispose()
        this.chart = null
      }
    },
    
    // 绑定事件
    bindEvents() {
      if (!this.chart) return
      
      // 点击事件
      this.chart.on('click', (params) => {
        this.$emit('click', params)
      })
      
      // 双击事件
      this.chart.on('dblclick', (params) => {
        this.$emit('dblclick', params)
      })
      
      // 鼠标悬停事件
      this.chart.on('mouseover', (params) => {
        this.$emit('mouseover', params)
      })
      
      // 鼠标移出事件
      this.chart.on('mouseout', (params) => {
        this.$emit('mouseout', params)
      })
      
      // 图例切换事件
      this.chart.on('legendselectchanged', (params) => {
        this.$emit('legendselectchanged', params)
      })
      
      // 数据区域缩放事件
      this.chart.on('datazoom', (params) => {
        this.$emit('datazoom', params)
      })
      
      // 地图区域选择事件
      this.chart.on('geoselectchanged', (params) => {
        this.$emit('geoselectchanged', params)
      })
      
      // 时间轴播放事件
      this.chart.on('timelinechanged', (params) => {
        this.$emit('timelinechanged', params)
      })
    },
    
    // 手动调整图表大小
    resize() {
      if (this.chart) {
        this.chart.resize()
      }
    },
    
    // 显示加载动画
    showLoading(options = {}) {
      if (this.chart) {
        this.chart.showLoading({
          text: this.loadingText,
          color: '#409EFF',
          textColor: '#000',
          maskColor: 'rgba(255, 255, 255, 0.8)',
          zlevel: 0,
          ...options
        })
      }
    },
    
    // 隐藏加载动画
    hideLoading() {
      if (this.chart) {
        this.chart.hideLoading()
      }
    },
    
    // 获取图表实例
    getChart() {
      return this.chart
    },
    
    // 获取图表配置
    getOption() {
      return this.chart ? this.chart.getOption() : null
    },
    
    // 设置图表配置
    setOption(option, notMerge = false, lazyUpdate = false) {
      if (this.chart) {
        this.chart.setOption(option, notMerge, lazyUpdate)
      }
    },
    
    // 清空图表
    clear() {
      if (this.chart) {
        this.chart.clear()
      }
    }
  }
}
</script>

<style scoped>
.chart {
  position: relative;
}
</style>
