<template>
  <view class="page-container">
    <app-navbar title="我的客服"></app-navbar>

    <!-- 客服头部信息 -->
    <view class="service-header">
      <view class="service-info">
        <image class="service-avatar" src="@/static/images/profile.jpg" mode="aspectFill"></image>
        <view class="service-details">
          <text class="service-name">叮咚号卡客服</text>
          <text class="service-desc">为您提供专业的服务支持</text>
          <view class="service-status online">
            <u-icon name="checkmark-circle-fill" color="#4cd964" size="12"></u-icon>
            <text>在线服务中</text>
          </view>
        </view>
      </view>

      <!-- 工作时间提示 -->
      <view class="work-time">
        <u-icon name="clock" color="#f09b7f" size="16"></u-icon>
        <text>服务时间：周一至周日 9:00-21:00</text>
      </view>
    </view>

    <!-- 联系方式 -->
    <view class="contact-section">
      <view class="section-title">
        <u-icon name="phone" color="#f09b7f"></u-icon>
        <text>联系我们</text>
      </view>

      <view class="contact-list">
        <!-- 微信客服 -->
        <view class="contact-item" @click="copyWechat">
          <view class="contact-left">
            <u-icon name="weixin" color="#07c160" size="24"></u-icon>
            <view class="contact-info">
              <text class="contact-title">微信客服</text>
              <text class="contact-value">{{ customerInfo.wechat || 'dingdong_service' }}</text>
            </view>
          </view>
          <view class="contact-right">
            <u-tag text="复制" size="mini" type="success" plain></u-tag>
            <u-icon name="arrow-right" color="#c0c4cc" size="16"></u-icon>
          </view>
        </view>

        <!-- QQ客服 -->
        <view class="contact-item" @click="copyQQ">
          <view class="contact-left">
            <u-icon name="qq" color="#12b7f5" size="24"></u-icon>
            <view class="contact-info">
              <text class="contact-title">QQ客服</text>
              <text class="contact-value">{{ customerInfo.qq || '888888888' }}</text>
            </view>
          </view>
          <view class="contact-right">
            <u-tag text="复制" size="mini" type="primary" plain></u-tag>
            <u-icon name="arrow-right" color="#c0c4cc" size="16"></u-icon>
          </view>
        </view>

        <!-- 客服电话 -->
        <view class="contact-item" @click="callPhone">
          <view class="contact-left">
            <u-icon name="phone-fill" color="#f56c6c" size="24"></u-icon>
            <view class="contact-info">
              <text class="contact-title">客服电话</text>
              <text class="contact-value">{{ customerInfo.phone || '400-888-8888' }}</text>
            </view>
          </view>
          <view class="contact-right">
            <u-tag text="拨打" size="mini" type="error" plain></u-tag>
            <u-icon name="arrow-right" color="#c0c4cc" size="16"></u-icon>
          </view>
        </view>

        <!-- 邮箱 -->
        <view class="contact-item" @click="copyEmail">
          <view class="contact-left">
            <u-icon name="email" color="#e6a23c" size="24"></u-icon>
            <view class="contact-info">
              <text class="contact-title">邮箱</text>
              <text class="contact-value">{{ customerInfo.email || 'service@dingdong.com' }}</text>
            </view>
          </view>
          <view class="contact-right">
            <u-tag text="复制" size="mini" type="warning" plain></u-tag>
            <u-icon name="arrow-right" color="#c0c4cc" size="16"></u-icon>
          </view>
        </view>
      </view>
    </view>

    <!-- 在线客服 -->
    <view class="online-service-section">
      <view class="section-title">
        <u-icon name="chat" color="#f09b7f"></u-icon>
        <text>在线咨询</text>
      </view>

      <view class="online-service-card" @click="openOnlineChat">
        <view class="card-content">
          <u-icon name="message" color="#409eff" size="32"></u-icon>
          <view class="card-info">
            <text class="card-title">在线客服</text>
            <text class="card-desc">点击进入在线客服系统，实时为您解答</text>
          </view>
        </view>
        <u-icon name="arrow-right" color="#c0c4cc"></u-icon>
      </view>
    </view>

    <!-- 常见问题 -->
    <view class="faq-section">
      <view class="section-title">
        <u-icon name="help" color="#f09b7f"></u-icon>
        <text>常见问题</text>
      </view>

      <view class="faq-list">
        <view
          v-for="(faq, index) in faqList"
          :key="index"
          class="faq-item"
          @click="toggleFaq(index)"
        >
          <view class="faq-question">
            <text>{{ faq.question }}</text>
            <u-icon
              :name="faq.expanded ? 'arrow-up' : 'arrow-down'"
              color="#c0c4cc"
              size="16"
            ></u-icon>
          </view>
          <view v-if="faq.expanded" class="faq-answer">
            <text>{{ faq.answer }}</text>
          </view>
        </view>
      </view>

      <!-- 查看更多常见问题 -->
      <view class="more-faq" @click="goToHelp">
        <text>查看更多常见问题</text>
        <u-icon name="arrow-right" color="#f09b7f" size="16"></u-icon>
      </view>
    </view>

    <!-- 反馈建议 -->
    <view class="feedback-section">
      <view class="section-title">
        <u-icon name="edit" color="#f09b7f"></u-icon>
        <text>意见反馈</text>
      </view>

      <view class="feedback-card" @click="showFeedbackForm">
        <view class="card-content">
          <u-icon name="edit-pen" color="#909399" size="24"></u-icon>
          <view class="card-info">
            <text class="card-title">提交反馈</text>
            <text class="card-desc">您的宝贵意见是我们改进的动力</text>
          </view>
        </view>
        <u-icon name="arrow-right" color="#c0c4cc"></u-icon>
      </view>
    </view>

    <!-- 反馈表单弹窗 -->
    <u-popup v-model="showFeedback" mode="bottom" border-radius="20">
      <view class="feedback-popup">
        <view class="popup-header">
          <text class="popup-title">意见反馈</text>
          <u-icon name="close" @click="showFeedback = false"></u-icon>
        </view>

        <view class="feedback-form">
          <view class="form-item">
            <text class="form-label">反馈类型</text>
            <u-radio-group v-model="feedbackForm.type" placement="row">
              <u-radio
                v-for="type in feedbackTypes"
                :key="type.value"
                :name="type.value"
                :label="type.label"
                :customStyle="{marginRight: '20px'}"
              ></u-radio>
            </u-radio-group>
          </view>

          <view class="form-item">
            <text class="form-label">详细描述</text>
            <u-textarea
              v-model="feedbackForm.content"
              placeholder="请详细描述您遇到的问题或建议..."
              :maxlength="500"
              count
            ></u-textarea>
          </view>

          <view class="form-item">
            <text class="form-label">联系方式（可选）</text>
            <u--input
              v-model="feedbackForm.contact"
              placeholder="微信/QQ/手机号，方便我们联系您"
            ></u--input>
          </view>

          <view class="form-actions">
            <u-button @click="showFeedback = false" size="large" type="info" plain>取消</u-button>
            <u-button @click="submitFeedback" size="large" type="primary">提交</u-button>
          </view>
        </view>
      </view>
    </u-popup>
  </view>
</template>

<script>
import { getCustomerService, submitCustomerFeedback } from '@/api/mine/customer.js';

export default {
  name: 'CustomerService',
  data() {
    return {
      // 客服信息
      customerInfo: {
        wechat: '',
        qq: '',
        phone: '',
        email: ''
      },

      // 常见问题
      faqList: [
        {
          question: '如何购买号卡？',
          answer: '进入商品页面，选择需要的号卡套餐，点击购买并完成支付即可。',
          expanded: false
        },
        {
          question: '佣金如何计算？',
          answer: '佣金根据您推广的订单金额和佣金比例计算，具体可在佣金设置页面查看。',
          expanded: false
        },
        {
          question: '如何提现？',
          answer: '进入佣金提现页面，选择提现方式，输入提现金额，提交申请即可。',
          expanded: false
        },
        {
          question: '订单状态如何查看？',
          answer: '在订单页面可以查看所有订单的状态和详情，包括处理进度。',
          expanded: false
        },
        {
          question: '如何成为代理？',
          answer: '完成实名认证后，联系客服申请代理资格，审核通过后即可成为代理。',
          expanded: false
        }
      ],

      // 反馈表单
      showFeedback: false,
      feedbackForm: {
        type: 1,
        content: '',
        contact: ''
      },
      feedbackTypes: [
        { label: '功能建议', value: 1 },
        { label: '问题反馈', value: 2 },
        { label: '其他', value: 3 }
      ]
    };
  },

  onLoad() {
    this.loadCustomerService();
  },

  methods: {
    // 加载客服信息
    async loadCustomerService() {
      try {
        const response = await getCustomerService();
        this.customerInfo = { ...this.customerInfo, ...response.data };
      } catch (error) {
        console.error('加载客服信息失败:', error);
      }
    },

    // 复制微信号
    copyWechat() {
      uni.setClipboardData({
        data: this.customerInfo.wechat || 'dingdong_service',
        success: () => {
          this.$u.toast('微信号已复制');
        }
      });
    },

    // 复制QQ号
    copyQQ() {
      uni.setClipboardData({
        data: this.customerInfo.qq || '888888888',
        success: () => {
          this.$u.toast('QQ号已复制');
        }
      });
    },

    // 拨打电话
    callPhone() {
      uni.makePhoneCall({
        phoneNumber: this.customerInfo.phone || '400-888-8888',
        fail: (err) => {
          this.$u.toast('拨打失败');
        }
      });
    },

    // 复制邮箱
    copyEmail() {
      uni.setClipboardData({
        data: this.customerInfo.email || 'service@dingdong.com',
        success: () => {
          this.$u.toast('邮箱已复制');
        }
      });
    },

    // 打开在线客服
    openOnlineChat() {
      // 这里可以集成第三方在线客服系统
      // 或者跳转到webview加载在线客服页面
      uni.navigateTo({
        url: '/pages/common/webview/index?url=' + encodeURIComponent('https://chat.dingdong.com')
      });
    },

    // 切换常见问题展开状态
    toggleFaq(index) {
      this.faqList[index].expanded = !this.faqList[index].expanded;
    },

    // 跳转到帮助页面
    goToHelp() {
      uni.navigateTo({
        url: '/pages/mine/help/index'
      });
    },

    // 显示反馈表单
    showFeedbackForm() {
      this.showFeedback = true;
    },

    // 提交反馈
    async submitFeedback() {
      if (!this.feedbackForm.content.trim()) {
        this.$u.toast('请输入反馈内容');
        return;
      }

      try {
        const response = await submitCustomerFeedback(this.feedbackForm);
        this.$u.toast('反馈提交成功，谢谢您的建议！');
        this.showFeedback = false;
        // 重置表单
        this.feedbackForm = {
          type: 1,
          content: '',
          contact: ''
        };
      } catch (error) {
        this.$u.toast('提交失败，请重试');
        console.error('提交反馈失败:', error);
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.page-container {
  background-color: #f5f5f5;
  min-height: 100vh;
}

.service-header {
  background: white;
  padding: 20px 15px;
  margin-bottom: 10px;

  .service-info {
    display: flex;
    align-items: center;
    margin-bottom: 15px;

    .service-avatar {
      width: 60px;
      height: 60px;
      border-radius: 30px;
      margin-right: 15px;
    }

    .service-details {
      flex: 1;

      .service-name {
        display: block;
        font-size: 18px;
        font-weight: bold;
        color: #333;
        margin-bottom: 5px;
      }

      .service-desc {
        display: block;
        font-size: 14px;
        color: #666;
        margin-bottom: 8px;
      }

      .service-status {
        display: flex;
        align-items: center;

        &.online {
          color: #4cd964;
          font-size: 12px;

          text {
            margin-left: 5px;
          }
        }
      }
    }
  }

  .work-time {
    display: flex;
    align-items: center;
    padding: 8px 12px;
    background: #fff5f0;
    border-radius: 6px;
    border-left: 3px solid #f09b7f;

    text {
      margin-left: 8px;
      font-size: 12px;
      color: #f09b7f;
    }
  }
}

.contact-section, .online-service-section, .faq-section, .feedback-section {
  background: white;
  margin-bottom: 10px;
  padding: 15px;

  .section-title {
    display: flex;
    align-items: center;
    margin-bottom: 15px;

    text {
      margin-left: 8px;
      font-size: 16px;
      font-weight: bold;
      color: #333;
    }
  }
}

.contact-list {
  .contact-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 15px 0;
    border-bottom: 1px solid #f0f0f0;

    &:last-child {
      border-bottom: none;
    }

    .contact-left {
      display: flex;
      align-items: center;
      flex: 1;

      .contact-info {
        margin-left: 12px;

        .contact-title {
          display: block;
          font-size: 14px;
          color: #333;
          margin-bottom: 4px;
        }

        .contact-value {
          display: block;
          font-size: 12px;
          color: #666;
        }
      }
    }

    .contact-right {
      display: flex;
      align-items: center;

      .u-tag {
        margin-right: 8px;
      }
    }
  }
}

.online-service-card, .feedback-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  border-radius: 10px;

  .card-content {
    display: flex;
    align-items: center;

    .card-info {
      margin-left: 15px;

      .card-title {
        display: block;
        font-size: 16px;
        font-weight: bold;
        color: #333;
        margin-bottom: 5px;
      }

      .card-desc {
        display: block;
        font-size: 12px;
        color: #666;
      }
    }
  }
}

.faq-list {
  .faq-item {
    border-bottom: 1px solid #f0f0f0;

    &:last-child {
      border-bottom: none;
    }

    .faq-question {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 15px 0;
      font-size: 14px;
      color: #333;
    }

    .faq-answer {
      padding-bottom: 15px;
      font-size: 13px;
      color: #666;
      line-height: 1.5;
      background: #f8f9fa;
      padding: 12px;
      border-radius: 6px;
      margin-bottom: 10px;
    }
  }
}

.more-faq {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 15px;
  margin-top: 10px;
  border-top: 1px solid #f0f0f0;

  text {
    margin-right: 5px;
    font-size: 14px;
    color: #f09b7f;
  }
}

// 反馈弹窗样式
.feedback-popup {
  padding: 20px;

  .popup-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    .popup-title {
      font-size: 18px;
      font-weight: bold;
    }
  }

  .feedback-form {
    .form-item {
      margin-bottom: 20px;

      .form-label {
        display: block;
        font-size: 14px;
        font-weight: bold;
        color: #333;
        margin-bottom: 8px;
      }
    }

    .form-actions {
      display: flex;
      gap: 15px;
      margin-top: 30px;

      .u-button {
        flex: 1;
      }
    }
  }
}
</style>