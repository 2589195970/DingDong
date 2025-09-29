<template>
  <view class="page-container">
    <app-navbar title="通知详情"></app-navbar>

    <view class="detail-container">
      <!-- 加载状态 -->
      <view v-if="isLoading" class="loading-container">
        <u-loading-icon mode="circle" color="#f09b7f"></u-loading-icon>
        <text class="loading-text">加载中...</text>
      </view>

      <!-- 通知详情内容 -->
      <view v-else-if="noticeData" class="notice-detail">
        <!-- 通知头部 -->
        <view class="notice-header">
          <view class="notice-title">{{ noticeData.noticeTitle }}</view>
          <view class="notice-meta">
            <view class="notice-time">
              <u-icon name="clock" size="14" color="#999"></u-icon>
              <text>{{ formatTime(noticeData.createTime) }}</text>
            </view>
            <view class="notice-status" :class="getStatusClass(noticeData.status)">
              {{ getStatusText(noticeData.status) }}
            </view>
          </view>
        </view>

        <!-- 分割线 -->
        <view class="divider"></view>

        <!-- 通知内容 -->
        <view class="notice-content">
          <rich-text :nodes="processedContent"></rich-text>
        </view>

        <!-- 通知类型和发布信息 -->
        <view class="notice-info" v-if="noticeData.noticeType">
          <view class="info-item">
            <text class="info-label">通知类型：</text>
            <text class="info-value">{{ getNoticeTypeText(noticeData.noticeType) }}</text>
          </view>
          <view class="info-item" v-if="noticeData.createBy">
            <text class="info-label">发布人：</text>
            <text class="info-value">{{ noticeData.createBy }}</text>
          </view>
        </view>
      </view>

      <!-- 错误状态 -->
      <view v-else class="error-container">
        <u-empty
            text="通知内容加载失败"
            mode="error"
            :show="true"
        >
          <template #bottom>
            <u-button
                text="重新加载"
                type="primary"
                color="#f09b7f"
                @click="loadNoticeDetail"
            ></u-button>
          </template>
        </u-empty>
      </view>
    </view>
  </view>
</template>

<script>
import { getNotice } from '@/api/system/notice.js';

export default {
  name: 'NoticeDetail',
  data() {
    return {
      noticeId: '',
      noticeData: null,
      isLoading: true
    };
  },
  computed: {
    processedContent() {
      if (!this.noticeData || !this.noticeData.noticeContent) {
        return '暂无内容';
      }

      const content = this.noticeData.noticeContent;

      // 如果内容包含HTML标签，直接返回用于rich-text组件
      if (content.includes('<') && content.includes('>')) {
        return content;
      }

      // 如果是纯文本，保持换行格式
      return content.replace(/\n/g, '<br/>');
    }
  },
  onLoad(options) {
    if (options.noticeId) {
      this.noticeId = options.noticeId;
      this.loadNoticeDetail();
    } else {
      uni.showToast({
        title: '参数错误',
        icon: 'none'
      });
      setTimeout(() => {
        uni.navigateBack();
      }, 1500);
    }
  },
  methods: {
    // 加载通知详情
    async loadNoticeDetail() {
      if (!this.noticeId) return;

      this.isLoading = true;

      try {
        const res = await getNotice(this.noticeId);

        if (res.code === 200 && res.data) {
          this.noticeData = res.data;
          // 动态设置页面标题
          if (this.noticeData.noticeTitle) {
            uni.setNavigationBarTitle({
              title: this.noticeData.noticeTitle.length > 10
                  ? this.noticeData.noticeTitle.substring(0, 10) + '...'
                  : this.noticeData.noticeTitle
            });
          }
        } else {
          throw new Error(res.msg || '获取通知详情失败');
        }
      } catch (error) {
        console.error('加载通知详情失败:', error);
        uni.showToast({
          title: error.message || '加载失败',
          icon: 'none'
        });
        this.noticeData = null;
      } finally {
        this.isLoading = false;
      }
    },

    // 格式化时间
    formatTime(timeStr) {
      if (!timeStr) return '';

      const time = new Date(timeStr);

      return time.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      });
    },

    // 获取状态文本
    getStatusText(status) {
      switch (status) {
        case '0':
          return '正常';
        case '1':
          return '关闭';
        default:
          return '正常';
      }
    },

    // 获取状态样式类
    getStatusClass(status) {
      switch (status) {
        case '0':
          return 'status-normal';
        case '1':
          return 'status-closed';
        default:
          return 'status-normal';
      }
    },

    // 获取通知类型文本
    getNoticeTypeText(type) {
      switch (type) {
        case '1':
          return '通知';
        case '2':
          return '公告';
        default:
          return '通知';
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.page-container {
  height: 100vh;
  background-color: #f5f6f7;
}

.detail-container {
  padding: 15px;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 20px;
}


.loading-text {
  margin-top: 15px;
  font-size: 14px;
  color: #999;
}

.notice-detail {
  background-color: #fff;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.notice-header {
  margin-bottom: 20px;
}

.notice-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  line-height: 1.4;
  margin-bottom: 15px;
}

.notice-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
}

.notice-time {
  display: flex;
  align-items: center;
  font-size: 13px;
  color: #999;

  text {
    margin-left: 5px;
  }
}

.notice-status {
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 12px;

  &.status-normal {
    background-color: #e8f5e8;
    color: #52c41a;
  }

  &.status-closed {
    background-color: #fff2e8;
    color: #fa8c16;
  }
}

.divider {
  height: 1px;
  background-color: #f0f0f0;
  margin: 20px 0;
}

.notice-content {
  font-size: 15px;
  color: #333;
  line-height: 1.8;
  word-wrap: break-word;
  word-break: break-word;

  // rich-text组件内的样式
  :deep(rich-text) {
    color: #333 !important;
    word-wrap: break-word;
    word-break: break-word;
    overflow-wrap: break-word;

    // HTML标签样式
    p {
      margin: 0 0 15px 0;
      color: #333;
      line-height: 1.8;
    }

    div {
      color: #333;
      margin-bottom: 10px;
      line-height: 1.8;
    }

    span {
      color: #333;
    }

    // 链接样式
    a {
      color: #f09b7f;
      text-decoration: underline;
    }

    // 强调样式
    strong, b {
      font-weight: bold;
      color: #333;
    }

    // 斜体样式
    em, i {
      font-style: italic;
      color: #333;
    }

    // 列表样式
    ul, ol {
      padding-left: 20px;
      color: #333;
    }

    li {
      margin-bottom: 8px;
      color: #333;
      line-height: 1.8;
    }

    // 标题样式
    h1, h2, h3, h4, h5, h6 {
      color: #333;
      margin: 20px 0 15px 0;
      font-weight: bold;
    }

    h1 { font-size: 20px; }
    h2 { font-size: 18px; }
    h3 { font-size: 16px; }
    h4 { font-size: 15px; }
    h5 { font-size: 14px; }
    h6 { font-size: 13px; }

    // 图片控制
    img {
      max-width: 100%;
      height: auto;
      display: block;
      margin: 15px 0;
      border-radius: 6px;
    }

    // 代码块样式
    pre {
      background-color: #f5f5f5;
      padding: 10px;
      border-radius: 6px;
      overflow-x: auto;
      margin: 15px 0;
      font-family: 'Courier New', monospace;
    }

    code {
      background-color: #f5f5f5;
      padding: 2px 6px;
      border-radius: 3px;
      font-family: 'Courier New', monospace;
      font-size: 90%;
    }

    // 引用样式
    blockquote {
      border-left: 4px solid #f09b7f;
      padding-left: 15px;
      margin: 15px 0;
      color: #666;
      font-style: italic;
    }

    // 表格样式
    table {
      width: 100%;
      border-collapse: collapse;
      margin: 15px 0;
    }

    th, td {
      border: 1px solid #ddd;
      padding: 8px;
      text-align: left;
    }

    th {
      background-color: #f5f5f5;
      font-weight: bold;
    }
  }
}

.notice-info {
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
}

.info-item {
  display: flex;
  margin-bottom: 10px;
  font-size: 14px;
}

.info-label {
  color: #999;
  min-width: 80px;
}

.info-value {
  color: #333;
  flex: 1;
}

.error-container {
  padding: 60px 20px;
  text-align: center;
}

/* 适配不同屏幕尺寸 */
@media (max-width: 750rpx) {
  .detail-container {
    padding: 10px;
  }

  .notice-detail {
    padding: 15px;
  }

  .notice-title {
    font-size: 16px;
  }

  .notice-content {
    font-size: 14px;
  }
}
</style>