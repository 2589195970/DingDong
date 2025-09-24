<template>
  <view class="page-container">
    <app-navbar title="我的代理"></app-navbar>

    <!-- 统计概览 -->
    <view class="stats-section">
      <view class="stats-grid">
        <view class="stats-item">
          <text class="stats-value">{{ agentStats.totalCount }}</text>
          <text class="stats-label">总代理数</text>
        </view>
        <view class="stats-item">
          <text class="stats-value active">{{ agentStats.activeCount }}</text>
          <text class="stats-label">活跃代理</text>
        </view>
        <view class="stats-item">
          <text class="stats-value verified">{{ agentStats.verifiedCount }}</text>
          <text class="stats-label">已实名</text>
        </view>
        <view class="stats-item">
          <text class="stats-value commission">¥{{ agentStats.totalCommission }}</text>
          <text class="stats-label">总佣金</text>
        </view>
      </view>
    </view>

    <!-- 筛选工具栏 -->
    <view class="filter-section">
      <view class="filter-bar">
        <u-subsection
          :list="statusList"
          v-model="queryParams.isEnabled"
          @change="handleStatusChange"
          :activeColor="'#f09b7f'"
        ></u-subsection>

        <u-icon
          name="search"
          @click="showSearch = !showSearch"
          :color="showSearch ? '#f09b7f' : '#c0c4cc'"
          size="20"
        ></u-icon>
      </view>

      <!-- 搜索框 -->
      <view v-if="showSearch" class="search-bar">
        <u-search
          v-model="searchKeyword"
          placeholder="搜索代理商名称或手机号"
          @search="handleSearch"
          @clear="handleClear"
          :show-action="false"
          bg-color="#f5f5f5"
        ></u-search>
      </view>
    </view>

    <!-- 代理列表 -->
    <view class="agent-list-section">
      <scroll-view
        scroll-y
        class="scroll-view"
        :refresher-enabled="true"
        :refresher-triggered="isRefreshing"
        @refresherrefresh="onRefresh"
        @scrolltolower="onLoadMore"
      >
        <view v-if="agentList.length > 0" class="agent-list">
          <view
            v-for="agent in agentList"
            :key="agent.agentAccountId"
            class="agent-item"
            @click="showAgentDetail(agent)"
          >
            <!-- 代理基本信息 -->
            <view class="agent-header">
              <view class="agent-avatar">
                <u-avatar
                  :text="agent.agentName ? agent.agentName.charAt(0) : 'A'"
                  bg-color="#f09b7f"
                  color="#fff"
                  size="40"
                ></u-avatar>
                <!-- 等级标识 -->
                <view class="agent-level">
                  <u-tag
                    :text="`L${agent.level}`"
                    type="warning"
                    size="mini"
                  ></u-tag>
                </view>
              </view>

              <view class="agent-info">
                <view class="agent-name-row">
                  <text class="agent-name">{{ agent.agentName }}</text>
                  <!-- 状态标识 -->
                  <view class="status-badges">
                    <u-tag
                      :text="agent.isRealName ? '已实名' : '未实名'"
                      :type="agent.isRealName ? 'success' : 'error'"
                      size="mini"
                      plain
                    ></u-tag>
                    <u-tag
                      :text="agent.isEnabled ? '正常' : '禁用'"
                      :type="agent.isEnabled ? 'success' : 'info'"
                      size="mini"
                      plain
                    ></u-tag>
                  </view>
                </view>

                <view class="agent-details">
                  <text class="phone">{{ formatPhone(agent.phone) }}</text>
                  <text class="parent-agent" v-if="agent.parentAgentName">
                    上级: {{ agent.parentAgentName }}
                  </text>
                </view>

                <!-- 余额和佣金信息 -->
                <view class="financial-info">
                  <view class="balance-info">
                    <text class="balance-label">余额:</text>
                    <text class="balance-value">¥{{ (agent.balance * 0.01).toFixed(2) }}</text>
                  </view>
                  <view class="commission-info" v-if="agent.totalCommission">
                    <text class="commission-label">累计佣金:</text>
                    <text class="commission-value">¥{{ (agent.totalCommission * 0.01).toFixed(2) }}</text>
                  </view>
                </view>
              </view>

              <view class="agent-actions">
                <u-icon name="arrow-right" color="#c0c4cc" size="16"></u-icon>
              </view>
            </view>

            <!-- 注册时间和加密状态 -->
            <view class="agent-footer">
              <text class="register-time">注册时间: {{ formatTime(agent.createTime) }}</text>
              <view class="encrypt-status">
                <u-tag
                  :text="agent.isEncrypt ? '订单解密' : '订单加密'"
                  :type="agent.isEncrypt ? 'warning' : 'success'"
                  size="mini"
                  plain
                ></u-tag>
              </view>
            </view>
          </view>
        </view>

        <!-- 空状态 -->
        <view v-else-if="!isLoading" class="empty-state">
          <u-empty
            text="暂无代理数据"
            mode="data"
            :show="true"
          ></u-empty>
        </view>

        <!-- 加载更多提示 -->
        <view v-if="hasMore && agentList.length > 0" class="load-more">
          <u-loadmore :status="loadStatus" />
        </view>
      </scroll-view>
    </view>

    <!-- 代理详情弹窗 -->
    <u-popup v-model="showDetailPopup" mode="bottom" border-radius="20">
      <view class="detail-popup" v-if="selectedAgent">
        <view class="popup-header">
          <text class="popup-title">代理详情</text>
          <u-icon name="close" @click="showDetailPopup = false"></u-icon>
        </view>

        <view class="detail-content">
          <!-- 基本信息 -->
          <view class="detail-section">
            <text class="section-title">基本信息</text>
            <view class="detail-row">
              <text class="detail-label">代理名称</text>
              <text class="detail-value">{{ selectedAgent.agentName }}</text>
            </view>
            <view class="detail-row">
              <text class="detail-label">手机号</text>
              <text class="detail-value">{{ selectedAgent.phone }}</text>
            </view>
            <view class="detail-row">
              <text class="detail-label">代理等级</text>
              <text class="detail-value">L{{ selectedAgent.level }}</text>
            </view>
            <view class="detail-row">
              <text class="detail-label">上级代理</text>
              <text class="detail-value">{{ selectedAgent.parentAgentName || '无' }}</text>
            </view>
          </view>

          <!-- 财务信息 -->
          <view class="detail-section">
            <text class="section-title">财务信息</text>
            <view class="detail-row">
              <text class="detail-label">账户余额</text>
              <text class="detail-value balance">¥{{ (selectedAgent.balance * 0.01).toFixed(2) }}</text>
            </view>
            <view class="detail-row" v-if="selectedAgent.totalCommission">
              <text class="detail-label">累计佣金</text>
              <text class="detail-value commission">¥{{ (selectedAgent.totalCommission * 0.01).toFixed(2) }}</text>
            </view>
            <view class="detail-row" v-if="selectedAgent.totalOrders">
              <text class="detail-label">总订单数</text>
              <text class="detail-value">{{ selectedAgent.totalOrders }}</text>
            </view>
          </view>

          <!-- 状态信息 -->
          <view class="detail-section">
            <text class="section-title">状态信息</text>
            <view class="detail-row">
              <text class="detail-label">账户状态</text>
              <u-tag
                :text="selectedAgent.isEnabled ? '正常' : '禁用'"
                :type="selectedAgent.isEnabled ? 'success' : 'error'"
                size="mini"
              ></u-tag>
            </view>
            <view class="detail-row">
              <text class="detail-label">实名状态</text>
              <u-tag
                :text="selectedAgent.isRealName ? '已实名' : '未实名'"
                :type="selectedAgent.isRealName ? 'success' : 'warning'"
                size="mini"
              ></u-tag>
            </view>
            <view class="detail-row">
              <text class="detail-label">订单加密</text>
              <u-tag
                :text="selectedAgent.isEncrypt ? '解密' : '加密'"
                :type="selectedAgent.isEncrypt ? 'warning' : 'success'"
                size="mini"
              ></u-tag>
            </view>
            <view class="detail-row">
              <text class="detail-label">注册时间</text>
              <text class="detail-value">{{ formatTime(selectedAgent.createTime) }}</text>
            </view>
          </view>

          <!-- 操作按钮 -->
          <view class="detail-actions">
            <u-button
              type="primary"
              size="small"
              @click="viewCommissionDetail"
              plain
            >佣金明细</u-button>
            <u-button
              type="success"
              size="small"
              @click="contactAgent"
              plain
            >联系代理</u-button>
          </view>
        </view>
      </view>
    </u-popup>

    <!-- 悬浮操作按钮 -->
    <view class="fab-container">
      <u-icon
        name="plus"
        @click="showInviteActions"
        color="#fff"
        size="24"
        :custom-style="fabStyle"
      ></u-icon>
    </view>

    <!-- 邀请操作选择 -->
    <u-action-sheet
      :list="inviteActions"
      v-model="showInviteSheet"
      @click="handleInviteAction"
    ></u-action-sheet>
  </view>
</template>

<script>
import { selectChildAgentList, getAgentStats } from '@/api/mine/agent.js';

export default {
  name: 'MyAgents',
  data() {
    return {
      // 查询参数
      queryParams: {
        isEnabled: '',
        isRealName: '',
        agentName: '',
        pageNo: 1,
        pageSize: 10
      },

      // 筛选条件
      statusList: [
        { name: '全部' },
        { name: '正常' },
        { name: '禁用' }
      ],

      // 搜索
      showSearch: false,
      searchKeyword: '',

      // 列表数据
      agentList: [],
      isLoading: false,
      isRefreshing: false,
      hasMore: true,
      loadStatus: 'loadmore',
      total: 0,

      // 统计数据
      agentStats: {
        totalCount: 0,
        activeCount: 0,
        verifiedCount: 0,
        totalCommission: '0.00'
      },

      // 详情弹窗
      showDetailPopup: false,
      selectedAgent: null,

      // 邀请操作
      showInviteSheet: false,
      inviteActions: [
        {
          text: '生成邀请码',
          color: '#007aff'
        },
        {
          text: '分享邀请链接',
          color: '#09bb07'
        },
        {
          text: '查看推广统计',
          color: '#ff9500'
        }
      ],

      // 悬浮按钮样式
      fabStyle: {
        position: 'fixed',
        right: '20px',
        bottom: '100px',
        width: '50px',
        height: '50px',
        borderRadius: '25px',
        backgroundColor: '#f09b7f',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        boxShadow: '0 4px 12px rgba(240, 155, 127, 0.4)',
        zIndex: 999
      }
    };
  },

  onLoad() {
    this.loadAgentList();
    this.loadAgentStats();
  },

  methods: {
    // 加载代理列表
    async loadAgentList(refresh = false) {
      if (this.isLoading && !refresh) return;

      this.isLoading = true;
      if (refresh) {
        this.queryParams.pageNo = 1;
        this.agentList = [];
      }

      try {
        const response = await selectChildAgentList(this.queryParams);
        const agents = response.data || [];

        if (refresh) {
          this.agentList = agents;
        } else {
          this.agentList.push(...agents);
        }

        this.total = agents.length;
        this.hasMore = agents.length >= this.queryParams.pageSize;

      } catch (error) {
        this.$u.toast('加载失败，请重试');
        console.error('加载代理列表失败:', error);
      } finally {
        this.isLoading = false;
        this.isRefreshing = false;
        this.loadStatus = this.hasMore ? 'loadmore' : 'nomore';
      }
    },

    // 加载统计数据
    async loadAgentStats() {
      try {
        const response = await getAgentStats();
        this.agentStats = { ...this.agentStats, ...response.data };
      } catch (error) {
        console.error('加载统计数据失败:', error);
      }
    },

    // 下拉刷新
    onRefresh() {
      this.isRefreshing = true;
      this.loadAgentList(true);
      this.loadAgentStats();
    },

    // 上拉加载更多
    onLoadMore() {
      if (this.hasMore && !this.isLoading) {
        this.queryParams.pageNo++;
        this.loadAgentList();
      }
    },

    // 状态筛选
    handleStatusChange(index) {
      if (index === 0) {
        this.queryParams.isEnabled = '';
      } else {
        this.queryParams.isEnabled = index === 1 ? 0 : 1;
      }
      this.loadAgentList(true);
    },

    // 搜索
    handleSearch() {
      this.queryParams.agentName = this.searchKeyword;
      this.loadAgentList(true);
    },

    // 清除搜索
    handleClear() {
      this.searchKeyword = '';
      this.queryParams.agentName = '';
      this.loadAgentList(true);
    },

    // 显示代理详情
    showAgentDetail(agent) {
      this.selectedAgent = agent;
      this.showDetailPopup = true;
    },

    // 查看佣金明细
    viewCommissionDetail() {
      if (this.selectedAgent) {
        uni.navigateTo({
          url: `/pages/mine/commission-detail/index?agentId=${this.selectedAgent.agentAccountId}`
        });
        this.showDetailPopup = false;
      }
    },

    // 联系代理
    contactAgent() {
      if (this.selectedAgent && this.selectedAgent.phone) {
        uni.makePhoneCall({
          phoneNumber: this.selectedAgent.phone,
          fail: () => {
            this.$u.toast('拨打失败');
          }
        });
      }
    },

    // 显示邀请操作
    showInviteActions() {
      this.showInviteSheet = true;
    },

    // 处理邀请操作
    handleInviteAction(index) {
      const actions = [
        () => this.generateInviteCode(),
        () => this.shareInviteLink(),
        () => this.viewPromoStats()
      ];
      actions[index] && actions[index]();
    },

    // 生成邀请码
    generateInviteCode() {
      uni.navigateTo({
        url: '/pages/mine/invite/index?action=generate'
      });
    },

    // 分享邀请链接
    shareInviteLink() {
      uni.navigateTo({
        url: '/pages/mine/invite/index?action=share'
      });
    },

    // 查看推广统计
    viewPromoStats() {
      uni.navigateTo({
        url: '/pages/mine/invite/index?action=stats'
      });
    },

    // 格式化手机号
    formatPhone(phone) {
      if (!phone) return '';
      return phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2');
    },

    // 格式化时间
    formatTime(timestamp) {
      if (!timestamp) return '';
      const date = new Date(timestamp);
      const year = date.getFullYear();
      const month = (date.getMonth() + 1).toString().padStart(2, '0');
      const day = date.getDate().toString().padStart(2, '0');
      return `${year}-${month}-${day}`;
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
  background: white;
  padding: 20px 15px;
  margin-bottom: 10px;

  .stats-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 15px;

    .stats-item {
      text-align: center;

      .stats-value {
        display: block;
        font-size: 18px;
        font-weight: bold;
        color: #333;
        margin-bottom: 5px;

        &.active {
          color: #4cd964;
        }

        &.verified {
          color: #007aff;
        }

        &.commission {
          color: #ff9500;
        }
      }

      .stats-label {
        display: block;
        font-size: 12px;
        color: #666;
      }
    }
  }
}

.filter-section {
  background: white;
  padding: 15px;
  margin-bottom: 10px;

  .filter-bar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px;
  }

  .search-bar {
    margin-top: 10px;
  }
}

.agent-list-section {
  flex: 1;
  padding: 0 15px;

  .scroll-view {
    height: calc(100vh - 300px);
  }
}

.agent-list {
  .agent-item {
    background: white;
    border-radius: 8px;
    padding: 15px;
    margin-bottom: 10px;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);

    .agent-header {
      display: flex;
      align-items: flex-start;

      .agent-avatar {
        position: relative;
        margin-right: 12px;

        .agent-level {
          position: absolute;
          top: -5px;
          right: -5px;
        }
      }

      .agent-info {
        flex: 1;

        .agent-name-row {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 8px;

          .agent-name {
            font-size: 16px;
            font-weight: bold;
            color: #333;
          }

          .status-badges {
            display: flex;
            gap: 5px;
          }
        }

        .agent-details {
          margin-bottom: 8px;

          .phone {
            display: block;
            font-size: 14px;
            color: #666;
            margin-bottom: 3px;
          }

          .parent-agent {
            display: block;
            font-size: 12px;
            color: #999;
          }
        }

        .financial-info {
          display: flex;
          justify-content: space-between;

          .balance-info, .commission-info {
            .balance-label, .commission-label {
              font-size: 12px;
              color: #666;
            }

            .balance-value {
              font-size: 14px;
              font-weight: bold;
              color: #4cd964;
              margin-left: 5px;
            }

            .commission-value {
              font-size: 14px;
              font-weight: bold;
              color: #ff9500;
              margin-left: 5px;
            }
          }
        }
      }

      .agent-actions {
        padding-top: 10px;
      }
    }

    .agent-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: 12px;
      padding-top: 12px;
      border-top: 1px solid #f0f0f0;

      .register-time {
        font-size: 12px;
        color: #999;
      }
    }
  }
}

.empty-state {
  padding: 50px 0;
  text-align: center;
}

.load-more {
  padding: 20px 0;
}

// 详情弹窗样式
.detail-popup {
  padding: 20px;

  .popup-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    .popup-title {
      font-size: 18px;
      font-weight: bold;
    }
  }

  .detail-content {
    .detail-section {
      margin-bottom: 20px;

      .section-title {
        display: block;
        font-size: 14px;
        font-weight: bold;
        color: #333;
        margin-bottom: 10px;
        padding-bottom: 5px;
        border-bottom: 1px solid #f0f0f0;
      }

      .detail-row {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 8px 0;

        .detail-label {
          font-size: 14px;
          color: #666;
        }

        .detail-value {
          font-size: 14px;
          color: #333;

          &.balance {
            color: #4cd964;
            font-weight: bold;
          }

          &.commission {
            color: #ff9500;
            font-weight: bold;
          }
        }
      }
    }

    .detail-actions {
      display: flex;
      gap: 15px;
      margin-top: 20px;

      .u-button {
        flex: 1;
      }
    }
  }
}

.fab-container {
  position: fixed;
  right: 20px;
  bottom: 100px;
  z-index: 999;
}
</style>