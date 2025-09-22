<template>
	<u-form :model="queryParams" label-width="100px" class="demo-ruleForm" :inline="true">
		<div class="topss">
			<p class="pz">账户余额:{{pz.balance * 0.01}}元</p>
			<p class="pz">增值税发票税费:{{pz.withdrawRate}}%</p>
			<p class="pz">最低提现金额:{{pz.withdrawMinAmount}}元</p>
		</div>
		<div class="topss">
			<u-form-item label="提现模式" prop="resource">
				<!-- 			<u-radio-group v-model="queryParams.withdrawalType">
					<u-radio label="微信">微信</u-radio>
					<u-radio label="支付宝">支付宝</u-radio>
					<u-radio label="银行卡"></u-radio>
				</u-radio-group> -->

				<u-radio-group v-model="radiovalue" placement="row" @change="groupChange">
					<u-radio :customStyle="{marginBottom: '8px'}" v-for="(item, index) in radiolist" :key="index"
						:label="item.name" :name="item.name">
					</u-radio>
				</u-radio-group>
			</u-form-item>
			<br>
			<div v-if="queryParams.withdrawalType=='0'" class="wxskm">
				<u-form-item label="微信收款码" prop="resource">
					<!-- 					<u-upload class="avatar-uploader" :action="uploadUrl" :show-file-list="false"
						:on-success="handleAvatarSuccess" :before-upload="handlesuccess" :headers=headers>
						<img v-if="queryParams.wxUrl" :src="queryParams.wxUrl" class="avatar">
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</u-upload> -->

					<!-- <img v-if="queryParams.wxUrl" :src="queryParams.wxUrl" class="avatar"> -->
					<u-upload :fileList="fileList1" @afterRead="afterRead" @delete="deletePic" name="1" :headers="headers" multiple
						:maxCount="1"></u-upload>
				</u-form-item>

			</div>
			<div v-if="queryParams.withdrawalType=='1'">
				<u-form-item label="支付宝账号">
					<u-input v-model="queryParams.zfbAccount" placeholder="支付宝账号"></u-input>
				</u-form-item>
				<br>
				<u-form-item label="真实姓名">
					<u-input v-model="queryParams.zfbAccountName" placeholder="真实姓名"></u-input>
				</u-form-item>
			</div>
			<div v-if="queryParams.withdrawalType=='2'">
				<u-form-item label="银行开户行">
					<u-input v-model="queryParams.bankName" placeholder="银行开户行" style="width: 100%;"></u-input>
				</u-form-item>
				<br>
				<u-form-item label="真实姓名">
					<u-input v-model="queryParams.bankNumberName" placeholder="真实姓名"></u-input>
				</u-form-item>
				<br>
				<u-form-item label="银行卡号">
					<u-input v-model="queryParams.bankNumber" placeholder="银行卡号"></u-input>
				</u-form-item>
				<br>
				<u-form-item label="手机号">
					<u-input v-model="queryParams.bankNumberPhone" placeholder="手机号"></u-input>
				</u-form-item>
			</div>
			<u-form-item label="提现金额">
				<u-input v-model="queryParams.withdrawalAmount" placeholder="提现金额" @input="hlv"></u-input>
				<p v-if="querwithd.withdrawalRate">提现费率:{{querwithd.withdrawalRate}}%</p>
				<p v-if="querwithd.withdrawalRateAmount">提现手续费:{{querwithd.withdrawalRateAmount * 0.01}}元</p>
				<p v-if="querwithd.receivedAmount">实际到账:{{querwithd.receivedAmount * 0.01}}元</p>
			</u-form-item>
			<br>
			<u-form-item
				style="position: relative; bottom:-70px;padding: 10px 0;height: 50px;text-align: center;width: 100%;">
				<u-button type="primary" @click="submitForm(queryParams)">提交申请</u-button>
				<!-- <el-button @click="resetForm()">重置</el-button> -->
			</u-form-item>

		</div>
	</u-form>
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
		computed: {},
		methods: {
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
<style>
	.pz {
		color: red;
		font-weight: 700;
	}

	.topss {
		margin: 20px;
		padding: 10px;
		border: 1px solid #F2F2F2;
	}

	.avatar-uploader .el-upload {
		border: 1px dashed #d9d9d9;
		border-radius: 6px;
		cursor: pointer;
		position: relative;
		overflow: hidden;
	}

	.avatar-uploader .el-upload:hover {
		border-color: #409EFF;
	}

	.avatar-uploader-icon {
		font-size: 28px;
		color: #8c939d;
		width: 178px;
		height: 178px;
		line-height: 178px;
		text-align: center;
	}

	.wxskm .el-icon-plus:before {
		line-height: 178px;
	}

	.avatar {
		width: 178px;
		height: 178px;
		display: block;
	}

	.u-input--suffix {
		width: 202px;
	}
</style>