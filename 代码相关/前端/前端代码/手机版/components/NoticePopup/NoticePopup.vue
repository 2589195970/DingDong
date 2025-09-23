<template>
  <view v-if="visible" class="notice-popup-overlay" @click="handleOverlayClick">
    <view class="notice-popup-container" @click.stop>
      <!-- 关闭按钮 -->
      <view class="close-btn" @click="handleClose">
        关闭
      </view>

      <!-- 标题 -->
      <view class="popup-title">
        {{ noticeData.noticeTitle }}
      </view>

      <!-- 副标题 -->
      <view class="popup-subtitle" v-if="showSubtitle">
        小程序添加桌面
      </view>

      <!-- 内容描述 -->
      <view class="content-wrapper">
        <view class="popup-description">
          <rich-text :nodes="processedContent"></rich-text>
        </view>
      </view>

      <!-- 时间戳 -->
      <view class="timestamp">
        {{ formatTime }}
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: 'NoticePopup',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    noticeData: {
      type: Object,
      default: () => ({})
    }
  },
  computed: {
    qrCodeUrl() {
      // 从通知内容中提取二维码URL，或使用默认二维码
      return this.noticeData.qrCodeUrl || ''
    },
    showSubtitle() {
      // 只在没有具体通知标题时显示默认副标题
      return !this.noticeData.noticeTitle
    },
    processedContent() {
      // 处理HTML内容，如果没有内容则显示默认文本
      const content = this.noticeData.noticeContent || '按住二维码关注公众号，发送"号卡分销"关键词，产品上下架实时通知您！';

      // 如果内容包含HTML标签，直接返回用于rich-text组件
      if (content.includes('<') && content.includes('>')) {
        return content;
      }

      // 如果是纯文本，也返回原文本
      return content;
    },
    formatTime() {
      if (!this.noticeData.createTime) {
        return new Date().toLocaleString('zh-CN', {
          year: 'numeric',
          month: '2-digit',
          day: '2-digit',
          hour: '2-digit',
          minute: '2-digit',
          second: '2-digit'
        }).replace(/\//g, '-')
      }
      return this.noticeData.createTime
    }
  },
  methods: {
    handleClose() {
      this.$emit('close')
    },
    handleOverlayClick() {
      // 点击遮罩层关闭弹窗
      this.handleClose()
    }
  }
}
</script>

<style lang="scss" scoped>
.notice-popup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 9999;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgba(0, 0, 0, 0.5);
}

.notice-popup-container {
  position: relative;
  width: 650rpx;
  max-height: 80vh;
  background: #ffffff;
  border-radius: 20rpx;
  padding: 50rpx 40rpx;
  margin: 0 40rpx;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  box-shadow: 0 10rpx 30rpx rgba(0, 0, 0, 0.2);
  border: 1rpx solid #e8e8e8;

  .close-btn {
    position: absolute;
    top: 20rpx;
    right: 30rpx;
    color: #666666;
    font-size: 28rpx;
    padding: 10rpx;
    z-index: 10;
  }

  .popup-title {
    color: #333333;
    font-size: 40rpx;
    font-weight: bold;
    text-align: center;
    margin-bottom: 25rpx;
  }

  .popup-subtitle {
    color: #333333;
    font-size: 32rpx;
    font-weight: 600;
    text-align: center;
    margin-bottom: 35rpx;
  }

  .content-wrapper {
    overflow: hidden;
    margin-bottom: 30rpx;
    width: 100%;
    max-width: 100%;
    box-sizing: border-box;
  }

  .popup-description {
    color: #333333;
    font-size: 28rpx;
    line-height: 45rpx;
    text-align: left;
    opacity: 1;
    max-height: 50vh;
    overflow-y: auto;
    overflow-x: hidden;
    padding: 0 15rpx;
    margin-bottom: 0;
    width: 100%;
    box-sizing: border-box;

    // rich-text组件内的样式
    :deep(rich-text) {
      color: #333333 !important;
      word-wrap: break-word;
      word-break: break-word;
      overflow-wrap: break-word;
      max-width: 100%;
      width: 100%;
      display: block;

      // HTML标签样式重置
      p {
        margin: 0 0 15rpx 0;
        color: #333333;
        line-height: 40rpx;
        max-width: 100%;
        word-wrap: break-word;
        word-break: break-word;
        overflow-wrap: break-word;
      }

      div {
        color: #333333;
        margin-bottom: 10rpx;
        line-height: 40rpx;
        max-width: 100%;
        word-wrap: break-word;
        word-break: break-word;
        overflow-wrap: break-word;
      }

      span {
        color: #333333;
        word-wrap: break-word;
        word-break: break-word;
        overflow-wrap: break-word;
      }

      // 链接样式
      a {
        color: #1890ff;
        text-decoration: underline;
        word-wrap: break-word;
        word-break: break-word;
        overflow-wrap: break-word;
      }

      // 强调样式
      strong, b {
        font-weight: bold;
        color: #333333;
        word-wrap: break-word;
        word-break: break-word;
        overflow-wrap: break-word;
      }

      // 斜体样式
      em, i {
        font-style: italic;
        color: #333333;
        word-wrap: break-word;
        word-break: break-word;
        overflow-wrap: break-word;
      }

      // 列表样式
      ul, ol {
        padding-left: 30rpx;
        color: #333333;
        max-width: 100%;
      }

      li {
        margin-bottom: 8rpx;
        color: #333333;
        line-height: 40rpx;
        word-wrap: break-word;
        word-break: break-word;
        overflow-wrap: break-word;
        max-width: 100%;
      }

      // 标题样式
      h1, h2, h3, h4, h5, h6 {
        color: #333333;
        margin: 20rpx 0 15rpx 0;
        font-weight: bold;
        max-width: 100%;
        word-wrap: break-word;
        word-break: break-word;
        overflow-wrap: break-word;
      }

      // 通用元素宽度控制
      * {
        max-width: 100%;
        box-sizing: border-box;
      }

      // 图片控制
      img {
        max-width: 100%;
        height: auto;
        display: block;
      }

      // 表格控制
      table {
        width: 100%;
        table-layout: fixed;
        word-wrap: break-word;
      }

      td, th {
        word-wrap: break-word;
        word-break: break-word;
        overflow-wrap: break-word;
      }
    }
  }

  .qr-code-section {
    background-color: rgba(255, 255, 255, 0.95);
    border-radius: 20rpx;
    padding: 40rpx 30rpx;
    margin-bottom: 30rpx;

    .qr-code-container {
      display: flex;
      justify-content: center;
      margin-bottom: 40rpx;

      .qr-code-image {
        width: 300rpx;
        height: 300rpx;
        border-radius: 10rpx;
        border: 4rpx solid #ff4444;
      }

      .qr-code-placeholder {
        width: 300rpx;
        height: 300rpx;
        border-radius: 10rpx;
        border: 4rpx solid #ff4444;
        display: flex;
        align-items: center;
        justify-content: center;
        background-color: #fff;
        padding: 20rpx;

        .qr-code-pattern {
          width: 100%;
          height: 100%;
          display: flex;
          flex-direction: column;

          .qr-row {
            flex: 1;
            display: flex;

            .qr-dot {
              flex: 1;
              margin: 1rpx;
              background-color: #f0f0f0;

              &.active {
                background-color: #333;
              }
            }
          }
        }
      }
    }

    .step-indicator {
      display: flex;
      justify-content: space-around;
      align-items: center;

      .step-item {
        display: flex;
        flex-direction: column;
        align-items: center;

        .step-number {
          width: 40rpx;
          height: 40rpx;
          background-color: #ff4444;
          color: #fff;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 24rpx;
          font-weight: bold;
          margin-bottom: 10rpx;
        }

        .step-text {
          color: #333;
          font-size: 24rpx;
          text-align: center;
        }
      }
    }
  }

  .timestamp {
    color: #666666;
    font-size: 26rpx;
    text-align: center;
    margin-top: 10rpx;
  }
}
</style>