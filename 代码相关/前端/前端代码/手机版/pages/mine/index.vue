<template>
  <view class="mine-container" :style="{height: `${windowHeight}px`}">
    <!--顶部个人信息栏-->
    <view class="header-section">
      <view class="header-content">
        <view class="profile-main">
          <view class="profile-avatar">
            <view v-if="!avatar" class="cu-avatar xl round bg-white">
              <view class="iconfont icon-people text-gray icon"></view>
            </view>
            <image
              v-else
              @click="handleToAvatar"
              :src="avatar"
              class="cu-avatar xl round"
              mode="aspectFill"
            />
          </view>
          <view class="profile-details">
            <view v-if="!name" @click="handleToLogin" class="login-tip">
              点击登录
            </view>
            <view v-else class="user-info" @click="handleToInfo">
              <view class="user-info__name">
                <text class="user-info__name-text">{{ name }}</text>
              </view>
            </view>
          </view>
        </view>
        <view @click="handleToInfo" class="profile-action">
          <text class="profile-action__text">个人信息</text>
          <u-icon name="arrow-right" size="20" color="#4a5260"></u-icon>
        </view>
      </view>
    </view>
    <view v-if="vipInfo" class="vip-summary-strip" @click.stop="handleVipBenefits">
      <view class="vip-summary-strip__icon">
        <image :src="vipTriggerIcon" mode="aspectFit" />
      </view>
      <view class="vip-summary-strip__text">
        <text class="vip-summary-strip__title">{{ vipTriggerTitle }}</text>
        <text class="vip-summary-strip__subtitle">{{ vipTriggerSubtitle }}</text>
      </view>
      <view class="vip-summary-strip__action">
        <text>权益详情</text>
        <u-icon name="arrow-right" size="18" color="#c46b08"></u-icon>
      </view>
    </view>

    <view class="content-section">

      <view class="revenue-card">
        <view class="revenue-info">
          <view class="revenue-item" @click="product">
            <text class="revenue-label">累计收益</text>
            <text class="revenue-value">{{ productList.depositAmount * 0.01 }}元</text>
          </view>
          <view class="revenue-divider"></view>
          <view class="revenue-item" @click="product">
            <text class="revenue-label">可提现收益</text>
            <text class="revenue-value">{{ productList.balance * 0.01 }}元</text>
          </view>
        </view>
        <view class="withdraw-btn" @click="payouts">
          <text class="withdraw-text">立即提现</text>
        </view>
      </view>

    </view>

    <!-- 数据统计组件 -->
    <view class="content-section">
      <view class="stats-card">
        <view class="stats-header">
          <view class="stats-title">
            数据统计
          </view>
          <view class="stats-controls">
            <!-- 统计类型切换 -->
            <view class="type-switch-wrapper">
              <view
                class="switch-item"
                :class="{ 'active': statisticsType === 0 }"
                @click="switchStatisticsType(0)">
                个人
              </view>
              <view
                class="switch-item"
                :class="{ 'active': statisticsType === 1 }"
                @click="switchStatisticsType(1)">
                团队
              </view>
            </view>
            <view class="visual-data-link" @click="goToVisualData">
              可视化数据
              <u-icon name="arrow-right" color="#999" size="14"></u-icon>
            </view>
          </view>
        </view>

        <!-- 今日数据 -->
        <view class="stats-section">
          <view class="stats-data-row">
            <view class="stats-item">
              <view class="stats-value">{{ todayStats.invitations || 0 }}</view>
              <view class="stats-label">今日邀请</view>
            </view>
            <view class="stats-item">
              <view class="stats-value">{{ todayStats.orders || 0 }}</view>
              <view class="stats-label">今日订单</view>
            </view>
            <view class="stats-item">
              <view class="stats-value">{{ todayStats.activations || 0 }}</view>
              <view class="stats-label">今日激活</view>
            </view>
            <view class="stats-item">
              <view class="stats-value">{{ (todayStats.commission || 0) * 0.01 }}元</view>
              <view class="stats-label">今日佣金</view>
            </view>
          </view>
        </view>

        <!-- 本月数据 -->
        <view class="stats-section stats-section-month">
          <view class="stats-data-row">
            <view class="stats-item">
              <view class="stats-value">{{ monthStats.invitations || 0 }}</view>
              <view class="stats-label">本月邀请</view>
            </view>
            <view class="stats-item">
              <view class="stats-value">{{ monthStats.orders || 0 }}</view>
              <view class="stats-label">本月订单</view>
            </view>
            <view class="stats-item">
              <view class="stats-value">{{ monthStats.activations || 0 }}</view>
              <view class="stats-label">本月激活</view>
            </view>
            <view class="stats-item">
              <view class="stats-value">{{ (monthStats.commission || 0) * 0.01 }}元</view>
              <view class="stats-label">本月佣金</view>
            </view>
          </view>
        </view>
      </view>
    </view>
    <view class="content-section">

      <view class="mine-actions grid col-4 text-center">
        <view class="function-btn" @click="product">
          <image src="@/static/images/mine/商品管理.png" class="btn-icon"></image>
          <text class="btn-text">代理商品</text>
        </view>
        <view class="function-btn" @click="miansc">
          <image src="@/static/images/mine/商城.png" class="btn-icon-with-padding"></image>
          <text class="btn-text">我的商城</text>
        </view>
        <view class="function-btn" @click="haibao">
          <image src="@/static/images/mine/招募.png" class="btn-icon-with-padding"></image>
          <text class="btn-text">商城海报</text>
        </view>
        <view class="function-btn" @click="vipBenefits">
          <image src="@/static/images/mine/佣金设置.png" class="btn-icon"></image>
          <text class="btn-text">权益详情</text>
        </view>
        <view class="function-btn" @click="payouts">
          <image src="@/static/images/mine/佣金提现.png" class="btn-icon"></image>
          <text class="btn-text">佣金提现</text>
        </view>
        <view class="function-btn" @click="invite">
          <image src="@/static/images/mine/推广邀请.png" class="btn-icon-with-padding"></image>
          <text class="btn-text">推广邀请</text>
        </view>
        <view class="function-btn" @click="myAgents">
          <image src="@/static/images/mine/代理名单.png" class="btn-icon"></image>
          <text class="btn-text">我的代理</text>
        </view>
        <view class="function-btn" @click="handleToEditInfo">
          <image src="@/static/images/mine/编辑资料.png" class="btn-icon-with-padding"></image>
          <text class="btn-text">编辑资料</text>
        </view>
        <view class="function-btn" @click="commissionDetail">
          <image src="@/static/images/mine/佣金明细.png" class="btn-icon-with-padding"></image>
          <text class="btn-text">佣金明细</text>
        </view>
        <view class="function-btn" @click="customerService">
          <image src="@/static/images/mine/客服.png" class="btn-icon-with-padding"></image>
          <text class="btn-text">我的客服</text>
        </view>
        <view class="function-btn" @click="liveConnect">
          <image src="@/static/images/mine/直播对接.png" class="btn-icon-with-padding"></image>
          <text class="btn-text">直播对接</text>
        </view>
        <view class="function-btn" @click="systemNotice">
          <image src="@/static/images/mine/通知.png" class="btn-icon-with-padding"></image>
          <text class="btn-text">系统通知</text>
        </view>
      </view>

    </view>

    <!-- 为最后一个功能按钮区域添加底部间距 -->
    <view style="height: 60px;"></view>
    <u-popup v-model="vipPopupVisible" mode="bottom" border-radius="26">
      <view class="vip-popup">
        <view class="vip-popup__header">
          <text class="vip-popup__title">VIP 权益</text>
          <view class="vip-popup__close" @click="vipPopupVisible = false">
            <u-icon name="close" size="20" color="#999"></u-icon>
          </view>
        </view>
        <view class="vip-popup__body">
          <view v-if="vipCardsLoading" class="vip-popup__status">权益加载中...</view>
          <view v-else-if="vipCardsError" class="vip-popup__status vip-popup__status--error">
            <text>{{ vipCardsError }}</text>
            <view class="vip-popup__retry" @click="retryVipCards">重新加载</view>
          </view>
          <view v-else-if="!vipCards.length" class="vip-popup__status">暂无 VIP 权益配置</view>
          <scroll-view v-else class="vip-popup__scroll" scroll-y>
            <view class="vip-popup__cards">
              <view
                v-for="card in vipCards"
                :key="card.vipLevel"
                class="vip-popup__card"
                :class="{ 'is-active': Number(card.vipLevel) === currentVipLevel }">
                <view class="vip-popup__card-icon">
                  <image :src="resolveVipIcon(card)" mode="aspectFit" />
                </view>
                <view class="vip-popup__card-info">
                  <text class="vip-popup__card-level">VIP{{ card.vipLevel }}</text>
                  <text class="vip-popup__card-name">{{ card.levelName || ('VIP' + card.vipLevel) }}</text>
                  <text v-if="card.requiredOrders != null" class="vip-popup__card-orders">升级需订单：{{ card.requiredOrders }}</text>
                  <text v-if="card.remark" class="vip-popup__card-remark">{{ card.remark }}</text>
                </view>
                <view class="vip-popup__card-amount">
                  <text class="vip-popup__card-value">¥{{ formatAmount(card.fixedCommission) }}</text>
                  <text class="vip-popup__card-desc">固定加成</text>
                </view>
              </view>
            </view>
          </scroll-view>
        </view>
      </view>
    </u-popup>
    <u-modal :show="commissionopen" @confirm="confirm" ref="uModal" @cancel="cancel" :showCancelButton='true'
             confirmText="保存图片" :asyncClose="true">
      <image :src="sc.shopQrcodeMap" alt="" class="qrcode-image"/>
    </u-modal>
  </view>
</template>

<script>
import {
  selectRevenue,
  getAgentExtendUrlVO,
  selectDashboardStatistics
} from "@/api/order/order.js";
import { getVipCommissionCards } from '@/api/mine/vip'
import constant from '@/utils/constant'
import {
  resolveVipIcon as resolveVipIconUtil,
  resolveVipLevelLabel,
  getVipLevelNumber,
  formatVipAmount,
  hasVipRecord
} from '@/utils/vip'

export default {
  data() {
    return {
      commissionopen: false,
      name: this.$store.state.user.name,
      version: getApp().globalData.config.appInfo.version,
      productList: {
        depositAmount: 0,
        balance: 0,
      },
      sc: {},
      // 统计类型：0-个人统计，1-团队统计
      statisticsType: 0,
      // 今日统计数据
      todayStats: {
        invitations: 0,
        orders: 0,
        activations: 0,
        commission: 0
      },
      // 本月统计数据
      monthStats: {
        invitations: 0,
        orders: 0,
        activations: 0,
        commission: 0
      },
      vipPopupVisible: false,
      vipCards: [],
      vipCardsLoading: false,
      vipCardsLoaded: false,
      vipCardsError: ''
    }
  },
  async onLoad() {
    await this.refreshVipInfo(true)
    this.select(); //获取列表
    this.getStatsData(); //获取统计数据
    this.ensureVipCards()
  },
  onShow() {
    this.refreshVipInfo()
    this.ensureVipCards()
  },
  computed: {
    vipInfo() {
      return this.$store.state.user.vipInfo || null
    },
    sanitizedVipInfo() {
      return this.vipInfo || {}
    },
    currentVipLevel() {
      return getVipLevelNumber(this.sanitizedVipInfo)
    },
    vipTriggerIcon() {
      return resolveVipIconUtil(this.sanitizedVipInfo)
    },
    currentVipCard() {
      if (!Array.isArray(this.vipCards) || !this.vipCards.length) {
        return null
      }
      const level = this.currentVipLevel
      if (level === null || level === undefined) {
        return null
      }
      return this.vipCards.find(card => Number(card.vipLevel) === Number(level)) || null
    },
    vipTriggerTitle() {
      if (!this.sanitizedVipInfo || (this.currentVipLevel === null && !this.sanitizedVipInfo.vipLevelName)) {
        return 'VIP 权益'
      }
      return resolveVipLevelLabel(this.sanitizedVipInfo)
    },
    vipTriggerSubtitle() {
      if (this.vipCardsLoading) {
        return '权益加载中...'
      }
      if (this.currentVipCard && this.currentVipCard.fixedCommission !== undefined && this.currentVipCard.fixedCommission !== null) {
        return `固定加成 ¥${formatVipAmount(this.currentVipCard.fixedCommission)}`
      }
      if (hasVipRecord(this.sanitizedVipInfo)) {
        return '查看权益详情'
      }
      return '成为 VIP 解锁权益'
    },
    avatar() {
      return this.$store.state.user.avatar
    },
    windowHeight() {
      const systemInfo = uni.getSystemInfoSync();
      // 确保有足够的页面高度来显示所有内容
      return Math.max(systemInfo.windowHeight, 800);
    },
    agentAccount() {
      return this.$store.state.user.agentAccount || {}
    }
  },
  methods: {
    handleVipBenefits() {
      if (!this.vipCardsLoaded && !this.vipCardsLoading) {
        this.ensureVipCards()
      }
      this.vipPopupVisible = false
      this.vipBenefits()
    },
    async refreshVipInfo(force = false) {
      const hasName = Boolean(this.name)
      const storeVipInfo = this.$store.state.user.vipInfo || {}
      const hasVipRecord = !!storeVipInfo.hasVipRecord
      if (!force && hasName && hasVipRecord) {
        return
      }
      try {
        await this.$store.dispatch('GetInfo')
        this.name = this.$store.state.user.name
      } catch (error) {
        console.error('刷新用户信息失败:', error)
      }
    },
    async ensureVipCards(force = false) {
      if (this.vipCardsLoading) {
        return
      }
      if (this.vipCardsLoaded && !force) {
        return
      }
      this.vipCardsLoading = true
      this.vipCardsError = ''
      try {
        const { data } = await getVipCommissionCards()
        this.vipCards = Array.isArray(data) ? data : []
        this.vipCardsLoaded = true
      } catch (error) {
        console.error('获取 VIP 权益失败:', error)
        this.vipCardsError = 'VIP 权益加载失败，请稍后重试'
        this.vipCardsLoaded = false
      } finally {
        this.vipCardsLoading = false
      }
    },
    async retryVipCards() {
      await this.ensureVipCards(true)
    },
    resolveVipIcon(item) {
      return resolveVipIconUtil(item)
    },
    formatAmount(value) {
      return formatVipAmount(value)
    },
    miansc() {
      // this.$tab.navigateTo(`/pages/common/webview/index?title=我的商城&url=`+ this.sc.shopUrl)
      window.location.href = this.sc.shopUrl;
    },
    confirm() {
      // 图片地址（需允许跨域访问）
      const url = this.sc.shopQrcodeMap;
      // 创建隐藏的 <a> 标签
      const link = document.createElement('a');
      link.href = url;
      link.download = 'img.jpg'; // 设置下载文件名
      document.body.appendChild(link);

      // 触发点击下载
      link.click();

      // 清理 DOM
      document.body.removeChild(link);
      this.commissionopen = false

    },

    haibao() {
      this.commissionopen = true;
    },
    cancel() {
      this.commissionopen = false
    },
    select() {
      selectRevenue().then(res => {
        if (res.data) {
          this.productList = res.data;
        }

      });
      getAgentExtendUrlVO({}).then(res => {
        if (res.data) {
          this.sc = res.data;
        }
      })
    },
    // 获取统计数据
    async getStatsData() {
      try {
        const response = await selectDashboardStatistics(this.statisticsType);
        if (response.code === 200 && response.data) {
          const data = response.data;
          // 映射接口返回字段到前端字段
          this.todayStats = {
            invitations: data.todayInviteCount || 0,
            orders: data.todayOrderCount || 0,
            activations: data.todayActivateCount || 0,
            commission: Math.round((data.todayCommission || 0) * 100) // 元转换为分
          };
          this.monthStats = {
            invitations: data.monthInviteCount || 0,
            orders: data.monthOrderCount || 0,
            activations: data.monthActivateCount || 0,
            commission: Math.round((data.monthCommission || 0) * 100) // 元转换为分
          };
        }
      } catch (error) {
        console.error('获取统计数据失败:', error);
        // 保持默认值不变，避免页面报错
      }
    },
    // 跳转到可视化数据页面
    goToVisualData() {
      this.$tab.navigateTo('/pages/mine/visual-data/index')
    },
    // 切换统计类型
    switchStatisticsType(type) {
      if (this.statisticsType !== type) {
        this.statisticsType = type;
        this.getStatsData(); // 重新获取统计数据
      }
    },
    use() {
      this.$tab.navigateTo('/pages/home/use')
    },

    getRealNameStatusText(status) {
      const statusMap = {
        [constant.REAL_NAME_STATUS.UNVERIFIED]: '未认证',
        [constant.REAL_NAME_STATUS.VERIFYING]: '认证中',
        [constant.REAL_NAME_STATUS.VERIFIED]: '已认证',
        [constant.REAL_NAME_STATUS.FAILED]: '认证失败'
      }
      return statusMap[status] || '未认证'
    },
    payouts() {
      const status = this.agentAccount.realNameStatus
      const isVerified = status === constant.REAL_NAME_STATUS.VERIFIED
      if (!isVerified) {
        const statusText = this.getRealNameStatusText(status)
        const content = status === constant.REAL_NAME_STATUS.UNVERIFIED
          ? '您还未进行实名认证，提现功能需要完成实名认证后才能使用。是否前往实名认证？'
          : `当前实名认证状态为“${statusText}”，通过审核后才能使用提现功能。是否前往实名认证页面查看进度？`
        this.$modal.confirm(content).then(() => {
          this.$tab.navigateTo('/pages/mine/realname/index')
        }).catch(() => {})
        return
      }
      this.$tab.navigateTo('/pages/mine/payouts/index')
    },
    product() {
      this.$tab.navigateTo('/pages/mine/product/index')
    },
    handleToInfo() {
      this.$tab.navigateTo('/pages/mine/info/index')
    },
    handleToEditInfo() {
      this.$tab.navigateTo('/pages/mine/info/edit')
    },
    handleToSetting() {
      this.$tab.navigateTo('/pages/mine/setting/index')
    },
    handleToLogin() {
      this.$tab.reLaunch('/pages/login')
    },
    handleToAvatar() {
      this.$tab.navigateTo('/pages/mine/avatar/index')
    },
    handleHelp() {
      this.$tab.navigateTo('/pages/mine/help/index')
    },
    handleAbout() {
      this.$tab.navigateTo('/pages/mine/about/index')
    },
    myAgents() {
      this.$tab.navigateTo('/pages/mine/my-agents/index')
    },
    commissionDetail() {
      this.$tab.navigateTo('/pages/mine/commission-detail/index')
    },
    customerService() {
      // H5环境下直接跳转到企业微信客服
      // #ifdef H5
      window.location.href = 'https://work.weixin.qq.com/kfid/kfcf856088b08cab7ad';
      // #endif
      // #ifndef H5
      this.$tab.navigateTo('/pages/mine/customer-service/index')
      // #endif
    },
    liveConnect() {
      this.$tab.navigateTo('/pages/mine/live-connect/index')
    },
    systemNotice() {
      this.$tab.navigateTo('/pages/notice/list')
    },
    vipBenefits() {
      this.$tab.navigateTo('/pages/mine/commission/index')
    },
    invite() {
      this.$tab.navigateTo('/pages/mine/about/index')
    },
    handleJiaoLiuQun() {
      this.$modal.showToast('QQ群：①133713780(满)、②146013835(满)、③189091635')
    },
    handleBuilding() {
      this.$modal.showToast('模块建设中~')
    }
  }
}
</script>

<style lang="scss" scoped>
page {
  background-color: #f5f6f7;
}

.action-item {
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
}

.mine-container {
  width: 100%;
  height: 100%;


  .header-section {
    padding: 30rpx 30rpx 90rpx;

    .header-content {
      display: flex;
      align-items: flex-start;
      justify-content: flex-start;
      gap: 24rpx;
      width: 100%;
      box-sizing: border-box;
      flex-wrap: wrap;
    }

    .profile-main {
      display: flex;
      align-items: flex-start;
      flex: 1;
      min-width: 0;
    }

    .profile-avatar {
      width: 120rpx;
      height: 120rpx;
      flex-shrink: 0;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .profile-avatar .cu-avatar {
      width: 120rpx;
      height: 120rpx;
      border: 2rpx solid #eaeaea;
    }

    .profile-avatar image {
      width: 120rpx;
      height: 120rpx;
      border-radius: 50%;
      object-fit: cover;
    }

    .profile-avatar .icon {
      font-size: 40rpx;
    }

    .profile-details {
      margin-left: 24rpx;
      flex: 1;
      display: flex;
      flex-direction: column;
      justify-content: center;
      min-width: 0;
    }

    .login-tip {
      font-size: 32rpx;
      color: #4b5563;
      cursor: pointer;
    }

    .user-info {
      display: flex;
      flex-direction: column;
      gap: 8rpx;
      min-width: 0;
      cursor: pointer;
    }

    .user-info__name {
      font-size: 36rpx;
      line-height: 46rpx;
      font-weight: 700;
      color: #1f2d3d;
      letter-spacing: 1rpx;
    }

    .user-info__name-text {
      max-width: 480rpx;
      display: inline-block;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .profile-action {
      align-self: flex-start;
      display: inline-flex;
      align-items: center;
      gap: 16rpx;
      padding: 20rpx 36rpx;
      border-radius: 999rpx;
      border: 1rpx solid #edf2f9;
      background: #ffffff;
      box-shadow: 0 16rpx 36rpx rgba(31, 45, 61, 0.12);
      color: #1f2d3d;
      font-size: 28rpx;
      cursor: pointer;
      margin-top: 12rpx;
      flex-shrink: 0;
      margin-left: auto;
    }

    .profile-action__text {
      font-size: 28rpx;
      font-weight: 600;
      letter-spacing: 1rpx;
    }

    .profile-action:active {
      transform: scale(0.97);
      box-shadow: 0 18rpx 38rpx rgba(255, 214, 102, 0.28);
    }
  }

.vip-summary-strip {
  margin: -60rpx 30rpx 24rpx;
  padding: 24rpx 28rpx;
  border-radius: 24rpx;
  background: linear-gradient(135deg, #fff4d6 0%, #ffe0a1 100%);
  box-shadow: 0 22rpx 40rpx rgba(255, 214, 102, 0.35);
  display: flex;
  align-items: center;
  gap: 20rpx;
  cursor: pointer;
}

  .vip-summary-strip__icon {
    width: 80rpx;
    height: 80rpx;
    border-radius: 20rpx;
    background: rgba(255, 214, 102, 0.25);
    display: flex;
    align-items: center;
    justify-content: center;
    overflow: hidden;
    flex-shrink: 0;
  }

  .vip-summary-strip__icon image {
    width: 48rpx;
    height: 48rpx;
  }

  .vip-summary-strip__text {
    flex: 1;
    min-width: 0;
    display: flex;
    flex-direction: column;
    gap: 8rpx;
  }

  .vip-summary-strip__title {
    font-size: 30rpx;
    font-weight: 600;
    color: #8c4b05;
  }

  .vip-summary-strip__subtitle {
    font-size: 24rpx;
    color: rgba(140, 75, 5, 0.75);
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .vip-summary-strip__action {
    display: inline-flex;
    align-items: center;
    gap: 8rpx;
    color: #c46b08;
    font-size: 24rpx;
    flex-shrink: 0;
  }

  .vip-summary-strip:active {
    transform: scale(0.98);
  }

.content-section {
  position: relative;
  top: 0;
  margin-top: 0;

  & + .content-section {
    margin-top: 20rpx;
  }

    .mine-actions {
      margin: 15px 15px;
      padding: 25px 15px;
      border-radius: 8px;
      background-color: white;

      .function-btn {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        margin-bottom: 15px;

        .btn-icon {
          width: 45px;
          height: 45px;
          margin-bottom: 8px;
          border-radius: 8px;
          background-color: rgba(240, 155, 127, 0.1);
          border: 1px solid rgba(240, 155, 127, 0.2);
        }

        .btn-icon-with-padding {
          width: 45px;
          height: 45px;
          margin-bottom: 8px;
          padding: 8px;
          border-radius: 8px;
          background-color: rgba(240, 155, 127, 0.1);
          border: 1px solid rgba(240, 155, 127, 0.2);
        }

        .btn-text {
          font-size: 12px;
          color: #333;
          text-align: center;
        }
      }

      .action-item {
        .icon {
          font-size: 28px;
        }

        .text {
          display: block;
          font-size: 13px;
          margin: 8px 0;
        }
      }
    }

    // 数据统计样式
    .stats-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;

      .shu1 {
        display: flex;
        align-items: center;
        font-size: 16px;
        font-weight: 300;
        color: #333;

        u-icon {
          margin-right: 15px;
        }

        &:last-child {
          cursor: pointer;
          font-size: 14px;
        }
      }

      .shu2 {
        display: flex;
        align-items: center;
        font-size: 16px;
        font-weight: 400;
        color: #333;

        u-icon {
          margin-right: 15px;
        }

        &:last-child {
          cursor: pointer;
          font-size: 14px;
        }
      }

    }

    .stats-section {
      .stats-title {
        font-size: 14px;
        color: #666;
        margin-bottom: 10px;
        font-weight: 500;
      }
    }

    // 统计数据卡片样式 (参考首页样式)
    .orderclass-dd {
      display: flex;
      flex-direction: column;
      align-items: center;
      flex: 1;
    }

    .dd-vla {
      font-size: 18px;
      font-weight: bold;
      color: #333;
      text-align: center;
      margin-bottom: 8px;
    }

    .dd-oimg {
      display: flex;
      flex-direction: column;
      align-items: center;

      .imgsi-dd {
        width: 24px;
        height: 24px;
        margin-bottom: 4px;
      }
    }

    .dd-lab {
      font-size: 12px;
      color: #999;
      text-align: center;
    }

    // 统计类型切换样式
    .type-switch {
      .switch-item {
        &.active {
          background-color: #f09b7f;
          color: white !important;
        }

        &:hover {
          opacity: 0.8;
        }
      }
    }
  }

  
  // 收益展示卡片样式
  .revenue-card {
    display: flex;
    background-color: #f09b7f;
    color: #fff;
    margin: 30rpx;
    border-radius: 8rpx;
    overflow: hidden;
    align-items: center;

    .revenue-info {
      display: flex;
      flex: 1;
      align-items: center;
      padding: 40rpx 30rpx;

      .revenue-item {
        display: flex;
        flex-direction: column;
        align-items: center;
        flex: 1;

        .revenue-label {
          font-size: 28rpx;
          margin-bottom: 10rpx;
          opacity: 0.9;
        }

        .revenue-value {
          font-size: 32rpx;
          font-weight: bold;
        }
      }

      .revenue-divider {
        width: 1rpx;
        height: 60rpx;
        background-color: rgba(255, 255, 255, 0.3);
        margin: 0 20rpx;
      }
    }

    .withdraw-btn {
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: rgba(255, 255, 255, 0.2);
      padding: 40rpx 30rpx;
      margin: 20rpx;
      border-radius: 8rpx;
      min-width: 160rpx;

      .withdraw-text {
        font-size: 28rpx;
        color: #fff;
        font-weight: 500;
      }
    }
  }

  // 数据统计卡片样式
  .stats-card {
    margin: 30rpx;
    padding: 40rpx;
    border-radius: 8rpx;
    background-color: white;

    .stats-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 40rpx;

      .stats-title {
        font-size: 32rpx;
        font-weight: 500;
        color: #333;
      }

      .stats-controls {
        display: flex;
        align-items: center;
        gap: 30rpx;

        .type-switch-wrapper {
          display: flex;
          background: #f5f5f5;
          border-radius: 8rpx;
          padding: 4rpx;

          .switch-item {
            padding: 12rpx 24rpx;
            border-radius: 8rpx;
            font-size: 24rpx;
            color: #666;
            transition: all 0.3s;

            &.active {
              background-color: #f09b7f;
              color: white;
            }
          }
        }

        .visual-data-link {
          display: flex;
          align-items: center;
          font-size: 28rpx;
          color: #666;

          u-icon {
            margin-left: 8rpx;
          }
        }
      }
    }

    .stats-section {
      .stats-data-row {
        display: flex;
        justify-content: space-around;
        margin-top: 30rpx;

        .stats-item {
          display: flex;
          flex-direction: column;
          align-items: center;
          flex: 1;

          .stats-value {
            font-size: 36rpx;
            font-weight: bold;
            color: #333;
            margin-bottom: 16rpx;
          }

          .stats-label {
            font-size: 24rpx;
            color: #999;
          }
        }
      }

      &.stats-section-month {
        margin-top: 40rpx;
        padding-top: 40rpx;
        border-top: 1rpx solid #f0f0f0;
      }
    }
  }

  // 二维码图片样式
  .qrcode-image {
    width: 100%;
    height: 800rpx;
    object-fit: contain;
  }
}

.vip-popup {
  padding: 20px 20px 28px;
  background-color: #ffffff;
  border-radius: 26px 26px 0 0;
  min-height: 260px;

  &__header {
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 12px;
  }

  &__title {
    font-size: 16px;
    font-weight: 600;
    color: #1f2d3d;
  }

  &__close {
    position: absolute;
    right: 0;
    top: 50%;
    transform: translateY(-50%);
    padding: 4px;
  }

  &__body {
    max-height: 60vh;
    display: flex;
    flex-direction: column;
  }

  &__status {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 32px 0;
    font-size: 13px;
    color: #909399;
    text-align: center;

    &--error {
      color: #d46b08;
    }
  }

  &__retry {
    margin-top: 12px;
    padding: 6px 20px;
    border-radius: 22px;
    background: linear-gradient(135deg, #ffd666 0%, #ffc53d 100%);
    color: #8c4b05;
    font-size: 13px;
  }

  &__scroll {
    max-height: 52vh;
  }

  &__cards {
    display: flex;
    flex-direction: column;
    gap: 14px;
    padding-bottom: 12px;
  }

  &__card {
    display: flex;
    align-items: center;
    padding: 16px;
    border-radius: 16px;
    background: linear-gradient(135deg, #ffffff 0%, #f7fbff 100%);
    box-shadow: 0 10px 24px rgba(31, 45, 61, 0.12);
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

    &-icon {
      width: 42px;
      height: 42px;
      border-radius: 14px;
      background: rgba(255, 214, 102, 0.25);
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 14px;
      overflow: hidden;

      image {
        width: 70%;
        height: 70%;
      }
    }

    &-info {
      flex: 1;
      display: flex;
      flex-direction: column;
      min-width: 0;
      position: relative;
    }

    &-level {
      font-size: 16px;
      font-weight: 700;
      color: #2f3c50;
    }

    &-name {
      font-size: 12px;
      color: rgba(47, 60, 80, 0.7);
      margin-top: 2px;
    }

    &-orders {
      font-size: 11px;
      color: rgba(47, 60, 80, 0.55);
      margin-top: 4px;
    }

    &-remark {
      font-size: 11px;
      color: rgba(47, 60, 80, 0.55);
      margin-top: 2px;
      overflow: hidden;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
    }

    &-amount {
      text-align: right;
    }

    &-value {
      font-size: 18px;
      font-weight: 700;
      color: #d46b08;
      display: block;
    }

    &-desc {
      font-size: 12px;
      color: rgba(47, 60, 80, 0.65);
      margin-top: 4px;
      display: block;
    }

    &.is-active {
      border: 1px solid rgba(250, 173, 20, 0.6);
      box-shadow: 0 18px 32px rgba(250, 173, 20, 0.25);
      transform: translateY(-2px);
    }
  }
}
</style>
