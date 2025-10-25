<template>
  <view class="vip-badge" v-if="visible">
    <image
      :src="badgeIcon"
      class="vip-badge__icon"
      :style="iconStyle"
      mode="aspectFit"
    />
    <text class="vip-badge__label" :style="labelStyle">{{ displayLabel }}</text>
  </view>
</template>

<script>
import { isEmpty } from '@/utils/validate'
import {
  resolveVipIcon,
  resolveVipLevelLabel,
  deriveVipLevelFromInfo
} from '@/utils/vip'

export default {
  name: 'VipBadge',
  props: {
    vipInfo: {
      type: Object,
      default: () => ({})
    },
    size: {
      type: String,
      default: 'medium'
    }
  },
  computed: {
    sanitizedInfo() {
      return this.vipInfo || {}
    },
    level() {
      const parsed = deriveVipLevelFromInfo(this.sanitizedInfo)
      return parsed === null ? 0 : parsed
    },
    levelName() {
      const label = resolveVipLevelLabel(this.sanitizedInfo)
      return isEmpty(label) ? `VIP${this.level}` : label
    },
    badgeIcon() {
      return resolveVipIcon(this.sanitizedInfo)
    },
    displayLabel() {
      return this.levelName
    },
    visible() {
      return true
    },
    iconSize() {
      switch (this.size) {
        case 'small':
          return 32
        case 'large':
          return 56
        default:
          return 40
      }
    },
    iconStyle() {
      const size = `${this.iconSize}rpx`
      return {
        width: size,
        height: size
      }
    },
    labelStyle() {
      let fontSize = 26
      if (this.size === 'small') {
        fontSize = 24
      } else if (this.size === 'large') {
        fontSize = 32
      }
      return {
        fontSize: `${fontSize}rpx`,
        letterSpacing: '1rpx'
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.vip-badge {
  display: flex;
  align-items: center;
  padding-left: 12rpx;
}

.vip-badge__icon {
  margin-right: 12rpx;
}

.vip-badge__label {
  color: #ffffff;
  font-weight: 600;
  letter-spacing: 1rpx;
  text-shadow: 0 6rpx 16rpx rgba(0, 0, 0, 0.25);
}
</style>
