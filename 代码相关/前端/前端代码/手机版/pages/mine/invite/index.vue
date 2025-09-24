<template>
  <view class="page-container">
    <app-navbar title="推广邀请"></app-navbar>

    <!-- 推广统计 -->
    <view class="stats-section">
      <view class="stats-header">
        <text class="stats-title">我的推广数据</text>
        <text class="stats-subtitle">邀请好友，共享收益</text>
      </view>

      <view class="stats-grid">
        <view class="stats-item">
          <text class="stats-value">{{ inviteStats.totalInvites }}</text>
          <text class="stats-label">累计邀请</text>
        </view>
        <view class="stats-item">
          <text class="stats-value active">{{ inviteStats.activeInvites }}</text>
          <text class="stats-label">成功注册</text>
        </view>
        <view class="stats-item">
          <text class="stats-value commission">¥{{ inviteStats.totalCommission }}</text>
          <text class="stats-label">推广收益</text>
        </view>
        <view class="stats-item">
          <text class="stats-value today">{{ inviteStats.todayInvites }}</text>
          <text class="stats-label">今日邀请</text>
        </view>
      </view>
    </view>

    <!-- 邀请码信息 -->
    <view class="invite-code-section">
      <view class="section-header">
        <u-icon name="gift" color="#f09b7f"></u-icon>
        <text class="section-title">我的邀请码</text>
        <u-button size="mini" type="primary" plain @click="refreshInviteCode">刷新</u-button>
      </view>

      <view class="invite-code-card">
        <view class="code-display">
          <text class="code-label">邀请码</text>
          <view class="code-value-row">
            <text class="code-value">{{ inviteInfo.inviteCode }}</text>
            <u-button size="mini" type="success" @click="copyInviteCode">复制</u-button>
          </view>
        </view>

        <view class="code-display">
          <text class="code-label">邀请链接</text>
          <view class="code-value-row">
            <text class="code-value link">{{ inviteInfo.inviteLink }}</text>
            <u-button size="mini" type="primary" @click="copyInviteLink">复制</u-button>
          </view>
        </view>

        <view class="code-stats">
          <text class="stats-text">有效期: {{ inviteInfo.expireTime || '永久' }}</text>
          <text class="stats-text">使用次数: {{ inviteInfo.usedCount || 0 }}/{{ inviteInfo.maxUseCount || '无限制' }}</text>
        </view>
      </view>
    </view>

    <!-- 推广工具 -->
    <view class="tools-section">
      <view class="section-header">
        <u-icon name="share" color="#f09b7f"></u-icon>
        <text class="section-title">推广工具</text>
      </view>

      <view class="tools-grid">
        <!-- 海报分享 -->
        <view class="tool-item" @click="generatePoster">
          <view class="tool-icon">
            <u-icon name="camera" color="#4cd964" size="32"></u-icon>
          </view>
          <text class="tool-title">推广海报</text>
          <text class="tool-desc">生成专属海报</text>
        </view>

        <!-- 微信分享 -->
        <view class="tool-item" @click="shareToWechat">
          <view class="tool-icon">
            <u-icon name="weixin" color="#07c160" size="32"></u-icon>
          </view>
          <text class="tool-title">微信分享</text>
          <text class="tool-desc">分享给好友</text>
        </view>

        <!-- 朋友圈分享 -->
        <view class="tool-item" @click="shareToMoments">
          <view class="tool-icon">
            <u-icon name="moments" color="#ff9500" size="32"></u-icon>
          </view>
          <text class="tool-title">朋友圈</text>
          <text class="tool-desc">分享到朋友圈</text>
        </view>

        <!-- 二维码 -->
        <view class="tool-item" @click="showQRCode">
          <view class="tool-icon">
            <u-icon name="scan" color="#007aff" size="32"></u-icon>
          </view>
          <text class="tool-title">二维码</text>
          <text class="tool-desc">扫码注册</text>
        </view>
      </view>
    </view>

    <!-- 推广规则 -->
    <view class="rules-section">
      <view class="section-header">
        <u-icon name="info-circle" color="#f09b7f"></u-icon>
        <text class="section-title">推广规则</text>
        <u-icon
          name="arrow-down"
          color="#c0c4cc"
          size="16"
          :style="{ transform: showRules ? 'rotate(180deg)' : 'rotate(0deg)' }"
          @click="showRules = !showRules"
        ></u-icon>
      </view>

      <view v-if="showRules" class="rules-content">
        <view class="rule-item">
          <u-icon name="checkmark-circle" color="#4cd964" size="16"></u-icon>
          <text>邀请好友注册成功，您可获得 {{ rules.registerReward }} 元奖励</text>
        </view>
        <view class="rule-item">
          <u-icon name="checkmark-circle" color="#4cd964" size="16"></u-icon>
          <text>好友首次购买，您可获得订单金额 {{ rules.firstOrderRate }}% 的佣金</text>
        </view>
        <view class="rule-item">
          <u-icon name="checkmark-circle" color="#4cd964" size="16"></u-icon>
          <text>好友后续购买，您可获得订单金额 {{ rules.normalOrderRate }}% 的佣金</text>
        </view>
        <view class="rule-item">
          <u-icon name="checkmark-circle" color="#4cd964" size="16"></u-icon>
          <text>推广奖励实时到账，可在佣金明细中查看</text>
        </view>
        <view class="rule-item">
          <u-icon name="checkmark-circle" color="#4cd964" size="16"></u-icon>
          <text>邀请码永久有效，好友关系长期绑定</text>
        </view>
      </view>
    </view>

    <!-- 邀请记录 -->
    <view class="records-section">
      <view class="section-header">
        <u-icon name="list" color="#f09b7f"></u-icon>
        <text class="section-title">邀请记录</text>
        <u-button size="mini" type="info" plain @click="viewAllRecords">查看全部</u-button>
      </view>

      <view v-if="inviteRecords.length > 0" class="records-list">
        <view
          v-for="record in inviteRecords.slice(0, 5)"
          :key="record.id"
          class="record-item"
        >
          <view class="record-left">
            <u-avatar
              :text="record.userName ? record.userName.charAt(0) : 'U'"
              bg-color="#f09b7f"
              color="#fff"
              size="32"
            ></u-avatar>
            <view class="record-info">
              <text class="user-name">{{ record.userName || '新用户' }}</text>
              <text class="register-time">{{ formatTime(record.registerTime) }}</text>
            </view>
          </view>

          <view class="record-right">
            <view class="status-info">
              <u-tag
                :text="getStatusText(record.status)"
                :type="getStatusType(record.status)"
                size="mini"
              ></u-tag>
              <text class="reward-amount" v-if="record.rewardAmount > 0">
                +¥{{ (record.rewardAmount / 100).toFixed(2) }}
              </text>
            </view>
          </view>
        </view>
      </view>

      <view v-else class="empty-records">
        <u-empty
          text="暂无邀请记录"
          mode="data"
          :show="true"
        ></u-empty>
      </view>
    </view>

    <!-- 二维码弹窗 -->
    <u-popup v-model="showQRPopup" mode="center" border-radius="10">
      <view class="qr-popup">
        <view class="qr-header">
          <text class="qr-title">邀请二维码</text>
          <u-icon name="close" @click="showQRPopup = false"></u-icon>
        </view>

        <view class="qr-content">
          <canvas
            canvas-id="qrCanvas"
            class="qr-canvas"
            :style="{ width: qrSize + 'px', height: qrSize + 'px' }"
          ></canvas>
          <text class="qr-desc">扫描二维码，立即注册</text>
          <view class="qr-actions">
            <u-button size="small" type="primary" @click="saveQRCode">保存图片</u-button>
            <u-button size="small" type="success" plain @click="shareQRCode">分享</u-button>
          </view>
        </view>
      </view>
    </u-popup>

    <!-- 海报生成弹窗 -->
    <u-popup v-model="showPosterPopup" mode="bottom" border-radius="20">
      <view class="poster-popup">
        <view class="poster-header">
          <text class="poster-title">选择海报模板</text>
          <u-icon name="close" @click="showPosterPopup = false"></u-icon>
        </view>

        <view class="poster-templates">
          <view
            v-for="(template, index) in posterTemplates"
            :key="index"
            class="template-item"
            @click="selectTemplate(template)"
          >
            <image :src="template.thumbnail" class="template-image" mode="aspectFit"></image>
            <text class="template-name">{{ template.name }}</text>
          </view>
        </view>
      </view>
    </u-popup>
  </view>
</template>

<script>
import {
  getInviteInfo,
  generateInviteCode,
  getInviteStats,
  getInviteRecords,
  getInviteRules
} from '@/api/mine/invite.js';

export default {
  name: 'InvitePromo',
  data() {
    return {
      // 邀请统计
      inviteStats: {
        totalInvites: 0,
        activeInvites: 0,
        totalCommission: '0.00',
        todayInvites: 0
      },

      // 邀请信息
      inviteInfo: {
        inviteCode: '',
        inviteLink: '',
        expireTime: '',
        usedCount: 0,
        maxUseCount: 0
      },

      // 推广规则
      rules: {
        registerReward: 5,
        firstOrderRate: 10,
        normalOrderRate: 5
      },
      showRules: false,

      // 邀请记录
      inviteRecords: [],

      // 二维码
      showQRPopup: false,
      qrSize: 200,

      // 海报
      showPosterPopup: false,
      posterTemplates: [
        {
          id: 1,
          name: '经典模板',
          thumbnail: '@/static/images/poster/template1.jpg'
        },
        {
          id: 2,
          name: '简约模板',
          thumbnail: '@/static/images/poster/template2.jpg'
        },
        {
          id: 3,
          name: '炫彩模板',
          thumbnail: '@/static/images/poster/template3.jpg'
        }
      ]
    };
  },

  onLoad(options) {
    this.loadInviteData();

    // 处理从其他页面传递的参数
    if (options.action) {
      this.handleAction(options.action);
    }
  },

  methods: {
    // 加载邀请相关数据
    async loadInviteData() {
      await Promise.all([
        this.loadInviteInfo(),
        this.loadInviteStats(),
        this.loadInviteRecords(),
        this.loadInviteRules()
      ]);
    },

    // 加载邀请信息
    async loadInviteInfo() {
      try {
        const response = await getInviteInfo();
        this.inviteInfo = { ...this.inviteInfo, ...response.data };
      } catch (error) {
        console.error('加载邀请信息失败:', error);
      }
    },

    // 加载统计数据
    async loadInviteStats() {
      try {
        const response = await getInviteStats();
        this.inviteStats = { ...this.inviteStats, ...response.data };
      } catch (error) {
        console.error('加载统计数据失败:', error);
      }
    },

    // 加载邀请记录
    async loadInviteRecords() {
      try {
        const response = await getInviteRecords({ pageSize: 10 });
        this.inviteRecords = response.data || [];
      } catch (error) {
        console.error('加载邀请记录失败:', error);
      }
    },

    // 加载推广规则
    async loadInviteRules() {
      try {
        const response = await getInviteRules();
        this.rules = { ...this.rules, ...response.data };
      } catch (error) {
        console.error('加载推广规则失败:', error);
      }
    },

    // 刷新邀请码
    async refreshInviteCode() {
      try {
        const response = await generateInviteCode();
        this.inviteInfo = { ...this.inviteInfo, ...response.data };
        this.$u.toast('邀请码已刷新');
      } catch (error) {
        this.$u.toast('刷新失败，请重试');
        console.error('刷新邀请码失败:', error);
      }
    },

    // 复制邀请码
    copyInviteCode() {
      uni.setClipboardData({
        data: this.inviteInfo.inviteCode,
        success: () => {
          this.$u.toast('邀请码已复制');
        }
      });
    },

    // 复制邀请链接
    copyInviteLink() {
      uni.setClipboardData({
        data: this.inviteInfo.inviteLink,
        success: () => {
          this.$u.toast('邀请链接已复制');
        }
      });
    },

    // 生成海报
    generatePoster() {
      this.showPosterPopup = true;
    },

    // 选择海报模板
    selectTemplate(template) {
      this.showPosterPopup = false;
      // 这里可以调用海报生成API
      this.$u.toast(`正在生成${template.name}...`);
      // 模拟生成过程
      setTimeout(() => {
        this.$u.toast('海报生成成功！');
        // 可以跳转到海报预览页面
      }, 2000);
    },

    // 微信分享
    shareToWechat() {
      // #ifdef MP-WEIXIN
      wx.shareAppMessage({
        title: '叮咚号卡邀请您注册',
        desc: '注册即送话费，更有丰厚奖励等您拿！',
        path: `/pages/register?invite=${this.inviteInfo.inviteCode}`,
        success: () => {
          this.$u.toast('分享成功');
        }
      });
      // #endif

      // #ifndef MP-WEIXIN
      this.$u.toast('请在微信中使用分享功能');
      // #endif
    },

    // 朋友圈分享
    shareToMoments() {
      // #ifdef MP-WEIXIN
      wx.shareTimeline({
        title: `叮咚号卡邀请码：${this.inviteInfo.inviteCode}，注册即送话费！`,
        success: () => {
          this.$u.toast('分享成功');
        }
      });
      // #endif

      // #ifndef MP-WEIXIN
      this.$u.toast('请在微信中使用分享功能');
      // #endif
    },

    // 显示二维码
    showQRCode() {
      this.showQRPopup = true;
      this.$nextTick(() => {
        this.generateQRCode();
      });
    },

    // 生成二维码
    generateQRCode() {
      // 这里可以使用第三方库生成二维码
      // 或者调用后端API生成二维码图片
      const ctx = uni.createCanvasContext('qrCanvas', this);

      // 模拟二维码绘制
      ctx.setFillStyle('#000000');
      for (let i = 0; i < 20; i++) {
        for (let j = 0; j < 20; j++) {
          if (Math.random() > 0.5) {
            ctx.fillRect(i * 10, j * 10, 10, 10);
          }
        }
      }
      ctx.draw();
    },

    // 保存二维码
    saveQRCode() {
      uni.canvasToTempFilePath({
        canvasId: 'qrCanvas',
        success: (res) => {
          uni.saveImageToPhotosAlbum({
            filePath: res.tempFilePath,
            success: () => {
              this.$u.toast('二维码已保存到相册');
            },
            fail: () => {
              this.$u.toast('保存失败');
            }
          });
        }
      }, this);
    },

    // 分享二维码
    shareQRCode() {
      // 实现二维码分享逻辑
      this.$u.toast('分享功能开发中...');
    },

    // 查看全部记录
    viewAllRecords() {
      uni.navigateTo({
        url: '/pages/mine/invite/records'
      });
    },

    // 处理页面操作
    handleAction(action) {
      switch (action) {
        case 'generate':
          this.refreshInviteCode();
          break;
        case 'share':
          this.shareToWechat();
          break;
        case 'stats':
          // 默认显示统计信息
          break;
      }
    },

    // 获取状态文本
    getStatusText(status) {
      const statusMap = {
        0: '已注册',
        1: '已购买',
        2: '待激活'
      };
      return statusMap[status] || '未知';
    },

    // 获取状态类型
    getStatusType(status) {
      const typeMap = {
        0: 'success',
        1: 'primary',
        2: 'warning'
      };
      return typeMap[status] || 'info';
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
}

.stats-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px 15px;
  margin-bottom: 10px;
  color: white;

  .stats-header {
    text-align: center;
    margin-bottom: 20px;

    .stats-title {
      display: block;
      font-size: 18px;
      font-weight: bold;
      margin-bottom: 5px;
    }

    .stats-subtitle {
      display: block;
      font-size: 14px;
      opacity: 0.8;
    }
  }

  .stats-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 15px;

    .stats-item {
      text-align: center;

      .stats-value {
        display: block;
        font-size: 20px;
        font-weight: bold;
        margin-bottom: 5px;

        &.active {
          color: #4cd964;
        }

        &.commission {
          color: #ffd700;
        }

        &.today {
          color: #ff6b6b;
        }
      }

      .stats-label {
        display: block;
        font-size: 12px;
        opacity: 0.8;
      }
    }
  }
}

.invite-code-section, .tools-section, .rules-section, .records-section {
  background: white;
  margin-bottom: 10px;
  padding: 15px;

  .section-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 15px;

    .section-title {
      flex: 1;
      margin-left: 8px;
      font-size: 16px;
      font-weight: bold;
      color: #333;
    }
  }
}

.invite-code-card {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 15px;

  .code-display {
    margin-bottom: 15px;

    &:last-child {
      margin-bottom: 0;
    }

    .code-label {
      display: block;
      font-size: 14px;
      color: #666;
      margin-bottom: 8px;
    }

    .code-value-row {
      display: flex;
      align-items: center;
      justify-content: space-between;

      .code-value {
        flex: 1;
        font-size: 16px;
        font-weight: bold;
        color: #333;
        margin-right: 10px;

        &.link {
          font-size: 12px;
          word-break: break-all;
        }
      }
    }
  }

  .code-stats {
    display: flex;
    justify-content: space-between;
    margin-top: 15px;
    padding-top: 15px;
    border-top: 1px solid #e9ecef;

    .stats-text {
      font-size: 12px;
      color: #666;
    }
  }
}

.tools-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 15px;

  .tool-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 15px;
    border-radius: 8px;
    background: #f8f9fa;

    .tool-icon {
      margin-bottom: 8px;
    }

    .tool-title {
      font-size: 14px;
      font-weight: bold;
      color: #333;
      margin-bottom: 3px;
    }

    .tool-desc {
      font-size: 12px;
      color: #666;
      text-align: center;
    }
  }
}

.rules-content {
  .rule-item {
    display: flex;
    align-items: flex-start;
    margin-bottom: 10px;

    text {
      margin-left: 8px;
      font-size: 14px;
      color: #666;
      line-height: 1.5;
    }
  }
}

.records-list {
  .record-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 12px 0;
    border-bottom: 1px solid #f0f0f0;

    &:last-child {
      border-bottom: none;
    }

    .record-left {
      display: flex;
      align-items: center;

      .record-info {
        margin-left: 10px;

        .user-name {
          display: block;
          font-size: 14px;
          color: #333;
          margin-bottom: 3px;
        }

        .register-time {
          display: block;
          font-size: 12px;
          color: #999;
        }
      }
    }

    .record-right {
      .status-info {
        text-align: right;

        .reward-amount {
          display: block;
          font-size: 12px;
          color: #4cd964;
          margin-top: 3px;
        }
      }
    }
  }
}

.empty-records {
  padding: 30px 0;
  text-align: center;
}

// 弹窗样式
.qr-popup, .poster-popup {
  padding: 20px;

  .qr-header, .poster-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    .qr-title, .poster-title {
      font-size: 18px;
      font-weight: bold;
    }
  }

  .qr-content {
    text-align: center;

    .qr-canvas {
      border: 1px solid #e9ecef;
      border-radius: 8px;
      margin-bottom: 15px;
    }

    .qr-desc {
      display: block;
      font-size: 14px;
      color: #666;
      margin-bottom: 20px;
    }

    .qr-actions {
      display: flex;
      gap: 15px;
      justify-content: center;
    }
  }

  .poster-templates {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 15px;

    .template-item {
      text-align: center;

      .template-image {
        width: 80px;
        height: 120px;
        border-radius: 6px;
        margin-bottom: 8px;
      }

      .template-name {
        display: block;
        font-size: 12px;
        color: #666;
      }
    }
  }
}
</style>