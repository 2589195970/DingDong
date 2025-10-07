<template>
  <view class="page-container">
    <!-- 导航栏 -->
    <app-navbar title="订单管理"></app-navbar>

    <view class="container">
      <!-- 订单类型切换 -->
      <view class="order-type-section">
        <u-subsection
          :list="orderTypeList"
          mode="subsection"
          :current="currentOrderType"
          @change="handleOrderTypeChange"
          activeColor="#f09b7f"
          fontSize="16"
        ></u-subsection>
      </view>
      <!-- 高级筛选 -->
      <view class="advanced-filter" v-show="showAdvancedFilter">
        <view class="filter-card">
          <view class="filter-row">
            <view class="filter-item">
              <view class="filter-label">订单号</view>
              <input class="filter-input" placeholder="请输入订单号" v-model="params.orderId" />
            </view>
            <view class="filter-item">
              <view class="filter-label">收件人</view>
              <input class="filter-input" placeholder="请输入收件人姓名" v-model="params.cardName" />
            </view>
          </view>
          <view class="filter-row">
            <view class="filter-item">
              <view class="filter-label">手机号</view>
              <input class="filter-input" placeholder="请输入手机号" v-model="params.cardPhone" />
            </view>
            <view class="filter-item">
              <view class="filter-label">产品名称</view>
              <input class="filter-input" placeholder="请输入产品名称" v-model="params.productName" />
            </view>
          </view>
          <view class="filter-row">
            <view class="filter-item">
              <view class="filter-label">订单状态</view>
              <view class="select-picker" @click="showOrderStatus = true">
                <text class="select-text" v-if="selectedOrderStatus">{{ selectedOrderStatus.name }}</text>
                <text class="select-placeholder" v-else>选择订单状态</text>
                <text class="iconfont icon-down"></text>
              </view>
            </view>
            <view class="filter-item">
              <view class="filter-label">首充状态</view>
              <view class="select-picker" @click="showIsRecharged = true">
                <text class="select-text" v-if="selectedIsRecharged">{{ selectedIsRecharged.name }}</text>
                <text class="select-placeholder" v-else>选择首充状态</text>
                <text class="iconfont icon-down"></text>
              </view>
            </view>
          </view>
          <view class="filter-row">
            <view class="filter-item">
              <view class="filter-label">佣金状态</view>
              <view class="select-picker" @click="showCommissionStatus = true">
                <text class="select-text" v-if="selectedCommissionStatus">{{ selectedCommissionStatus.name }}</text>
                <text class="select-placeholder" v-else>选择佣金状态</text>
                <text class="iconfont icon-down"></text>
              </view>
            </view>
            <view class="filter-item" v-if="currentOrderType === 0">
              <view class="filter-label">订单来源</view>
              <view class="select-picker" @click="showOrderSource = true">
                <text class="select-text" v-if="selectedOrderSource">{{ selectedOrderSource.name }}</text>
                <text class="select-placeholder" v-else>选择订单来源</text>
                <text class="iconfont icon-down"></text>
              </view>
            </view>
          </view>
          <view class="filter-row">
            <view class="filter-item full-width">
              <view class="filter-label">时间范围</view>
              <view class="date-picker" @click="show = true">
                <text class="date-text" v-if="first && last">{{ first }} ~ {{ last }}</text>
                <text class="date-placeholder" v-else>选择日期范围</text>
                <text class="iconfont icon-right"></text>
              </view>
            </view>
          </view>
          <view class="filter-actions">
            <view class="filter-btn reset-btn" @click="resetFilter">重置</view>
            <view class="filter-btn search-btn" @click="handleSearch">搜索</view>
          </view>
        </view>
      </view>

      <!-- 筛选操作栏 -->
      <view class="filter-toolbar">
        <view class="filter-toggle" @click="toggleAdvancedFilter">
          <text class="iconfont icon-filter"></text>
          <text class="filter-text">筛选</text>
          <text class="iconfont" :class="showAdvancedFilter ? 'icon-up' : 'icon-down'"></text>
        </view>
      </view>

      <!-- 加载状态 -->
      <view class="loading-section" v-if="loading">
        <text class="loading-text">正在加载...</text>
      </view>

      <!-- 订单列表 -->
      <view class="order-list" v-else-if="list.length > 0">
        <view
          class="order-card"
          v-for="(item, index) in list"
          :key="item.orderId"
          @click="handleOrderDetail(item)"
        >
          <!-- 订单头部 -->
          <view class="order-header">
            <view class="order-id">订单号：{{ item.orderId }}</view>
            <view class="order-status" :class="getStatusClass(item.orderStatus)">
              {{ getStatusText(item.orderStatus) }}
            </view>
          </view>

          <!-- 订单内容 -->
          <view class="order-content">
            <view class="product-image">
              <image
                :src="item.productMasterMap || '/static/images/default-product.png'"
                class="product-img"
                mode="aspectFill"
              ></image>
            </view>
            <view class="product-info">
              <view class="product-name">{{ item.productName }}</view>
              <view class="order-info">
                <text class="info-label">收件信息：</text>
                <text class="info-value">{{ item.cardName }} {{ item.cardPhone }}</text>
              </view>
              <view class="order-info" v-if="item.express || item.trackingNumber">
                <text class="info-label">物流信息：</text>
                <text class="info-value">{{ item.express }} {{ item.trackingNumber }}</text>
              </view>
            </view>
          </view>

          <!-- 订单底部 -->
          <view class="order-footer">
            <view class="order-time">{{ formatTimestamp(item.createTime) }}</view>
            <view class="order-agent" v-if="item.showDownstreamName">
              推广人：{{ item.showDownstreamName }}
            </view>
          </view>
        </view>

        <u-loadmore :status="status" />
      </view>

      <!-- 空状态 -->
      <view class="empty-section" v-else>
        <text class="empty-text">暂无订单数据</text>
      </view>
    </view>

    <!-- 日期选择器 -->
    <u-calendar
      :show="show"
      v-model="show"
      allowSameDay
      @confirm="handleDateConfirm"
      @close="show = false"
      mode="range"
      monthNum="50"
      :minDate="minDate"
    ></u-calendar>

    <!-- 订单状态选择 -->
    <u-action-sheet
      :actions="listOrderStatus"
      title="选择订单状态"
      :show="showOrderStatus"
      @close="showOrderStatus = false"
      @select="handleStatusSelect"
    ></u-action-sheet>

    <!-- 首充状态选择 -->
    <u-action-sheet
      :actions="listIsRecharged"
      title="选择首充状态"
      :show="showIsRecharged"
      @close="showIsRecharged = false"
      @select="handleIsRechargedSelect"
    ></u-action-sheet>

    <!-- 佣金状态选择 -->
    <u-action-sheet
      :actions="listCommissionStatus"
      title="选择佣金状态"
      :show="showCommissionStatus"
      @close="showCommissionStatus = false"
      @select="handleCommissionStatusSelect"
    ></u-action-sheet>

    <!-- 订单来源选择 -->
    <u-action-sheet
      :actions="listOrderSource"
      title="选择订单来源"
      :show="showOrderSource"
      @close="showOrderSource = false"
      @select="handleOrderSourceSelect"
    ></u-action-sheet>
  </view>
</template>

<script>
import { agentSelectOrderListPage } from "@/api/order/order.js";
import AppNavbar from '@/components/app-navbar/app-navbar.vue';

export default {
  name: 'OrderIndex',
  components: {
    AppNavbar
  },
  data() {
    return {
      // 订单类型
      orderTypeList: ['我的订单', '代理商订单'],
      currentOrderType: 0, // 当前选中的订单类型
      // 快速筛选
      quickFilterList: [
        { label: '全部', value: '' },
        { label: '待发货', value: '1' },
        { label: '已发货', value: '2' },
        { label: '已激活', value: '4' },
        { label: '失败', value: '-1' }
      ],
      quickFilter: '', // 当前快速筛选值

      // 高级筛选
      showAdvancedFilter: true, // 是否显示高级筛选

      // 订单状态选择
      listOrderStatus: [
        { name: '全部', value: '' },
        { name: '订单失败', value: '-1' },
        { name: '订单预创建', value: '0' },
        { name: '订单申请成功', value: '1' },
        { name: '订单已发货', value: '2' },
        { name: '订单已签收', value: '3' },
        { name: '订单已激活', value: '4' }
      ],
      showOrderStatus: false,
      selectedOrderStatus: null,

      // 首充状态选择
      listIsRecharged: [
        { name: '全部', value: '' },
        { name: '未充值', value: '0' },
        { name: '已充值', value: '1' }
      ],
      showIsRecharged: false,
      selectedIsRecharged: null,

      // 佣金状态选择
      listCommissionStatus: [
        { name: '全部', value: '' },
        { name: '未到结算状态', value: '0' },
        { name: '待结算', value: '1' },
        { name: '已结算', value: '3' },
        { name: '无法结算', value: '4' }
      ],
      showCommissionStatus: false,
      selectedCommissionStatus: null,

      // 订单来源选择
      listOrderSource: [
        { name: '全部', value: '' },
        { name: '信息流', value: '0' },
        { name: '合作方API进单', value: '1' },
        { name: '导单', value: '2' },
        { name: '重推', value: '3' }
      ],
      showOrderSource: false,
      selectedOrderSource: null,

      // 日期选择
      show: false,
      first: '',
      last: '',
      minDate: new Date(2023, 0, 1).getTime(),

      // 请求参数
      params: {
        pageNo: 1,
        pageSize: 10,
        orderId: '',
        cardName: '',
        cardPhone: '',
        cardId: '',
        productName: '',
        orderStatus: '',
        isRecharged: '',
        orderCommissionStatus: '',
        orderSource: '',
        startTime: null,
        endTime: null
      },

      // 页面状态
      status: 'loadmore',
      loading: true,
      list: []
    };
  },
  onLoad() {
    this.getList();
  },

  // 下拉刷新
  onPullDownRefresh() {
    this.resetPagination();
    this.getList();
    setTimeout(() => {
      uni.stopPullDownRefresh();
    }, 1000);
  },

  // 上拉加载
  onReachBottom() {
    if (this.status !== 'nomore') {
      this.status = 'loading';
      this.params.pageNo++;
      this.getList();
    }
  },

  methods: {
    // 订单类型切换
    handleOrderTypeChange(index) {
      this.currentOrderType = index;
      this.resetPagination();
      this.getList();
    },

    // 快速筛选
    handleQuickFilter(value) {
      this.quickFilter = value;
      this.params.orderStatus = value;
      this.resetPagination();
      this.getList();
    },

    // 切换高级筛选
    toggleAdvancedFilter() {
      this.showAdvancedFilter = !this.showAdvancedFilter;
    },

    // 重置筛选
    resetFilter() {
      this.params = {
        pageNo: 1,
        pageSize: 10,
        orderId: '',
        cardName: '',
        cardPhone: '',
        cardId: '',
        productName: '',
        orderStatus: '',
        isRecharged: '',
        orderCommissionStatus: '',
        orderSource: '',
        startTime: null,
        endTime: null
      };
      this.quickFilter = '';
      this.selectedOrderStatus = null;
      this.selectedIsRecharged = null;
      this.selectedCommissionStatus = null;
      this.selectedOrderSource = null;
      this.first = '';
      this.last = '';
      this.getList();
    },

    // 搜索
    handleSearch() {
      this.resetPagination();
      this.getList();
    },

    // 重置分页
    resetPagination() {
      this.params.pageNo = 1;
      this.status = 'loadmore';
    },

    // 日期确认
    handleDateConfirm(dates) {
      this.show = false;
      if (dates && dates.length >= 2) {
        this.first = dates[0];
        this.last = dates[dates.length - 1];
        this.params.startTime = new Date(this.first).getTime();
        this.params.endTime = new Date(this.last).getTime();
      }
    },

    // 状态选择
    handleStatusSelect(item) {
      this.params.orderStatus = item.value;
      this.selectedOrderStatus = item.value ? item : null;
      this.quickFilter = item.value;
      this.showOrderStatus = false;
      this.resetPagination();
      this.getList();
    },

    // 首充状态选择
    handleIsRechargedSelect(item) {
      this.params.isRecharged = item.value;
      this.selectedIsRecharged = item.value ? item : null;
      this.showIsRecharged = false;
      this.resetPagination();
      this.getList();
    },

    // 佣金状态选择
    handleCommissionStatusSelect(item) {
      this.params.orderCommissionStatus = item.value;
      this.selectedCommissionStatus = item.value ? item : null;
      this.showCommissionStatus = false;
      this.resetPagination();
      this.getList();
    },

    // 订单来源选择
    handleOrderSourceSelect(item) {
      this.params.orderSource = item.value;
      this.selectedOrderSource = item.value ? item : null;
      this.showOrderSource = false;
      this.resetPagination();
      this.getList();
    },

    // 订单详情
    handleOrderDetail(data) {
      uni.navigateTo({
        url: `/pages/Order/orderxiangqing?key=${encodeURIComponent(JSON.stringify(data))}`
      });
    },

    // 获取状态文字
    getStatusText(status) {
      const statusMap = {
        '-1': '失败',
        '0': '申请成功',
        '1': '申请中',
        '2': '已发货',
        '4': '已激活'
      };
      return statusMap[status] || '未知状态';
    },

    // 获取状态样式类
    getStatusClass(status) {
      const statusClassMap = {
        '-1': 'status-error',
        '0': 'status-success',
        '1': 'status-warning',
        '2': 'status-info',
        '4': 'status-success'
      };
      return statusClassMap[status] || 'status-default';
    },

    // 时间戳转换
    formatTimestamp(timestamp) {
      const date = new Date(timestamp);
      const year = date.getFullYear();
      const month = ("0" + (date.getMonth() + 1)).slice(-2);
      const day = ("0" + date.getDate()).slice(-2);
      const hours = ("0" + date.getHours()).slice(-2);
      const minutes = ("0" + date.getMinutes()).slice(-2);
      return `${year}-${month}-${day} ${hours}:${minutes}`;
    },

    // 获取订单列表
    async getList() {
      try {
        this.loading = this.params.pageNo === 1;

        // 根据订单类型调用不同接口或设置不同参数
        const requestParams = {
          ...this.params,
          orderType: this.currentOrderType // 0: 我的订单, 1: 代理商订单
        };

        const resp = await agentSelectOrderListPage(requestParams);

        if (resp.code === 200) {
          const { data } = resp;
          const orderList = data.rows || [];

          if (orderList.length < this.params.pageSize) {
            this.status = 'nomore';
          }

          if (this.params.pageNo === 1) {
            this.list = orderList;
          } else {
            this.list = [...this.list, ...orderList];
          }
        } else {
          uni.showToast({
            title: resp.msg || '获取订单列表失败',
            icon: 'none'
          });
        }
      } catch (error) {
        console.error('获取订单列表错误:', error);
        uni.showToast({
          title: '网络错误，请重试',
          icon: 'none'
        });
      } finally {
        this.loading = false;
        if (this.status === 'loading') {
          this.status = 'loadmore';
        }
      }
    }
  }
	};
</script>

<style lang="scss" scoped>
// 页面基础样式
page {
  background-color: #f5f6f7;
}

.page-container {
  min-height: 100vh;
  background-color: #f5f6f7;
}

.container {
  padding: 15rpx;
}

// 订单类型切换区域
.order-type-section {
  background-color: #fff;
  padding: 30rpx;
  margin-bottom: 20rpx;
  border-radius: 8rpx;
}

// 快速筛选区域
.quick-filter-section {
  background-color: #fff;
  padding: 20rpx 30rpx;
  margin-bottom: 20rpx;
  border-radius: 8rpx;

  .filter-tags {
    display: flex;
    flex-wrap: wrap;
    gap: 20rpx;

    .filter-tag {
      padding: 12rpx 24rpx;
      border-radius: 30rpx;
      font-size: 28rpx;
      color: #666;
      background-color: #f5f6f7;
      transition: all 0.3s ease;

      &.active {
        background-color: #f09b7f;
        color: #fff;
      }
    }
  }
}

// 高级筛选区域
.advanced-filter {
  margin-bottom: 20rpx;

  .filter-card {
    background-color: #fff;
    border-radius: 8rpx;
    padding: 30rpx;

    .filter-row {
      display: flex;
      gap: 20rpx;
      margin-bottom: 20rpx;

      &:last-child {
        margin-bottom: 0;
      }

      .filter-item {
        flex: 1;
        display: flex;
        align-items: center;
        min-width: 0; /* 防止flex子项溢出 */

        &.full-width {
          flex: 1 1 100%;
        }

        .filter-label {
          width: 120rpx;
          font-size: 28rpx;
          color: #333;
          flex-shrink: 0;
        }

        .filter-input {
          flex: 1;
          height: 60rpx;
          padding: 0 16rpx;
          background-color: #f5f6f7;
          border-radius: 6rpx;
          font-size: 26rpx;
          color: #333;
          border: none;
          min-width: 0; /* 防止input溢出 */
        }

        .date-picker {
          flex: 1;
          height: 60rpx;
          padding: 0 16rpx;
          background-color: #f5f6f7;
          border-radius: 6rpx;
          display: flex;
          align-items: center;
          justify-content: space-between;
          min-width: 0; /* 防止溢出 */

          .date-text {
            font-size: 26rpx;
            color: #333;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
          }

          .date-placeholder {
            font-size: 26rpx;
            color: #999;
          }

          .iconfont {
            font-size: 20rpx;
            color: #c0c0c0;
            flex-shrink: 0;
          }
        }

        .select-picker {
          flex: 1;
          height: 60rpx;
          padding: 0 16rpx;
          background-color: #f5f6f7;
          border-radius: 6rpx;
          display: flex;
          align-items: center;
          justify-content: space-between;
          min-width: 0; /* 防止溢出 */

          .select-text {
            font-size: 26rpx;
            color: #333;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
          }

          .select-placeholder {
            font-size: 26rpx;
            color: #999;
          }

          .iconfont {
            font-size: 20rpx;
            color: #c0c0c0;
            flex-shrink: 0;
          }
        }
      }
    }

    .filter-actions {
      display: flex;
      gap: 20rpx;
      margin-top: 30rpx;

      .filter-btn {
        flex: 1;
        height: 80rpx;
        border-radius: 8rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 32rpx;

        &.reset-btn {
          background-color: #f5f6f7;
          color: #666;
        }

        &.search-btn {
          background-color: #f09b7f;
          color: #fff;
        }
      }
    }
  }
}

// 筛选工具栏
.filter-toolbar {
  background-color: #fff;
  padding: 20rpx 30rpx;
  margin-bottom: 20rpx;
  border-radius: 8rpx;

  .filter-toggle {
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 28rpx;
    color: #666;

    .iconfont {
      margin: 0 10rpx;
      font-size: 28rpx;
    }
  }
}

// 加载状态
.loading-section {
  text-align: center;
  padding: 80rpx 0;

  .loading-text {
    font-size: 28rpx;
    color: #999;
  }
}

// 空状态
.empty-section {
  text-align: center;
  padding: 120rpx 0;

  .empty-text {
    font-size: 32rpx;
    color: #999;
  }
}

// 订单列表
.order-list {
  .order-card {
    background-color: #fff;
    border-radius: 8rpx;
    margin-bottom: 20rpx;
    overflow: hidden;

    // 订单头部
    .order-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 30rpx;
      border-bottom: 1rpx solid #eaeef1;

      .order-id {
        font-size: 32rpx;
        color: #333;
        font-weight: 500;
      }

      .order-status {
        font-size: 28rpx;
        padding: 8rpx 16rpx;
        border-radius: 20rpx;

        &.status-success {
          background-color: #f0f9ff;
          color: #52c41a;
        }

        &.status-error {
          background-color: #fff2f0;
          color: #f5222d;
        }

        &.status-warning {
          background-color: #fffbe6;
          color: #faad14;
        }

        &.status-info {
          background-color: #f6f7f9;
          color: #909399;
        }

        &.status-default {
          background-color: #f5f6f7;
          color: #666;
        }
      }
    }

    // 订单内容
    .order-content {
      display: flex;
      padding: 30rpx;

      .product-image {
        width: 160rpx;
        height: 160rpx;
        margin-right: 30rpx;
        flex-shrink: 0;

        .product-img {
          width: 100%;
          height: 100%;
          border-radius: 8rpx;
          background-color: #f5f6f7;
        }
      }

      .product-info {
        flex: 1;
        display: flex;
        flex-direction: column;
        justify-content: space-between;

        .product-name {
          font-size: 32rpx;
          color: #333;
          font-weight: 500;
          margin-bottom: 20rpx;
          line-height: 1.4;
        }

        .order-info {
          margin-bottom: 15rpx;
          font-size: 28rpx;

          &:last-child {
            margin-bottom: 0;
          }

          .info-label {
            color: #666;
          }

          .info-value {
            color: #333;
          }
        }
      }
    }

    // 订单底部
    .order-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 20rpx 30rpx;
      background-color: #fafbfc;
      font-size: 24rpx;
      color: #999;

      .order-time {
        flex: 1;
      }

      .order-agent {
        text-align: right;
      }
    }
  }
}

// 响应式适配
@media screen and (max-width: 750rpx) {
  .order-content {
    .product-image {
      width: 120rpx !important;
      height: 120rpx !important;
      margin-right: 20rpx !important;
    }

    .product-info {
      .product-name {
        font-size: 30rpx !important;
      }

      .order-info {
        font-size: 26rpx !important;
      }
    }
  }

  // 小屏幕下优化筛选区域，但保持两列布局
  .advanced-filter {
    .filter-card {
      padding: 20rpx;

      .filter-row {
        gap: 15rpx;

        .filter-item {
          .filter-label {
            width: 90rpx;
            font-size: 24rpx;
          }

          .filter-input,
          .date-picker,
          .select-picker {
            height: 52rpx;
            padding: 0 12rpx;
            font-size: 22rpx;
          }

          .date-text,
          .select-text,
          .date-placeholder,
          .select-placeholder {
            font-size: 22rpx;
          }

          .iconfont {
            font-size: 18rpx;
          }
        }
      }
    }
  }
}

// 超小屏幕适配，保持两列布局
@media screen and (max-width: 600rpx) {
  .advanced-filter {
    .filter-card {
      padding: 15rpx;

      .filter-row {
        gap: 12rpx;
        margin-bottom: 15rpx;

        .filter-item {
          .filter-label {
            width: 80rpx;
            font-size: 22rpx;
          }

          .filter-input,
          .date-picker,
          .select-picker {
            height: 48rpx;
            padding: 0 10rpx;
            font-size: 20rpx;
          }

          .date-text,
          .select-text,
          .date-placeholder,
          .select-placeholder {
            font-size: 20rpx;
          }

          .iconfont {
            font-size: 16rpx;
          }
        }
      }

      .filter-actions {
        gap: 12rpx;

        .filter-btn {
          height: 64rpx;
          font-size: 26rpx;
        }
      }
    }
  }
}
</style>