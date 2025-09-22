<template>
	<view style="margin: 10px;">
		<H3 style="display: flex;">
			<u-icon name="star-fill" color='#3374EB'></u-icon>
			我的店铺
			<span style="margin-left: 120px;color:red;" @click="commissionopen=true">分享店铺</span>
		</H3>
		
		<view class="">
			<view  style="border: 1px solid #3374EB; margin: 10px;padding: 10px; border-radius: 10rpx">
				<a :href="productList.shopUrl">{{productList.shopUrl}}</a>
			</view>
		</view>
		<H3 style="display: flex;"><u-icon name="star-fill" color='#3374EB'></u-icon>推广邀请</H3>
		<view class="">
			<view style="border: 1px solid #3374EB; margin: 10px;padding: 10px; border-radius: 10rpx">
				<a :href="productList.extendUrl">{{productList.extendUrl}}</a>
			</view>
		</view>
		<u-modal :show="commissionopen" @confirm="confirm" ref="uModal" @cancel="cancel" :showCancelButton='true'
			confirmText="保存图片" :asyncClose="true">
			<image :src="productList.shopQrcodeMap" alt="" style="height: 400px;" />
		</u-modal>
	</view>
</template>

<script>
	import {
		getAgentExtendUrlVO
	} from "@/api/order/order.js";
	export default {
		data() {
			return {
				commissionopen: false,
				productList: {
					extendUrl: "",
				},
			}
		},
		mounted() {
			this.getdata();
		},
		methods: {
			cancel() {
				this.commissionopen = false
			},
			confirm() {
				// 图片地址（需允许跨域访问）
				const url = this.productList.shopQrcodeMap;

				// 创建隐藏的 <a> 标签
				const link = document.createElement('a');
				link.href = url;
				link.download = 'img.jpg'; // 设置下载文件名
				document.body.appendChild(link);

				// 触发点击下载
				link.click();

				// 清理 DOM
				document.body.removeChild(link);
				this.commissionopen = false

			},

			getdata() {
				getAgentExtendUrlVO({}).then(res => {
					if (res.data) {
						this.productList = res.data;
					}

				})

			},
		}
	}
</script>

<style lang="scss" scoped>
	uni-page-body {
		height: 100%;
	}

	page {
		background-color: #f8f8f8;
	}

	.copyright {
		margin-top: 50rpx;
		text-align: center;
		line-height: 60rpx;
		color: #999;
	}

	.header-section {
		display: flex;
		height: 100%;
		padding: 30rpx 0 0;
		flex-direction: column;
		align-items: center;
	}
</style>