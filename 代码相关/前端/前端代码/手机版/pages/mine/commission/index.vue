<template>
	<view class="page-container">
		<app-navbar title="佣金设置"></app-navbar>
		<view>

		<u-radio-group v-model="queryParams.commissionConfigType" placement="row">
			佣金模式：<u-radio :customStyle="{marginBottom: '8px'}" v-for="(item, index) in radiolist1" :key="index"
				:label="item.name" :name="item.id" @change="radioChange(item)">
			</u-radio>
		</u-radio-group>
		<u--input placeholder="固定佣金" border="surround" shape="circle" v-model="queryParams.fixedCommission"
			style="margin: 5px;" v-if="queryParams.commissionConfigType==0"></u--input>
		<u--input placeholder="百分比佣金" border="surround" shape="circle" v-model="queryParams.scaleCommission"
			style="margin: 5px;" v-if="queryParams.commissionConfigType==1"></u--input>
		<u-button type="primary" shape="circle" text="提交设置" @click="submitForm(queryParams)"></u-button>
		</view>
	</view>

</template>

<script>
	import {

		selectAgentCommissionConfig,

		agentUpdateCommissionConfig,
	} from "@/api/product/product.js";
	export default {
		data() {
			return {
				// 基本案列数据
				value: '',
				radiolist1: [{
						name: '固定佣金',
						disabled: false,
						id: 0,
					},
					{
						name: '百分比佣金',
						disabled: false,
						id: 1,
					},

				],
				// u-radio-group的v-model绑定的值如果设置为某个radio的name，就会被默认选中
				queryParams: {
					commissionConfigType: 1,
				},
			};
		},
		async created() {
			try {
				// 获取配置数据
				const configRes = await selectAgentCommissionConfig({});
				if (configRes.data) {
					this.queryParams = configRes.data;
				}

			} catch (error) {
				console.error('Error occurred while fetching data:', error);
				// 在这里可以显示用户友好的错误提示
			}
		},
		methods: {
			submitForm(formName) {
				agentUpdateCommissionConfig(formName).then((res) => {
					this.$modal.msgSuccess("设置成功");
				})
			},
			radioChange(n) {
				console.log(n);
			}
		}
	};
</script>

<style>
</style>