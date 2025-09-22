<template>
  <div class="register">
    <el-form ref="registerForm" :model="registerForm" :rules="registerRules" class="register-form">
      <h3 class="title">叮咚号卡</h3>
      <el-form-item prop="parentAgentCode">
        <el-input v-model="registerForm.parentAgentCode" type="text" auto-complete="off" placeholder="推荐人编码" :disabled="true">
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="username">
        <el-input v-model="registerForm.userName" type="text" auto-complete="off" placeholder="账号">
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="phone">
        <el-input v-model="registerForm.phone" type="text" auto-complete="off" placeholder="手机号"  maxlength="11">
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="smsCode">
        <el-input
          v-model="registerForm.smsCode"
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
        <div class="login-code" @click="getCode1">
          {{ countdown }}
        </div>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="registerForm.password"
          type="password"
          auto-complete="off"
          placeholder="密码"
          @keyup.enter.native="handleRegister"
        >
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="confirmPassword">
        <el-input
          v-model="registerForm.confirmPassword"
          type="password"
          auto-complete="off"
          placeholder="确认密码"
          @keyup.enter.native="handleRegister"
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
          @click.native.prevent="handleRegister"
        >
          <span v-if="!loading">注 册</span>
          <span v-else>注 册 中...</span>
        </el-button>
        <div style="float: right;">
          <router-link class="link-type" :to="'/login'">使用已有账户登录</router-link>
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
import { getCodeImg, register ,sendSms} from "@/api/login";
 import CryptoJS from 'crypto-js'
export default {
  name: "Register",
  data() {
    const equalToPassword = (rule, value, callback) => {
      if (this.registerForm.password !== value) {
        callback(new Error("两次输入的密码不一致"));
      } else {
        callback();
      }
    };
    return {
      codeUrl: "",
      countdown: "获取验证码",
      registerForm: {
        username: "",
        password: "",
        confirmPassword: "",
        code: "",
        uuid: ""
      },
      encryptionConfig: {
          key: 'ADGcp7Kiixe1x3Sn', // 32字节密钥
        },
      registerRules: {
        parentAgentCode:[
          { required: true, trigger: "blur", message: "请输入推荐人" },
          { min: 2, max: 20, message: '请输入推荐人', trigger: 'blur' }
        ],
        // username: [
        //   { required: true, trigger: "blur", message: "请输入您的账号" },
        //   { min: 2, max: 10, message: '用户账号长度必须介于 2 和 10 之间', trigger: 'blur' }
        // ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" },
          { min: 5, max: 20, message: "用户密码长度必须介于 5 和 20 之间", trigger: "blur" },
          { pattern: /^[^<>"'|\\]+$/, message: "不能包含非法字符：< > \" ' \\\ |", trigger: "blur" }
        ],
        confirmPassword: [
          { required: true, trigger: "blur", message: "请再次输入您的密码" },
          { required: true, validator: equalToPassword, trigger: "blur" }
        ],
        smsCode: [{ required: true, trigger: "change", message: "请输入验证码" }]
      },
      loading: false,
      captchaEnabled: true
    };
  },
  created() {
    console.log(this.$route.query.aasasa);
    this.getCode();
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
    getCode1() {
      
      
      if (this.registerForm.phone) {
        if (this.countdown == "获取验证码") {
          sendSms(this.encryptAndEncode({
            phoneNumber: this.loginForm.username,
            smsTemplateType: 0,
          })).then((res) => {
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
    getCode() {
     this.registerForm.parentAgentCode=this.$route.query.agentCode
    },
    handleRegister() {
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          this.loading = true;
          register(this.registerForm).then(res => {
            const username = this.registerForm.username;
            this.$alert("<font color='red'>恭喜你，您的账号 " + username + " 注册成功！</font>", '系统提示', {
              dangerouslyUseHTMLString: true,
              type: 'success'
            }).then(() => {
              this.$router.push("/login");
            }).catch(() => {});
          }).catch(ref=> {
            this.loading = false;
          })
        }
      });
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss">
.register {
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

.register-form {
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
.register-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.register-code {
  width: 33%;
  height: 38px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-register-footer {
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
.login-code {
  width: 25%;
  height: 38px;
  float: right;
  text-align: center;
  border-radius: 20px;
  border: 1px solid #dcdfe6;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.register-code-img {
  height: 38px;
}
</style>
