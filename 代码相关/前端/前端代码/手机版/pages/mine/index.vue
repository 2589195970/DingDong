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
            <view class="u_title">
              {{ name }}
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

      <view class="mine-actions grid col-4 text-center" style="background-color: #f09b7f; color: #fff;">
        <view style="display: flex; width: 60%;justify-content: space-around; border-right: 1px solid #FFF;">
          <view style="display: flex; flex-direction: column; align-items: center;" @click="product">
            <text>累计收益</text>
            <text>{{ productList.depositAmount * 0.01 }}元</text>
          </view>
          <view style="display: flex; flex-direction: column; align-items: center;" @click="product">
            <text>可提现收益</text>
            <text>{{ productList.balance * 0.01 }}元</text>
          </view>
        </view>
        <view class="" @click="payouts">
          <text style="width: 100%; text-align: center; line-height: 40px;">立即提现</text>
        </view>

      </view>

    </view>

    <!-- 数据统计组件 -->
    <view class="content-section">
      <view class="mine-actions" style="margin: 15px 15px; padding: 20px; border-radius: 8px; background-color: white;">
        <view class="stats-header" style="display: flex; justify-content: space-between; margin-bottom: 20px;">
          <view class="shu2">
            数据统计
          </view>
          <view class="shu1" @click="goToVisualData">
            可视化数据
            <u-icon name="arrow-right" color="#999" size="14"></u-icon>
          </view>
        </view>

        <!-- 今日数据 -->
        <view class="stats-section">
          <view class="daili-kan"
                style="display: flex; flex-wrap: nowrap; justify-content: space-around; margin-top: 15px;">
            <view class="orderclass-dd">
              <view class="dd-vla">{{ todayStats.invitations || 0 }}</view>
              <view class="dd-oimg">
                <view class="dd-lab">今日邀请</view>
              </view>
            </view>
            <view class="orderclass-dd">
              <view class="dd-vla">{{ todayStats.orders || 0 }}</view>
              <view class="dd-oimg">
                <view class="dd-lab">今日订单</view>
              </view>
            </view>
            <view class="orderclass-dd">
              <view class="dd-vla">{{ todayStats.activations || 0 }}</view>
              <view class="dd-oimg">
                <view class="dd-lab">今日激活</view>
              </view>
            </view>
            <view class="orderclass-dd">
              <view class="dd-vla">{{ (todayStats.commission || 0) * 0.01 }}元</view>
              <view class="dd-oimg">
                <view class="dd-lab">今日佣金</view>
              </view>
            </view>
          </view>
        </view>

        <!-- 本月数据 -->
        <view class="stats-section" style="margin-top: 20px;">
          <view class="daili-kan"
                style="display: flex; flex-wrap: nowrap; justify-content: space-around; margin-top: 15px;">
            <view class="orderclass-dd">
              <view class="dd-vla">{{ monthStats.invitations || 0 }}</view>
              <view class="dd-oimg">
                <view class="dd-lab">本月邀请</view>
              </view>
            </view>
            <view class="orderclass-dd">
              <view class="dd-vla">{{ monthStats.orders || 0 }}</view>
              <view class="dd-oimg">
                <view class="dd-lab">本月订单</view>
              </view>
            </view>
            <view class="orderclass-dd">
              <view class="dd-vla">{{ monthStats.activations || 0 }}</view>
              <view class="dd-oimg">
                <view class="dd-lab">本月激活</view>
              </view>
            </view>
            <view class="orderclass-dd">
              <view class="dd-vla">{{ (monthStats.commission || 0) * 0.01 }}元</view>
              <view class="dd-oimg">
                <view class="dd-lab">本月佣金</view>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>
    <view class="content-section">

      <view class="mine-actions grid col-4 text-center">
        <view class="function-btn" @click="product">
          <image src="@/static/images/mine/商品管理.png" class="btn-icon"></image>
          <text class="btn-text">商品设置</text>
        </view>
        <view class="function-btn" @click="miansc">
          <image src="@/static/images/mine/商城.png" class="btn-icon-with-padding"></image>
          <text class="btn-text">我的商城</text>
        </view>
        <view class="function-btn" @click="haibao">
          <image src="@/static/images/mine/商城.png" class="btn-icon-with-padding"></image>
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
    <u-modal :show="commissionopen" @confirm="confirm" ref="uModal" @cancel="cancel" :showCancelButton='true'
             confirmText="保存图片" :asyncClose="true">
      <image :src="sc.shopQrcodeMap" alt="" style="height: 400px;"/>
    </u-modal>
  </view>
</template>

<script>
import {
  selectRevenue,
  getAgentExtendUrlVO
} from "@/api/order/order.js";

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
  onLoad() {
    this.select(); //获取列表
    this.getStatsData(); //获取统计数据
  },
  computed: {
    avatar() {
      return this.$store.state.user.avatar
    },
    windowHeight() {
      return uni.getSystemInfoSync().windowHeight - 50
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
    getStatsData() {
      // TODO: 这里需要调用实际的API接口获取统计数据
      // 目前使用模拟数据
      this.todayStats = {
        invitations: 5,
        orders: 12,
        activations: 8,
        commission: 15000 // 分为单位，显示时除以100
      };
      this.monthStats = {
        invitations: 156,
        orders: 380,
        activations: 245,
        commission: 450000 // 分为单位，显示时除以100
      };
    },
    // 跳转到可视化数据页面
    goToVisualData() {
      this.$tab.navigateTo('/pages/mine/visual-data/index')
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
      this.$tab.navigateTo('/pages/mine/customer-service/index')
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
      padding: 20px 0px;
      border-radius: 8px;
      background-color: white;

      .function-btn {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        margin-bottom: 10px;

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
  }
}
</style>