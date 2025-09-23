<template>
  <view class="page-container">
    <app-navbar title="实名认证"></app-navbar>
    <view class="container">

      <!-- 状态提示区域 -->
      <view class="status-tip" v-if="showStatusTip">
        <view class="tip-card">
          <view class="tip-icon">
            <text class="iconfont" :class="statusTipIcon"></text>
          </view>
          <view class="tip-content">
            <view class="tip-title">{{ statusTipTitle }}</view>
            <view class="tip-desc">{{ statusTipDesc }}</view>
          </view>
        </view>
      </view>

      <!-- 基本信息卡片 -->
      <view class="info-card">
        <view class="card-title">基本信息</view>

        <view class="info-item has-border">
          <view class="item-left">真实姓名</view>
          <view class="item-right">
            <input
              class="form-input"
              v-model="formData.cardName"
              placeholder="请输入真实姓名"
              :class="{ 'error': errors.cardName }"
              @blur="validateName"
            />
          </view>
        </view>

        <view class="info-item">
          <view class="item-left">身份证号</view>
          <view class="item-right">
            <input
              class="form-input"
              v-model="formData.cardId"
              placeholder="请输入18位身份证号"
              :class="{ 'error': errors.cardId }"
              @blur="validateIdCard"
            />
          </view>
        </view>
      </view>

      <!-- 错误提示 -->
      <view class="error-tips" v-if="hasErrors">
        <view class="error-item" v-if="errors.cardName">
          <text class="error-text">{{ errors.cardName }}</text>
        </view>
        <view class="error-item" v-if="errors.cardId">
          <text class="error-text">{{ errors.cardId }}</text>
        </view>
      </view>

      <!-- 照片上传卡片 -->
      <view class="info-card">
        <view class="card-title">身份证照片</view>

        <!-- 身份证正面 -->
        <view class="upload-item has-border">
          <view class="upload-left">
            <view class="upload-title">身份证正面</view>
            <view class="upload-desc">请上传身份证人像面</view>
          </view>
          <view class="upload-right">
            <view class="upload-area" @click="chooseImage('front')">
              <image
                v-if="formData.cardIdUrlFront"
                :src="formData.cardIdUrlFront"
                class="upload-image"
                mode="aspectFill"
              />
              <view v-else class="upload-placeholder">
                <text class="iconfont icon-camera"></text>
                <text class="placeholder-text">点击上传</text>
              </view>
            </view>
          </view>
        </view>

        <!-- 身份证反面 -->
        <view class="upload-item">
          <view class="upload-left">
            <view class="upload-title">身份证反面</view>
            <view class="upload-desc">请上传身份证国徽面</view>
          </view>
          <view class="upload-right">
            <view class="upload-area" @click="chooseImage('back')">
              <image
                v-if="formData.cardIdUrlBack"
                :src="formData.cardIdUrlBack"
                class="upload-image"
                mode="aspectFill"
              />
              <view v-else class="upload-placeholder">
                <text class="iconfont icon-camera"></text>
                <text class="placeholder-text">点击上传</text>
              </view>
            </view>
          </view>
        </view>
      </view>

      <!-- 提交按钮 -->
      <view class="submit-button" :class="{ 'disabled': !canSubmit || isSubmitting }" @click="handleSubmit">
        <text class="submit-text">{{ submitButtonText }}</text>
      </view>

    </view>

    <!-- 上传进度弹窗 -->
    <view class="upload-modal" v-if="showUploadModal">
      <view class="modal-mask"></view>
      <view class="modal-content">
        <view class="upload-progress">
          <view class="loading-icon">
            <view class="loading-spinner"></view>
          </view>
          <text class="progress-text">正在上传图片，请稍候...</text>
        </view>
      </view>
    </view>

  </view>
</template>

<script>
import { addNameAudit, updateNameAudit, selectNameAudit } from "@/api/system/user"
import upload from '@/utils/upload'
import appNavbar from '@/components/app-navbar/app-navbar.vue'

export default {
  name: 'RealNameAuth',
  components: {
    appNavbar
  },
  data() {
    return {
      // 表单数据
      formData: {
        nameAuditId: null, // 实名认证记录ID（编辑时需要）
        cardName: '',
        cardId: '',
        cardIdUrlFront: '',
        cardIdUrlBack: ''
      },

      // 错误信息
      errors: {
        cardName: '',
        cardId: ''
      },

      // 状态变量
      isEdit: false, // 是否为编辑模式（重新认证）
      isSubmitting: false,
      showUploadModal: false,
      currentUploadType: '', // 当前上传类型 'front' 或 'back'

      // 实名认证状态信息
      realNameStatus: 0,
      statusTipConfig: {
        0: {
          icon: 'icon-warning',
          title: '还未进行实名认证',
          desc: '请填写真实信息并上传身份证照片完成认证'
        },
        1: {
          icon: 'icon-time',
          title: '实名认证审核中',
          desc: '您的认证信息正在审核中，请耐心等待'
        },
        3: {
          icon: 'icon-close-circle',
          title: '实名认证失败',
          desc: '认证信息审核未通过，请重新提交正确信息'
        }
      }
    }
  },

  computed: {
    // 从 Vuex 获取实名认证信息
    realNameInfo() {
      return this.$store.state.user.realNameInfo || {}
    },

    // 是否显示状态提示
    showStatusTip() {
      return this.realNameStatus !== 2 && this.statusTipConfig[this.realNameStatus]
    },

    // 状态提示图标
    statusTipIcon() {
      const config = this.statusTipConfig[this.realNameStatus]
      return config ? config.icon : ''
    },

    // 状态提示标题
    statusTipTitle() {
      const config = this.statusTipConfig[this.realNameStatus]
      return config ? config.title : ''
    },

    // 状态提示描述
    statusTipDesc() {
      const config = this.statusTipConfig[this.realNameStatus]
      return config ? config.desc : ''
    },

    // 是否有错误
    hasErrors() {
      return Object.values(this.errors).some(error => error)
    },

    // 是否可以提交
    canSubmit() {
      return this.formData.cardName &&
             this.formData.cardId &&
             this.formData.cardIdUrlFront &&
             this.formData.cardIdUrlBack &&
             !this.hasErrors
    },

    // 提交按钮文字
    submitButtonText() {
      if (this.isSubmitting) {
        return '提交中...'
      }
      return this.isEdit ? '重新提交认证' : '提交认证'
    }
  },

  onLoad() {
    this.initPage()
  },

  methods: {
    // 初始化页面
    async initPage() {
      try {
        // 获取当前实名认证状态
        this.realNameStatus = this.realNameInfo.realNameStatus || 0

        // 如果是重新认证（状态为3-认证失败），需要获取之前的认证信息
        if (this.realNameStatus === 3) {
          this.isEdit = true
          await this.loadExistingData()
        }
      } catch (error) {
        console.error('初始化页面失败:', error)
        this.$modal.showToast('页面初始化失败')
      }
    },

    // 加载现有认证数据
    async loadExistingData() {
      try {
        const response = await selectNameAudit()
        if (response.code === 200 && response.data) {
          this.formData = {
            ...this.formData,
            nameAuditId: response.data.nameAuditId, // 保存ID用于更新
            cardName: response.data.cardName || '',
            cardId: response.data.cardId || '',
            cardIdUrlFront: response.data.cardIdUrlFront || '',
            cardIdUrlBack: response.data.cardIdUrlBack || ''
          }
        }
      } catch (error) {
        console.error('加载认证数据失败:', error)
        this.$modal.showToast('加载数据失败')
      }
    },

    // 验证姓名
    validateName() {
      const name = this.formData.cardName.trim()
      if (!name) {
        this.errors.cardName = '请输入真实姓名'
      } else if (!/^[\u4e00-\u9fa5]{2,10}$/.test(name)) {
        this.errors.cardName = '姓名应为2-10位中文字符'
      } else {
        this.errors.cardName = ''
      }
    },

    // 验证身份证号
    validateIdCard() {
      const idCard = this.formData.cardId.trim()
      if (!idCard) {
        this.errors.cardId = '请输入身份证号'
      } else if (!/^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/.test(idCard)) {
        this.errors.cardId = '请输入正确的18位身份证号'
      } else {
        this.errors.cardId = ''
      }
    },

    // 选择图片
    chooseImage(type) {
      this.currentUploadType = type

      uni.chooseImage({
        count: 1,
        sizeType: ['compressed'],
        sourceType: ['camera', 'album'],
        success: (res) => {
          const tempFilePath = res.tempFilePaths[0]
          this.uploadImage(tempFilePath, type)
        },
        fail: (error) => {
          console.error('选择图片失败:', error)
          this.$modal.showToast('选择图片失败')
        }
      })
    },

    // 上传图片
    async uploadImage(filePath, type) {
      try {
        this.showUploadModal = true

        const uploadResult = await upload({
          url: '/picture/addPicture',
          filePath: filePath,
          name: 'file'
        })

        if (uploadResult.code === 200) {
          if (type === 'front') {
            this.formData.cardIdUrlFront = uploadResult.data || uploadResult.message
          } else {
            this.formData.cardIdUrlBack = uploadResult.data || uploadResult.message
          }
          this.$modal.showToast('上传成功')
        } else {
          throw new Error(uploadResult.msg || '上传失败')
        }
      } catch (error) {
        console.error('上传图片失败:', error)
        this.$modal.showToast('上传失败，请重试')
      } finally {
        this.showUploadModal = false
      }
    },

    // 处理提交
    async handleSubmit() {
      if (!this.canSubmit || this.isSubmitting) {
        return
      }

      // 最终验证
      this.validateName()
      this.validateIdCard()

      if (this.hasErrors) {
        this.$modal.showToast('请检查输入信息')
        return
      }

      try {
        this.isSubmitting = true

        // 准备提交数据 - 包含所有必需字段
        const userInfo = this.$store.state.user.userInfo || {}
        const submitData = {
          cardName: this.formData.cardName.trim(),
          cardId: this.formData.cardId.trim(),
          cardIdUrlFront: this.formData.cardIdUrlFront,
          cardIdUrlBack: this.formData.cardIdUrlBack,
          agentCode: userInfo.agentCode || userInfo.userName,
          sysUserId: userInfo.userId || userInfo.id, // 用户ID
          agentName: userInfo.nickName || userInfo.agentName || userInfo.userName, // 代理商名称
          status: 0, // 待审核状态
          remark: null // 备注字段
        }

        // 如果是编辑模式，需要添加nameAuditId
        if (this.isEdit && this.formData.nameAuditId) {
          submitData.nameAuditId = this.formData.nameAuditId
        }

        // 根据是否为编辑模式调用不同接口
        const response = this.isEdit
          ? await updateNameAudit(submitData)
          : await addNameAudit(submitData)

        if (response.code === 200) {
          // 更新 Vuex 中的实名认证状态
          this.$store.commit('SET_REALNAME_INFO', {
            ...this.realNameInfo,
            realNameStatus: 1, // 设置为认证中状态
            cardName: submitData.cardName,
            cardId: this.maskIdCard(submitData.cardId)
          })

          this.$modal.showToast('提交成功，请等待审核')

          // 延迟返回上一页
          setTimeout(() => {
            uni.navigateBack()
          }, 1500)
        } else {
          throw new Error(response.msg || '提交失败')
        }
      } catch (error) {
        console.error('提交认证失败:', error)
        this.$modal.showToast(error.message || '提交失败，请重试')
      } finally {
        this.isSubmitting = false
      }
    },

    // 身份证号脱敏处理
    maskIdCard(idCard) {
      if (!idCard || idCard.length < 6) return idCard
      return idCard.substring(0, 6) + '********' + idCard.substring(idCard.length - 4)
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

/* 状态提示样式 */
.status-tip {
  margin-bottom: 20rpx;
}

.tip-card {
  background-color: #fff;
  border-radius: 8rpx;
  padding: 30rpx;
  display: flex;
  align-items: center;
}

.tip-icon {
  margin-right: 20rpx;

  .iconfont {
    font-size: 40rpx;

    &.icon-warning {
      color: #ff9900;
    }

    &.icon-time {
      color: #f09b7f;
    }

    &.icon-close-circle {
      color: #fa3534;
    }
  }
}

.tip-content {
  flex: 1;
}

.tip-title {
  font-size: 32rpx;
  font-weight: 500;
  color: #333;
  margin-bottom: 8rpx;
}

.tip-desc {
  font-size: 28rpx;
  color: #666;
  line-height: 1.4;
}

/* 信息卡片样式 */
.info-card {
  background-color: #fff;
  border-radius: 8rpx;
  overflow: hidden;
  margin-bottom: 20rpx;
}

.card-title {
  font-size: 32rpx;
  font-weight: 500;
  color: #333;
  padding: 30rpx 30rpx 20rpx 30rpx;
  border-bottom: 1rpx solid #eaeef1;
}

/* 表单项样式 */
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
    width: 140rpx;
  }

  .item-right {
    display: flex;
    align-items: center;
    flex: 1;
    justify-content: flex-end;
  }
}

.form-input {
  border: none;
  outline: none;
  background: transparent;
  font-size: 32rpx;
  color: #333;
  text-align: right;
  width: 100%;

  &::placeholder {
    color: #c0c0c0;
  }

  &.error {
    color: #fa3534;
  }
}

/* 错误提示样式 */
.error-tips {
  margin-bottom: 20rpx;
}

.error-item {
  margin-bottom: 10rpx;
}

.error-text {
  font-size: 28rpx;
  color: #fa3534;
  padding-left: 15rpx;
}

/* 上传项样式 */
.upload-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 30rpx;

  &.has-border {
    border-bottom: 1rpx solid #eaeef1;
  }
}

.upload-left {
  flex: 1;
}

.upload-title {
  font-size: 32rpx;
  color: #333;
  margin-bottom: 8rpx;
}

.upload-desc {
  font-size: 28rpx;
  color: #666;
}

.upload-right {
  margin-left: 20rpx;
}

.upload-area {
  width: 120rpx;
  height: 80rpx;
  border-radius: 8rpx;
  overflow: hidden;
  position: relative;
}

.upload-image {
  width: 100%;
  height: 100%;
}

.upload-placeholder {
  width: 100%;
  height: 100%;
  background-color: #f8f9fa;
  border: 1rpx dashed #ddd;
  border-radius: 8rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;

  .iconfont {
    font-size: 32rpx;
    color: #c0c0c0;
    margin-bottom: 4rpx;
  }

  .placeholder-text {
    font-size: 20rpx;
    color: #c0c0c0;
  }
}

/* 提交按钮样式 */
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

/* 上传进度弹窗 */
.upload-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 9999;
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-mask {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
}

.modal-content {
  background-color: #fff;
  border-radius: 16rpx;
  padding: 60rpx 40rpx;
  margin: 0 60rpx;
  position: relative;
  z-index: 1;
  min-width: 300rpx;
}

.upload-progress {
  text-align: center;
}

.loading-icon {
  margin-bottom: 20rpx;
}

.loading-spinner {
  width: 60rpx;
  height: 60rpx;
  border: 4rpx solid #f3f3f3;
  border-top: 4rpx solid #f09b7f;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.progress-text {
  font-size: 28rpx;
  color: #666;
}
</style>