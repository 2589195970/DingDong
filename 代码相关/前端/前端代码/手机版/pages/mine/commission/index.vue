<template>
  <view class="vip-benefits-page">
    <app-navbar title="权益详情"></app-navbar>
    <view class="vip-benefits__container">
      <view class="vip-benefits__summary">
        <view class="vip-summary-card">
          <view class="vip-summary-card__icon">
            <image :src="currentIcon" mode="aspectFit" />
          </view>
          <view class="vip-summary-card__info">
            <text class="vip-summary-card__title">{{ vipLevelLabel }}</text>
            <text class="vip-summary-card__subtitle">{{ vipSubtitle }}</text>
          </view>
          <view v-if="currentVipCard" class="vip-summary-card__tag">
            <text class="vip-summary-card__tag-label">固定加成</text>
            <text class="vip-summary-card__tag-value">¥{{ formatAmount(currentVipCard.fixedCommission) }}</text>
          </view>
        </view>
        <!--<view class="vip-summary-card__meta">-->
        <!--  <view-->
        <!--    v-if="currentVipCard && currentVipCard.requiredOrders != null"-->
        <!--    class="vip-summary-card__meta-item">-->
        <!--    &lt;!&ndash;<text class="vip-summary-card__meta-label">升级所需订单</text>&ndash;&gt;-->
        <!--    <text class="vip-summary-card__meta-value">{{ currentVipCard.requiredOrders }}</text>-->
        <!--  </view>-->
        <!--  <view v-if="vipRemark" class="vip-summary-card__meta-item">-->
        <!--    <text class="vip-summary-card__meta-label">权益说明</text>-->
        <!--    <text class="vip-summary-card__meta-value vip-summary-card__meta-value&#45;&#45;wrap">{{ vipRemark }}</text>-->
        <!--  </view>-->
        <!--</view>-->
      </view>

      <!--<view class="vip-benefits__hint">-->
      <!--  <text>不同等级的权益由平台统一配置，实际权益以平台审核结果为准。</text>-->
      <!--</view>-->

      <view class="vip-benefits__cards">
        <view v-if="loading" class="vip-status-box">权益加载中...</view>
        <view v-else-if="errorMessage" class="vip-status-box vip-status-box--error">
          <text>{{ errorMessage }}</text>
          <view class="vip-status-box__action" @click="fetchVipCards(true)">重新加载</view>
        </view>
        <view v-else-if="!vipCards.length" class="vip-status-box">暂无 VIP 权益配置</view>
        <view v-else class="vip-card-grid">
          <view
            v-for="card in vipCards"
            :key="card.vipLevel"
            class="vip-card"
            :class="{ 'is-active': levelMatches(card, currentVipLevel) }">
            <view class="vip-card__icon">
              <image :src="resolveVipIcon(card)" mode="aspectFit" />
            </view>
            <view class="vip-card__header">
              <text class="vip-card__level">VIP{{ card.vipLevel }}</text>
              <text class="vip-card__name">{{ card.levelName || ('VIP' + card.vipLevel) }}</text>
            </view>
            <view class="vip-card__body">
              <text class="vip-card__amount">¥{{ formatAmount(card.fixedCommission) }}</text>
              <text class="vip-card__desc">固定加成</text>
              <text v-if="card.requiredOrders != null" class="vip-card__meta">升级需订单：{{ card.requiredOrders }}</text>
              <!--<text v-if="card.remark" class="vip-card__remark">{{ card.remark }}</text>-->
            </view>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { getVipCommissionCards } from '@/api/mine/vip'
import {
  resolveVipIcon as resolveVipIconUtil,
  resolveVipLevelLabel,
  formatVipAmount,
  hasVipRecord,
  deriveVipLevelFromInfo,
  normalizeVipLevel
} from '@/utils/vip'

export default {
  data() {
    return {
      loading: false,
      vipCards: [],
      errorMessage: ''
    }
  },
  onLoad() {
    this.fetchVipCards()
  },
  computed: {
    vipInfo() {
      return this.$store.state.user.vipInfo || {}
    },
    currentVipLevel() {
      return deriveVipLevelFromInfo(this.vipInfo)
    },
    currentIcon() {
      return resolveVipIconUtil(this.vipInfo)
    },
    vipLevelLabel() {
      if (!hasVipRecord(this.vipInfo) && this.currentVipLevel === null) {
        return '未开通 VIP'
      }
      return resolveVipLevelLabel(this.vipInfo)
    },
    currentVipCard() {
      if (!Array.isArray(this.vipCards) || !this.vipCards.length) {
        return null
      }
      const level = this.currentVipLevel
      if (level === null || level === undefined) {
        return null
      }
      return this.vipCards.find(card => this.levelMatches(card, level)) || null
    },
    vipRemark() {
      if (this.vipInfo && this.vipInfo.remark) {
        return this.vipInfo.remark
      }
      if (this.currentVipCard && this.currentVipCard.remark) {
        return this.currentVipCard.remark
      }
      return ''
    },
    vipSubtitle() {
      if (this.loading) {
        return '权益加载中...'
      }
      if (this.currentVipCard) {
        return `固定加成 ¥${formatVipAmount(this.currentVipCard.fixedCommission)}`
      }
      if (hasVipRecord(this.vipInfo)) {
        return '查看不同等级的权益详情'
      }
      return '成为 VIP 解锁权益'
    }
  },
  methods: {
    async fetchVipCards(force = false) {
      if (this.loading) {
        return
      }
      if (!force && this.vipCards.length) {
        return
      }
      this.loading = true
      this.errorMessage = ''
      try {
        const { data } = await getVipCommissionCards()
        this.vipCards = Array.isArray(data) ? data : []
      } catch (error) {
        console.error('获取 VIP 权益失败:', error)
        this.errorMessage = 'VIP 权益加载失败，请稍后重试'
      } finally {
        this.loading = false
      }
    },
    resolveVipIcon(item) {
      return resolveVipIconUtil(item)
    },
    formatAmount(value) {
      return formatVipAmount(value)
    },
    levelMatches(card = {}, level) {
      const targetLevel = normalizeVipLevel(level)
      if (targetLevel === null) {
        return false
      }
      const candidates = [
        card.vipLevel,
        card.level,
        card.vipLevelCode,
        card.levelCode,
        card.vipLevelId
      ]
      return candidates.some(
        candidate => normalizeVipLevel(candidate) === targetLevel
      )
    }
  }
}
</script>

<style lang="scss" scoped>
page {
  background-color: #f5f6f7;
}

.vip-benefits-page {
  min-height: 100vh;
  background: linear-gradient(180deg, #fefcf5 0%, #f5f7fb 100%);
}

.vip-benefits__container {
  padding: 20rpx 24rpx 48rpx;
}

.vip-benefits__summary {
  margin-bottom: 24rpx;
}

.vip-summary-card {
  display: flex;
  align-items: center;
  padding: 28rpx;
  border-radius: 24rpx;
  background: linear-gradient(135deg, #fff4d6 0%, #ffe7b0 100%);
  box-shadow: 0 18rpx 36rpx rgba(255, 214, 102, 0.38);
  position: relative;
  overflow: hidden;

  &__icon {
    width: 96rpx;
    height: 96rpx;
    border-radius: 50%;
    background: rgba(255, 214, 102, 0.22);
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 28rpx;

    image {
      width: 60rpx;
      height: 60rpx;
    }
  }

  &__info {
    flex: 1;
    display: flex;
    flex-direction: column;
    min-width: 0;
  }

  &__title {
    font-size: 34rpx;
    font-weight: 700;
    color: #8c4b05;
    margin-bottom: 8rpx;
    letter-spacing: 1rpx;
    text-shadow: 0 6rpx 18rpx rgba(255, 214, 102, 0.5);
  }

  &__subtitle {
    font-size: 26rpx;
    color: rgba(140, 75, 5, 0.75);
  }

  &__tag {
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    min-width: 180rpx;
  }

  &__tag-label {
    font-size: 24rpx;
    color: rgba(140, 75, 5, 0.75);
  }

  &__tag-value {
    font-size: 40rpx;
    font-weight: 700;
    color: #d46b08;
    margin-top: 6rpx;
  }

  &__meta {
    margin-top: 20rpx;
    display: flex;
    flex-direction: column;
    gap: 14rpx;
  }

  &__meta-item {
    display: flex;
    flex-direction: column;
  }

  &__meta-label {
    font-size: 24rpx;
    color: rgba(47, 60, 80, 0.6);
  }

  &__meta-value {
    margin-top: 6rpx;
    font-size: 28rpx;
    color: #2f3c50;
    font-weight: 500;

    &--wrap {
      white-space: normal;
      line-height: 1.4;
    }
  }
}

.vip-benefits__hint {
  padding: 18rpx 24rpx;
  background-color: #fff8e1;
  border-radius: 18rpx;
  color: #a37a1a;
  font-size: 24rpx;
  line-height: 1.4;
  margin-bottom: 28rpx;
}

.vip-benefits__cards {
  min-height: 300rpx;
}

.vip-status-box {
  padding: 60rpx 0;
  text-align: center;
  font-size: 26rpx;
  color: #909399;

  &--error {
    color: #d46b08;
  }

  &__action {
    margin-top: 20rpx;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    padding: 12rpx 32rpx;
    border-radius: 999rpx;
    background: linear-gradient(135deg, #ffd666 0%, #ffc53d 100%);
    color: #8c4b05;
    font-size: 26rpx;
  }
}

.vip-card-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240rpx, 1fr));
  gap: 20rpx;
}

.vip-card {
  border-radius: 22rpx;
  padding: 28rpx;
  background: linear-gradient(135deg, #ffffff 0%, #f7fbff 100%);
  border: 1rpx solid rgba(236, 245, 255, 0.9);
  box-shadow: 0 18rpx 32rpx rgba(31, 45, 61, 0.12);
  position: relative;
  overflow: hidden;
  transition: transform 0.2s ease, box-shadow 0.2s ease;

  &::after {
    content: '';
    position: absolute;
    inset: 0;
    pointer-events: none;
    background-image: radial-gradient(circle at 20% 20%, rgba(255, 255, 255, 0.9) 0, rgba(255, 255, 255, 0) 55%),
      radial-gradient(circle at 80% 15%, rgba(255, 255, 255, 0.6) 0, rgba(255, 255, 255, 0) 60%);
    opacity: 0.35;
  }

  &__icon {
    width: 80rpx;
    height: 80rpx;
    border-radius: 24rpx;
    background: rgba(255, 214, 102, 0.25);
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 18rpx;

    image {
      width: 52rpx;
      height: 52rpx;
    }
  }

  &__header {
    position: relative;
    margin-bottom: 24rpx;
  }

  &__level {
    font-size: 36rpx;
    font-weight: 800;
    color: #d46b08;
    letter-spacing: 2rpx;
    text-transform: uppercase;
    text-shadow: 0 4rpx 12rpx rgba(212, 107, 8, 0.35);
  }

  &__name {
    display: block;
    font-size: 26rpx;
    margin-top: 6rpx;
    color: rgba(47, 60, 80, 0.8);
    font-weight: 600;
    letter-spacing: 1rpx;
  }

  &__body {
    text-align: left;
  }

  &__amount {
    font-size: 40rpx;
    font-weight: 700;
    color: #d46b08;
    line-height: 1.1;
  }

  &__desc {
    font-size: 26rpx;
    color: rgba(47, 60, 80, 0.65);
    margin-top: 10rpx;
    display: block;
  }

  &__meta {
    display: block;
    margin-top: 12rpx;
    font-size: 24rpx;
    color: rgba(47, 60, 80, 0.65);
  }

  &__remark {
    margin-top: 8rpx;
    font-size: 24rpx;
    color: rgba(47, 60, 80, 0.55);
    line-height: 1.4;
  }

  &.is-active {
    border-color: rgba(250, 173, 20, 0.6);
    box-shadow: 0 24rpx 40rpx rgba(250, 173, 20, 0.28);
    transform: translateY(-6rpx);
  }
}
</style>
