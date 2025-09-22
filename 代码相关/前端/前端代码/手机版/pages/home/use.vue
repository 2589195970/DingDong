<template>
	<view class="">
		<view class="item2" v-for="dict in productList" :key="dict.productId">
			<view style="display: flex;justify-content: space-between;">
				<view class="shu1">
					<u-icon name="account-fill" color="#5C98F9"></u-icon>
					账号：{{dict.agentName}}
					<br />
					手机号：{{dict.phone}}
					<br />
					注册时间：{{formatTimestamp(dict.createTime)}}
				</view>
			</view>
		</view>
	</view>

</template>

<script>
	import {
		selectChildAgentList
	} from "@/api/home/home.js";
	export default {
		data() {
			return {
				productList: [],
			}
		},
		mounted() {
			this.getdata();
		},
		methods: {
			// 时间戳转换
			formatTimestamp(timestamp) {
				const date = new Date(timestamp);
				const year = date.getFullYear();
				const month = ("0" + (date.getMonth() + 1)).slice(-2);
				const day = ("0" + date.getDate()).slice(-2);
				const hours = ("0" + date.getHours()).slice(-2);
				const minutes = ("0" + date.getMinutes()).slice(-2);
				const seconds = ("0" + date.getSeconds()).slice(-2);
				return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
			},
			getdata() {
				selectChildAgentList({}).then(res => {
					this.productList = res.data;
				})

			},
		}
	}
</script>

<style>
	.item2 {
		height: 150rpx;
		background-color: aliceblue;
		border-radius: 10px;
		margin: 10px;
		padding: 10px;
	}

	.shu1 {
		color: rgb(51, 51, 51);
		font-size: 14px;
		height: 20px;
		display: flex;
		font-weight: bold;
	}
</style>