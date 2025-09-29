<template>
  <view class="page-container">
    <app-navbar title="抖音小黄车直播配置"></app-navbar>

    <!-- 配置表单 -->
    <view class="config-section">
      <view class="section-title">
        <u-icon name="setting" color="#f09b7f"></u-icon>
        <text>基础配置</text>
      </view>

      <view class="config-form">
        <!-- API密钥配置 -->
        <view class="form-group">
          <text class="form-label">API密钥 *</text>
          <u--input
            v-model="liveConfig.apiKey"
            placeholder="请输入抖音开放平台API密钥"
            type="password"
            :show-password="true"
            border="surround"
          ></u--input>
          <text class="form-tip">请从抖音开放平台获取API密钥</text>
        </view>

        <!-- 应用ID -->
        <view class="form-group">
          <text class="form-label">应用ID *</text>
          <u--input
            v-model="liveConfig.appId"
            placeholder="请输入抖音应用ID"
            border="surround"
          ></u--input>
        </view>

        <!-- 直播间ID（可选） -->
        <view class="form-group">
          <text class="form-label">直播间ID</text>
          <u--input
            v-model="liveConfig.roomId"
            placeholder="默认为主直播间"
            border="surround"
          ></u--input>
          <text class="form-tip">留空则使用默认直播间</text>
        </view>

        <!-- 回调地址 -->
        <view class="form-group">
          <text class="form-label">回调地址</text>
          <view class="readonly-input">
            <text>{{ callbackUrl }}</text>
            <u-button size="mini" type="primary" plain @click="copyCallback">复制</u-button>
          </view>
          <text class="form-tip">请将此地址配置到抖音开放平台回调设置中</text>
        </view>
      </view>
    </view>

    <!-- 功能配置 -->
    <view class="feature-section">
      <view class="section-title">
        <u-icon name="tool" color="#f09b7f"></u-icon>
        <text>功能配置</text>
      </view>

      <view class="feature-list">
        <view class="feature-item">
          <view class="feature-info">
            <text class="feature-name">自动推送商品</text>
            <text class="feature-desc">直播时自动推送号卡商品</text>
          </view>
          <u-switch v-model="liveConfig.autoPushProduct" active-color="#4cd964"></u-switch>
        </view>

        <view class="feature-item">
          <view class="feature-info">
            <text class="feature-name">实时订单提醒</text>
            <text class="feature-desc">有新订单时发送直播间提醒</text>
          </view>
          <u-switch v-model="liveConfig.orderNotify" active-color="#4cd964"></u-switch>
        </view>

        <view class="feature-item">
          <view class="feature-info">
            <text class="feature-name">粉丝专属优惠</text>
            <text class="feature-desc">为直播间粉丝提供专属优惠码</text>
          </view>
          <u-switch v-model="liveConfig.fanDiscount" active-color="#4cd964"></u-switch>
        </view>

        <view class="feature-item">
          <view class="feature-info">
            <text class="feature-name">数据统计同步</text>
            <text class="feature-desc">同步直播间销售数据到平台</text>
          </view>
          <u-switch v-model="liveConfig.syncStats" active-color="#4cd964"></u-switch>
        </view>
      </view>
    </view>

    <!-- 高级配置 -->
    <view class="advanced-section">
      <view class="section-title" @click="showAdvanced = !showAdvanced">
        <u-icon name="settings" color="#f09b7f"></u-icon>
        <text>高级配置</text>
        <u-icon
          name="arrow-down"
          :style="{ transform: showAdvanced ? 'rotate(180deg)' : 'rotate(0deg)' }"
          color="#c0c4cc"
          size="16"
        ></u-icon>
      </view>

      <view class="advanced-config" v-if="showAdvanced">
        <view class="form-group">
          <text class="form-label">连接超时（秒）</text>
          <u-number-box
            v-model="liveConfig.timeout"
            :min="5"
            :max="60"
            :step="5"
          ></u-number-box>
        </view>

        <view class="form-group">
          <text class="form-label">重试次数</text>
          <u-number-box
            v-model="liveConfig.retryTimes"
            :min="1"
            :max="10"
            :step="1"
          ></u-number-box>
        </view>

        <view class="form-group">
          <text class="form-label">启用日志</text>
          <u-switch v-model="liveConfig.enableLog" active-color="#4cd964"></u-switch>
        </view>
      </view>
    </view>

    <!-- 底部操作按钮 -->
    <view class="bottom-actions">
      <u-button
        type="info"
        size="large"
        plain
        @click="testConnection"
        :loading="testing"
        :customStyle="{ flex: 1, marginRight: '10px' }"
      >{{ testing ? '测试中...' : '测试连接' }}</u-button>
      <u-button
        type="primary"
        size="large"
        @click="saveConfig"
        :loading="saving"
        :customStyle="{ flex: 2, backgroundColor: '#f09b7f', borderColor: '#f09b7f' }"
      >{{ saving ? '保存中...' : '保存配置' }}</u-button>
    </view>

    <!-- 测试结果弹窗 -->
    <u-popup v-model="showTestResult" mode="center" border-radius="10">
      <view class="test-result-popup">
        <view class="result-header">
          <u-icon
            :name="testResult.success ? 'checkmark-circle' : 'close-circle'"
            :color="testResult.success ? '#4cd964' : '#ff6b6b'"
            size="32"
          ></u-icon>
          <text class="result-title">{{ testResult.success ? '连接成功' : '连接失败' }}</text>
        </view>

        <view class="result-content">
          <text class="result-message">{{ testResult.message }}</text>
          <view v-if="testResult.details" class="result-details">
            <text class="details-title">详细信息:</text>
            <text class="details-content">{{ testResult.details }}</text>
          </view>
        </view>

        <view class="result-actions">
          <u-button @click="showTestResult = false" type="primary" size="small">确定</u-button>
        </view>
      </view>
    </u-popup>
  </view>
</template>

<script>
export default {
  name: 'CreateLive',
  data() {
    return {
      // 直播配置
      liveConfig: {
        platformId: 'douyin-cart',
        apiKey: '',
        appId: '',
        roomId: '',
        timeout: 30,
        retryTimes: 3,
        enableLog: false,
        // 功能配置
        autoPushProduct: true,
        orderNotify: true,
        fanDiscount: false,
        syncStats: true
      },

      // 回调地址
      callbackUrl: 'https://api.dingdong.com/live/callback/douyin',

      // 界面状态
      showAdvanced: false,
      testing: false,
      saving: false,

      // 测试结果
      showTestResult: false,
      testResult: {
        success: false,
        message: '',
        details: ''
      }
    };
  },

  onLoad() {
    this.loadLiveConfig();
  },

  methods: {
    // 加载直播配置
    async loadLiveConfig() {
      try {
        // 模拟API调用
        // const response = await getLiveConfig('douyin-cart');
        // this.liveConfig = { ...this.liveConfig, ...response.data };
        console.log('加载抖音小黄车直播配置');
      } catch (error) {
        console.error('加载配置失败:', error);
      }
    },

    // 测试连接
    async testConnection() {
      if (!this.liveConfig.apiKey || !this.liveConfig.appId) {
        this.$u.toast('请先配置API密钥和应用ID');
        return;
      }

      this.testing = true;
      try {
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 2000));

        this.testResult = {
          success: true,
          message: '连接成功！已成功连接到抖音小黄车直播平台',
          details: '您可以开始使用直播带货功能'
        };

        this.showTestResult = true;

      } catch (error) {
        this.testResult = {
          success: false,
          message: '测试连接失败',
          details: error.message || '网络错误，请重试'
        };
        this.showTestResult = true;
      } finally {
        this.testing = false;
      }
    },

    // 保存配置
    async saveConfig() {
      if (!this.liveConfig.apiKey || !this.liveConfig.appId) {
        this.$u.toast('请完善必填配置项');
        return;
      }

      this.saving = true;
      try {
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 1500));

        this.$u.toast('配置保存成功');

        // 延迟返回上一页
        setTimeout(() => {
          uni.navigateBack();
        }, 1000);

      } catch (error) {
        this.$u.toast('保存失败，请重试');
        console.error('保存配置失败:', error);
      } finally {
        this.saving = false;
      }
    },

    // 复制回调地址
    copyCallback() {
      uni.setClipboardData({
        data: this.callbackUrl,
        success: () => {
          this.$u.toast('回调地址已复制');
        }
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.page-container {
  background-color: #f5f5f5;
  min-height: 100vh;
  padding-bottom: 80px;
}

.config-section, .feature-section, .advanced-section {
  background: white;
  margin: 10px 15px;
  padding: 15px;
  border-radius: 10px;

  .section-title {
    display: flex;
    align-items: center;
    margin-bottom: 15px;

    text {
      margin-left: 8px;
      font-size: 16px;
      font-weight: bold;
      color: #333;
      flex: 1;
    }
  }
}

.config-form, .advanced-config {
  .form-group {
    margin-bottom: 20px;

    &:last-child {
      margin-bottom: 0;
    }

    .form-label {
      display: block;
      font-size: 14px;
      font-weight: bold;
      color: #333;
      margin-bottom: 8px;
    }

    .form-tip {
      display: block;
      font-size: 12px;
      color: #999;
      margin-top: 5px;
    }

    .readonly-input {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 10px 15px;
      background: #f8f9fa;
      border-radius: 6px;
      border: 1px solid #e9ecef;

      text {
        flex: 1;
        font-size: 14px;
        color: #666;
        word-break: break-all;
        margin-right: 10px;
      }
    }
  }
}

.feature-list {
  .feature-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 15px 0;
    border-bottom: 1px solid #f0f0f0;

    &:last-child {
      border-bottom: none;
    }

    .feature-info {
      .feature-name {
        display: block;
        font-size: 14px;
        font-weight: bold;
        color: #333;
        margin-bottom: 3px;
      }

      .feature-desc {
        display: block;
        font-size: 12px;
        color: #666;
      }
    }
  }
}

.bottom-actions {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  padding: 15px;
  background: white;
  border-top: 1px solid #f0f0f0;
}

// 测试结果弹窗
.test-result-popup {
  padding: 30px 20px 20px;
  text-align: center;

  .result-header {
    margin-bottom: 20px;

    .result-title {
      display: block;
      font-size: 18px;
      font-weight: bold;
      color: #333;
      margin-top: 10px;
    }
  }

  .result-content {
    margin-bottom: 20px;

    .result-message {
      display: block;
      font-size: 14px;
      color: #666;
      margin-bottom: 10px;
    }

    .result-details {
      background: #f8f9fa;
      border-radius: 6px;
      padding: 10px;
      text-align: left;

      .details-title {
        display: block;
        font-size: 12px;
        font-weight: bold;
        color: #333;
        margin-bottom: 5px;
      }

      .details-content {
        display: block;
        font-size: 12px;
        color: #666;
        line-height: 1.5;
      }
    }
  }

  .result-actions {
    display: flex;
    justify-content: center;
  }
}
</style>