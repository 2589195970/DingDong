<template>
  <view class="page-container">
    <app-navbar title="修改密码"></app-navbar>
    <view class="container">
      <!-- 密码修改卡片 -->
      <view class="pwd-card">
        <view class="form-item" :class="{ 'has-border': true }">
          <view class="item-left">当前密码</view>
          <view class="item-right">
            <input
              v-model="user.oldPassword"
              :type="showOldPwd ? 'text' : 'password'"
              placeholder="请输入当前密码"
              class="form-input"
              maxlength="20"
            />
            <view class="eye-toggle" @click="toggleOldPwd">
              <text class="toggle-text">{{ showOldPwd ? '隐藏' : '显示' }}</text>
            </view>
          </view>
        </view>

        <view class="form-item" :class="{ 'has-border': true }">
          <view class="item-left">新密码</view>
          <view class="item-right">
            <input
              v-model="user.newPassword"
              :type="showNewPwd ? 'text' : 'password'"
              placeholder="请输入新密码 (6-20位)"
              class="form-input"
              maxlength="20"
            />
            <view class="eye-toggle" @click="toggleNewPwd">
              <text class="toggle-text">{{ showNewPwd ? '隐藏' : '显示' }}</text>
            </view>
          </view>
        </view>

        <view class="form-item">
          <view class="item-left">确认密码</view>
          <view class="item-right">
            <input
              v-model="user.confirmPassword"
              :type="showConfirmPwd ? 'text' : 'password'"
              placeholder="请再次确认新密码"
              class="form-input"
              maxlength="20"
            />
            <view class="eye-toggle" @click="toggleConfirmPwd">
              <text class="toggle-text">{{ showConfirmPwd ? '隐藏' : '显示' }}</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 密码强度提示 -->
      <view class="password-tips" v-if="user.newPassword">
        <view class="tips-title">密码强度：</view>
        <view class="strength-bar">
          <view
            class="strength-item"
            :class="passwordStrength >= 1 ? 'active weak' : ''"
          ></view>
          <view
            class="strength-item"
            :class="passwordStrength >= 2 ? 'active medium' : ''"
          ></view>
          <view
            class="strength-item"
            :class="passwordStrength >= 3 ? 'active strong' : ''"
          ></view>
        </view>
        <view class="strength-text">{{ strengthText }}</view>
      </view>

      <!-- 安全提示 -->
      <view class="security-tips">
        <view class="tips-title">安全提示</view>
        <view class="tips-list">
          <text>• 密码长度建议6-20位</text>
          <text>• 建议包含字母、数字和特殊字符</text>
          <text>• 不要使用过于简单的密码</text>
        </view>
      </view>

      <!-- 提交按钮 -->
      <view class="submit-button" @click="submit" :class="{ 'disabled': !canSubmit }">
        <text class="submit-text">{{ loading ? '修改中...' : '确认修改' }}</text>
      </view>
    </view>
  </view>
</template>

<script>
  import { updateUserPwd } from "@/api/system/user"

  export default {
    data() {
      return {
        user: {
          oldPassword: '',
          newPassword: '',
          confirmPassword: ''
        },
        // 密码可见性控制
        showOldPwd: false,
        showNewPwd: false,
        showConfirmPwd: false,
        // 加载状态
        loading: false
      }
    },
    computed: {
      // 密码强度计算
      passwordStrength() {
        const pwd = this.user.newPassword
        if (!pwd) return 0

        let strength = 0

        // 长度检查
        if (pwd.length >= 6) strength++

        // 包含数字和字母
        if (/(?=.*[0-9])(?=.*[a-zA-Z])/.test(pwd)) strength++

        // 包含特殊字符或长度大于8
        if (/[!@#$%^&*(),.?":{}|<>]/.test(pwd) || pwd.length > 8) strength++

        return strength
      },

      // 强度文本
      strengthText() {
        switch(this.passwordStrength) {
          case 1: return '弱'
          case 2: return '中'
          case 3: return '强'
          default: return ''
        }
      },

      // 是否可以提交
      canSubmit() {
        return this.user.oldPassword.length > 0 &&
               this.user.newPassword.length >= 6 &&
               this.user.confirmPassword === this.user.newPassword &&
               !this.loading
      }
    },
    methods: {
      // 切换旧密码可见性
      toggleOldPwd() {
        this.showOldPwd = !this.showOldPwd
      },

      // 切换新密码可见性
      toggleNewPwd() {
        this.showNewPwd = !this.showNewPwd
      },

      // 切换确认密码可见性
      toggleConfirmPwd() {
        this.showConfirmPwd = !this.showConfirmPwd
      },

      // 表单验证
      validateForm() {
        // 旧密码验证
        if (!this.user.oldPassword) {
          this.$modal.showToast('请输入当前密码')
          return false
        }

        // 新密码验证
        if (!this.user.newPassword) {
          this.$modal.showToast('请输入新密码')
          return false
        }

        if (this.user.newPassword.length < 6 || this.user.newPassword.length > 20) {
          this.$modal.showToast('新密码长度应为6-20位')
          return false
        }

        // 确认密码验证
        if (!this.user.confirmPassword) {
          this.$modal.showToast('请确认新密码')
          return false
        }

        if (this.user.newPassword !== this.user.confirmPassword) {
          this.$modal.showToast('两次输入的密码不一致')
          return false
        }

        // 新旧密码不能相同
        if (this.user.oldPassword === this.user.newPassword) {
          this.$modal.showToast('新密码不能与当前密码相同')
          return false
        }

        return true
      },

      // 提交表单
      async submit() {
        if (!this.canSubmit) return
        if (!this.validateForm()) return

        this.loading = true

        try {
          await updateUserPwd(this.user.oldPassword, this.user.newPassword)
          this.$modal.msgSuccess("密码修改成功")

          // 延迟返回，让用户看到成功提示
          setTimeout(() => {
            this.$tab.navigateBack()
          }, 1500)

        } catch (error) {
          console.error('修改密码失败:', error)

          // 根据错误信息给出不同提示
          if (error.message && error.message.includes('旧密码')) {
            this.$modal.showToast('当前密码不正确，请重新输入')
          } else {
            this.$modal.showToast('修改失败，请稍后重试')
          }
        } finally {
          this.loading = false
        }
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

/* 密码修改卡片 */
.pwd-card {
  background-color: #fff;
  border-radius: 8rpx;
  overflow: hidden;
  margin-bottom: 20rpx;
}

/* 表单项样式 */
.form-item {
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
    width: 140rpx;
  }

  .item-right {
    display: flex;
    align-items: center;
    flex: 1;
    justify-content: flex-end;

    .form-input {
      flex: 1;
      text-align: right;
      font-size: 32rpx;
      color: #333;
      border: none;
      outline: none;
      background: transparent;
      margin-right: 20rpx;

      &::placeholder {
        color: #c0c0c0;
      }
    }

    .eye-toggle {
      padding: 8rpx 16rpx;
      background-color: #f8f9fa;
      border-radius: 4rpx;

      .toggle-text {
        font-size: 24rpx;
        color: #f09b7f;
      }

      &:active {
        background-color: #f0f0f0;
      }
    }
  }
}

/* 密码强度提示 */
.password-tips {
  background-color: #fff;
  margin-bottom: 20rpx;
  padding: 20rpx 30rpx;
  border-radius: 8rpx;

  .tips-title {
    font-size: 28rpx;
    color: #333;
    margin-bottom: 15rpx;
  }

  .strength-bar {
    display: flex;
    gap: 8rpx;
    margin-bottom: 10rpx;

    .strength-item {
      flex: 1;
      height: 8rpx;
      background: #e4e7ed;
      border-radius: 4rpx;
      transition: all 0.3s;

      &.active.weak {
        background: #f56c6c;
      }

      &.active.medium {
        background: #e6a23c;
      }

      &.active.strong {
        background: #67c23a;
      }
    }
  }

  .strength-text {
    font-size: 24rpx;
    text-align: right;
    margin-top: 8rpx;
    font-weight: 500;
    color: #666;
  }
}

/* 安全提示 */
.security-tips {
  background-color: #fff;
  margin-bottom: 20rpx;
  padding: 20rpx 30rpx;
  border-radius: 8rpx;

  .tips-title {
    font-size: 28rpx;
    color: #333;
    margin-bottom: 15rpx;
  }

  .tips-list {
    display: flex;
    flex-direction: column;
    gap: 8rpx;

    text {
      font-size: 26rpx;
      color: #666;
      line-height: 1.6;
    }
  }
}

/* 提交按钮 */
.submit-button {
  background-color: #f09b7f;
  margin: 30rpx 0;
  padding: 26rpx 30rpx;
  border-radius: 8rpx;
  text-align: center;

  &.disabled {
    background-color: #ddd;
    opacity: 0.6;
  }

  &:not(.disabled):active {
    background-color: #d87d63;
  }

  .submit-text {
    font-size: 32rpx;
    color: #fff;
    font-weight: 500;
  }
}
</style>
