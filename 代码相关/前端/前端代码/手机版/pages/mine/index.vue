<template>
  <view class="mine-container" :style="{height: `${windowHeight}px`}">
    <!--顶部个人信息栏-->
    <view class="header-section">
      <view class="flex padding justify-between">
        <view class="flex align-center">
          <view v-if="!avatar" class="cu-avatar xl round bg-white">
            <view class="iconfont icon-people text-gray icon"></view>
          </view>
          <image v-if="avatar" @click="handleToAvatar" :src="avatar" class="cu-avatar xl round"
                 mode="widthFix">
          </image>
          <view v-if="!name" @click="handleToLogin" class="login-tip">
            点击登录
          </view>
          <view v-if="name" @click="handleToInfo" class="user-info">
            <view class="user-info__row">
              <view class="u_title">
                {{ name }}
              </view>
              <vip-badge v-if="vipInfo" :vip-info="vipInfo" size="small" />
            </view>
          </view>
        </view>
        <view @click="handleToInfo" class="flex align-center">
          <text>个人信息</text>
          <view class="iconfont icon-right"></view>
        </view>
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
        <view class="function-btn" @click="commission">
          <image src="@/static/images/mine/佣金设置.png" class="btn-icon"></image>
          <text class="btn-text">佣金设置</text>
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
import VipBadge from '@/components/vip-badge/vip-badge.vue'

export default {
  components: {
    VipBadge
  },
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
    }
  },
  computed: {
    vipInfo() {
      return this.$store.state.user.vipInfo || null
    }
  },
  onLoad() {
    this.select(); //获取列表
    this.getStatsData(); //获取统计数据
  },
  computed: {
    avatar() {
      return this.$store.state.user.avatar
    },
    windowHeight() {
      const systemInfo = uni.getSystemInfoSync();
      // 确保有足够的页面高度来显示所有内容
      return Math.max(systemInfo.windowHeight, 800);
    }
  },
  methods: {
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

    payouts() {
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
    commission() {
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
    padding: 15px 15px 45px 15px;
    // background-color: #3c96f3;
    // color: white;

    .login-tip {
      font-size: 18px;
      margin-left: 10px;
    }

    .cu-avatar {
      border: 2px solid #eaeaea;

      .icon {
        font-size: 40px;
      }
    }

      .user-info {
        margin-left: 15px;

        .user-info__row {
          display: flex;
          align-items: center;
        }

        .u_title {
          font-size: 18px;
          line-height: 30px;
        }
      }
    }

  .content-section {
    position: relative;
    top: -50px;

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
</style>
