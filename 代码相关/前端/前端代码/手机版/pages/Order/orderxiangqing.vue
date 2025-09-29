<template>
  <view class="page-container">
    <!-- 导航栏 -->
    <app-navbar title="订单详情"></app-navbar>

    <view class="container">
      <!-- 订单状态突出显示 -->
      <view class="status-highlight">
        <view class="status-badge" :class="orderStatusClass">
          {{ orderStatusText }}
        </view>
        <view class="order-id">订单号：{{ xqdata.orderId }}</view>
      </view>

      <!-- 订单信息卡片 -->
      <view class="info-card">
        <view class="card-header">
          <text class="iconfont icon-order"></text>
          <text class="card-title">订单信息</text>
        </view>
        <view class="info-item" v-for="(item, index) in orderInfoList" :key="index"
              :class="{ 'has-border': index < orderInfoList.length - 1 }">
          <view class="item-left">{{ item.label }}</view>
          <view class="item-right">
            <text class="item-value" :class="item.valueClass">{{ item.value }}</text>
          </view>
        </view>
      </view>

      <!-- 开卡人信息卡片 -->
      <view class="info-card">
        <view class="card-header">
          <text class="iconfont icon-people"></text>
          <text class="card-title">开卡人信息</text>
        </view>
        <view class="info-item" v-for="(item, index) in cardHolderInfoList" :key="index"
              :class="{ 'has-border': index < cardHolderInfoList.length - 1 }">
          <view class="item-left">{{ item.label }}</view>
          <view class="item-right">
            <text class="item-value">{{ item.value }}</text>
          </view>
        </view>
      </view>

      <!-- 订单状态卡片 -->
      <view class="info-card">
        <view class="card-header">
          <text class="iconfont icon-statistics"></text>
          <text class="card-title">订单状态</text>
        </view>
        <view class="info-item" v-for="(item, index) in orderStatusInfoList" :key="index"
              :class="{ 'has-border': index < orderStatusInfoList.length - 1 }">
          <view class="item-left">{{ item.label }}</view>
          <view class="item-right">
            <text class="item-value" :class="item.valueClass">{{ item.value }}</text>
          </view>
        </view>
      </view>

      <!-- 生产信息卡片 -->
      <view class="info-card">
        <view class="card-header">
          <text class="iconfont icon-logistics"></text>
          <text class="card-title">生产信息</text>
        </view>
        <view class="info-item" v-for="(item, index) in productionInfoList" :key="index"
              :class="{ 'has-border': index < productionInfoList.length - 1 }">
          <view class="item-left">{{ item.label }}</view>
          <view class="item-right">
            <text class="item-value">{{ item.value }}</text>
          </view>
        </view>
      </view>

      <!-- 接口信息卡片 -->
      <view class="info-card" v-if="hasInterfaceInfo">
        <view class="card-header">
          <text class="iconfont icon-api"></text>
          <text class="card-title">接口信息</text>
        </view>
        <view class="info-item" v-for="(item, index) in interfaceInfoList" :key="index"
              :class="{ 'has-border': index < interfaceInfoList.length - 1 }">
          <view class="item-left">{{ item.label }}</view>
          <view class="item-right">
            <text class="item-value">{{ item.value }}</text>
          </view>
        </view>
      </view>

      <!-- 产品图片展示 -->
      <view class="info-card" v-if="xqdata.productMasterMap">
        <view class="card-header">
          <text class="iconfont icon-image"></text>
          <text class="card-title">产品图片</text>
        </view>
        <view class="product-image">
          <image :src="xqdata.productMasterMap" class="product-img" mode="aspectFit"></image>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import AppNavbar from '@/components/app-navbar/app-navbar.vue';

export default {
  name: 'OrderDetail',
  components: {
    AppNavbar
  },
  data() {
    return {
      xqdata: {
        "orderId": "1117964472650399744",
        "orderDownstreamId": null,
        "cardName": "张三",
        "cardPhone": "18753796611",
        "cardId": "3703051983********",
        "provinceCode": "120000",
        "provinceName": "天津市",
        "cityCode": "120100",
        "cityName": "天津市",
        "countyCode": "120101",
        "countyName": "和平区",
        "cardAddress": "测*******",
        "orderStatus": -1,
        "orderMessage": "2025-05-08系统初审:超过最大下单数量",
        "productCode": "b9uy875x",
        "productName": "M电信清秋卡29元80G流量+0.1元/分钟（只发山东）",
        "productType": "1",
        "downstreamCode": "dpoo0ncr",
        "downstreamName": "叮咚号",
        "showDownstreamCode": "dpoo0ncr",
        "showDownstreamName": "叮咚号",
        "isRecharged": 0,
        "rechargeAmount": null,
        "rechargeTime": null,
        "express": null,
        "trackingNumber": null,
        "deliveryTime": null,
        "activeTime": null,
        "createTime": 1746709966846,
        "updateTime": 1746709968837,
        "accNumber": null,
        "orderSource": 0,
        "jsonParam": "{\"productCode\":\"b9uy875x\",\"agentCode\":\"dpoo0ncr\",\"link\":\"https%3A%2F%2Fh5.shengdakeji.vip%2F%3FproductCode%3Db9uy875x%26agentCode%3Ddpoo0ncr\",\"GthSubmitOrderResponse\":\"{\\\"createdAt\\\":\\\"2025-05-08 21:12:47\\\",\\\"id\\\":\\\"127340986\\\",\\\"productSku\\\":\\\"M电信5G清秋卡 29元/月80G流量+0.1元/分钟 免首月月租\\\",\\\"shareId\\\":\\\"356390\\\",\\\"sourceId\\\":\\\"1117964472650399744\\\",\\\"status\\\":\\\"120\\\",\\\"strRand\\\":\\\"zHQDHuDhzmPkhgAd42\\\"}\"}",
        "orderCommissionStatus": 0,
        "operatorType": 1,
        "productMasterMap": "http://yun.shengda.live/numberCard/a9660c6f-6680-458d-b0a3-d98bb2e5de41.jpg",
        "upstreamApiName": "GTH接口",
        "upstreamProductName": "电信清秋卡",
        "orderUpstreamId": "127340986",
        "upstreamOrderStatusMessage": "申请成功"
      }
    };
  },

  computed: {
    // 订单状态文字
    orderStatusText() {
      const statusMap = {
        '-1': '失败',
        '0': '申请成功',
        '1': '申请中',
        '2': '已发货',
        '4': '已激活'
      };
      return statusMap[this.xqdata.orderStatus] || '未知状态';
    },

    // 订单状态样式类
    orderStatusClass() {
      const statusClassMap = {
        '-1': 'status-error',
        '0': 'status-success',
        '1': 'status-warning',
        '2': 'status-info',
        '4': 'status-success'
      };
      return statusClassMap[this.xqdata.orderStatus] || 'status-default';
    },

    // 订单来源文字
    orderSourceText() {
      const sourceMap = {
        '0': '信息流',
        '1': '合作方API进单',
        '2': '导单',
        '3': '重推'
      };
      return sourceMap[this.xqdata.orderSource] || '未知来源';
    },

    // 运营商文字
    operatorTypeText() {
      const operatorMap = {
        '0': '中国移动',
        '1': '中国电信',
        '2': '中国联通',
        '3': '中国广电'
      };
      return operatorMap[this.xqdata.operatorType] || '未知运营商';
    },

    // 结算模式文字
    productTypeText() {
      const typeMap = {
        '0': '日结秒返',
        '1': '月结产品',
        '2': '长期产品',
        '3': '其它',
        '4': '组合返佣'
      };
      return typeMap[this.xqdata.productType] || '未知模式';
    },

    // 是否首充文字
    isRechargedText() {
      return this.xqdata.isRecharged === 1 ? '已充值' : '未充值';
    },

    // 佣金状态文字
    commissionStatusText() {
      const statusMap = {
        '0': '未到结算状态',
        '1': '待结算',
        '3': '已结算',
        '4': '无法结算'
      };
      return statusMap[this.xqdata.orderCommissionStatus] || '未知状态';
    },

    // 佣金状态样式类
    commissionStatusClass() {
      const classMap = {
        '0': 'text-gray',
        '1': 'text-warning',
        '3': 'text-success',
        '4': 'text-error'
      };
      return classMap[this.xqdata.orderCommissionStatus] || 'text-gray';
    },

    // 订单信息列表
    orderInfoList() {
      return [
        {
          label: '订单来源',
          value: this.orderSourceText,
          valueClass: 'text-primary'
        },
        {
          label: '系统订单号',
          value: this.xqdata.orderUpstreamId || '暂无'
        },
        {
          label: '产品名称',
          value: this.xqdata.productName || '暂无'
        },
        {
          label: '运营商',
          value: this.operatorTypeText,
          valueClass: 'text-primary'
        },
        {
          label: '代理商名称',
          value: this.xqdata.showDownstreamName || '暂无'
        },
        {
          label: '下单时间',
          value: this.formatTimestamp(this.xqdata.createTime)
        }
      ];
    },

    // 开卡人信息列表
    cardHolderInfoList() {
      return [
        {
          label: '证件姓名',
          value: this.xqdata.cardName || '暂无'
        },
        {
          label: '证件号码',
          value: this.xqdata.cardId || '暂无'
        },
        {
          label: '联系电话',
          value: this.xqdata.cardPhone || '暂无'
        },
        {
          label: '省市区',
          value: `${this.xqdata.provinceName || ''}${this.xqdata.cityName || ''}${this.xqdata.countyName || ''}` || '暂无'
        },
        {
          label: '收货地址',
          value: this.xqdata.cardAddress || '暂无'
        }
      ];
    },

    // 订单状态信息列表
    orderStatusInfoList() {
      return [
        {
          label: '结算模式',
          value: this.productTypeText,
          valueClass: 'text-primary'
        },
        {
          label: '订单状态',
          value: this.orderStatusText,
          valueClass: this.orderStatusClass
        },
        {
          label: '是否首充',
          value: this.isRechargedText,
          valueClass: this.xqdata.isRecharged === 1 ? 'text-success' : 'text-gray'
        },
        {
          label: '首充金额',
          value: this.xqdata.rechargeAmount ? `${this.xqdata.rechargeAmount}元` : '暂无'
        },
        {
          label: '佣金状态',
          value: this.commissionStatusText,
          valueClass: this.commissionStatusClass
        },
        {
          label: '佣金说明',
          value: this.xqdata.orderCommissionMessage || '暂无'
        }
      ];
    },

    // 生产信息列表
    productionInfoList() {
      return [
        {
          label: '生产号码',
          value: this.xqdata.accNumber || '暂无'
        },
        {
          label: '物流名称',
          value: this.xqdata.express || '暂无'
        },
        {
          label: '物流单号',
          value: this.xqdata.trackingNumber || '暂无'
        },
        {
          label: '发货时间',
          value: this.xqdata.deliveryTime ? this.formatTimestamp(this.xqdata.deliveryTime) : '暂无'
        },
        {
          label: '激活时间',
          value: this.xqdata.activeTime ? this.formatTimestamp(this.xqdata.activeTime) : '暂无'
        },
        {
          label: '失败原因',
          value: this.xqdata.orderMessage || '暂无'
        }
      ];
    },

    // 接口信息列表
    interfaceInfoList() {
      return [
        {
          label: '接口名称',
          value: this.xqdata.upstreamApiName || '暂无'
        },
        {
          label: '接口产品',
          value: this.xqdata.upstreamProductName || '暂无'
        },
        {
          label: '接口订单号',
          value: this.xqdata.orderUpstreamId || '暂无'
        },
        {
          label: '接口状态',
          value: this.xqdata.upstreamOrderStatusMessage || '暂无'
        }
      ];
    },

    // 是否有接口信息
    hasInterfaceInfo() {
      return this.xqdata.upstreamApiName || this.xqdata.upstreamProductName ||
             this.xqdata.orderUpstreamId || this.xqdata.upstreamOrderStatusMessage;
    }
  },

  onLoad(options) {
    if (options.key) {
      this.xqdata = JSON.parse(decodeURIComponent(options.key));
    }
  },

  methods: {
    // 时间戳转换
    formatTimestamp(timestamp) {
      if (!timestamp) return '暂无';
      const date = new Date(timestamp);
      const year = date.getFullYear();
      const month = ("0" + (date.getMonth() + 1)).slice(-2);
      const day = ("0" + date.getDate()).slice(-2);
      const hours = ("0" + date.getHours()).slice(-2);
      const minutes = ("0" + date.getMinutes()).slice(-2);
      return `${year}-${month}-${day} ${hours}:${minutes}`;
    }
  }
}
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

// 订单状态突出显示区域
.status-highlight {
  background-color: #fff;
  padding: 30rpx;
  margin-bottom: 20rpx;
  border-radius: 8rpx;
  text-align: center;

  .status-badge {
    display: inline-block;
    padding: 12rpx 24rpx;
    border-radius: 30rpx;
    font-size: 32rpx;
    font-weight: 500;
    margin-bottom: 20rpx;

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

  .order-id {
    font-size: 28rpx;
    color: #666;
  }
}

// 信息卡片
.info-card {
  background-color: #fff;
  border-radius: 8rpx;
  margin-bottom: 20rpx;
  overflow: hidden;

  // 卡片头部
  .card-header {
    display: flex;
    align-items: center;
    padding: 30rpx;
    background-color: #fafbfc;
    border-bottom: 1rpx solid #eaeef1;

    .iconfont {
      font-size: 32rpx;
      color: #f09b7f;
      margin-right: 16rpx;
    }

    .card-title {
      font-size: 32rpx;
      font-weight: 500;
      color: #333;
    }
  }

  // 信息项
  .info-item {
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
      width: 160rpx;
    }

    .item-right {
      display: flex;
      align-items: center;
      flex: 1;
      justify-content: flex-end;

      .item-value {
        font-size: 32rpx;
        color: #666;
        text-align: right;
        line-height: 1.4;

        &.text-primary {
          color: #f09b7f;
        }

        &.text-success {
          color: #52c41a;
        }

        &.text-warning {
          color: #faad14;
        }

        &.text-error {
          color: #f5222d;
        }

        &.text-info {
          color: #909399;
        }

        &.text-gray {
          color: #999;
        }

        &.status-success {
          color: #52c41a;
        }

        &.status-error {
          color: #f5222d;
        }

        &.status-warning {
          color: #faad14;
        }

        &.status-info {
          color: #909399;
        }

        &.status-default {
          color: #666;
        }
      }
    }
  }

  // 产品图片展示
  .product-image {
    padding: 30rpx;

    .product-img {
      width: 100%;
      max-height: 400rpx;
      border-radius: 8rpx;
      background-color: #f5f6f7;
    }
  }
}

// 响应式适配
@media screen and (max-width: 750rpx) {
  .info-item {
    .item-left {
      width: 140rpx !important;
      font-size: 30rpx !important;
    }

    .item-right {
      .item-value {
        font-size: 30rpx !important;
      }
    }
  }

  .card-header {
    .card-title {
      font-size: 30rpx !important;
    }
  }

  .status-highlight {
    .status-badge {
      font-size: 30rpx !important;
    }

    .order-id {
      font-size: 26rpx !important;
    }
  }
}
</style>