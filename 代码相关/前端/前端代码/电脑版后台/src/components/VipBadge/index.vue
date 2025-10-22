<template>
  <div class="vip-badge" v-if="visible">
    <el-tooltip :content="tooltipContent" placement="bottom" effect="dark">
      <div class="vip-badge__content">
        <img v-if="iconUrl" :src="iconUrl" class="vip-badge__icon" alt="vip-icon">
        <svg-icon v-else :icon-class="localIcon" class="vip-badge__icon" />
        <span class="vip-badge__label">{{ displayLabel }}</span>
      </div>
    </el-tooltip>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'VipBadge',
  constants: {
    LEVEL_ICON_MAP: {
      6: 'vip-zungui',
      5: 'vip-zuanshi',
      4: 'vip-baijin',
      3: 'vip-huangjin',
      2: 'vip-baijin',
      1: 'vip-qingtong',
      0: 'vip-default'
    },
    NAME_ICON_MAP: {
      supreme: 'vip-zungui',
      尊贵: 'vip-zungui',
      尊贵级: 'vip-zungui',
      尊贵会员: 'vip-zungui',
      钻石: 'vip-zuanshi',
      钻石级: 'vip-zuanshi',
      钻石会员: 'vip-zuanshi',
      diamond: 'vip-zuanshi',
      白金: 'vip-baijin',
      白金级: 'vip-baijin',
      白金会员: 'vip-baijin',
      platinum: 'vip-baijin',
      银牌: 'vip-baijin',
      银牌会员: 'vip-baijin',
      黄金: 'vip-huangjin',
      黄金级: 'vip-huangjin',
      金牌: 'vip-huangjin',
      金牌会员: 'vip-huangjin',
      gold: 'vip-huangjin',
      青铜: 'vip-qingtong',
      青铜级: 'vip-qingtong',
      青铜会员: 'vip-qingtong',
      bronze: 'vip-qingtong',
      普通会员: 'vip-default',
      normal: 'vip-default'
    }
  },
  computed: {
    ...mapGetters(['vipInfo']),
    sanitizedVipInfo() {
      return this.vipInfo || {}
    },
    level() {
      const level = Number(this.sanitizedVipInfo.vipLevel)
      return Number.isNaN(level) ? 0 : level
    },
    iconUrl() {
      const icon = this.sanitizedVipInfo.vipLevelIcon
      if (!icon) {
        return ''
      }
      if (/^(https?:)?\/\//i.test(icon) || icon.startsWith('data:')) {
        return icon
      }
      if (icon.startsWith('/')) {
        return `${process.env.VUE_APP_BASE_API}${icon}`
      }
      return icon
    },
    localIcon() {
      if (this.iconUrl) {
        return 'vip-default'
      }
      const levelIcon = this.$options.constants.LEVEL_ICON_MAP[this.level]
      if (levelIcon) {
        return levelIcon
      }
      const levelName = (this.levelName || '').toLowerCase()
      const matchedByName = this.$options.constants.NAME_ICON_MAP[levelName]
      if (matchedByName) {
        return matchedByName
      }
      const originalName = this.sanitizedVipInfo.vipLevelName || ''
      return this.$options.constants.NAME_ICON_MAP[originalName] || 'vip-default'
    },
    levelName() {
      return this.sanitizedVipInfo.vipLevelName || `VIP${this.level}`
    },
    displayLabel() {
      return this.levelName
    },
    tooltipContent() {
      return `当前等级：${this.levelName}`
    },
    visible() {
      return true
    }
  }
}
</script>

<style scoped>
.vip-badge {
  display: inline-flex;
  align-items: center;
  height: 100%;
}

.vip-badge__content {
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.vip-badge__icon {
  width: 20px;
  height: 20px;
  object-fit: contain;
}

.vip-badge__label {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
}

.vip-badge :deep(.svg-icon) {
  width: 20px;
  height: 20px;
}
</style>
