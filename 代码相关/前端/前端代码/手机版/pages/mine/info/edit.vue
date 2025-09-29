<template>
  <view class="page-container">
    <app-navbar title="编辑资料"></app-navbar>
    <view class="container">
      <!-- 编辑表单卡片 -->
      <view class="edit-card">
        <view class="form-item" :class="{ 'has-border': true }">
          <view class="item-left">用户昵称</view>
          <view class="item-right">
            <input
              v-model="user.nickName"
              placeholder="请输入昵称"
              class="form-input"
              maxlength="20"
            />
          </view>
        </view>

        <view class="form-item" :class="{ 'has-border': true }">
          <view class="item-left">手机号码</view>
          <view class="item-right">
            <input
              v-model="user.phonenumber"
              placeholder="请输入手机号码"
              class="form-input"
              type="number"
              maxlength="11"
            />
          </view>
        </view>

        <view class="form-item" :class="{ 'has-border': true }">
          <view class="item-left">邮箱</view>
          <view class="item-right">
            <input
              v-model="user.email"
              placeholder="请输入邮箱"
              class="form-input"
              type="email"
            />
          </view>
        </view>

        <view class="form-item">
          <view class="item-left">性别</view>
          <view class="item-right">
            <view class="gender-options">
              <view
                class="gender-option"
                :class="{ 'active': user.sex === '0' }"
                @click="selectGender('0')"
              >
                男
              </view>
              <view
                class="gender-option"
                :class="{ 'active': user.sex === '1' }"
                @click="selectGender('1')"
              >
                女
              </view>
            </view>
          </view>
        </view>
      </view>

      <!-- 提交按钮 -->
      <view class="submit-button" @click="submit">
        <text class="submit-text">保存修改</text>
      </view>
    </view>
  </view>
</template>

<script>
  import { getUserProfile } from "@/api/system/user"
  import { updateUserProfile } from "@/api/system/user"

  export default {
    data() {
      return {
        user: {
          nickName: "",
          phonenumber: "",
          email: "",
          sex: ""
        },
        sexs: [{
          text: '男',
          value: "0"
        }, {
          text: '女',
          value: "1"
        }]
      }
    },
    onLoad() {
      this.getUser()
    },
    methods: {
      getUser() {
        getUserProfile().then(response => {
          this.user = response.data
        })
      },
      selectGender(value) {
        this.user.sex = value
      },
      validateForm() {
        if (!this.user.nickName || this.user.nickName.trim() === '') {
          this.$modal.showToast('请输入用户昵称')
          return false
        }
        if (!this.user.phonenumber || this.user.phonenumber.trim() === '') {
          this.$modal.showToast('请输入手机号码')
          return false
        }
        const phonePattern = /^1[3-9]\d{9}$/
        if (!phonePattern.test(this.user.phonenumber)) {
          this.$modal.showToast('请输入正确的手机号码')
          return false
        }
        if (!this.user.email || this.user.email.trim() === '') {
          this.$modal.showToast('请输入邮箱地址')
          return false
        }
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
        if (!emailPattern.test(this.user.email)) {
          this.$modal.showToast('请输入正确的邮箱地址')
          return false
        }
        return true
      },
      submit() {
        if (!this.validateForm()) {
          return
        }

        updateUserProfile(this.user).then(() => {
          this.$modal.msgSuccess("修改成功")
          setTimeout(() => {
            this.$tab.navigateBack()
          }, 1500)
        }).catch(error => {
          this.$modal.showToast('修改失败，请重试')
          console.error('修改用户信息失败:', error)
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

/* 编辑表单卡片 */
.edit-card {
  background-color: #fff;
  border-radius: 8rpx;
  overflow: hidden;
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

      &::placeholder {
        color: #c0c0c0;
      }
    }

    .gender-options {
      display: flex;
      gap: 20rpx;

      .gender-option {
        padding: 12rpx 24rpx;
        border: 1rpx solid #e5e5e5;
        border-radius: 4rpx;
        font-size: 28rpx;
        color: #666;
        background-color: #f8f9fa;
        transition: all 0.2s;

        &.active {
          background-color: #f09b7f;
          color: #fff;
          border-color: #f09b7f;
        }

        &:active {
          opacity: 0.8;
        }
      }
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

  &:active {
    background-color: #d87d63;
  }

  .submit-text {
    font-size: 32rpx;
    color: #fff;
    font-weight: 500;
  }
}
</style>
