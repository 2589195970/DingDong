<template>
  <view class="page-container">
    <app-navbar title="可视化数据"></app-navbar>

    <view class="container">
      <!-- 代理商排行榜卡片 -->
      <view class="info-card">
        <view class="chart-header">
          <text class="chart-title">代理商排行榜</text>
          <view class="ranking-tabs">
            <text class="tab-item"
                  v-for="(tab, index) in rankingTabs"
                  :key="index"
                  :class="{ 'active': activeTab === index }"
                  @click="switchTab(index)">
              {{ tab.label }}
            </text>
          </view>
        </view>

        <!-- 排行榜类型选择 -->
        <view class="ranking-types">
          <text class="type-item"
                v-for="(type, key) in rankingTypes"
                :key="key"
                :class="{ 'active': currentRankingType == key }"
                @click="switchRankingType(key)">
            {{ type }}
          </text>
        </view>

        <!-- 排行榜列表 -->
        <view class="ranking-list" v-if="!rankingLoading">
          <view class="ranking-item" v-for="(item, index) in rankingData" :key="index">
            <view class="ranking-left">
              <view class="ranking-number" :class="`rank-${item.ranking <= 3 ? item.ranking : 'other'}`">
                {{ item.ranking }}
              </view>
              <view class="agent-info">
                <text class="agent-name">{{ item.agentName }}</text>
                <text class="agent-level">等级{{ item.level }}</text>
              </view>
            </view>
            <view class="ranking-right">
              <text class="ranking-value">{{ formatStatValue(item) }}</text>
            </view>
          </view>
          <view class="no-data" v-if="rankingData.length === 0">
            暂无排行榜数据
          </view>
        </view>

        <!-- 加载状态 -->
        <view class="loading-container" v-if="rankingLoading">
          <text class="loading-text">正在加载排行榜数据...</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { getDailyRanking, getMonthlyRanking, formatStatValue } from '@/api/agent/ranking';

export default {
  name: 'VisualData',
  data() {
    return {
      // 排行榜相关数据
      rankingTabs: [
        { label: '日榜', value: 'daily' },
        { label: '月榜', value: 'monthly' }
      ],
      activeTab: 0,
      rankingTypes: {
        1: '佣金排行',
        // 2: '订单量排行',
        // 3: '激活量排行',
        // 4: '团队发展排行'
      },
      currentRankingType: 1,
      rankingData: [],
      rankingLoading: false
    };
  },
  mounted() {
    this.loadRankingData();
  },
  methods: {

    // 切换排行榜标签
    switchTab(index) {
      this.activeTab = index;
      this.loadRankingData();
    },

    // 切换排行榜类型
    switchRankingType(type) {
      this.currentRankingType = parseInt(type);
      this.loadRankingData();
    },

    // 加载排行榜数据
    async loadRankingData() {
      this.rankingLoading = true;
      try {
        const params = {
          rankingType: this.currentRankingType,
          topCount: 10
        };

        let response;
        if (this.activeTab === 0) {
          // 日榜
          response = await getDailyRanking(params);
        } else {
          // 月榜
          response = await getMonthlyRanking(params);
        }

        if (response.code === 200) {
          this.rankingData = response.data || [];
        } else {
          uni.showToast({
            title: response.msg || '获取排行榜数据失败',
            icon: 'none'
          });
        }
      } catch (error) {
        console.error('获取排行榜数据失败:', error);
        uni.showToast({
          title: '网络错误，请稍后重试',
          icon: 'none'
        });
      } finally {
        this.rankingLoading = false;
      }
    },

    // 格式化统计数值显示
    formatStatValue(item) {
      return formatStatValue(item);
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

/* 排行榜卡片样式 */
.info-card {
  background-color: #fff;
  border-radius: 8rpx;
  overflow: hidden;
  margin-bottom: 20rpx;
}

/* 排行榜头部样式 */
.chart-header {
  padding: 30rpx 30rpx 20rpx 30rpx;
  border-bottom: 1rpx solid #eaeef1;
  display: flex;
  align-items: center;
  justify-content: space-between;

  .chart-title {
    font-size: 32rpx;
    color: #333;
    font-weight: 500;
  }
}

/* 排行榜相关样式 */
.ranking-tabs {
  display: flex;
  margin-left: auto;

  .tab-item {
    padding: 8rpx 24rpx;
    font-size: 28rpx;
    color: #909399;
    border-radius: 16rpx;
    margin-left: 16rpx;

    &.active {
      color: #f09b7f;
      background-color: rgba(240, 155, 127, 0.1);
    }
  }
}

.ranking-types {
  padding: 20rpx 30rpx;
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
  border-bottom: 1rpx solid #eaeef1;

  .type-item {
    padding: 12rpx 24rpx;
    font-size: 26rpx;
    color: #909399;
    border: 1rpx solid #eaeef1;
    border-radius: 20rpx;

    &.active {
      color: #f09b7f;
      border-color: #f09b7f;
      background-color: rgba(240, 155, 127, 0.1);
    }
  }
}

.ranking-list {
  .ranking-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 24rpx 30rpx;
    border-bottom: 1rpx solid #f5f6f7;

    &:last-child {
      border-bottom: none;
    }

    .ranking-left {
      display: flex;
      align-items: center;

      .ranking-number {
        width: 48rpx;
        height: 48rpx;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 24rpx;
        font-weight: 600;
        margin-right: 20rpx;

        &.rank-1 {
          background-color: #FFD700;
          color: #fff;
        }

        &.rank-2 {
          background-color: #C0C0C0;
          color: #fff;
        }

        &.rank-3 {
          background-color: #CD7F32;
          color: #fff;
        }

        &.rank-other {
          background-color: #f5f6f7;
          color: #909399;
        }
      }

      .agent-info {
        .agent-name {
          display: block;
          font-size: 30rpx;
          color: #333;
          margin-bottom: 4rpx;
        }

        .agent-level {
          font-size: 24rpx;
          color: #909399;
        }
      }
    }

    .ranking-right {
      .ranking-value {
        font-size: 32rpx;
        color: #f09b7f;
        font-weight: 600;
      }
    }
  }

  .no-data {
    text-align: center;
    padding: 60rpx 30rpx;
    color: #909399;
    font-size: 28rpx;
  }
}

.loading-container {
  text-align: center;
  padding: 60rpx 30rpx;

  .loading-text {
    color: #909399;
    font-size: 28rpx;
  }
}
</style>