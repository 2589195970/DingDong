<template>
  <view class="page-container">
    <app-navbar title="佣金明细"></app-navbar>

    <!-- 筛选条件 -->
    <view class="filter-section">
      <view class="filter-row">
        <u-subsection
          :list="typeList"
          v-model="queryParams.amountType"
          @change="handleTypeChange"
        ></u-subsection>
      </view>

      <view class="filter-row">
        <u-datetime-picker
          v-model="dateRange"
          mode="range"
          :formatter="formatter"
          @confirm="handleDateChange"
        >
          <u-button size="small" type="primary" plain>
            <u-icon name="calendar" size="14"></u-icon>
            选择时间范围
          </u-button>
        </u-datetime-picker>

        <u-button
          size="small"
          type="primary"
          @click="handleQuery"
          style="margin-left: 10px;"
        >
          <u-icon name="search" size="14"></u-icon>
          搜索
        </u-button>
      </view>
    </view>

    <!-- 统计卡片 -->
    <view class="stats-section">
      <view class="stats-card">
        <view class="stats-row">
          <view class="stats-item">
            <text class="stats-label">总收入</text>
            <text class="stats-value income">¥{{ totalIncome }}</text>
          </view>
          <view class="stats-item">
            <text class="stats-label">总支出</text>
            <text class="stats-value expense">¥{{ totalExpense }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 明细列表 -->
    <view class="list-section">
      <scroll-view
        scroll-y
        class="scroll-view"
        :refresher-enabled="true"
        :refresher-triggered="isRefreshing"
        @refresherrefresh="onRefresh"
        @scrolltolower="onLoadMore"
      >
        <view v-if="commissionList.length > 0" class="commission-list">
          <view
            v-for="item in commissionList"
            :key="item.withdrawalRecordId"
            class="commission-item"
            @click="showDetail(item)"
          >
            <view class="commission-header">
              <view class="commission-type">
                <u-tag
                  :text="getTypeText(item.withdrawalType)"
                  :type="getTypeStyle(item.withdrawalType)"
                  size="mini"
                ></u-tag>
                <text class="amount-type">{{ getAmountTypeText(item.amountType) }}</text>
              </view>
              <view class="commission-time">{{ formatTime(item.createTime) }}</view>
            </view>

            <view class="commission-content">
              <view class="amount-section">
                <text class="main-amount" :class="item.amountType == 0 ? 'income' : 'expense'">
                  {{ item.amountType == 0 ? '+' : '-' }}¥{{ (item.withdrawalAmount / 100).toFixed(2) }}
                </text>
                <text class="received-amount">
                  实际：¥{{ (item.receivedAmount / 100).toFixed(2) }}
                </text>
              </view>

              <view class="detail-info">
                <text class="order-no" v-if="item.sourceNumber">订单号：{{ item.sourceNumber }}</text>
                <text class="fee-info" v-if="item.withdrawalRateAmount > 0">
                  手续费：¥{{ (item.withdrawalRateAmount / 100).toFixed(2) }}
                </text>
              </view>
            </view>

            <view class="commission-footer">
              <u-tag
                :text="getPaymentTypeText(item.applyWithdrawalType)"
                type="info"
                size="mini"
                plain
              ></u-tag>
            </view>
          </view>
        </view>

        <!-- 空状态 -->
        <view v-else-if="!isLoading" class="empty-state">
          <u-empty
            text="暂无佣金记录"
            mode="data"
            :show="true"
          ></u-empty>
        </view>

        <!-- 加载更多提示 -->
        <view v-if="hasMore && commissionList.length > 0" class="load-more">
          <u-loadmore :status="loadStatus" />
        </view>
      </scroll-view>
    </view>

    <!-- 详情弹窗 -->
    <u-popup v-model="showDetailPopup" mode="bottom" border-radius="20">
      <view class="detail-popup" v-if="selectedItem">
        <view class="popup-header">
          <text class="popup-title">佣金详情</text>
          <u-icon name="close" @click="showDetailPopup = false"></u-icon>
        </view>

        <view class="detail-content">
          <view class="detail-row">
            <text class="detail-label">类型</text>
            <text class="detail-value">{{ getTypeText(selectedItem.withdrawalType) }}</text>
          </view>
          <view class="detail-row">
            <text class="detail-label">金额</text>
            <text class="detail-value" :class="selectedItem.amountType == 0 ? 'income' : 'expense'">
              {{ selectedItem.amountType == 0 ? '+' : '-' }}¥{{ (selectedItem.withdrawalAmount / 100).toFixed(2) }}
            </text>
          </view>
          <view class="detail-row">
            <text class="detail-label">实际金额</text>
            <text class="detail-value">¥{{ (selectedItem.receivedAmount / 100).toFixed(2) }}</text>
          </view>
          <view class="detail-row" v-if="selectedItem.withdrawalRate">
            <text class="detail-label">提现费率</text>
            <text class="detail-value">{{ selectedItem.withdrawalRate }}%</text>
          </view>
          <view class="detail-row" v-if="selectedItem.withdrawalRateAmount > 0">
            <text class="detail-label">手续费</text>
            <text class="detail-value">¥{{ (selectedItem.withdrawalRateAmount / 100).toFixed(2) }}</text>
          </view>
          <view class="detail-row">
            <text class="detail-label">支付方式</text>
            <text class="detail-value">{{ getPaymentTypeText(selectedItem.applyWithdrawalType) }}</text>
          </view>
          <view class="detail-row" v-if="selectedItem.sourceNumber">
            <text class="detail-label">订单号</text>
            <text class="detail-value">{{ selectedItem.sourceNumber }}</text>
          </view>
          <view class="detail-row">
            <text class="detail-label">时间</text>
            <text class="detail-value">{{ formatTime(selectedItem.createTime) }}</text>
          </view>
        </view>
      </view>
    </u-popup>
  </view>
</template>

<script>
import { selectAgentWithdrawalRecordListPage } from '@/api/mine/commission.js';

export default {
  name: 'CommissionDetail',
  data() {
    return {
      // 筛选条件
      queryParams: {
        withdrawalType: '',  // 操作类型
        amountType: '',      // 存入/提现类型
        sourceNumber: '',    // 订单号
        pageNo: 1,
        pageSize: 10
      },

      // 类型选择
      typeList: [
        { name: '全部' },
        { name: '存入' },
        { name: '提现' }
      ],

      // 时间范围
      dateRange: [],

      // 列表数据
      commissionList: [],
      isLoading: false,
      isRefreshing: false,
      hasMore: true,
      loadStatus: 'loadmore',
      total: 0,

      // 统计数据
      totalIncome: '0.00',
      totalExpense: '0.00',

      // 详情弹窗
      showDetailPopup: false,
      selectedItem: null
    };
  },

  onLoad() {
    this.loadCommissionList();
    this.calculateStats();
  },

  methods: {
    // 加载佣金列表
    async loadCommissionList(refresh = false) {
      if (this.isLoading && !refresh) return;

      this.isLoading = true;
      if (refresh) {
        this.queryParams.pageNo = 1;
        this.commissionList = [];
      }

      try {
        const response = await selectAgentWithdrawalRecordListPage(this.queryParams);
        const { rows, total } = response.data;

        if (refresh) {
          this.commissionList = rows;
        } else {
          this.commissionList.push(...rows);
        }

        this.total = total;
        this.hasMore = this.commissionList.length < total;

      } catch (error) {
        this.$u.toast('加载失败，请重试');
        console.error('加载佣金列表失败:', error);
      } finally {
        this.isLoading = false;
        this.isRefreshing = false;
        this.loadStatus = this.hasMore ? 'loadmore' : 'nomore';
      }
    },

    // 计算统计数据
    calculateStats() {
      let income = 0;
      let expense = 0;

      this.commissionList.forEach(item => {
        if (item.amountType == 0) { // 存入
          income += item.withdrawalAmount;
        } else { // 提现
          expense += item.withdrawalAmount;
        }
      });

      this.totalIncome = (income / 100).toFixed(2);
      this.totalExpense = (expense / 100).toFixed(2);
    },

    // 下拉刷新
    onRefresh() {
      this.isRefreshing = true;
      this.loadCommissionList(true);
    },

    // 上拉加载更多
    onLoadMore() {
      if (this.hasMore && !this.isLoading) {
        this.queryParams.pageNo++;
        this.loadCommissionList();
      }
    },

    // 搜索
    handleQuery() {
      this.loadCommissionList(true);
    },

    // 类型切换
    handleTypeChange(index) {
      if (index === 0) {
        this.queryParams.amountType = '';
      } else {
        this.queryParams.amountType = index - 1;
      }
      this.handleQuery();
    },

    // 时间范围选择
    handleDateChange(value) {
      this.dateRange = value;
      // 这里可以添加时间范围到查询参数
      this.handleQuery();
    },

    // 显示详情
    showDetail(item) {
      this.selectedItem = item;
      this.showDetailPopup = true;
    },

    // 获取类型文本
    getTypeText(type) {
      const typeMap = {
        0: '号卡佣金',
        1: '代理商提现',
        2: '管理员操作'
      };
      return typeMap[type] || '未知';
    },

    // 获取类型样式
    getTypeStyle(type) {
      const styleMap = {
        0: 'success',
        1: 'warning',
        2: 'info'
      };
      return styleMap[type] || 'default';
    },

    // 获取金额类型文本
    getAmountTypeText(type) {
      return type == 0 ? '存入' : '提现';
    },

    // 获取支付方式文本
    getPaymentTypeText(type) {
      const paymentMap = {
        0: '微信',
        1: '支付宝',
        2: '银行转账'
      };
      return paymentMap[type] || '其他';
    },

    // 格式化时间
    formatTime(timestamp) {
      const date = new Date(timestamp);
      const month = (date.getMonth() + 1).toString().padStart(2, '0');
      const day = date.getDate().toString().padStart(2, '0');
      const hours = date.getHours().toString().padStart(2, '0');
      const minutes = date.getMinutes().toString().padStart(2, '0');
      return `${month}-${day} ${hours}:${minutes}`;
    },

    // 时间格式化器
    formatter(type, value) {
      if (type === 'year') {
        return `${value}年`;
      }
      if (type === 'month') {
        return `${value}月`;
      }
      if (type === 'day') {
        return `${value}日`;
      }
      return value;
    }
  }
};
</script>

<style lang="scss" scoped>
.page-container {
  background-color: #f5f5f5;
  min-height: 100vh;
}

.filter-section {
  background: white;
  padding: 15px;
  margin-bottom: 10px;

  .filter-row {
    display: flex;
    align-items: center;
    margin-bottom: 10px;

    &:last-child {
      margin-bottom: 0;
    }
  }
}

.stats-section {
  margin-bottom: 10px;
  padding: 0 15px;

  .stats-card {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 10px;
    padding: 20px;

    .stats-row {
      display: flex;
      justify-content: space-around;
    }

    .stats-item {
      text-align: center;
      color: white;

      .stats-label {
        display: block;
        font-size: 14px;
        opacity: 0.8;
        margin-bottom: 5px;
      }

      .stats-value {
        display: block;
        font-size: 20px;
        font-weight: bold;

        &.income {
          color: #4cd964;
        }

        &.expense {
          color: #ff6b6b;
        }
      }
    }
  }
}

.list-section {
  flex: 1;
  padding: 0 15px;

  .scroll-view {
    height: calc(100vh - 400px);
  }
}

.commission-list {
  .commission-item {
    background: white;
    border-radius: 8px;
    padding: 15px;
    margin-bottom: 10px;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);

    .commission-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 10px;

      .commission-type {
        display: flex;
        align-items: center;

        .amount-type {
          margin-left: 8px;
          font-size: 12px;
          color: #666;
        }
      }

      .commission-time {
        font-size: 12px;
        color: #999;
      }
    }

    .commission-content {
      margin-bottom: 10px;

      .amount-section {
        display: flex;
        justify-content: space-between;
        align-items: baseline;
        margin-bottom: 5px;

        .main-amount {
          font-size: 18px;
          font-weight: bold;

          &.income {
            color: #4cd964;
          }

          &.expense {
            color: #ff6b6b;
          }
        }

        .received-amount {
          font-size: 12px;
          color: #666;
        }
      }

      .detail-info {
        .order-no, .fee-info {
          display: block;
          font-size: 12px;
          color: #999;
          margin-bottom: 2px;
        }
      }
    }

    .commission-footer {
      display: flex;
      justify-content: flex-end;
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
    .detail-row {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 12px 0;
      border-bottom: 1px solid #f0f0f0;

      &:last-child {
        border-bottom: none;
      }

      .detail-label {
        color: #666;
        font-size: 14px;
      }

      .detail-value {
        font-size: 14px;
        font-weight: 500;

        &.income {
          color: #4cd964;
        }

        &.expense {
          color: #ff6b6b;
        }
      }
    }
  }
}
</style>