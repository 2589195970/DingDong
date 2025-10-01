<template>
  <view class="page-container">
    <app-navbar title="抖音小黄车直播配置"></app-navbar>

    <view class="container">
      <!-- 配置表单卡片 -->
      <view class="form-card">
        <view class="form-item has-border">
          <view class="item-left">
            <text class="required-mark">*</text>
            <text>抖音UID</text>
          </view>
          <view class="item-right">
            <input
              v-model="formData.douyinUid"
              placeholder="请输入您的抖音UID"
              class="form-input"
              type="text"
              @blur="validateUid"
              :class="{ 'error': errors.douyinUid }"
              :disabled="disableEdit"
            />
          </view>
        </view>

        <view class="form-item">
          <view class="item-left">
            <text class="required-mark">*</text>
            <text>抖音号</text>
          </view>
          <view class="item-right">
            <input
              v-model="formData.douyinAccount"
              placeholder="请输入您的抖音号"
              class="form-input"
              type="text"
              @blur="validateAccount"
              :class="{ 'error': errors.douyinAccount }"
              :disabled="disableEdit"
            />
          </view>
        </view>
      </view>

      <!-- 错误提示 -->
      <view class="error-tips" v-if="hasErrors">
        <view class="error-item" v-if="errors.douyinUid">
          <text class="error-text">{{ errors.douyinUid }}</text>
        </view>
        <view class="error-item" v-if="errors.douyinAccount">
          <text class="error-text">{{ errors.douyinAccount }}</text>
        </view>
      </view>

      <!-- 提示信息 -->
      <view class="tips-section">
        <view class="tips-content">
          <text class="tips-text">• 请确保填写的抖音UID和抖音号准确无误</text>
          <text class="tips-text">• 提交后将进入审核流程，预计24小时内完成</text>
          <text class="tips-text">• 审核结果将通过系统通知告知</text>
        </view>
      </view>

      <!-- 按钮组 -->
      <view class="button-group">
        <!-- 提交按钮 -->
        <view
          class="submit-button"
          :class="{ 'disabled': !canSubmit || isSubmitting }"
          @click="handleSubmit"
        >
          <text class="button-text">{{ isSubmitting ? '提交中...' : '提交' }}</text>
        </view>

        <!-- 教程按钮 -->
        <view class="tutorial-button" @click="showTutorial">
          <text class="button-text">点我查看教程</text>
        </view>
      </view>
    </view>

    <!-- 教程弹窗 -->
    <view class="tutorial-modal" v-if="showTutorialModal" @click="closeTutorial">
      <view class="modal-mask"></view>
      <view class="modal-content" @click.stop>
        <view class="modal-header">
          <text class="modal-title">配置说明</text>
          <view class="close-icon" @click="closeTutorial">
            <text class="iconfont icon-close"></text>
          </view>
        </view>

        <scroll-view class="modal-body" scroll-y>
          <view class="tutorial-content">
            <view class="tutorial-section">
              <view v-if="loadingTutorial" class="loading-tip">
                <text>正在加载配置说明...</text>
              </view>
              <text v-else-if="tutorialContent" class="tutorial-text">{{ tutorialContent }}</text>
              <text v-else class="tutorial-text">
1. 打开抖音APP，进入个人主页

2. 点击右上角"三横线"菜单，进入"设置"

3. 选择"账号与安全"，查看您的抖音UID

4. 抖音号即为您的个性化账号名称

5. 将获取到的UID和抖音号填写到表单中

6. 提交后等待审核，审核通过后即可开始直播带货

注意事项：
• 确保填写信息准确无误
• 审核时间为24小时内（节假日除外）
• 禁止虚假选择、私自包装
• 违规者将解除合作
              </text>
            </view>
          </view>
        </scroll-view>

        <view class="modal-footer">
          <view class="confirm-button" @click="closeTutorial">
            <text>我知道了</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 加载提示弹窗 -->
    <view class="loading-modal" v-if="showLoadingModal">
      <view class="modal-mask"></view>
      <view class="loading-content">
        <view class="loading-spinner"></view>
        <text class="loading-text">提交中，请稍候...</text>
      </view>
    </view>
  </view>
</template>

<script>
import { addLiveAudit, selectLiveAudit, updateLiveAudit, getLiveConfig } from '@/api/agent/liveAudit.js'
import { getUserProfile } from '@/api/system/user'
import appNavbar from '@/components/app-navbar/app-navbar.vue'

export default {
  name: 'CreateLive',
  components: {
    appNavbar
  },
  data() {
    return {
      // 表单数据
      formData: {
        liveAuditId: null, // 如果是编辑模式，需要记录ID
        douyinUid: '',
        douyinAccount: ''
      },

      // 错误信息
      errors: {
        douyinUid: '',
        douyinAccount: ''
      },

      // 用户和代理商信息
      user: {},
      agentAccount: {},

      // 状态变量
      isEdit: false, // 是否为编辑模式
      currentStatus: null, // 当前审核状态 0:待认证 1:审核失败 2:审核成功
      disableEdit: false, // 是否禁用编辑
      isSubmitting: false,
      showTutorialModal: false,
      showLoadingModal: false,
      loadingTutorial: false, // 是否正在加载教程
      tutorialContent: '' // 从后台获取的教程内容
    }
  },

  computed: {
    // 是否有错误
    hasErrors() {
      return Object.values(this.errors).some(error => error)
    },

    // 是否可以提交
    canSubmit() {
      return !this.disableEdit &&
             this.formData.douyinUid.trim() &&
             this.formData.douyinAccount.trim() &&
             !this.hasErrors
    }
  },

  onLoad() {
    this.initPage()
  },

  methods: {
    // 获取用户信息
    async getUser() {
      try {
        const response = await getUserProfile()
        this.user = response.data

        // 更新代理商信息
        if (response.agentAccount) {
          this.agentAccount = response.agentAccount
          this.$store.commit('SET_AGENT_ACCOUNT', response.agentAccount)
        }
      } catch (error) {
        this.$modal.showToast('获取用户信息失败')
        console.error('获取用户信息失败:', error)
      }
    },

    // 初始化页面
    async initPage() {
      try {
        // 获取用户信息
        await this.getUser()

        // 检查是否已有配置记录
        await this.checkExistingConfig()

        // 注意：教程内容延迟加载，在用户点击时才获取
      } catch (error) {
        console.error('初始化页面失败:', error)
      }
    },

    // 加载教程内容
    async loadTutorialContent() {
      if (this.tutorialContent) {
        // 已经加载过，直接返回
        return
      }

      try {
        this.loadingTutorial = true
        const response = await getLiveConfig()
        if (response.code === 200 && response.data) {
          // 后端返回 LiveConfig 实体，字段名为 configValue
          this.tutorialContent = response.data.configValue || ''
        } else {
          console.log('接口返回失败，code:', response.code)
        }
      } catch (error) {
        console.error('加载教程内容失败:', error)
        this.$modal.showToast('获取配置说明失败，显示默认内容')
      } finally {
        this.loadingTutorial = false
      }
    },

    // 检查是否已有配置
    async checkExistingConfig() {
      try {
        const response = await selectLiveAudit()
        if (response.code === 200 && response.data) {
          this.currentStatus = response.data.status

          // 状态：0-待认证 1-审核失败 2-审核成功
          if (response.data.status === 0) {
            // 待认证状态，显示提示并禁止修改
            this.$modal.showToast('您的配置正在审核中，请耐心等待')
            this.isEdit = true
            this.formData = {
              liveAuditId: response.data.liveAuditId,
              douyinUid: response.data.douyinUid || '',
              douyinAccount: response.data.douyinAccount || ''
            }
            // 禁用输入框
            this.disableEdit = true
          } else if (response.data.status === 2) {
            // 审核成功，显示提示并禁止修改
            this.$modal.showToast('您的配置已审核通过，无需重复提交')
            this.isEdit = true
            this.formData = {
              liveAuditId: response.data.liveAuditId,
              douyinUid: response.data.douyinUid || '',
              douyinAccount: response.data.douyinAccount || ''
            }
            this.disableEdit = true
          } else if (response.data.status === 1) {
            // 审核失败，允许修改
            this.isEdit = true
            this.formData = {
              liveAuditId: response.data.liveAuditId,
              douyinUid: response.data.douyinUid || '',
              douyinAccount: response.data.douyinAccount || ''
            }
            this.disableEdit = false
          }
        }
      } catch (error) {
        // 没有配置记录，保持新增模式
        console.log('暂无配置记录')
        this.disableEdit = false
      }
    },

    // 验证抖音UID
    validateUid() {
      const uid = this.formData.douyinUid.trim()
      if (!uid) {
        this.errors.douyinUid = '请输入抖音UID'
      } else if (!/^[0-9_]+$/.test(uid)) {
        this.errors.douyinUid = '抖音UID格式不正确（仅支持数字和下划线）'
      } else {
        this.errors.douyinUid = ''
      }
    },

    // 验证抖音号
    validateAccount() {
      const account = this.formData.douyinAccount.trim()
      if (!account) {
        this.errors.douyinAccount = '请输入抖音号'
      } else if (account.length < 4) {
        this.errors.douyinAccount = '抖音号长度至少4位'
      } else {
        this.errors.douyinAccount = ''
      }
    },

    // 处理提交
    async handleSubmit() {
      if (!this.canSubmit || this.isSubmitting) {
        return
      }

      // 检查是否禁用编辑
      if (this.disableEdit) {
        if (this.currentStatus === 0) {
          this.$modal.showToast('您的配置正在审核中，请耐心等待')
        } else if (this.currentStatus === 2) {
          this.$modal.showToast('您的配置已审核通过，无需重复提交')
        }
        return
      }

      // 最终验证
      this.validateUid()
      this.validateAccount()

      if (this.hasErrors) {
        this.$modal.showToast('请检查输入信息')
        return
      }

      try {
        this.isSubmitting = true
        this.showLoadingModal = true

        // 准备提交数据
        const submitData = {
          douyinUid: this.formData.douyinUid.trim(),
          douyinAccount: this.formData.douyinAccount.trim(),
          agentCode: this.agentAccount.agentCode,
          agentName: this.agentAccount.agentName || this.user.userName,
          sysUserId: this.user.userId || this.user.id,
          status: 0,  // 新增时默认为待认证状态
          remark: ''  // 审核备注默认为空
        }

        // 如果是编辑模式，需要添加ID
        if (this.isEdit && this.formData.liveAuditId) {
          submitData.liveAuditId = this.formData.liveAuditId
        }

        // 根据是否为编辑模式调用不同接口
        const response = this.isEdit
          ? await updateLiveAudit(submitData)
          : await addLiveAudit(submitData)

        if (response.code === 200) {
          this.$modal.showToast('提交成功，请等待审核')

          // 延迟返回上一页
          setTimeout(() => {
            uni.navigateBack()
          }, 1500)
        } else {
          throw new Error(response.msg || '提交失败')
        }
      } catch (error) {
        console.error('提交配置失败:', error)
        this.$modal.showToast(error.message || '提交失败，请重试')
      } finally {
        this.isSubmitting = false
        this.showLoadingModal = false
      }
    },

    // 显示教程
    async showTutorial() {
      this.showTutorialModal = true
      // 显示弹窗时加载教程内容
      await this.loadTutorialContent()
    },

    // 关闭教程
    closeTutorial() {
      this.showTutorialModal = false
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

/* 表单卡片样式 */
.form-card {
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

  &.has-border {
    border-bottom: 1rpx solid #eaeef1;
  }

  .item-left {
    font-size: 32rpx;
    color: #333;
    flex-shrink: 0;
    width: 140rpx;
    display: flex;
    align-items: center;

    .required-mark {
      color: #fa3534;
      margin-right: 4rpx;
      font-size: 32rpx;
    }
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

  &:disabled {
    color: #999;
    background-color: #f5f5f5;
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

/* 提示信息样式 */
.tips-section {
  background-color: #fff;
  border-radius: 8rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.tips-content {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}

.tips-text {
  font-size: 28rpx;
  color: #666;
  line-height: 1.6;
}

/* 按钮组样式 */
.button-group {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
  margin-top: 30rpx;
}

/* 提交按钮样式 */
.submit-button {
  background: linear-gradient(135deg, #ff6b6b 0%, #f09b7f 100%);
  padding: 26rpx 30rpx;
  border-radius: 8rpx;
  text-align: center;
  box-shadow: 0 4rpx 12rpx rgba(240, 155, 127, 0.3);

  &.disabled {
    background: #ddd;
    opacity: 0.6;
    box-shadow: none;
  }

  &:not(.disabled):active {
    background: linear-gradient(135deg, #d87d63 0%, #c77d63 100%);
  }

  .button-text {
    font-size: 32rpx;
    color: #fff;
    font-weight: 500;
  }
}

/* 教程按钮样式 */
.tutorial-button {
  background-color: #fff;
  padding: 26rpx 30rpx;
  border-radius: 8rpx;
  text-align: center;
  border: 2rpx solid #f09b7f;

  &:active {
    background-color: #f8f9fa;
  }

  .button-text {
    font-size: 32rpx;
    color: #f09b7f;
    font-weight: 500;
  }
}

/* 教程弹窗样式 */
.tutorial-modal {
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
  background-color: rgba(0, 0, 0, 0.6);
}

.modal-content {
  background-color: #fff;
  border-radius: 16rpx;
  margin: 0 60rpx;
  position: relative;
  z-index: 1;
  width: calc(100% - 120rpx);
  max-height: 80vh;
  display: flex;
  flex-direction: column;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #eaeef1;
}

.modal-title {
  font-size: 36rpx;
  font-weight: 600;
  color: #333;
}

.close-icon {
  width: 48rpx;
  height: 48rpx;
  display: flex;
  align-items: center;
  justify-content: center;

  .iconfont {
    font-size: 32rpx;
    color: #999;
  }
}

.modal-body {
  flex: 1;
  padding: 30rpx;
  max-height: 60vh;
}

.tutorial-content {
  .tutorial-section {
    margin-bottom: 30rpx;
  }

  .tutorial-text {
    font-size: 28rpx;
    color: #666;
    line-height: 2;
    white-space: pre-wrap;
  }

  .loading-tip {
    text-align: center;
    padding: 40rpx;
    color: #999;
    font-size: 28rpx;
  }
}

.modal-footer {
  padding: 20rpx 30rpx 30rpx;
  border-top: 1rpx solid #eaeef1;
}

.confirm-button {
  background-color: #f09b7f;
  padding: 26rpx;
  border-radius: 8rpx;
  text-align: center;

  &:active {
    background-color: #d87d63;
  }

  text {
    font-size: 32rpx;
    color: #fff;
    font-weight: 500;
  }
}

/* 加载弹窗样式 */
.loading-modal {
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

.loading-content {
  background-color: #fff;
  border-radius: 16rpx;
  padding: 60rpx 40rpx;
  margin: 0 60rpx;
  position: relative;
  z-index: 1;
  min-width: 300rpx;
  text-align: center;
}

.loading-spinner {
  width: 60rpx;
  height: 60rpx;
  border: 4rpx solid #f3f3f3;
  border-top: 4rpx solid #f09b7f;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 20rpx;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.loading-text {
  font-size: 28rpx;
  color: #666;
}
</style>
