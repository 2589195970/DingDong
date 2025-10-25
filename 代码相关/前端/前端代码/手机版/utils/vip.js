import config from '@/config'
import { isHttp } from '@/utils/validate'

const ICON_BASE_PATH = '/static/vip-icons/'
const LEVEL_ICON_RANGE = { min: 0, max: 5 }

const NAME_LEVEL_MAP = {
  supreme: 5,
  'supreme vip': 5,
  尊贵: 5,
  尊贵级: 5,
  尊贵会员: 5,
  钻石: 4,
  钻石级: 4,
  钻石会员: 4,
  diamond: 4,
  'diamond vip': 4,
  白金: 3,
  白金级: 3,
  白金会员: 3,
  platinum: 3,
  银牌: 3,
  银牌会员: 3,
  silver: 3,
  黄金: 2,
  黄金级: 2,
  金牌: 2,
  金牌会员: 2,
  gold: 2,
  青铜: 1,
  青铜级: 1,
  青铜会员: 1,
  bronze: 1,
  普通会员: 0,
  normal: 0
}

export function normalizeVipLevel(value) {
  if (value === null || value === undefined) {
    return null
  }
  if (typeof value === 'number' && Number.isFinite(value)) {
    return value
  }
  const str = String(value).trim()
  if (!str) {
    return null
  }
  const matched = str.match(/-?\d+/)
  if (matched) {
    const numeric = Number(matched[0])
    return Number.isNaN(numeric) ? null : numeric
  }
  const lower = str.toLowerCase()
  if (Object.prototype.hasOwnProperty.call(NAME_LEVEL_MAP, lower)) {
    return NAME_LEVEL_MAP[lower]
  }
  if (Object.prototype.hasOwnProperty.call(NAME_LEVEL_MAP, str)) {
    return NAME_LEVEL_MAP[str]
  }
  return null
}

export function deriveVipLevelFromInfo(info = {}) {
  if (!info || typeof info !== 'object') {
    return null
  }
  const candidates = [
    info.vipLevel,
    info.level,
    info.vipLevelCode,
    info.levelCode,
    info.vipLevelId
  ]
  for (const candidate of candidates) {
    const parsed = normalizeVipLevel(candidate)
    if (parsed !== null) {
      return parsed
    }
  }
  const nameCandidates = [
    info.vipLevelName,
    info.levelName,
    info.vipLevelLabel,
    info.levelLabel
  ]
  for (const candidate of nameCandidates) {
    const parsed = normalizeVipLevel(candidate)
    if (parsed !== null) {
      return parsed
    }
  }
  return null
}

export function getVipLevelNumber(info = {}) {
  return deriveVipLevelFromInfo(info)
}

export function resolveVipLevelLabel(info = {}, prefix = 'VIP') {
  if (info.vipLevelName) {
    return info.vipLevelName
  }
  if (info.levelName) {
    return info.levelName
  }
  const levelNumber = getVipLevelNumber(info)
  if (levelNumber === null) {
    return `${prefix}0`
  }
  return `${prefix}${levelNumber}`
}

export function resolveVipIcon(info = {}) {
  const rawIcon = info.vipLevelIcon || info.levelIcon
  if (rawIcon) {
    if (typeof rawIcon === 'string' && rawIcon.startsWith('data:')) {
      return rawIcon
    }
    if (isHttp(rawIcon)) {
      return rawIcon
    }
    return `${config.baseUrl}${rawIcon}`
  }

  const levelNumber = getVipLevelNumber(info)
  if (levelNumber !== null && Number.isFinite(levelNumber)) {
    const clamped = Math.max(
      LEVEL_ICON_RANGE.min,
      Math.min(levelNumber, LEVEL_ICON_RANGE.max)
    )
    return `${ICON_BASE_PATH}vip-${clamped}.svg`
  }

  return `${ICON_BASE_PATH}vip-default.svg`
}

export function formatVipAmount(value) {
  const amount = Number(value || 0)
  return amount.toFixed(2)
}

export function hasVipRecord(info = {}) {
  return Boolean(info.hasVipRecord)
}

export { ICON_BASE_PATH }
