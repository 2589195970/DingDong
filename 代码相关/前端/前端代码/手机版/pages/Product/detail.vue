<template>
  <view class="page-container">
    <app-navbar title="商品详情"></app-navbar>

    <view class="content-container">
      <!-- 产品基本信息卡片 -->
      <view class="info-card">
        <view class="card-header">
          <u-icon name="star-fill" color="#f09b7f"></u-icon>
          <text class="card-title">产品套餐</text>
        </view>
        <view class="card-content">
          <text class="product-name">{{ xqdata.productName }}</text>
          <view class="description-box">
            <text class="description-text">{{ xqdata.productTcjs || '暂无套餐介绍' }}</text>
          </view>
        </view>
      </view>

      <!-- 快递方式卡片 -->
      <view class="info-card">
        <view class="card-header">
          <u-icon name="star-fill" color="#f09b7f"></u-icon>
          <text class="card-title">快递方式</text>
        </view>
        <view class="card-content">
          <view class="description-box">
            <text class="description-text">{{ xqdata.productFafs || '暂无快递信息' }}</text>
          </view>
        </view>
      </view>

      <!-- 办卡年龄卡片 -->
      <view class="info-card">
        <view class="card-header">
          <u-icon name="star-fill" color="#f09b7f"></u-icon>
          <text class="card-title">办卡年龄</text>
        </view>
        <view class="card-content">
          <view class="description-box">
            <text class="description-text">{{ xqdata.productAgeMin }}-{{ xqdata.productAgeMax }}岁</text>
          </view>
        </view>
      </view>

      <!-- 推广要求卡片 -->
      <view class="info-card">
        <view class="card-header">
          <u-icon name="star-fill" color="#f09b7f"></u-icon>
          <text class="card-title">推广要求</text>
        </view>
        <view class="card-content">
          <view class="description-box">
            <text class="description-text">{{ xqdata.productDemand || '暂无特殊要求' }}</text>
          </view>
        </view>
      </view>

      <!-- 产品详情图卡片 -->
      <view class="info-card" v-if="xqdata.productDetailMap">
        <view class="card-header">
          <u-icon name="star-fill" color="#f09b7f"></u-icon>
          <text class="card-title">产品详情</text>
        </view>
        <view class="card-content">
          <view class="detail-image-container">
            <image
              :src="xqdata.productDetailMap"
              class="detail-image"
              mode="widthFix"
              @error="handleImageError">
            </image>
          </view>
        </view>
      </view>

      <!-- 空状态提示 -->
      <view class="empty-state" v-if="!hasProductData">
        <u-icon name="info-circle" color="#909399" size="60"></u-icon>
        <text class="empty-text">暂无产品详情信息</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      xqdata: {
        productId: null,
        agentProductId: null,
        productCode: '',
        productName: '',
        operatorType: null,
        productType: null,
        productDemand: '',
        productMasterMap: null,
        productTemplateJson: '',
        productStatus: null,
        productCommission: 0,
        revenueProductCommission: 0,
        distributionProductCommission: 0,
        productSort: 0,
        createTime: null,
        updateTime: null,
        productDetailMap: '',
        productTcjs: '',
        productFafs: '',
        productAgeMin: null,
        productAgeMax: null,
        h5Url: '',
        agentCodeList: []
      }
    }
  },
  computed: {
    // 检查是否有产品数据
    hasProductData() {
      return this.xqdata.productName &&
             (this.xqdata.productTcjs ||
              this.xqdata.productFafs ||
              this.xqdata.productDemand ||
              this.xqdata.productDetailMap)
    }
  },
  onLoad(options) {
    if (options.key) {
      try {
        this.xqdata = JSON.parse(decodeURIComponent(options.key));
        console.log('产品详情数据：', this.xqdata);
      } catch (error) {
        console.error('解析产品数据失败：', error);
        uni.showToast({
          title: '数据解析失败',
          icon: 'error'
        });
      }
    }
  },
  methods: {
    // 处理图片加载错误
    handleImageError() {
      uni.showToast({
        title: '图片加载失败',
        icon: 'error'
      });
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

// 内容容器
.content-container {
  padding: 20rpx;
}

// 信息卡片（单卡片多项目设计模式）
.info-card {
  background-color: #fff;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
  overflow: hidden;
}

// 卡片头部
.card-header {
  display: flex;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #eaeef1;

  .card-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
    margin-left: 16rpx;
  }
}

// 卡片内容
.card-content {
  padding: 30rpx;

  .product-name {
    font-size: 36rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 20rpx;
    display: block;
  }

  .description-box {
    background-color: #fff2ed;
    border: 2rpx solid #f09b7f;
    border-radius: 12rpx;
    padding: 30rpx;

    .description-text {
      font-size: 28rpx;
      color: #333;
      line-height: 1.6;
    }
  }

  .detail-image-container {
    border-radius: 12rpx;
    overflow: hidden;

    .detail-image {
      width: 100%;
      display: block;
      border-radius: 12rpx;
    }
  }
}

// 空状态
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 120rpx 40rpx;
  margin-top: 100rpx;

  .empty-text {
    font-size: 28rpx;
    color: #909399;
    margin-top: 30rpx;
  }
}

// 响应式适配
@media screen and (max-width: 750rpx) {
  .card-header {
    padding: 24rpx;

    .card-title {
      font-size: 30rpx;
    }
  }

  .card-content {
    padding: 24rpx;

    .product-name {
      font-size: 32rpx;
    }

    .description-box {
      padding: 24rpx;

      .description-text {
        font-size: 26rpx;
      }
    }
  }
}

// 特殊样式适配
// 修复某些情况下图片显示问题
.detail-image {
  max-width: 100%;
  height: auto;
}

// 卡片悬浮效果（可选）
.info-card {
  transition: transform 0.2s ease;

  &:active {
    transform: translateY(2rpx);
  }
}

// 统一文字选择样式
.description-text {
  user-select: text;
  -webkit-user-select: text;
}

// 优化图标和文字的对齐
.card-header {
  .card-title {
    line-height: 1.2;
  }
}
</style>