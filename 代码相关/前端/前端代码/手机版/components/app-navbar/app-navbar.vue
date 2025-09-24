<template>
  <view class="app-navbar">
    <u-navbar
      :title="title"
      :safeAreaInsetTop="true"
      :fixed="true"
      :placeholder="true"
      :border="false"
      :bgColor="bgColor"
      :leftIcon="leftIcon"
      :leftText="leftText"
      :leftIconColor="leftIconColor"
      :leftIconSize="leftIconSize"
      :autoBack="autoBack"
      :titleStyle="titleStyle"
      @leftClick="handleLeftClick"
    >
      <!-- 自定义右侧内容 -->
      <template #right v-if="$slots.right">
        <slot name="right"></slot>
      </template>
    </u-navbar>
  </view>
</template>

<script>
export default {
  name: 'AppNavbar',
  props: {
    // 标题
    title: {
      type: String,
      default: ''
    },
    // 背景颜色
    bgColor: {
      type: String,
      default: '#f09b7f' // 项目主题色
    },
    // 左侧图标
    leftIcon: {
      type: String,
      default: 'arrow-left'
    },
    // 左侧文字
    leftText: {
      type: String,
      default: ''
    },
    // 左侧图标颜色
    leftIconColor: {
      type: String,
      default: '#ffffff'
    },
    // 左侧图标大小
    leftIconSize: {
      type: [String, Number],
      default: 20
    },
    // 是否自动返回（禁用u-navbar的自动返回，由我们自己处理）
    autoBack: {
      type: Boolean,
      default: false
    },
    // 标题样式
    titleStyle: {
      type: [Object, String],
      default: () => ({
        color: '#ffffff',
        fontWeight: 'bold',
        fontSize: '18px'
      })
    },
    // 自定义返回行为
    customBack: {
      type: Function,
      default: null
    }
  },
  methods: {
    handleLeftClick() {
      if (this.customBack && typeof this.customBack === 'function') {
        // 如果提供了自定义返回函数，执行自定义函数
        this.customBack();
      } else {
        // 默认执行智能返回逻辑（不再依赖autoBack参数）
        this.smartNavigateBack();
      }

      // 触发自定义事件
      this.$emit('leftClick');
    },

    smartNavigateBack() {
      const pages = getCurrentPages();

      if (pages.length > 1) {
        // 有上级页面，直接返回上一页
        uni.navigateBack({
          fail: (err) => {
            // 如果返回失败，尝试智能返回
            this.fallbackNavigate();
          }
        });
      } else {
        // 没有上级页面，执行兜底逻辑
        this.fallbackNavigate();
      }
    },

    fallbackNavigate() {
      const pages = getCurrentPages();
      const currentPage = pages[pages.length - 1];
      const currentRoute = currentPage.route;

      // 根据页面路径判断应该返回的页面
      if (currentRoute.includes('/mine/')) {
        // 个人中心相关页面，返回到个人中心首页
        uni.reLaunch({
          url: '/pages/mine/index'
        });
      } else if (currentRoute.includes('/Product/')) {
        // 商品相关页面，返回到商品页面
        uni.reLaunch({
          url: '/pages/Product/index'
        });
      } else if (currentRoute.includes('/Order/')) {
        // 订单相关页面，返回到订单页面
        uni.reLaunch({
          url: '/pages/Order/index'
        });
      } else if (currentRoute.includes('/home/')) {
        // 工作台相关页面，返回到首页
        uni.reLaunch({
          url: '/pages/index'
        });
      } else if (currentRoute.includes('/notice/')) {
        // 通知相关页面，返回到首页
        uni.reLaunch({
          url: '/pages/index'
        });
      } else {
        // 其他页面，返回到首页
        uni.reLaunch({
          url: '/pages/index'
        });
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.app-navbar {
  /* 确保导航栏在最顶层 */
  position: relative;
  z-index: 1000;
}

/* 适配不同平台的样式调整 */
</style>