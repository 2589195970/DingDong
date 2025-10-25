<template>
  <el-popover
    placement="bottom-end"
    trigger="hover"
    popper-class="vip-commission-popover"
    @show="ensureLoaded">
    <div class="vip-card-container" v-loading="loading">
      <template v-if="!loading && cards.length">
        <div
          v-for="card in cards"
          :key="card.vipLevel"
          class="vip-card"
          :class="{ 'is-active': levelMatches(card, currentVipLevel) }">
          <div class="vip-card__icon">
            <img
              v-if="isImageSource(resolveIcon(card))"
              :src="resolveIcon(card)"
              alt="vip-icon" />
            <svg-icon v-else :icon-class="resolveIcon(card)" />
          </div>
          <div class="vip-card__header">
            <span class="vip-card__level">VIP{{ normalizeDisplayLevel(card.vipLevel) }}</span>
            <span class="vip-card__name">{{ card.levelName || `VIP${normalizeDisplayLevel(card.vipLevel)}` }}</span>
          </div>
          <div class="vip-card__body">
            <div class="vip-card__amount">¥{{ formatAmount(card.fixedCommission) }}</div>
            <div class="vip-card__desc">固定加成</div>
            <div v-if="hasRequiredOrders(card)" class="vip-card__meta">
              升级需订单：{{ card.requiredOrders }}
            </div>
          </div>
        </div>
      </template>
      <div v-else-if="!loading" class="vip-card-empty">
        <i class="el-icon-info" />
        <span>{{ errorMessage || '暂无 VIP 配置数据' }}</span>
      </div>
    </div>
    <div slot="reference" class="vip-card-trigger">
      <div class="vip-trigger-icon">
        <img
          v-if="isImageSource(currentIcon)"
          :src="currentIcon"
          alt="vip-icon" />
        <svg-icon v-else :icon-class="currentIcon" />
      </div>
      <div class="vip-trigger-text">
        <span class="vip-trigger-title">{{ currentVipLabel }}</span>
        <!--<span class="vip-trigger-subtitle">{{ currentVipAmount }}</span>-->
        <!--<span v-if="currentVipOrders" class="vip-trigger-meta">升级需订单：{{ currentVipOrders }}</span>-->
      </div>
    </div>
  </el-popover>
</template>

<script>
import { mapGetters } from 'vuex'
import { getVipCommissionCards } from '@/api/monitor/finance'
import {
  deriveVipLevelFromInfo,
  normalizeVipLevel,
  resolveVipIcon,
  resolveVipLevelLabel
} from '@/utils/vip'

export default {
  name: 'VipCommissionCards',
  data() {
    return {
      loading: false,
      loaded: false,
      cards: [],
      errorMessage: ''
    }
  },
  computed: {
    ...mapGetters(['vipInfo']),
    currentVipLevel() {
      return deriveVipLevelFromInfo(this.vipInfo)
    },
    currentIcon() {
      return resolveVipIcon(this.vipInfo)
    },
    currentVipLabel() {
      if (!this.vipInfo) {
        return 'VIP 权益'
      }
      const label = resolveVipLevelLabel(this.vipInfo)
      return label || 'VIP 权益'
    },
    currentVipAmount() {
      const card = this.findCardByLevel(this.currentVipLevel)
      if (!card) {
        return '固定加成 ¥0.00'
      }
      return `固定加成 ¥${this.formatAmount(card.fixedCommission)}`
    },
    currentVipOrders() {
      const card = this.findCardByLevel(this.currentVipLevel)
      if (!card || card.requiredOrders === null || card.requiredOrders === undefined || card.requiredOrders === '') {
        return ''
      }
      return card.requiredOrders
    }
  },
  mounted() {
    this.ensureLoaded()
  },
  methods: {
    async ensureLoaded() {
      if (this.loaded || this.loading) {
        return
      }
      this.loading = true
      this.errorMessage = ''
      try {
        const { data } = await getVipCommissionCards()
        this.cards = Array.isArray(data) ? data : []
        this.loaded = true
      } catch (error) {
        console.error('获取 VIP 配置失败', error)
        this.errorMessage = 'VIP 配置加载失败'
      } finally {
        this.loading = false
      }
    },
    formatAmount(value) {
      const amount = Number(value || 0)
      return amount.toFixed(2)
    },
    isImageSource(url) {
      if (typeof url !== 'string') {
        return false
      }
      const trimmed = url.trim()
      if (!trimmed) {
        return false
      }
      return /^https?:\/\//i.test(trimmed) || /^data:/i.test(trimmed) || trimmed.startsWith('/') || trimmed.startsWith('./') || trimmed.startsWith('../')
    },
    resolveIcon(item) {
      return resolveVipIcon(item)
    },
    normalizeDisplayLevel(value) {
      const parsed = normalizeVipLevel(value)
      return parsed === null ? value : parsed
    },
    findCardByLevel(level) {
      const targetLevel = normalizeVipLevel(level)
      if (targetLevel === null) {
        return null
      }
      return this.cards.find(card => normalizeVipLevel(card.vipLevel) === targetLevel) || null
    },
    levelMatches(card, level) {
      if (!card) {
        return false
      }
      const targetLevel = normalizeVipLevel(level)
      if (targetLevel === null) {
        return false
      }
      return normalizeVipLevel(card.vipLevel) === targetLevel
    },
    hasRequiredOrders(card) {
      if (!card) {
        return false
      }
      const { requiredOrders } = card
      return requiredOrders !== null && requiredOrders !== undefined && requiredOrders !== ''
    }
  }
}
</script>

<style lang="scss" scoped>
.vip-card-trigger {
  display: inline-flex;
  align-items: center;
  padding: 6px 14px;
  height: 38px;
  border-radius: 22px;
  border: 1px solid #edf2f9;
  background: #fff;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 13px;
  color: #1f2d3d;

  .vip-trigger-icon {
    width: 18px;
    height: 18px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 8px;
    color: #d46b08;

    img,
    :deep(.svg-icon) {
      width: 18px;
      height: 18px;
    }
  }

  .vip-trigger-text {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    line-height: 1.2;
  }

  .vip-trigger-title {
    font-weight: 700;
    font-size: 14px;
    letter-spacing: 0.5px;
    color: #2f3c50;
  }

  .vip-trigger-subtitle {
    font-size: 11px;
    color: #909399;
    margin-top: 2px;
  }

  .vip-trigger-meta {
    font-size: 11px;
    color: #b37b16;
    margin-top: 2px;
    letter-spacing: 0.5px;
  }

  &:hover {
    border-color: #ffd666;
    box-shadow: 0 4px 12px rgba(255, 214, 102, 0.35);
  }
}

.vip-card-container {
  width: calc(160px * 2 + 14px);
  min-width: 320px;
  max-width: calc(160px * 2 + 14px);
  display: grid;
  grid-template-columns: repeat(2, 160px);
  grid-auto-rows: 1fr;
  gap: 14px;
  padding: 6px;
  justify-content: center;
}

.vip-card {
  width: 160px;
  border-radius: 14px;
  padding: 16px;
  background: linear-gradient(135deg, #ffffff 0%, #f7fbff 100%);
  border: 1px solid rgba(236, 245, 255, 0.9);
  box-shadow: 0 10px 24px rgba(31, 45, 61, 0.12);
  transition: all 0.2s ease;
  position: relative;
  overflow: hidden;

  &::after {
    content: '';
    position: absolute;
    inset: 0;
    pointer-events: none;
    background-image: radial-gradient(circle at 20% 20%, rgba(255, 255, 255, 0.9) 0, rgba(255, 255, 255, 0) 55%),
      radial-gradient(circle at 85% 25%, rgba(255, 255, 255, 0.6) 0, rgba(255, 255, 255, 0) 60%);
    opacity: 0.35;
  }

  &__icon {
    position: absolute;
    right: 12px;
    top: 12px;
    width: 40px;
    height: 40px;

    img,
    :deep(.svg-icon) {
      width: 100%;
      height: 100%;
    }
  }

  &__header {
    position: relative;
    margin-bottom: 18px;
  }

  &__level {
    font-size: 20px;
    font-weight: 700;
    color: #d46b08;
    letter-spacing: 1px;
    text-transform: uppercase;
    text-shadow: 0 4px 12px rgba(212, 107, 8, 0.25);
  }

  &__name {
    display: block;
    font-size: 13px;
    margin-top: 4px;
    color: rgba(47, 60, 80, 0.78);
    font-weight: 600;
    letter-spacing: 0.5px;
  }

  &__body {
    text-align: left;
    position: relative;
  }

  &__amount {
    font-size: 24px;
    font-weight: 700;
    color: #d46b08;
    line-height: 1.1;
  }

  &__desc {
    font-size: 12px;
    color: rgba(47, 60, 80, 0.65);
    margin-top: 6px;
  }

  &__meta {
    font-size: 12px;
    color: #b37b16;
    margin-top: 6px;
    font-weight: 500;
  }

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 18px 32px rgba(31, 45, 61, 0.18);
  }

  &.is-active {
    border-color: rgba(250, 173, 20, 0.6);
    box-shadow: 0 22px 40px rgba(250, 173, 20, 0.35);
  }
}

.vip-card-empty {
  width: 100%;
  padding: 20px 0;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #909399;
  font-size: 13px;

  i {
    margin-right: 6px;
  }
}
</style>
<style lang="scss">
.vip-commission-popover {
  padding: 14px 16px;
  border-radius: 18px;
}
</style>
