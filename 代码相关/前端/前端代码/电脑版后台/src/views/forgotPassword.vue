<template>
  <div class="forgot-password">
    <el-form ref="forgotPasswordForm" :model="forgotPasswordForm" :rules="forgotPasswordRules" class="forgot-password-form">
      <h3 class="title">忘记密码</h3>
      <el-form-item prop="phone">
        <el-input v-model="forgotPasswordForm.phone" type="text" auto-complete="off" placeholder="手机号" maxlength="11">
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="smsCode">
        <el-input
          v-model="forgotPasswordForm.smsCode"
          auto-complete="off"
          placeholder="验证码"
          style="width: 70%"
        >
          <svg-icon
            slot="prefix"
            icon-class="validCode"
            class="el-input__icon input-icon"
          />
        </el-input>
        <div 
          class="login-code" 
          :class="{ 'disabled': countdown !== '获取验证码' }"
          @click="getSmsCode"
        >
          {{ countdown }}
        </div>
      </el-form-item>
      <el-form-item prop="newPassword">
        <el-input
          v-model="forgotPasswordForm.newPassword"
          type="password"
          auto-complete="off"
          placeholder="新密码"
          @keyup.enter.native="handleResetPassword"
        >
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="confirmPassword">
        <el-input
          v-model="forgotPasswordForm.confirmPassword"
          type="password"
          auto-complete="off"
          placeholder="确认新密码"
          @keyup.enter.native="handleResetPassword"
        >
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item style="width:100%;">
        <el-button
          :loading="loading"
          size="medium"
          type="primary"
          style="width:100%;"
          @click.native.prevent="handleResetPassword"
        >
          <span v-if="!loading">重置密码</span>
          <span v-else>重置中...</span>
        </el-button>
        <div style="float: right;">
          <router-link class="link-type" :to="'/login'">返回登录</router-link>
        </div>
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-login-footer">
      <a href="https://beian.miit.gov.cn"><span>浙ICP备2024139459号</span></a>
    </div>
  </div>
</template>

<script>
import { sendSms, resetPassword } from "@/api/login";
import CryptoJS from 'crypto-js'

export default {
  name: "ForgotPassword",
  data() {
    const equalToPassword = (rule, value, callback) => {
      if (this.forgotPasswordForm.newPassword !== value) {
        callback(new Error("两次输入的密码不一致"));
      } else {
        callback();
      }
    };
    return {
      countdown: "获取验证码",
      forgotPasswordForm: {
        phone: "",
        smsCode: "",
        newPassword: "",
        confirmPassword: ""
      },
      encryptionConfig: {
        key: 'ADGcp7Kiixe1x3Sn', // 32字节密钥
      },
      forgotPasswordRules: {
        phone: [
          { required: true, trigger: "blur", message: "请输入手机号" },
          { pattern: /^1[3-9]\d{9}$/, message: "请输入正确的手机号格式", trigger: "blur" }
        ],
        smsCode: [
          { required: true, trigger: "change", message: "请输入验证码" },
          { min: 4, max: 6, message: "验证码长度为4-6位", trigger: "blur" }
        ],
        newPassword: [
          { required: true, trigger: "blur", message: "请输入新密码" },
          { min: 5, max: 20, message: "密码长度必须介于 5 和 20 之间", trigger: "blur" },
          { pattern: /^[^<>"'|\\]+$/, message: "不能包含非法字符：< > \" ' \\\ |", trigger: "blur" }
        ],
        confirmPassword: [
          { required: true, trigger: "blur", message: "请再次输入新密码" },
          { required: true, validator: equalToPassword, trigger: "blur" }
        ]
      },
      loading: false
    };
  },
  methods: {
    encryptAndEncode(data) {
      try {
        // 1. 对象转JSON字符串
        const jsonString = JSON.stringify(data)

        // 2. 准备密钥和IV
        const key = CryptoJS.enc.Utf8.parse(this.encryptionConfig.key)

        // 3. AES-CBC加密
        const encrypted = CryptoJS.AES.encrypt(jsonString, key, {
          mode: CryptoJS.mode.ECB,
          padding: CryptoJS.pad.Pkcs7
        })

        // 4. 获取Base64格式的加密字符串
        const base64Cipher = encrypted.toString()

        // 5. URL编码处理（关键步骤）
        const urlSafeCipher = encodeURIComponent(base64Cipher)

        return urlSafeCipher
      } catch (error) {
        console.error('加密失败:', error)
        throw new Error('数据加密处理失败')
      }
    },
    getSmsCode() {
      // 如果正在倒计时，不允许点击
      if (this.countdown !== "获取验证码") {
        return;
      }
      
      if (this.forgotPasswordForm.phone) {
        // 先验证手机号格式
        this.$refs.forgotPasswordForm.validateField('phone', (errorMessage) => {
          if (!errorMessage) {
            sendSms(this.encryptAndEncode({
              phoneNumber: this.forgotPasswordForm.phone,
              smsTemplateType: 1, // 1表示忘记密码验证码
            })).then((res) => {
              this.$message({
                message: "验证码已发送",
                type: "success",
              });
              this.startCountdown();
            }).catch((error) => {
              this.$message.error("验证码发送失败，请稍后重试");
            });
          }
        });
      } else {
        this.$message.error("请先输入手机号");
      }
    },
    
    startCountdown() {
      this.countdown = 60;
      const taskId = setInterval(() => {
        this.countdown--;
        if (this.countdown === 0) {
          this.countdown = "获取验证码";
          clearInterval(taskId);
        }
      }, 1000);
    },
    handleResetPassword() {
      this.$refs.forgotPasswordForm.validate(valid => {
        if (valid) {
          this.loading = true;
          resetPassword(this.forgotPasswordForm).then(res => {
            this.$alert("<font color='red'>密码重置成功！请使用新密码登录。</font>", '系统提示', {
              dangerouslyUseHTMLString: true,
              type: 'success'
            }).then(() => {
              this.$router.push("/login");
            }).catch(() => {});
          }).catch(error => {
            this.loading = false;
            this.$message.error(error.message || "密码重置失败，请稍后重试");
          });
        }
      });
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss">
.forgot-password {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../assets/images/th.png");
  background-size: cover;
}
.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #707070;
}
.el-login-footer {
    height: 40px;
    line-height: 40px;
    position: fixed;
    bottom: 0;
    width: 100%;
    text-align: center;
    color: #fff;
    font-family: Arial;
    font-size: 12px;
    letter-spacing: 1px;
  }

.forgot-password-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;
  .el-input {
    height: 38px;
    input {
      height: 38px;
    }
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}
.forgot-password-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 25%;
  height: 38px;
  float: right;
  text-align: center;
  border-radius: 20px;
  border: 1px solid #dcdfe6;
  cursor: pointer;
  user-select: none;
  transition: all 0.3s;
  
  &:hover:not(.disabled) {
    background-color: #f5f7fa;
    border-color: #c0c4cc;
  }
  
  &.disabled {
    cursor: not-allowed;
    background-color: #f5f7fa;
    color: #c0c4cc;
    border-color: #e4e7ed;
  }
  
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.link-type {
  color: #337ab7;
  cursor: pointer;
  text-decoration: none;
  &:hover {
    color: #2e6da4;
  }
}
</style>
