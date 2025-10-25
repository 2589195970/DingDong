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
  银牌级: 3,
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

const ICON_ALIAS_MAP = {
  'vip-zungui': 'vip-5',
  'vip-zuanshi': 'vip-4',
  'vip-baijin': 'vip-3',
  'vip-huangjin': 'vip-2',
  'vip-silver': 'vip-3',
  'vip-qingtong': 'vip-1',
  'vip-default': 'vip-default'
}

const HTTP_PATTERN = /^https?:\/\//i
const DATA_URI_PATTERN = /^data:/i

function isRemoteAsset(path) {
  return HTTP_PATTERN.test(path) || DATA_URI_PATTERN.test(path) || path.startsWith('//')
}

function isAbsolutePath(path) {
  return path.startsWith('/') || path.startsWith('./') || path.startsWith('../')
}

function withBaseApi(path) {
  const base = process.env.VUE_APP_BASE_API || ''
  if (!base) {
    return path
  }
  if (base.endsWith('/') && path.startsWith('/')) {
    return `${base}${path.substring(1)}`
  }
  if (!base.endsWith('/') && !path.startsWith('/')) {
    return `${base}/${path}`
  }
  return `${base}${path}`
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
  const match = str.match(/-?\d+/)
  if (match) {
    const numeric = Number(match[0])
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
  for (const value of candidates) {
    const parsed = normalizeVipLevel(value)
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
  for (const value of nameCandidates) {
    const parsed = normalizeVipLevel(value)
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
  const level = getVipLevelNumber(info)
  if (level === null) {
    return `${prefix}0`
  }
  return `${prefix}${level}`
}

export function resolveVipIcon(info = {}) {
  if (!info || typeof info !== 'object') {
    return 'vip-default'
  }
  const rawIcon = info.vipLevelIcon || info.levelIcon
  if (typeof rawIcon === 'string' && rawIcon.trim()) {
    if (isRemoteAsset(rawIcon)) {
      return rawIcon
    }
    if (isAbsolutePath(rawIcon)) {
      return withBaseApi(rawIcon)
    }
    const normalized = rawIcon.trim()
    const plainName = normalized.replace(/\.svg$/i, '')
    if (!normalized.includes('/') && Object.prototype.hasOwnProperty.call(ICON_ALIAS_MAP, plainName)) {
      return ICON_ALIAS_MAP[plainName]
    }
    if (!normalized.includes('/') && plainName !== normalized) {
      return plainName
    }
    return normalized
  }

  const level = getVipLevelNumber(info)
  if (level !== null && Number.isFinite(level)) {
    const clamped = Math.max(LEVEL_ICON_RANGE.min, Math.min(level, LEVEL_ICON_RANGE.max))
    return `vip-${clamped}`
  }
  return 'vip-default'
}

export const VIP_NAME_LEVEL_MAP = NAME_LEVEL_MAP
