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
				<input v-model="loginForm.username" class="input" type="text" placeholder="请输入账号" maxlength="30" />
			</view>
			<view class="input-item flex align-center">
				<view class="iconfont icon-password icon"></view>
				<input v-model="loginForm.password" type="password" class="input" placeholder="请输入密码" maxlength="20" />
			</view>
			<view class="input-item flex align-center" style="width: 60%;margin: 0px;" v-if="captchaEnabled">
				<view class="iconfont icon-code icon"></view>
				<input v-model="loginForm.code" type="number" class="input" placeholder="请输入验证码" maxlength="4" />
				<view class="login-code">
					<image :src="codeUrl" @click="getCode" class="login-code-img"></image>
				</view>
			</view>
			<view class="action-btn">
				<button @click="handleLogin" class="login-btn cu-btn block bg-primary lg round">登录</button>
			</view>

			<view class="reg text-center">

				<u-checkbox-group>
					<u-checkbox @change="rememberChange" v-model="rememberMe" checkedColor="#f09b7f" :checked="rememberMe"
						activeColor="#f09b7f"></u-checkbox>
					<view>
						<text>记住密码</text>
					</view>
				</u-checkbox-group>
			</view>

			<!-- <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox> -->
			<view class="reg text-center">
				<text @click="handleUserRegister1" class="text-primary">{{dl}}</text>
			</view>
			<!--     <view class="xieyi text-center">
        <text class="text-grey1">登录即代表同意</text>
        <text @click="handleUserAgrement" class="text-primary">《用户协议》</text>
        <text @click="handlePrivacy" class="text-primary">《隐私协议》</text>
      </view> -->
		</view>

	</view>
</template>

<script>
	import {
		getCodeImg
	} from '@/api/login'
	export default {
		data() {
			return {
				codeUrl: "",
				dl: "手机号登录",
				captchaEnabled: true,
				rememberMe: true,
				// 用户注册开关
				register: false,
				globalConfig: getApp().globalData.config,
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
			rememberChange(){
				this.rememberMe=!this.rememberMe;
			},
			// 用户注册
			handleUserRegister() {
				this.$tab.redirectTo(`/pages/register`)
			},

			handleUserRegister1() {
				this.$tab.redirectTo(`/pages/login-sjihao`)
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
				this.loginForm.username = uni.getStorageSync('username')
				console.log(uni.getStorageSync('username'));
				console.log(this.loginForm.username);
				this.loginForm.password = uni.getStorageSync('password')
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
				} else if (this.loginForm.password === "") {
					this.$modal.msgError("请输入密码")
				} else if (this.loginForm.code === "" && this.captchaEnabled) {
					this.$modal.msgError("请输入验证码")
				} else {
					this.$modal.loading("登录中，请耐心等待...")
					console.log(this.rememberMe);
					if (this.rememberMe) {
						uni.setStorageSync('username', this.loginForm.username);
						uni.setStorageSync('password', this.loginForm.password);
							console.log(uni.getStorageSync('username'));
							console.log(this.loginForm.username);
					} else {
						uni.removeStorageSync("username");
						uni.removeStorageSync("password");
					}
					this.pwdLogin()
				}
			},
			// 密码登录
			async pwdLogin() {
				this.$store.dispatch('Login', this.loginForm).then(() => {
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