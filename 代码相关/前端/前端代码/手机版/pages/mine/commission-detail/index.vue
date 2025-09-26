<template>
  <view class="page-container">
    <app-navbar title="佣金明细"></app-navbar>

    <view class="container">
      <!-- 筛选条件卡片 -->
      <view class="filter-card">
        <view class="filter-item" :class="{ 'has-border': true }">
          <view class="item-left">金额类型</view>
          <view class="item-right">
            <view class="select-field" @click="openAmountTypePicker">
              <text class="select-text" :class="{ 'placeholder': selectedAmountType === 0 }">
                {{ getAmountTypeLabel() }}
              </text>
              <u-icon name="arrow-down" size="16" color="#999"></u-icon>
            </view>
          </view>
        </view>

        <view class="filter-item">
          <view class="item-left">操作类型</view>
          <view class="item-right">
            <view class="select-field" @click="openWithdrawalTypePicker">
              <text class="select-text" :class="{ 'placeholder': selectedWithdrawalType === 0 }">
                {{ getWithdrawalTypeLabel() }}
              </text>
              <u-icon name="arrow-down" size="16" color="#999"></u-icon>
            </view>
          </view>
        </view>
      </view>


      <!-- 明细列表 -->
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
            v-for="(item, index) in commissionList"
            :key="'commission_' + index + '_' + (item.withdrawalRecordId || 'default')"
            class="commission-card"
            @click="showDetail(item)"
          >
            <view class="commission-item" :class="{ 'has-border': true }">
              <view class="item-left">
                <view class="type-info">
                  <text class="type-tag" :class="getTypeClass(item.withdrawalType)">
                    {{ getTypeText(item.withdrawalType) }}
                  </text>
                  <text class="amount-type">{{ getAmountTypeText(item.amountType) }}</text>
                </view>
              </view>
              <view class="item-right">
                <text class="commission-time">{{ formatTime(item.createTime) }}</text>
              </view>
            </view>

            <view class="commission-item" :class="{ 'has-border': true }">
              <view class="item-left">
                <text class="detail-label">交易金额</text>
              </view>
              <view class="item-right">
                <text class="main-amount" :class="item.amountType == 0 ? 'income' : 'expense'">
                  {{ item.amountType == 0 ? '+' : '-' }}¥{{ (item.withdrawalAmount / 100).toFixed(2) }}
                </text>
              </view>
            </view>

            <view class="commission-item" v-if="item.receivedAmount !== item.withdrawalAmount" :class="{ 'has-border': true }">
              <view class="item-left">
                <text class="detail-label">实际金额</text>
              </view>
              <view class="item-right">
                <text class="detail-value">¥{{ (item.receivedAmount / 100).toFixed(2) }}</text>
              </view>
            </view>

            <view class="commission-item" v-if="item.withdrawalRateAmount > 0" :class="{ 'has-border': true }">
              <view class="item-left">
                <text class="detail-label">手续费</text>
              </view>
              <view class="item-right">
                <text class="detail-value">¥{{ (item.withdrawalRateAmount / 100).toFixed(2) }}</text>
              </view>
            </view>

            <view class="commission-item" v-if="item.sourceNumber" :class="{ 'has-border': true }">
              <view class="item-left">
                <text class="detail-label">订单号</text>
              </view>
              <view class="item-right">
                <text class="detail-value">{{ item.sourceNumber }}</text>
              </view>
            </view>

            <view class="commission-item">
              <view class="item-left">
                <text class="detail-label">支付方式</text>
              </view>
              <view class="item-right">
                <text class="payment-type">{{ getPaymentTypeText(item.applyWithdrawalType) }}</text>
              </view>
            </view>
          </view>
        </view>

        <!-- 空状态 -->
        <view v-else-if="!isLoading" class="empty-state">
          <view class="empty-card">
            <text class="iconfont icon-empty"></text>
            <text class="empty-text">暂无佣金记录</text>
          </view>
        </view>

        <!-- 加载更多提示 -->
        <view v-if="hasMore && commissionList.length > 0" class="load-more">
          <text class="load-text">{{ loadStatus === 'loading' ? '加载中...' : loadStatus === 'nomore' ? '已加载全部' : '上拉加载更多' }}</text>
        </view>
      </scroll-view>
    </view>


    <!-- 详情弹窗 -->
    <u-popup v-model="showDetailPopup" mode="bottom" border-radius="20">
      <view class="detail-popup" v-if="selectedItem">
        <view class="popup-header">
          <text class="popup-title">佣金详情</text>
          <text class="iconfont icon-close" @click="showDetailPopup = false"></text>
        </view>

        <view class="detail-card">
          <view class="detail-item" :class="{ 'has-border': true }">
            <view class="item-left">类型</view>
            <view class="item-right">
              <text class="detail-value">{{ getTypeText(selectedItem.withdrawalType) }}</text>
            </view>
          </view>
          <view class="detail-item" :class="{ 'has-border': true }">
            <view class="item-left">金额</view>
            <view class="item-right">
              <text class="detail-value" :class="selectedItem.amountType == 0 ? 'income' : 'expense'">
                {{ selectedItem.amountType == 0 ? '+' : '-' }}¥{{ (selectedItem.withdrawalAmount / 100).toFixed(2) }}
              </text>
            </view>
          </view>
          <view class="detail-item" :class="{ 'has-border': true }">
            <view class="item-left">实际金额</view>
            <view class="item-right">
              <text class="detail-value">¥{{ (selectedItem.receivedAmount / 100).toFixed(2) }}</text>
            </view>
          </view>
          <view class="detail-item" v-if="selectedItem.withdrawalRate" :class="{ 'has-border': true }">
            <view class="item-left">提现费率</view>
            <view class="item-right">
              <text class="detail-value">{{ selectedItem.withdrawalRate }}%</text>
            </view>
          </view>
          <view class="detail-item" v-if="selectedItem.withdrawalRateAmount > 0" :class="{ 'has-border': true }">
            <view class="item-left">手续费</view>
            <view class="item-right">
              <text class="detail-value">¥{{ (selectedItem.withdrawalRateAmount / 100).toFixed(2) }}</text>
            </view>
          </view>
          <view class="detail-item" :class="{ 'has-border': true }">
            <view class="item-left">支付方式</view>
            <view class="item-right">
              <text class="detail-value">{{ getPaymentTypeText(selectedItem.applyWithdrawalType) }}</text>
            </view>
          </view>
          <view class="detail-item" v-if="selectedItem.sourceNumber" :class="{ 'has-border': true }">
            <view class="item-left">订单号</view>
            <view class="item-right">
              <text class="detail-value">{{ selectedItem.sourceNumber }}</text>
            </view>
          </view>
          <view class="detail-item">
            <view class="item-left">时间</view>
            <view class="item-right">
              <text class="detail-value">{{ formatTime(selectedItem.createTime) }}</text>
            </view>
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

      // 金额类型选择
      selectedAmountType: 0,
      amountTypeOptions: [
        ['全部', '存入', '提现']
      ],
      amountTypeData: [
        { label: '全部', value: '' },
        { label: '存入', value: 0 },
        { label: '提现', value: 1 }
      ],

      // 操作类型选择
      selectedWithdrawalType: 0,
      withdrawalTypeOptions: [
        ['全部', '佣金结算', '代理商提现', '管理员操作']
      ],
      withdrawalTypeData: [
        { label: '全部', value: '' },
        { label: '佣金结算', value: 0 },
        { label: '代理商提现', value: 1 },
        { label: '管理员操作', value: 2 }
      ],


      // 列表数据
      commissionList: [],
      isLoading: false,
      isRefreshing: false,
      hasMore: true,
      loadStatus: 'loadmore',
      total: 0,


      // 详情弹窗
      showDetailPopup: false,
      selectedItem: null
    };
  },

  onLoad() {
    this.loadCommissionList();
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


    // 显示金额类型选择器
    openAmountTypePicker() {
      console.log('点击金额类型选择器');
      // 使用程序化方式触发picker选择
      uni.showActionSheet({
        itemList: this.amountTypeOptions[0],
        success: (res) => {
          this.onAmountTypeChange({ detail: { value: res.tapIndex } });
        }
      });
    },

    // 显示操作类型选择器
    openWithdrawalTypePicker() {
      console.log('点击操作类型选择器');
      // 使用程序化方式触发picker选择
      uni.showActionSheet({
        itemList: this.withdrawalTypeOptions[0],
        success: (res) => {
          this.onWithdrawalTypeChange({ detail: { value: res.tapIndex } });
        }
      });
    },

    // 金额类型选择改变
    onAmountTypeChange(e) {
      const index = e.detail.value;
      console.log('金额类型选择:', index);
      this.selectedAmountType = index;
      const selectedData = this.amountTypeData[index];
      this.queryParams.amountType = selectedData.value;
      console.log('更新查询参数amountType:', this.queryParams.amountType);
      this.loadCommissionList(true);
    },

    // 操作类型选择改变
    onWithdrawalTypeChange(e) {
      const index = e.detail.value;
      console.log('操作类型选择:', index);
      this.selectedWithdrawalType = index;
      const selectedData = this.withdrawalTypeData[index];
      this.queryParams.withdrawalType = selectedData.value;
      console.log('更新查询参数withdrawalType:', this.queryParams.withdrawalType);
      this.loadCommissionList(true);
    },

    // 获取金额类型标签
    getAmountTypeLabel() {
      const option = this.amountTypeData[this.selectedAmountType];
      return option ? option.label : '请选择';
    },

    // 获取操作类型标签
    getWithdrawalTypeLabel() {
      const option = this.withdrawalTypeData[this.selectedWithdrawalType];
      return option ? option.label : '请选择';
    },

    // 获取类型样式类
    getTypeClass(type) {
      const classMap = {
        0: 'success',
        1: 'warning',
        2: 'info'
      };
      return classMap[type] || 'default';
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

  }
};
</script>

<style lang="scss" scoped>
.page-container {
  background-color: #f5f6f7;
  min-height: 100vh;
}

.container {
  padding: 30rpx;
}

/* 筛选条件卡片 - 符合单卡片多项目设计模式 */
.filter-card {
  background-color: #fff;
  border-radius: 8rpx;
  overflow: hidden;
  margin-bottom: 20rpx;

  .filter-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 26rpx 30rpx;
    min-height: 88rpx;

    &.has-border {
      border-bottom: 1rpx solid #eaeef1;
    }

    .item-left {
      font-size: 32rpx;
      color: #333;
      flex-shrink: 0;
      width: 140rpx;
    }

    .item-right {
      display: flex;
      align-items: center;
      flex: 1;
      justify-content: flex-end;

      .select-field {
        display: flex;
        align-items: center;
        gap: 12rpx;
        min-width: 120rpx;

        .select-text {
          font-size: 32rpx;
          color: #333;

          &.placeholder {
            color: #999;
          }
        }
      }
    }
  }
}


/* 滚动区域 */
.scroll-view {
  height: calc(100vh - 280rpx);
}

/* 佣金列表 - 符合单卡片多项目设计模式 */
.commission-list {
  .commission-card {
    background-color: #fff;
    border-radius: 8rpx;
    overflow: hidden;
    margin-bottom: 20rpx;

    .commission-item {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 26rpx 30rpx;
      min-height: 88rpx;

      &.has-border {
        border-bottom: 1rpx solid #eaeef1;
      }

      .item-left {
        flex-shrink: 0;
        width: 140rpx;

        .type-info {
          display: flex;
          align-items: center;
          gap: 8rpx;

          .type-tag {
            padding: 6rpx 12rpx;
            border-radius: 8rpx;
            font-size: 22rpx;
            color: #fff;

            &.success {
              background-color: #52c41a;
            }

            &.warning {
              background-color: #faad14;
            }

            &.info {
              background-color: #909399;
            }

            &.default {
              background-color: #909399;
            }
          }

          .amount-type {
            font-size: 22rpx;
            color: #666;
          }
        }

        .detail-label {
          font-size: 32rpx;
          color: #333;
        }
      }

      .item-right {
        display: flex;
        align-items: center;
        flex: 1;
        justify-content: flex-end;

        .commission-time {
          font-size: 26rpx;
          color: #999;
        }

        .main-amount {
          font-size: 32rpx;
          font-weight: bold;

          &.income {
            color: #52c41a;
          }

          &.expense {
            color: #f5222d;
          }
        }

        .detail-value {
          font-size: 32rpx;
          color: #333;
        }

        .payment-type {
          font-size: 32rpx;
          color: #666;
        }
      }
    }
  }
}

/* 空状态 */
.empty-state {
  padding: 100rpx 0;

  .empty-card {
    background-color: #fff;
    border-radius: 8rpx;
    padding: 80rpx 30rpx;
    text-align: center;

    .iconfont {
      font-size: 80rpx;
      color: #ccc;
      margin-bottom: 20rpx;
    }

    .empty-text {
      font-size: 32rpx;
      color: #999;
    }
  }
}

/* 加载更多 */
.load-more {
  padding: 40rpx 0;
  text-align: center;

  .load-text {
    font-size: 28rpx;
    color: #999;
  }
}

/* 详情弹窗 - 符合单卡片多项目设计模式 */
.detail-popup {
  padding: 40rpx;

  .popup-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 40rpx;

    .popup-title {
      font-size: 36rpx;
      font-weight: bold;
      color: #333;
    }

    .iconfont {
      font-size: 36rpx;
      color: #666;
    }
  }

  .detail-card {
    background-color: #fff;
    border-radius: 8rpx;
    overflow: hidden;

    .detail-item {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 26rpx 30rpx;
      min-height: 88rpx;

      &.has-border {
        border-bottom: 1rpx solid #eaeef1;
      }

      .item-left {
        font-size: 32rpx;
        color: #333;
        flex-shrink: 0;
        width: 140rpx;
      }

      .item-right {
        display: flex;
        align-items: center;
        flex: 1;
        justify-content: flex-end;

        .detail-value {
          font-size: 32rpx;
          color: #333;

          &.income {
            color: #52c41a;
          }

          &.expense {
            color: #f5222d;
          }
        }
      }
    }
  }
}
</style>