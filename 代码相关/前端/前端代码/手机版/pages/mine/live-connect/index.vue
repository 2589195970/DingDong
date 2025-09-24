<template>
  <view class="page-container">
    <app-navbar title="直播对接"></app-navbar>

    <!-- 对接状态卡片 -->
    <view class="status-section">
      <view class="status-card">
        <view class="status-header">
          <u-icon
            name="wifi"
            :color="liveConfig.isConnected ? '#4cd964' : '#ff6b6b'"
            size="24"
          ></u-icon>
          <text class="status-title">直播对接状态</text>
        </view>

        <view class="status-content">
          <view class="status-info">
            <u-tag
              :text="liveConfig.isConnected ? '已连接' : '未连接'"
              :type="liveConfig.isConnected ? 'success' : 'error'"
              size="medium"
            ></u-tag>
            <text class="last-check" v-if="liveConfig.lastCheckTime">
              最后检测: {{ formatTime(liveConfig.lastCheckTime) }}
            </text>
          </view>

          <u-button
            size="small"
            :type="liveConfig.isConnected ? 'warning' : 'primary'"
            @click="testConnection"
            :loading="testing"
          >
            {{ testing ? '检测中...' : '测试连接' }}
          </u-button>
        </view>
      </view>
    </view>

    <!-- 平台选择 -->
    <view class="platform-section">
      <view class="section-title">
        <u-icon name="list" color="#f09b7f"></u-icon>
        <text>选择直播平台</text>
      </view>

      <view class="platform-list">
        <view
          v-for="platform in platforms"
          :key="platform.id"
          class="platform-item"
          :class="{ active: liveConfig.platformId === platform.id }"
          @click="selectPlatform(platform)"
        >
          <view class="platform-left">
            <image :src="platform.logo" class="platform-logo" mode="aspectFit"></image>
            <view class="platform-info">
              <text class="platform-name">{{ platform.name }}</text>
              <text class="platform-desc">{{ platform.description }}</text>
            </view>
          </view>

          <view class="platform-right">
            <u-icon
              name="checkmark-circle-fill"
              color="#4cd964"
              size="20"
              v-if="liveConfig.platformId === platform.id"
            ></u-icon>
            <u-icon
              name="arrow-right"
              color="#c0c4cc"
              size="16"
              v-else
            ></u-icon>
          </view>
        </view>
      </view>
    </view>

    <!-- 配置表单 -->
    <view class="config-section" v-if="selectedPlatform">
      <view class="section-title">
        <u-icon name="setting" color="#f09b7f"></u-icon>
        <text>{{ selectedPlatform.name }}配置</text>
      </view>

      <view class="config-form">
        <!-- API密钥配置 -->
        <view class="form-group">
          <text class="form-label">API密钥 *</text>
          <u--input
            v-model="liveConfig.apiKey"
            placeholder="请输入API密钥"
            type="password"
            :show-password="true"
            border="surround"
          ></u--input>
          <text class="form-tip">请从{{ selectedPlatform.name }}开放平台获取API密钥</text>
        </view>

        <!-- 应用ID -->
        <view class="form-group">
          <text class="form-label">应用ID *</text>
          <u--input
            v-model="liveConfig.appId"
            placeholder="请输入应用ID"
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
          <text class="form-tip">请将此地址配置到{{ selectedPlatform.name }}回调设置中</text>
        </view>

        <!-- 高级配置 -->
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

        <!-- 高级配置切换 -->
        <view class="advanced-toggle" @click="showAdvanced = !showAdvanced">
          <text>高级配置</text>
          <u-icon
            name="arrow-down"
            :style="{ transform: showAdvanced ? 'rotate(180deg)' : 'rotate(0deg)' }"
            color="#c0c4cc"
            size="16"
          ></u-icon>
        </view>
      </view>
    </view>

    <!-- 功能配置 -->
    <view class="feature-section" v-if="selectedPlatform">
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

    <!-- 帮助文档 -->
    <view class="help-section">
      <view class="section-title">
        <u-icon name="help-circle" color="#f09b7f"></u-icon>
        <text>帮助文档</text>
      </view>

      <view class="help-list">
        <view class="help-item" @click="openDoc('setup')">
          <u-icon name="file-text" color="#007aff" size="20"></u-icon>
          <text>对接配置教程</text>
          <u-icon name="arrow-right" color="#c0c4cc" size="16"></u-icon>
        </view>

        <view class="help-item" @click="openDoc('api')">
          <u-icon name="code" color="#4cd964" size="20"></u-icon>
          <text>API接口文档</text>
          <u-icon name="arrow-right" color="#c0c4cc" size="16"></u-icon>
        </view>

        <view class="help-item" @click="openDoc('faq')">
          <u-icon name="help" color="#ff9500" size="20"></u-icon>
          <text>常见问题解答</text>
          <u-icon name="arrow-right" color="#c0c4cc" size="16"></u-icon>
        </view>

        <view class="help-item" @click="contactSupport">
          <u-icon name="chat" color="#f56c6c" size="20"></u-icon>
          <text>联系技术支持</text>
          <u-icon name="arrow-right" color="#c0c4cc" size="16"></u-icon>
        </view>
      </view>
    </view>

    <!-- 底部操作按钮 -->
    <view class="bottom-actions">
      <u-button
        type="info"
        size="large"
        plain
        @click="resetConfig"
        :customStyle="{ flex: 1, marginRight: '10px' }"
      >重置配置</u-button>
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
import {
  getLiveConfig,
  saveLiveConfig,
  testLiveConnection,
  getLivePlatforms
} from '@/api/mine/live.js';

export default {
  name: 'LiveConnect',
  data() {
    return {
      // 直播配置
      liveConfig: {
        platformId: '',
        apiKey: '',
        appId: '',
        roomId: '',
        timeout: 30,
        retryTimes: 3,
        enableLog: false,
        isConnected: false,
        lastCheckTime: null,
        // 功能配置
        autoPushProduct: true,
        orderNotify: true,
        fanDiscount: false,
        syncStats: true
      },

      // 直播平台列表
      platforms: [
        {
          id: 'douyin',
          name: '抖音直播',
          description: '抖音平台直播带货',
          logo: '@/static/images/platforms/douyin.png'
        },
        {
          id: 'kuaishou',
          name: '快手直播',
          description: '快手平台直播带货',
          logo: '@/static/images/platforms/kuaishou.png'
        },
        {
          id: 'taobao',
          name: '淘宝直播',
          description: '淘宝平台直播带货',
          logo: '@/static/images/platforms/taobao.png'
        },
        {
          id: 'xiaohongshu',
          name: '小红书直播',
          description: '小红书平台直播带货',
          logo: '@/static/images/platforms/xiaohongshu.png'
        }
      ],

      // 选中的平台
      selectedPlatform: null,

      // 回调地址
      callbackUrl: 'https://api.dingdong.com/live/callback',

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
    this.loadPlatforms();
  },

  methods: {
    // 加载直播配置
    async loadLiveConfig() {
      try {
        const response = await getLiveConfig();
        this.liveConfig = { ...this.liveConfig, ...response.data };

        // 设置选中的平台
        if (this.liveConfig.platformId) {
          this.selectedPlatform = this.platforms.find(p => p.id === this.liveConfig.platformId);
        }
      } catch (error) {
        console.error('加载配置失败:', error);
      }
    },

    // 加载平台列表
    async loadPlatforms() {
      try {
        const response = await getLivePlatforms();
        if (response.data && response.data.length > 0) {
          this.platforms = response.data;
        }
      } catch (error) {
        console.error('加载平台列表失败:', error);
      }
    },

    // 选择平台
    selectPlatform(platform) {
      this.selectedPlatform = platform;
      this.liveConfig.platformId = platform.id;
    },

    // 测试连接
    async testConnection() {
      if (!this.selectedPlatform) {
        this.$u.toast('请先选择直播平台');
        return;
      }

      if (!this.liveConfig.apiKey || !this.liveConfig.appId) {
        this.$u.toast('请先配置API密钥和应用ID');
        return;
      }

      this.testing = true;
      try {
        const response = await testLiveConnection({
          platformId: this.liveConfig.platformId,
          apiKey: this.liveConfig.apiKey,
          appId: this.liveConfig.appId,
          roomId: this.liveConfig.roomId
        });

        this.testResult = {
          success: response.success,
          message: response.message || (response.success ? '连接成功' : '连接失败'),
          details: response.details || ''
        };

        // 更新连接状态
        this.liveConfig.isConnected = response.success;
        this.liveConfig.lastCheckTime = Date.now();

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
      if (!this.selectedPlatform) {
        this.$u.toast('请先选择直播平台');
        return;
      }

      if (!this.liveConfig.apiKey || !this.liveConfig.appId) {
        this.$u.toast('请完善必填配置项');
        return;
      }

      this.saving = true;
      try {
        const response = await saveLiveConfig(this.liveConfig);
        this.$u.toast('配置保存成功');

        // 刷新配置
        this.loadLiveConfig();
      } catch (error) {
        this.$u.toast('保存失败，请重试');
        console.error('保存配置失败:', error);
      } finally {
        this.saving = false;
      }
    },

    // 重置配置
    resetConfig() {
      uni.showModal({
        title: '确认重置',
        content: '确定要重置所有配置吗？此操作不可恢复。',
        success: (res) => {
          if (res.confirm) {
            this.liveConfig = {
              platformId: '',
              apiKey: '',
              appId: '',
              roomId: '',
              timeout: 30,
              retryTimes: 3,
              enableLog: false,
              isConnected: false,
              lastCheckTime: null,
              autoPushProduct: true,
              orderNotify: true,
              fanDiscount: false,
              syncStats: true
            };
            this.selectedPlatform = null;
            this.$u.toast('配置已重置');
          }
        }
      });
    },

    // 复制回调地址
    copyCallback() {
      uni.setClipboardData({
        data: this.callbackUrl,
        success: () => {
          this.$u.toast('回调地址已复制');
        }
      });
    },

    // 打开文档
    openDoc(type) {
      const urls = {
        setup: 'https://docs.dingdong.com/live/setup',
        api: 'https://docs.dingdong.com/live/api',
        faq: 'https://docs.dingdong.com/live/faq'
      };

      if (urls[type]) {
        uni.navigateTo({
          url: `/pages/common/webview/index?url=${encodeURIComponent(urls[type])}`
        });
      }
    },

    // 联系技术支持
    contactSupport() {
      uni.navigateTo({
        url: '/pages/mine/customer-service/index'
      });
    },

    // 格式化时间
    formatTime(timestamp) {
      if (!timestamp) return '';
      const date = new Date(timestamp);
      const month = (date.getMonth() + 1).toString().padStart(2, '0');
      const day = date.getDate().toString().padStart(2, '0');
      const hours = date.getHours().toString().padStart(2, '0');
      const minutes = date.getMinutes().toString().padStart(2, '0');
      return `${month}-${day} ${hours}:${minutes}`;
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

.status-section {
  padding: 15px;
  margin-bottom: 10px;

  .status-card {
    background: white;
    border-radius: 10px;
    padding: 20px;

    .status-header {
      display: flex;
      align-items: center;
      margin-bottom: 15px;

      .status-title {
        margin-left: 10px;
        font-size: 16px;
        font-weight: bold;
        color: #333;
      }
    }

    .status-content {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .status-info {
        .last-check {
          display: block;
          font-size: 12px;
          color: #999;
          margin-top: 5px;
        }
      }
    }
  }
}

.platform-section, .config-section, .feature-section, .help-section {
  background: white;
  margin-bottom: 10px;
  padding: 15px;

  .section-title {
    display: flex;
    align-items: center;
    margin-bottom: 15px;

    text {
      margin-left: 8px;
      font-size: 16px;
      font-weight: bold;
      color: #333;
    }
  }
}

.platform-list {
  .platform-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 15px;
    border: 1px solid #e9ecef;
    border-radius: 8px;
    margin-bottom: 10px;

    &.active {
      border-color: #f09b7f;
      background: #fff5f0;
    }

    &:last-child {
      margin-bottom: 0;
    }

    .platform-left {
      display: flex;
      align-items: center;

      .platform-logo {
        width: 40px;
        height: 40px;
        border-radius: 6px;
        margin-right: 12px;
      }

      .platform-info {
        .platform-name {
          display: block;
          font-size: 16px;
          font-weight: bold;
          color: #333;
          margin-bottom: 3px;
        }

        .platform-desc {
          display: block;
          font-size: 12px;
          color: #666;
        }
      }
    }
  }
}

.config-form {
  .form-group {
    margin-bottom: 20px;

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

  .advanced-toggle {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 15px;
    margin-top: 20px;
    border-top: 1px solid #f0f0f0;
    color: #666;

    text {
      margin-right: 5px;
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

.help-list {
  .help-item {
    display: flex;
    align-items: center;
    padding: 15px 0;
    border-bottom: 1px solid #f0f0f0;

    &:last-child {
      border-bottom: none;
    }

    text {
      flex: 1;
      margin-left: 12px;
      font-size: 14px;
      color: #333;
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