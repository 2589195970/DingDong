<template>
  <view class="vip-badge" v-if="visible">
    <image
      v-if="remoteIcon"
      :src="remoteIcon"
      class="vip-badge__icon"
      :style="iconStyle"
      mode="aspectFit"
    />
    <image
      v-else
      :src="localIcon"
      class="vip-badge__icon"
      :style="iconStyle"
      mode="aspectFit"
    />
    <text class="vip-badge__label" :style="labelStyle">{{ displayLabel }}</text>
  </view>
</template>

<script>
import config from '@/config'
import { isHttp, isEmpty } from '@/utils/validate'

const ICON_BASE_PATH = '/static/vip-icons/'
const LEVEL_ICON_MAP = {
  5: 'vip-zungui',
  4: 'vip-zuanshi',
  3: 'vip-baijin',
  2: 'vip-huangjin',
  1: 'vip-qingtong',
  0: 'vip-default'
}
const NAME_ICON_MAP = {
  supreme: 'vip-zungui',
  '尊贵': 'vip-zungui',
  '尊贵级': 'vip-zungui',
  'supreme vip': 'vip-zungui',
  '钻石': 'vip-zuanshi',
  '钻石级': 'vip-zuanshi',
  diamond: 'vip-zuanshi',
  'diamond vip': 'vip-zuanshi',
  '白金': 'vip-baijin',
  '白金级': 'vip-baijin',
  platinum: 'vip-baijin',
  '黄金': 'vip-huangjin',
  '黄金级': 'vip-huangjin',
  gold: 'vip-huangjin',
  '青铜': 'vip-qingtong',
  '青铜级': 'vip-qingtong',
  bronze: 'vip-qingtong'
}

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
      const level = Number(this.sanitizedInfo.vipLevel)
      return Number.isNaN(level) ? 0 : level
    },
    levelName() {
      const name = this.sanitizedInfo.vipLevelName
      return isEmpty(name) ? `VIP${this.level}` : name
    },
    remoteIcon() {
      const icon = this.sanitizedInfo.vipLevelIcon
      if (!icon) {
        return ''
      }
      if (icon.startsWith('data:')) {
        return icon
      }
      if (isHttp(icon)) {
        return icon
      }
      return `${config.baseUrl}${icon}`
    },
    localIcon() {
      const mapIcon = LEVEL_ICON_MAP[this.level]
      if (mapIcon) {
        return `${ICON_BASE_PATH}${mapIcon}.svg`
      }
      const normalized = (this.levelName || '').toLowerCase()
      if (NAME_ICON_MAP[normalized]) {
        return `${ICON_BASE_PATH}${NAME_ICON_MAP[normalized]}.svg`
      }
      if (NAME_ICON_MAP[this.levelName]) {
        return `${ICON_BASE_PATH}${NAME_ICON_MAP[this.levelName]}.svg`
      }
      return `${ICON_BASE_PATH}vip-default.svg`
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
        fontSize: `${fontSize}rpx`
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
}
</style>
