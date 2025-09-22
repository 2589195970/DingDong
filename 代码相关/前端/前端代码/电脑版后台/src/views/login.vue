<template>
  <div class="login">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
      <h3 class="title">叮咚号卡</h3>
      <div v-if="sji">
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" type="text" auto-complete="off" placeholder="账号">
            <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" type="password" auto-complete="off" placeholder="密码"
            @keyup.enter.native="handleLogin">
            <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        <el-form-item v-if="captchaEnabled" prop="code">
          <el-input v-model="loginForm.code" auto-complete="off" placeholder="验证码" style="width: 63%"
            @keyup.enter.native="handleLogin">
            <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
          </el-input>
          <div class="login-code">
            <img :src="codeUrl" class="login-code-img" @click="getCode">
          </div>
        </el-form-item>
      </div>
      <div v-else>
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" type="text" auto-complete="off" placeholder="手机号">
            <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        <el-form-item prop="smsCode">
          <el-input v-model="loginForm.smsCode" auto-complete="off" placeholder="验证码" style="width: 70%">
            <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
          </el-input>
          <div class="login-code1" @click="getCode1">
            {{ countdown }}
          </div>
        </el-form-item>
      </div>

      <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>
      <el-form-item style="width:100%;">
        <el-button :loading="loading" size="medium" type="primary" style="width:100%;"
          @click.native.prevent="handleLogin">
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
        <!-- <div v-if="register" style="float: right;">
          <router-link class="link-type" :to="'/register'">立即注册</router-link>
        </div> -->
        <div style="float: right;">
          <span @click="handoff">{{dl}}</span>
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
  import { getCodeImg, loginPhone, sendSms } from '@/api/login'
  import { getToken, setToken, removeToken } from '@/utils/auth'
  import Cookies from 'js-cookie'
  import { encrypt, decrypt } from '@/utils/jsencrypt'
  import CryptoJS from 'crypto-js'
  export default {
    name: 'Login',
    data() {
      return {
        codeUrl: '',
        sji: true,
        dl: '手机号登录',
        countdown: "获取验证码",
        loginForm: {
          username: '',
          password: '',
          rememberMe: false,
          code: '',
          uuid: ''
        },
        loginRules: {
          username: [
            { required: true, trigger: 'blur', message: '请输入您的账号' }
          ],
          password: [
            { required: true, trigger: 'blur', message: '请输入您的密码' }
          ],
          code: [{ required: true, trigger: 'change', message: '请输入验证码' }]
        },
        loading: false,
        // 验证码开关
        captchaEnabled: true,

        encryptionConfig: {
          key: 'ADGcp7Kiixe1x3Sn', // 32字节密钥
        },
        // 注册开关
        register: true,
        redirect: undefined
      }
    },
    watch: {
      $route: {
        handler: function (route) {
          this.redirect = route.query && route.query.redirect
        },
        immediate: true
      }
    },
    created() {
      this.getCode()
      this.getCookie()
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
      handoff() {
        if (this.sji) {
          this.dl = '账号登录';
          this.sji = false;
        } else {
          this.dl = '手机号登录';
          this.sji = true;
        }

      },
      getCode1() {
        if (this.loginForm.username) {
          if (this.countdown == "获取验证码") {
           
            sendSms( this.encryptAndEncode({
            phoneNumber: this.loginForm.username,
            smsTemplateType: 2,
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
          this.$message.error("请填写手机号");
        }
      },
      getCode() {
        getCodeImg().then(res => {
          this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
          if (this.captchaEnabled) {
            this.codeUrl = 'data:image/gif;base64,' + res.img
            this.loginForm.uuid = res.uuid
          }
        })
      },
      getCookie() {
        const username = Cookies.get('username')
        const password = Cookies.get('password')
        const rememberMe = Cookies.get('rememberMe')
        this.loginForm = {
          username: username === undefined ? this.loginForm.username : username,
          password: password === undefined ? this.loginForm.password : decrypt(password),
          rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
        }
      },
      handleLogin() {
        if (this.sji) {
          this.$refs.loginForm.validate(valid => {
            if (valid) {
              this.loading = true
              if (this.loginForm.rememberMe) {
                Cookies.set('username', this.loginForm.username, { expires: 30 })
                Cookies.set('password', encrypt(this.loginForm.password), { expires: 30 })
                Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 })
              } else {
                Cookies.remove('username')
                Cookies.remove('password')
                Cookies.remove('rememberMe')
              }
              this.$store.dispatch('Login', this.loginForm).then(() => {
                this.$router.push({ path: this.redirect || '/' }).catch(() => { })
              }).catch(() => {
                this.loading = false
                if (this.captchaEnabled) {
                  this.getCode()
                }
              })
            }
          })
        } else {
          loginPhone({
            username: this.loginForm.username,
            password: this.loginForm.smsCode,
            uuid: this.loginForm.uuid
          }).then((res) => {
            setToken(res.token)
            this.$router.push('/login')
            this.$router.go(0)
          })
        }

      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss">
  .login {
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

  .login-form {
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

  .login-tip {
    font-size: 13px;
    text-align: center;
    color: #bfbfbf;
  }

  .login-code {
    width: 33%;
    height: 38px;
    float: right;

    img {
      cursor: pointer;
      vertical-align: middle;
    }
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

  .login-code1 {
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

  .login-code-img {
    height: 38px;
  }
</style>