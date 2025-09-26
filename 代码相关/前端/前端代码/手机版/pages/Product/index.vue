<template>
  <view class="page-container">
    <!-- 搜索栏 -->
    <view class="search-container">
      <u-search
        :showAction="false"
        v-model="queryParams.productName"
        @search="soplist"
        @clickIcon="soplist"
        placeholder="搜索产品..."
        :height="35"
        bgColor="#f5f6f7"
        borderColor="#f09b7f"
        searchIconColor="#f09b7f">
      </u-search>
    </view>
    <!-- 运营商选择器 -->
    <view class="operator-tabs">
      <view class="tab-bar">
        <view v-for="(item, index) in list" :key="index" class="tab-item"
              :class="{ 'active': current === index }" @click="click(index)">
          <image :src="item.icon" class="tab-icon"/>
          <text class="tab-text">{{ item.name }}</text>
        </view>
        <!-- 底部滑动条 -->
        <view class="slider" :style="sliderStyle"></view>
      </view>
    </view>
    <!-- 筛选区域 -->
    <view class="filter-container">
      <u-collapse :value="['Docs guide']">
        <u-collapse-item title="筛选" name="Docs guide">
          <u-tabs :list="list1" lineWidth="30" :scrollable="false" lineColor="#f09b7f" @click="click1"></u-tabs>
          <u-tabs :list="list2" lineWidth="30" :scrollable="false" lineColor="#f09b7f" @click="click2"></u-tabs>
        </u-collapse-item>
      </u-collapse>
    </view>
    <!-- 加载页面 -->
    <u-loading-page :loading="loading"></u-loading-page>

    <!-- 产品列表 -->
    <view class="product-list">
      <view class="product-card" v-for="dict in productList" :key="dict.productId">
        <!-- 左侧图片区域 -->
        <view class="product-image-section">
          <image :src="dict.productMasterMap" class="product-image" mode="aspectFit"></image>
          <!-- 左侧底部佣金信息 -->
          <view class="image-bottom-info">
            <view class="commission-area-left">
              <text class="commission-price-left">¥{{ dict.productCommission }}</text>
              <text class="price-label-left">我的佣金</text>
            </view>
          </view>
        </view>

        <!-- 右侧信息区域 -->
        <view class="product-info-section">
          <!-- 商品标题区域 -->
          <view class="product-title-area">
            <text class="product-title">{{ dict.productName }}</text>
            <text class="product-id-text">ID: {{ dict.productId }}</text>
          </view>

          <!-- 产品特色描述 -->
          <view class="product-features">
            <text class="features-text">
              {{ dict.productTyll }}GB通用流量 + {{ dict.productDxll }}GB定向 | {{ dict.productThfz }}分钟通话 | {{ dict.productGsdq }}
            </text>
          </view>

          <!-- 标签区域 -->
          <view class="tags-area">
            <view class="product-tag" :class="getProductTypeClass(dict.productType)">
              <text class="tag-text">{{ getProductTypeText(dict.productType) }}</text>
            </view>
            <view class="product-tag" :class="getProductStatusClass(dict.productStatus)">
              <text class="tag-text">{{ getProductStatusText(dict.productStatus) }}</text>
            </view>
            <view class="product-tag age-tag">
              <text class="tag-text">{{ dict.productAgeMin }}-{{ dict.productAgeMax }}岁</text>
            </view>
          </view>

          <!-- 底部操作区域 -->
          <view class="bottom-actions">
            <view class="left-info">
              <!-- 左侧信息区域，可以放置其他信息 -->
            </view>
            <view class="right-buttons">
              <view class="action-btn secondary" @click="xianqing(dict)">
                <text class="btn-text">详情</text>
              </view>
              <view class="action-btn primary" @click="showTrue(dict)">
                <text class="btn-text">推广</text>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>
    <!-- 二维码模态框 -->
    <u-modal :show="commissionopen" @confirm="confirm" ref="uModal" @cancel="cancel" :showCancelButton='true'
             confirmText="保存" :asyncClose="true">
      <view class="qrcode-container">
        <image :src="fzhi.productQrcodeMap" class="qrcode-image"/>
      </view>
    </u-modal>
    <!-- 推广选项弹窗 -->
    <u-popup :show="show" @close="close" @open="open" :round="10">
      <view class="popup-content">
        <view class="popup-item" @click="dakalinjie(fzhi)">
          <text class="popup-text">打开链接</text>
        </view>
        <view class="popup-item" @click="handlefuzhi(fzhi)">
          <text class="popup-text">复制链接</text>
        </view>
        <view class="popup-item" @click="commissionOpoe">
          <text class="popup-text">二维码</text>
        </view>
      </view>
    </u-popup>
  </view>
</template>

<script>
import {
  agentSelectProductListPage,
  updateProductCommission
} from "@/api/product/product";
import USearch from "../../uview-ui/components/u-search/u-search.vue";

export default {
  components: {USearch},
  data() {
    return {
      value: '',
      current: 0,
      list: [{
        name: '全部',
        id: null,
        icon: require('@/static/images/home/qb.png'),
      }, {
        name: '移动',
        icon: require('@/static/images/home/yd.png'),
        id: 0
      }, {
        name: '电信',
        icon: require('@/static/images/home/dx.png'),
        id: 1
      }, {
        name: '联通',
        icon: require('@/static/images/home/lt.png'),
        id: 2
      }, {
        name: '广电',
        icon: require('@/static/images/home/gd.png'),
        id: 3
      },],
      list1: [{
        name: '全部',
        id: null
      }, {
        name: '月结',
        id: 1
      }, {
        name: '秒返',
        id: 0
      },],
      list2: [{
        name: '全部',
        id: null
      }, {
        name: '上架中',
        id: 1
      }, {
        name: '已下架',
        id: 0
      },],
      productList: [],
      keyword: '遥看瀑布挂前川',
      show: false,
      loading: false,
      fzhi: '',
      queryParams: {
        pageNo: 1,
        pageSize: 10000,
        productType: 1,
      },
      commissionopen: false,
      frme: {},
    }
  },
  computed: {
    // 动态计算滑动条位置
    sliderStyle() {
      return {
        width: `${100 / this.list.length}%`,
        transform: `translateX(${100 * this.current}%)`
      }
    }
  },
  created() {
    this.soplist();
  },
  methods: {
    // 获取产品类型文字
    getProductTypeText(type) {
      const typeMap = {
        0: '日结秒返',
        1: '月结产品',
        2: '长期产品',
        3: '其它',
        4: '组合返佣'
      }
      return typeMap[type] || '未知'
    },

    // 获取产品类型样式类
    getProductTypeClass(type) {
      const classMap = {
        0: 'type-instant',
        1: 'type-monthly',
        2: 'type-long',
        3: 'type-other',
        4: 'type-combo'
      }
      return classMap[type] || 'type-default'
    },

    // 获取产品状态文字
    getProductStatusText(status) {
      const statusMap = {
        0: '已下架',
        1: '上架中'
      }
      return statusMap[status] || '未知'
    },

    // 获取产品状态样式类
    getProductStatusClass(status) {
      const classMap = {
        0: 'status-offline',
        1: 'status-online'
      }
      return classMap[status] || 'status-default'
    },

    handleewma() {

    },
    cancel() {
      this.commissionopen = false
    },
    confirm() {
      // 图片地址（需允许跨域访问）
      const url = this.fzhi.productQrcodeMap;

      // 创建隐藏的 <a> 标签
      const link = document.createElement('a');
      link.href = url;
      link.download = 'img.jpg'; // 设置下载文件名
      document.body.appendChild(link);

      // 触发点击下载
      link.click();

      // 清理 DOM
      document.body.removeChild(link);
      this.commissionopen = false

    },
    commissionOpoe() {
      this.show = false;
      this.commissionopen = true;
    },
    showTrue(data) {
      this.fzhi = data;
      this.show = true;
    },
    dakalinjie(data) {
      window.open(data.h5Url);
    },
    handlefuzhi(data) {
      const textToCopy = data.h5Url;

      // 现代剪贴板API
      if (navigator.clipboard) {
        navigator.clipboard.writeText(textToCopy)
            .then(() => {
              this.$modal.msgSuccess("复制成功");
            })
            .catch(() => {
              // 现代API失败时降级处理
              this.copyWithExecCommand(textToCopy);
            });
      } else {
        // 旧浏览器直接使用execCommand
        this.copyWithExecCommand(textToCopy);
      }
    },

    // 封装传统复制方法
    copyWithExecCommand(text) {
      // 创建临时文本域
      const textArea = document.createElement('textarea');
      textArea.value = text;
      textArea.style.position = 'fixed'; // 避免滚动
      textArea.style.opacity = 0; // 透明

      // 添加到DOM并选中
      document.body.appendChild(textArea);
      textArea.select();

      try {
        // 执行复制命令
        const success = document.execCommand('copy');
        if (success) {
          this.$modal.msgSuccess("复制成功");
        } else {
          this.$modal.msgError("复制失败，请手动复制");
        }
      } catch (err) {
        this.$modal.msgError("复制失败，请手动复制");
      } finally {
        // 清理DOM
        document.body.removeChild(textArea);
      }
    },
    xianqing(data) {
      uni.navigateTo({
        url: `/pages/Product/detail?key=${encodeURIComponent(JSON.stringify(data))}`
      })
    },
    open() {
      // console.log('open');
    },
    close() {
      this.show = false
      // console.log('close');
    },
    click(item) {
      console.log(item);
      this.current = item

      if (item == 0) {
        this.queryParams.operatorType = null;
      } else {
        this.queryParams.operatorType = item - 1;
      }

      this.soplist()
    },
    click2(item) {
      this.queryParams.productStatus = item.id;
      this.soplist()
    },
    click1(item) {
      console.log(item);
      this.queryParams.productType = item.id;
      this.soplist()
    },
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    },
    soplist() {
      this.loading = true;
      agentSelectProductListPage(this.queryParams).then(res => {

        this.productList = res.data.rows;
        this.loading = false;
      })
    },
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

// 搜索栏容器
.search-container {
  background-color: #fff;
  padding: 30rpx 20rpx;
}

// 运营商选择器
.operator-tabs {
  background-color: #fff;

  .tab-bar {
    display: flex;
    position: relative;
    padding: 15rpx 0;
  }

  .tab-item {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    transition: all 0.3s;

    .tab-icon {
      width: 60rpx;
      height: 60rpx;
      margin-bottom: 16rpx;
    }

    .tab-text {
      font-size: 26rpx;
      color: #666;
    }

    &.active .tab-text {
      color: #f09b7f;
      font-weight: bold;
    }
  }

  .slider {
    position: absolute;
    bottom: 0;
    height: 6rpx;
    background: #f09b7f;
    border-radius: 3rpx;
    transition: transform 0.3s ease;
  }
}

// 筛选区域
.filter-container {
  background-color: #fff;
  margin-bottom: 20rpx;
}

// 产品列表
.product-list {
  padding: 0 20rpx;
}

// 产品卡片（电商风格左图右文布局）
.product-card {
  display: flex;
  background-color: #fff;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
  padding: 24rpx;
  overflow: hidden;
  transition: transform 0.2s ease;

  &:active {
    transform: scale(0.98);
  }
}

// 左侧图片区域
.product-image-section {
  width: 200rpx;
  flex-shrink: 0;
  margin-right: 24rpx;
  display: flex;
  flex-direction: column;

  .product-image {
    width: 100%;
    height: 140rpx;
    border-radius: 12rpx;
    background-color: #f5f6f7;
    margin-bottom: 12rpx;
  }

  .image-bottom-info {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: flex-end;

    .commission-area-left {
      display: flex;
      flex-direction: column;
      align-items: center;
      text-align: center;

      .commission-price-left {
        font-size: 45rpx;
        font-weight: bold;
        color: #f09b7f;
        margin-bottom: 4rpx;
      }

      .price-label-left {
        font-size: 20rpx;
        color: #666;
      }
    }
  }
}

// 右侧信息区域
.product-info-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  min-height: 200rpx;
}

// 商品标题区域
.product-title-area {
  margin-bottom: 16rpx;

  .product-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
    line-height: 1.4;
    display: block;
    margin-bottom: 8rpx;
  }

  .product-id-text {
    font-size: 24rpx;
    color: #999;
  }
}

// 产品特色描述
.product-features {
  margin-bottom: 16rpx;

  .features-text {
    font-size: 26rpx;
    color: #666;
    line-height: 1.4;
  }
}

// 佣金价格区域
.price-area {
  display: flex;
  align-items: baseline;
  margin-bottom: 16rpx;

  .commission-price {
    font-size: 36rpx;
    font-weight: bold;
    color: #f09b7f;
    margin-right: 12rpx;
  }

  .price-label {
    font-size: 24rpx;
    color: #999;
  }
}

// 标签区域（三个标签统一显示）
.tags-area {
  display: flex;
  flex-wrap: wrap;
  gap: 8rpx;
  margin-bottom: 20rpx;

  .product-tag {
    padding: 6rpx 12rpx;
    border-radius: 4rpx;
    background-color: #f09b7f;

    .tag-text {
      font-size: 22rpx;
      color: #fff;
      font-weight: 500;
    }

    // 产品类型样式
    &.type-instant {
      background-color: #f5222d;
    }

    &.type-monthly {
      background-color: #52c41a;
    }

    &.type-long {
      background-color: #f5222d;
    }

    &.type-other {
      background-color: #52c41a;
    }

    &.type-combo {
      background-color: #f5222d;
    }

    // 产品状态样式
    &.status-online {
      background-color: #52c41a;
    }

    &.status-offline {
      background-color: #f5222d;
    }

    // 年龄标签样式
    &.age-tag {
      background-color: #faad14;
    }
  }
}

// 底部操作区域
.bottom-actions {
  display: flex;
  align-items: center;
  justify-content: space-between;

  .left-info {
    flex: 1;
  }

  .right-buttons {
    display: flex;
    gap: 12rpx;
  }

  .action-btn {
    padding: 12rpx 24rpx;
    border-radius: 8rpx;
    transition: all 0.2s ease;

    .btn-text {
      font-size: 26rpx;
      font-weight: 500;
    }

    &.primary {
      background-color: #f09b7f;

      .btn-text {
        color: #fff;
      }

      &:active {
        background-color: #d87d63;
      }
    }

    &.secondary {
      background-color: #f5f6f7;
      border: 1rpx solid #e0e0e0;

      .btn-text {
        color: #666;
      }

      &:active {
        background-color: #e6e8ea;
      }
    }
  }
}

// 二维码模态框
.qrcode-container {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40rpx;

  .qrcode-image {
    width: 600rpx;
    height: 600rpx;
    border-radius: 12rpx;
  }
}

// 推广选项弹窗
.popup-content {
  padding: 40rpx;

  .popup-item {
    padding: 40rpx 0;
    border-bottom: 1rpx solid #eaeef1;
    text-align: center;

    &:last-child {
      border-bottom: none;
    }

    &:active {
      background-color: #f5f6f7;
    }

    .popup-text {
      font-size: 32rpx;
      color: #333;
    }
  }
}

// 响应式适配
@media screen and (max-width: 750rpx) {
  .product-card {
    padding: 20rpx;
  }

  .product-image-section {
    width: 160rpx;
    margin-right: 20rpx;

    .product-image {
      height: 120rpx;
    }

    .image-bottom-info {
      .commission-area-left {
        .commission-price-left {
          font-size: 38rpx;
        }

        .price-label-left {
          font-size: 18rpx;
        }
      }
    }
  }

  .product-info-section {
    min-height: 160rpx;
  }

  .product-title-area {
    .product-title {
      font-size: 28rpx;
    }

    .product-id-text {
      font-size: 22rpx;
    }
  }

  .product-features {
    .features-text {
      font-size: 24rpx;
    }
  }

  .price-area {
    .commission-price {
      font-size: 32rpx;
    }

    .price-label {
      font-size: 22rpx;
    }
  }

  .tags-area {
    gap: 6rpx;

    .product-tag {
      padding: 4rpx 8rpx;

      .tag-text {
        font-size: 26rpx;
      }
    }
  }

  .bottom-actions {
    .action-btn {
      padding: 10rpx 20rpx;

      .btn-text {
        font-size: 24rpx;
      }
    }
  }
}

// 超小屏幕适配
@media screen and (max-width: 600rpx) {
  .product-image-section {
    width: 140rpx;

    .product-image {
      height: 100rpx;
    }

    .image-bottom-info {
      .commission-area-left {
        .commission-price-left {
          font-size: 32rpx;
        }

        .price-label-left {
          font-size: 16rpx;
        }
      }
    }
  }

  .product-info-section {
    min-height: 140rpx;
  }

  .product-title-area {
    .product-title {
      font-size: 26rpx;
      line-height: 1.3;
    }
  }

  .tags-area {
    margin-bottom: 16rpx;
  }

  .right-buttons {
    gap: 8rpx;
  }

  .action-btn {
    padding: 8rpx 16rpx;

    .btn-text {
      font-size: 22rpx;
    }
  }
}
</style>