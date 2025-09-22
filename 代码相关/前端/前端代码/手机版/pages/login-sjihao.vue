<template>
	<view class="normal-login-container">
		<view class="logo-content align-center justify-center flex">
			<image style="width: 100rpx;height: 100rpx;" :src="globalConfig.appInfo.logo" mode="widthFix">
			</image>
			<text class="title">叮咚号卡</text>
		</view>
		<view class="login-form-content">
			<view class="input-item flex align-center">
				<view class="iconfont icon-user icon"></view>
				<input v-model="loginForm.username" class="input" type="text" placeholder="请输入手机号" maxlength="30" />
			</view>
			<view class="flex">
				<view class="input-item flex align-center" style="width: 100%;margin: 0px;" v-if="captchaEnabled">
					<view class="iconfont icon-code icon"></view>
					<input v-model="loginForm.password" type="number" class="input" placeholder="请输入验证码" maxlength="4" />

				</view>
				<view class="login-code" @click="getCode1" style="width: 40%;">
					{{countdown}}
				</view>
			</view>

			<view class="action-btn">
				<button @click="handleLogin" class="login-btn cu-btn block bg-blue lg round">登录</button>
			</view>
			<view class="reg text-center" v-if="register">
				<text class="text-grey1">没有账号？</text>
				<text @click="handleUserRegister" class="text-blue">立即注册</text>
			</view>
			<view class="reg text-center">
				<text @click="handleUserRegister" class="text-blue">{{dl}}</text>
			</view>
			<!--     <view class="xieyi text-center">
        <text class="text-grey1">登录即代表同意</text>
        <text @click="handleUserAgrement" class="text-blue">《用户协议》</text>
        <text @click="handlePrivacy" class="text-blue">《隐私协议》</text>
      </view> -->
		</view>

	</view>
</template>

<script>
	import {
		getCodeImg,
		sendSms,
		loginPhone
	} from '@/api/login'
  import CryptoJS from 'crypto-js'
	export default {
		data() {
			return {
				codeUrl: "",
				dl: "账号登录",
				countdown: "获取验证码",
				captchaEnabled: true,
				// 用户注册开关
				register: false,
				globalConfig: getApp().globalData.config,
				  encryptionConfig: {
				                    key: 'ADGcp7Kiixe1x3Sn', // 32字节密钥
				                },
				loginForm: {
					username: "",
					password: "",
					code: "",
					uuid: ""
				}
			}
		},
		created() {
			this.getCode()
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
				if (this.loginForm.username) {
					if (this.countdown == "获取验证码") {
						sendSms(this.encryptAndEncode({
                            phoneNumber: this.loginForm.username,
                            smsTemplateType: 2,
                        })).then((res) => {
							this.$modal.loading("已发送")
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
						this.$modal.msgError("已发送验证码,请稍后再试")
					}
				} else {
					this.$modal.msgError("请填写手机号")
				}
			},
			// 用户注册
			handleUserRegister() {
				this.$tab.redirectTo(`/pages/login`)
			},
			// 隐私协议
			handlePrivacy() {
				let site = this.globalConfig.appInfo.agreements[0]
				this.$tab.navigateTo(`/pages/common/webview/index?title=${site.title}&url=${site.url}`)
			},
			// 用户协议
			handleUserAgrement() {
				let site = this.globalConfig.appInfo.agreements[1]
				this.$tab.navigateTo(`/pages/common/webview/index?title=${site.title}&url=${site.url}`)
			},
			// 获取图形验证码
			getCode() {
				getCodeImg().then(res => {
					this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
					if (this.captchaEnabled) {
						this.codeUrl = 'data:image/gif;base64,' + res.img
						this.loginForm.uuid = res.uuid
					}
				})
			},
			// 登录方法
			async handleLogin() {
				if (this.loginForm.username === "") {
					this.$modal.msgError("请输入账号")
				} else if (this.loginForm.password === "" && this.captchaEnabled) {
					this.$modal.msgError("请输入验证码")
				} else {
					this.$modal.loading("登录中，请耐心等待...")
					this.pwdLogin()
				}
			},
			// 密码登录
			async pwdLogin() {
			 this.$store.dispatch('loginPhone1', this.loginForm).then(() => {
					this.$modal.closeLoading()
					this.loginSuccess()
				}).catch(() => {
					if (this.captchaEnabled) {
						this.getCode()
					}
				})
			},
			// 登录成功后，处理函数
			loginSuccess(result) {
				// 设置用户信息
				this.$store.dispatch('GetInfo').then(res => {
					this.$tab.reLaunch('/pages/index')
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	page {
		background-color: #ffffff;
	}

	.login-code {
		width: 100%;
		height: 40px;
		float: right;
		line-height: 40px;
		text-align: center;
		border-radius: 20px;
		margin: 5px;
		border: 1px solid #dcdfe6;
	}

	.normal-login-container {
		width: 100%;

		.logo-content {
			width: 100%;
			font-size: 21px;
			text-align: center;
			padding-top: 15%;

			image {
				border-radius: 4px;
			}

			.title {
				margin-left: 10px;
			}
		}

		.login-form-content {
			text-align: center;
			margin: 20px auto;
			margin-top: 15%;
			width: 80%;

			.input-item {
				margin: 20px auto;
				background-color: #f5f6f7;
				height: 45px;
				border-radius: 20px;

				.icon {
					font-size: 38rpx;
					margin-left: 10px;
					color: #999;
				}

				.input {
					width: 100%;
					font-size: 14px;
					line-height: 20px;
					text-align: left;
					padding-left: 15px;
				}

			}

			.login-btn {
				margin-top: 40px;
				height: 45px;
			}

			.reg {
				margin-top: 15px;
			}

			.xieyi {
				color: #333;
				margin-top: 20px;
			}

			.login-code {
				height: 38px;
				float: right;

				.login-code-img {
					height: 38px;
					position: absolute;
					margin-left: 10px;
					width: 200rpx;
				}
			}
		}
	}
</style>