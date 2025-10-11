<template>
  <div class="app-container">
    <div class="profile-header">
      <h2 class="page-title">个人信息中心</h2>
      <p class="page-subtitle">管理您的个人资料和账户设置</p>
    </div>

    <el-row :gutter="24">
      <!-- 左侧：用户头像和基本信息 -->
      <el-col :span="6" :xs="24">
        <div class="user-profile">
          <div class="avatar-section">
            <userAvatar :user="user" />
          </div>
          <div class="user-info-card">
            <div class="info-item">
              <div class="info-label">
                <svg-icon icon-class="user" />
                <span>用户名称</span>
              </div>
              <div class="info-value">{{ user.userName || '未设置' }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">
                <svg-icon icon-class="date" />
                <span>创建日期</span>
              </div>
              <div class="info-value">{{ user.createTime || '未知' }}</div>
            </div>
          </div>
        </div>
      </el-col>

      <!-- 右侧：表单信息 -->
      <el-col :span="18" :xs="24">
        <el-tabs v-model="activeTab" class="profile-tabs">
            <!-- 基本资料 -->
            <el-tab-pane label="基本资料" name="userinfo">
              <userInfo :user="user" @refresh="getUserProfile" />
            </el-tab-pane>

            <!-- 修改密码 -->
            <el-tab-pane label="修改密码" name="resetPwd">
              <resetPwd />
            </el-tab-pane>

            <!-- 手机号绑定 -->
            <el-tab-pane label="手机号绑定" name="phone">
              <el-form label-width="150px" class="demo-ruleForm" :inline="true">
                <div class="form-content">
                  <el-form-item label="当前手机号：">
                    <div v-if="queryParams2.phone">
                      <span>{{queryParams2.phone}}</span>
                      &emsp; &emsp;<span @click="clickPhone(queryParams2)" class="clickable-text">点击更换</span>
                    </div>
                    <span v-if="!queryParams2.phone">未绑定</span>
                  </el-form-item>
                </div>
              </el-form>
            </el-tab-pane>

            <!-- 实名认证 -->
            <el-tab-pane label="实名认证" name="realname">
              <el-form label-width="150px" class="demo-ruleForm" :inline="true">
                <div class="form-content">
                  <el-form-item label="认证状态：">
                    <div v-if="queryParams2.isRealName === 0">
                      <span>未实名</span>
                      &emsp; &emsp; <span @click="clickName" class="clickable-text"> 点击实名</span>
                    </div>
                    <div v-else-if="queryParams2.isRealName === 1">
                      <span>已实名</span>
                    </div>
                    <div v-else-if="queryParams2.isRealName === 2 || queryParams2.isRealName === 3">
                      <span v-if="queryParams2.isRealName === 2">实名认证中</span>
                      <span v-else-if="queryParams2.isRealName === 3" style="color: red;">实名认证失败</span>
                      <span @click="clickName1" class="clickable-text"> 更改实名信息</span>
                    </div>
                  </el-form-item>
                </div>
              </el-form>
            </el-tab-pane>
          </el-tabs>
        </el-col>
      </el-row>

    <!-- 手机号修改弹窗 -->
    <el-dialog :visible.sync="openphone" width="450px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" v-model="formphone" label-width="100px">
        <el-form-item label="手机号：">
          <el-input v-model="formphone.phone"></el-input>
        </el-form-item>
        <el-form-item prop="smsCode">
          <el-input v-model="formphone.smsCode" auto-complete="off" placeholder="验证码" style="width: 70%">
            <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
          </el-input>
          <div class="login-code" @click="getCode1">
            {{ countdown }}
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitphone">修改</el-button>
      </div>
    </el-dialog>

    <!-- 实名认证弹窗 -->
    <el-dialog :visible.sync="openName" width="450px" append-to-body>
      <el-form ref="form" v-model="form" label-width="100px">
        <el-form-item label="姓名">
          <el-input v-model="form.cardName"></el-input>
        </el-form-item>
        <el-form-item label="身份证">
          <el-input v-model="form.cardId"></el-input>
        </el-form-item>
        <el-form-item label="身份证正面" prop="resource" style="width: 200px;height: 200px;">
          <el-upload class="avatar-uploader" :action="uploadUrl" :show-file-list="false"
            :on-success="handleAvatarSuccess" :headers=headers>
            <img v-if="form.cardIdUrlFront" :src="form.cardIdUrlFront" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="身份证反面" prop="resource" style="width: 200px;height: 200px;">
          <el-upload class="avatar-uploader" :action="uploadUrl" :show-file-list="false"
            :on-success="handleAvatarSuccess1" :headers=headers>
            <img v-if="form.cardIdUrlBack" :src="form.cardIdUrlBack" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAudit">提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getAgentInfoVO, updateAgentPhone } from "@/api/monitor/finance";
import { addNameAudit, updateNameAudit, selectNameAudit } from "@/api/monitor/business";
import { getUserProfile } from "@/api/system/user";
import { getToken } from "@/utils/auth";
import { sendSms } from "@/api/login";
import CryptoJS from 'crypto-js'
import userAvatar from "@/views/system/user/profile/userAvatar";
import userInfo from "@/views/system/user/profile/userInfo";
import resetPwd from "@/views/system/user/profile/resetPwd";

export default {
  name: "PersonalInfo",
  components: { userAvatar, userInfo, resetPwd },
  data() {
    return {
      uploadUrl: process.env.VUE_APP_BASE_API + "/picture/addPicture",
      headers: { Authorization: "Bearer " + getToken() },
      queryParams2: {},
      user: {},
      form: {},
      formphone: {},
      countdown: "获取验证码",
      openName: false,
      openphone: false,
      xgai: false,
      activeTab: "userinfo",
      encryptionConfig: {
        key: 'ADGcp7Kiixe1x3Sn',
      },
    };
  },
  created() {
    this.getUser();
    this.getAgentInfo();
  },
  methods: {
    getUser() {
      getUserProfile().then(response => {
        this.user = response.data;
      });
    },
    getAgentInfo() {
      getAgentInfoVO().then((res) => {
        this.queryParams2 = Object.assign({}, res.data);
        console.log(this.queryParams2);
      });
    },
    getUserProfile() {
      getUserProfile().then(response => {
        this.user = response.data;
      });
    },
    // 点击更换手机号
    clickPhone(data) {
      this.formphone = data;
      this.openphone = true;
    },

    // 点击实名认证
    clickName() {
      this.openName = true;
      this.xgai = true;
    },

    // 点击更改实名信息
    clickName1() {
      selectNameAudit().then((res) => {
        this.openName = true;
        this.form = res.data;
        this.xgai = false;
      });
    },

    // 获取验证码
    getCode1() {
      if (this.formphone.phone) {
        if (this.countdown === "获取验证码") {
          sendSms(this.encryptAndEncode({
            phoneNumber: this.formphone.phone,
            smsTemplateType: 0,
          })).then(() => {
            this.$message({
              message: "验证码已发送",
              type: "success",
            });
            this.countdown = 60;
            this.countdown--;
            const taskId = setInterval(() => {
              this.countdown--;
              if (this.countdown === 0) {
                this.countdown = "获取验证码";
                clearInterval(taskId);
              }
            }, 1000);
          });
        } else {
          this.$message.error("已发送验证码,请稍后再试");
        }
      } else {
        this.$message.error("请填写账号信息");
      }
    },

    // 加密方法
    encryptAndEncode(data) {
      try {
        const jsonString = JSON.stringify(data)
        const key = CryptoJS.enc.Utf8.parse(this.encryptionConfig.key)
        const encrypted = CryptoJS.AES.encrypt(jsonString, key, {
          mode: CryptoJS.mode.ECB,
          padding: CryptoJS.pad.Pkcs7
        })
        const base64Cipher = encrypted.toString()
        const urlSafeCipher = encodeURIComponent(base64Cipher)
        return urlSafeCipher
      } catch (error) {
        console.error('加密失败:', error)
        throw new Error('数据加密处理失败')
      }
    },

    // 提交手机号修改
    submitphone() {
      updateAgentPhone(this.formphone.smsCode, this.formphone.phone).then((res) => {
        this.$message({
          type: 'success',
          message: '修改成功!'
        });
        // 刷新代理商信息
        getAgentInfoVO().then((res) => {
          this.queryParams2 = Object.assign({}, res.data);
          console.log(this.queryParams2);
        });
        // 刷新用户基本信息
        this.getUserProfile();
        this.openphone = false;
      })
    },

    // 提交实名认证
    submitAudit() {
      this.form.agentCode = this.queryParams2.agentCode;
      if (this.xgai) {
        addNameAudit(this.form).then(() => {
          this.$message({
            type: 'success',
            message: '已提交审核'
          })
          // 刷新代理商信息
          this.getAgentInfo();
        })
      } else {
        updateNameAudit(this.form).then(() => {
          this.$message({
            type: 'success',
            message: '已提交'
          })
          // 刷新代理商信息
          this.getAgentInfo();
        })
      }
      this.openName = false;
    },

    // 身份证正面上传成功
    handleAvatarSuccess(res) {
      this.$set(this.form, 'cardIdUrlFront', res.message)
    },

    // 身份证反面上传成功
    handleAvatarSuccess1(res) {
      this.$set(this.form, 'cardIdUrlBack', res.message)
    },
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
// 页面头部样式
.profile-header {
  text-align: center;
  margin-bottom: 32px;
  padding: 20px 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  color: white;

  .page-title {
    font-size: 28px;
    font-weight: 600;
    margin: 0 0 8px 0;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }

  .page-subtitle {
    font-size: 16px;
    margin: 0;
    opacity: 0.9;
  }
}

// 用户资料卡片
.user-profile {
  background: #ffffff;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.06);
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 12px 40px rgba(0, 0, 0, 0.1);
  }
}

.avatar-section {
  text-align: center;
  margin-bottom: 24px;
}

.user-info-card {
  .info-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 0;
    border-bottom: 1px solid #f0f2f5;

    &:last-child {
      border-bottom: none;
    }

    .info-label {
      display: flex;
      align-items: center;
      color: #606266;
      font-size: 14px;

      .svg-icon {
        margin-right: 8px;
        color: #409EFF;
        font-size: 16px;
      }

      span {
        font-weight: 500;
      }
    }

    .info-value {
      color: #303133;
      font-weight: 600;
      font-size: 14px;
    }
  }
}

// Tab样式
.profile-tabs {
  background: #ffffff;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.06);

  ::v-deep .el-tabs__header {
    margin-bottom: 24px;
  }

  ::v-deep .el-tabs__nav-wrap::after {
    display: none;
  }

  ::v-deep .el-tabs__item {
    font-size: 15px;
    font-weight: 500;
    color: #606266;
    padding: 0 24px;

    &.is-active {
      color: #409EFF;
      font-weight: 600;
    }
  }

  ::v-deep .el-tabs__active-bar {
    background: #409EFF;
    height: 3px;
    border-radius: 2px;
  }
}

// 表单样式
.form-content {
  padding: 16px 0;
}

::v-deep .el-form-item {
  margin-bottom: 20px;

  .el-form-item__label {
    color: #606266;
    font-weight: 500;
  }
}

::v-deep .el-input__inner {
  border-radius: 8px;
  border: 1px solid #e0e6ed;
  transition: all 0.3s ease;

  &:focus {
    border-color: #409EFF;
    box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.1);
  }
}

// 按钮样式
::v-deep .el-button {
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.3s ease;

  &--primary {
    background: linear-gradient(135deg, #409EFF 0%, #1890ff 100%);
    border: none;

    &:hover {
      transform: translateY(-1px);
      box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
    }
  }
}

// 点击链接样式
.clickable-text {
  color: #409EFF;
  cursor: pointer;
  transition: all 0.3s ease;

  &:hover {
    color: #1890ff;
    text-decoration: underline;
  }
}

// 上传组件样式
::v-deep .avatar-uploader {
  .el-upload {
    border: 2px dashed #d9d9d9;
    border-radius: 12px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: all 0.3s ease;

    &:hover {
      border-color: #409EFF;
      background: rgba(64, 158, 255, 0.02);
    }
  }
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
  border-radius: 12px;
}

// 验证码样式
.login-code {
  width: 25%;
  height: 38px;
  float: right;
  text-align: center;
  border-radius: 8px;
  border: 1px solid #dcdfe6;
  cursor: pointer;
  background: #f5f7fa;
  color: #606266;
  font-size: 14px;
  line-height: 38px;
  transition: all 0.3s ease;

  &:hover {
    background: #409EFF;
    color: white;
    border-color: #409EFF;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .profile-header {
    padding: 16px;

    .page-title {
      font-size: 24px;
    }

    .page-subtitle {
      font-size: 14px;
    }
  }

  .user-profile {
    margin-bottom: 16px;
  }
}
</style>