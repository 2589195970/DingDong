<template>
  <view class="page-container">
    <app-navbar title="个人信息"></app-navbar>
    <view class="container">
      <!-- 头像展示区域（可选简化版） -->
      <view class="profile-header" v-if="user.nickName">
        <view class="avatar-section">
          <view v-if="!user.avatar" class="cu-avatar round bg-white">
            <view class="iconfont icon-people text-gray"></view>
          </view>
          <image v-if="user.avatar" :src="user.avatar" class="cu-avatar round" mode="aspectFill"></image>
        </view>
        <view class="user-info">
          <view class="user-name">{{ user.nickName }}</view>
        </view>
      </view>

      <!-- 主信息卡片 -->
      <view class="info-card">
        <!-- 基本信息项 -->
        <view class="info-item" v-for="(item, index) in allInfoList" :key="index" :class="{ 'has-border': index < allInfoList.length - 1 }">
          <view class="item-left">{{ item.label }}</view>
          <view class="item-right">
            <text class="item-value" :class="item.valueClass">{{ item.value || '暂无' }}</text>
            <view class="arrow-icon" v-if="item.clickable">
              <text class="iconfont icon-right"></text>
            </view>
          </view>
        </view>

        <!-- 操作项 -->
        <view class="info-item has-border" @click="handleEditProfile">
          <view class="item-left">编辑资料</view>
          <view class="item-right">
            <view class="arrow-icon">
              <text class="iconfont icon-right"></text>
            </view>
          </view>
        </view>

        <view class="info-item" @click="handleChangePassword">
          <view class="item-left">修改密码</view>
          <view class="item-right">
            <view class="arrow-icon">
              <text class="iconfont icon-right"></text>
            </view>
          </view>
        </view>
      </view>

      <!-- 退出登录 -->
      <view class="logout-button" @click="handleLogout">
        <text class="logout-text">退出登录</text>
      </view>
    </view>
  </view>
</template>

<script>
import { getUserProfile } from "@/api/system/user"

export default {
  name: 'UserInfo',
  data() {
    return {
      user: {},
      roleGroup: "",
      postGroup: "",
      realNameInfo: {},
      logoutButtonStyle: {
        width: '100%',
        height: '45px',
        fontSize: '16px'
      }
    }
  },
  computed: {
    // 从 Vuex 获取实名认证信息
    realNameInfo() {
      return this.$store.state.user.realNameInfo || {}
    },

    // 格式化实名状态显示
    realNameStatusText() {
      const status = this.realNameInfo.realNameStatus
      const statusMap = {
        0: '未认证',
        1: '认证中',
        2: '已认证',
        3: '认证失败'
      }
      return statusMap[status] || '未认证'
    },

    // 实名状态样式类
    realNameStatusClass() {
      const status = this.realNameInfo.realNameStatus
      const classMap = {
        0: 'text-gray',
        1: 'text-warning',
        2: 'text-success',
        3: 'text-danger'
      }
      return classMap[status] || 'text-gray'
    },

    allInfoList() {
      return [
        {
          label: '昵称',
          value: this.user.nickName,
          clickable: false
        },
        {
          label: '实名状态',
          value: this.realNameStatusText,
          valueClass: this.realNameStatusClass,
          clickable: false
        },
        // 只有已认证时才显示脱敏信息
        ...(this.realNameInfo.realNameStatus === 2 ? [
          {
            label: '真实姓名',
            value: this.realNameInfo.realName,
            clickable: false
          },
          {
            label: '身份证号',
            value: this.realNameInfo.cardId,
            clickable: false
          },
          {
            label: '认证时间',
            value: this.realNameInfo.auditTime,
            clickable: false
          }
        ] : []),
        {
          label: '手机号码',
          value: this.user.phonenumber ?
            this.user.phonenumber.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2') : '暂无',
          clickable: false
        }
      ]
    }
  },
  onLoad() {
    this.getUser()
  },
  methods: {
    async getUser() {
      try {
        const response = await getUserProfile()
        this.user = response.data
        this.roleGroup = response.roleGroup
        this.postGroup = response.postGroup

        // 更新 Vuex 中的实名认证信息
        if (response.realNameInfo) {
          this.$store.commit('SET_REALNAME_INFO', response.realNameInfo)
        }
      } catch (error) {
        this.$modal.showToast('获取用户信息失败')
        console.error('获取用户信息失败:', error)
      }
    },
    handleEditProfile() {
      this.$tab.navigateTo('/pages/mine/info/edit')
    },
    handleChangePassword() {
      this.$tab.navigateTo('/pages/mine/pwd/index')
    },
    handleLogout() {
      this.$modal.confirm('确定退出系统吗？').then(() => {
        this.$store.dispatch('LogOut').then(() => {
          this.$modal.showToast('退出成功')
        }).finally(() => {
          this.$tab.reLaunch('/pages/index')
        })
      }).catch(() => {
        // 用户取消退出
      })
    }
  }
}
</script>

<style lang="scss" scoped>
page {
  background-color: #f5f6f7;
}

.container {
  padding: 15px;
}

/* 简化的头像区域 */
.profile-header {
  background-color: #fff;
  padding: 20rpx 30rpx;
  margin-bottom: 20rpx;
  border-radius: 8rpx;
  display: flex;
  align-items: center;

  .avatar-section {
    margin-right: 20rpx;
  }

  .user-info {
    flex: 1;

    .user-name {
      font-size: 32rpx;
      font-weight: 500;
      color: #333;
    }
  }
}

/* 主信息卡片 */
.info-card {
  background-color: #fff;
  border-radius: 8rpx;
  overflow: hidden;
}

/* 信息项样式 */
.info-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 26rpx 30rpx;
  min-height: 88rpx;
  box-sizing: border-box;

  &.has-border {
    border-bottom: 1rpx solid #eaeef1;
  }

  .item-left {
    font-size: 32rpx;
    color: #333;
    flex-shrink: 0;
  }

  .item-right {
    display: flex;
    align-items: center;
    flex: 1;
    justify-content: flex-end;

    .item-value {
      font-size: 32rpx;
      color: #666;
      margin-right: 12rpx;

      &.text-primary {
        color: #f09b7f;
      }

      &.text-success {
        color: #19be6b;
      }

      &.text-warning {
        color: #ff9900;
      }

      &.text-danger {
        color: #fa3534;
      }

      &.text-gray {
        color: #909399;
      }
    }

    .arrow-icon {
      .iconfont {
        font-size: 24rpx;
        color: #c0c0c0;
      }
    }
  }

  /* 可点击项的样式 */
  &:active {
    background-color: #f8f9fa;
  }
}

/* 退出登录按钮 */
.logout-button {
  background-color: #fff;
  margin: 30rpx 0;
  padding: 26rpx 30rpx;
  border-radius: 8rpx;
  text-align: center;

  &:active {
    background-color: #f8f9fa;
  }

  .logout-text {
    font-size: 32rpx;
    color: #333;
  }
}
</style>
