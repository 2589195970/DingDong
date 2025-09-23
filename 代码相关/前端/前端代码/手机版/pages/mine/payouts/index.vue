<template>
	<view class="page-container">
		<app-navbar title="佣金提现"></app-navbar>

		<!-- 实名认证提示区域 -->
		<view v-if="!isRealNameVerified" class="container">
			<view class="realname-tip-card">
				<view class="tip-icon">
					<u-icon name="warning-fill" color="#ff9900" size="40"></u-icon>
				</view>
				<view class="tip-content">
					<view class="tip-title">需要实名认证</view>
					<view class="tip-desc">提现功能需要完成实名认证后才能使用</view>
					<view class="tip-status">当前状态：{{ getRealNameStatusText() }}</view>
				</view>
			</view>

			<view class="action-button" @click="showRealNameModal">
				<text class="action-text">前往实名认证</text>
			</view>
		</view>

		<!-- 提现表单区域 -->
		<view v-if="isRealNameVerified" class="container">
			<!-- 账户信息卡片 -->
			<view class="info-card">
				<view class="info-item" v-for="(item, index) in accountInfoList" :key="index"
					  :class="{ 'has-border': index < accountInfoList.length - 1 }">
					<view class="item-left">{{ item.label }}</view>
					<view class="item-right">
						<text class="item-value" :class="item.valueClass">{{ item.value }}</text>
					</view>
				</view>
			</view>

			<!-- 提现模式选择卡片 -->
			<view class="info-card">
				<view class="info-item">
					<view class="item-left">提现模式</view>
					<view class="item-right">
						<u-radio-group v-model="radiovalue" placement="row" @change="groupChange">
							<u-radio :customStyle="{marginBottom: '8px'}" v-for="(item, index) in radiolist" :key="index"
								:label="item.name" :name="item.name">
							</u-radio>
						</u-radio-group>
					</view>
				</view>
			</view>

			<!-- 微信收款码 -->
			<view v-if="queryParams.withdrawalType=='0'" class="info-card">
				<view class="info-item">
					<view class="item-left">微信收款码</view>
					<view class="item-right">
						<u-upload :fileList="fileList1" @afterRead="afterRead" @delete="deletePic" name="1" :headers="headers" multiple
							:maxCount="1"></u-upload>
					</view>
				</view>
			</view>

			<!-- 支付宝信息 -->
			<view v-if="queryParams.withdrawalType=='1'" class="info-card">
				<view class="info-item has-border">
					<view class="item-left">支付宝账号</view>
					<view class="item-right">
						<input v-model="queryParams.zfbAccount" placeholder="请输入支付宝账号" class="form-input" />
					</view>
				</view>
				<view class="info-item">
					<view class="item-left">真实姓名</view>
					<view class="item-right">
						<input v-model="queryParams.zfbAccountName" placeholder="请输入真实姓名" class="form-input" />
					</view>
				</view>
			</view>

			<!-- 银行卡信息 -->
			<view v-if="queryParams.withdrawalType=='2'" class="info-card">
				<view class="info-item has-border">
					<view class="item-left">银行开户行</view>
					<view class="item-right">
						<input v-model="queryParams.bankName" placeholder="请输入银行开户行" class="form-input" />
					</view>
				</view>
				<view class="info-item has-border">
					<view class="item-left">真实姓名</view>
					<view class="item-right">
						<input v-model="queryParams.bankNumberName" placeholder="请输入真实姓名" class="form-input" />
					</view>
				</view>
				<view class="info-item has-border">
					<view class="item-left">银行卡号</view>
					<view class="item-right">
						<input v-model="queryParams.bankNumber" placeholder="请输入银行卡号" class="form-input" />
					</view>
				</view>
				<view class="info-item">
					<view class="item-left">手机号</view>
					<view class="item-right">
						<input v-model="queryParams.bankNumberPhone" placeholder="请输入手机号" class="form-input" />
					</view>
				</view>
			</view>

			<!-- 提现金额卡片 -->
			<view class="info-card">
				<view class="info-item" :class="{ 'has-border': querwithd.withdrawalRate }">
					<view class="item-left">提现金额</view>
					<view class="item-right">
						<input v-model="queryParams.withdrawalAmount" placeholder="请输入提现金额" class="form-input" @input="hlv" />
					</view>
				</view>
				<view v-if="querwithd.withdrawalRate" class="info-item has-border">
					<view class="item-left">提现费率</view>
					<view class="item-right">
						<text class="item-value">{{querwithd.withdrawalRate}}%</text>
					</view>
				</view>
				<view v-if="querwithd.withdrawalRateAmount" class="info-item has-border">
					<view class="item-left">提现手续费</view>
					<view class="item-right">
						<text class="item-value">{{querwithd.withdrawalRateAmount * 0.01}}元</text>
					</view>
				</view>
				<view v-if="querwithd.receivedAmount" class="info-item">
					<view class="item-left">实际到账</view>
					<view class="item-right">
						<text class="item-value text-primary">{{querwithd.receivedAmount * 0.01}}元</text>
					</view>
				</view>
			</view>

			<!-- 提交按钮 -->
			<view class="submit-button" @click="submitForm(queryParams)">
				<text class="submit-text">提交申请</text>
			</view>
		</view>
	</view>
</template>
<script>
	import {
		// 申请提现
		addAgentWithdrawalApplication,
		// 计算汇率
		computeRate,
		// 查询提现配置
		selectWithdrawalConfig,
		// 查询余额
		selectWithdrawalRecord
	} from "@/api/finance/finance";
	import {
		getToken
	} from "@/utils/auth";
	export default {
		name: "Payouts",
		dicts: ['sys_oper_type', 'sys_common_status'],
		data() {
			return {
				// 实名认证检查相关
				isRealNameVerified: false,
				showRealNameTip: false,
				radiolist: [{
						name: '微信',
						disabled: false
					},
					{
						name: '支付宝',
						disabled: false
					},
					{
						name: '银行卡',
						disabled: false
					},
				],
				// u-radio-group的v-model绑定的值如果设置为某个radio的name，就会被默认选中
				radiovalue: '微信',
				uploadUrl: process.env.VUE_APP_BASE_API + "/picture/addPicture", // 上传的图片服务器地址,
				headers: {
					Authorization: "Bearer " + getToken()
				},
				// 表格数据
				list: [],
				ruleForm: [],
				fileList1: [],
				imageUrl: false,
				// 导入文件
				form: {

				},


				api: [],
				groupCode: [],
				queryParams: {
					withdrawalType: '0',

				},
				querwithd: {},
				pz: {}
			};
		},
		async created() {
			// 首先检查实名认证状态
			this.checkRealNameStatus();

			// 如果未实名认证，直接返回不加载数据
			if (!this.isRealNameVerified) {
				return;
			}

			try {
				// 获取配置数据
				const configRes = await selectWithdrawalConfig({});
				this.pz = configRes.data;

				// 获取提现记录并更新余额
				const recordRes = await selectWithdrawalRecord();
				this.$set(this.pz, 'balance', recordRes.data.balance);
			} catch (error) {
				console.error('Error occurred while fetching data:', error);
				// 在这里可以显示用户友好的错误提示
			}
		},

		// beforeCreate() {
		//     // this.getList();
		//     selectWithdrawalConfig({}).then((res) => {
		//         this.pz = res.data;
		//     })
		//     selectWithdrawalRecord().then((res) => {
		//         this.$set(this.pz, 'balance', res.data.balance);
		//     })
		// },
		computed: {
			// 从 Vuex 获取实名认证信息
			realNameInfo() {
				return this.$store.state.user.realNameInfo || {}
			},

			// 账户信息列表
			accountInfoList() {
				return [
					{
						label: '账户余额',
						value: `${(this.pz.balance * 0.01) || 0}元`,
						valueClass: 'text-primary'
					},
					{
						label: '增值税发票税费',
						value: `${this.pz.withdrawRate || 0}%`,
						valueClass: ''
					},
					{
						label: '最低提现金额',
						value: `${this.pz.withdrawMinAmount || 0}元`,
						valueClass: ''
					}
				]
			}
		},
		methods: {
			// 检查实名认证状态
			checkRealNameStatus() {
				const realNameInfo = this.realNameInfo;

				// 检查是否已通过实名认证（状态为 2）
				this.isRealNameVerified = realNameInfo.realNameStatus === 2;

				if (!this.isRealNameVerified) {
					this.showRealNameTip = true;
					this.showRealNameModal();
				}
			},

			// 显示实名认证提示弹窗
			showRealNameModal() {
				const statusText = this.getRealNameStatusText();
				const content = statusText === '未认证'
					? '您还未进行实名认证，提现功能需要完成实名认证后才能使用。是否前往实名认证？'
					: `您的实名认证状态为"${statusText}"，提现功能需要通过实名认证后才能使用。`;

				this.$modal.confirm(content).then(() => {
					if (statusText === '未认证') {
						// 跳转到实名认证页面
						this.$tab.navigateTo('/pages/mine/realname/index');
					} else {
						// 其他状态暂时返回上一页
						uni.navigateBack();
					}
				}).catch(() => {
					// 用户取消，返回上一页
					uni.navigateBack();
				});
			},

			// 获取实名认证状态文本
			getRealNameStatusText() {
				const status = this.realNameInfo.realNameStatus;
				const statusMap = {
					0: '未认证',
					1: '认证中',
					2: '已认证',
					3: '认证失败'
				}
				return statusMap[status] || '未认证';
			},
			deletePic(event) {
				this[`fileList${event.name}`].splice(event.index, 1);
			},
			// 新增图片
			async afterRead(event) {
				// 当设置 multiple 为 true 时, file 为数组格式，否则为对象格式
				let lists = [].concat(event.file);
				let fileListLen = this[`fileList${event.name}`].length;
				lists.map((item) => {
					this[`fileList${event.name}`].push({
						...item,
						status: "uploading",
						message: "上传中",
					});
				});
				for (let i = 0; i < lists.length; i++) {
					const result = await this.uploadFilePromise(lists[i].url);
					let item = this[`fileList${event.name}`][fileListLen];
					this[`fileList${event.name}`].splice(
						fileListLen,
						1,
						Object.assign(item, {
							status: "success",
							message: "",
							url: result,
						})
					);
					fileListLen++;
				}
				
			},
			uploadFilePromise(url) {
				return new Promise((resolve, reject) => {
					let a = uni.uploadFile({
						url:"https://www.dingdonghaoka.com/prod-api/picture/addPicture", // 仅为示例，非真实的接口地址
						filePath: url,
						name: "file",
					       header: this.headers,
						success: (res) => {
							setTimeout(() => {
								resolve(res.data);
								console.log(JSON.parse(res.data))
								var wxUrl=JSON.parse(res.data)
								this.queryParams.wxUrl=wxUrl.message;
								console.log(this.queryParams.wxUrl)
							}, 1000);
						},
					});
				});
				
			},

			groupChange(n) {
				if (n == "微信") {
					this.queryParams.withdrawalType = '0'

				} else if (n == "支付宝") {
					this.queryParams.withdrawalType = '1'
				} else if (n == "银行卡") {
					this.queryParams.withdrawalType = '2'
				}

				console.log('groupChange', n);
			},
			hlv() {
				if (this.queryParams.withdrawalAmount) {
					computeRate(this.queryParams.withdrawalAmount).then((res) => {
						this.querwithd = res.data;
					})
				}

			},
			submitForm(formName) {
				addAgentWithdrawalApplication(formName).then((res) => {
					this.$modal.msgSuccess("已提交申请");
				})
			},
			handleSelectionChange(val) {

			},
			handleAvatarSuccess(res, file) {
				this.$set(this.queryParams, 'wxUrl', res.message)
			},
			// 图片返回
			handlesuccess(file) {
				// this.queryParams.productPlacardMap = file.msg;
			},
		},
	}
</script>
<style lang="scss" scoped>
page {
	background-color: #f5f6f7;
}

.container {
	padding: 15px;
}

/* 实名认证提示样式 */
.realname-tip-card {
	background-color: #fff;
	border-radius: 8rpx;
	padding: 30rpx;
	margin-bottom: 20rpx;
	display: flex;
	align-items: center;
}

.tip-icon {
	margin-right: 20rpx;
	flex-shrink: 0;
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
	margin-bottom: 8rpx;
}

.tip-status {
	font-size: 24rpx;
	color: #909399;
}

.action-button {
	background-color: #f09b7f;
	border-radius: 8rpx;
	padding: 26rpx 30rpx;
	text-align: center;
	&:active {
		background-color: #d87d63;
	}
}

.action-text {
	font-size: 32rpx;
	color: #fff;
}

/* 单卡片多项目模式样式 */
.info-card {
	background-color: #fff;
	border-radius: 8rpx;
	overflow: hidden;
	margin-bottom: 20rpx;
}

.info-item {
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

		.item-value {
			font-size: 32rpx;
			color: #666;
			margin-right: 12rpx;

			&.text-primary {
				color: #f09b7f;
				font-weight: 500;
			}
		}
	}
}

/* 表单输入框样式 */
.form-input {
	width: 100%;
	font-size: 32rpx;
	color: #333;
	text-align: right;
	border: none;
	outline: none;
	background: transparent;

	&::placeholder {
		color: #c0c0c0;
		font-size: 32rpx;
	}
}

/* 提交按钮样式 */
.submit-button {
	background-color: #f09b7f;
	border-radius: 8rpx;
	padding: 26rpx 30rpx;
	text-align: center;
	margin-top: 40rpx;

	&:active {
		background-color: #d87d63;
	}
}

.submit-text {
	font-size: 32rpx;
	color: #fff;
	font-weight: 500;
}
</style>