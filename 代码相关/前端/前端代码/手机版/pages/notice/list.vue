<template>
  <view class="page-container">
    <app-navbar title="系统通知"></app-navbar>

    <view class="notice-list-container">
      <!-- 下拉刷新 -->
      <scroll-view
          scroll-y
          class="scroll-view"
          :refresher-enabled="true"
          :refresher-triggered="isRefreshing"
          @refresherrefresh="onRefresh"
          @scrolltolower="onLoadMore"
      >
        <!-- 通知列表 -->
        <view v-if="noticeList.length > 0" class="notice-list">
          <view
              v-for="notice in noticeList"
              :key="notice.noticeId"
              class="notice-item"
              @click="goToDetail(notice.noticeId)"
          >
            <view class="notice-header">
              <view class="notice-title">{{ notice.noticeTitle }}</view>
              <view class="notice-time">{{ formatTime(notice.createTime) }}</view>
            </view>
            <view class="notice-content">
              {{ getNoticePreview(notice.noticeContent) }}
            </view>
            <view class="notice-footer">
              <view class="notice-status" :class="getStatusClass(notice.status)">
                {{ getStatusText(notice.status) }}
              </view>
            </view>
          </view>
        </view>

        <!-- 空状态 -->
        <view v-else-if="!isLoading" class="empty-state">
          <u-empty
              text="暂无通知"
              mode="data"
              :show="true"
          ></u-empty>
        </view>

        <!-- 加载更多提示 -->
        <view v-if="hasMore && noticeList.length > 0" class="load-more">
          <u-loadmore :status="loadStatus" />
        </view>
      </scroll-view>
    </view>
  </view>
</template>

<script>
import { listNotice } from '@/api/system/notice.js';

export default {
  name: 'NoticeList',
  data() {
    return {
      noticeList: [],
      isLoading: false,
      isRefreshing: false,
      hasMore: true,
      loadStatus: 'loadmore',
      pageNum: 1,
      pageSize: 10,
      total: 0
    };
  },
  onLoad() {
    this.loadNotices();
  },
  methods: {
    // 加载通知列表
    async loadNotices(refresh = false) {
      if (this.isLoading && !refresh) return;

      this.isLoading = true;
      if (refresh) {
        this.pageNum = 1;
        this.hasMore = true;
      }

      try {
        const res = await listNotice({
          pageNum: this.pageNum,
          pageSize: this.pageSize
        });

        if (res.code === 200) {
          const newData = res.rows || [];

          if (refresh) {
            this.noticeList = newData;
          } else {
            this.noticeList = [...this.noticeList, ...newData];
          }

          this.total = res.total || 0;
          this.hasMore = this.noticeList.length < this.total;

          if (this.hasMore) {
            this.pageNum++;
          }
        }
      } catch (error) {
        console.error('加载通知失败:', error);
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        });
      } finally {
        this.isLoading = false;
        this.isRefreshing = false;
        this.loadStatus = this.hasMore ? 'loadmore' : 'nomore';
      }
    },

    // 下拉刷新
    onRefresh() {
      this.isRefreshing = true;
      this.loadNotices(true);
    },

    // 上拉加载更多
    onLoadMore() {
      if (this.hasMore && !this.isLoading) {
        this.loadStatus = 'loading';
        this.loadNotices();
      }
    },

    // 跳转到详情页
    goToDetail(noticeId) {
      uni.navigateTo({
        url: `/pages/notice/detail?noticeId=${noticeId}`
      });
    },

    // 格式化时间
    formatTime(timeStr) {
      if (!timeStr) return '';

      const time = new Date(timeStr);
      const now = new Date();
      const diff = now - time;

      // 一分钟内
      if (diff < 60 * 1000) {
        return '刚刚';
      }

      // 一小时内
      if (diff < 60 * 60 * 1000) {
        return `${Math.floor(diff / (60 * 1000))}分钟前`;
      }

      // 一天内
      if (diff < 24 * 60 * 60 * 1000) {
        return `${Math.floor(diff / (60 * 60 * 1000))}小时前`;
      }

      // 超过一天，显示具体日期
      return time.toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
      });
    },

    // 获取通知预览内容
    getNoticePreview(content) {
      if (!content) return '暂无内容';

      // 移除HTML标签
      const text = content.replace(/<[^>]*>/g, '');
      // 截取前100个字符
      return text.length > 100 ? text.substring(0, 100) + '...' : text;
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
    }
  }
};
</script>

<style lang="scss" scoped>
.page-container {
  height: 100vh;
  background-color: #f5f6f7;
}

.notice-list-container {
  flex: 1;
  height: calc(100vh - 88px); /* 减去导航栏高度 */
}

.scroll-view {
  height: 100%;
}

.notice-list {
  padding: 0 15px;
}

.notice-item {
  background-color: #fff;
  border-radius: 10px;
  margin: 10px 0;
  padding: 15px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s;

  &:active {
    transform: scale(0.98);
  }
}

.notice-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 10px;
}

.notice-title {
  flex: 1;
  font-size: 16px;
  font-weight: bold;
  color: #333;
  line-height: 1.4;
  margin-right: 10px;
}

.notice-time {
  font-size: 12px;
  color: #999;
  white-space: nowrap;
}

.notice-content {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin-bottom: 10px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.notice-footer {
  display: flex;
  justify-content: flex-end;
}

.notice-status {
  font-size: 12px;
  padding: 2px 8px;
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

.empty-state {
  padding: 60px 20px;
  text-align: center;
}

.load-more {
  padding: 20px;
  text-align: center;
}

/* 适配不同屏幕尺寸 */
@media (max-width: 750rpx) {
  .notice-item {
    margin: 8px 0;
    padding: 12px;
  }

  .notice-title {
    font-size: 15px;
  }

  .notice-content {
    font-size: 13px;
  }
}
</style>