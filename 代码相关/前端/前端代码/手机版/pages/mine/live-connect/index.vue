<template>
  <view class="page-container">
    <app-navbar title="直播对接"></app-navbar>
    <!-- 直播注意事项 -->
    <view class="notice-section">
      <view class="section-title">
        <text>直播注意事项</text>
      </view>

      <view class="notice-content">
        <text class="notice-text">
          绑定审核时间24小时内，节假日清单独联系上游处理，多账户绑定同一个账号，禁止虚假选择、私自包装，违约者解除合作，快手和抖音小程序官方有使用考核，挂载不适用或者浪费名额的将会解除权限
        </text>
      </view>
    </view>
    <!-- 直播平台网格 -->
    <view class="platform-section">
      <view class="platform-grid">
        <view
          class="platform-card"
          @click="handlePlatformClick('douyin-cart')"
        >
          <view class="platform-icon">
            <image src="@/static/images/terrace/小黄车.png" class="platform-image" mode="aspectFit"></image>
          </view>
          <text class="platform-name">抖音小黄车直播</text>
          <text class="platform-status available">可用</text>
        </view>

        <view
          class="platform-card disabled"
          @click="handlePlatformClick('kuaishou-cart')"
        >
          <view class="platform-icon">
            <image src="@/static/images/terrace/小黄车.png" class="platform-image" mode="aspectFit"></image>
          </view>
          <text class="platform-name">快手小黄车直播</text>
          <text class="platform-status preparing">筹备中</text>
        </view>

        <view
          class="platform-card disabled"
          @click="handlePlatformClick('douyin-mini')"
        >
          <view class="platform-icon">
            <image src="@/static/images/terrace/抖音.png" class="platform-image" mode="aspectFit"></image>
          </view>
          <text class="platform-name">抖音小程序直播</text>
          <text class="platform-status preparing">筹备中</text>
        </view>

        <view
          class="platform-card disabled"
          @click="handlePlatformClick('kuaishou-mini')"
        >
          <view class="platform-icon">
            <image src="@/static/images/terrace/快手.png" class="platform-image" mode="aspectFit"></image>
          </view>
          <text class="platform-name">快手小程序直播</text>
          <text class="platform-status preparing">筹备中</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: 'LiveConnect',

  methods: {
    // 处理平台点击事件
    handlePlatformClick(platformType) {
      switch (platformType) {
        case 'douyin-cart':
          // 跳转到抖音小黄车直播配置页面
          uni.navigateTo({
            url: '/pages/mine/live-connect/create-live'
          });
          break;
        case 'kuaishou-cart':
        case 'douyin-mini':
        case 'kuaishou-mini':
          // 显示暂未开放提示
          this.$u.toast('正在筹备暂未开放');
          break;
        default:
          console.log('未知平台类型');
          break;
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.page-container {
  background-color: #f5f5f5;
  min-height: 100vh;
  padding-bottom: 20px;
}

.platform-section, .info-section, .notice-section {
  background: white;
  margin: 10px 15px;
  padding: 15px;
  border-radius: 10px;

  .section-title {
    display: flex;
    align-items: center;
    margin-bottom: 15px;

    .title-icon {
      width: 20px;
      height: 20px;
    }

    text {
      margin-left: 8px;
      font-size: 16px;
      font-weight: bold;
      color: #333;
    }
  }
}

.platform-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 15px;
}

.platform-card {
  background: #fff;
  border: 1px solid #e9ecef;
  border-radius: 10px;
  padding: 20px 15px;
  text-align: center;
  transition: all 0.3s ease;

  &:active {
    transform: scale(0.95);
  }

  &.disabled {
    opacity: 0.6;
    background: #f8f9fa;
  }

  .platform-icon {
    margin-bottom: 10px;

    .platform-image {
      width: 48px;
      height: 48px;
      border-radius: 8px;
    }
  }

  .platform-name {
    display: block;
    font-size: 14px;
    font-weight: bold;
    color: #333;
    margin-bottom: 5px;
    line-height: 1.3;
  }

  .platform-status {
    display: block;
    font-size: 12px;
    padding: 2px 8px;
    border-radius: 10px;
    margin-top: 5px;

    &.available {
      background: #e8f5e8;
      color: #4cd964;
      border: 1px solid #4cd964;
    }

    &.preparing {
      background: #fff3cd;
      color: #ff9800;
      border: 1px solid #ff9800;
    }
  }
}

.notice-section {
  border: 1px solid #ffebee;
  background: #fff8f8;

  .section-title {
    text {
      color: #ff6b6b;
    }
  }
}

.notice-content {
  .notice-text {
    display: block;
    font-size: 14px;
    color: #666;
    line-height: 1.6;
    padding: 10px 15px;
    background: #ffebee;
    border-radius: 8px;
  }
}

.info-content {
  .info-item {
    display: flex;
    align-items: center;
    padding: 8px 0;

    text {
      margin-left: 8px;
      font-size: 14px;
      color: #666;
      line-height: 1.4;
    }
  }
}
</style>